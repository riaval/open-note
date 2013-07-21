package dao;

import org.hibernate.Query;

import domain.GroupRole;

public class GroupRoleDAOImpl extends DAOImpl<GroupRole> {

	public GroupRole findByRole(String role) {
		Query query = getSession().createQuery("from GroupRole where role = :role");
		query.setString("role", role);
		return findOne(query);
	}

}
