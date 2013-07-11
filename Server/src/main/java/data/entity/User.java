package data.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	long id;
	String login;
	String email;
	String password;
	String firstName;
	String secondName;
	Date date;
	
	public User(){
	}
	
	public User(String login, String email, String password, String firstName, String secondName) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
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
	
//	email
	@Column(unique=true, nullable = false, length = 45)
	public String getEmail() {
        return email;
    }
	public void setEmail(String email){
		this.email = email;
	}
	
//	password
	@Column(unique=false, nullable = false, length = 24)
	public String getPassword() {
        return password;
    }
	public void setPassword(String password){
		this.password = password;
	}
	
//	first name
	@Column(unique=false, nullable = false, length = 36)
	public String getFirstName() {
        return firstName;
    }
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
//	second name
	@Column(unique=false, nullable = false, length = 36)
	public String getSecondName() {
        return secondName;
    }
	public void setSecondName(String secondName){
		this.secondName = secondName;
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
