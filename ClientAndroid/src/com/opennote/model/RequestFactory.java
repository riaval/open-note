package com.opennote.model;

import com.foxykeep.datadroid.requestmanager.Request;

public class RequestFactory {

	public static final int SIGN_IN = 1;
	public static final int SIGN_UP = 2;
	public static final int LOAD_NOTES = 3;
	public static final int ADD_NOTE = 4;
	public static final int CREATE_GROUP = 5;
	public static final int LOAD_GROUPS = 6;
	public static final int INVITE_USER = 7;
	public static final int LOAD_INVITATIONS = 8;
	public static final int DELETE_INVITATION = 9;
	public static final int CREATE_INVITATION = 10;
	public static final int ACCEPT_INVITATION = 11;
	public static final int DELETE_GROUP = 12;
	public static final int REMOVE_USER = 13;
	public static final int EDIT_NOTE = 14;
	public static final int LOAD_ALL_NOTES = 15;
	public static final int DELETE_NOTES = 16;
	
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
	
	public static Request getInviteUserRequest(String sessionHash, String searchValue){
		Request request = new Request(INVITE_USER);
		request.put("session_hash", sessionHash);
		request.put("search", searchValue);
		return request;
	}
	
	public static Request getLoadInvitationsRequest(String sessionHash){
		Request request = new Request(LOAD_INVITATIONS);
		request.put("session_hash", sessionHash);
		return request;
	}
	
	public static Request getDeleteInvitationsRequest(String sessionHash, String invitationId){
		Request request = new Request(DELETE_INVITATION);
		request.put("session_hash", sessionHash);
		request.put("invitation_id", invitationId);
		return request;
	}
	
	public static Request getCreateInvitationsRequest(String sessionHash, String userLogin, String groupSlug){
		Request request = new Request(CREATE_INVITATION);
		request.put("session_hash", sessionHash);
		request.put("login", userLogin);
		request.put("slug", groupSlug);
		return request;
	}
	
	public static Request getAcceptInvitationsRequest(String sessionHash, String groupSlug, String groupName){
		Request request = new Request(ACCEPT_INVITATION);
		request.put("session_hash", sessionHash);
		request.put("slug", groupSlug);
		request.put("name", groupName);
		return request;
	}
	
	public static Request getDeleteGroupRequest(String sessionHash, String groupSlug){
		Request request = new Request(DELETE_GROUP);
		request.put("session_hash", sessionHash);
		request.put("slug", groupSlug);
		return request;
	}
	
	public static Request getRemoveUserFromGroupRequest(String sessionHash, String groupSlug){
		Request request = new Request(REMOVE_USER);
		request.put("session_hash", sessionHash);
		request.put("slug", groupSlug);
		return request;
	}
	
	public static Request getEditNoteRequest(String sessionHash, long id, String title, String body){
		Request request = new Request(EDIT_NOTE);
		request.put("session_hash", sessionHash);
		request.put("id", id);
		request.put("title", title);
		request.put("body", body);
		return request;
	}
	
	public static Request getLoadAllNoteRequest(String sessionHash){
		Request request = new Request(LOAD_ALL_NOTES);
		request.put("session_hash", sessionHash);
		return request;
	}
	
	public static Request getDeleteNotesRequest(String sessionHash, long[] IDs){
		Request request = new Request(DELETE_NOTES);
		request.put("session_hash", sessionHash);
		int counter = 0;
		for (long id : IDs){
			request.put("id"+counter++, id);
		}
		request.put("length", counter);
		
		return request;
	}
	
	private RequestFactory() {
	}
	
}
