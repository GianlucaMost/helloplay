package dao;

import java.util.Collection;

import models.Mannschaft;
import models.Trunde;
import models.User;

public interface TrundeDao extends GenericDao<Integer, Trunde> {

	public void delete(Trunde tr);
    
    public Collection<Trunde> findAll();
    
    public Collection<User> findSortedMember(Trunde tr);
}
