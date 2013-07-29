package com.opennote.model;

import com.foxykeep.datadroid.requestmanager.Request;

public class RequestFactory {

	public static final int SIGN_IN = 1;
	public static final int SIGN_UP = 2;
	
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
	
	private RequestFactory() {
	}
	
}
