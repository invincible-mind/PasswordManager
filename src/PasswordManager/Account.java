package passwordManager;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class Account {
	public enum Verified {
		ImproperUser, ImproperPassword, Correct
	}
	
	private String username;
	private String password;
	private Date dateCreated;
	private PasswordVerifier passwordVerifier;
	private Map<Date,String> oldPasswords = new HashMap<>();


	public Account(String username, String password, PasswordVerifier passwordVerifier) {
		this.username = username;					// check in here for password validity?
		this.passwordVerifier = passwordVerifier;
		this.password = password;
		this.dateCreated = new Date();
	}
	
	public Verified update(String newPassword) {
		if (passwordVerifier.verifyNewPassword(newPassword)) {
			return Verified.ImproperPassword;
		}
		oldPasswords.put(dateCreated, password);
		password = newPassword;
		dateCreated = new Date();
		return Verified.Correct;
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