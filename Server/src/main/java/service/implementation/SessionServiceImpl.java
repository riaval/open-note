package service.implementation;

import service.ServiceUtil;
import service.SessionService;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Session;
import domain.User;

public class SessionServiceImpl implements SessionService {

	public Session openSession(String login, String password, String hostIp, String hostAgent) throws Exception {
		String deffErrorMessage = "Login or password incorrect.";
		HibernateUtil.beginTransaction(); // ---->
		try {
			User user = DAOFactory.getUserDAO().findByLogin(login);
			if (user == null) {
				throw new IllegalArgumentException(deffErrorMessage);
			}
			String passwordHash = ServiceUtil.getSaltMD5(password);
			if (!user.getPasswordHash().equals(passwordHash)) {
				throw new IllegalArgumentException(deffErrorMessage);
			}

			String clientHash = ServiceUtil.getSaltMD5(login + hostIp + hostAgent);
			Session session = DAOFactory.getSessionDAO().findByHash(clientHash);
			if (!(session == null)) {
				HibernateUtil.commitTransaction(); // <----
				return session;
			}

			session = new Session(clientHash);
			session.setUser(user);

			DAOFactory.getSessionDAO().save(session);
			HibernateUtil.commitTransaction(); // <----
			return session;
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
