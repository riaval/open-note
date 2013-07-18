package controller;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.UserService;
import domain.Session;

public class UserResource extends ServerResource {
	
	private String login;
	
	@Override
	protected void doInit() throws ResourceException {
	    this.login = (String) getRequest().getAttributes().get("login");
	}
	
	@Post
	public Representation createUser(Representation entity) {
		Form form = new Form(entity);
		try {
			UserService userService = new UserService();
			
			String fullName = form.getFirstValue("full_name");
			String password = form.getFirstValue("password");
			String hostIp = getClientInfo().getAddress();
			String hostAgent = getClientInfo().getAgent();
			
			Session session = userService.createUser(
					  login
					, fullName
					, password
					, hostIp
					, hostAgent
			);
	
			return openSessionJson(session);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Get("json")
	private Representation openSessionJson(Session session){
		return new JacksonRepresentation<Session>(session);
	}

}
