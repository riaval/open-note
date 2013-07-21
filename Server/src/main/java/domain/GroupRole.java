package domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class GroupRole {
	private long id;
	private String role;
	private Set<UserGroup> userGroups = new HashSet<UserGroup>();	
	
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
	
//	UserGroup
	@JsonIgnore
	@OneToMany(mappedBy="groupRole")
	public Set<UserGroup> getUserGroup() {
		return this.userGroups;
	}
	public void setUserGroup(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
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
