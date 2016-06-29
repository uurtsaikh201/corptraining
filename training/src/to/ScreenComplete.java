package to;

import java.io.Serializable;
import java.util.List;

public class ScreenComplete implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String text,type,video_url,audio_url,question,title,media;
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	List<TextTO> textList;
	public List<TextTO> getTextList() {
		return textList;
	}
	public void setTextList(List<TextTO> textList) {
		this.textList = textList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	List<QuestionTO> options;
	List<ImageTO> images;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
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
	
	public List<QuestionTO> getOptions() {
		return options;
	}
	public void setOptions(List<QuestionTO> options) {
		this.options = options;
	}
	public List<ImageTO> getImages() {
		return images;
	}
	public void setImages(List<ImageTO> images) {
		this.images = images;
	}
	Integer Id,position;
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
}
