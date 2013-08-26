package controller.representation;

public class StatusFactory {

	static private Status sOk;
	static private Status sCreated;
	static private Status sBadRequest;
	static private Status sUnauthorized;
	static private Status sNotFound;
	static private Status sTooLarge;
	static private Status sInternalError;
	static private Status sServiceUnavailable;

	static public Status ok() {
		if (sOk == null) {
			sOk = new Status("OK", 200);
		}
		return sOk;
	}

	static public Status created() {
		if (sCreated == null) {
			sCreated = new Status("Created ", 201);
		}
		return sCreated;
	}

	static public Status clientBadRequest() {
		if (sBadRequest == null) {
			sBadRequest = new Status("Bad Request", 400);
		}
		return sBadRequest;
	}

	static public Status clientUnauthorized() {
		if (sUnauthorized == null) {
			sUnauthorized = new Status("Unauthorized", 401);
		}
		return sUnauthorized;
	}

	static public Status clientNotFound() {
		if (sNotFound == null) {
			sNotFound = new Status("Not Found", 404);
		}
		return sNotFound;
	}

	static public Status clientTooLargeEntity() {
		if (sTooLarge == null) {
			sTooLarge = new Status("Request Entity Too Large", 413);
		}
		return sTooLarge;
	}

	static public Status serverInternalError() {
		if (sInternalError == null) {
			sInternalError = new Status("Internal Server Error", 500);
		}
		return sInternalError;
	}

	static public Status serverServiceUnavailable() {
		if (sServiceUnavailable == null) {
			sServiceUnavailable = new Status("Service Unavailable", 503);
		}
		return sServiceUnavailable;
	}

	// Show message about errors
	static public String getErrorMessage(Exception e) {
		return e.getClass().getName() + " : " + e.getMessage();
	}

}
