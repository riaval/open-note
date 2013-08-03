package service;

import java.util.HashSet;
import java.util.Set;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.GroupRole;
import domain.Session;
import domain.User;
import domain.UserGroup;

public class GroupService {

	public void createGroup(String slug, String name, String sessionHash) throws Exception {
		if (slug.length() < 4) {
			throw new IllegalArgumentException("Slug length < 6 characters.");
		}

		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		Group group = new Group(slug, name);
		GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");

		UserGroup userGroup = new UserGroup(user, group, groupRole);
		DAOFactory.getGroupDAO().save(group);
		DAOFactory.getUserGroupDAO().save(userGroup);
		HibernateUtil.commitTransaction(); // <----

	}

	public Set<Group> getGroups(String sessionHash) {
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		Set<UserGroup> userGroups = user.getUserGroups();
		
		Set<Group> groups = new HashSet<Group>();
		for (UserGroup each : userGroups) {
			groups.add(each.getGroup());
		}		
		HibernateUtil.commitTransaction(); // <---
		HibernateUtil.closeSession();

		return groups;
	}

}
