package domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
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
	private Map<Group, GroupRole> groups = new HashMap<Group, GroupRole>();
	
	public User(){
	}
	
	public User(String login, String fullName, String passwordHash) {
		this.login = login;
		this.fullName = fullName;
		this.passwordHash = passwordHash;
//		this.email = ""; null or ""
		date = Calendar.getInstance().getTime();
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
	@Column(unique=false, nullable = false, length = 124)
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash){
		this.passwordHash = passwordHash;
	}
	
//	email
	@Column(unique=false, nullable = true, length = 45)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
//	date
	@Column(unique=false, nullable = false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date){
		this.date = date;
	}
	
//	sessions
	@JsonIgnore
	@OneToMany(mappedBy="user")
	public Set<Session> getSession(){
		return sessions;
	}
	public void setSession(Set<Session> sessions){
		this.sessions = sessions;
	}
	
//	Group_Role
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "UserGroup", catalog = "opennote",
		joinColumns = @JoinColumn(name = "user", nullable = false, unique = false), 
		inverseJoinColumns = @JoinColumn(name = "groupRole",	nullable = false, unique = false) )
	@MapKeyJoinColumn(name = "[group]")
	@ElementCollection	
	public Map<Group, GroupRole> getGroups() {
		return this.groups;
	}
	public void setGroups(Map<Group, GroupRole> groups) {
		this.groups = groups;
	}

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	public long getId() {
		return id;
	}
	protected void setId(long id){
		this.id = id;
	}
}
