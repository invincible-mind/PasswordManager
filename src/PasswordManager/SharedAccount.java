package passwordManager;
import java.util.Set;

/**
 * Shared account with multiple users and permission levels
 * @author Thane Gill
 */
public class SharedAccount extends Account {
    /** Enumeration of possible user types */
    private enum UserType {
        ADMIN, 				/** User with administrative privileges */
        USER, 				/** Regular user with standard privileges */
        NONE				/** Unrecognized user (no access) */
    }
    
    private Set<User> admins;
    private Set<User> users;
    
    /**
     * Constructs new SharedAccount with specified credentials and user sets
     *
     * @param username, account username (inherited from Account)
     * @param password, account password (inherited from Account)
     * @param passwordVerifier, password validator (inherited from Account)
     * @param admins, initial set of administrative users (cannot be null)
     * @param users, initial set of regular users (cannot be null)
     */
    public SharedAccount(String username, String password, PasswordVerifier passwordVerifier, 
    		Set<User> admins, Set<User> users) {
        super(username, password, passwordVerifier);
        this.admins = admins;
        this.users = users;
    }
    
    /**
     * Adds new regular user to account (admin-only operation)
     *
     * @param currentUser, user attempting operation (must be admin)
     * @param user, user to be added
     * @return Verify.CORRECT if successful, Verify.USER_ERROR if currentUser lacks permissions,
     *         Verify.INCORRECT if operation fails
     */
    public Verify addUser(User currentUser, User user) {
        return performAdminOperation(
                currentUser, 
                () -> users.add(user)
        );
    }
    
    /**
     * Adds new admin user to account (admin-only operation)
     *
     * @param currentUser, user attempting operation (must be admin)
     * @param user, user to be promoted to admin
     * @return Verify.CORRECT if successful, Verify.USER_ERROR if currentUser lacks permissions,
     *         Verify.INCORRECT if operation fails
     */
    public Verify addAdmin(User currentUser, User user) {
        return performAdminOperation(
                currentUser, 
                () -> admins.add(user)
        );
    }
    
    /**
     * Removes regular user from account (admin-only operation)
     *
     * @param currentUser, user attempting operation (must be admin)
     * @param user, user to be removed
     * @return Verify.CORRECT if successful, Verify.USER_ERROR if currentUser lacks permissions,
     *         Verify.INCORRECT if operation fails
     */
    public Verify removeUser(User currentUser, User user) {
        return performAdminOperation(
                currentUser, 
                () -> users.remove(user)
        );
    }
    
    /**
     * Removes admin user from account (admin-only operation)
     *
     * @param currentUser, user attempting operation (must be admin)
     * @param user, admin to be demoted
     * @return Verify.CORRECT if successful, Verify.USER_ERROR if currentUser lacks permissions,
     *         Verify.INCORRECT if operation fails
     */
    public Verify removeAdmin(User currentUser, User user) {
        return performAdminOperation(
                currentUser, 
                () -> admins.remove(user)
        );
    }
    
    /**
     * Internal method to perform privileged operations after admin verification
     *
     * @param currentUser, user requesting operation
     * @param operation, action to perform
     * @return Verify.CORRECT if successful, Verify.USER_ERROR if currentUser lacks permissions,
     *         Verify.INCORRECT if operation fails
     */
    private Verify performAdminOperation(User currentUser, Operation operation) {
        if (verifyUser(currentUser) != UserType.ADMIN) {
            return Verify.USER_ERROR;
        }
        
        boolean success = operation.execute();
        if (success) {
            return Verify.CORRECT;
        } else {
            return Verify.INCORRECT;
        }
    }
    
    /**
     * Verifies user's permission level
     *
     * @param currentUser, user to check
     * @return UserType.ADMIN if user has admin privileges, UserType.USER if user has regular access,
     *         UserType.NONE if user unrecognized
     */
    public UserType verifyUser(User currentUser) {
        if (admins.contains(currentUser)) {
            return UserType.ADMIN;
        } else if (users.contains(currentUser)) {
            return UserType.USER;
        } else {
            return UserType.NONE;
        }
    }
    
    /**
     * Updates account password (admin-only operation)
     *
     * @param newPassword, proposed new password
     * @param currentUser, user attempting change (must be admin)
     * @return Verify.CORRECT if successful, Verify.USER_ERROR if currentUser lacks permissions,
     *         Verify.INCORRECT if new password fails verification
     */
    public Verify update(String newPassword, User currentUser) {
        if (verifyUser(currentUser) != UserType.ADMIN) {
            return Verify.USER_ERROR;
        }
        return super.update(newPassword);
    }
    
    /** Functional interface for operations */
    @FunctionalInterface
    private interface Operation {
        /**
         * Executes operation
         * @return true if operation success, else false
         */
        boolean execute();
    }
}