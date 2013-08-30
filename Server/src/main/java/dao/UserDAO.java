package dao;

import java.util.List;

import domain.User;

public interface UserDAO extends DAO<User> {

	public User findByLogin(String login);

	public List<User> findUsers(String search);

}
