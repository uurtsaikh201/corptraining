package to;

import java.io.Serializable;

public class LoginResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String result;
	String token;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
