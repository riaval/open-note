package service;

import java.util.List;

import domain.Session;
import domain.User;

public interface UserService {

	public Session createUser(String login, String fullName, String password, String hostIp, String hostAgent)
			throws Exception;

	public List<User> getUsers(String sessionHash, String search) throws Exception;

	public User getCurrentUser(String sessionHash) throws Exception;

	public void editSimpleData(String sessionHash, String fullName, String email) throws Exception;

	public void editPassword(String sessionHash, String oldPassword, String newPassword) throws Exception;

}
