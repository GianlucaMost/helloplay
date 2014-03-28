package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.*;

import javax.persistence.*;

import models.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

/**
 * Trunde entity managed by JPA
 */
@Entity 
@Table(name="trunde")
public class Trunde {  
	@Id													// id der tbl
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
	@Column(name="trid")
    public int trid;
    
    @Constraints.Required
    @Column(name="bezeichnung")
    public String bezeichnung;
    
    @ManyToMany(mappedBy="trunden")
    private Collection<User> member;
    
    @ManyToOne
    @JoinColumn(name="fk_admin", referencedColumnName="uid")
    private User tradmin;
    
    /**
     * Default constructor
     */
    public Trunde()
    {
    	
    }
    
    /**
     * Konstruktor
     * @param b
     * @param admin
     */
    public Trunde(String b, User admin)
    {
    	this.bezeichnung=b;
    	this.tradmin=admin;
    }
    
    /**
     * get all user included in this TippRunde
     * @return
     */
    public Collection<User> getMember(){
    	return this.member;
    }
    
    /**
     * get the admin of this tr
     * @return
     */
    public User getTrAdmin(){
    	return this.tradmin;
    }
    
    /**
     * Set given user u to the admin of this tr
     * @param u
     */
    public void setTrAdmin(User u){
    	this.tradmin=u;
    }
    
    /**
     * Find a 'Trunde' by id.
     */
    @Transactional(readOnly=true)
    public static Trunde findById(int trid) {
    	return JPA.em().find(Trunde.class, trid);
    }
    
    /**
     * persist the trunde
     */
    @Transactional
    public void persist() {
		JPA.em().persist(this);
    }
    
    /**
     * Get all trunden.
     */
    @Transactional(readOnly=true)
    public static Collection<Trunde> findAll(){
        Query query = JPA.em().createQuery("SELECT tr FROM trunde tr");
        return (Collection<Trunde>) query.getResultList();
    }
    
    /**
     * Delete this trunde.
     */
    @Transactional
    public void delete(){
        JPA.em().remove(this);
    }
    
    /**
     * find all Tipps from this trunde
     * @return
     */
    @Transactional
    public Collection<Tipp> findTipps() {
    	
    	Collection<Tipp> tipps = new ArrayList<Tipp>();
    	for(User m: this.getMember()){
    		for (Tipp t: m.getTipps()){
    			tipps.add(t);
    		}
    	}
    	return tipps;
    }
    
    /**
     * find all tipps from this trunde for the given spiel
     * @param s
     * @return
     */
    @Transactional
    public Collection<Tipp> findTippsSpiel(Spiel s) {
    	Collection<Tipp> tipps = new ArrayList<Tipp>();
    	for(User m: this.getMember()){
    		for (Tipp t: m.getTipps()){
    			if(t.getSpiel().equals(s)){
    				tipps.add(t);
    			}
    		}
    	}
    	return tipps;
    }
    
    /**
     * find all tipps from this trunde for the given spiel with the tipp from the current user at first place
     * @param s
     * @param cU
     * @return
     */
    @Transactional
    public Collection<Tipp> findTippsSpielSorted(Spiel s, User cU) {
    	List<Tipp> tipps = new ArrayList<Tipp>();
    	
    	for(User m: this.getMember()){
    		for (Tipp t: m.getTipps()){
    			if(t.getSpiel().equals(s)){
    				if(t.getUser().equals(cU)){
    					tipps.add(t);
    				}
    			}
    		}
    	}
    	for(User m: this.getMember()){
    		for (Tipp t: m.getTipps()){
    			if(t.getSpiel().equals(s)){
    				if(!t.getUser().equals(cU)){
    					tipps.add(t);
    				}
    			}
    		}
    	}
    	
    	return tipps;
    }
    
    /**
     * return the sum of all points from every user in this tipprunde
     * @return
     */
    @Transactional
    public int punkte() {
    	int p=0;
    	for(User u: this.getMember()){
    		p=p+u.punkte;
    	}
    	return p;
    }
    
    @Transactional
    public Collection<User> findSortedMember(){
    	String sqlQuery = "SELECT uid, name, pw, punkte, admin FROM user AS u INNER JOIN user_trunde AS utr ON utr.fk_uid=u.uid WHERE fk_trid=? ORDER BY punkte DESC, name ASC";
    	Query q = JPA.em().createNativeQuery(sqlQuery, User.class);
    	q.setParameter(1, this.trid);
    	return q.getResultList();
    }
}
