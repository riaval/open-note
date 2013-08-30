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

import service.ServiceFactory;
import service.SimpleNoteService;
import service.exception.BadAuthenticationException;
import controller.representation.SimpleNoteRepresentation;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.SimpleNote;

public class GroupSimpleNoteResource extends ServerResource {

	private String mGroupSlug;
	private SimpleNoteService mSimpleNoteService;

	@Override
	protected void doInit() throws ResourceException {
		mSimpleNoteService = ServiceFactory.getSimpleNoteService();
		this.mGroupSlug = getRequest().getAttributes().get("groupSlug").toString();
	}

	@Post
	public Representation createSimpleNote(Representation entity) {
		Form form = new Form(entity);
		try {
			String title = form.getFirstValue("title");
			String body = form.getFirstValue("body");
			String sessionHash = form.getFirstValue("session_hash");

			SimpleNote simpleNote = mSimpleNoteService.createSimpleNote(title, body, mGroupSlug, sessionHash);

			return new JacksonRepresentation<SimpleNoteRepresentation>( new SimpleNoteRepresentation(simpleNote) );
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
	public Representation getSimpleNotes() {
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<SimpleNote> simpleNotes = mSimpleNoteService.getSimpleNotes(mGroupSlug, sessionHash);

			List<SimpleNoteRepresentation> simpleNotesResponse = new ArrayList<SimpleNoteRepresentation>();
			for (SimpleNote each : simpleNotes) {
				simpleNotesResponse.add(new SimpleNoteRepresentation(each));
			}

			return new JacksonRepresentation<List<SimpleNoteRepresentation>>(simpleNotesResponse);
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
