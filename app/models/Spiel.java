package models;

import java.util.Collection;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.mindrot.jbcrypt.*;

import javax.persistence.*;

import models.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

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
    
    @Constraints.Required
    @Column(name="fk_midheim")
    public int fk_midheim;
    
    @Constraints.Required
    @Column(name="fk_midgast")
    public int fk_midgast;
    
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
    
    /**
     * Default constructor
     */
    public Spiel(){
    	
    }
    
    /**
     * Constructor without game-result
     * @param fk_midheim
     * @param fk_midgast
     * @param ort
     * @param beginn
     * @param ende
     */
    public Spiel(int fk_midheim, int fk_midgast, String ort, Timestamp beginn, Timestamp ende){
    	this.fk_midheim = fk_midheim;
    	this.fk_midgast = fk_midgast;
    	this.ort = ort;
    	this.beginn=beginn;
    	this.ende=ende;
    }
    
    /**
     * Find a Soiel by id.
     */
    @Transactional(readOnly=true)
    public static Spiel findById(int sid) {
    	return JPA.em().find(Spiel.class, sid);
    }
    
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
}
