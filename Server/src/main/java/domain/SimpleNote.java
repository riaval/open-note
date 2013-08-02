package domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class SimpleNote {
	private long id;
	private String title;
	private String body;
	private Date date;
	private UserGroup userGroup = new UserGroup();	
	
	public SimpleNote(){
	}
	
	public SimpleNote(String title, String body) {
		this.title = title;
		this.body = body;
		this.date = Calendar.getInstance().getTime();
	}
	
//	title
	@Column(unique=false, nullable = false, length = 45)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

//	body
	@Column(unique=false, nullable = true, length = 250)
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

//	date
	@JsonIgnore
	@Column(unique=false, nullable = false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
//	UserGroup
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="[userGroup]", nullable=false)
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
	@Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    protected long getId() {
        return id;
    }
	protected void setId(long id){
		this.id = id;
	}

//	JSON
	@JsonProperty("user")
	public String jsonUser() {
		return this.userGroup.getUser().getLogin();
	}
	
	@JsonProperty("date")
	public String jsonDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM. HH:mm", Locale.US);
		return sdf.format(date);
	}
	
}