package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.SimpleNoteService;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.SimpleNote;
import domain.response.SimpleNoteResponse;

public class GroupSimpleNoteResource extends ServerResource {

	private String groupSlug;

	@Override
	protected void doInit() throws ResourceException {
		this.groupSlug = getRequest().getAttributes().get("groupSlug").toString();
	}

	@Post
	public Representation createSimpleNote(Representation entity) {
		Form form = new Form(entity);
		try {
			SimpleNoteService simpleNoteService = new SimpleNoteService();

			String title = form.getFirstValue("title");
			String body = form.getFirstValue("body");
			String sessionHash = form.getFirstValue("session_hash");

			simpleNoteService.createSimpleNote(title, body, groupSlug, sessionHash);

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

	@Get("json")
	public Representation getSimpleNotes() {
		SimpleNoteService simpleNoteService = new SimpleNoteService();
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<SimpleNote> simpleNotes = simpleNoteService.getSimpleNotes(groupSlug, sessionHash);

			List<SimpleNoteResponse> simpleNotesResponse = new ArrayList<SimpleNoteResponse>();
			for (SimpleNote each : simpleNotes) {
				simpleNotesResponse.add(new SimpleNoteResponse(each));
			}

			return new JacksonRepresentation<List<SimpleNoteResponse>>(simpleNotesResponse);
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
