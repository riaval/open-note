package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Group {
	private long id;
	private String slug;
	private String name;
	
	public Group(){
	}
	
	public Group(String slug, String name) {
		this.slug = slug;
		this.name = name;
	}
	
//	slug
	@Column(unique=true, nullable = false, length = 20)
	public String getSlug() {
	    return slug;
	}
	public void setSlug(String slug){
		this.slug = slug;
	}
	
//	name
	@Column(unique=false, nullable = false, length = 30)
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
	protected void setId(long id){
		this.id = id;
	}
}
