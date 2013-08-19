package domain.response;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.codehaus.jackson.annotate.JsonProperty;

import domain.SimpleNote;

public class SimpleNoteResponse {

	protected SimpleNote mSimpleNote;

	public SimpleNoteResponse(SimpleNote simpleNote) {
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
	public UserPublicResponse getUser() {
		return new UserPublicResponse(
				mSimpleNote.getUserGroup().getUser()
			);
	}
	
	@JsonProperty("group")
	public GroupResponse getGroup() {
		return new GroupResponse(
				mSimpleNote.getUserGroup().getGroup()
			);
	}
	
}
