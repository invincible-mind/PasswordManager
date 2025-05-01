package PasswordManager;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * @Author Ashton Gabbeitt
 * The main system for the PasswordManager
 */
public class PasswordSystem {
    private ArrayList<User> users;
    private ArrayList<User> administrators;
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;

    public PasswordSystem() {
        users = new ArrayList<>();
        administrators = new ArrayList<>();
    }

    /**
     * Encrypts a plaintext string
     * @param password the plaintext string to be encrypted
     * @param secretKey the SecretKey value to be used for encryption
     * @param iv initialization vector for GCMParametersSpec, required for the encryption Cipher
     * @return the encrypted string
     */
    public String encryptPwd(String password, SecretKey secretKey, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
                try {
                    byte[] encryptedBytes = cipher.doFinal(password.getBytes());
                    return Base64.getEncoder().encodeToString(encryptedBytes);
                } catch (BadPaddingException | IllegalBlockSizeException e ) {
                    throw new RuntimeException(e);
                }

            }
            catch (InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException et) {
            throw new RuntimeException(et);
        }
        }

    /**
     * Adds a user to the system list of Users
     * @param user to be added
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Edits an existing user
     * @param existingUser the user to be edited
     * @param newUserInfo the user info to overwrite with
     */
    public void editUser(User existingUser, User newUserInfo) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(existingUser)) {
                users.set(i, newUserInfo);
            }
        }
    }

    /**
     * Gets the list of all system Users
     * @return a list of all system Users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Adds an administrator User to the list of system administrators
     * @param user the user to be added to the list of system Administrators
     */
    public void addAdministrator(User user) {
        administrators.add(user);
    }


    /**
     * Verifies the username and password of a system User
     * @param username of the User to be verified
     * @param password of the User to be verified
     *  @param secretKey the SecretKey value to be used for decryption
     * @param iv initialization vector for GCMParametersSpec, required for the decryption Cipher
     * @return the User if the username and password match that of a system User
     */
    public User verifyUser(String username, String password, SecretKey secretKey, byte[] iv) {
        User retrieved = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                String userPassword = user.getPassword();
                try {
                    Cipher cipher = Cipher.getInstance(ALGORITHM);
                    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                    try {
                        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
                        try {
                            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(userPassword));
                            String decryptedUserPassword = new String(decryptedBytes);
                            if (decryptedUserPassword.equals(password)) {
                                retrieved = user;
                            }
                        } catch (IllegalBlockSizeException | BadPaddingException e) {
                            throw new RuntimeException(e);
                        }

                    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
                        throw new RuntimeException(e);
                    }

                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return retrieved;
    }


}
