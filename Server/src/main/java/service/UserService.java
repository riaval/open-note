package service;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class UserService {

	public Session createUser(String login, String fullName, String password, String hostIp, String hostAgent) throws Exception {
		if (login == null || login.length() < 4 || login.length() > 15) {
			throw new IllegalArgumentException("Login must be between 6 and 15 characters long.");
		}
		if (password == null || password.length() < 6) {
			throw new IllegalArgumentException("Password must be at least 6 characters long.");
		}
		if (password.length() > 64) {
			throw new IllegalArgumentException("Password is too long.");
		}
		if (fullName == null || fullName.length() == 0){
			throw new IllegalArgumentException("Full name field is required.");
		}

		String passwordHash = ServiceUtil.getSaltMD5(password);
		String clientHash = ServiceUtil.getSaltMD5(login + hostIp + hostAgent);
		Date date = Calendar.getInstance().getTime();
		int color = ServiceUtil.generateRandomColor(new Color(255, 255, 255)).getRGB();
		User user = new User(login, fullName, passwordHash, date, color);
		Session session = new Session(clientHash);
		session.setUser(user);

		HibernateUtil.beginTransaction(); // ---->
		try {
			User defUser = DAOFactory.getUserDAO().findByLogin(login);
			if (defUser != null) {
				throw new IllegalArgumentException("An account with this login already exists.");
			}
			
			DAOFactory.getUserDAO().save(user);
			DAOFactory.getSessionDAO().save(session);
			HibernateUtil.commitTransaction(); // <----
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

		return session;
	}

	public List<User> getUsers(String sessionHash, String search) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			User user = session.getUser();
			List<User> users = DAOFactory.getUserDAO().findUsers(search);
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).equals(user)) {
					users.remove(i);
				}
			}
			HibernateUtil.commitTransaction(); // <----

			return users;
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public User getCurrentUser(String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			User user = session.getUser();

			return user;
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	public void editSimpleData(String sessionHash, String fullName, String email) throws Exception {
		if (fullName == null || fullName.length() == 0){
			throw new IllegalArgumentException("Full name field is required.");
		}
		
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			User user = session.getUser();
			user.setFullName(fullName);
			user.setEmail(email);
			
			DAOFactory.getUserDAO().save(user);
			HibernateUtil.commitTransaction(); // <----
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	public void editPassword(String sessionHash, String oldPassword, String newPassword) throws Exception {
		if (newPassword == null || newPassword.length() < 6) {
			throw new IllegalArgumentException("Password must be at least 6 characters long.");
		}
		if (newPassword.length() > 64) {
			throw new IllegalArgumentException("Password is too long.");
		}
		
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			User user = session.getUser();
			
			String userPassHash = user.getPasswordHash();
			String requestPassHash = ServiceUtil.getSaltMD5(oldPassword);
			
			if (!userPassHash.equals(requestPassHash)) {
				throw new IllegalArgumentException("Wrong old password.");
			}
			
			user.setPasswordHash(ServiceUtil.getSaltMD5(newPassword));
			
			DAOFactory.getUserDAO().save(user);
			HibernateUtil.commitTransaction(); // <----
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
