package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class HttpStatusFactory {

	static public class Json {

		static public String clientBadRequest() {
			return generate("Bad Request", 400);
		}

		static public String clientUnauthorized() {
			return generate("Unauthorized", 401);
		}

		static public String clientNotFound() {
			return generate("Not Found", 404);
		}

		static public String clientTooLargeEntity() {
			return generate("Request Entity Too Large", 413);
		}

		static public String serverInternalError() {
			return generate("Internal Server Error", 500);
		}

		static public String serverServiceUnavailable() {
			return generate("Service Unavailable", 503);
		}

		static private String generate(String message, int code) {
			JsonObject inside = new JsonObject();
			inside.add("message", new JsonPrimitive(message));
			inside.add("code", new JsonPrimitive(code));

			JsonObject outside = new JsonObject();
			outside.add("errors", inside);

			return outside.toString();
		}
	};

}
