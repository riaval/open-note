package dao;

public class DAOFactory {
	
	private static GroupDAO groupDAO;
	private static GroupRoleDAO groupRoleDAO;
	private static InviteDAO inviteDAO;
	private static SessionDAO sessionDAO;
	private static SimpleNoteDAO simpleNoteDAO;
	private static UserDAO userDAO;
	private static UserGroupDAO userGroupDAO;
	private static UserSimpleNoteDAO userSimpleNoteDAO;
	
	public static GroupDAO getGroupDAO() {
		if(groupDAO == null){
			groupDAO = new GroupDAO();
		}
		return groupDAO;
	}
	public static GroupRoleDAO getGroupRoleDAO() {
		if(groupRoleDAO == null){
			groupRoleDAO = new GroupRoleDAO();
		}
		return groupRoleDAO;
	}
	public static InviteDAO getInviteDAO() {
		if(inviteDAO == null){
			inviteDAO = new InviteDAO();
		}
		return inviteDAO;
	}
	public static SessionDAO getSessionDAO() {
		if(sessionDAO == null){
			sessionDAO = new SessionDAO();
		}
		return sessionDAO;
	}
	public static SimpleNoteDAO getSimpleNoteDAO() {
		if(simpleNoteDAO == null){
			simpleNoteDAO = new SimpleNoteDAO();
		}
		return simpleNoteDAO;
	}
	public static UserDAO getUserDAO() {
		if(userDAO == null){
			userDAO = new UserDAO();
		}
		return userDAO;
	}
	public static UserGroupDAO getUserGroupDAO() {
		if(userGroupDAO == null){
			userGroupDAO = new UserGroupDAO();
		}
		return userGroupDAO;
	}
	public static UserSimpleNoteDAO getUserSimpleNoteDAO() {
		if(userSimpleNoteDAO == null){
			userSimpleNoteDAO = new UserSimpleNoteDAO();
		}
		return userSimpleNoteDAO;
	}
	
}
