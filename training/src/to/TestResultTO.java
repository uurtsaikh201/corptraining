package to;

import java.io.Serializable;

public class TestResultTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public String getUserDisplayName() {
		return userDisplayName;
	}
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	public Long getLessonId() {
		return lessonId;
	}
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	public Long getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}
	public Long getUserScore() {
		return userScore;
	}
	public void setUserScore(Long userScore) {
		this.userScore = userScore;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	String lessonName,userDisplayName;
	Long lessonId,totalScore,userScore,userId;
}
