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

import service.SimpleNoteService;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.SimpleNote;
import domain.response.SimpleNoteResponse;

public class SimpleNoteResource extends ServerResource {

	private long snoteID;

	@Override
	protected void doInit() throws ResourceException {
		Object snoteIDObj = getRequest().getAttributes().get("snoteID");
		if (snoteIDObj != null){
			this.snoteID = Long.parseLong(snoteIDObj.toString());
		}
	}
	
	@Get("json")
	public Representation getAllSimpleNotes(){
		SimpleNoteService simpleNoteService = new SimpleNoteService();
		try {
			String sessionHash = getQuery().getValues("session_hash");
			Set<SimpleNote> simpleNotes = simpleNoteService.getAllSimpleNotes(sessionHash);

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
	
	@Put
	public Representation editSimpleNote(Representation entity){
		Form form = new Form(entity);
		SimpleNoteService simpleNoteService = new SimpleNoteService();
		try {
			String sessionHash = form.getFirstValue("session_hash");
			String title = form.getFirstValue("title");
			String body = form.getFirstValue("body");
			simpleNoteService.editSimpleNote(sessionHash, snoteID, title, body);

			return new JacksonRepresentation<Status>( StatusFactory.ok() );
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
	
	@Delete
	public Representation deleteSimpleNotes(){
		SimpleNoteService simpleNoteService = new SimpleNoteService();
		try {
			String sessionHash = getQuery().getValues("session_hash");
			String[] stringIDs = getQuery().getValuesArray("id");
			Long[] IDs = new Long[stringIDs.length];
			for(int i=0; i<IDs.length; i++){
				IDs[i] = Long.parseLong(stringIDs[i]);
			}
			simpleNoteService.deleteSimpleNotes(sessionHash, IDs);

			return new JacksonRepresentation<Status>( StatusFactory.ok() );
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
