package dao;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Tipp;
import models.User;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.*;


public class UserDaoImpl implements UserDao {
	
	private static final EntityManager em = JPA.em();
	
	@Override
	public void persistOrMerge(User u) {
		if(findAll().contains(u)){
			em.merge(u);
		}else{
			em.persist(u);
		}
	}

	@Override
	public void add(String name, String pwHash) {
		User user = new User();
		user.name=name;
		user.pw=pwHash;
		persistOrMerge(user);
	}

	@Override
	public void delete(User u) {
		em.remove(u);
	}

	@Override
	public User findById(int id) {
		return em.find(User.class, id);
	}

	@Override
	public User findByName(String name) {
		if (User.userExist(name)) {
    		Query query = em.createQuery("SELECT u FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	return (User) query.getSingleResult();
    	}else {
    		return null;
    	}
	}

	@Override
	public Collection<User> findAll() {
		Query query = em.createQuery("SELECT u FROM User u");
        return (Collection<User>) query.getResultList();
	}
	
	@Override
	public boolean userExist(String name) {
		try {
	    	Query query = em.createQuery("SELECT u.name FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	String tmp = (String) query.getSingleResult();
	    	return true;
    	} catch (NoResultException ex) {
    		return false;
    	}
	}

	@Override
	public Collection<Tipp> findSortedTipps(User u) {
		String sqlQuery = "SELECT t.* FROM tipp AS t INNER JOIN spiel AS s ON s.sid=t.fk_sid WHERE t.fk_uid=? ORDER BY beginn";
    	Query q = em.createNativeQuery(sqlQuery, Tipp.class);
    	q.setParameter(1, u.uid);
    	return q.getResultList();
	}

	@Override
	public void update(User u, String name, String pwHash) {
		User user = em.find(User.class, u.uid);
    	user.name = name;
    	user.pw = pwHash;
    	persistOrMerge(user);
	}

	@Override
	public void changeName(User u, String name) {
		u.name = name;
		persistOrMerge(u);
	}

	@Override
	public void changePw(User u, String pwHash) {
		u.pw = pwHash;
		persistOrMerge(u);
	}

	@Override
	public boolean validate(String name, String pw) {
		try {
	    	Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	User user = (User) query.getSingleResult();
	    	return BCrypt.checkpw(pw, user.pw);
    	} catch (NoResultException ex) {
    		return false;
    	}
	}

}
