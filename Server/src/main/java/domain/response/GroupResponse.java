package domain.response;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.Group;

public class GroupResponse {

	protected Group mGroup;

	public GroupResponse(Group group) {
		mGroup = group;
	}

	@JsonProperty("slug")
	public String getSlug() {
		return mGroup.getSlug();
	}

	@JsonProperty("name")
	public String getName() {
		return mGroup.getName();
	}

}