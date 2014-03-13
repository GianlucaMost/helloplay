package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.mindrot.jbcrypt.*;

import javax.persistence.*;

import models.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

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
    
    public Spiel(Mannschaft mannschaft_heim, Mannschaft mannschaft_gast, String ort, Timestamp beginn){
    	this.mannschaft_heim = mannschaft_heim;
    	this.mannschaft_gast = mannschaft_gast;
    	this.ort = ort;
    	this.beginn=beginn;
    	this.ende.setTime(beginn.getTime()+110*60*10);
    }
    
    /**
     * Constructor without game-result
     * @param fk_midheim
     * @param fk_midgast
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
//    	this.ende.after(new Timestamp(System.currentTimeMillis()));
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
     * Find a Spiel by id.
     */
    @Transactional(readOnly=true)
    public static Spiel findById(int sid) {
    	return JPA.em().find(Spiel.class, sid);
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
    	this.toreheim=th;
    	this.toregast=tg;
    	JPA.em().persist(this);
    }
    
    /**
     * return true if this games end-timestamp is before the current system-time
     * @return
     */
    public boolean gameOver(){
    	return this.ende.before(new Timestamp(System.currentTimeMillis()));
    }
    
    public boolean gameRunning(){
    	if (!gameOver()){
    		return this.beginn.before(new Timestamp(System.currentTimeMillis()));
    	}else{
    		return false;
    	}
    }
    
    public void ladeRSS() throws IllegalArgumentException, FeedException, IOException{
//      URL feedSource = new URL("http://some.rss.feed");
//      SyndFeedInput input = new SyndFeedInput();
//      SyndFeed feed = input.build(new XmlReader(feedSource));
//      
//      
//      List<SyndEntry> entries = feed.getEntries();
//      Iterator itEntries = entries.iterator();
//      
//      for(SyndEntry se: entries){
//      	String autor=se.getAuthor();
//      }
    	
    	URL feedSource = new URL("http://some.rss.feed");
    	SyndFeedInput input = new SyndFeedInput();
    	SyndFeed feed = input.build(new XmlReader(feedSource));
    	
    	List<SyndEntry> entries = feed.getEntries();
    	
    	for(SyndEntry se: entries){
    		String titel=se.getTitle();
    	}
    }
    
    public List<String> holeRSS() throws IllegalArgumentException, FeedException, IOException{
//      URL feedSource = new URL("http://some.rss.feed");
//      SyndFeedInput input = new SyndFeedInput();
//      SyndFeed feed = input.build(new XmlReader(feedSource));
//      
//      
//      List<SyndEntry> entries = feed.getEntries();
//      Iterator itEntries = entries.iterator();
//      
//      for(SyndEntry se: entries){
//      	String autor=se.getAuthor();
//      }
    	
    	URL feedSource = new URL("http://some.rss.feed");
    	SyndFeedInput input = new SyndFeedInput();
    	SyndFeed feed = input.build(new XmlReader(feedSource));
    	
    	List<SyndEntry> entries = feed.getEntries();
    	List<String> titles = new ArrayList<String>();
    	
    	for(SyndEntry se: entries){
    		String title=se.getTitle();
    		titles.add(title);
    	}
    	return titles;
    }
}
