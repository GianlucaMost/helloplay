package dao;

import java.util.Collection;

import models.Tipp;

public interface TippDao {

	public void delete(Tipp t);
    
    public Collection<Tipp> findAll();
    
    public void update(Tipp t, byte th, byte tg);
}
