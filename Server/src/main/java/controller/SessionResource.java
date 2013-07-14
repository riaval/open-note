package controller;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.SessionService;

public class SessionResource extends ServerResource {
	
	private String userLogin;
	
    @Override
    protected void doInit() throws ResourceException {
        this.userLogin = (String) getRequest().getAttributes().get("userLogin");
    }
	
	@Post
	public String openSession(){
		try {
			SessionService ss = new SessionService();
			String hostIp = getClientInfo().getAddress();
			long sessionId = ss.openSession(userLogin, "qqqqqpass2", hostIp);
			return ((Long)sessionId).toString();
		} catch (Exception e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
			return Status.CLIENT_ERROR_BAD_REQUEST.toString();
		}
	}
	
}
