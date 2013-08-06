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

public class UserGroupService {

	public void addUserToGroup(String sessionHash, String groupSlug) throws Exception {
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
		Set<Invite> invites = user.getInvites();
		for(Invite each : invites){ 
			UserGroup userGroup = each.getUserGroup();
			Group invitedGroup = userGroup.getGroup();
			if(invitedGroup.equals(group)){
				GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("member");
				UserGroup newUserGroup = new UserGroup(user, group, groupRole);
				DAOFactory.getUserGroupDAO().save(newUserGroup);
				HibernateUtil.commitTransaction(); // <----
				return;
			}
		}
		HibernateUtil.commitTransaction(); // <----
		throw new IllegalArgumentException("No rules");
	}
	
}
