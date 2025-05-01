package PasswordManager;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle
 * This is the password generator.
 * It takes a password verifier and randomly generates a password that works with it
 */
public class PasswordGenerator {

  //these are for later
	private static char[] specialChars = {
			'{','}','[',']','`','~','!','@','#','$','%','^','&','*','(',')','_','-','|','\\','\'','\"','<','>',',','.','/','?','=','+'};
	private static char[] lowerCaseChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static char[] upperCaseChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static char[] numChars = {'1','2','3','4','5','6','7','8','9','0'};
	
	/**
	 * Generates a password that works with a given PasswordVerifier.
	 * It randomly adds different allowed characters to the end of the string
	 * Until the max size is reached. If there is no PasswordVerifier or
	 * extra criteria is used and the password is not verified, null will be returned
	 * @param pv the used verifier, should never be null
	 * @return the generated password, or null if there is an error
	 */
	public static String generatePassword(PasswordVerifier pv) {
		if (pv == null) {
			return null;
		}
		String password = "";
		int maxSize = pv.maxCharacters();
		//System.out.println("This is size: " + maxSize);
		List<Character> allowed = new ArrayList<>(pv.allowedCharacters());
		//System.out.println("This is worked");
		int allowedSize = allowed.size();
		//System.out.println("Size of set is: " + allowedSize);
		for (int i = 0; i < maxSize; i++) {
			int index = (int)(Math.random()*allowedSize);
			//System.out.println(index);
			password = password + allowed.get(index);
		}
		
		if (pv.verifyPassword(password)) {
			return password;
		} else {
			return null;
		}
	}
	
}
