package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Session {
	private long id;
	private User user;
	private String hash;
	
	public Session(){
	}
	
	public Session(String hash) {
		this.hash = hash;
	}
	
//	user
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="user", referencedColumnName="id", nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

//	hash
	@Column(unique=true, nullable = false, length = 140)
	public String getHash() {
        return hash;
    }
	public void setHash(String hash){
		this.hash = hash;
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
