package dao;

public class DAOFactory {

	private static GroupDAOImpl sGroupDAO;
	private static GroupRoleDAOImpl sGroupRoleDAO;
	private static InviteDAOImpl sInviteDAO;
	private static SessionDAOImpl sSessionDAO;
	private static SimpleNoteDAOImpl sSimpleNoteDAO;
	private static UserDAOImpl sUserDAO;
	private static UserGroupDAOImpl sUserGroupDAO;

	public static GroupDAOImpl getGroupDAO() {
		if (sGroupDAO == null) {
			sGroupDAO = new GroupDAOImpl();
		}
		return sGroupDAO;
	}

	public static GroupRoleDAOImpl getGroupRoleDAO() {
		if (sGroupRoleDAO == null) {
			sGroupRoleDAO = new GroupRoleDAOImpl();
		}
		return sGroupRoleDAO;
	}

	public static InviteDAOImpl getInviteDAO() {
		if (sInviteDAO == null) {
			sInviteDAO = new InviteDAOImpl();
		}
		return sInviteDAO;
	}

	public static SessionDAOImpl getSessionDAO() {
		if (sSessionDAO == null) {
			sSessionDAO = new SessionDAOImpl();
		}
		return sSessionDAO;
	}

	public static SimpleNoteDAOImpl getSimpleNoteDAO() {
		if (sSimpleNoteDAO == null) {
			sSimpleNoteDAO = new SimpleNoteDAOImpl();
		}
		return sSimpleNoteDAO;
	}

	public static UserDAOImpl getUserDAO() {
		if (sUserDAO == null) {
			sUserDAO = new UserDAOImpl();
		}
		return sUserDAO;
	}

	public static UserGroupDAOImpl getUserGroupDAO() {
		if (sUserGroupDAO == null) {
			sUserGroupDAO = new UserGroupDAOImpl();
		}
		return sUserGroupDAO;
	}

}
