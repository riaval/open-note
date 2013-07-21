package service;

import java.util.Set;

import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.GroupRole;
import domain.Invite;
import domain.Session;
import domain.User;
import domain.UserGroup;

public class InviteService {
	
	public void createInvite(String slug, String login, String sessionHash) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User userFrom = session.getUser();
		User userTo = DAOFactory.getUserDAO().findByLogin(login);
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		Set<UserGroup> userGroups = userFrom.getUserGroups();
		HibernateUtil.commitTransaction(); // <----
		
		if(userFrom == null || group == null || userTo == null)
			throw new IllegalArgumentException("users or group is null");
		
		UserGroup userGroup = null;
		for (UserGroup each : userGroups) {
			if (each.getGroup().equals(group)){
	        	userGroup = each;
	        	break;
	        }
	    }
		if(userGroup == null)
			throw new BadAuthenticationException("Bad Authentication data");
		
		HibernateUtil.beginTransaction(); // ---->
		GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("member");
		Invite invite = new Invite(userTo, userGroup, groupRole);
		invite.setUserGroup(userGroup);
		DAOFactory.getInviteDAO().save(invite);
		HibernateUtil.commitTransaction(); // <----
	}
	
	public Set<Invite> getInvites(String sessionHash){
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		Set<Invite> invites = user.getInvites();
		
		for (Invite each : invites) {
			System.out.println(each.getUser().getLogin());
	    }
		
		HibernateUtil.commitTransaction(); // <----
		
		
		
		return invites;
	}
	
}
