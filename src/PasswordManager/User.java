package PasswordManager;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String username;
    private String password;
    private HashMap<String, Account> passwords;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        passwords = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addAccount(String label, Account account) {
        passwords.put(label, account);
    }

    public void editAccount(String label, Account account){
        passwords.put(label, account);
    }

    public void deleteAccount(String label){
        passwords.remove(label);
    }

    public Account getAccount(String label) {
        return passwords.get(label);
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(passwords.values());
    }




}
