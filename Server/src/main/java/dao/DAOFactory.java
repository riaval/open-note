package dao;

public class DAOFactory {

	private static GroupDAOImpl groupDAO;
	private static GroupRoleDAOImpl groupRoleDAO;
	private static InviteDAOImpl inviteDAO;
	private static SessionDAOImpl sessionDAO;
	private static SimpleNoteDAOImpl simpleNoteDAO;
	private static UserDAOImpl userDAO;
	private static UserGroupDAOImpl userGroupDAO;

	public static GroupDAOImpl getGroupDAO() {
		if (groupDAO == null) {
			groupDAO = new GroupDAOImpl();
		}
		return groupDAO;
	}

	public static GroupRoleDAOImpl getGroupRoleDAO() {
		if (groupRoleDAO == null) {
			groupRoleDAO = new GroupRoleDAOImpl();
		}
		return groupRoleDAO;
	}

	public static InviteDAOImpl getInviteDAO() {
		if (inviteDAO == null) {
			inviteDAO = new InviteDAOImpl();
		}
		return inviteDAO;
	}

	public static SessionDAOImpl getSessionDAO() {
		if (sessionDAO == null) {
			sessionDAO = new SessionDAOImpl();
		}
		return sessionDAO;
	}

	public static SimpleNoteDAOImpl getSimpleNoteDAO() {
		if (simpleNoteDAO == null) {
			simpleNoteDAO = new SimpleNoteDAOImpl();
		}
		return simpleNoteDAO;
	}

	public static UserDAOImpl getUserDAO() {
		if (userDAO == null) {
			userDAO = new UserDAOImpl();
		}
		return userDAO;
	}

	public static UserGroupDAOImpl getUserGroupDAO() {
		if (userGroupDAO == null) {
			userGroupDAO = new UserGroupDAOImpl();
		}
		return userGroupDAO;
	}

}
