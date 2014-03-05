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
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public int sid;
    
    @Constraints.Required
    public int fk_midheim;
    
    @Constraints.Required
    public int fk_midgast;
    
    public byte toreheim;
    
    public byte toregast;
    
    @Constraints.Required
    public String ort;
    
    @Constraints.Required
    public Timestamp beginn = new Timestamp(System.currentTimeMillis());
    
    @Constraints.Required
    public Timestamp ende = new Timestamp(System.currentTimeMillis());
    
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
    public Spiel(int fk_midheim, int fk_midgast, String ort){
    	this.fk_midheim = fk_midheim;
    	this.fk_midgast = fk_midgast;
    	this.ort = ort;
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
        Query query = JPA.em().createQuery("SELECT s FROM Spiel s WHERE s.fk_midheim = :pMid");
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
