package service;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class SessionService {

	public long openSession(String login, String password, String hostIp) throws Exception{
		HibernateUtil.beginTransaction();
		User user = DAOFactory.getUserDAO().findByLogin(login);
		if(user == null){
			throw new IllegalArgumentException("Wrong login.");
		}
		
		String passwordHash = ServiceUtil.passwordHash(password);

		if(!user.getPasswordHash().equals(passwordHash)){
			throw new IllegalArgumentException("Wrong password.");
		}
		
		Session session = new Session(hostIp);
		session.setUser(user);
		
		
		DAOFactory.getSessionDAO().save(session);
		HibernateUtil.commitTransaction();
		
		return session.getId();
	}
	
}
