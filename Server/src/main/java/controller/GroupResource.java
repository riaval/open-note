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
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.UserGroup;
import domain.response.UserGroupResponse;

public class GroupResource extends ServerResource {

	private String slug;

	@Override
	protected void doInit() throws ResourceException {
		this.slug = (String) getRequest().getAttributes().get("slug");
	}

	@Post
	public Representation createGroup(Representation entity) {
		Form form = new Form(entity);
		try {
			GroupService groupService = new GroupService();

			String name = form.getFirstValue("name");
			String sessionHash = form.getFirstValue("session_hash");
			groupService.createGroup(slug, name, sessionHash);

			return new JacksonRepresentation<Status>( StatusFactory.created() );
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized() );
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest() );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

	@Get("json")
	public Representation getGroups() {
		try {
			GroupService groupService = new GroupService();
			String sessionHash = getQuery().getValues("session_hash");
			Set<UserGroup> userGroups = groupService.getGroups(sessionHash);

			List<UserGroupResponse> groupsResponse = new ArrayList<UserGroupResponse>();
			for (UserGroup each : userGroups) {
				groupsResponse.add(new UserGroupResponse(each));
			}

			return new JacksonRepresentation<List<UserGroupResponse>>(groupsResponse);
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized() );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

	@Delete
	public Representation deleteGroup(){
		try {
			GroupService groupService = new GroupService();
			String sessionHash = getQuery().getValues("session_hash");
			groupService.deleteGroup(slug, sessionHash);

			return new JacksonRepresentation<Status>( StatusFactory.ok() );
		} catch (BadAuthenticationException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientUnauthorized() );
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest() );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

}
