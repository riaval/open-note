package data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GroupRole {
	Long id;
	String role;
	
//	role
	@Column(unique=false, nullable = false)
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
	protected void setId(Long id){
		this.id = id;
	}
}
