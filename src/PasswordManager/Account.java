package PasswordManager;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Account class used for password manager
 * @author Thane Gill
 */
public class Account {
    /** Verification status enumeration for account operations. */
    public enum Verify {
        INCORRECT, 				/** Indicates a failed verification or operation */
        CORRECT, 				/** Indicates a successful verification or operation */
        USER_ERROR				/** Indicates an error via improper user */
    }
    
    private String username;
    private String password;
    private Date dateCreated;
    private PasswordVerifier passwordVerifier;
    private Map<Date, String> oldPasswords = new HashMap<>();

    /**
     * Constructs new Account with the specified credentials
     *
     * @param username, username for account (cannot be null or empty)
     * @param password, initial password for account (must meet verifier requirements)
     * @param passwordVerifier, password policy verifier (cannot be null)
     */
    public Account(String username, String password, PasswordVerifier passwordVerifier) {
        this.username = username;
        this.passwordVerifier = passwordVerifier;
        this.password = password;
        this.dateCreated = new Date();
    }
    
    /**
     * Updates account password after verifying it meets policy requirements
     * Stores old password in password history (oldPasswords HashMap)
     *
     * @param newPassword, the proposed new password
     * @return Verify.CORRECT if update was successful, Verify.INCORRECT if new password fails verification
     */
    public Verify update(String newPassword) {
        if (passwordVerifier.verifyNewPassword(newPassword, oldPasswords)) {
            return Verify.INCORRECT;
        }
        oldPasswords.put(dateCreated, password);
        password = newPassword;
        dateCreated = new Date();
        return Verify.CORRECT;
    }

    /**
     * Gets account username
     * @return username associated with account
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Gets current account password
     * @return current active password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Gets date when account was created or last updated
     * @return creation/last modification date
     */
    public Date getDateCreated() {
        return dateCreated;
    }
    
    /**
     * Gets record of previous passwords and dates
     * @return unmodifiable map of date-password pairs
     */
    public Map<Date, String> getOldPasswords() {
        return oldPasswords;
    }
}