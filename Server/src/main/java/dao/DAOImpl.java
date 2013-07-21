package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public abstract class DAOImpl<T> implements DAO<T> {

	protected Session getSession() {
		return HibernateUtil.getSession();
	}

	public void save(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.saveOrUpdate(entity);
	}

	public void merge(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.merge(entity);
	}

	public void delete(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> findMany(Query query) {
		List<T> t;
		t = (List<T>) query.list();
		return t;
	}

	@SuppressWarnings("unchecked")
	public T findOne(Query query) {
		T t;
		t = (T) query.uniqueResult();
		return t;
	}

	@SuppressWarnings("unchecked")
	public T findByID(Class<T> clazz, long id) {
		Session hibernateSession = this.getSession();
		T t = null;
		t = (T) hibernateSession.get(clazz, id);
		return t;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> clazz) {
		Session hibernateSession = this.getSession();
		List<T> T = null;
		Query query = hibernateSession.createQuery("from " + clazz.getName());
		T = query.list();
		return T;
	}

}

// private static final Logger log = Logger.getAnonymousLogger();
// private static final ThreadLocal session = new ThreadLocal();
// private static final SessionFactory sessionFactory = new
// AnnotationConfiguration().configure().buildSessionFactory();
//
// protected DAO() {
// }
//
// public static Session getSession() {
// Session session = (Session) DAO.session.get();
// if (session == null) {
// session = sessionFactory.openSession();
// DAO.session.set(session);
// }
// return session;
// }
//
// protected void begin() {
// getSession().beginTransaction();
// }
//
// protected void commit() {
// getSession().getTransaction().commit();
// }
//
// protected void rollback() {
// try {
// getSession().getTransaction().rollback();
// } catch (HibernateException e) {
// log.log(Level.WARNING, "Cannot rollback", e);
// }
// try {
// getSession().close();
// } catch (HibernateException e) {
// log.log(Level.WARNING, "Cannot close", e);
// }
// DAO.session.set(null);
// }
//
// public static void close() {
// getSession().close();
// DAO.session.set(null);
// }
// }
