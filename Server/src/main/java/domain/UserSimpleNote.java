package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserSimpleNote {
	private long id;
	private long userGroup;
	private long simpleNote;
	
	public UserSimpleNote(){
	}
	
	public UserSimpleNote(long userGroup, long simpleNote) {
		this.userGroup = userGroup;
		this.simpleNote = simpleNote;
	}
	
//	userGroup
	@ManyToOne
    @JoinColumn(name = "UserGroup")
	@Column(unique = false, nullable = false)
	public long getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(long userGroup) {
		this.userGroup = userGroup;
	}
	
//	simpleNote
	@ManyToOne
    @JoinColumn(name = "SimpleNote")
	@Column(unique = false, nullable = false)
	public long getSimpleNote() {
		return simpleNote;
	}
	public void setSimpleNote(long simpleNote) {
		this.simpleNote = simpleNote;
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
