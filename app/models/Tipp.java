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
    
    @Constraints.Required
    @Column(name="fk_uid")
    public int fk_uid;
    
    @Constraints.Required
    @Column(name="fk_sid")
    public int fk_sid;
    
    @Constraints.Required
    @Column(name="toreheim")
    public byte toreheim;
    
    @Constraints.Required
    @Column(name="toregast")
    public byte toregast;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="sid", referencedColumnName="sid")
    private Spiel spiel;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="uid", referencedColumnName="uid")
    private User user;

    /**
     * Default constructor
     */
    public Tipp()
    {
    	
    }
    

    /**
     * Konstruktor
     * @param fk_uid
     * @param fk_sid
     * @param toreheim
     * @param toregast
     */
    public Tipp(int fk_uid, int fk_sid, byte toreheim, byte toregast)
    {
    	this.fk_uid=fk_uid;
    	this.fk_sid=fk_sid;
    	this.toreheim=toreheim;
    	this.toregast=toregast;
    }
    
    /**
     * Find a Tipp by id.
     */
    @Transactional(readOnly=true)
    public static Tipp findById(int tid) {
    	return JPA.em().find(Tipp.class, tid);
    }
    
    /**
     * Delete this tipp.
     */
    @Transactional
    public void delete(){
        JPA.em().remove(this);
    }
    
}
