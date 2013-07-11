package webapi;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import data.dao.UserDAO;
import data.entity.User;

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
	
    @Post
    public String createUser() {
		UserDAO userDAO = new UserDAO();
		try {
			User user = userDAO.createUser(
					  userLogin
					, getQueryValue("email")
					, getQueryValue("password")
					, getQueryValue("firstName")
					, getQueryValue("secondName")
			);
			setStatus(Status.SUCCESS_CREATED);
			return Status.SUCCESS_CREATED.toString();
		} catch (Exception e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
			return Status.CLIENT_ERROR_BAD_REQUEST.toString();
		}
		    	
    	
        
    }
    
    @Get("json")
    public String getUserData() {
    	try {
    		UserDAO userDAO = new UserDAO();
			User user = userDAO.retrieveUser(userLogin);
			
			Gson gson = new Gson();
			return gson.toJson(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
    	
        
    }

}
