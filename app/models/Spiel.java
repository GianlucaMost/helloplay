package models;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import dao.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;


/**
 * Spiel entity managed by JPA
 */
@Entity 
@Table(name="spiel")
public class Spiel {  
	
	//testweise den static modifier entfernen
//	private MannschaftDao mannschaftDao = new MannschaftDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	private static TippDao tippDao = new TippDaoImpl();
	
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
    public Calendar beginn = Calendar.getInstance();
    
    @Constraints.Required
    @Column(name="ende")
// 	public Timestamp ende;
    public Calendar ende = Calendar.getInstance();
    
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
    
//    public void setVersusByState(String mhs, String mgs){
//    	this.mannschaft_heim=mannschaftDao.findByState(mhs);
//    	this.mannschaft_gast=mannschaftDao.findByState(mgs);
//    }
    
    public Mannschaft searchWinner(){
    	Mannschaft m = new Mannschaft();
    	if(this.toreheim>this.toregast){
			m = this.getMannschaftHeim();
		}else if (this.toregast>this.toreheim){
			m = this.getMannschaftGast();
		}
    	return m;
    }
    
//    /**
//     * non static game-result-setting
//     * @param toreheim
//     * @param toregast
//     * @throws Throwable 
//     */
//    @Transactional
//    public void setErgebnis(byte th, byte tg) throws Throwable{
//    	byte thp = this.toreheim;
//    	byte tgp = this.toregast;
//    	Mannschaft mh = this.getMannschaftHeim();
//		Mannschaft mg = this.getMannschaftGast();
//		Spiel spiel = this;
//		
//		Collection<Spiel> spiele = Spiel.findAll();
//		
////		final Collection<Spiel> spiele = JPA.withTransaction(new F.Function0<Collection<Spiel>>() {
////			@Override
////			public Collection<Spiel> apply() throws Throwable {
////				return Spiel.findAll();
////			}
////		});
//		
//    	if (thp!=th || tgp!=tg){
//    		spiel.toreheim = th;
//        	spiel.toregast = tg;
////        	JPA.em().persist(this);
////        	JPA.em().merge(this);
//        	spiel = JPA.em().merge(spiel);
//    	}
//    	if (spiel.gameOver()){
//    		//Punkte an Benutzer verteilen
//    		handOutUserPoints(spiel.tipps, th, tg);
//    		
//    		if(spiel.checked==0){
//    			handOutTeamPoints(spiel, mh, mg, th, tg);
//	    		
//	    		//wenn das hier das letzte gruppenspiel war, setze AchtelFinalSpiele.
//	    		if(spiel.getBezeichnung().equals("gg48")){
//	    			setAF();
//	    		}
//	    		//wenn das hier das letzte AchtelFinalSpiel war, setze viertelFinale
//	    		if(spiel.getBezeichnung().equals("af8")){
//	    			//setze vf
//	    			setVF(spiele);
//	    		}
//	    		//wenn das hier das letzte VF Spiel war setze HF
//	    		if(spiel.getBezeichnung().equals("vf4")){
//					//setze hf
//	    			setHF(spiele);
//				}
//	    		//wenn das hier das letzte HF Spiel war setze Finale und SP3
//	    		if(spiel.getBezeichnung().equals("hf2")){
//	    			//setze fi
//	    			setFI(spiele);
//				}
//    		}
//    	}
//    }
//    
//    public static void setFinalGames(Spiel s){
//    	Collection<Spiel> spiele = Spiel.findAll();
//    	
//    	//wenn das hier das letzte gruppenspiel war, setze AchtelFinalSpiele.
//		if(s.getBezeichnung().equals("gg48")){
//			Spiel.setAF();
//		}
//		//wenn das hier das letzte AchtelFinalSpiel war, setze viertelFinale
//		if(s.getBezeichnung().equals("af8")){
//			//setze vf
//			Spiel.setVF(spiele);
//		}
//		//wenn das hier das letzte VF Spiel war setze HF
//		if(s.getBezeichnung().equals("vf4")){
//			//setze hf
//			Spiel.setHF(spiele);
//		}
//		//wenn das hier das letzte HF Spiel war setze Finale und SP3
//		if(s.getBezeichnung().equals("hf2")){
//			//setze fi
//			Spiel.setFI(spiele);
//		}
//    }
    
    @Transactional
	public static void handOutUserPoints(Collection<Tipp> tipps, byte th, byte tg){
    	// Punkte an User verteilen
		//jeden tipp durchlaufen
		for (Tipp t: tipps) {
			//pruefen ob dieser tipp.checked=0 ist
			if(t.checked==0){
				//punkte vergeben
				User user = t.getUser();
				int p=user.punkte;
					//wenn genau richtiges ergebnis dann 3punkte
					if(th==t.toreheim && tg==t.toregast){
						p=p+3;
					//wenn richtige tordifferenz dann 2punkte
					}else if(th-tg==t.toreheim-t.toregast){
						p=p+2;
					//wenn richtige mannschaft dann 1punkt
					}else if(th>tg && t.toreheim>t.toregast){
						p=p+1;
					}else if(tg>th && t.toregast>t.toreheim){
						p=p+1;
					}
				user.punkte=p;
	/**
	* temporär auskommentiert
	 */
//				user.persist();
				userDao.update(user);
				//diesen tipp.checked=1 setzen
				t.checked=1;
	/**
	* temporär auskommentiert
	 */
//				t.persist();
				tippDao.update(t);
			}
		}
    }
    
//    @Transactional
//	public static void handOutTeamPoints(Spiel s, Mannschaft mh, Mannschaft mg, byte th, byte tg){
//    	mh.anzahlspiele++;
//		mg.anzahlspiele++;
//		//Punkte an Mannschaften verteilen
//		if (th>tg){
//			//Bei Sieg drei Punkte fuer Gewinner
//			mh.punkte=mh.punkte+3;
//			mh.siege++;
//			mg.niederlagen++;
//		}else if (th<tg){
//			//Bei Sieg 3 Punkte fuer Gewinner
//			mg.punkte=mg.punkte+3;
//			mg.siege++;
//			mh.niederlagen++;
//		}else if (th==tg){
//			//Bei Unentschieden einen Punkt fuer beide
//			mh.punkte=mh.punkte+1;
//			mg.punkte=mg.punkte+1;
//			mh.unentschieden++;
//			mg.unentschieden++;
//		}
//		//Tore und Gegentore setzen
//		mh.tore=mh.tore+th;
//		mg.tore=mg.tore+tg;
//		mh.gegentore=mh.gegentore+tg;
//		mg.gegentore=mg.gegentore+th;
//		//Dieses Spiel abhaken, so dass keine Punkte mehr hierfuer vergeben werden
//		s.checked=1;
//		s.persist();
//		mannschaftDao.update(mh);
//		mannschaftDao.update(mg);
//    }
//    
//    @Transactional
//	public static void setAF(){
//    	//Sieger und Zweitplatzierte der GruppenSpiele ermitteln
//		String[] gruppen = {"A", "B", "C", "D", "E", "F", "G", "H"};
//		Map<String, List<Mannschaft>> mannschaften = mannschaftDao.findAll();
//		for(String key: gruppen){
//			//Liste der Mannschaften in dieser Gruppe, sortiert nach Punkten.
//			List<Mannschaft> mGruppe = mannschaftDao.findByGroup(key);
//			Mannschaft m0=mGruppe.get(0);
//			Mannschaft m1=mGruppe.get(1);
//			Mannschaft m2=mGruppe.get(2);
//			if(m0.punkte!=m1.punkte){
//				//wenn kein Punktegleichstand herrscht
//				m0.status="Sieger";
//				m1.status="Zweiter";
//			}else if(m0.punkte==m1.punkte && m0.punkte!=m2.punkte){
//				//wenn Punktegleichstand zwischen den ersten beiden Mannschaften herrscht (und nicht zwischen den ersten 3)
//				if(m0.tore-m0.gegentore>m1.tore-m1.gegentore){
//					//wenn Tordifferenz groesser
//					m0.status="Sieger";
//					m1.status="Zweiter";
//				}else if(m0.tore-m0.gegentore<m1.tore-m1.gegentore){
//					//wenn Tordifferenz groesser
//					m1.status="Sieger";
//					m0.status="Zweiter";
//				}else if(m0.tore-m0.gegentore==m1.tore-m1.gegentore){
//					//wenn Tordifferenz gleich ist
//					if(m0.tore>m1.tore){
//						//wenn anzahl der Tore groesser
//						m0.status="Sieger";
//						m1.status="Zweiter";
//					}else if(m0.tore<m1.tore){
//						//wenn anzahl der Tore groesser
//						m1.status="Sieger";
//						m0.status="Zweiter";
//					}else if(m0.tore==m1.tore){
//						//wenn Anzahl der tore gleich ist
//						Spiel db = Spiel.findVs(m0, m1);
//						if (db.toreheim>db.toregast){
//							//wenn anzahl der Punkte aus db groesser
//							m0.status="Sieger";
//							m1.status="Zweiter";
//						}else if(db.toregast>db.toreheim){
//							//wenn anzahl der Punkte aus db groesser
//							m0.status="Zweiter";
//							m1.status="Sieger";
//						}else if(db.toreheim==db.toregast){
//							//wenn anzahl der punkte aus db gleich
//							if(db.toreheim-db.toregast>db.toregast-db.toreheim){
//								//wenn tordifferenz aus db groesser
//								m0.status="Sieger";
//    							m1.status="Zweiter";
//							}else if(db.toregast-db.toreheim>db.toreheim-db.toregast){
//								//wenn tordifferenz aus db groesser
//								m0.status="Zweiter";
//    							m1.status="Sieger";
//							}else if(db.toreheim-db.toregast==db.toregast-db.toregast){
//								//wenn tordifferenz aus db gleich
//								/**
//								f. Anzahl der in den Direktbegegnungen der punktgleichen Mannschaften erzielten Tore.
//								Sollten diese Kriterien nichtzu den eindeutigen Platzierungen führen, entscheidet die FIFA per Los.
//								**/
//							}
//						}
//						/**
//						 	d. Anzahl Punkte aus Direktbegegnungen der punktgleichen Mannschaften,
//							e. Tordifferenz aus den Direktbegegnungen der punktgleichen Mannschaften,
//							f. Anzahl der in den Direktbegegnungen der punktgleichen Mannschaften erzielten Tore.
//							Sollten diese Kriterien nichtzu den eindeutigen Platzierungen führen, entscheidet die FIFA per Los.
//						 **/
//						//bitte die gewinner und zweiten der jeweiligen gruppe per hand eintragen
//						//mysql: UPDATE mannschaft SET status="Sieger/Zweiter <Gruppe>" WHERE mid=X;
//					}
//				}
//				m0.status=m0.status+" "+key;
//				m1.status=m1.status+" "+key;
//				mannschaftDao.update(m0);
//				mannschaftDao.update(m1);
//			}else if(m0.punkte==m2.punkte){
//				//bitte die gewinner und zweiten der jeweiligen gruppe per hand eintragen
//				//mysql: UPDATE mannschaft SET status="Sieger/Zweiter <Gruppe>" WHERE mid=X;
//			}
//		}
//		
////		//finde alle AchtelFinal-Spiele
////		Spiel af1 = Spiel.findByBezeichnung("af1");
////		Spiel af2 = Spiel.findByBezeichnung("af2");
////		Spiel af3 = Spiel.findByBezeichnung("af3");
////		Spiel af4 = Spiel.findByBezeichnung("af4");
////		Spiel af5 = Spiel.findByBezeichnung("af5");
////		Spiel af6 = Spiel.findByBezeichnung("af6");
////		Spiel af7 = Spiel.findByBezeichnung("af7");
////		Spiel af8 = Spiel.findByBezeichnung("af8");
////		
////		//setze AchtelFinale
////		af1.setVersusByState("Sieger A", "Zweiter B");
////		af2.setVersusByState("Sieger C", "Zweiter D");
////		af3.setVersusByState("Sieger B", "Zweiter A");
////		af4.setVersusByState("Sieger D", "Zweiter C");
////		af5.setVersusByState("Sieger E", "Zweiter F");
////		af6.setVersusByState("Sieger G", "Zweiter H");
////		af7.setVersusByState("Sieger F", "Zweiter E");
////		af8.setVersusByState("Sieger H", "Zweiter G");
////		
////		af1.persist();
////		af2.persist();
////		af3.persist();
////		af4.persist();
////		af5.persist();
////		af6.persist();
////		af7.persist();
////		af8.persist();
//		
//		for(int i=1; i<=8; i++){
//			Spiel af = Spiel.findByBezeichnung("af"+i);
//			switch (i){
//			case 1:
//				af.setVersusByState("Sieger A", "Zweiter B");
//				break;
//				
//			case 2:
//				af.setVersusByState("Sieger C", "Zweiter D");
//				break;
//				
//			case 3:
//				af.setVersusByState("Sieger B", "Zweiter A");
//				break;
//				
//			case 4:
//				af.setVersusByState("Sieger D", "Zweiter C");
//				break;
//				
//			case 5:
//				af.setVersusByState("Sieger E", "Zweiter F");
//				break;
//				
//			case 6:
//				af.setVersusByState("Sieger G", "Zweiter H");
//				break;
//				
//			case 7:
//				af.setVersusByState("Sieger F", "Zweiter E");
//				break;
//				
//			case 8:
//				af.setVersusByState("Sieger H", "Zweiter G");
//				break;
//			}
//			af.persist();
//		}
//    }
//    
//    @Transactional
//    public static void setVF(Collection<Spiel> spiele){
//    	Mannschaft m = new Mannschaft();
//		for (Spiel s: spiele){
//			switch (s.getBezeichnung()){
//			case "af1":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF1";
//				break;
//				
//			case "af2":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF2";
//				break;
//				
//			case "af3":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF3";
//				break;
//				
//			case "af4":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF4";
//				break;
//				
//			case "af5":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF5";
//				break;
//				
//			case "af6":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF6";
//				break;
//				
//			case "af7":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF7";
//				break;
//				
//			case "af8":
//				m = s.findWinner();
//				m.bezeichnung="Sieger AF8";
//				break;
//			}
//			mannschaftDao.update(m);
//		}
//		
//		//Sieger der AchtelFinal-Spiele ermitteln
//		Mannschaft siegerAF1 = mannschaftDao.findByState("Sieger AF1");
//		Mannschaft siegerAF2 = mannschaftDao.findByState("Sieger AF2");
//		Mannschaft siegerAF3 = mannschaftDao.findByState("Sieger AF3");
//		Mannschaft siegerAF4 = mannschaftDao.findByState("Sieger AF4");
//		Mannschaft siegerAF5 = mannschaftDao.findByState("Sieger AF5");
//		Mannschaft siegerAF6 = mannschaftDao.findByState("Sieger AF6");
//		Mannschaft siegerAF7 = mannschaftDao.findByState("Sieger AF7");
//		Mannschaft siegerAF8 = mannschaftDao.findByState("Sieger AF8");
//		
//		//finde alle ViertelFinal-Spiele
//		Spiel vf1 = Spiel.findByBezeichnung("vf1");
//		Spiel vf2 = Spiel.findByBezeichnung("vf2");
//		Spiel vf3 = Spiel.findByBezeichnung("vf3");
//		Spiel vf4 = Spiel.findByBezeichnung("vf4");
//		
//		//setze ViertelFinale
//		vf1.setVersus(siegerAF5, siegerAF6);
//		vf2.setVersus(siegerAF1, siegerAF2);
//		vf3.setVersus(siegerAF7, siegerAF8);
//		vf4.setVersus(siegerAF3, siegerAF4);
//		
//		vf1.persist();
//		vf2.persist();
//		vf3.persist();
//		vf4.persist();
//    }
//    
//    @Transactional
//    public static void setHF(Collection<Spiel> spiele){
//    	Mannschaft m = new Mannschaft();
//		for (Spiel s: spiele){
//			switch (s.getBezeichnung()){
//			case "vf1":
//				m = s.findWinner();
//				m.bezeichnung="Sieger VF1";
//				break;
//				
//			case "vf2":
//				m = s.findWinner();
//				m.bezeichnung="Sieger VF2";
//				break;
//				
//			case "vf3":
//				m = s.findWinner();
//				m.bezeichnung="Sieger VF3";
//				break;
//				
//			case "vf4":
//				m = s.findWinner();
//				m.bezeichnung="Sieger VF4";
//				break;
//			}
//			mannschaftDao.update(m);
//		}
//		
//		//Sieger der ViertelFinal-Spiele ermitteln
//		Mannschaft siegerVF1 = mannschaftDao.findByState("Sieger VF1");
//		Mannschaft siegerVF2 = mannschaftDao.findByState("Sieger VF2");
//		Mannschaft siegerVF3 = mannschaftDao.findByState("Sieger VF3");
//		Mannschaft siegerVF4 = mannschaftDao.findByState("Sieger VF4");
//		
//		//finde alle HalbFinal-Spiele
//		Spiel hf1 = Spiel.findByBezeichnung("hf1");
//		Spiel hf2 = Spiel.findByBezeichnung("hf2");
//		
//		//setze HalbFinale
//		hf1.setVersus(siegerVF1, siegerVF2);
//		hf2.setVersus(siegerVF3, siegerVF4);
//		
//		hf1.persist();
//		hf2.persist();
//    }
//    
//    @Transactional
//    public static void setFI(Collection<Spiel> spiele){
//    	for (Spiel s: spiele){
//			Mannschaft mHeim = s.getMannschaftHeim();
//			Mannschaft mGast = s.getMannschaftGast();
//			switch (s.getBezeichnung()){
//			case "hf1":
//				if(s.toreheim>s.toregast){;
//					mHeim.bezeichnung="Sieger HF1";
//					mGast.bezeichnung="Verlierer HF1";
//				}else if (s.toregast>s.toreheim){
//					mGast.bezeichnung="Sieger HF1";
//					mHeim.bezeichnung="Verlierer HF1";
//				}
//				break;
//				
//			case "hf2":
//				if(s.toreheim>s.toregast){;
//					mHeim.bezeichnung="Sieger HF2";
//					mGast.bezeichnung="Verlierer HF2";
//				}else if (s.toregast>s.toreheim){
//					mGast.bezeichnung="Sieger HF2";
//					mHeim.bezeichnung="Verlierer HF2";
//				}
//				break;
//			}
//			mannschaftDao.update(mHeim);
//			mannschaftDao.update(mGast);
//		}
//		
//		//Sieger und Verlierer der HalbFinal-Spiele ermitteln
//		Mannschaft siegerHF1 = mannschaftDao.findByState("Sieger HF1");
//		Mannschaft verliererHF1 = mannschaftDao.findByState("Verlierer HF1");
//		Mannschaft siegerHF2 = mannschaftDao.findByState("Sieger HF2");
//		Mannschaft verliererHF2 = mannschaftDao.findByState("Verlierer HF2");
//		
//		//finde Spiel um Platz 3 und Finale
//		Spiel sp3 = Spiel.findByBezeichnung("sp3");
//		Spiel fi = Spiel.findByBezeichnung("fi");
//		
//		//setze Spiel um Platz 3 und Finale
//		sp3.setVersus(verliererHF1, verliererHF2);
//		fi.setVersus(siegerHF1, siegerHF2);
//		
//		sp3.persist();
//		fi.persist();
//    }
    
    
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
//    	Timestamp now = new Timestamp(System.currentTimeMillis());
//    	Date date = new Date(System.currentTimeMillis());
    	Calendar now = Calendar.getInstance();
//    	now.setTime(date);
    	return this.beginn.before(now) && this.ende.after(now);
    }
    
    public boolean isGg(){
    	return this.getMannschaftHeim().gruppe.equals("A") || this.getMannschaftHeim().gruppe.equals("B") || this.getMannschaftHeim().gruppe.equals("C") || this.getMannschaftHeim().gruppe.equals("D") || this.getMannschaftHeim().gruppe.equals("E") || this.getMannschaftHeim().gruppe.equals("F") || this.getMannschaftHeim().gruppe.equals("G") || this.getMannschaftHeim().gruppe.equals("H");
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
    
//    @Transactional
//    public static void setResultWithRss(){
//		try {
//			byte th=0;
//			byte tg=0;
//			Mannschaft mh=null;
//			Mannschaft mg=null;
//			String mhRename="";
//			String mgRename="";
//			
////			URL feedSource = new URL("http://rss.kicker.de/live/championsleaguequalifikation");
////			URL feedSource = new URL("http://rss.kicker.de/live/wm");
////			URL feedSource = new URL("http://www.localhost:9000/rss.xml");
////			URL feedSource = new URL("file://rss.xml");
//			URL feedSource = new File("rss.xml").toURI().toURL();
//			SyndFeedInput input = new SyndFeedInput();
//	    	SyndFeed feed = input.build(new XmlReader(feedSource));
//	    	
//	    	List<SyndEntry> entries = feed.getEntries();
//	    	
//	    	for(SyndEntry se: entries){
//	    		String title=se.getTitle();
//	    		Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9])$");
////	    		Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9]) (.*)$");
//	    		Matcher matcher = pattern.matcher(title);
//	    		if(matcher.matches()){
//	    			Logger.info("RSSfeed matches");
//	    			//umbenennen nicht identischer mannschafts-bezeichnungen
//	    			mhRename=matcher.group(1);
//	    			mgRename=matcher.group(2);
//	    			switch(mhRename){
//	    			case "Elfenbeinküste":
//	    				mhRename="Elfenbeinkueste";
//	    				break;
//	    			case "Bosnien-Herzegowina":
//	    				mhRename="Bosnien-H.";
//	    				break;
//	    			case "Südkorea":
//	    				mhRename="Korea Republik";
//	    				break;
//	    			}
//	    			switch(mgRename){
//	    			case "Elfenbeinküste":
//	    				mgRename="Elfenbeinkueste";
//	    				break;
//	    			case "Bosnien-Herzegowina":
//	    				mgRename="Bosnien-H.";
//	    				break;
//	    			case "Südkorea":
//	    				mgRename="Korea Republik";
//	    				break;
//	    			}
//	    			Logger.info("finde Mannschaften");
//	    			
//	    			JPA.withTransaction(new F.Callback0() {
//	    				@Override
//	    				public void invoke() throws Throwable {
//	    					mh=Mannschaft.findByName(mhRename);
//	    				}
//	    			});
//	    			mh = Mannschaft.findByName(mhRename);
//	    			mg=Mannschaft.findByName(mgRename);
//	    			Logger.info("setze tore");
//	    			th=Byte.parseByte((matcher.group(3)));
//	    			tg=Byte.parseByte((matcher.group(4)));
//	    			Logger.info("Found RSSfeed that match!");
//	    			Logger.info("mh = " + mh + "(" + mhRename + ")");
//	    			Logger.info("mg = " + mg + "(" + mgRename + ")");
//	    			Logger.info("th = " + th);
//	    			Logger.info("tg = " + tg);
//	    			Spiel gg = Spiel.findGroupGame(mh, mg);
//	    	    	gg.setErgebnis(th, tg);
//	    		}else{
//	    			Logger.warn("Found RSSfeed, that doesnt match!");
//	    			Logger.info("Title: " + title);
//	    		}
//	    	}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FeedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
}
