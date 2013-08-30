package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.Session;

public class SessionRepresentation {

	protected Session mSession;

	public SessionRepresentation(Session session) {
		mSession = session;
	}

	@JsonProperty("session_hash")
	public String getSessionHash() {
		return mSession.getHash();
	}

}