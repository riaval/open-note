package dao;

import org.hibernate.Query;

import domain.Group;

public class GroupDAOImpl extends DAOImpl<Group> {

	public Group findBySlug(String slug){
		Query query = getSession().createQuery("from Group where slug = :slug");
		query.setString("slug", slug);
		return findOne(query);
	}
	
}
