package dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import models.Mannschaft;
import models.Spiel;

public interface MannschaftDao extends GenericDao<Integer, Mannschaft>{
    public Mannschaft findByName(String bezeichnung);
    public Mannschaft findByState(String status);
    public Map<String, List<Mannschaft>> findAll();
	public Collection<Mannschaft> findAllCol();
    public List<Mannschaft> findByGroup(String grp);
    public List<Spiel> findGamesSorted(Mannschaft m);
}
