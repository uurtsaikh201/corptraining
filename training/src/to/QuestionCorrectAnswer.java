package to;

import java.io.Serializable;

public class QuestionCorrectAnswer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long id,screenId;
	String title;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getScreenId() {
		return screenId;
	}
	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
