package dao;

import java.util.Collection;

import javax.persistence.EntityManager;

import models.Mannschaft;
import models.Spiel;
import models.Tipp;
import play.db.jpa.JPA;

import javax.persistence.Query;

public class SpielDaoImpl extends GenericDaoImpl<Integer, Spiel> implements SpielDao {
	/**
	 * return  Collection of all games
	 */
	@Override
	public Collection<Spiel> findAll() {
		EntityManager em = JPA.em();
		Query query = em.createQuery("SELECT s FROM Spiel s ORDER BY s.beginn");
        return (Collection<Spiel>) query.getResultList();
    }
	
	/**
	 * return the game with the given designation, if not found return null
	 */
	@Override
	public Spiel findByBezeichnung(String b) {
		for (Spiel s: findAll()){
    		if(s.getBezeichnung().equals(b)){
    			return s;
    		}
    	}
		return null;
	}

	/**
	 * return the game where team para1(mh) play against team para2(mg)
	 */
	@Override
	public Spiel findGroupGame(Mannschaft mh, Mannschaft mg) {
		EntityManager em = JPA.em();
		Query query = em.createQuery("SELECT s FROM Spiel s WHERE s.mannschaft_heim = :pMh AND s.mannschaft_gast = :pMg");
    	query.setParameter("pMh", mh);
    	query.setParameter("pMg", mg);
    	return (Spiel) query.getSingleResult();
	}
	
	/**
	 * alternative version of findGroupGame()
	 */
	@Override
	public Spiel findGame(Mannschaft mh, Mannschaft mg) {
		for (Spiel s: findAll()){
			Mannschaft sMh = s.getMannschaftHeim();
			Mannschaft sMg = s.getMannschaftGast();
    		if(sMh.mid==mh.mid && sMg.mid==mg.mid){
    			return s;
    		}
    	}
    	return null;
	}
	
	/**
	 * return a collection of all games of a team
	 */
	@Override
	public Collection<Spiel> gamesOfTeam(int mid) {
		EntityManager em = JPA.em();
		Query query = em.createQuery("SELECT s FROM Spiel s WHERE s.fk_midheim=:pMid OR s.fk_midgast=:pMid");
        query.setParameter("pMid", mid);
        Collection<Spiel> col = query.getResultList();
        return col;	
	}
	
	/**
	 * set the result of the given game(para1) to given goals(para2/3)
	 */
	@Override
	public void setErgebnis(Spiel s, byte th, byte tg) throws Throwable {
		byte thp = s.toreheim;
    	byte tgp = s.toregast;
//    	Mannschaft mh = s.getMannschaftHeim();
//		Mannschaft mg = s.getMannschaftGast();
//		Collection<Spiel> spiele = findAll();
		
    	if (thp!=th || tgp!=tg){
    		s.toreheim = th;
        	s.toregast = tg;
        	update(s);
    	}
	}

	@Override
	public Spiel findVs(Mannschaft a, Mannschaft b) {
		Collection<Spiel> spiele = findAll();
	    for (Spiel s: spiele){
			if(s.getMannschaftHeim().equals(a) && s.getMannschaftHeim().equals(b)){
				return s;
			}
		}
	    return null;
	}
	
	@Override
	public void check(Spiel s) {
		s.checked=1;
	}

}
