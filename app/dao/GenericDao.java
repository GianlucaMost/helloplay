package dao;

public interface GenericDao<K,E> {
	public void persist(E e);
	public void update(E e);
	public E findById(K key);
}
