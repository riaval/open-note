package domain.response;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.User;

public class UserPrivateResponse extends UserPublicResponse {

	public UserPrivateResponse(User user) {
		super(user);
	}

	@JsonProperty("email")
	public String getEmail() {
		String email = mUser.getEmail();
		if (email == null) {
			return "";
		} else {
			return mUser.getEmail();
		}
	}

}