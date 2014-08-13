package dao;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class GenericDaoImpl<K,E> implements GenericDao<K, E> {

	protected Class<E> entityClass;
	
	public GenericDaoImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[1];
	}
	
	public void persist(E e) {
		EntityManager em = JPA.em();
		em.persist(e);
	}
	
	public void update(E e) {
		EntityManager em = JPA.em();
		em.merge(e);
	}
	
	public E findById(K key) {
		EntityManager em = JPA.em();
		return em.find(entityClass, key);
	}

}
