package dao;

import java.util.Collection;

import models.Trunde;
import models.User;

public interface TrundeDao {
	
    public void persistOrMerge(Trunde tr);

	public void delete(Trunde tr);

	public Trunde findById(int trid);
    
    public Collection<Trunde> findAll();
    
    public Collection<User> findSortedMember(Trunde tr);
}
