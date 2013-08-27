package domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

	private long mId;
	private String mLogin;
	private String mFullName;
	private String mPasswordHash;
	private String mEmail;
	private Date mDate;
	private int mColor;
	private Set<Session> mSessions = new HashSet<Session>();
	private Set<UserGroup> mUserGroups = new HashSet<UserGroup>();
	private Set<Invite> mInvites = new HashSet<Invite>();

	public User(){
	}

	public User(String login, String fullName, String passwordHash, Date date, int color) {
		mLogin = login;
		mFullName = fullName;
		mPasswordHash = passwordHash;
		mDate = date;
		mColor = color;
	}

	// login
	@Column(unique=true, nullable = false, length = 24)
	public String getLogin() {
		return mLogin;
	}
	public void setLogin(String login){
		this.mLogin = login;
	}

	// full name
	@Column(unique=false, nullable = false, length = 36)
	public String getFullName() {
		return mFullName;
	}
	public void setFullName(String firstName){
		this.mFullName = firstName;
	}

	// passwordHash
	@Column(unique=false, nullable = false, length = 124)
	public String getPasswordHash() {
		return mPasswordHash;
	}
	public void setPasswordHash(String passwordHash){
		this.mPasswordHash = passwordHash;
	}

	// email
	@Column(unique=false, nullable = true, length = 45)
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String email){
		this.mEmail = email;
	}

	// date
	@Column(unique=false, nullable = false)
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date){
		this.mDate = date;
	}

	// sessions
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<Session> getSession(){
		return mSessions;
	}
	public void setSession(Set<Session> sessions){
		this.mSessions = sessions;
	}

	// userGroups
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<UserGroup> getUserGroups() {
		return this.mUserGroups;
	}
	public void setUserGroups(Set<UserGroup> userGroups) {
		this.mUserGroups = userGroups;
	}

	// invites
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	public Set<Invite> getInvites() {
		return mInvites;
	}
	public void setInvites(Set<Invite> invites) {
		this.mInvites = invites;
	}

	@Column(unique=false, nullable = true)
	public int getColor() {
		return mColor;
	}
	public void setColor(int color) {
		this.mColor = color;
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
		User other = (User) obj;
		if (mId != other.mId)
			return false;
		return true;
	}

}
