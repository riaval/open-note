package webapi;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class GroupResource extends ServerResource {

    @Post
    public String createGroup() {
        return "hello, world";
    }

}

