package dao;

import domain.GroupRole;

public interface GroupRoleDAO extends DAO<GroupRole> {

	public GroupRole findByRole(String role);

}
