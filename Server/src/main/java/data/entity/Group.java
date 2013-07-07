package data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Group {
	Long id;
	String name;
	
//	name
	@Column(unique=false, nullable = false)
	public String getName() {
        return name;
    }
	public void setName(String name){
		this.name = name;
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
