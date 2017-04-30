package net.hannesrunelov.passman;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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

	private static final byte[] BASE = { -27,-60,58,41,112,-126,64,-97,
										 -38,62,-11,99,-2,-79,44,-77,
										 16,121,-55,-73,-68,88,-32,-95,
										 94,111,49,40,70,17,111,12,
										 -12,-107,-44,18,-101,-91,16,24,
										 -37,20,-67,37,-81,-86,110,19,
										 127,-125,4,104,-47,-104,-83,-89,
										 -96,-62,-119,95,102,22,-111,-109 };

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

		int minLength = 1;
		int maxLength = BASE.length;

		String e = null;
		if (key.length()<minLength || key.length()>maxLength) {
			e = String.format(Locale.getDefault(),
					"Key length out of range (%d, expected between %d and %d)",
					key.length(), minLength, maxLength);
		}
		if (length<minLength || length>maxLength) {
			e = String.format(Locale.getDefault(),
					"Password length out of range (%d, expected between %d and %d)",
					length, minLength, maxLength);
		}
		if (e != null) {
			throw new IllegalArgumentException(e);
		}

		List<Character> chars = new ArrayList<>();

		if ((include & UPPERCASE) == UPPERCASE) {
			for (char c : UPPERCASE_CHARS) {
				chars.add(c);
			}
		}
		if ((include & LOWERCASE) == LOWERCASE) {
			for (char c : LOWERCASE_CHARS) {
				chars.add(c);
			}
		}
		if ((include & NUMBERS) == NUMBERS) {
			for (char c : NUMBER_CHARS) {
				chars.add(c);
			}
		}
		if ((include & SYMBOLS) == SYMBOLS) {
			for (char c : SYMBOL_CHARS) {
				chars.add(c);
			}
		}

		StringBuilder sb = new StringBuilder();
		char c = 0;
		for (int i = 0; i < length; ++i) {
			if (i < key.length()) {
				c = key.charAt(i);
			}
			sb.append(chars.get((Math.abs(c+(i+1)*hash(key)*key.length()+BASE[i]) % 2147483647) % chars.size()));
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

	private static int hash(String str) {
		int h = 0;
		if (str.length() > 0) {
			for (int i = 0; i < str.length(); ++i) {
				h = 31 * h + str.charAt(i);
			}
		}
		return h;
	}
}
