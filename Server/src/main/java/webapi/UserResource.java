package webapi;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class UserResource extends ServerResource {
	
	private String userLogin;
	
//    @Post
//    public String createUser() {
//        return "hello, world";
//    }
    
    @Get
    public String getUser() {
        return "hello, world";
    }

}
