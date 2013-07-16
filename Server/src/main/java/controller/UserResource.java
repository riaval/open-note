package controller;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import service.UserService;

public class UserResource extends ServerResource {
	
	private String userLogin;
	
	@Override
	protected void doInit() throws ResourceException {
	    this.userLogin = (String) getRequest().getAttributes().get("userLogin");
	}
	
	@Post
	public String createUser(Representation entity) {
		Form form = new Form(entity);
		try {
			UserService userService = new UserService();
			
			String fullName = form.getFirstValue("fullName");
			String password = form.getFirstValue("password");
			String hostIp = getClientInfo().getAddress();
			String hostAgent = getClientInfo().getAgent();
			
			String sessionHash = userService.createUser(
					  userLogin
					, fullName
					, password
					, hostIp
					, hostAgent
			);
	
			setStatus(Status.SUCCESS_CREATED);
			return openSessionJson(sessionHash);
		} catch (IllegalArgumentException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return ErrorFactory.Json.clientBadRequest();
		} catch (Exception e) {
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return ErrorFactory.Json.serverInternalError();
		}
	}
	
	private String openSessionJson(String sessionHash) {
		JsonPrimitive jsonPrimitive = new JsonPrimitive(sessionHash);
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.add("session_hash", jsonPrimitive);
		
		return jsonObject.toString();
	}

}
