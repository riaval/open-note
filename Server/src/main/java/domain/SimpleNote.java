package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SimpleNote {
	private long id;
	private String title;
	private String body;
	private Date date;
	
	public SimpleNote(){
	}
	
	public SimpleNote(String title, String body, Date date) {
		this.title = title;
		this.body = body;
		this.date = date;
	}
	
//	title
	@Column(unique=false, nullable = false, length = 45)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

//	body
	@Column(unique=false, nullable = true, length = 250)
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

//	date
	@Column(unique=false, nullable = false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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