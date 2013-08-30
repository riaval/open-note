package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.Invite;

public class InviteRepresentation {

	protected Invite mInvite;

	public InviteRepresentation(Invite invite) {
		mInvite = invite;
	}

	@JsonProperty("id")
	public long getId() {
		return mInvite.getId();
	}

	@JsonProperty("user")
	public UserPublicRepresentation getUser() {
		return new UserPublicRepresentation(
				mInvite.getUserGroup().getUser()
				);
	}

	@JsonProperty("group")
	public GroupRepresentation getGroup() {
		return new GroupRepresentation(
				mInvite.getUserGroup().getGroup()
				);
	}

}