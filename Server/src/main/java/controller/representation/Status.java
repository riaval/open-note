package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

public class Status {

	@JsonProperty("status")
	public StatusValue mStatus;

	public Status(String message, int code) {
		mStatus = new StatusValue(message, code);
	}

	class StatusValue {
		@JsonProperty("message")
		public String mMessage;
		@JsonProperty("code")
		public int mCode;

		StatusValue(String message, int code) {
			mMessage = message;
			mCode = code;
		}
	}

}
