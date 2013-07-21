package service;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class UserService {

	public Session createUser(String login, String fullName, String password,
			String hostIp, String hostAgent) throws Exception {
		if (login.length() < 4) {
			throw new IllegalArgumentException("Login length < 4 characters.");
		}
		if (password.length() < 6) {
			throw new IllegalArgumentException(
					"Password length < 6 characters.");
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

}
