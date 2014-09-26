package services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
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

	public Mannschaft searchWinner(Spiel s){
		Mannschaft m = new Mannschaft();
    	if(s.toreheim>s.toregast){
			m = s.getMannschaftHeim();
		}else if (s.toregast>s.toreheim){
			m = s.getMannschaftGast();
		}
    	return m;
	}
	    
	  public static void setFinalGames(String sBezeichnung){
		  switch (sBezeichnung) {
			case "gg48":
				//wenn das hier das letzte gruppenspiel war, setze AchtelFinalSpiele.
				Logger.info("Ermittel und setze AchtelFinale");
				setAF();
				break;
				
			case "af8":
				//wenn das hier das letzte AchtelFinalSpiel war, setze viertelFinale
				Logger.info("Ermittel und setze ViertelFinale");
				setVF(spielDao.findAll());
				break;
				
			case "vf4":
				//wenn das hier das letzte VF Spiel war setze HF
				Logger.info("Ermittel und setze HalbFinale");
				setHFalternative(spielDao.findAll());
				break;
				
			case "hf2":
				//wenn das hier das letzte HF Spiel war setze Finale und SP3
				Logger.info("Ermittel und setze Finale und Spiel um Platz 3");
				setFI(spielDao.findAll());
				break;
	
			default:
				break;
		  }
	  }
	    
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
				  userDao.update(user);
				  //diesen tipp.checked=1 setzen
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
				//Bei Sieg drei Punkte fuer Gewinner
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
	    	
	    	//Sieger und Zweitplatzierte der GruppenSpiele ermitteln
			String[] gruppen = {"A", "B", "C", "D", "E", "F", "G", "H"};
			Map<String, List<Mannschaft>> mannschaften = mannschaftDao.findAll();
			for(String key: gruppen){
				//Liste der Mannschaften in dieser Gruppe, sortiert nach Punkten.
				List<Mannschaft> mGruppe = mannschaftDao.findByGroup(key);
				Mannschaft m0=mGruppe.get(0);
				Mannschaft m1=mGruppe.get(1);
				Mannschaft m2=mGruppe.get(2);
				if(m0.punkte!=m1.punkte){
					//wenn kein Punktegleichstand herrscht
					if(m1.punkte!=m2.punkte){
						//wenn kein Punktegleichstand zwischen der zweiten und dritten Mannschaft herrscht
						m0.status="Sieger";
						m1.status="Zweiter";
					}else if (m1.punkte==m2.punkte){
						m0.status="Sieger";
						if(m1.tore-m1.gegentore>m2.tore-m2.gegentore){
							//wenn Tordifferenz groesser
							m1.status="Zweiter";
						}else if(m1.tore-m1.gegentore<m2.tore-m2.gegentore){
							//wenn Tordifferenz groesser
							m2.status="Zweiter";
						}
					}
				}else if(m0.punkte==m1.punkte && m0.punkte!=m2.punkte){
					//wenn Punktegleichstand zwischen den ersten beiden Mannschaften herrscht (und nicht zwischen den ersten 3)
					if(m0.tore-m0.gegentore>m1.tore-m1.gegentore){
						//wenn Tordifferenz groesser
						m0.status="Sieger";
						m1.status="Zweiter";
					}else if(m0.tore-m0.gegentore<m1.tore-m1.gegentore){
						//wenn Tordifferenz groesser
						m1.status="Sieger";
						m0.status="Zweiter";
					}else if(m0.tore-m0.gegentore==m1.tore-m1.gegentore){
						//wenn Tordifferenz gleich ist
						if(m0.tore>m1.tore){
							//wenn anzahl der Tore groesser
							m0.status="Sieger";
							m1.status="Zweiter";
						}else if(m0.tore<m1.tore){
							//wenn anzahl der Tore groesser
							m1.status="Sieger";
							m0.status="Zweiter";
						}else if(m0.tore==m1.tore){
							//wenn Anzahl der tore gleich ist
							Spiel db = spielDao.findVs(m0, m1);
							if (db.toreheim>db.toregast){
								//wenn anzahl der Punkte aus db groesser
								m0.status="Sieger";
								m1.status="Zweiter";
							}else if(db.toregast>db.toreheim){
								//wenn anzahl der Punkte aus db groesser
								m0.status="Zweiter";
								m1.status="Sieger";
							}else if(db.toreheim==db.toregast){
								//wenn anzahl der punkte aus db gleich
								if(db.toreheim-db.toregast>db.toregast-db.toreheim){
									//wenn tordifferenz aus db groesser
									m0.status="Sieger";
	    							m1.status="Zweiter";
								}else if(db.toregast-db.toreheim>db.toreheim-db.toregast){
									//wenn tordifferenz aus db groesser
									m0.status="Zweiter";
	    							m1.status="Sieger";
								}else if(db.toreheim-db.toregast==db.toregast-db.toregast){
									//wenn tordifferenz aus db gleich
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
							//bitte die gewinner und zweiten der jeweiligen gruppe per hand eintragen
							//mysql: UPDATE mannschaft SET status="Sieger/Zweiter <Gruppe>" WHERE mid=X;
						}
					}
//					af.get("af1").setMannschaftHeim(m0.status.startsWith("Sieger") ? m0 : m1);
				}else if(m0.punkte==m2.punkte){
					//bitte die gewinner und zweiten der jeweiligen gruppe per hand eintragen
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
	    
	    @Transactional
	    public static void setVF(Collection<Spiel> spiele){
	    	int i = 1;
	    	Mannschaft m = new Mannschaft();
	    	//finde alle ViertelFinal-Spiele
			Spiel vf1 = spielDao.findByBezeichnung("vf1");
			Spiel vf2 = spielDao.findByBezeichnung("vf2");
			Spiel vf3 = spielDao.findByBezeichnung("vf3");
			Spiel vf4 = spielDao.findByBezeichnung("vf4");
			
			for (Spiel s: spiele){
				if (s.getBezeichnung().equals("af"+i)) {
					m = s.searchWinner();
					m.status="Sieger AF" + i;
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
	    
	    @Transactional
	    public static void setHF(Collection<Spiel> spiele){
	    	Mannschaft m = new Mannschaft();
			for (Spiel s: spiele){
				switch (s.getBezeichnung()){
				case "vf1":
					m = s.searchWinner();
					m.status="Sieger VF1";
					break;
					
				case "vf2":
					m = s.searchWinner();
					m.status="Sieger VF2";
					break;
					
				case "vf3":
					m = s.searchWinner();
					m.status="Sieger VF3";
					break;
					
				case "vf4":
					m = s.searchWinner();
					m.status="Sieger VF4";
					break;
				}
				mannschaftDao.update(m);
			}
			
			//Sieger der ViertelFinal-Spiele ermitteln
			Mannschaft siegerVF1 = mannschaftDao.findByState("Sieger VF1");
			Mannschaft siegerVF2 = mannschaftDao.findByState("Sieger VF2");
			Mannschaft siegerVF3 = mannschaftDao.findByState("Sieger VF3");
			Mannschaft siegerVF4 = mannschaftDao.findByState("Sieger VF4");
			
			//finde alle HalbFinal-Spiele
			Spiel hf1 = spielDao.findByBezeichnung("hf1");
			Spiel hf2 = spielDao.findByBezeichnung("hf2");
			
			//setze HalbFinale
			hf1.setVersus(siegerVF1, siegerVF2);
			hf2.setVersus(siegerVF3, siegerVF4);

			spielDao.update(hf1);
			spielDao.update(hf2);
	    }
	    
	    @Transactional
	    public static void setHFalternative(Collection<Spiel> spiele){
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
							hf1.setMannschaftHeim(m);
							break;
						case 2:
							hf1.setMannschaftGast(m);
							break;
						case 3:
							hf2.setMannschaftHeim(m);
							break;
						case 4:
							hf2.setMannschaftGast(m);
							break;
					}
					i++;
					mannschaftDao.update(m);
				}
				spielDao.update(hf1);
				spielDao.update(hf2);
			}
	    }
	    
	    @Transactional
	    public static void setFI(Collection<Spiel> spiele){
	    	for (Spiel s: spiele){
				Mannschaft mHeim = s.getMannschaftHeim();
				Mannschaft mGast = s.getMannschaftGast();
				switch (s.getBezeichnung()){
					case "hf1":
						if(s.toreheim>s.toregast){;
							mHeim.status="Sieger HF1";
							mGast.status="Verlierer HF1";
						}else if (s.toregast>s.toreheim){
							mGast.status="Sieger HF1";
							mHeim.status="Verlierer HF1";
						}
						mannschaftDao.update(mHeim);
						mannschaftDao.update(mGast);
						break;
						
					case "hf2":
						if(s.toreheim>s.toregast){;
							mHeim.status="Sieger HF2";
							mGast.status="Verlierer HF2";
						}else if (s.toregast>s.toreheim){
							mGast.status="Sieger HF2";
							mHeim.status="Verlierer HF2";
						}
						mannschaftDao.update(mHeim);
						mannschaftDao.update(mGast);
						break;
				}
			}
			
			//Sieger und Verlierer der HalbFinal-Spiele ermitteln
			Mannschaft siegerHF1 = mannschaftDao.findByState("Sieger HF1");
			Mannschaft verliererHF1 = mannschaftDao.findByState("Verlierer HF1");
			Mannschaft siegerHF2 = mannschaftDao.findByState("Sieger HF2");
			Mannschaft verliererHF2 = mannschaftDao.findByState("Verlierer HF2");
			
			//finde Spiel um Platz 3 und Finale
			Spiel sp3 = spielDao.findByBezeichnung("sp3");
			Spiel fi = spielDao.findByBezeichnung("fi");
			
			//setze Spiel um Platz 3 und Finale
			sp3.setVersus(verliererHF1, verliererHF2);
			fi.setVersus(siegerHF1, siegerHF2);
			
			spielDao.update(sp3);
			spielDao.update(fi);
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
//	    	Timestamp now = new Timestamp(System.currentTimeMillis());
//	    	Date date = new Date(System.currentTimeMillis());
	    	Calendar now = Calendar.getInstance();
//	    	now.setTime(date);
	    	return s.beginn.before(now) && s.ende.after(now);
	    }
	    
	    public boolean isGg(Spiel s){
	    	return s.getMannschaftHeim().gruppe.equals("A") || s.getMannschaftHeim().gruppe.equals("B") || s.getMannschaftHeim().gruppe.equals("C") || s.getMannschaftHeim().gruppe.equals("D") || s.getMannschaftHeim().gruppe.equals("E") || s.getMannschaftHeim().gruppe.equals("F") || s.getMannschaftHeim().gruppe.equals("G") || s.getMannschaftHeim().gruppe.equals("H");
	    }
	    
	    public boolean checkAfTippReady(Spiel s){
//	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-06-26 22:00:00.0");
	    	return s.getMannschaftHeim().gruppe.equals("AF") && now.after(rdy);
	    }
	    
	    public boolean checkVfTippReady(Spiel s){
//	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-07-02 00:00:00.0");
	    	return s.getMannschaftHeim().gruppe.equals("VF") && now.after(rdy);
	    }
	    
	    public boolean checkHfTippReady(Spiel s){
//	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-07-06 00:00:00.0");
	    	return s.getMannschaftHeim().gruppe.equals("HF") && now.after(rdy);
	    }
	    
	    public boolean checkFTippReady(Spiel s){
//	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	Calendar now = Calendar.getInstance();
	    	Timestamp rdy = Timestamp.valueOf("2014-07-10 00:00:00.0");
	    	if(s.getMannschaftHeim().gruppe.equals("FI") || s.getMannschaftHeim().gruppe.equals("SP3")){
	    		return now.after(rdy);
	    	}else{
	    		return false;
	    	}
	    }
}
