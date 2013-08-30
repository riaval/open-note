package controller;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.ServiceFactory;
import service.UserService;
import service.exception.BadAuthenticationException;
import controller.representation.SessionRepresentation;
import controller.representation.Status;
import controller.representation.StatusFactory;
import controller.representation.UserPrivateRepresentation;
import controller.representation.UserPublicRepresentation;
import domain.Session;
import domain.User;

public class UserResource extends ServerResource {

	private String mLogin;
	private UserService mUserService;

	@Override
	protected void doInit() throws ResourceException {
		mUserService = ServiceFactory.getUserService();
		mLogin = (String) getRequest().getAttributes().get("login");
	}

	@Post
	public Representation createUser(Representation entity) {
		Form form = new Form(entity);
		try {
			String fullName = form.getFirstValue("full_name");
			String password = form.getFirstValue("password");
			String hostIp = getClientInfo().getAddress();
			String hostAgent = getClientInfo().getAgent();

			Session session = mUserService.createUser(
					  mLogin
					, fullName
					, password
					, hostIp
					, hostAgent
					);

			return new JacksonRepresentation<SessionRepresentation>(
					new SessionRepresentation(session)
					);
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest(e.getMessage()) );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
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
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized(e.getMessage()) );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}
	
	@Put
	public Representation editData(Representation entity){
		Form form = new Form(entity);
		String password = form.getFirstValue("old_password");
		
		if (password == null){
			return editSimpleData(form);
		} else {
			return editPasswordData(form);
		}
	}
	
	private Representation multi() throws Exception {
		String sessionHash = getQuery().getValues("session_hash");
		String search = getQuery().getValues("search");

		List<User> users = mUserService.getUsers(sessionHash, search);
		List<UserPublicRepresentation> usersResponse = new ArrayList<UserPublicRepresentation>();
		for (User each : users) {
			usersResponse.add(new UserPublicRepresentation(each));
		}

		return new JacksonRepresentation<List<UserPublicRepresentation>>(usersResponse);
	}
	
	private Representation mono() throws Exception {
		String sessionHash = getQuery().getValues("session_hash");
		
		User user = mUserService.getCurrentUser(sessionHash);
		
		return new JacksonRepresentation<UserPrivateRepresentation>(
				new UserPrivateRepresentation(user)
			);
	}
	
	private Representation editSimpleData(Form form) {
		String sessionHash = form.getFirstValue("session_hash");
		String fullName = form.getFirstValue("full_name");
		String email = form.getFirstValue("email");
		
		try {
			mUserService.editSimpleData(sessionHash, fullName, email);
			
			return new JacksonRepresentation<Status>( StatusFactory.ok() );
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized(e.getMessage()) );
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest(e.getMessage()) );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}
	
	private Representation editPasswordData(Form form) {
		String sessionHash = form.getFirstValue("session_hash");
		String oldPassword = form.getFirstValue("old_password");
		String newPassword = form.getFirstValue("new_password");
		
		try {
			mUserService.editPassword(sessionHash, oldPassword, newPassword);
			
			return new JacksonRepresentation<Status>( StatusFactory.ok() );
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized(e.getMessage()) );
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest(e.getMessage()) );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

}
