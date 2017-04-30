package net.hannesrunelov.passman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that deterministically generates a password from a string.
 */
public class PasswordGenerator {

	private static final char[] UPPERCASE_CHARS = { 'A','B','C','D','E','F','G','H','I','J','K','L','M',
													'N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
	private static final char[] LOWERCASE_CHARS = { 'a','b','c','d','e','f','g','h','i','j','k','l','m',
													'n','o','p','q','r','s','t','u','v','w','x','y','z' };
	private static final char[] NUMBER_CHARS    = { '1','2','3','4','5','6','7','8','9','0' };
	private static final char[] SYMBOL_CHARS    = { '@','%','+','\\','/','\'','!','#','$','?',
													':','.','(',')' ,'{','}' ,'[',']','-','_' };

	public static final byte UPPERCASE = 0b0001;
	public static final byte LOWERCASE = 0b0010;
	public static final byte NUMBERS   = 0b0100;
	public static final byte SYMBOLS   = 0b1000;

	/**
	 * Generates a password from a key.
	 * @param key Key (length between 1 and 64)
	 * @param length Password length (between 1 and 64)
	 * @param include Types of characters to include.<br>0b0001: Uppercase letters<br>
	 * 												     0b0010: Lowercase letters<br>
	 * 												     0b0100: Numbers<br>
	 * 													 0b1000: Symbols<br>
	 * @return The resulting password
	 */
	public static String getPassword(String key, int length, int include) {
		if (include == 0b0000) {
			include = 0b1111;
		}

		String e = null;
		if (key.length()<1) {
			e = "Empty key";
		}
		if (length<1) {
			e = "Non-positive password length";
		}
		if (e != null) {
			throw new IllegalArgumentException(e);
		}

		List<char[]> chars = new ArrayList<>();

		if ((include & UPPERCASE) == UPPERCASE) {
			chars.add(UPPERCASE_CHARS);
		}
		if ((include & LOWERCASE) == LOWERCASE) {
			chars.add(LOWERCASE_CHARS);
		}
		if ((include & NUMBERS) == NUMBERS) {
			chars.add(NUMBER_CHARS);
		}
		if ((include & SYMBOLS) == SYMBOLS) {
			chars.add(SYMBOL_CHARS);
		}

		Random rnd = new Random(key.hashCode());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			char[] charArr = chars.get(Math.abs(rnd.nextInt())%chars.size());
			sb.append(charArr[Math.abs(rnd.nextInt())%charArr.length]);
		}
		return sb.toString();
	}

	/**
	 * Generates a password from a key, containing both uppercase and lowercase letters, numbers and symbols.
	 * @param key Key (length between 1 and 64)
	 * @param length Password length (between 1 and 64)
	 * @return The resulting password
	 */
	public static String getPassword(String key, int length) {
		return getPassword(key, length, 0b1111);
	}

	/**
	 * Generates a password of length 20 from a key, containing both uppercase and lowercase letters, numbers and symbols.
	 * @param key Key (length between 1 and 64)
	 * @return The resulting password
	 */
	public static String getPassword(String key) {
		return getPassword(key, 20);
	}
}
