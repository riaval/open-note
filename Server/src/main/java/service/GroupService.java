package service;

import java.util.HashSet;
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
		if (slug.length() < 4) {
			throw new IllegalArgumentException("Slug length < 4 characters.");
		}
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		User user = session.getUser();
		Group group = new Group(slug, name);
		GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");
		UserGroup userGroup = new UserGroup(user, group, groupRole);
		DAOFactory.getGroupDAO().save(group);
		DAOFactory.getUserGroupDAO().save(userGroup);
		HibernateUtil.commitTransaction(); // <----
	}

	public Set<UserGroup> getGroups(String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		User user = session.getUser();
		Set<UserGroup> userGroups = user.getUserGroups();
		
//		Set<Group> groups = new HashSet<Group>();
//		for (UserGroup each : userGroups) {
//			groups.add(each.getGroup());
//		}		
		HibernateUtil.commitTransaction(); // <---
		HibernateUtil.closeSession();

		return userGroups;
	}

	public void deleteGroup(String slug, String sessionHash) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		User user = session.getUser();
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		if(user == null || group == null){
			HibernateUtil.commitTransaction(); // <---
			throw new BadAuthenticationException();
		}
		GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");
		Set<UserGroup> useGroup = group.getUserGroup();
		for(UserGroup each : useGroup){
			if (each.getUser().getLogin().equals(user.getLogin()) && each.getGroupRole().equals(groupRole)){
				DAOFactory.getGroupDAO().delete(group);
				HibernateUtil.commitTransaction(); // <---
				return;
			}
		}
		throw new BadAuthenticationException("no rights");
	}
	
}
