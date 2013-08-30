package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.UserGroup;

public class UserGroupRepresentation {

	protected UserGroup mUserGroup;

	public UserGroupRepresentation(UserGroup userGroup) {
		mUserGroup = userGroup;
	}

	@JsonProperty("group")
	public GroupRepresentation getSlug() {
		return new GroupRepresentation(
				mUserGroup.getGroup()
				);
	}

	@JsonProperty("group_role")
	public String groupRole() {
		return mUserGroup.getGroupRole().getRole();
	}

}
