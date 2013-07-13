package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserGroup {
	private long id;
	private long user;
	private long group;
	private long groupRole;
	
	public UserGroup(){
	}
	
	public UserGroup(long user, long group, long groupRole) {
		this.user = user;
		this.group = group;
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
	
//	group
	@ManyToOne
    @JoinColumn(name = "Group")
	@Column(unique = false, nullable = false)
	public long getGroup() {
		return group;
	}
	public void setGroup(long group) {
		this.group = group;
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
