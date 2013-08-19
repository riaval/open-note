package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.InviteService;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.Invite;
import domain.SimpleNote;
import domain.response.InviteResponse;
import domain.response.SimpleNoteResponse;

public class InviteResource extends ServerResource {
	
	private String slug;
	private String login;
	private long invitationId;

	@Override
	protected void doInit() throws ResourceException {
		this.slug = (String) getRequest().getAttributes().get("slug");
		this.login = (String) getRequest().getAttributes().get("login");
		String invitationId = (String) getRequest().getAttributes().get("invitationId");
		if(invitationId != null){
			this.invitationId = Long.parseLong(invitationId);
		}
	}
	
	@Post
	public Representation createInvite(Representation entity) {
		Form form = new Form(entity);
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = form.getFirstValue("session_hash");
			inviteService.createInvitation(slug, login, sessionHash);
			
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
	
	@Get
	public Representation getInvites() {
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = getQuery().getValues("session_hash");
			Set<Invite> invites = inviteService.getInvitations(sessionHash);

			List<InviteResponse> invitationsResponse = new ArrayList<InviteResponse>();
			for (Invite each : invites) {
				invitationsResponse.add(new InviteResponse(each));
			}

			return new JacksonRepresentation<List<InviteResponse>>(invitationsResponse);
		} catch (BadAuthenticationException e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized() );
		} catch (Exception e) {
			e.printStackTrace();
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}
	
	@Delete
	public Representation deleteInvite() {
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = getQuery().getValues("session_hash");
			inviteService.deleteInvitation(sessionHash, invitationId);

			return new JacksonRepresentation<Status>( StatusFactory.ok() );
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
