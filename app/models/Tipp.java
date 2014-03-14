package models;

import java.util.Collection;
import java.util.List;

import org.mindrot.jbcrypt.*;

import javax.persistence.*;

import models.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

/**
 * Tipp entity managed by JPA
 */
@Entity 
@Table(name="tipp")
public class Tipp {  
	@Id													// id der tbl
	@Column(name="tid", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public int tid;
    
    @Column(name="toreheim")
    public byte toreheim;
    
    @Column(name="toregast")
    public byte toregast;
    
    @Column(name="checked")
    public byte checked=0;
    
    @ManyToOne
    @JoinColumn(name="fk_sid", referencedColumnName="sid")
    private Spiel spiel;
    
    @ManyToOne
    @JoinColumn(name="fk_uid", referencedColumnName="uid")
    private User user;

    /**
     * Default constructor
     */
    public Tipp()
    {
    	
    }
    

    /**
     * Konstruktor
     * @param currentUser
     * @param game
     * @param toreheim
     * @param toregast
     */
    public Tipp(User currentUser, Spiel game, byte toreheim, byte toregast)
    {
    	this.user=currentUser;
    	this.spiel=game;
    	this.toreheim=toreheim;
    	this.toregast=toregast;
    }
    
    /**
     * get Spiel from this Tipp
     * @return
     */
    public Spiel getSpiel(){
    	return this.spiel;
    }
    
    /**
     * set Spiel from this Tipp
     * @param s
     */
    public void setSpiel(Spiel s){
    	this.spiel=s;
    }
    
    /**
     * get User from this Tipp
     * @return
     */
    public User getUser(){
    	return this.user;
    }
    
    /**
     * set User from this Tipp
     * @param u
     */
    public void setUser(User u){
    	this.user=u;
    }
    
    /**
     * Find a Tipp by id.
     */
    @Transactional(readOnly=true)
    public static Tipp findById(int tid) {
    	return JPA.em().find(Tipp.class, tid);
    }
    
    @Transactional(readOnly=true)
    public static Collection<Tipp> findAll() {
        Query query = JPA.em().createQuery("SELECT t FROM Tipp t");
        return (Collection<Tipp>) query.getResultList();
    }
    
    @Transactional
    public void add() {
		JPA.em().persist(this);
    }
    
    @Transactional
    public void update(byte th, byte tg) {
    	this.toreheim=th;
    	this.toregast=tg;
		JPA.em().persist(this);
    }
    
    @Transactional
    public void persist() {
		JPA.em().persist(this);
    }
    
    /**
     * Delete this tipp.
     */
    @Transactional
    public void delete(){
        JPA.em().remove(this);
    }
    
}
