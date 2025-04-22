import PasswordManager.PasswordSystem;
import PasswordManager.User;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {


        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        PasswordSystem sys = new PasswordSystem();


        User u1 = new User("AshtonGabbeitt", sys.encryptPwd("FrenchFry", secretKey, iv));
        sys.addUser(u1);

        User verified = sys.verifyUser("AshtonGabbeitt", "FrenchFry", secretKey, iv);

        System.out.println(verified.getUsername());

    }
}