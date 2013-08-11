package com.opennote.model;

public class RestGroup {

	private String mSlug;
	private String mName;
	private String mRole;
	
	public RestGroup(String slug, String name, String role){
		mSlug = slug;
		mName = name;
		mRole = role;
	}
	
	// slug
	public String getSlug() {
		return mSlug;
	}
	public void setSlug(String slug) {
		this.mSlug = slug;
	}
	
	// name
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	
	// role
	public String getRole() {
		return mRole;
	}
	public void setRole(String role) {
		this.mRole = role;
	}
	
}
