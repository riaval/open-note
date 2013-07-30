package com.opennote.model;

import com.foxykeep.datadroid.requestmanager.Request;

public class RequestFactory {

	public static final int SIGN_IN = 1;
	public static final int SIGN_UP = 2;
	public static final int LOAD_NOTES = 3;
	public static final int ADD_NOTE = 4;
	
	public static Request getSignInRequest(String login, String password) {
		Request request = new Request(SIGN_IN);
		request.put("login", login);
		request.put("password", password);
		return request;
	}
	
	public static Request getSignUpRequest(String login, String fullName, String password) {
		Request request = new Request(SIGN_UP);
		request.put("login", login);
		request.put("full_name", fullName);
		request.put("password", password);
		return request;
	}
	
	public static Request getLoadNotesRequest(String sessionHash, String groupSlug){
		Request request = new Request(LOAD_NOTES);
		request.put("session_hash", sessionHash);
		request.put("slug", groupSlug);
		return request;
	}
	
	public static Request getAddNoteRequest(String sessionHash, String groupSlug, String title, String body){
		Request request = new Request(ADD_NOTE);
		request.put("session_hash", sessionHash);
		request.put("slug", groupSlug);
		request.put("title", title);
		request.put("body", body);
		return request;
	}
	
	private RequestFactory() {
	}
	
}
