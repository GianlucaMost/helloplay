package dao;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Tipp;
import play.db.jpa.JPA;

public class TippDaoImpl implements TippDao{
	
	private static final EntityManager em = JPA.em();

	/**
	 * persist a new object or update/merge an old one.
	 */
	@Override
	public void persistOrMerge(Tipp t) {
		if(findAll().contains(t)){
			em.merge(t);
		}else{
			em.persist(t);
		}
	}

	@Override
	public void delete(Tipp t) {
		em.remove(t);
	}

	@Override
	public Tipp findById(int tid) {
		return em.find(Tipp.class, tid);
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
		persistOrMerge(t);
	}
	
}
