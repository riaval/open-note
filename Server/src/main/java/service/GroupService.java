package service;

import java.util.Set;

import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.GroupRole;
import domain.Session;
import domain.User;

public class GroupService {

	public void createGroup(String slug, String name, String sessionHash) throws Exception{
		if(slug.length() < 4){
			throw new IllegalArgumentException("Slug length < 6 characters.");
		} 
		
		HibernateUtil.beginTransaction();		// ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		Group group = new Group(slug, name);
		GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("creator");
		HibernateUtil.commitTransaction();		// <----
		
		HibernateUtil.beginTransaction();		// ---->
		user.getGroups().put(group, groupRole);
		DAOFactory.getGroupDAO().save(group);
		DAOFactory.getUserDAO().save(user);
		HibernateUtil.commitTransaction();		// <----
	}
	
	public Set<Group> getGroups(String sessionHash){
		HibernateUtil.beginTransaction();		// ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		HibernateUtil.commitTransaction();		// <----
		
		Set<Group> groups = user.getGroups().keySet();
		
		for(Group each : groups)
			System.out.println(each.getSlug());

		return groups;
	}
	
}
