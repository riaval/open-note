package controller;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.SessionService;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SessionResource extends ServerResource {
	
	private String userLogin;
	
	@Override
	protected void doInit() throws ResourceException {
	    this.userLogin = (String) getRequest().getAttributes().get("userLogin");
	}
	
	@Post
	public String openSession(Representation entity){
		Form form = new Form(entity);
		try {
			SessionService sessionService = new SessionService();
			
			String password = form.getFirstValue("password");
			String hostIp = getClientInfo().getAddress();
			String hostAgent = getClientInfo().getAgent();
			
			String sessionHash = sessionService.openSession(
					  userLogin
					, password
					, hostIp
					, hostAgent
			);
			
			return openSessionJson(sessionHash);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ErrorFactory.Json.clientBadRequest();
		} catch (Exception e) {
			e.printStackTrace();
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
