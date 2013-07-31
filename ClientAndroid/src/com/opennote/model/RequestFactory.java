package com.opennote.model;

import com.foxykeep.datadroid.requestmanager.Request;

public class RequestFactory {

	public static final int SIGN_IN = 1;
	public static final int SIGN_UP = 2;
	public static final int LOAD_NOTES = 3;
	public static final int ADD_NOTE = 4;
	public static final int CREATE_GROUP = 5;
	public static final int LOAD_GROUPS = 6;
	
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
	
	public static Request getCreateGroupRequest(String sessionHash, String groupSlug, String groupName){
		Request request = new Request(CREATE_GROUP);
		request.put("session_hash", sessionHash);
		request.put("slug", groupSlug);
		request.put("name", groupName);
		return request;
	}
	
	public static Request getLoadGroupsRequest(String sessionHash){
		Request request = new Request(LOAD_GROUPS);
		request.put("session_hash", sessionHash);
		return request;
	}
	
	private RequestFactory() {
	}
	
}
