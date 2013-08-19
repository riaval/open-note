package domain.response;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.Invite;

public class InviteResponse {

	protected Invite mInvite;

	public InviteResponse(Invite invite) {
		mInvite = invite;
	}

	@JsonProperty("id")
	public long getId() {
		return mInvite.getId();
	}
	
	@JsonProperty("user")
	public UserPublicResponse getUser() {
		return new UserPublicResponse(
				mInvite.getUserGroup().getUser()
			);
	}
	
	@JsonProperty("group")
	public GroupResponse getGroup() {
		return new GroupResponse(
				mInvite.getUserGroup().getGroup()
			);
	}

}
