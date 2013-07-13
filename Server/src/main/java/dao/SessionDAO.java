package dao;

import org.hibernate.HibernateException;

import domain.Session;
import domain.User;

public class SessionDAO extends DAO {

	public void createSession(Session session) throws Exception {
//        try {
//            begin();
            getSession().save(session);
//            commit();
//        } catch (HibernateException e) {
//            rollback();
//            throw new Exception("Could not create session for ip " + session.getIp(), e);
//        }
    }
	
	public void addUser(User user){
		
	}
	
}
