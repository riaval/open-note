package controller.representation;

public class StatusFactory {

	static public Status ok() {
		return new Status("OK", 200);
	}
	
	static public Status created () {
		return new Status("Created ", 201);
	}

	static public Status clientBadRequest() {
		return new Status("Bad Request", 400);
	}

	static public Status clientUnauthorized() {
		return new Status("Unauthorized", 401);
	}

	static public Status clientNotFound() {
		return new Status("Not Found", 404);
	}

	static public Status clientTooLargeEntity() {
		return new Status("Request Entity Too Large", 413);
	}

	static public Status serverInternalError() {
		return new Status("Internal Server Error", 500);
	}

	static public Status serverServiceUnavailable() {
		return new Status("Service Unavailable", 503);
	}

}
