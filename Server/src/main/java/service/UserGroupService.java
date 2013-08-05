package service;

import java.util.Set;

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
		User user = session.getUser();
		Set<Invite> invites = user.getInvites();
		for(Invite each : invites){
			UserGroup usergroup = each.getUserGroup();
			String inviteUserLogin = each.getUser().getLogin();
			if (inviteUserLogin.equals(user.getLogin())){
				Group group = DAOFactory.getGroupDAO().findBySlug(groupSlug);
				GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("member");
				UserGroup userGroup = new UserGroup(user, group, groupRole);
				DAOFactory.getUserGroupDAO().save(userGroup);
				HibernateUtil.commitTransaction(); // <----
				return;
			}
		}
		HibernateUtil.commitTransaction(); // <----
		throw new IllegalArgumentException();
	}
	
}
