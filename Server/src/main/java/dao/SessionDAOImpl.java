package dao;

import org.hibernate.Query;

import domain.Session;

public class SessionDAOImpl extends DAOImpl<Session> {
	
	public Session findByHash(String hash){
		Query query = getSession().createQuery("from Session where hash = :hash");
		query.setString("hash", hash);
		return findOne(query);
	}
	
}
