package service;

import java.util.List;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class SessionService {

	public String openSession(String login, String password, String hostIp, String hostAgent) throws Exception{
		HibernateUtil.beginTransaction();
		
		User user = DAOFactory.getUserDAO().findByLogin(login);
		if(user == null){
			throw new IllegalArgumentException("Wrong login.");
		}
		
		String passwordHash = ServiceUtil.getSaltMD5(password);
		if(!user.getPasswordHash().equals(passwordHash)){
			throw new IllegalArgumentException("Wrong password.");
		}
		
		String clientHash = ServiceUtil.getSaltMD5(hostIp + hostAgent);
		Session session = DAOFactory.getSessionDAO().findByUserIDAndHash(user.getId(), clientHash);
		if(!(session == null)){
			return session.getHash();
		}
		
		session = new Session(clientHash);
		session.setUser(user);
		
		
		DAOFactory.getSessionDAO().save(session);
		HibernateUtil.commitTransaction();
		
		return session.getHash();
	}
	
}
