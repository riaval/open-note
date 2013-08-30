package dao.implementation;

import org.hibernate.Query;

import dao.GroupDAO;
import domain.Group;

public class GroupDAOImpl extends DAOImpl<Group> implements GroupDAO {

	public Group findBySlug(String slug) {
		Query query = getSession().createQuery("from Group where slug = :slug");
		query.setString("slug", slug);
		return findOne(query);
	}

}
