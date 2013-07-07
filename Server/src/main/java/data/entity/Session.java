package data.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Session {
	Long id;
	Long user;
	Date time;	
	String host;
	String ip;
	
//	time
	@Column(unique=false, nullable = false)
	public Date getTime() {
        return time;
    }
	public void setTime(Date time){
		this.time = time;
	}
	
//	user
	@OneToOne
    @JoinColumn(name = "task_status_id")
    @Column(unique = true, nullable = false)
    protected long getUser() {
        return user;
    }
	protected void setUser(Long user){
		this.user = user;
	}
	
//	host
	@Column(unique=false, nullable = false)
	public String getHost() {
        return host;
    }
	public void setLogin(String host){
		this.host = host;
	}
	
//	ip
	@Column(unique=false, nullable = false)
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
	protected void setId(Long id){
		this.id = id;
	}
}
