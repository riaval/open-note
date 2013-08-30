package controller.representation;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.Group;

public class GroupRepresentation {

	protected Group mGroup;

	public GroupRepresentation(Group group) {
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