package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.User;

public class UserPrivateRepresentation extends UserPublicRepresentation {

	public UserPrivateRepresentation(User user) {
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