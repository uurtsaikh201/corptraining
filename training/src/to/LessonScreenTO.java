package to;

import java.io.Serializable;
import java.util.List;

public class LessonScreenTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer id,screenCount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getScreenCount() {
		return screenCount;
	}
	public void setScreenCount(Integer screenCount) {
		this.screenCount = screenCount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getScreenList() {
		return screenList;
	}
	public void setScreenList(List<String> screenList) {
		this.screenList = screenList;
	}
	String title,description;
	List<String> screenList;
	
}
