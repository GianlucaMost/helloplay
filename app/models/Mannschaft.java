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

import dao.MannschaftDao;
import dao.MannschaftDaoImpl;

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
	@Column(name="mid")
    public int mid;
    
    @Constraints.Required
    @Column(name="bezeichnung")
    public String bezeichnung;
    
    @Constraints.Required
    @Column(name="gruppe")
    public String gruppe;
    
    @Constraints.Required
    @Column(name="anzahlspiele")
    public int anzahlspiele=0;
    
    @Constraints.Required
    @Column(name="siege")
    public byte siege=0;
    
    @Constraints.Required
    @Column(name="unentschieden")
    public byte unentschieden=0;
    
    @Constraints.Required
    @Column(name="niederlagen")
    public byte niederlagen=0;
    
    @Constraints.Required
    @Column(name="tore")
    public int tore=0;
    
    @Constraints.Required
    @Column(name="gegentore")
    public int gegentore=0;
    
    @Constraints.Required
    @Column(name="punkte")
    public int punkte=0;
    
    @Column(name="status")
    public String status;
    
    @OneToMany(mappedBy="mannschaft_heim")
    private Collection<Spiel> heimSpiele;
    
    @OneToMany(mappedBy="mannschaft_gast")
    private Collection<Spiel> auswaertsSpiele;
    
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
     * @param anzahlspiele
     * @param siege
     * @param unentschieden
     * @param niederlagen
     * @param tore
     * @param gegentore
     * @param punkte
     */
    public Mannschaft(String bezeichnung, String gruppe, int anzahlspiele, byte siege, byte unentschieden, byte niederlagen, int tore, int gegentore, int punkte)
    {
    	this.bezeichnung=bezeichnung;
    	this.gruppe=gruppe;
    	this.anzahlspiele=anzahlspiele;
    	this.siege=siege;
    	this.unentschieden=unentschieden;
    	this.niederlagen=niederlagen;
    	this.tore=tore;
    	this.gegentore=gegentore;
    	this.punkte=punkte;
    }
    
    /**
     * get games from this team
     * @return
     */
    public Collection<Spiel> getSpiele(){
    	List<Spiel> games = new ArrayList<Spiel>();
    	games.addAll(this.heimSpiele);
    	games.addAll(this.auswaertsSpiele);
    	return games;
    }
    
    /**
     * get all heimspiele from this team
     * @return
     */
    public Collection<Spiel> getHeimSpiele(){
    	return this.heimSpiele;
    }
}
