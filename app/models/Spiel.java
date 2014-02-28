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
@SequenceGenerator(name = "spiel_seq", sequenceName = "spiel_seq")
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
    public SimpleDateFormat beginn = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    @Constraints.Required
    public SimpleDateFormat ende = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    /**
     * Konstruktor without game-result
     * @param fk_midheim
     * @param fk_midgast
     * @param ort
     * @param beginn
     * @param ende
     */
    public Spiel(int fk_midheim, int fk_midgast, String ort, SimpleDateFormat beginn, SimpleDateFormat ende)
    {
    	this.fk_midheim = fk_midheim;
    	this.fk_midgast = fk_midgast;
    	this.ort = ort;
    	this.beginn = beginn;
    	this.ende = ende;
    }
    
    /**
     * Find a Spiel by id.
     */
    @Transactional(readOnly=true)
    public static Spiel findById(int sid) {
    	return JPA.em().find(Spiel.class, sid);
    }
}
