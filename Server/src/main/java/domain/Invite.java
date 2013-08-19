package domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Invite {
	
	private long mId;
	private User mUser;
	private UserGroup mUserGroup;
	private GroupRole mGroupRole;
	
	public Invite(){
	}
	
	public Invite(User user, UserGroup userGroup, GroupRole groupRole) {
		mUser = user;
		mUserGroup = userGroup;
		mGroupRole = groupRole;
	}
	
	// user
	@ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name="[user]")
	public User getUser() {
		return mUser;
	}
	public void setUser(User user) {
		this.mUser = user;
	}
	
	// userGroup
	@ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name="[userGroup]")
	public UserGroup getUserGroup() {
		return mUserGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.mUserGroup = userGroup;
	}
	
	// groupRole
	@ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name="[groupRole]")
	public GroupRole getGroupRole() {
		return mGroupRole;
	}
	public void setGroupRole(GroupRole groupRole) {
		this.mGroupRole = groupRole;
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
		Invite other = (Invite) obj;
		if (mId != other.mId)
			return false;
		return true;
	}

}
