package dao;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Trunde;
import models.User;

public class TrundeDaoImpl implements TrundeDao{
	
	private static final EntityManager em = JPA.em();
	
	@Override
	public void persistOrMerge(Trunde tr) {
		if(findAll().contains(tr)){
			em.merge(tr);
		}else{
			em.persist(tr);
		}
	}

	@Override
	public void delete(Trunde tr) {
		em.remove(tr);
	}

	@Override
	public Trunde findById(int trid) {
		return em.find(Trunde.class, trid);
	}

	@Override
	public Collection<Trunde> findAll() {
		Query query = em.createQuery("SELECT tr FROM trunde tr");
        return (Collection<Trunde>) query.getResultList();
	}

	@Override
	public Collection<User> findSortedMember(Trunde tr) {
		String sqlQuery = "SELECT uid, name, pw, punkte, admin FROM user AS u INNER JOIN user_trunde AS utr ON utr.fk_uid=u.uid WHERE fk_trid=? ORDER BY punkte DESC, name ASC";
    	Query q = em.createNativeQuery(sqlQuery, User.class);
    	q.setParameter(1, tr.trid);
    	return q.getResultList();
	}

}
