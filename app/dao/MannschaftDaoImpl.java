package dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Mannschaft;
import models.Spiel;
import models.Tipp;

public class MannschaftDaoImpl extends GenericDao<Integer, Mannschaft> implements MannschaftDao{
	
	/**
     * Find a team by name
     * @param bezeichnung
     * @return Mannschaft
     */
	@Override
	public Mannschaft findByName(String bezeichnung) {
		Query query = em.createQuery("SELECT m FROM Mannschaft m WHERE m.bezeichnung = :pBezeichnung");
    	query.setParameter("pBezeichnung", bezeichnung);
    	return (Mannschaft) query.getSingleResult();
	}
	
	/**
     * Find a team by state
     * @param status
     * @return Mannschaft
     */
	@Override
	public Mannschaft findByState(String status) {
		Query query = em.createQuery("SELECT m FROM Mannschaft m WHERE m.status = :pStatus");
    	query.setParameter("pStatus", status);
    	return (Mannschaft) query.getSingleResult();
	}
	
	/**
     * Holt alle Mannschaften aus der db und erstellt eine nach gruppen sortierte treeMap
     * @return
     */
	@Override
		public Map<String, List<Mannschaft>> findAll() {
			String sqlQuery = "SELECT * FROM mannschaft WHERE LENGTH(gruppe)=1";
	    	Query q = em.createNativeQuery(sqlQuery, Mannschaft.class);
	//      Query query = JPA.em().createQuery("SELECT m FROM Mannschaft m WHERE m.mid<=32");
	        Collection<Mannschaft> col = (Collection<Mannschaft>) q.getResultList();
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

	@Override
	public Collection<Mannschaft> findAllCol() {
		Query query = JPA.em().createQuery("SELECT m FROM Mannschaft m");
        return (Collection<Mannschaft>) query.getResultList();
	}
	
	@Override
	public List<Mannschaft> findByGroup(String grp) {
		String sqlQuery = "SELECT * FROM mannschaft WHERE gruppe=? ORDER BY punkte DESC";
    	Query q = em.createNativeQuery(sqlQuery, Mannschaft.class);
    	q.setParameter(1, grp);
    	List<Mannschaft> list = (List<Mannschaft>) q.getResultList();
    	return list;
	}

	@Override
	public List<Spiel> findGamesSorted(Mannschaft m) {
		String sqlQuery = "SELECT s.* FROM spiel AS s INNER JOIN mannschaft AS m ON m.mid=s.fk_midheim OR m.mid=s.fk_midgast WHERE mid=? ORDER BY s.beginn";
    	Query q = JPA.em().createNativeQuery(sqlQuery, Spiel.class);
    	q.setParameter(1, m.mid);
    	List<Spiel> list = (List<Spiel>) q.getResultList();
    	return list;
	}

}
