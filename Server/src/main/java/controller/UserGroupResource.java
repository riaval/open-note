package controller;

import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class UserGroupResource extends ServerResource{

	private String groupSlug;
	private String userLogin;
	
	@Override
	protected void doInit() throws ResourceException {
		this.groupSlug = (String) getRequest().getAttributes().get("groupSlug");
		this.userLogin = (String) getRequest().getAttributes().get("userLogin");
	}
	
}
