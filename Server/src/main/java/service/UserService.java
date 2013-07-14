package service;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class UserService {

	public long createUser(String login, String fullName, String password, String email, String hostIp) throws Exception {
		if(login.length() < 6){
			throw new IllegalArgumentException("Login length < 6 characters.");
		}
		if(password.length() < 6){
			throw new IllegalArgumentException("Password length < 6 characters.");
		}
		
		String passwordHash = ServiceUtil.passwordHash(password);
		User user = new User(
				  login
				, fullName
				, passwordHash
				, email
		);
		Session session = new Session(hostIp);
		session.setUser(user);
		
		HibernateUtil.beginTransaction();
		DAOFactory.getUserDAO().save(user);
		DAOFactory.getSessionDAO().save(session);
		HibernateUtil.commitTransaction();
		
		return session.getId();
	}

}
