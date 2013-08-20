package controller;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.UserService;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.Session;
import domain.User;
import domain.response.SessionResponse;
import domain.response.UserPrivateResponse;
import domain.response.UserPublicResponse;

public class UserResource extends ServerResource {

	private String mLogin;

	@Override
	protected void doInit() throws ResourceException {
		mLogin = (String) getRequest().getAttributes().get("login");
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
					  mLogin
					, fullName
					, password
					, hostIp
					, hostAgent
					);

			return new JacksonRepresentation<SessionResponse>(
					new SessionResponse(session)
					);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest() );
		} catch (Exception e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

	@Get("json")
	public Representation getUserData(){
		try {
			if (getQuery().getValues("search") == null){
				return mono();
			} else {
				return multi();
			}
		} catch (BadAuthenticationException e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized() );
		} catch (Exception e) {
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}
	
	private Representation multi() throws Exception {
		UserService userService = new UserService();
		String sessionHash = getQuery().getValues("session_hash");
		String search = getQuery().getValues("search");

		List<User> users = userService.getUsers(sessionHash, search);
		List<UserPublicResponse> usersResponse = new ArrayList<UserPublicResponse>();
		for (User each : users) {
			usersResponse.add(new UserPublicResponse(each));
		}

		return new JacksonRepresentation<List<UserPublicResponse>>(usersResponse);
	}
	
	private Representation mono() throws Exception {
		UserService userService = new UserService();
		String sessionHash = getQuery().getValues("session_hash");
		
		User user = userService.getCurrentUser(sessionHash);
		
		return new JacksonRepresentation<UserPrivateResponse>(
				new UserPrivateResponse(user)
				);
	}

}
