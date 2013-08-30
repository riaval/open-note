package dao;

import domain.Session;

public interface SessionDAO extends DAO<Session> {

	public Session findByHash(String hash);

}
