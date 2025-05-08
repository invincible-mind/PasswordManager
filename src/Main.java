import PasswordManager.*;

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


        User u1 = new User("BuddyHolly", sys.encryptPwd("FrenchFry", secretKey, iv));
        sys.addUser(u1);
        sys.addAdministrator(u1);

        User u2 = new User("SirDude", sys.encryptPwd("ChickenWingz", secretKey, iv));
        sys.addUser(u2);

        User u3 = new User("ManBat", sys.encryptPwd("KingBurger", secretKey, iv));
        sys.addUser(u3);


        PasswordVerifier pv = new PasswordVerifier();
        pv.minLength(10);
        pv.maxLength(21);
        pv.needUpperCase(true);
        pv.needLowerCase(true);
        pv.needSpecial(true);
        pv.needNums(true);

            Account ac1 = new Account("BuddyHolly123", sys.encryptPwd(PasswordGenerator.generatePassword(pv), secretKey, iv), pv);
            u1.addAccount("Netflix", ac1);

            String pw = "LuckyDuck!2";

            if (pv.verifyPassword(pw)){
                SharedAccount sc1 = new SharedAccount("SirDudeALot", sys.encryptPwd(pw, secretKey, iv), pv, sys.getAdmins(), sys.getUsers());
            }


            User verified = sys.verifyUser("BuddyHolly", "FrenchFry", secretKey, iv);

            System.out.println(verified.getUsername());

        }
}