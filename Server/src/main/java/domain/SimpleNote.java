package domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SimpleNote {
	
	private long mId;
	private String mTitle;
	private String mBody;
	private Date mDate;
	private UserGroup mUserGroup = new UserGroup();	
	
	public SimpleNote(){
	}
	
	public SimpleNote(String title, String body) {
		mTitle = title;
		mBody = body;
		mDate = Calendar.getInstance().getTime();
	}
	
	// title
	@Column(unique=false, nullable = false, length = 45)
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		this.mTitle = title;
	}

	// body
	@Column(unique=false, nullable = true, length = 250)
	public String getBody() {
		return mBody;
	}
	public void setBody(String body) {
		this.mBody = body;
	}

	// date
	@Column(unique=false, nullable = false)
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		this.mDate = date;
	}
	public void updateDate(){
		this.mDate = Calendar.getInstance().getTime();
	}
	
	// userGroup
	@ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="[userGroup]", nullable=false)
	public UserGroup getUserGroup() {
		return mUserGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.mUserGroup = userGroup;
	}
	
	@Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    public long getId() {
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
		SimpleNote other = (SimpleNote) obj;
		if (mId != other.mId)
			return false;
		return true;
	}
	
}