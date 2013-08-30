package dao.implementation;

import java.util.List;

import org.hibernate.Query;

import dao.SimpleNoteDAO;
import domain.SimpleNote;

public class SimpleNoteDAOImpl extends DAOImpl<SimpleNote> implements SimpleNoteDAO {

	public List<SimpleNote> findByIDs(Long[] IDs) {
		Query query = getSession().createQuery("from SimpleNote where id in (:ids)");
		query.setParameterList("ids", IDs);
		List<SimpleNote> simpleNotes = findMany(query);
		return simpleNotes;
	}

}
