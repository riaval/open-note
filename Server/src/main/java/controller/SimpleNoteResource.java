package controller;

import java.util.Set;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.SimpleNoteService;
import domain.SimpleNote;

public class SimpleNoteResource extends ServerResource {

	private String groupSlug;
//	private long snoteID;

	@Override
	protected void doInit() throws ResourceException {
		this.groupSlug = getRequest().getAttributes().get("groupSlug").toString();
//		this.snoteID = Long.parseLong(getRequest().getAttributes().get("snoteID").toString());
	}

	@Post
	public String createSimpleNote(Representation entity) {
		Form form = new Form(entity);
		try {
			SimpleNoteService simpleNoteService = new SimpleNoteService();

			String title = form.getFirstValue("title");
			String body = form.getFirstValue("body");
			String sessionHash = form.getFirstValue("session_hash");

			simpleNoteService.createSimpleNote(title, body, groupSlug, sessionHash);

			return "createSimpleNote";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Get("json")
	public Representation getGroups() {
		SimpleNoteService simpleNoteService = new SimpleNoteService();
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<SimpleNote> simpleNotes = simpleNoteService.getSimpleNotes(groupSlug, sessionHash);
			System.out.println(simpleNotes.isEmpty());

			return new JacksonRepresentation<Set<SimpleNote>>(simpleNotes);
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

}
