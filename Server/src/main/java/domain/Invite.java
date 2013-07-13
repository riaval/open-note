package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Invite {
	private long id;
	private long user;
	private long userGroup;
	private long groupRole;
	
	public Invite(){
	}
	
	public Invite(long user, long userGroup, long groupRole) {
		this.user = user;
		this.userGroup = userGroup;
		this.groupRole = groupRole;
	}
	
//	user
	@ManyToOne
    @JoinColumn(name = "User")
	@Column(unique = false, nullable = false)
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	
//	userGroup
	@ManyToOne
    @JoinColumn(name = "UserGroup")
	@Column(unique = false, nullable = false)
	public long getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(long userGroup) {
		this.userGroup = userGroup;
	}
	
//	groupRole
	@ManyToOne
    @JoinColumn(name = "GroupRole")
	@Column(unique = false, nullable = false)
	public long getGroupRole() {
		return groupRole;
	}
	public void setGroupRole(long groupRole) {
		this.groupRole = groupRole;
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
