package to;

import java.io.Serializable;

public class TestAnswerRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String LessonID,DataID,Answer,UserID;

	public String getLessonID() {
		return LessonID;
	}

	public void setLessonID(String lessonID) {
		LessonID = lessonID;
	}

	public String getDataID() {
		return DataID;
	}

	public void setDataID(String dataID) {
		DataID = dataID;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}
	
}
