package dao;

import dao.implementation.GroupDAOImpl;
import dao.implementation.GroupRoleDAOImpl;
import dao.implementation.InviteDAOImpl;
import dao.implementation.SessionDAOImpl;
import dao.implementation.SimpleNoteDAOImpl;
import dao.implementation.UserDAOImpl;
import dao.implementation.UserGroupDAOImpl;

public class DAOFactory {

	private static GroupDAOImpl sGroupDAOImpl;
	private static GroupRoleDAOImpl sGroupRoleDAOImpl;
	private static InviteDAOImpl sInviteDAOImpl;
	private static SessionDAOImpl sSessionDAOImpl;
	private static SimpleNoteDAOImpl sSimpleNoteDAOImpl;
	private static UserDAOImpl sUserDAOImpl;
	private static UserGroupDAOImpl sUserGroupDAOImpl;

	public static GroupDAO getGroupDAO() {
		if (sGroupDAOImpl == null) {
			sGroupDAOImpl = new GroupDAOImpl();
		}
		return sGroupDAOImpl;
	}

	public static GroupRoleDAO getGroupRoleDAO() {
		if (sGroupRoleDAOImpl == null) {
			sGroupRoleDAOImpl = new GroupRoleDAOImpl();
		}
		return sGroupRoleDAOImpl;
	}

	public static InviteDAO getInviteDAO() {
		if (sInviteDAOImpl == null) {
			sInviteDAOImpl = new InviteDAOImpl();
		}
		return sInviteDAOImpl;
	}

	public static SessionDAO getSessionDAO() {
		if (sSessionDAOImpl == null) {
			sSessionDAOImpl = new SessionDAOImpl();
		}
		return sSessionDAOImpl;
	}

	public static SimpleNoteDAO getSimpleNoteDAO() {
		if (sSimpleNoteDAOImpl == null) {
			sSimpleNoteDAOImpl = new SimpleNoteDAOImpl();
		}
		return sSimpleNoteDAOImpl;
	}

	public static UserDAO getUserDAO() {
		if (sUserDAOImpl == null) {
			sUserDAOImpl = new UserDAOImpl();
		}
		return sUserDAOImpl;
	}

	public static UserGroupDAO getUserGroupDAO() {
		if (sUserGroupDAOImpl == null) {
			sUserGroupDAOImpl = new UserGroupDAOImpl();
		}
		return sUserGroupDAOImpl;
	}

}
