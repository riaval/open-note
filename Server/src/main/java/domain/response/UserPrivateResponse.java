package domain.response;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.User;

public class UserPrivateResponse extends UserPublicResponse {

	public UserPrivateResponse(User user) {
		super(user);
	}

	@JsonProperty("email")
	public String getEmail() {
		return mUser.getEmail();
	}

}
