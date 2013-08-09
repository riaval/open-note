package controller;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.InviteService;
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
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized() );
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest() );
		} catch (Exception e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}
	
}
