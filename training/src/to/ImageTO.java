package to;

import java.io.Serializable;

public class ImageTO implements Serializable{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 9005412563447624291L;
	String title;
	String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
