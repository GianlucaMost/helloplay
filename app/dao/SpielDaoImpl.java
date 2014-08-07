package dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import models.Mannschaft;
import models.Spiel;

import org.hibernate.event.SaveOrUpdateEvent;

import play.db.jpa.JPA;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

import play.Logger;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class SpielDaoImpl implements SpielDao {
	
	EntityManager em = JPA.em();
	
	/**
	 * persist a new object or update/merge an old one.
	 */
	@Override
	public void persistOrMerge(Spiel s) {
		if(this.findAll().contains(s)){
			em.merge(s);
		}else{
			em.persist(s);
		}
	}
	
	/**
	 * Find a game by his id
	 */
	@Override
	public Spiel findById(int sid) {
		return em.find(Spiel.class, sid);
	}
	
	
	/**
	 * return  Collection of all games
	 */
	@Override
	public Collection<Spiel> findAll() {
		Query query = em.createQuery("SELECT s FROM Spiel s ORDER BY s.beginn");
        return (Collection<Spiel>) query.getResultList();
    }
	
	/**
	 * return the game with the given designation, if not found return null
	 */
	@Override
	public Spiel findByBezeichnung(String b) {
		for (Spiel s: findAll()){
    		if(s.getBezeichnung().equals(b)){
    			return s;
    		}
    	}
		return null;
	}

	/**
	 * return the game where team para1(mh) play against team para2(mg)
	 */
	@Override
	public Spiel findGroupGame(Mannschaft mh, Mannschaft mg) {
		Query query = em.createQuery("SELECT s FROM Spiel s WHERE s.mannschaft_heim = :pMh AND s.mannschaft_gast = :pMg");
    	query.setParameter("pMh", mh);
    	query.setParameter("pMg", mg);
    	return (Spiel) query.getSingleResult();
	}
	
	/**
	 * alternative version of findGroupGame()
	 */
	@Override
	public Spiel findGame(Mannschaft mh, Mannschaft mg) {
		for (Spiel s: findAll()){
    		if(s.getMannschaftHeim().mid==mh.mid && s.getMannschaftGast().mid==mg.mid){
    			return s;
    		}
    	}
    	return null;
	}
	
	/**
	 * return a collection of all games of a team
	 */
	@Override
	public Collection<Spiel> gamesOfTeam(int mid) {
		Query query = em.createQuery("SELECT s FROM Spiel s WHERE s.fk_midheim=:pMid OR s.fk_midgast=:pMid");
        query.setParameter("pMid", mid);
        Collection<Spiel> col = query.getResultList();
        return col;	
	}
	
	/**
	 * set the result of the given game(para1) to given goals(para2/3)
	 */
	@Override
	public void setErgebnis(Spiel s, byte th, byte tg) throws Throwable {
		byte thp = s.toreheim;
    	byte tgp = s.toregast;
    	Mannschaft mh = s.getMannschaftHeim();
		Mannschaft mg = s.getMannschaftGast();
		Collection<Spiel> spiele = findAll();
		
    	if (thp!=th || tgp!=tg){
    		s.toreheim = th;
        	s.toregast = tg;
//        	em.persist(s);
//        	s = em.merge(s);
        	persistOrMerge(s);
    	}
    	if (s.gameOver()){
    		//Punkte an Benutzer verteilen
    		Spiel.handOutUserPoints(s.getTipps(), th, tg);
    		
    		if(s.checked==0){
    			Spiel.handOutTeamPoints(s, mh, mg, th, tg);
	    		
	    		//wenn das hier das letzte gruppenspiel war, setze AchtelFinalSpiele.
	    		if(s.getBezeichnung().equals("gg48")){
	    			Spiel.setAF();
	    		}
	    		//wenn das hier das letzte AchtelFinalSpiel war, setze viertelFinale
	    		if(s.getBezeichnung().equals("af8")){
	    			//setze vf
	    			Spiel.setVF(spiele);
	    		}
	    		//wenn das hier das letzte VF Spiel war setze HF
	    		if(s.getBezeichnung().equals("vf4")){
					//setze hf
	    			Spiel.setHF(spiele);
				}
	    		//wenn das hier das letzte HF Spiel war setze Finale und SP3
	    		if(s.getBezeichnung().equals("hf2")){
	    			//setze fi
	    			Spiel.setFI(spiele);
				}
    		}
    	}
	}

}
