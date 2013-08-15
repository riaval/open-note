package domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name="[user]", nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

//	hash
	@JsonProperty("session_hash")
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
		Session other = (Session) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
