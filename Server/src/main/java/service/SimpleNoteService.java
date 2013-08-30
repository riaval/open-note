package service;

import java.util.Set;

import domain.SimpleNote;

public interface SimpleNoteService {

	public SimpleNote createSimpleNote(String title, String body, String slug, String sessionHash) throws Exception;

	public Set<SimpleNote> getSimpleNotes(String slug, String sessionHash) throws Exception;

	public void editSimpleNote(String sessionHash, long id, String title, String body) throws Exception;

	public Set<SimpleNote> getAllSimpleNotes(String sessionHash) throws Exception;

	public void deleteSimpleNotes(String sessionHash, Long[] IDs) throws Exception;

}
