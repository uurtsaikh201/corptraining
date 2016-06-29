package to;


import java.io.Serializable;
import java.util.List;

public class Screen implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String text,type,video_url,audio_url,question;
	Integer id,position;
	String imagesUrl,optionsUrl,title,media;
	List<TextTO> textList;
	
	
	public List<TextTO> getTextList() {
		return textList;
	}
	public void setTextList(List<TextTO> textList) {
		this.textList = textList;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImagesUrl() {
		return imagesUrl;
	}
	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}
	public String getOptionsUrl() {
		return optionsUrl;
	}
	public void setOptionsUrl(String optionsUrl) {
		this.optionsUrl = optionsUrl;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getAudio_url() {
		return audio_url;
	}
	public void setAudio_url(String audio_url) {
		this.audio_url = audio_url;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
