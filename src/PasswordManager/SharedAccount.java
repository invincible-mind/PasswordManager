package passwordManager;

import java.util.Set;

public class SharedAccount extends Account {
	private Set<User> admins;
	private Set<User> users;
	
	public SharedAccount(String username, String password, PasswordVerifier passwordVerifier, Set<User> admins, Set<User> users) {
		super(username, password, passwordVerifier);
		this.admins = admins;
		this.users = users;
	}
	
	public boolean addUser(User user) {
		return users.add(user);
	}
	
	public boolean addAdmin(User user) {
		return admins.add(user);
	}
	
	public boolean removeUser() {
		return users.remove(user);
	}
	
	public boolean removeAdmin() {
		return admins.remove(user);
	}
	
	public int verifyUser(User currentUser) {
		if (admins.contains(currentUser)) {
			return 1;
		} else if (users.contains(currentUser)) {
			return 0;
		} else {
			return -1;
		}
	}
	
	public Verified update(String newPassword, User currentUser) {
		if (verifyUser(currentUser) != 1) {
			return Verified.ImproperUser;
		}
		super.update(newPassword);
	}
}