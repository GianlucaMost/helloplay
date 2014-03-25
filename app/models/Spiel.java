package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.mindrot.jbcrypt.*;

import javax.persistence.*;

import models.*;
import play.Logger;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Akka;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import akka.actor.Props;
import akka.util.*;
import scala.concurrent.duration.Duration;

/**
 * Spiel entity managed by JPA
 */
@Entity 
@Table(name="spiel")
public class Spiel {  
	@Id													// id der tbl
	@Column(name="sid", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public int sid;
    
    @Column(name="toreheim")
    public byte toreheim;
    
    @Column(name="toregast")
    public byte toregast;
    
    @Constraints.Required
    @Column(name="ort")
    public String ort;
    
    @Constraints.Required
    @Column(name="beginn")
    public Timestamp beginn;
    
    @Constraints.Required
    @Column(name="ende")
    public Timestamp ende;
    
    @Constraints.Required
    @Column(name="checked")
    public byte checked=0;
    
    @OneToMany(mappedBy="spiel", targetEntity=Tipp.class)
    private Collection<Tipp> tipps;
    
    @ManyToOne()
    @JoinColumn(name="fk_midheim", referencedColumnName="mid")
    private Mannschaft mannschaft_heim;
    
    @ManyToOne()
    @JoinColumn(name="fk_midgast", referencedColumnName="mid")
    private Mannschaft mannschaft_gast;
    
    /**
     * Default constructor
     */
    public Spiel(){
    	
    }
    
    /**
     * Constructor
     * @param mannschaft_heim
     * @param mannschaft_gast
     * @param ort
     * @param beginn
     */
    public Spiel(Mannschaft mannschaft_heim, Mannschaft mannschaft_gast, String ort, Timestamp beginn){
    	this.mannschaft_heim = mannschaft_heim;
    	this.mannschaft_gast = mannschaft_gast;
    	this.ort = ort;
    	this.beginn=beginn;
//    	this.ende.setTime(beginn.getTime()+110*60*10);
    }
    
    /**
     * Constructor without game-result
     * @param mannschaft_heim
     * @param mannschaft_gast
     * @param ort
     * @param beginn
     * @param ende
     */
    public Spiel(Mannschaft mannschaft_heim, Mannschaft mannschaft_gast, String ort, Timestamp beginn, Timestamp ende){
    	this.mannschaft_heim = mannschaft_heim;
    	this.mannschaft_gast = mannschaft_gast;
    	this.ort = ort;
    	this.beginn=beginn;
    	this.ende=ende;
    }
    
    /**
     * get tipps from this spiel
     * @return
     */
    public Collection<Tipp> getTipps(){
    	return this.tipps;
    }
    
    /**
     * get mannschaft_heim from this user
     * @return
     */
    public Mannschaft getMannschaftHeim(){
    	return this.mannschaft_heim;
    }
    
    /**
     * set mannschaft_heim from this user
     * @param m
     */
    public void setMannschaftHeim(Mannschaft m){
    	this.mannschaft_heim=m;
    }
    
    /**
     * get mannschaft_gast from this user
     * @return
     */
    public Mannschaft getMannschaftGast(){
    	return this.mannschaft_gast;
    }
    
    /**
     * set mannschaft_gast from this user
     * @param m
     */
    public void setMannschaftGast(Mannschaft m){
    	this.mannschaft_gast=m;
    }
    
    /**
     * persist this
     */
    @Transactional
    public void persist() {
		JPA.em().persist(this);
    }
    
    /**
     * Find a Spiel by id.
     */
    @Transactional(readOnly=true)
    public static Spiel findById(int sid) {
    	return JPA.em().find(Spiel.class, sid);
    }
    
    @Transactional
    public static Spiel findGroupGame(Mannschaft mh, Mannschaft mg) {
    	Integer midh=mh.mid;
    	Integer midg=mg.mid;
    	
		Query query = JPA.em().createQuery("SELECT s FROM Spiel s WHERE s.fk_midheim = :pMidh AND s.fk_midgast = :pMidg");
    	query.setParameter("pMidh", midh);
    	query.setParameter("pMidg", midg);
    	return (Spiel) query.getSingleResult();
    }
    
    @Transactional(readOnly=true)
    public static Collection<Spiel> findAll() {
        Query query = JPA.em().createQuery("SELECT s FROM Spiel s ORDER BY s.beginn");
        return (Collection<Spiel>) query.getResultList();
    }
    
    /**
     * return a collection of all games of a team
     * @param mid
     * @return
     */
    @Transactional(readOnly=true)
    public static Collection<Spiel> gamesOfTeam(int mid){
        Query query = JPA.em().createQuery("SELECT s FROM Spiel s WHERE s.fk_midheim=:pMid OR s.fk_midgast=:pMid");
        query.setParameter("pMid", mid);
        Collection<Spiel> col = query.getResultList();
        return col;	
    }
    
    /**
     * non static game-result-setting
     * @param toreheim
     * @param toregast
     */
    @Transactional
    public void setErgebnis(byte th, byte tg){
    	byte thp = this.toreheim;
    	byte tgp = this.toregast;
    	Mannschaft mh = this.getMannschaftHeim();
		Mannschaft mg = this.getMannschaftGast();
    	if (thp!=th || tgp!=tg){
    		this.toreheim=th;
        	this.toregast=tg;
        	JPA.em().persist(this);
    	}
    	if (this.gameOver()){
    		// Punkte an User verteilen
    		//tipps von diesem spiel holen
    		Collection<Tipp> tipps = this.tipps;
    		//jeden tipp durchlaufen
    		for (Tipp t: tipps){
    			//gucken ob dieser tipp.checked=0 ist
    			if(t.checked==0){
    				//punkte vergeben
    				User user = t.getUser();
    				int p=user.punkte;
    					//wenn genau richtiges ergebnis dann 3punkte
    					if(th==t.toreheim && tg==t.toregast){
    						p=p+3;
    					//wenn richtige tordifferenz dann 2punkte
    					}else if(th-tg==t.toreheim-t.toregast){
    						p=p+2;
    					//wenn richtige mannschaft dann 1punkt
    					}else if(th>tg && t.toreheim>t.toregast){
    						p=p+1;
    					}else if(tg>th && t.toregast>t.toreheim){
    						p=p+1;
    					}
    				user.punkte=p;
    				user.persist();
    				//diesen tipp.checked=1 setzen
    				t.checked=1;
    				t.persist();
    			}
    		}
    		
    		if(this.checked==0){
    			//Punkte an Mannschaften verteilen
	    		if (th>tg){
	    			//Bei Sieg drei Punkte fuer Gewinner
	    			mh.punkte=mh.punkte+3;
	    			mh.siege++;
	    			mg.niederlagen++;
	    		}else if (th<tg){
	    			//Bei Sieg 3 Punkte fuer Gewinner
	    			mg.punkte=mg.punkte+3;
	    			mg.siege++;
	    			mh.niederlagen++;
	    		}else if (th==tg){
	    			//Bei Unentschieden einen Punkt fuer beide
	    			mh.punkte=mh.punkte+1;
	    			mg.punkte=mg.punkte+1;
	    			mh.unentschieden++;
	    			mg.unentschieden++;
	    		}
	    		//Tore und Gegentore setzen
	    		mh.tore=mh.tore+th;
	    		mg.tore=mg.tore+tg;
	    		mh.gegentore=mh.gegentore+tg;
	    		mg.gegentore=mg.gegentore+th;
	    		//Dieses Spiel abhacken, so dass keine Punkte mehr hierfuer vergeben werden
	    		this.checked=1;
	    		this.persist();
	    		mh.persist();
	    		mg.persist();
    		}
    	}
    }
    
    /**
     * return true if this games end-timestamp is before the current system-time
     * @return
     */
    public boolean gameOver(){
    	return this.ende.before(new Timestamp(System.currentTimeMillis()));
    }
    
    /**
     * return true if a game is currently running
     * @return
     */
    public boolean gameRunning(){
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	return this.beginn.before(now) && this.ende.after(now);
    }
    
    public boolean isGg(){
    	return this.getMannschaftHeim().gruppe.equals("A") || this.getMannschaftHeim().gruppe.equals("B") || this.getMannschaftHeim().gruppe.equals("C") || this.getMannschaftHeim().gruppe.equals("D") || this.getMannschaftHeim().gruppe.equals("E") || this.getMannschaftHeim().gruppe.equals("F") || this.getMannschaftHeim().gruppe.equals("G") || this.getMannschaftHeim().gruppe.equals("H");
    }
    
    public boolean checkAfTippReady(){
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Timestamp rdy = Timestamp.valueOf("2014-06-26 22:00:00.0");
    	return this.getMannschaftHeim().gruppe.equals("AF") && now.after(rdy);
    }
    
    public boolean checkVfTippReady(){
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Timestamp rdy = Timestamp.valueOf("2014-07-02 00:00:00.0");
    	return this.getMannschaftHeim().gruppe.equals("VF") && now.after(rdy);
    }
    
    public boolean checkHfTippReady(){
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Timestamp rdy = Timestamp.valueOf("2014-07-06 00:00:00.0");
    	return this.getMannschaftHeim().gruppe.equals("HF") && now.after(rdy);
    }
    
    public boolean checkFTippReady(){
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Timestamp rdy = Timestamp.valueOf("2014-07-10 00:00:00.0");
    	if(this.getMannschaftHeim().gruppe.equals("FI") || this.getMannschaftHeim().gruppe.equals("SP3")){
    		return now.after(rdy);
    	}else{
    		return false;
    	}
    }
    
    public static void setResultWithRss(){
    	
		try {
			
			byte th=0;
			byte tg=0;
			Mannschaft mh=null;
			Mannschaft mg=null;
			String mhRename="";
			String mgRename="";
			
//			URL feedSource = new URL("http://rss.kicker.de/live/championsleaguequalifikation");
			URL feedSource = new URL("http://rss.kicker.de/live/wm");
			SyndFeedInput input = new SyndFeedInput();
	    	SyndFeed feed = input.build(new XmlReader(feedSource));
	    	
	    	List<SyndEntry> entries = feed.getEntries();
	    	
	    	for(SyndEntry se: entries){
	    		String title=se.getTitle();
	    		Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9])$");
//	    		Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9]) (.*)$");
	    		Matcher matcher = pattern.matcher(title);
	    		if(matcher.matches()){
	    			Logger.info("RSSfeed matches");
	    			//umbenennen nicht identischer mannschafts-bezeichnungen
	    			mhRename=matcher.group(1);
	    			mgRename=matcher.group(2);
	    			switch(mhRename){
	    			case "Elfenbeinküste":
	    				mhRename="Elfenbeinkueste";
	    				break;
	    			case "Bosnien-Herzegowina":
	    				mhRename="Bosnien-H.";
	    				break;
	    			case "Südkorea":
	    				mhRename="Korea Republik";
	    				break;
	    			}
	    			switch(mgRename){
	    			case "Elfenbeinküste":
	    				mgRename="Elfenbeinkueste";
	    				break;
	    			case "Bosnien-Herzegowina":
	    				mgRename="Bosnien-H.";
	    				break;
	    			case "Südkorea":
	    				mgRename="Korea Republik";
	    				break;
	    			}
	    			mh=Mannschaft.findByName(mhRename);
	    			mg=Mannschaft.findByName(mgRename);
	    			th=Byte.parseByte((matcher.group(3)));
	    			tg=Byte.parseByte((matcher.group(4)));
	    			Logger.info("Found RSSfeed that matches!");
	    			Logger.info("mh = " + mh + "(" + mhRename + ")");
	    			Logger.info("mg = " + mg + "(" + mgRename + ")");
	    			Logger.info("th = " + th);
	    			Logger.info("tg = " + tg);
	    			Spiel gg = Spiel.findGroupGame(mh, mg);
	    	    	gg.setErgebnis(th, tg);
	    		}else{
//	    			Logger.warn("Found RSSfeed, that doesnt match!");
//	    			Logger.info("Title: " + title);
	    		}
	    	}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
