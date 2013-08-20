package domain.response;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.UserGroup;

public class UserGroupResponse {

	protected UserGroup mUserGroup;

	public UserGroupResponse(UserGroup userGroup) {
		mUserGroup = userGroup;
	}

	@JsonProperty("group")
	public GroupResponse getSlug() {
		return new GroupResponse(
				mUserGroup.getGroup()
				);
	}

	@JsonProperty("group_role")
	public String groupRole() {
		return mUserGroup.getGroupRole().getRole();
	}

}
