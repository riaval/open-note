package controller.representation;

public class StatusFactory {

	static public Status ok() {
		return new Status("OK", 200, null);
	}

	static public Status created() {
		return new Status("Created ", 201, null);
	}

	static public Status clientBadRequest(String comment) {
		return new Status("Bad Request", 400, comment);
	}

	static public Status clientUnauthorized(String comment) {
		return new Status("Unauthorized", 401, comment);
	}

	static public Status clientNotFound(String comment) {
		return new Status("Not Found", 404, comment);
	}

	static public Status clientTooLargeEntity(String comment) {
		return new Status("Request Entity Too Large", 413, comment);
	}

	static public Status serverInternalError() {
		return new Status("Internal Server Error", 500, "Internal server error.");
	}

	static public Status serverServiceUnavailable(String comment) {
		return new Status("Service Unavailable", 503, comment);
	}

	// Show message about error
	static public String getErrorMessage(Exception e) {
		return e.getClass().getName() + " : " + e.getMessage();
	}

}
