package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     */
    public Trunde(String b)
    {
    	this.bezeichnung=b;
    }
    
    /**
     * get all user included in this TippRunde
     * @return
     */
    public Collection<User> getMember(){
    	return this.member;
    }
    
    public User getTrAdmin(){
    	return this.tradmin;
    }
    
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
    	
//    	String sqlQ = "SELECT t.* FROM tipp AS t "
//    	+ "INNER JOIN user AS u ON t.fk_uid=u.uid "
//    	+ "INNER JOIN trunde AS tr ON u.fk_trid=tr.trid "
//    	+ "WHERE tr.trid = ?";
//    	Query q = JPA.em().createNativeQuery(sqlQ);
//		q.setParameter(1, this.trid);
//    	return (Collection<Tipp>) q.getResultList();
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
    	
//    	String sqlQ = "select tipp.* from tipp "
//    	+ "inner join user on tipp.fk_uid=user.uid "
//    	+ "inner join trunde on user.fk_trid=trunde.trid "
//    	+ "where trunde.trid = ? "
//    	+ "AND tipp.fk_sid = ?";
//    	Query q = JPA.em().createNativeQuery(sqlQ);
//		q.setParameter(1, this.trid);
//		q.setParameter(2, s.sid);
//    	return (Collection<Tipp>) q.getResultList();
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
}
