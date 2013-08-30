package dao.implementation;

import java.util.List;

import org.hibernate.Query;

import dao.UserDAO;
import domain.User;

public class UserDAOImpl extends DAOImpl<User> implements UserDAO {

	public User findByLogin(String login) {
		Query query = getSession().createQuery("from User where login = :login");
		query.setString("login", login);
		return findOne(query);
	}

	public List<User> findUsers(String search) {
		Query query = getSession()
				.createQuery(
						"from User where login LIKE CONCAT('%', :search, '%') or lower(fullName) LIKE CONCAT('%', :search, '%')");
		query.setString("search", search);
		return findMany(query);
	}

}
