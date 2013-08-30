package dao.implementation;

import org.hibernate.Query;

import dao.GroupRoleDAO;
import domain.GroupRole;

public class GroupRoleDAOImpl extends DAOImpl<GroupRole> implements GroupRoleDAO {

	public GroupRole findByRole(String role) {
		Query query = getSession().createQuery("from GroupRole where role = :role");
		query.setString("role", role);
		return findOne(query);
	}

}
