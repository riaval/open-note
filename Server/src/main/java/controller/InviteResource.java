package controller;

import java.util.Set;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.InviteService;
import domain.Invite;

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
	public String createInvite(Representation entity) {
		Form form = new Form(entity);
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = form.getFirstValue("session_hash");
			inviteService.createInvite(slug, login, sessionHash);
			return "OK";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return HttpStatusFactory.Json.clientBadRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpStatusFactory.Json.serverInternalError();
		}
	}
	
	@Get("json")
	public Representation getInvites() {
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = getQuery().getValues("session_hash");
			Set<Invite> invites = inviteService.getInvites(sessionHash);

			return new JacksonRepresentation<Set<Invite>>(invites);
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
	
	@Delete
	public String deleteInvite(Representation entity) {
		try {
			InviteService inviteService = new InviteService();
			String sessionHash = getQuery().getValues("session_hash");
			inviteService.deleteInvites(sessionHash, invitationId);

			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error";
	}
	
}
