package net.hannesrunelov.passman;

import java.util.ArrayList;
import java.util.List;

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

	public static final int DEFAULT_LENGTH = 20;

	/**
	 * Generates a password from a key.
	 * @param key Key to transform
	 * @param length Password length
	 * @param include Types of characters to include.<br>UPPERCASE: Uppercase letters<br>
	 * 												     LOWERCASE: Lowercase letters<br>
	 * 												     NUMBERS: Numbers<br>
	 * 													 SYMBOLS: Symbols<br>
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

		Random rnd = new Random(hash(key));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			char[] charArr = chars.get(Math.abs(rnd.next())%chars.size());
			sb.append(charArr[Math.abs(rnd.next())%charArr.length]);
		}
		return sb.toString();
	}

	/**
	 * Generates a password from a key, containing both uppercase and lowercase letters,
	 * numbers and symbols.
	 * @param key Key to transform
	 * @param include Types of characters to include.<br>UPPERCASE: Uppercase letters<br>
	 * 												     LOWERCASE: Lowercase letters<br>
	 * 												     NUMBERS: Numbers<br>
	 * 													 SYMBOLS: Symbols<br>
	 * @return The resulting password
	 */
	public static String getPassword(String key, int include) {
		return getPassword(key, DEFAULT_LENGTH, 0b1111);
	}

	/**
	 * Generates a password of length 20 from a key, containing both uppercase and
	 * lowercase letters, numbers and symbols.
	 * @param key Key to transform
	 * @return The resulting password
	 */
	public static String getPassword(String key) {
		return getPassword(key, DEFAULT_LENGTH);
	}

	// String hash
	private static int hash(String str) {
		int result = 0;
		int len = str.length();
		for (int i = 0; i < len; ++i) {
			result = 31 * result + str.charAt(i);
		}
		return result;
	}

	// Stripped down implementation of java.util.Random with only what's needed for the
	// password generator.
	private static class Random {
		private long seed;

		Random(long seed) {
			setSeed(seed);
		}

		void setSeed(long seed) {
			this.seed = (seed ^ 25214903917L) & ((1L << 48) - 1);
		}

		int next()
		{
			seed = (seed * 25214903917L + 11) & ((1L << 48) - 1);
			return (int)(seed >>> 16);
		}
	}
}
