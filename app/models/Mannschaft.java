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
 * Mannschaft entity managed by JPA
 */
@Entity 
@Table(name="mannschaft")
public class Mannschaft {  
	@Id													// id der tbl
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public int mid;
    
    @Constraints.Required
    public String bezeichnung;
    
    @Constraints.Required
    public String gruppe;
    
    @Constraints.Required
    public int spiele=0;
    
    @Constraints.Required
    public byte siege=0;
    
    @Constraints.Required
    public byte unentschieden=0;
    
    @Constraints.Required
    public byte niederlagen=0;
    
    @Constraints.Required
    public int tore=0;
    
    @Constraints.Required
    public int gegentore=0;
    
    @Constraints.Required
    public int punkte=0;
    
    /**
     * Default constructor
     */
    public Mannschaft()
    {
    	
    }
    
    /**
     * Konstruktor
     * @param bezeichnung
     * @param gruppe
     * @param spiele
     * @param siege
     * @param unentschieden
     * @param niederlagen
     * @param tore
     * @param gegentore
     * @param punkte
     */
    public Mannschaft(String bezeichnung, String gruppe, int spiele, byte siege, byte unentschieden, byte niederlagen, int tore, int gegentore, int punkte)
    {
    	this.bezeichnung=bezeichnung;
    	this.gruppe=gruppe;
    	this.spiele=spiele;
    	this.siege=siege;
    	this.unentschieden=unentschieden;
    	this.niederlagen=niederlagen;
    	this.tore=tore;
    	this.gegentore=gegentore;
    	this.punkte=punkte;
    }
    
    /**
     * Find a Mannschaft by id.
     * @param mid
     * @return
     */
    @Transactional(readOnly=true)
    public static Mannschaft findById(int mid) {
    	return JPA.em().find(Mannschaft.class, mid);
    }
    
    /**
     * holt alle Mannschaften aus der db
     * @return
     */
    @Transactional(readOnly=true)
    public static Collection<Mannschaft> findAll(){
        Query query = JPA.em().createQuery("SELECT m FROM Mannschaft m");
        return (Collection<Mannschaft>) query.getResultList();
    }
}
