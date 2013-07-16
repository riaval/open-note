package controller;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class WebAPIApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        router.attach("/users/{userLogin}", UserResource.class);
        router.attach("/sessions/{userLogin}", SessionResource.class);
        
//        Group
//        router.attach("/groups/{groupSlug}", GroupResource.class);
//        router.attach("/groups/", GroupResource.class);
        
        return router;
    }

}
