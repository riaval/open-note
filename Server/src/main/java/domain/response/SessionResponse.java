package domain.response;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.Session;

public class SessionResponse {

	protected Session mSession;

	public SessionResponse(Session session) {
		mSession = session;
	}

	@JsonProperty("session_hash")
	public String getSessionHash() {
		return mSession.getHash();
	}

}
