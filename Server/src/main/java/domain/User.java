package domain;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class User {
	private long id;
	private String login;
	private String fullName;
	private String passwordHash;
	private String email;
	private Date date;
	private int color;
	private Set<Session> sessions = new HashSet<Session>();
	private Set<UserGroup> userGroups = new HashSet<UserGroup>();
	private Set<Invite> invites = new HashSet<Invite>();
	
	public User(){
	}
	
	public User(String login, String fullName, String passwordHash) {
		this.login = login;
		this.fullName = fullName;
		this.passwordHash = passwordHash;
//		this.email = ""; null or ""
		this.date = Calendar.getInstance().getTime();
		this.color = generateRandomColor(new Color(255, 255, 255)).getRGB();
	}
	
//	login
	@Column(unique=true, nullable = false, length = 24)
	public String getLogin() {
		return login;
	}
	public void setLogin(String login){
		this.login = login;
	}
	
//	full name
	@Column(unique=false, nullable = false, length = 36)
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String firstName){
		this.fullName = firstName;
	}
	
//	passwordHash
	@JsonIgnore
	@Column(unique=false, nullable = false, length = 124)
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash){
		this.passwordHash = passwordHash;
	}
	
//	email
	@JsonIgnore
	@Column(unique=false, nullable = true, length = 45)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
//	date
	@JsonIgnore
	@Column(unique=false, nullable = false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date){
		this.date = date;
	}
	
//	sessions
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public Set<Session> getSession(){
		return sessions;
	}
	public void setSession(Set<Session> sessions){
		this.sessions = sessions;
	}
	
//	UserGroup
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<UserGroup> getUserGroups() {
		return this.userGroups;
	}
	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
//	Invite
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<Invite> getInvites() {
		return invites;
	}
	public void setInvites(Set<Invite> invites) {
		this.invites = invites;
	}
	
	@Column(unique=false, nullable = true)
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
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

	@JsonProperty("date")
	public String jsonDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.US);
		return sdf.format(date);
	}
	
	private Color generateRandomColor(Color mix) {
	    Random random = new Random();
	    int red = random.nextInt(256);
	    int green = random.nextInt(256);
	    int blue = random.nextInt(256);

	    // mix the color
	    if (mix != null) {
	        red = (red + mix.getRed()) / 2;
	        green = (green + mix.getGreen()) / 2;
	        blue = (blue + mix.getBlue()) / 2;
	    }

	    Color color = new Color(red, green, blue);
	    return color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
