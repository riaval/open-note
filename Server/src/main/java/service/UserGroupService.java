package service;

import java.util.Set;

import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.Session;
import domain.User;
import domain.UserGroup;


public class UserGroupService {

	public void deleteUserFromGroup(String sessionHash, String groupSlug) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		Group group = DAOFactory.getGroupDAO().findBySlug(groupSlug);
		if(group == null){
			HibernateUtil.commitTransaction(); // <----
			throw new IllegalArgumentException("Group does not exist");
		}
		User user = session.getUser();
		Set<UserGroup> userGroups = user.getUserGroups();
		for(UserGroup each :userGroups){
			if (each.getGroup().getSlug().equals(groupSlug)){
				if(each.getGroupRole().getRole().equals("member")){
					DAOFactory.getUserGroupDAO().delete(each);
					HibernateUtil.commitTransaction(); // <----
					return;
				}
				HibernateUtil.commitTransaction(); // <----
				throw new IllegalArgumentException("not member");
			}
		}
		HibernateUtil.commitTransaction(); // <----
		throw new IllegalArgumentException("No rules");
	}
	
}
