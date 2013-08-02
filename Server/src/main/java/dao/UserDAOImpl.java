package dao;

import java.util.List;

import org.hibernate.Query;

import domain.User;

public class UserDAOImpl extends DAOImpl<User>{
	
	public User findByLogin(String login){
		Query query = getSession().createQuery("from User where login = :login");
		query.setString("login", login);
		return findOne(query);
	}
	
	public List<User> findUsers(String login, String fullName){
		Query query = getSession().createQuery("from User where login LIKE CONCAT('%', :login, '%') and lower(fullName) LIKE CONCAT('%', :fullName, '%')");
		query.setString("login", login);
		query.setString("fullName", fullName);
		return findMany(query);
	}
	
}
