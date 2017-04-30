package net.hannesrunelov.passman;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class that deterministically generates a password from two strings (an input and a key).
 */
public class PasswordGenerator {

	private static final char[] UPPERCASE_CHARS = { 'A','B','C','D','E','F','G','H','I','J','K','L','M',
										            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
	private static final char[] LOWERCASE_CHARS = { 'a','b','c','d','e','f','g','h','i','j','k','l','m',
											        'n','o','p','q','r','s','t','u','v','w','x','y','z' };
	private static final char[] NUMBER_CHARS    = { '1','2','3','4','5','6','7','8','9','0' };
	private static final char[] SYMBOL_CHARS    = { '@','%','+','\\','/','\'','!','#','$','?',
											        ':','.','(',')' ,'{','}' ,'[',']','-','_' };

	private static final byte[] DEFAULT_INPUT = {  0x2b,-0x38, 0x41,-0x55,-0x3a, 0x61,-0x4d,-0x46,
												  -0x70,-0x2b,-0x16,-0x72,-0x5b,-0x77, 0x01,-0x1e,
												   0x39,-0x18,-0x17,-0x44,-0x78, 0x76,-0x7d,-0x34,
												   0x2b,-0x19,-0x24, 0x5d, 0x47,-0x17,-0x78, 0x19  };
	private static final byte[] DEFAULT_KEY   = { -0x73,-0x21,-0x64,-0x35, 0x4a,-0x39, 0x73, 0x7d,
												  -0x6e,-0x14, 0x37,-0x29,-0x71,-0x2a, 0x3f,-0x5f,
												  -0x15,-0x1f,-0x38, 0x48,-0x40,-0x4c,-0x46, 0x7b,
												   0x1c, 0x0c, 0x37,-0x48, 0x17, 0x60,-0x04, 0x75  };

	public static final byte UPPERCASE = 0b0001;
	public static final byte LOWERCASE = 0b0010;
	public static final byte NUMBERS   = 0b0100;
	public static final byte SYMBOLS   = 0b1000;

	/**
	 * Generates a password.
	 * @param input Input (e.g. name)
	 * @param key Key ("Master" password)
	 * @param length Length of password
	 * @param include Types of characters to include<br>
	 *                (0b0001: Uppercase letters,<br>
	 *                 0b0010: Lowercase letters,<br>
	 *                 0b0100: Numbers,<br>
	 *                 0b1000: Symbols)
	 * @return Resulting password
	 */
	public static String getPassword(String input, String key, int length, byte include) {
		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());

		byte[] inputBytesRaw = input.getBytes();
	    byte[] keyBytesRaw = key.getBytes();

		int iSize = DEFAULT_INPUT.length;
		int kSize = DEFAULT_KEY.length;

		byte[] inputBytes = new byte[iSize];
		int s = 0;
		for (int i = 0; i < iSize; ++i) {
			if (i < inputBytesRaw.length) {
				byte b = inputBytesRaw[i];
				inputBytes[i] += b;
				s += b;
			} else {
				s += i;
				inputBytes[i] += s;
			}
		}

		byte[] keyBytes = new byte[kSize];
		for (int i = 0; i < kSize; ++i) {
			if (i < keyBytesRaw.length) {
				byte b = keyBytesRaw[i];
				keyBytes[i] += b;
				s += b;
			} else {
				s += i;
				keyBytes[i] += s;
			}
		}

	    SecretKeySpec cKey = new SecretKeySpec(keyBytes, "AES");

	    Cipher cipher = null;
	    byte[] cipherBytes = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, cKey);
			cipherBytes = new byte[cipher.getOutputSize(inputBytes.length)];
			int cLength = cipher.update(inputBytes, 0, inputBytes.length, cipherBytes, 0);
		    cLength += cipher.doFinal(cipherBytes, cLength);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytesToPassword(cipherBytes, include).substring(0,length);
	}

	/**
	 * Generates a password containing both uppercase and lowercase letters, numbers and symbols.
	 * @param input Input (e.g. name)
	 * @param key Key ("Master" password)
	 * @param length Length of password
	 * @return Resulting password
	 */
	public static String getPassword(String input, String key, int length) {
		return getPassword(input, key, length, (byte)0b1111);
	}

	/**
	 * Converts an array of bytes into a password
	 * @param bytes Bytes to convert
	 * @param include Types of characters to include<br>
	 *                (0b0001: Uppercase letters,<br>
	 *                 0b0010: Lowercase letters,<br>
	 *                 0b0100: Numbers,<br>
	 *                 0b1000: Symbols)
	 * @return Resulting password
	 */
	private static String bytesToPassword(byte[] bytes, byte include) {
		if (include == 0b0000) {
			include = 0b1111;
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
		int i = 0;
		for (byte b : bytes) {
			sb.append(chars.get((b+128+i++) % chars.size()));
		}
		return sb.toString();
	}

	/**
	 * Converts an array of bytes into a password containing both uppercase and lowercase letters,
	 * numbers and symbols.
	 * @param bytes Bytes to convert
	 * @return Resulting password
	 */
	private static String bytesToPassword(byte[] bytes) {
		return bytesToPassword(bytes, (byte)0b1111);
	}
}
