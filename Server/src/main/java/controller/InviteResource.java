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
import service.ServiceFactory;
import service.exception.BadAuthenticationException;
import controller.representation.InviteRepresentation;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.Invite;

public class InviteResource extends ServerResource {

	private String mSlug;
	private String mLogin;
	private long mInvitationId;
	private InviteService mInviteService;

	@Override
	protected void doInit() throws ResourceException {
		mInviteService = ServiceFactory.getInviteService();
		this.mSlug = (String) getRequest().getAttributes().get("slug");
		this.mLogin = (String) getRequest().getAttributes().get("login");
		String invitationId = (String) getRequest().getAttributes().get("invitationId");
		if(invitationId != null){
			this.mInvitationId = Long.parseLong(invitationId);
		}
	}

	@Post
	public Representation createInvite(Representation entity) {
		Form form = new Form(entity);
		try {
			String sessionHash = form.getFirstValue("session_hash");
			mInviteService.createInvitation(mSlug, mLogin, sessionHash);

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

	@Get
	public Representation getInvites() {
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<Invite> invites = mInviteService.getInvitations(sessionHash);

			List<InviteRepresentation> invitationsResponse = new ArrayList<InviteRepresentation>();
			for (Invite each : invites) {
				invitationsResponse.add(new InviteRepresentation(each));
			}

			return new JacksonRepresentation<List<InviteRepresentation>>(invitationsResponse);
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized(e.getMessage()) );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

	@Delete
	public Representation deleteInvite() {
		try {
			String sessionHash = getQuery().getValues("session_hash");
			mInviteService.deleteInvitation(sessionHash, mInvitationId);

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
