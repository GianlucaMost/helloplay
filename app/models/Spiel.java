package models;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints;
import services.SpielService;


/**
 * Spiel entity managed by JPA
 */
@Entity 
@Table(name="spiel")
public class Spiel{  
	
	@Id													// id der tbl
	@Column(name="sid", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public int sid;
    
    @Column(name="toreheim")
    public byte toreheim;
    
    @Column(name="toregast")
    public byte toregast;
    
    @Constraints.Required
    @Column(name="ort")
    public String ort;
    
    @Constraints.Required
    @Column(name="beginn")
//  public Timestamp beginn;
    public Calendar beginn;
    
    @Constraints.Required
    @Column(name="ende")
// 	public Timestamp ende;
    public Calendar ende;
    
    @Constraints.Required
    @Column(name="checked")
    public byte checked=0;
    
    @Column(name="bezeichnung")
    private String bezeichnung;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="spiel", targetEntity=Tipp.class)
    private Collection<Tipp> tipps;
    
    @ManyToOne()
    @JoinColumn(name="fk_midheim", referencedColumnName="mid")
    private Mannschaft mannschaft_heim;
    
    @ManyToOne()
    @JoinColumn(name="fk_midgast", referencedColumnName="mid")
    private Mannschaft mannschaft_gast;
    
    /**
     * Default constructor
     */
    public Spiel(){
    	
    }
    
    /**
     * Constructor
     * @param mannschaft_heim
     * @param mannschaft_gast
     * @param ort
     * @param beginn
     */
    public Spiel(Mannschaft mannschaft_heim, Mannschaft mannschaft_gast, String ort, Calendar beginn){
    	this.mannschaft_heim = mannschaft_heim;
    	this.mannschaft_gast = mannschaft_gast;
    	this.ort = ort;
    	this.beginn=beginn;
//    	this.ende.setTime(beginn.getTime()+110*60*10);
    }
    
    /**
     * Constructor without game-result
     * @param mannschaft_heim
     * @param mannschaft_gast
     * @param ort
     * @param beginn
     * @param ende
     */
    public Spiel(Mannschaft mannschaft_heim, Mannschaft mannschaft_gast, String ort, Calendar beginn, Calendar ende){
    	this.mannschaft_heim = mannschaft_heim;
    	this.mannschaft_gast = mannschaft_gast;
    	this.ort = ort;
    	this.beginn=beginn;
    	this.ende=ende;
    }
    
    /**
     * get tipps from this spiel
     * @return
     */
    public Collection<Tipp> getTipps(){
    	return this.tipps;
    }
    
    /**
     * get mannschaft_heim from this spiel
     * @return
     */
    public Mannschaft getMannschaftHeim(){
    	return this.mannschaft_heim;
    }
    
    /**
     * set mannschaft_heim from this spiel
     * @param m
     */
    public void setMannschaftHeim(Mannschaft m){
    	this.mannschaft_heim=m;
    }
    
    /**
     * get mannschaft_gast from this spiel
     * @return
     */
    public Mannschaft getMannschaftGast(){
    	return this.mannschaft_gast;
    }
    
    /**
     * get bezeichnung form this spiel
     * @return
     */
    public String getBezeichnung(){
    	return this.bezeichnung;
    }
    
    /**
     * set bezeichnung form this spiel
     * @param b
     */
    public void setBezeichnung(String b){
    	this.bezeichnung = b;
    }
    
    /**
     * set mannschaft_gast from this spiel
     * @param m
     */
    public void setMannschaftGast(Mannschaft m){
    	this.mannschaft_gast=m;
    }
    
    /**
     * set mannschaft_heim AND mannschaft_gast from this spiel
     * @param mh
     * @param mg
     */
    public void setVersus(Mannschaft mh, Mannschaft mg){
    	this.mannschaft_heim=mh;
    	this.mannschaft_gast=mg;
    }
    
    public Mannschaft searchWinner() {
    	Mannschaft m = new Mannschaft();
    	if(this.toreheim>this.toregast) {
			m = this.getMannschaftHeim();
		}else if (this.toregast>this.toreheim) {
			m = this.getMannschaftGast();
		}
    	return m;
    }
    
    public Mannschaft searchLoser() {
    	Mannschaft m = new Mannschaft();
    	if(this.toreheim < this.toregast) {
			m = this.getMannschaftHeim();
		}else if (this.toregast < this.toreheim) {
			m = this.getMannschaftGast();
		}
    	return m;
    }
    
    /**
     * return true if this games end-timestamp is before the current system-time
     * @return
     */
    public boolean gameOver(){
    	Calendar now = Calendar.getInstance();
    	return this.ende.before(now);
    }
    
    /**
     * return true if a game is currently running
     * @return
     */
    public boolean gameRunning(){
    	Calendar now = Calendar.getInstance();
    	return this.beginn.before(now) && this.ende.after(now);
    }
    
    public boolean isGg(){
    	//List<String> groupNames = Arrays.asList("A", "B", "C");
    	final GroupNames name = GroupNames.valueOf(this.getMannschaftHeim().gruppe);
    	return name != null;
    }
    
    public boolean checkAfTippReady(){
//    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Calendar now = Calendar.getInstance();
    	Timestamp rdy = Timestamp.valueOf("2014-06-26 22:00:00.0");
    	return this.getMannschaftHeim().gruppe.equals("AF") && now.after(rdy);
    }
    
    public boolean checkVfTippReady(){
//    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Calendar now = Calendar.getInstance();
    	Timestamp rdy = Timestamp.valueOf("2014-07-02 00:00:00.0");
    	return this.getMannschaftHeim().gruppe.equals("VF") && now.after(rdy);
    }
    
    public boolean checkHfTippReady(){
//    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Calendar now = Calendar.getInstance();
    	Timestamp rdy = Timestamp.valueOf("2014-07-06 00:00:00.0");
    	return this.getMannschaftHeim().gruppe.equals("HF") && now.after(rdy);
    }
    
    public boolean checkFTippReady(){
//    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Calendar now = Calendar.getInstance();
    	Timestamp rdy = Timestamp.valueOf("2014-07-10 00:00:00.0");
    	if(this.getMannschaftHeim().gruppe.equals("FI") || this.getMannschaftHeim().gruppe.equals("SP3")){
    		return now.after(rdy);
    	}else{
    		return false;
    	}
    }
    
    enum GroupNames {
    	A,
    	B,
    	C,
    	D,
    	E,
    	F,
    	G,
    	H;
    }
}
