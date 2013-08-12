package controller;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.SimpleNoteService;
import service.exception.BadAuthenticationException;
import controller.representation.Status;
import controller.representation.StatusFactory;

public class SimpleNoteResource extends ServerResource {

	private long snoteID;

	@Override
	protected void doInit() throws ResourceException {
		this.snoteID = Long.parseLong(getRequest().getAttributes().get("snoteID").toString());
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

}
