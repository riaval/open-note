package service;

import java.util.Set;

import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.GroupRole;
import domain.Session;
import domain.User;
import domain.UserGroup;

public class GroupService {

	public void createGroup(String slug, String name, String sessionHash) throws Exception {
		if (slug.length() < 4 || slug.length() > 15) {
			throw new IllegalArgumentException("Goup short name must be between 6 and 15 characters long.");
		}
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("Goup name is empty.");
		}
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			Group currentGroup = DAOFactory.getGroupDAO().findBySlug(slug);
			if (currentGroup != null){
				throw new IllegalArgumentException("A group with this short name already exists.");
			}
			User user = session.getUser();
			Group newGroup = new Group(slug, name);
			GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");
			UserGroup userGroup = new UserGroup(user, newGroup, groupRole);
			DAOFactory.getGroupDAO().save(newGroup);
			DAOFactory.getUserGroupDAO().save(userGroup);
			HibernateUtil.commitTransaction(); // <----
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Set<UserGroup> getGroups(String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			User user = session.getUser();
			Set<UserGroup> userGroups = user.getUserGroups();

			HibernateUtil.commitTransaction(); // <---

			return userGroups;
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void deleteGroup(String slug, String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				HibernateUtil.commitTransaction(); // <----
				throw new BadAuthenticationException("Session is empty");
			}
			User user = session.getUser();
			Group group = DAOFactory.getGroupDAO().findBySlug(slug);
			if (user == null || group == null) {
				HibernateUtil.commitTransaction(); // <---
				throw new BadAuthenticationException();
			}
			GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");
			Set<UserGroup> useGroup = group.getUserGroup();
			for (UserGroup each : useGroup) {
				if (each.getUser().getLogin().equals(user.getLogin()) && each.getGroupRole().equals(groupRole)) {
					DAOFactory.getGroupDAO().delete(group);
					HibernateUtil.commitTransaction(); // <---
					return;
				}
			}
			throw new BadAuthenticationException("no rights");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
