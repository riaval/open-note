package dao;

import java.util.List;

import domain.SimpleNote;

public interface SimpleNoteDAO extends DAO<SimpleNote> {

	public List<SimpleNote> findByIDs(Long[] IDs);

}
