package service;

import java.util.List;

import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class UserService {

	public Session createUser(String login, String fullName, String password, String hostIp, String hostAgent) throws Exception {
		if (login.length() < 4) {
			throw new IllegalArgumentException("Login length < 4 characters.");
		}
		if (password.length() < 6) {
			throw new IllegalArgumentException("Password length < 6 characters.");
		}

		String passwordHash = ServiceUtil.getSaltMD5(password);
		String clientHash = ServiceUtil.getSaltMD5(login + hostIp + hostAgent);
		User user = new User(login, fullName, passwordHash);
		Session session = new Session(clientHash);
		session.setUser(user);

		HibernateUtil.beginTransaction(); // ---->
		DAOFactory.getUserDAO().save(user);
		DAOFactory.getSessionDAO().save(session);
		HibernateUtil.commitTransaction(); // <----

		return session;
	}
	
	public List<User> getUsers(String sessionHash, String search) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		User user = session.getUser();
		List<User> users = DAOFactory.getUserDAO().findUsers(search);
		for (int i=0; i<users.size(); i++){
			if (users.get(i).equals(user)){
				users.remove(i);
			}
		}
		HibernateUtil.commitTransaction(); // <----
		return users;
	}

}
