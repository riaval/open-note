package domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
@Table(catalog="opennote")
public class Group{
	
	private long id;
	private String slug;
	private String name;
	private Set<UserGroup> userGroups = new HashSet<UserGroup>();	
//	private Set<SimpleNote> simpleNotes = new HashSet<SimpleNote>();
	
	public Group(){
	}
	
	public Group(String slug, String name) {
		this.slug = slug;
		this.name = name;
	}
	
//	slug
	@Column(name="[slug]", unique=true, nullable = false, length = 20)
	public String getSlug() {
	    return slug;
	}
	public void setSlug(String slug){
		this.slug = slug;
	}
	
//	name
	@Column(name="[name]", unique=false, nullable = false, length = 30)
	public String getName() {
	    return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
//	User_Role
	@JsonIgnore
	@OneToMany(mappedBy="group", cascade=CascadeType.ALL)
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
	
	public String toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("slug", this.slug);
		json.put("name", this.name);
		return json.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
