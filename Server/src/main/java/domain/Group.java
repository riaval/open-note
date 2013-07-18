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
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
@Table(catalog = "opennote")
public class Group{
	private long id;
	private String slug;
	private String name;
	private Map<User, GroupRole> users = new HashMap<User, GroupRole>();
	
	
	public Group(){
	}
	
	public Group(String slug, String name) {
		this.slug = slug;
		this.name = name;
	}
	
//	slug
	@Column(name = "[slug]", unique=true, nullable = false, length = 20)
	public String getSlug() {
	    return slug;
	}
	public void setSlug(String slug){
		this.slug = slug;
	}
	
//	name
	@Column(name = "[name]", unique=false, nullable = false, length = 30)
	public String getName() {
	    return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
//	User_Role
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "UserGroup", catalog = "opennote",
		joinColumns = @JoinColumn(name = "[group]", nullable = false, unique = false), 
		inverseJoinColumns = @JoinColumn(name = "groupRole",	nullable = false, unique = false) )
	@MapKeyJoinColumn(name = "user")
	@ElementCollection	
	public Map<User, GroupRole> getUsers() {
		return this.users;
	}
	public void setUsers(Map<User, GroupRole> users) {
		this.users = users;
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
}
