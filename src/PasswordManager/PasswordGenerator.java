package PasswordManager;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kyle
 * This is the password generator.
 * It takes a password verifier and randomly generates a password that works with it
 */
public class PasswordGenerator {

//	public static void main(String[] arrs) {
//		
//		String psswd = "NOLOWERS";
//		PasswordVerifier pv = new PasswordVerifier();
//		
//		System.out.println(checkLowerCase(psswd,pv));
//		
//	}
	
	private static final char[] specialChars = {
			'{','}','[',']','`','~','!','@','#','$','%','^','&','*','(',')','_','-','|','\\','\'','\"','<','>',',','.','/','?','=','+'};
	private static final char[] lowerCaseChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static final char[] upperCaseChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static final char[] numChars = {'1','2','3','4','5','6','7','8','9','0'};
	
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
		password = checkNeeded(password,pv);

		if (pv.verifyPassword(password)) {
			return password;
		} else {
			return null;
		}
	}
	
	private static String checkNeeded(String password, PasswordVerifier pv) {
		Set<Character> allowed = pv.allowedCharacters();
		StringBuilder sb = new StringBuilder(password);
		int[] indexs = {-1,-1,-1,-1};
		if (pv.doesNeedLowerCase()) {
			List<Character> allowedLower = new ArrayList<Character>();
			for (char c : lowerCaseChars) {
				if (allowed.contains(c)) {
					allowedLower.add(c);
				} 
			}
			indexs[0] = (int)(password.length()*Math.random());
			sb.setCharAt(indexs[0],allowedLower.get((int)(allowedLower.size()*Math.random())));
		}
		if (pv.doesNeedUpperCase()) {
			List<Character> allowedUpper = new ArrayList<Character>();
			for (char c : upperCaseChars) {
				if (allowed.contains(c)) {
					allowedUpper.add(c);
				} 
			}
			indexs[1] = (int)(password.length()*Math.random());
			if (indexs[1]==indexs[0]) {
				indexs[1]++;
			}
			sb.setCharAt(indexs[1],allowedUpper.get((int)(allowedUpper.size()*Math.random())));
		}
		if (pv.doesNeedNumbers()) {
			List<Character> allowedNums = new ArrayList<Character>();
			for (char c : numChars) {
				if (allowed.contains(c)) {
					allowedNums.add(c);
				} 
			}
			indexs[2] = (int)(password.length()*Math.random());
			if (indexs[2]==indexs[0]) {
				indexs[2]++;
			}
			if (indexs[2]==indexs[1]) {
				indexs[2]++;
			}
			sb.setCharAt(indexs[2],allowedNums.get((int)(allowedNums.size()*Math.random())));
		}
		if (pv.doesNeedUpperCase()) {
			List<Character> allowedSpecial = new ArrayList<Character>();
			for (char c : specialChars) {
				if (allowed.contains(c)) {
					allowedSpecial.add(c);
				} 
			}
			indexs[3] = (int)(password.length()*Math.random());
			if (indexs[3]==indexs[0]) {
				indexs[3]++;
			}
			if (indexs[3]==indexs[1]) {
				indexs[3]++;
			}
			if (indexs[3]==indexs[2]) {
				indexs[3]++;
			}
			sb.setCharAt(indexs[3],allowedSpecial.get((int)(allowedSpecial.size()*Math.random())));
		}
		password = sb.toString();		
		return password;
	}
}
