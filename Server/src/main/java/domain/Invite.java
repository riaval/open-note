package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class Invite {
	private long id;
	private User user;
	private UserGroup userGroup;
	private GroupRole groupRole;
	
	public Invite(){
	}
	
	public Invite(User user, UserGroup userGroup, GroupRole groupRole) {
		this.user = user;
		this.userGroup = userGroup;
		this.groupRole = groupRole;
	}
	
//	user
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "[user]")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
//	userGroup
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "[userGroup]")
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
//	groupRole
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "[groupRole]")
	public GroupRole getGroupRole() {
		return groupRole;
	}
	public void setGroupRole(GroupRole groupRole) {
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
	
	//Json
	@JsonProperty("user")
	public User user() {
		return userGroup.getUser();
	}
	@JsonProperty("group")
	public Group group() {
		return userGroup.getGroup();
	}
	@JsonProperty("id")
	public long id() {
		return id;
	}
}
