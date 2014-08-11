package dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import models.Mannschaft;
import models.Spiel;

public interface SpielDao extends GenericDao<Integer, Spiel> {
	
	Collection<Spiel> findAll();
	
	//---
	
    Spiel findByBezeichnung(String b);
    
    Spiel findGroupGame(Mannschaft mh, Mannschaft mg);
    
    Spiel findGame(Mannschaft mh, Mannschaft mg);
    
    Collection<Spiel> gamesOfTeam(int mid);
    
    void setErgebnis(Spiel s, byte th, byte tg) throws Throwable;

}
