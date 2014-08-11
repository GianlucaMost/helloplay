package dao;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class GenericDaoImpl<K,E> implements GenericDao<K, E> {
	
	protected Class<E> entityClass;
	protected final EntityManager em = JPA.em();
	
	public void persist(E e) {
		em.persist(e);
	}
	
	public void update(E e) {
		em.merge(e);
	}
	
	public E findById(K key) {
		return em.find(entityClass, key);
	}

}
