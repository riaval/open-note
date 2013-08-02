package controller;

import java.util.List;
import java.util.Set;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.UserService;
import domain.Invite;
import domain.Session;
import domain.User;

public class UserResource extends ServerResource {
	
	private String login;
	
	@Override
	protected void doInit() throws ResourceException {
	    this.login = (String) getRequest().getAttributes().get("login");
	}
	
	@Get("json")
	public Representation getUsers(){
		try {
			UserService userService = new UserService();
			String sessionHash = getQuery().getValues("session_hash");
			String login = getQuery().getValues("login");
			String fullName = getQuery().getValues("full_name");
			
			List<User> users = userService.getUsers(sessionHash, login, fullName);

			return new JacksonRepresentation<List<User>>(users);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new StringRepresentation("Item created",
					MediaType.TEXT_PLAIN);
		} catch (Exception e) {
			e.printStackTrace();
			return new StringRepresentation("Item created",
					MediaType.TEXT_PLAIN);
		}
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
	
			return new JacksonRepresentation<Session>(session);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
