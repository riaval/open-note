package service;

import service.implementation.GroupServiceImpl;
import service.implementation.InviteServiceImpl;
import service.implementation.SessionServiceImpl;
import service.implementation.SimpleNoteServiceImpl;
import service.implementation.UserGroupServiceImpl;
import service.implementation.UserServiceImpl;

public class ServiceFactory {

	private static GroupServiceImpl sGroupServiceImpl;
	private static InviteServiceImpl sInviteServiceImpl;
	private static SessionServiceImpl sSessionServiceImpl;
	private static SimpleNoteServiceImpl sSimpleNoteServiceImpl;
	private static UserGroupServiceImpl sUserGroupServiceImpl;
	private static UserServiceImpl sUserServiceImpl;

	public static GroupService getGroupService() {
		if (sGroupServiceImpl == null) {
			sGroupServiceImpl = new GroupServiceImpl();
		}
		return sGroupServiceImpl;
	}

	public static InviteService getInviteService() {
		if (sInviteServiceImpl == null) {
			sInviteServiceImpl = new InviteServiceImpl();
		}
		return sInviteServiceImpl;
	}

	public static SessionService getSessionService() {
		if (sSessionServiceImpl == null) {
			sSessionServiceImpl = new SessionServiceImpl();
		}
		return sSessionServiceImpl;
	}

	public static SimpleNoteService getSimpleNoteService() {
		if (sSimpleNoteServiceImpl == null) {
			sSimpleNoteServiceImpl = new SimpleNoteServiceImpl();
		}
		return sSimpleNoteServiceImpl;
	}

	public static UserGroupService getUserGroupService() {
		if (sUserGroupServiceImpl == null) {
			sUserGroupServiceImpl = new UserGroupServiceImpl();
		}
		return sUserGroupServiceImpl;
	}

	public static UserService getUserService() {
		if (sUserServiceImpl == null) {
			sUserServiceImpl = new UserServiceImpl();
		}
		return sUserServiceImpl;
	}

}
