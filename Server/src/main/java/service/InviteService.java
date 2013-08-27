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

	public void createInvitation(String slug, String login, String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			User userFrom = session.getUser();
			User userTo = DAOFactory.getUserDAO().findByLogin(login);
			if (userFrom.equals(userTo)) {
				throw new IllegalArgumentException("Invitation to oneself.");
			}
			Group group = DAOFactory.getGroupDAO().findBySlug(slug);
			if (group == null || userTo == null) {
				throw new IllegalArgumentException("User or group is null.");
			}

			Set<UserGroup> userToGroups = userTo.getUserGroups();
			for (UserGroup each : userToGroups) {
				Group eachGroup = each.getGroup();
				if (eachGroup.equals(group)){
					throw new IllegalArgumentException("User is already accepted an invitation.");
				}
			}
			
			Set<UserGroup> userFromGroups = userFrom.getUserGroups();
			for (UserGroup each : userFromGroups) {
				if (each.getGroup().equals(group)) {
					GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("member");
					Invite invite = new Invite(userTo, each, groupRole);

					Set<Invite> invitations = userTo.getInvites();
					for (Invite eachInvitation : invitations) {
						if (eachInvitation.getUserGroup().equals(each)) {
							throw new IllegalArgumentException("Invitation is already created.");
						}
					}

					DAOFactory.getInviteDAO().save(invite);
					HibernateUtil.commitTransaction(); // <----
					return;
				}
			}
			throw new IllegalArgumentException("No rules");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Set<Invite> getInvitations(String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				HibernateUtil.commitTransaction(); // <----
				throw new BadAuthenticationException("Session is empty");
			}
			User user = session.getUser();
			Set<Invite> invites = user.getInvites();
			HibernateUtil.commitTransaction(); // <----

			return invites;
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void deleteInvitation(String sessionHash, long id) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			User sessionUser = session.getUser();
			Invite invite = DAOFactory.getInviteDAO().findByID(Invite.class, id);
			if (invite == null) {
				throw new IllegalArgumentException("No invitation found");
			}
			User inviteUser = invite.getUser();
			User inviteAuthor = invite.getUserGroup().getUser();
			if (sessionUser.equals(inviteUser) || sessionUser.equals(inviteAuthor)) {
				DAOFactory.getInviteDAO().delete(invite);
				HibernateUtil.commitTransaction(); // <----
				return;
			}
			throw new IllegalArgumentException("No rules");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void acceptInvitation(String sessionHash, String groupSlug) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty");
			}
			Group group = DAOFactory.getGroupDAO().findBySlug(groupSlug);
			if (group == null) {
				throw new IllegalArgumentException("Group does not exist");
			}
			User user = session.getUser();
			Set<Invite> invites = user.getInvites();
			for (Invite each : invites) {
				UserGroup userGroup = each.getUserGroup();
				Group invitedGroup = userGroup.getGroup();
				if (invitedGroup.equals(group)) {
					GroupRole groupRole = DAOFactory.getGroupRoleDAO().findByRole("member");
					UserGroup newUserGroup = new UserGroup(user, group, groupRole);
					DAOFactory.getUserGroupDAO().save(newUserGroup);
					// Delete invitation
					DAOFactory.getInviteDAO().delete(each);
					HibernateUtil.commitTransaction(); // <----
					return;
				}
			}
			throw new IllegalArgumentException("No rules");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
