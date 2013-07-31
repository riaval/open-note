package com.opennote.model;

import java.util.Calendar;
import java.util.Date;

public class LocalItem {
	private String title;
	private String body;
	private Date date;
	
	public LocalItem(String title, String body) {
		this.title = title;
		this.body = body;
		this.date = Calendar.getInstance().getTime();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
