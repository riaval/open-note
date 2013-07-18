package domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class GroupRole {
	private long id;
	private String role;
	private Map<Group, User> groupUser = new HashMap<Group, User>();	
	
	public GroupRole(){
	}
	
	public GroupRole(String role) {
		this.role = role;
	}
	
	//	role
	@Column(unique=true, nullable = false, length = 20)
	public String getRole() {
        return role;
    }
	public void setRole(String role){
		this.role = role;
	}
	
//	Group_User
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "UserGroup", catalog = "opennote",
		joinColumns = @JoinColumn(name = "groupRole", nullable = false, unique = false), 
		inverseJoinColumns = @JoinColumn(name = "user",	nullable = false, unique = false) )
	@MapKeyJoinColumn(name = "[group]")
	@ElementCollection
	public Map<Group, User> getGroupUser() {
		return this.groupUser;
	}
	public void setGroupUser(Map<Group, User> groupUser) {
		this.groupUser = groupUser;
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
