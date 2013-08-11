package service;

import java.util.HashSet;
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
			throw new IllegalArgumentException("Title is empty");
		}
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		if(group == null){
			HibernateUtil.commitTransaction(); // <----
			throw new IllegalArgumentException("Group does not exist");
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
		throw new IllegalArgumentException("No rules");
	}

	public Set<SimpleNote> getSimpleNotes(String slug, String sessionHash) throws Exception {	
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		if(session == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Session is empty");
		}
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		if(group == null){
			HibernateUtil.commitTransaction(); // <----
			throw new IllegalArgumentException("Group does not exist");
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
		throw new IllegalArgumentException("No rules");
	}
	
}
