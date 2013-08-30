package dao;

import domain.Group;

public interface GroupDAO extends DAO<Group> {
	
	public Group findBySlug(String slug);
	
}
