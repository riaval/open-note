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
		UserGroup userGroup = checkData(slug, sessionHash);
		
		SimpleNote simpleNote = new SimpleNote(title, body);
		simpleNote.setUserGroup(userGroup);
		DAOFactory.getSimpleNoteDAO().save(simpleNote);
		HibernateUtil.commitTransaction(); // <----
	}

	public Set<SimpleNote> getSimpleNotes(String slug, String sessionHash) throws Exception {
//		UserGroup userGroup = checkData(slug, sessionHash);		
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		Set<UserGroup> userGroups = group.getUserGroup();
		Set<SimpleNote> allSimpleNotes = new HashSet();
		for (UserGroup each : userGroups) {
			Set<SimpleNote> simpleNotes = each.getSimpleNotes();
			allSimpleNotes.addAll(simpleNotes);
	    }
		
//		Set<SimpleNote> simpleNotes = userGroup.getSimpleNotes();
		HibernateUtil.commitTransaction(); // <----
		
		return allSimpleNotes;
	}
	
	private UserGroup checkData(String slug, String sessionHash) throws Exception{
		HibernateUtil.beginTransaction(); // ---->
		Session session = DAOFactory.getSessionDAO().findByHash(sessionHash);
		User user = session.getUser();
		Group group = DAOFactory.getGroupDAO().findBySlug(slug);
		Set<UserGroup> userGroups = user.getUserGroups();
		
		if(user == null || group == null){
			HibernateUtil.commitTransaction(); // <----
			throw new IllegalArgumentException("user or group is null");
		}
		
		UserGroup userGroup = null;
		for (UserGroup each : userGroups) {
			if (each.getGroup().equals(group)){
	        	userGroup = each;
	        	break;
	        }
	    }
		if(userGroup == null){
			HibernateUtil.commitTransaction(); // <----
			throw new BadAuthenticationException("Bad Authentication data");
		}
			
		return userGroup;
	}
	
}
