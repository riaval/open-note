package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

public class Status {

	@JsonProperty("status")
	public StatusValue mStatus;

	@JsonProperty("comment")
	public String mComment;

	public Status(String message, int code, String comment) {
		mStatus = new StatusValue(message, code);
		mComment = comment;
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
