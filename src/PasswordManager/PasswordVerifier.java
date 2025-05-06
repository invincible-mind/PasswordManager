package PasswordManager;

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
	@SuppressWarnings("javadoc")
	public static void main(String[] argm) {
		String password = "HelloWorld";
		PasswordVerifier pv = new PasswordVerifier();
		
		pv.maxLength(50);
		pv.minLength(10);
		pv.needSpecial(true);
		
		System.out.println(pv.verifyPassword(password));
		//System.out.println();
	}
	
	private Set<Predicate<String>> criteria;  // never null, never contains null
	private int minChars; //never less then 0 or more then maxchars
	private int maxChars; //never less then minchars
	private Set<Character> allowedChars; // never null, contains no nulls
	
	private boolean needCaps; //false if there are no upper case characters in the allowed characters set
	private boolean needNums; //false if there are number characters in the allowed characters set
	private boolean needSpecial; //false if there are no special characters in the allowed characters set
	private boolean needLowercase; //false if there are no lower case characters in the allowed characters set
	
	private static char[] specialChars = {
			'{','}','[',']','`','~','!','@','#','$','%','^','&','*','(',')','_','-','|','\\','\'','\"','<','>',',','.','/','?','=','+'};
	private static char[] lowerCaseChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static char[] upperCaseChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static char[] numChars = {'1','2','3','4','5','6','7','8','9','0'};
	
	/**
	 * Creates a PasswordVerifier object
	 * All characters start as allowed, and and requirements start as false
	 * This min and max characters start at 1 and 50 respectively. 
	 * This should be changed as soon as possible. 
	 */
	public PasswordVerifier() {
		criteria = new HashSet<Predicate<String>>();
		minChars=1;
		maxChars=50;
		this.allowedChars = new HashSet<Character>();
		addAllowedCharacters(lowerCaseChars);
		addAllowedCharacters(upperCaseChars);
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
	public void addAllowedCharacters(char[] chars) {
		for (char c : chars) {
			this.allowedChars.add(c);
		}
	}
	
	/**
	 * This is for removing chars in an array from the allowed character set
	 * @param chars the chars being removed
	 */
	public void removeAllowedCharacters(char[] chars) {
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
	public boolean verifyPassword(String password) {
		if (password == null) return false;
		int len = password.length();
		boolean hasNeededChars = true;
		if (minChars >= len||maxChars <= len) return false;
		if (needLowercase) {
			hasNeededChars = false;
			for (char c : lowerCaseChars) {
				if (password.contains(c+"")) {
					hasNeededChars = true;
					break;
				}
			}
			if (!hasNeededChars) return false;
		}
		if (needCaps) {
			hasNeededChars = false;
			for (char c : upperCaseChars) {
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
	
	/**
	 * Uses the criteria defined by the object
	 * Additionally prevents the password from being an old one
	 * @param password the password being verified
	 * @param oldPasswords all of the old passwords
	 * @return true if valid, otherwise false
	 */
	public boolean verifyNewPassword(String password, Map<Date,String> oldPasswords) {
		if (password == null) return false;
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
	 * @return the generated password, or null if it fails
	 */
	public String generatePassword() {
		return PasswordGenerator.generatePassword(this);
	}
	
	/**
	 * Adds a criteria that has no direct implementation 
	 * Does not work with generate password
	 * @param criteria the rule being added
	 */
	public void addCriteria(Predicate<String> criteria) {
		if (criteria == null) {
			return;
		}
		this.criteria.add(criteria);
	}
	
	/**
	 * Removes a criteria that was added through addCriteria
	 * @param criteria the rule being removed
	 */
	public void removeCriteria(Predicate<String> criteria) {
		this.criteria.remove(criteria);
	}
	
	/**
	 * Sets the maximum character limit
	 * @param min
	 */
	public void minLength(int min) {
		this.minChars = min;
		repOK();
	}
	
	/**
	 * Sets the minimum character limit
	 * @param max
	 */
	public void maxLength(int max) {
		this.maxChars = max;
		repOK();
	}
	
	/**
	 * Sets the flag that at least 1 upper case letter is needed
	 * @param need true if needed false if not
	 */
	public void needUpperCase(boolean need) {
		this.needCaps=need;
	}
	
	/**
	 * Removes all upper case characters from the set of available characters
	 * If an upper case character was needed, the requirement is removed
	 */
	public void noUpperCase() {
		removeAllowedCharacters(upperCaseChars);
		this.needCaps=false;
	}
	
	/**
	 * Adds all upper case characters to the available character set
	 */
	public void allowUpperCase() {
		addAllowedCharacters(upperCaseChars);
	}
	
	/**
	 * Sets the flag that at least 1 lower case letter is needed
	 * @param need true if needed false if not
	 */
	public void needLowerCase(boolean need) {
		this.needLowercase=need;
	}
	
	/**
	 * Removes all lower case characters from the set of available characters
	 * If a lower case case character was needed, the requirement is removed
	 */
	public void noLowerCase() {
		removeAllowedCharacters(lowerCaseChars);
		this.needLowercase=false;
	}
	
	/**
	 * Adds all lower case characters to the available character set
	 */
	public void allowLowerCase() {
		addAllowedCharacters(lowerCaseChars);
	}
	
	/**
	 * Sets the flag that at least 1 number character is needed
	 * @param need true if needed false if not
	 */
	public void needNums(boolean need) {
		this.needNums=need;
	}
	
	/**
	 * Removes all number characters from the set of available characters
	 * If a number character was needed, the requirement is removed
	 */
	public void noNums() {
		removeAllowedCharacters(numChars);
		this.needNums=false;
	}
	
	/**
	 * Adds all number characters to the available character set
	 */
	public void allowNums() {
		addAllowedCharacters(numChars);
	}
	
	/**
	 * Sets the flag that at least 1 special character is needed
	 * Special characters are: {}[]`~!@#$%^&*()_-|\'"<>,./?=+
	 * @param need 
	 */
	public void needSpecial(boolean need) {
		this.needSpecial=need;
	}
	
	/**
	 * Removes all special characters from the set of available characters
	 * If a special character was needed, the requirement is removed
	 * Special characters are: {}[]`~!@#$%^&*()_-|\'"<>,./?=+
	 */
	public void noSpecial() {
		removeAllowedCharacters(specialChars);
		this.needSpecial=false;
	}
	
	/**
	 * Adds all special characters to the available character set
	 * Special characters are: {}[]`~!@#$%^&*()_-|\'"<>,./?=+
	 */
	public void allowSpecial() {
		addAllowedCharacters(specialChars);
	}
	
	/**
	 * @return the maximum number of characters criteria
	 */
	public int maxCharacters() {
		return this.maxChars;
	}
	
	/**
	 * @return the minimum number of characters criteria
	 */
	public int minCharacters() {
		return this.minChars;
	}
	
	/**
	 * @return set of allowed characters in the password
	 */
	public Set<Character> allowedCharacters() {
		return new HashSet<Character>(this.allowedChars);
	}
	
	/**
	 * @return set of extra criteria
	 */
	public Set<Predicate<String>> extraCriteria() {
		return new HashSet<>(this.criteria);
	}
	
	/**
	 * @return if the password requires a least 1 upper case character
	 */
	public boolean doesNeedUpperCase() {
		return this.needCaps;
	}
	
	/**
	 * @return if the password requires a least 1 lower case character
	 */
	public boolean doesNeedLowerCase() {
		return this.needLowercase;
	}
	
	/**
	 * @return if the password requires a least 1 number character
	 */
	public boolean doesNeedNumbers() {
		return this.needNums;
	}
	
	/**
	 * @return if the password requires a least 1 special character
	 */
	public boolean doesNeedSpecial() {
		return this.needSpecial;
	}
	
	private void repOK() {
		assert(this.allowedChars != null);
		for (Character c : this.allowedChars) {
			assert(c != null);
		}
		assert(this.criteria != null);
		for (Predicate<String> p : this.criteria) {
			assert(p != null);
		}
		assert(this.minChars >= 0);
		assert(this.maxChars >= minChars);
	}
	
}
