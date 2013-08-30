package dao.implementation;

import org.hibernate.Query;

import dao.SessionDAO;
import domain.Session;

public class SessionDAOImpl extends DAOImpl<Session> implements SessionDAO {

	public Session findByHash(String hash) {
		Query query = getSession().createQuery("from Session where hash = :hash");
		query.setString("hash", hash);
		return findOne(query);
	}

}
