package controller.representation;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.SimpleNote;

public class SimpleNoteRepresentation {

	protected SimpleNote mSimpleNote;

	public SimpleNoteRepresentation(SimpleNote simpleNote) {
		mSimpleNote = simpleNote;
	}

	@JsonProperty("id")
	public long getID() {
		return mSimpleNote.getId();
	}

	@JsonProperty("title")
	public String getTitle() {
		return mSimpleNote.getTitle();
	}

	@JsonProperty("body")
	public String getBody() {
		return mSimpleNote.getBody();
	}

	@JsonProperty("date")
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM. HH:mm", Locale.US);
		return sdf.format(mSimpleNote.getDate());
	}

	@JsonProperty("user")
	public UserPublicRepresentation getUser() {
		return new UserPublicRepresentation(
				mSimpleNote.getUserGroup().getUser()
				);
	}

	@JsonProperty("group")
	public GroupRepresentation getGroup() {
		return new GroupRepresentation(
				mSimpleNote.getUserGroup().getGroup()
				);
	}

}
