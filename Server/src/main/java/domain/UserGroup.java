package domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class UserGroup {
	
	private long mId;
	private User mUser;
	private Group mGroup;
	private GroupRole mGroupRole;
	private Set<SimpleNote> mSimpleNotes = new HashSet<SimpleNote>();
	private Set<Invite> mInvites = new HashSet<Invite>();
	
	public UserGroup(){
	}
	
	public UserGroup(User user, Group group, GroupRole groupRole) {
		this.mUser = user;
		this.mGroup = group;
		this.mGroupRole = groupRole;
	}
	
	// user
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
    @JoinColumn(name = "[user]")
	public User getUser() {
		return mUser;
	}
	public void setUser(User user) {
		this.mUser = user;
	}
	
	// group
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
    @JoinColumn(name = "[group]")
	public Group getGroup() {
		return mGroup;
	}
	public void setGroup(Group group) {
		this.mGroup = group;
	}
	
	// groupRole
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
    @JoinColumn(name = "[groupRole]")
	public GroupRole getGroupRole() {
		return mGroupRole;
	}
	public void setGroupRole(GroupRole groupRole) {
		this.mGroupRole = groupRole;
	}
	
	// simpleNotes
	@OneToMany(mappedBy="userGroup", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<SimpleNote> getSimpleNotes(){
		return mSimpleNotes;
	}
	public void setSimpleNotes(Set<SimpleNote> simpleNotes){
		this.mSimpleNotes = simpleNotes;
	}
	
	// invites
	@OneToMany(mappedBy="userGroup", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<Invite> getInvites(){
		return mInvites;
	}
	public void setInvites(Set<Invite> invites){
		this.mInvites = invites;
	}
	
	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	protected long getId() {
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
		UserGroup other = (UserGroup) obj;
		if (mId != other.mId)
			return false;
		return true;
	}
	
}
