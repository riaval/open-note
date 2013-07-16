package domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	private long id;
	private String login;
	private String fullName;
	private String passwordHash;
	private String email;
	private Date date;
	List<Session> sessions;
	
	public User(){
	}
	
	public User(String login, String fullName, String passwordHash) {
		this.login = login;
		this.fullName = fullName;
		this.passwordHash = passwordHash;
//		this.email = ""; null or ""
		date = Calendar.getInstance().getTime();
	}
	
	@OneToMany(mappedBy="user")
	public List<Session> getSession(){
		return sessions;
	}
	public void setSession(List<Session> sessions){
		this.sessions = sessions;
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
