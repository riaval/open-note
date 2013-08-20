package domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class GroupRole {

	private long mId;
	private String mRole;
	private Set<UserGroup> mUserGroups = new HashSet<UserGroup>();	

	public GroupRole(){
	}

	public GroupRole(String role) {
		mRole = role;
	}

	// role
	@Column(name="[role]", unique=true, nullable = false, length = 20)
	public String getRole() {
		return mRole;
	}
	public void setRole(String role){
		this.mRole = role;
	}

	// userGroups
	@OneToMany(mappedBy="groupRole", cascade=CascadeType.REFRESH)
	public Set<UserGroup> getUserGroup() {
		return this.mUserGroups;
	}
	public void setUserGroup(Set<UserGroup> userGroups) {
		this.mUserGroups = userGroups;
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
		GroupRole other = (GroupRole) obj;
		if (mId != other.mId)
			return false;
		return true;
	}

}
