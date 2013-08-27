package controller;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.InviteService;
import service.UserGroupService;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;

public class UserGroupResource extends ServerResource{

	private String groupSlug;

	@Override
	protected void doInit() throws ResourceException {
		this.groupSlug = (String) getRequest().getAttributes().get("groupSlug");
	}

	@Post
	public Representation addUserToGroup(Representation entity) {
		Form form = new Form(entity);
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = form.getFirstValue("session_hash");
			inviteService.acceptInvitation(sessionHash, groupSlug);

			return new JacksonRepresentation<Status>( StatusFactory.created() );
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

	@Delete
	public Representation deleteUserFromGroup(){
		try {
			UserGroupService userGroupService = new UserGroupService();
			String sessionHash = getQuery().getValues("session_hash");
			userGroupService.deleteUserFromGroup(sessionHash, groupSlug);

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
