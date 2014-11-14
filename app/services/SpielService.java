package services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import dao.*;
import models.*;
import play.Logger;
import play.db.jpa.Transactional;


public class SpielService extends Spiel{
	
	private static MannschaftDao mannschaftDao = new MannschaftDaoImpl();
	private static SpielDao spielDao = new SpielDaoImpl();
	private static TippDao tippDao = new TippDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	
	/**
	 * Ermittelt den Gewinner eines Spiels und gibt ihn zurueck
	 * @param s
	 * @return
	 */
	public Mannschaft searchWinner(Spiel s){
		Mannschaft m = new Mannschaft();
    	if(s.toreheim>s.toregast){
			m = s.getMannschaftHeim();
		}else if (s.toregast>s.toreheim){
			m = s.getMannschaftGast();
		}
    	return m;
	}
	
	/**
	 * Ueberpruft ob es sich bei einem Spiel mit der uebergebenen Spielbezeichnung um das jeweils letzte einer Phase handelt.
	 * Dem entsprechend wird jeweils die entsprechende Methode zum setzen der jeweils naechsten Phase aufgerufen.
	 * @param sBezeichnung
	 */
	public static void setFinalGames(String sBezeichnung){
		switch (sBezeichnung) {
			case "gg48":
				//wenn das hier das letzte Gruppenspiel war, setze AchtelFinalSpiele.
				Logger.info("---");
				Logger.info("Ermittel und setze AchtelFinale");
				setAF();
				break;
				
			case "af8":
				//wenn das hier das letzte AchtelFinalSpiel war, setze viertelFinale
				Logger.info("---");
				Logger.info("Ermittel und setze ViertelFinale");
				setVF(spielDao.findAll());
				break;
				
			case "vf4":
				//wenn das hier das letzte VF Spiel war setze HF
				Logger.info("---");
				Logger.info("Ermittel und setze HalbFinale");
				setHF(spielDao.findAll());
				break;
				
			case "hf2":
				//wenn das hier das letzte HF Spiel war setze Finale und SP3
				Logger.info("---");
				Logger.info("Ermittel und setze Finale und Spiel um Platz Drei");
				setFI(spielDao.findAll());
				break;
			
			case "fi":
				//Setze die ensprechenden Status der besten drei Mannschaften
				Logger.info("Die Weltmeisterschaft 2014 ist vorbei!");
				Spiel sp3 = spielDao.findByBezeichnung("sp3");
				Spiel fi = spielDao.findByBezeichnung("fi");
				Mannschaft mDritter = sp3.searchWinner();
				Mannschaft mVize = fi.searchLoser();
				Mannschaft mWeltmeister = fi.searchWinner();
				mDritter.status="DRITTER";
				mVize.status="VIZE";
				mWeltmeister.status="WELTMEISTER";
				break;
	
			default:
				break;
		  }
	}
	    
	  @Transactional
	  public static void handOutUserPoints(Collection<Tipp> tipps, byte th, byte tg){
		  //Punkte an User verteilen
		  //Jeden Tipp durchlaufen
		  for (Tipp t: tipps) {
			  //Pruefen, ob fuer den aktuellen Tipp schon Punkte vergeben wurden
			  if(t.checked==0){
				  //Punkte an Benutzer vergeben
				  User user = t.getUser();
				  int p=user.punkte;
				  //Wenn der Tipp genau richtig war, dann 3 Punkte
				  if(th==t.toreheim && tg==t.toregast){
					  p=p+3;
					  //Wenn die Tordifferenz richtig getippt wurde, dann 2 Punkte
				  }else if(th-tg==t.toreheim-t.toregast){
					  p=p+2;
					  //Wenn auf die richtige Mannschaft getippt wurde, dann 1 Punkt
				  }else if(th>tg && t.toreheim>t.toregast){
					  p=p+1;
				  }else if(tg>th && t.toregast>t.toreheim){
					  p=p+1;
				  }
				  user.punkte=p;
				  userDao.update(user);
				  //Diesen Tipp abhacken
				  t.checked=1;
				  tippDao.update(t);
			  }
		  }
	  }
	    
	    @Transactional
		public static void handOutTeamPoints(Spiel s, byte th, byte tg){
	    	Mannschaft mh = s.getMannschaftHeim();
	    	Mannschaft mg = s.getMannschaftGast();
	    	
	    	mh.anzahlspiele++;
			mg.anzahlspiele++;
			//Punkte an Mannschaften verteilen
			if (th>tg){
				//Bei Sieg 3 Punkte fuer Gewinner
				mh.punkte=mh.punkte+3;
				mh.siege++;
				mg.niederlagen++;
			}else if (th<tg){
				//Bei Sieg 3 Punkte fuer Gewinner
				mg.punkte=mg.punkte+3;
				mg.siege++;
				mh.niederlagen++;
			}else if (th==tg){
				//Bei Unentschieden einen Punkt fuer beide
				mh.punkte=mh.punkte+1;
				mg.punkte=mg.punkte+1;
				mh.unentschieden++;
				mg.unentschieden++;
			}
			//Tore und Gegentore setzen
			mh.tore=mh.tore+th;
			mg.tore=mg.tore+tg;
			mh.gegentore=mh.gegentore+tg;
			mg.gegentore=mg.gegentore+th;
			//Dieses Spiel abhaken, so dass keine Punkte mehr hierfuer vergeben werden
			s.checked=1;
			spielDao.update(s);
			mannschaftDao.update(mh);
			mannschaftDao.update(mg);
	    }
	    
	    /**
	     * Ermittelt die Mannschaften, welche im Achtelfinale gegeneinander antreten
	     * und setzt diese in die ensprechenden Spiele ein.
	     * @param spiele
	     */
	    @Transactional
		public static void setAF(){
//	    	//sorted
//	    	List<Spiel> spiele = spielDao.findAF();
//	    	
//	    	Map<String, Spiel> af = new HashMap<String, Spiel>();
//	    	
//	    	for(Spiel s : spiele) {
//	    		af.put(s.getBezeichnung(), s);
//	    	}
	    	/*
	    	 * Sieger und Zweitplatzierte der Gruppenspiele ermitteln
	    	 */
			String[] gruppen = {"A", "B", "C", "D", "E", "F", "G", "H"};
			Map<String, List<Mannschaft>> mannschaften = mannschaftDao.findAll();
			for(String key: gruppen){
				//Liste der Mannschaften in dieser Gruppe, sortiert nach Punkten.
				List<Mannschaft> mGruppe = mannschaftDao.findByGroup(key);
				Mannschaft m0=mGruppe.get(0);
				Mannschaft m1=mGruppe.get(1);
				Mannschaft m2=mGruppe.get(2);
				if(m0.punkte!=m1.punkte){
					//Wenn kein Punktegleichstand herrscht
					if(m1.punkte!=m2.punkte){
						//Wenn kein Punktegleichstand zwischen der zweiten und dritten Mannschaft herrscht
						m0.status="Sieger";
						m1.status="Zweiter";
					}else if (m1.punkte==m2.punkte){
						m0.status="Sieger";
						if(m1.tore-m1.gegentore>m2.tore-m2.gegentore){
							//Wenn Tordifferenz groesser
							m1.status="Zweiter";
						}else if(m1.tore-m1.gegentore<m2.tore-m2.gegentore){
							//Wenn Tordifferenz groesser
							m2.status="Zweiter";
						}
					}
				}else if(m0.punkte==m1.punkte && m0.punkte!=m2.punkte){
					//Wenn Punktegleichstand zwischen den ersten beiden Mannschaften herrscht (und nicht zwischen den ersten 3)
					if(m0.tore-m0.gegentore>m1.tore-m1.gegentore){
						//Wenn Tordifferenz groesser
						m0.status="Sieger";
						m1.status="Zweiter";
					}else if(m0.tore-m0.gegentore<m1.tore-m1.gegentore){
						//Wenn Tordifferenz groesser
						m1.status="Sieger";
						m0.status="Zweiter";
					}else if(m0.tore-m0.gegentore==m1.tore-m1.gegentore){
						//Wenn Tordifferenz gleich ist
						if(m0.tore>m1.tore){
							//Wenn anzahl der Tore groesser
							m0.status="Sieger";
							m1.status="Zweiter";
						}else if(m0.tore<m1.tore){
							//Wenn anzahl der Tore groesser
							m1.status="Sieger";
							m0.status="Zweiter";
						}else if(m0.tore==m1.tore){
							//Wenn Anzahl der Tore gleich ist
							Spiel db = spielDao.findVs(m0, m1);
							if (db.toreheim>db.toregast){
								//Wenn anzahl der Punkte aus db groesser
								m0.status="Sieger";
								m1.status="Zweiter";
							}else if(db.toregast>db.toreheim){
								//Wenn anzahl der Punkte aus db groesser
								m0.status="Zweiter";
								m1.status="Sieger";
							}else if(db.toreheim==db.toregast){
								//Wenn anzahl der Punkte aus db gleich
								if(db.toreheim-db.toregast>db.toregast-db.toreheim){
									//Wenn Tordifferenz aus db groesser
									m0.status="Sieger";
	    							m1.status="Zweiter";
								}else if(db.toregast-db.toreheim>db.toreheim-db.toregast){
									//Wenn Tordifferenz aus Datenbank groesser
									m0.status="Zweiter";
	    							m1.status="Sieger";
								}else if(db.toreheim-db.toregast==db.toregast-db.toregast){
									//Wenn Tordifferenz aus Datenbank gleich
									/**
									f. Anzahl der in den Direktbegegnungen der punktgleichen Mannschaften erzielten Tore.
									Sollten diese Kriterien nichtzu den eindeutigen Platzierungen führen, entscheidet die FIFA per Los.
									**/
								}
							}
							/**
							 	d. Anzahl Punkte aus Direktbegegnungen der punktgleichen Mannschaften,
								e. Tordifferenz aus den Direktbegegnungen der punktgleichen Mannschaften,
								f. Anzahl der in den Direktbegegnungen der punktgleichen Mannschaften erzielten Tore.
								Sollten diese Kriterien nichtzu den eindeutigen Platzierungen führen, entscheidet die FIFA per Los.
							 **/
							//Bitte die Gewinner und zweiten der jeweiligen Gruppe per Hand eintragen
							//mysql: UPDATE mannschaft SET status="Sieger/Zweiter <Gruppe>" WHERE mid=X;
						}
					}
//					af.get("af1").setMannschaftHeim(m0.status.startsWith("Sieger") ? m0 : m1);
				}else if(m0.punkte==m2.punkte){
					//Bitte die Gewinner und zweiten der jeweiligen Gruppe per Hand eintragen
					//mysql: UPDATE mannschaft SET status="Sieger/Zweiter <Gruppe>" WHERE mid=X;
				}
				m0.status=m0.status+" "+key;
				mannschaftDao.update(m0);
				if(m1.status!=null) {
					m1.status=m1.status+" "+key;
					mannschaftDao.update(m1);
				}
				if(m2.status!=null) {
					m2.status=m2.status+" "+key;
					mannschaftDao.update(m2);
				}
			}
			
			for(int i=1; i<=8; i++){
				Spiel af = spielDao.findByBezeichnung("af"+i);
				switch (i){
				case 1:
					Mannschaft mSA = mannschaftDao.findByState("Sieger A");
					Mannschaft mZB = mannschaftDao.findByState("Zweiter B");
					af.setVersus(mSA, mZB);
					break;
					
				case 2:
					Mannschaft mSC = mannschaftDao.findByState("Sieger C");
					Mannschaft mZD = mannschaftDao.findByState("Zweiter D");
					af.setVersus(mSC, mZD);
					break;
					
				case 3:
					Mannschaft mSB = mannschaftDao.findByState("Sieger B");
					Mannschaft mZA = mannschaftDao.findByState("Zweiter A");
					af.setVersus(mSB, mZA);
					break;
					
				case 4:
					Mannschaft mSD = mannschaftDao.findByState("Sieger D");
					Mannschaft mZC = mannschaftDao.findByState("Zweiter C");
					af.setVersus(mSD, mZC);
					break;
					
				case 5:
					Mannschaft mSE = mannschaftDao.findByState("Sieger E");
					Mannschaft mZF = mannschaftDao.findByState("Zweiter F");
					af.setVersus(mSE, mZF);
					break;
					
				case 6:
					Mannschaft mSG = mannschaftDao.findByState("Sieger G");
					Mannschaft mZH = mannschaftDao.findByState("Zweiter H");
					af.setVersus(mSG, mZH);
					break;
					
				case 7:
					Mannschaft mSF = mannschaftDao.findByState("Sieger F");
					Mannschaft mZE = mannschaftDao.findByState("Zweiter E");
					af.setVersus(mSF, mZE);
					break;
					
				case 8:
					Mannschaft mSH = mannschaftDao.findByState("Sieger H");
					Mannschaft mZG = mannschaftDao.findByState("Zweiter G");
					af.setVersus(mSH, mZG);
					break;
				}
				spielDao.update(af);
			}
	    }
	    
	    /**
	     * Ermittelt die Mannschaften, welche im Viertelfinale gegeneinander antreten
	     * und setzt diese in die ensprechenden Spiele ein.
	     * @param spiele
	     */
	    @Transactional
	    public static void setVF(Collection<Spiel> spiele){
	    	int i = 1;
	    	Mannschaft m = new Mannschaft();
	    	//Finde alle Viertelfinal-Spiele
			Spiel vf1 = spielDao.findByBezeichnung("vf1");
			Spiel vf2 = spielDao.findByBezeichnung("vf2");
			Spiel vf3 = spielDao.findByBezeichnung("vf3");
			Spiel vf4 = spielDao.findByBezeichnung("vf4");
			
			//Durchlaeft alle Spiele
			for (Spiel s: spiele){
				//Wenn es sich beim akuellen Spiel um ein Achtelfinal-Spiel handelt:
				if (s.getBezeichnung().equals("af"+i)) {
					//Ermittel den Gewinner des Spiels
					m = s.searchWinner();
					//Setze den Gewinner-Status
					m.status="Sieger AF" + i;
					//Setze den Gewinner in das entsprechende Viertelfinal-Spiel ein
					switch (i) {
						case 1:
							vf2.setMannschaftHeim(m);
							break;
						case 2:
							vf2.setMannschaftGast(m);
							break;
						case 3:
							vf4.setMannschaftHeim(m);
							break;
						case 4:
							vf4.setMannschaftGast(m);
							break;
						case 5:
							vf1.setMannschaftHeim(m);
							break;
						case 6:
							vf1.setMannschaftGast(m);
							break;
						case 7:
							vf3.setMannschaftHeim(m);
							break;
						case 8:
							vf3.setMannschaftGast(m);
							break;
					}
					i++;
					mannschaftDao.update(m);
				}
				spielDao.update(vf1);
				spielDao.update(vf2);
				spielDao.update(vf3);
				spielDao.update(vf4);
			}
	    }
	    
	    /**
	     * Ermittelt die Mannschaften, welche im Halbfinale gegeneinander antreten
	     * und setzt diese in die ensprechenden Spiele ein.
	     * @param spiele
	     */
	    @Transactional
	    public static void setHF(Collection<Spiel> spiele){
	    	int i = 1;
	    	Mannschaft m = new Mannschaft();
	    	//finde alle HalbFinal-Spiele
			Spiel hf1 = spielDao.findByBezeichnung("hf1");
			Spiel hf2 = spielDao.findByBezeichnung("hf2");
			
			for (Spiel s: spiele){
				if (s.getBezeichnung().equals("vf"+i)) {
					m = s.searchWinner();
					m.status="Sieger VF" + i;
					switch (i) {
						case 1:
							hf1.setMannschaftGast(m);
							break;
						case 2:
							hf1.setMannschaftHeim(m);
							break;
						case 3:
							hf2.setMannschaftGast(m);
							break;
						case 4:
							hf2.setMannschaftHeim(m);
							break;
					}
					i++;
					mannschaftDao.update(m);
				}
				spielDao.update(hf1);
				spielDao.update(hf2);
			}
	    }
	    
	    /**
	     * Ermittelt die Mannschaften, welche im Finale und im Spiel um Platz Drei gegeneinander antreten 
	     * und setzt diese in die ensprechenden Spiele ein.
	     * @param spiele
	     */
	    @Transactional
	    public static void setFI(Collection<Spiel> spiele) {
	    	int i = 1;
	    	Mannschaft mW = new Mannschaft();
	    	Mannschaft mL = new Mannschaft();
	    	//Finde Spiel um Platz Drei und Finalspiel
			Spiel sp3 = spielDao.findByBezeichnung("sp3");
			Spiel fi = spielDao.findByBezeichnung("fi");
			
			for (Spiel s: spiele){
				if (s.getBezeichnung().equals("hf"+i)) {
					mW = s.searchWinner();
					mW.status="Sieger HF" + i;
					mL = s.searchLoser();
					mL.status="Verlierer HF" + i;
					switch (i) {
						case 1:
							fi.setMannschaftHeim(mW);
							sp3.setMannschaftHeim(mL);
							break;
						case 2:
							fi.setMannschaftGast(mW);
							sp3.setMannschaftGast(mL);
							break;
					}
					i++;
					mannschaftDao.update(mW);
					mannschaftDao.update(mL);
				}
				spielDao.update(sp3);
				spielDao.update(fi);
			}
	    }
	    
	    /**
	     * return true if this games end-timestamp is before the current system-time
	     * @return
	     */
	    public boolean gameOver(Spiel s){
	    	Calendar now = Calendar.getInstance();
	    	return s.ende.before(now);
	    }
	    
	    /**
	     * return true if a game is currently running
	     * @return
	     */
	    public boolean gameRunning(Spiel s){
	    	Calendar now = Calendar.getInstance();
	    	return s.beginn.before(now) && s.ende.after(now);
	    }
	    
	    /**
	     * return true when the game s is a groupgame
	     * @param s
	     * @return
	     */
	    public boolean isGg(Spiel s){
	    	return s.getMannschaftHeim().gruppe.equals("A") || s.getMannschaftHeim().gruppe.equals("B") || s.getMannschaftHeim().gruppe.equals("C") || s.getMannschaftHeim().gruppe.equals("D") || s.getMannschaftHeim().gruppe.equals("E") || s.getMannschaftHeim().gruppe.equals("F") || s.getMannschaftHeim().gruppe.equals("G") || s.getMannschaftHeim().gruppe.equals("H");
	    }
	    
	    /**
	     * Ueberprueft, ob das uebergebene Achtelfinal-Spiel s bereit zum tippen ist.
	     * @param s
	     * @return
	     */
	    public boolean checkAfTippReady(Spiel s){
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-06-26 22:00:00.0");
	    	return s.getMannschaftHeim().gruppe.equals("AF") && now.after(rdy);
	    }
	    
	    /**
	     * Ueberprueft, ob das uebergebene Viertelfinal-Spiel s bereit zum tippen ist.
	     * @param s
	     * @return
	     */
	    public boolean checkVfTippReady(Spiel s){
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-07-02 00:00:00.0");
	    	return s.getMannschaftHeim().gruppe.equals("VF") && now.after(rdy);
	    }
	    
	    /**
	     * Ueberprueft, ob das uebergebene Halbfinal-Spiel s bereit zum tippen ist.
	     * @param s
	     * @return
	     */
	    public boolean checkHfTippReady(Spiel s){
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-07-06 00:00:00.0");
	    	return s.getMannschaftHeim().gruppe.equals("HF") && now.after(rdy);
	    }
	    
	    /**
	     * Ueberprueft, ob das uebergebene Final-Spiel s bereit zum tippen ist.
	     * @param s
	     * @return
	     */
	    public boolean checkFTippReady(Spiel s){
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-07-10 00:00:00.0");
	    	if(s.getMannschaftHeim().gruppe.equals("FI") || s.getMannschaftHeim().gruppe.equals("SP3")){
	    		return now.after(rdy);
	    	}else{
	    		return false;
	    	}
	    }
}
