package dao;

import java.util.Collection;

import models.Tipp;

public interface TippDao {

    public void persistOrMerge(Tipp t);

	public void delete(Tipp t);

	public Tipp findById(int tid);
    
    public Collection<Tipp> findAll();
    
    public void update(Tipp t, byte th, byte tg);
}
