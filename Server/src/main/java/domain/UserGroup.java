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

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class UserGroup {
	private long id;
	private User user;
	private Group group;
	private GroupRole groupRole;
	private Set<SimpleNote> simpleNotes = new HashSet<SimpleNote>();
	private Set<Invite> invites = new HashSet<Invite>();
	
	public UserGroup(){
	}
	
	public UserGroup(User user, Group group, GroupRole groupRole) {
		this.user = user;
		this.group = group;
		this.groupRole = groupRole;
	}
	
//	user
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "[user]")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
//	group
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "[group]")
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
//	groupRole
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "[groupRole]")
	public GroupRole getGroupRole() {
		return groupRole;
	}
	public void setGroupRole(GroupRole groupRole) {
		this.groupRole = groupRole;
	}
	
//	simpleNotes
	@JsonIgnore
	@OneToMany(mappedBy="userGroup", fetch=FetchType.EAGER)
	public Set<SimpleNote> getSimpleNotes(){
		return simpleNotes;
	}
	public void setSimpleNotes(Set<SimpleNote> simpleNotes){
		this.simpleNotes = simpleNotes;
	}
	
//	invites
	@JsonIgnore
	@OneToMany(mappedBy="userGroup", fetch=FetchType.EAGER)
	public Set<Invite> getInvites(){
		return invites;
	}
	public void setInvites(Set<Invite> invites){
		this.invites = invites;
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
