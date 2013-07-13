package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GroupRole {
	private long id;
	private String role;
	
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
