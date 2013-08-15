package service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import service.exception.BadAuthenticationException;
import dao.DAOFactory;
import dao.HibernateUtil;
import domain.Group;
import domain.Session;
import domain.SimpleNote;
import domain.User;
import domain.UserGroup;

public class SimpleNoteService {

	public void createSimpleNote(String title, String body, String slug, String sessionHash) throws Exception {
		if(title.isEmpty()){
			throw new IllegalArgumentException("Title is empty.");
		}
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty.");
		}
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		if(group == null){
			HibernateUtil.commitTransaction(); // <----
			throw new IllegalArgumentException("Group does not exist.");
		}
		User user = session.getUser();
		Set<UserGroup> userUserGroups = user.getUserGroups();
		
		for (UserGroup each : userUserGroups) {
			if (each.getGroup().equals(group)){
				SimpleNote simpleNote = new SimpleNote(title, body);
				simpleNote.setUserGroup(each);
				DAOFactory.getSimpleNoteDAO().save(simpleNote);
				HibernateUtil.commitTransaction(); // <----
				return;
			}
	    }
		HibernateUtil.commitTransaction(); // <----
		throw new IllegalArgumentException("No rules.");
	}

	public Set<SimpleNote> getSimpleNotes(String slug, String sessionHash) throws Exception {	
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty.");
		}
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		if(group == null){
			HibernateUtil.commitTransaction(); // <----
			throw new IllegalArgumentException("Group does not exist.");
		}
		User user = session.getUser();
		Set<UserGroup> userUserGroups = user.getUserGroups();
		
		for (UserGroup eachOut : userUserGroups) {
			if (eachOut.getGroup().equals(group)){
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
		HibernateUtil.commitTransaction(); // <----
		throw new IllegalArgumentException("No rules.");
	}
	
	public void editSimpleNote(String sessionHash, long id, String title, String body) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty.");
		}
		SimpleNote simpleNote = DAOFactory.getSimpleNoteDAO().findByID(SimpleNote.class, id);
		UserGroup userGroup = simpleNote.getUserGroup();
		if (userGroup.getUser().equals(session.getUser())){
			simpleNote.setTitle(title);
			simpleNote.setBody(body);
			simpleNote.updateDate();
			DAOFactory.getSimpleNoteDAO().merge(simpleNote);
			HibernateUtil.commitTransaction(); // <----
			return;
		}
		HibernateUtil.commitTransaction(); // <----
		throw new IllegalArgumentException("No rules.");
	}
	
	public Set<SimpleNote> getAllSimpleNotes(String sessionHash) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty.");
		}
		Set<UserGroup> userGroups = session.getUser().getUserGroups();
		Set<Group> groups = new HashSet<Group>();
		for(UserGroup each: userGroups){
			groups.add(each.getGroup());
		}
		
		Set<SimpleNote> allSimpleNotes = new HashSet<SimpleNote>();
		for(UserGroup eachUserGroup: userGroups){
			Set<SimpleNote> simpleNotes = eachUserGroup.getSimpleNotes();
			allSimpleNotes.addAll(simpleNotes);
		}
		
		HibernateUtil.commitTransaction(); // <----
		return allSimpleNotes;
	}
	
	public void deleteSimpleNotes(String sessionHash, Long[] IDs) throws Exception {
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty.");
		}
		User user = session.getUser();
		List<SimpleNote> simpleNotes = DAOFactory.getSimpleNoteDAO().findByIDs(IDs);
		for(SimpleNote eachSimpleNote : simpleNotes){
			UserGroup userGroup = eachSimpleNote.getUserGroup();
			Group group = userGroup.getGroup();
			Set<UserGroup> userGroups = group.getUserGroup();
			User groupCreator = null;
			for(UserGroup eachUserGroup : userGroups){
				if(eachUserGroup.getGroupRole().getRole().equals("creator"))
					groupCreator = eachUserGroup.getUser();
			}
			if (!eachSimpleNote.getUserGroup().getUser().equals(user) && !groupCreator.equals(user)){
				HibernateUtil.commitTransaction(); // <----
				throw new IllegalArgumentException("No rules.");
			}
			System.out.println(eachSimpleNote.getTitle());
		}
		HibernateUtil.commitTransaction(); // <----
	}
	
}
