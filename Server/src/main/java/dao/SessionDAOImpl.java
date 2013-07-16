package dao;

import org.hibernate.Query;

import domain.Session;

public class SessionDAOImpl extends DAOImpl<Session> {
	
	public Session findByUserIDAndHash(long id, String hash){
		Query query = getSession().createQuery("from Session where user = :id and hash = :hash");
		query.setLong("id", id);
		query.setString("hash", hash);
		return findOne(query);
	}
	
}
