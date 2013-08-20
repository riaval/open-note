package domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Session {

	private long mId;
	private User mUser;
	private String mHash;

	public Session(){
	}

	public Session(String hash) {
		mHash = hash;
	}

	// user
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="[user]", nullable=false)
	public User getUser() {
		return mUser;
	}
	public void setUser(User user) {
		this.mUser = user;
	}

	// hash
	@Column(unique=true, nullable = false, length = 140)
	public String getHash() {
		return mHash;
	}
	public void setHash(String hash){
		this.mHash = hash;
	}

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	public long getId() {
		return mId;
	}
	protected void setId(long id){
		this.mId = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (mId ^ (mId >>> 32));
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
		if (mId != other.mId)
			return false;
		return true;
	}

}
