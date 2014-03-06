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
    
    @OneToMany(mappedBy="trunde", targetEntity=User.class)
    private Collection<User> users;
    
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
    public Collection<User> getUsers(){
    	return this.users;
    }
    
    /**
     * Find a 'Trunde' by id.
     */
    @Transactional(readOnly=true)
    public static Trunde findById(int trid) {
    	return JPA.em().find(Trunde.class, trid);
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
    
}
