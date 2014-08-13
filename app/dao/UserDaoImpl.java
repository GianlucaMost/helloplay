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


public class UserDaoImpl extends GenericDaoImpl<Integer, User> implements UserDao {
	@Override
	public void add(String name, String pwHash) {
		User user = new User(name, pwHash);
		persist(user);
	}

	@Override
	public void delete(User u) {
		EntityManager em = JPA.em();
		em.remove(u);
	}

	@Override
	public User findByName(String name) {
		EntityManager em = JPA.em();
		if (userExist(name)) {
    		Query query = em.createQuery("SELECT u FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	return (User) query.getSingleResult();
    	}else {
    		return null;
    	}
	}

	@Override
	public Collection<User> findAll() {
		EntityManager em = JPA.em();
		Query query = em.createQuery("SELECT u FROM User u");
        return (Collection<User>) query.getResultList();
	}
	
	@Override
	public boolean userExist(String name) {
		EntityManager em = JPA.em();
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
		EntityManager em = JPA.em();
		String sqlQuery = "SELECT t.* FROM tipp AS t INNER JOIN spiel AS s ON s.sid=t.fk_sid WHERE t.fk_uid=? ORDER BY beginn";
    	Query q = em.createNativeQuery(sqlQuery, Tipp.class);
    	q.setParameter(1, u.uid);
    	return q.getResultList();
	}

	@Override
	public void update(User u, String name, String pwHash) {
		EntityManager em = JPA.em();
		User user = em.find(User.class, u.uid);
    	user.name = name;
    	user.pw = pwHash;
    	update(user);
	}

	@Override
	public void changeName(User u, String name) {
		u.name = name;
		update(u);
	}

	@Override
	public void changePw(User u, String pwHash) {
		u.pw = pwHash;
		update(u);
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
