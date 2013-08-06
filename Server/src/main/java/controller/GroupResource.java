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

import service.GroupService;
import domain.Group;

public class GroupResource extends ServerResource {

	private String slug;

	@Override
	protected void doInit() throws ResourceException {
		this.slug = (String) getRequest().getAttributes().get("slug");
	}

	@Post
	public String createGroup(Representation entity) {
		Form form = new Form(entity);
		try {
			GroupService groupService = new GroupService();

			String name = form.getFirstValue("name");
			String sessionHash = form.getFirstValue("session_hash");

			groupService.createGroup(slug, name, sessionHash);

			return "hello world";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return HttpStatusFactory.Json.clientBadRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpStatusFactory.Json.serverInternalError();
		}
	}

	@Get("json")
	public Representation getGroups() {
		try {
			GroupService groupService = new GroupService();
			String sessionHash = getQuery().getValues("session_hash");
			Set<Group> groups = groupService.getGroups(sessionHash);

			return new JacksonRepresentation<Set<Group>>(groups);
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
	public String deleteGroup(){
		try {
			GroupService groupService = new GroupService();
			String sessionHash = getQuery().getValues("session_hash");
			groupService.deleteGroup(slug, sessionHash);

			return "OK";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "Exception";
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception";
		}
	}

}
