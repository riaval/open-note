package controller;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import service.UserService;

import com.google.gson.Gson;

import domain.User;

public class UserResource extends ServerResource {
	
	private String userLogin;
	
    @Override
    protected void doInit() throws ResourceException {
        this.userLogin = (String) getRequest().getAttributes().get("userLogin");

        // Get the item directly from the "persistence layer".
//        this.item = getItems().get(itemName);
//
//        setExisting(this.item != null);
    }
	
    @Get
    public String createUser() {
//		UserDAO userDAO = new UserDAO();
		try {
			UserService us = new UserService();
			long sessionID = us.createUser(userLogin, "qqqqname2", "qqqqqpass2", "qqqqmail2", getClientInfo().getAddress());
//			User user = userDAO.createUser(
//					  userLogin
//					, getQueryValue("fullName")
//					, getQueryValue("password")
//					, getQueryValue("email")
//			);
//			setStatus(Status.SUCCESS_CREATED);
//			return Status.SUCCESS_CREATED.toString();
			return ((Long)sessionID).toString();
		} catch (Exception e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
			return Status.CLIENT_ERROR_BAD_REQUEST.toString();
		}
		    	
    	
        
    }
    
//    @Get("json")
//    public String getUserData() {
//    	try {
//    		UserDAO userDAO = new UserDAO();
//			User user = userDAO.retrieveUser(userLogin);
//			
//			Gson gson = new Gson();
//			return gson.toJson(user);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "Error";
//		}
//    	
//        
//    }

}
