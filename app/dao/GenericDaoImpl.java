package dao;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class GenericDaoImpl<K,E> implements GenericDao<K, E> {
	
//	final Class<E> typeParameterClass;
	
//	private Class entityBeanType;
	protected Class<E> entityClass;
	
//	protected Class<E> entityClass;
//	protected EntityManager em = JPA.em();
	
//	public GenericDaoImpl(Class<E> typeParameterClass){
//		this.typeParameterClass = typeParameterClass;
//	}
	
//	public GenericDaoImpl(){
//		this.entityBeanType = ((Class) (ParametreizedType) getClass().getGenericSuperclass().getActualTypeArguments()[0]);
//	}
	
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
