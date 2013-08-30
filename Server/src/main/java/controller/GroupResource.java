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

import service.GroupService;
import service.ServiceFactory;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;
import controller.representation.UserGroupRepresentation;
import domain.UserGroup;

public class GroupResource extends ServerResource {

	private String mSlug;
	private GroupService mGroupService;

	@Override
	protected void doInit() throws ResourceException {
		mGroupService = ServiceFactory.getGroupService();
		this.mSlug = (String) getRequest().getAttributes().get("slug");
	}

	@Post
	public Representation createGroup(Representation entity) {
		Form form = new Form(entity);
		try {
			String name = form.getFirstValue("name");
			String sessionHash = form.getFirstValue("session_hash");
			mGroupService.createGroup(mSlug, name, sessionHash);

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

	@Get("json")
	public Representation getGroups() {
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<UserGroup> userGroups = mGroupService.getGroups(sessionHash);

			List<UserGroupRepresentation> groupsResponse = new ArrayList<UserGroupRepresentation>();
			for (UserGroup each : userGroups) {
				groupsResponse.add(new UserGroupRepresentation(each));
			}

			return new JacksonRepresentation<List<UserGroupRepresentation>>(groupsResponse);
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized(e.getMessage()) );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

	@Delete
	public Representation deleteGroup(){
		try {
			String sessionHash = getQuery().getValues("session_hash");
			mGroupService.deleteGroup(mSlug, sessionHash);

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
