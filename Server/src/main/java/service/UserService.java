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
		HibernateUtil.commitTransaction(); // ---->

		return session;
	}
	
	public List<User> getUsers(String sessionHash, String login, String fullName) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		if(user == null){
			HibernateUtil.commitTransaction(); // ---->
			throw new BadAuthenticationException();
		}
		List<User> users = DAOFactory.getUserDAO().findUsers(login, fullName);
		HibernateUtil.commitTransaction(); // ---->
		return users;
	}

}
