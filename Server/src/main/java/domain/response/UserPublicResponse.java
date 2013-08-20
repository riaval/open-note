package domain.response;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.User;

public class UserPublicResponse {

	protected User mUser;

	public UserPublicResponse(User user){
		mUser = user;
	}

	@JsonProperty("login")
	public String getLogin(){
		return mUser.getLogin();
	}

	@JsonProperty("full_name")
	public String getFullName(){
		return mUser.getFullName();
	}

	@JsonProperty("color")
	public int getColor(){
		return mUser.getColor();
	}

	@JsonProperty("date")
	public String getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.US);
		return sdf.format(mUser.getDate());
	}

}
