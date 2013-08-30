package controller;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.ServiceFactory;
import service.SessionService;
import service.implementation.SessionServiceImpl;
import controller.representation.SessionRepresentation;
import controller.representation.Status;
import controller.representation.StatusFactory;
import domain.Session;

public class SessionResource extends ServerResource {

	private String mLogin;
	private SessionService mSessionService = new SessionServiceImpl();

	@Override
	protected void doInit() throws ResourceException {
		mSessionService = ServiceFactory.getSessionService();
		this.mLogin = (String) getRequest().getAttributes().get("login");
	}

	@Post
	public Representation openSession(Representation entity) {
		Form form = new Form(entity);
		try {
			String password = form.getFirstValue("password");
			String hostIp = getClientInfo().getAddress();
			String hostAgent = getClientInfo().getAgent();

			Session session = mSessionService.openSession(mLogin, password, hostIp, hostAgent);

			return new JacksonRepresentation<SessionRepresentation>(new SessionRepresentation(session));
		} catch (IllegalArgumentException e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.clientBadRequest(e.getMessage()));
		} catch (Exception e) {
			System.err.println(StatusFactory.getErrorMessage(e));
			return new JacksonRepresentation<Status>( StatusFactory.serverInternalError() );
		}
	}

}
