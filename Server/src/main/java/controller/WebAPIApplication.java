package controller;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class WebAPIApplication extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attach("/sessions/{login}", SessionResource.class);
		router.attach("/users/{login}", UserResource.class);
		router.attach("/users/", UserResource.class);

		router.attach("/groups/{slug}", GroupResource.class);
		router.attach("/groups/", GroupResource.class);
		
		router.attach("/snote", SimpleNoteResource.class);
		router.attach("/snote/{snoteID}", SimpleNoteResource.class);
		router.attach("/groups/{groupSlug}/snote/", GroupSimpleNoteResource.class);
		
		router.attach("/invitations/users/{login}/groups/{slug}", InviteResource.class);
		router.attach("/invitations/{invitationId}", InviteResource.class);
		router.attach("/invitations", InviteResource.class);
		
		router.attach("/groups/{groupSlug}/users", UserGroupResource.class);

		return router;
	}

}
