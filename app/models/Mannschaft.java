package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    public Long mid;
    
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
    public static Mannschaft findById(long mid) {
    	return JPA.em().find(Mannschaft.class, mid);
    }
    
    /**
     * Holt alle Mannschaften aus der db
     * @return
     */
    @Transactional(readOnly=true)
    public static Collection<Mannschaft> getColl() {
        Query query = JPA.em().createQuery("SELECT m FROM Mannschaft m");
        return (Collection<Mannschaft>) query.getResultList();
    }
    
    /**
     * Holt alle Mannschaften aus der db
     * @return
     */
    @Transactional(readOnly=true)
    public static  Map<String, List<Mannschaft>> findAll() {
        Query query = JPA.em().createQuery("SELECT m FROM Mannschaft m");
        Collection<Mannschaft> col = (Collection<Mannschaft>) query.getResultList();
        Map<String, List<Mannschaft>> teamMap = new HashMap<String, List<Mannschaft>>();
        
        for (Mannschaft team : col) {
        	if (!teamMap.containsKey(team.gruppe)) {
        		List<Mannschaft> teamList = new ArrayList<Mannschaft>();
        		teamList.add(team);
        		teamMap.put(team.gruppe, teamList);
        	} else {
        		teamMap.get(team.gruppe).add(team);
        	}
        }
        
        Map<String, List<Mannschaft>> treeMap = new TreeMap<String, List<Mannschaft>>(teamMap);
        
        return treeMap;
    }
}
