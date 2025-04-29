package passwordManager;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class Account {
	private String username;
	private String password;
	private Date dateCreated;
	private PasswordVerifier passwordVerifier;
	private Map<Date,String> oldPasswords = new HashMap<>();


	public Account(String username, String password, PasswordVerifier passwordVerifier) {
		this.username = username;					// check in here for password validity?
		this.passwordVerifier = passwordVerifier;
		this.password = password;
		this.dateCreated = 	new Date();
	}
	
	public int update(String newPassword) {
		if (passwordVerifier.checkPassword(newPassword)) {
			return 0;
		}
		oldPasswords.put(dateCreated, password);
		password = newPassword;
		dateCreated = new Date();
		return 1;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public Map<Date,String> getOldPasswords() {
		return oldPasswords;
	}
}