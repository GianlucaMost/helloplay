package dao;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Tipp;
import play.db.jpa.JPA;

public class TippDaoImpl extends GenericDao<Integer, Tipp> implements TippDao{
	
	@Override
	public void delete(Tipp t) {
		em.remove(t);
	}

	@Override
	public Collection<Tipp> findAll() {
		Query query = em.createQuery("SELECT t FROM Tipp t");
        return (Collection<Tipp>) query.getResultList();
	}

	@Override
	public void update(Tipp t, byte th, byte tg) {
		t.toreheim=th;
    	t.toregast=tg;
		update(t);
	}
	
}
