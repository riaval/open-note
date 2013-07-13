package domain;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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
	private String password;
	private String email;
	private Date date;
	Set<Session> sessions;
	
	public User(){
	}
	
	public User(String login, String fullName, String password, String email) {
        this.login = login;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        date = Calendar.getInstance().getTime();
    }
	
	@OneToMany(mappedBy="user")
	public Set<Session> getSession(){
		return sessions;
	}
	public void setSession(Set<Session> sessions){
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
	
//	password
	@Column(unique=false, nullable = false, length = 24)
	public String getPassword() {
        return password;
    }
	public void setPassword(String password){
		this.password = password;
	}
	
//	email
	@Column(unique=true, nullable = false, length = 45)
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
    protected long getId() {
        return id;
    }
	protected void setId(long id){
		this.id = id;
	}
}
