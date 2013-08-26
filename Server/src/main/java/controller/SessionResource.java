package controller;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.SessionService;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.Session;
import domain.response.SessionResponse;

public class SessionResource extends ServerResource {

	private String login;

	@Override
	protected void doInit() throws ResourceException {
		this.login = (String) getRequest().getAttributes().get("login");
	}

	@Post
	public Representation openSession(Representation entity) {
		Form form = new Form(entity);
		try {
			SessionService sessionService = new SessionService();

			String password = form.getFirstValue("password");
			String hostIp = getClientInfo().getAddress();
			String hostAgent = getClientInfo().getAgent();

			Session session = sessionService.openSession(login, password, hostIp, hostAgent);

			return new JacksonRepresentation<SessionResponse>(new SessionResponse(session));
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest() );
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

}
