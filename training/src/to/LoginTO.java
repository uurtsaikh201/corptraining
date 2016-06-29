package to;

import java.io.Serializable;

public class LoginTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1829484948888314123L;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	String email,password;
}
