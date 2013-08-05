package controller;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.UserGroupService;

public class UserGroupResource extends ServerResource{

	private String groupSlug;
	
	@Override
	protected void doInit() throws ResourceException {
		this.groupSlug = (String) getRequest().getAttributes().get("groupSlug");
	}
	
	@Post
	public String addUserToGroup(Representation entity) {
		Form form = new Form(entity);
		try {
			UserGroupService userGroupService = new UserGroupService();
			String sessionHash = form.getFirstValue("session_hash");
			userGroupService.addUserToGroup(sessionHash, groupSlug);
			
			return ErrorFactory.Json.successOK();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return HttpStatusFactory.Json.clientBadRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpStatusFactory.Json.serverInternalError();
		}
	}
	
}
