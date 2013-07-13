package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Session {
	private long id;
	private User user;
	private String ip;
	
	public Session(){
	}
	
	public Session(String ip) {
		this.ip = ip;
	}
	
//	user
	@ManyToOne
    @JoinColumn(name="user", referencedColumnName="id", nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

//	ip
	@Column(unique=false, nullable = false, length = 40)
	public String getIp() {
        return ip;
    }
	public void setIp(String ip){
		this.ip = ip;
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
