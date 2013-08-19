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

@Entity
@Table(catalog="opennote")
public class Group{
	
	private long mId;
	private String mSlug;
	private String mName;
	private Set<UserGroup> mUserGroups = new HashSet<UserGroup>();	
	
	public Group(){
	}
	
	public Group(String slug, String name) {
		this.mSlug = slug;
		this.mName = name;
	}
	
	// slug
	@Column(name="[slug]", unique=true, nullable = false, length = 20)
	public String getSlug() {
	    return mSlug;
	}
	public void setSlug(String slug){
		this.mSlug = slug;
	}
	
	// name
	@Column(name="[name]", unique=false, nullable = false, length = 30)
	public String getName() {
	    return mName;
	}
	public void setName(String name){
		this.mName = name;
	}
	
	// userGroups
	@OneToMany(mappedBy="group", cascade=CascadeType.REFRESH)
	public Set<UserGroup> getUserGroup() {
		return this.mUserGroups;
	}
	public void setUserGroup(Set<UserGroup> userGroups) {
		this.mUserGroups = userGroups;
	}
	
	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	protected long getId() {
		return mId;
	}
	protected void setId(long id){
		this.mId = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (mId ^ (mId >>> 32));
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
		if (mId != other.mId)
			return false;
		return true;
	}
	
}
