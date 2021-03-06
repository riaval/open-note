package service.implementation;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import service.SimpleNoteService;
import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.Session;
import domain.SimpleNote;
import domain.User;
import domain.UserGroup;

public class SimpleNoteServiceImpl implements SimpleNoteService {

	public SimpleNote createSimpleNote(String title, String body, String slug, String sessionHash) throws Exception {
		if (title.isEmpty()) {
			throw new IllegalArgumentException("Title is empty.");
		}
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			Group group = DAOFactory.getGroupDAO().findBySlug(slug);
			if (group == null) {
				throw new IllegalArgumentException("Group does not exist.");
			}
			User user = session.getUser();
			Set<UserGroup> userUserGroups = user.getUserGroups();

			for (UserGroup each : userUserGroups) {
				if (each.getGroup().equals(group)) {
					Date date = Calendar.getInstance().getTime();
					SimpleNote simpleNote = new SimpleNote(title, body, date);
					simpleNote.setUserGroup(each);
					DAOFactory.getSimpleNoteDAO().save(simpleNote);
					HibernateUtil.commitTransaction(); // <----
					return simpleNote;
				}
			}
			throw new IllegalArgumentException("No rules.");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Set<SimpleNote> getSimpleNotes(String slug, String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			Group group = DAOFactory.getGroupDAO().findBySlug(slug);
			if (group == null) {
				throw new IllegalArgumentException("Group does not exist.");
			}
			User user = session.getUser();
			Set<UserGroup> userUserGroups = user.getUserGroups();

			for (UserGroup eachOut : userUserGroups) {
				if (eachOut.getGroup().equals(group)) {
					Set<SimpleNote> allSimpleNotes = new HashSet<SimpleNote>();
					Set<UserGroup> groupUserGroups = group.getUserGroup();
					for (UserGroup eachIn : groupUserGroups) {
						Set<SimpleNote> simpleNotes = eachIn.getSimpleNotes();
						allSimpleNotes.addAll(simpleNotes);
					}
					HibernateUtil.commitTransaction(); // <----
					return allSimpleNotes;
				}
			}
			throw new IllegalArgumentException("No rules.");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void editSimpleNote(String sessionHash, long id, String title, String body) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			SimpleNote simpleNote = DAOFactory.getSimpleNoteDAO().findByID(SimpleNote.class, id);
			UserGroup userGroup = simpleNote.getUserGroup();
			if (userGroup.getUser().equals(session.getUser())) {
				simpleNote.setTitle(title);
				simpleNote.setBody(body);
				Date date = Calendar.getInstance().getTime();
				simpleNote.setDate(date);
				DAOFactory.getSimpleNoteDAO().merge(simpleNote);
				HibernateUtil.commitTransaction(); // <----
				return;
			}
			throw new IllegalArgumentException("No rules.");
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Set<SimpleNote> getAllSimpleNotes(String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			Set<UserGroup> userGroups = session.getUser().getUserGroups();
			Set<Group> groups = new HashSet<Group>();
			for (UserGroup each : userGroups) {
				groups.add(each.getGroup());
			}

			Set<SimpleNote> allSimpleNotes = new HashSet<SimpleNote>();
			for (Group eachGroups : groups) {
				Set<UserGroup> targetUserGroups = eachGroups.getUserGroup();
				for (UserGroup target : targetUserGroups) {
					Set<SimpleNote> simpleNotes = target.getSimpleNotes();
					allSimpleNotes.addAll(simpleNotes);
				}
			}
			HibernateUtil.commitTransaction(); // <----
			return allSimpleNotes;
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void deleteSimpleNotes(String sessionHash, Long[] IDs) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		try {
			Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
			if (session == null) {
				throw new BadAuthenticationException("Session is empty.");
			}
			User user = session.getUser();
			List<SimpleNote> simpleNotes = DAOFactory.getSimpleNoteDAO().findByIDs(IDs);
			for (SimpleNote eachSimpleNote : simpleNotes) {
				UserGroup userGroup = eachSimpleNote.getUserGroup();
				Group group = userGroup.getGroup();
				Set<UserGroup> userGroups = group.getUserGroup();
				User groupCreator = null;
				for (UserGroup eachUserGroup : userGroups) {
					if (eachUserGroup.getGroupRole().getRole().equals("creator"))
						groupCreator = eachUserGroup.getUser();
				}
				if (!eachSimpleNote.getUserGroup().getUser().equals(user) && !groupCreator.equals(user)) {
					throw new IllegalArgumentException("No rules.");
				}
				DAOFactory.getSimpleNoteDAO().delete(eachSimpleNote);
			}
			HibernateUtil.commitTransaction(); // <----
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
