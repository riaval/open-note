package dao;

import java.util.List;

import org.hibernate.Query;

public interface DAO<T> {

	public void save(T entity);

	public void merge(T entity);

	public void delete(T entity);

	public List<T> findMany(Query query);

	public T findOne(Query query);

	public List<T> findAll(Class<T> clazz);

	public T findByID(Class<T> clazz, long id);

}
