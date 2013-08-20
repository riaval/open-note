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
			throw new IllegalArgumentException("Wrong slug length");
		}
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			User user = session.getUser();
			Group group = new Group(slug, name);
			GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");
			UserGroup userGroup = new UserGroup(user, group, groupRole);
			DAOFactory.getGroupDAO().save(group);
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
