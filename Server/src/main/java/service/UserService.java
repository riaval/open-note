package service;

import domain.Session;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.DAO;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.User;

public class UserService {

	public void createUser(String login, String fullName, String password, String email, String hostIp) throws Exception{
		if(login.length() < 6){
			throw new IllegalArgumentException("Login length < 6 characters.");
		}

		
		User user = new User(login, fullName, password, email);
		Session session = new Session(hostIp);
		
//		open session
		DAO.begin();
		sess.beginTransaction();
		
		DAOFactory.getUserDAO().createUser(user);
		DAOFactory.getSessionDAO().addUser(user);
		
		sess.getTransaction().commit();
		

	}
	
	public User getUser(String login) throws Exception{
		return DAOFactory.getUserDAO().retrieveUser(login);
	}
	
	public void editUser(User user){
		
	}
	
	public void deleteUser(String login) throws Exception{
		DAOFactory.getUserDAO().deleteUserByLogin(login);
	}

}
