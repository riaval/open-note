package service;

import domain.Session;

public interface SessionService {

	public Session openSession(String login, String password, String hostIp, String hostAgent) throws Exception;

}
