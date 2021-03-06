package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.ServiceFactory;
import service.SimpleNoteService;
import service.exception.BadAuthenticationException;
import controller.representation.SimpleNoteRepresentation;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.SimpleNote;

public class SimpleNoteResource extends ServerResource {

	private long mSnoteID;
	private SimpleNoteService mSimpleNoteService;

	@Override
	protected void doInit() throws ResourceException {
		mSimpleNoteService = ServiceFactory.getSimpleNoteService();
		Object snoteIDObj = getRequest().getAttributes().get("snoteID");
		if (snoteIDObj != null){
			this.mSnoteID = Long.parseLong(snoteIDObj.toString());
		}
	}

	@Get("json")
	public Representation getAllSimpleNotes(){
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<SimpleNote> simpleNotes = mSimpleNoteService.getAllSimpleNotes(sessionHash);

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

	@Put
	public Representation editSimpleNote(Representation entity){
		Form form = new Form(entity);
		try {
			String sessionHash = form.getFirstValue("session_hash");
			String title = form.getFirstValue("title");
			String body = form.getFirstValue("body");
			mSimpleNoteService.editSimpleNote(sessionHash, mSnoteID, title, body);

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

	@Delete
	public Representation deleteSimpleNotes(){
		try {
			String sessionHash = getQuery().getValues("session_hash");
			String[] stringIDs = getQuery().getValuesArray("id");
			Long[] IDs = new Long[stringIDs.length];
			for(int i=0; i<IDs.length; i++){
				IDs[i] = Long.parseLong(stringIDs[i]);
			}
			mSimpleNoteService.deleteSimpleNotes(sessionHash, IDs);

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
