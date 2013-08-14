package dao;

import java.util.List;

import org.hibernate.Query;

import domain.SimpleNote;

public class SimpleNoteDAOImpl extends DAOImpl<SimpleNote> {

	public List<SimpleNote> findByIDs(Long[] IDs){
		Query query = getSession().createQuery("from SimpleNote where id in (:ids)");
		query.setParameterList("ids", IDs);
		List<SimpleNote> simpleNotes = findMany(query);
		return simpleNotes;
	}
	
}
