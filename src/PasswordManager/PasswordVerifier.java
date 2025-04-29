package PasswordManager;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Kyle
 * the PasswordVerifier class that contains a number of criteria that the user can set
 * It also allows for custom criteria that it doesn't contain implementation form
 * These additional criteria will not be considered when generating a password.
 * What this does is it allows for the easy use of common password requirements
 * Such as minimum length and requires special characters.
 * It still allows for the user to add their own criteria
 * It implements a method that will check a string to test if it is valid
 */
public class PasswordVerifier {
	public static void main(String[] argm) {
		String password = "HelloWorld";
		PasswordVerifier pv = new PasswordVerifier();
		
		pv.maxLength(50);
		pv.minLength(10);
		pv.needSpecial();
		
		System.out.println(pv.verifyPassword(password));
		//System.out.println();
	}
	
	// TODO: Getters will be needed for all of these in the generator
	// Also all methods need javadoc
	private Set<Predicate<String>> criteria;  // never null, never contains null
	private int minchars; //never less then 1 or more then maxchars
	private int maxchars; //never less then minchars
	private Set<Character> allowedChars; // never null, contains no nulls
	
	// TODO: make "notNeeded" methods for all of these
	private boolean needCaps; 
	private boolean needNums;
	private boolean needSpecial;
	private boolean needLowercase;
	
	private static char[] specialChars = {
			'{','}','[',']','`','~','!','@','#','$','%','^','&','*','(',')','_','-','|','\\','\'','\"','<','>',',','.','/','?','=','+'};
	private static char[] lowercaseChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static char[] uppercaseChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static char[] numChars = {'1','2','3','4','5','6','7','8','9','0'};
	
	/**
	 * Creates a PasswordVerifier object
	 * All characters start as allowed, and and requirements start as false
	 * This min and max characters start at 1 and 50 respectively. 
	 * This should be changed as soon as possible. 
	 */
	public PasswordVerifier() {
		criteria = new HashSet<Predicate<String>>();
		minchars=1;
		maxchars=50;
		this.allowedChars = new HashSet<Character>();
		addAllowedCharacters(lowercaseChars);
		addAllowedCharacters(uppercaseChars);
		addAllowedCharacters(specialChars);
		addAllowedCharacters(numChars);
		needCaps = false;
		needLowercase = false;
		needNums = false;
		needSpecial = false;
	}
	
	/**
	 * This is for adding chars in an array to the allowed character set
	 * @param chars the chars being added
	 */
	private void addAllowedCharacters(char[] chars) {
		for (char c : chars) {
			this.allowedChars.add(c);
		}
	}
	
	/**
	 * This is for removing chars in an array from the allowed character set
	 * @param chars the chars being removed
	 */
	private void removeAllowedCharacters(char[] chars) {
		for (char c : chars) {
			this.allowedChars.remove(c);
		}
	}
	
	/**
	 * Verifies the inputed String as password
	 * Uses the criteria defined by the object
	 * @param password the password being verified
	 * @return true if valid, otherwise false
	 */
	public boolean verifyPassword(String password) { //this is like a fifth of my code on this document
		int len = password.length();
		boolean hasNeededChars = true;
		if (minchars >= len||maxchars <= len) return false;
		if (needLowercase) {
			hasNeededChars = false;
			for (char c : lowercaseChars) {
				if (password.contains(c+"")) {
					hasNeededChars = true;
					break;
				}
			}
			if (!hasNeededChars) return false;
		}
		if (needCaps) {
			hasNeededChars = false;
			for (char c : uppercaseChars) {
				hasNeededChars = false;
				if (password.contains(c+"")) {
					hasNeededChars = true;
					break;
				}
			}
			if (!hasNeededChars) return false;
		}
		if (needNums) {
			hasNeededChars = false;
			for (char c : numChars) {
				if (password.contains(c+"")) {
					hasNeededChars = true;
					break;
				}
			}
			if (!hasNeededChars) return false;
		}
		if (needSpecial) {
			hasNeededChars = false;
			for (char c : specialChars) {
				if (password.contains(c+"")) {
					System.out.println("This is special: " + c);
					hasNeededChars = true;
					break;
				}
			}
			if (!hasNeededChars) return false;
		}
		for (char c : password.toCharArray()) {
			if (!allowedChars.contains(c)) {
				return false;
			}
		}
		for (Predicate<String> p: this.criteria) {
			if (!p.test(password)) return false;
		}
		return true;
	}
	
	/** ##Thane, if oldPasswords doesn't match your signature, just tell me so I can change this one
	 * Verifies the inputed String as password
	 * Uses the criteria defined by the object
	 * Additionally prevents the password from being an old one
	 * @param password the password being verified
	 * @param oldPasswords all of the old passwords
	 * @return true if valid, otherwise false
	 */
	public boolean verifyNewPassword(String password, Map<Date,String> oldPasswords) {
		for (String p : oldPasswords.values()) {
			if (password.equals(p)) {
				return false;
			}
		}
		return verifyPassword(password);
	}
	
	/**
	 * Generates a password based on the password verifier.
	 * If it can not generate a valid password it will return NULL
	 * @return "TODO"
	 */
	public String generatePassword() {
		return "TODO";
	}
	
	/**
	 * @param criteria
	 */
	public void addCritiera(Predicate<String> criteria) {
		if (criteria == null) {
			return;
		}
		this.criteria.add(criteria);
	}
	
	/**
	 * @param criteria
	 */
	public void removeCritiera(Predicate<String> criteria) {
		this.criteria.remove(criteria);
	}
	
	/**
	 * @param min
	 */
	public void minLength(int min) {
		this.minchars = min;
	}
	
	/**
	 * @param max
	 */
	public void maxLength(int max) {
		this.maxchars = max;
	}
	
	/**
	 * 
	 */
	public void needUppercase() {
		this.needCaps=true;
	}
	
	/**
	 * 
	 */
	public void noUppercase() {
		removeAllowedCharacters(uppercaseChars);
	}
	
	/**
	 * 
	 */
	public void allowUppercase() {
		addAllowedCharacters(uppercaseChars);
	}
	
	/**
	 * 
	 */
	public void needLowercase() {
		this.needLowercase=true;
	}
	
	/**
	 * 
	 */
	public void noLowercase() {
		removeAllowedCharacters(lowercaseChars);
	}
	
	/**
	 * 
	 */
	public void allowLowercase() {
		addAllowedCharacters(lowercaseChars);
	}
	
	/**
	 * 
	 */
	public void needNums() {
		this.needNums=true;
	}
	
	/**
	 * 
	 */
	public void noNums() {
		removeAllowedCharacters(numChars);
	}
	
	/**
	 * 
	 */
	public void allowNums() {
		addAllowedCharacters(numChars);
	}
	
	/**
	 * 
	 */
	public void needSpecial() {
		this.needSpecial=true;
	}
	
	/**
	 * 
	 */
	public void noSpecial() {
		removeAllowedCharacters(specialChars);
	}
	
	/**
	 * 
	 */
	public void allowSpecial() {
		addAllowedCharacters(specialChars);
	}
	
}
