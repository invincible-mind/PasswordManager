package PasswordManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author Ashton Gabbeitt
 * A User for the PasswordManager
 */
public class User {

    private String username;
    private String password;
    private HashMap<String, Account> passwords;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        passwords = new HashMap<>();
    }

    /**
     * Retrieves the username of the User
     * @return the username for the User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the User
     * @param username new username for the User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the stored password of the User
     * @return the password for the User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     * @param password the password to be set for the user. Needs to be encrypted first.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Adds an account to the User's list of accounts
     * @param label the label for the account, to be used as the identifier (key)
     * @param account the account to be added
     */
    public void addAccount(String label, Account account) {
        passwords.put(label, account);
    }

    /**
     * Updates the existing account if it exists in the User's list of accounts, will be created if it does not
     * @param label for the Account
     * @param account the new Account to be added
     */
    public void editAccount(String label, Account account){
        passwords.put(label, account);
    }

    /**
     * Removes the existing account from the User's list of accounts
     * @param label the label for the account to be deleted
     */
    public void deleteAccount(String label){
        passwords.remove(label);
    }

    /**
     * Retrieves the Account from the User's list of accounts
     * @param label of the Account to look for
     * @return Account if exists, null otherwise
     */
    public Account getAccount(String label) {
        return passwords.get(label);
    }

    /**
     * Retrieves a list of all Accounts from the User's list of accounts
     * @return a list of all Accounts from the User's list of accounts
     */
    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(passwords.values());
    }

}
