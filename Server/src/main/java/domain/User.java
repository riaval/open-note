package domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class User {
	private long id;
	private String login;
	private String fullName;
	private String passwordHash;
	private String email;
	private Date date;
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
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	public Set<Session> getSession(){
		return sessions;
	}
	public void setSession(Set<Session> sessions){
		this.sessions = sessions;
	}
	
//	UserGroup
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	public Set<UserGroup> getUserGroups() {
		return this.userGroups;
	}
	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
//	Invite
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	public Set<Invite> getInvites() {
		return invites;
	}
	public void setInvites(Set<Invite> invites) {
		this.invites = invites;
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

}
