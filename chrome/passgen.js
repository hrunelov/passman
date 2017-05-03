
/**
 * Deterministically generates a password from a string.
 */
 
var UPPERCASE_CHARS = [ 'A','B','C','D','E','F','G','H','I','J','K','L','M',
						'N','O','P','Q','R','S','T','U','V','W','X','Y','Z' ];
var LOWERCASE_CHARS = [ 'a','b','c','d','e','f','g','h','i','j','k','l','m',
						'n','o','p','q','r','s','t','u','v','w','x','y','z' ];
var NUMBER_CHARS    = [ '1','2','3','4','5','6','7','8','9','0' ];
var SYMBOL_CHARS    = [ '@','%','+','\\','/','\'','!','#','$','?',
						':','.','(',')' ,'{','}' ,'[',']','-','_' ];

var UPPERCASE = 0b0001;
var LOWERCASE = 0b0010;
var NUMBERS   = 0b0100;
var SYMBOLS   = 0b1000;

/**
 * Generates a password from a key.
 * key: Key to transform
 * length: Password length (default 20)
 * include (types of characters to include)
 * return: The resulting password
 */
function getPassword(key, length, include) {
	if (length == undefined) length = 20;
	if (include == undefined || key == 0b0000) include = 0b1111;

	var e = null;
	if (key.length<1) {
		e = "Empty key";
	}
	if (length<1) {
		e = "Non-positive password length";
	}
	if (e != null) {
		throw e;
	}

	var chars = new Array();

	if ((include & UPPERCASE) == UPPERCASE) {
		chars.push(UPPERCASE_CHARS);
	}
	if ((include & LOWERCASE) == LOWERCASE) {
		chars.push(LOWERCASE_CHARS);
	}
	if ((include & NUMBERS) == NUMBERS) {
		chars.push(NUMBER_CHARS);
	}
	if ((include & SYMBOLS) == SYMBOLS) {
		chars.push(SYMBOL_CHARS);
	}

	var rnd = new Random(hash(key));
	var result = "";
	for (var i = 0; i < length; ++i) {
		var charArr = chars[Math.abs(rnd.next())%chars.length];
		result += charArr[Math.abs(rnd.next())%charArr.length];
	}
	return result;
}

// String hashcode
function hash(str) {
	var result = new int(0,32);
	var len = str.length;
	for (var i = 0; i < len; ++i) {
		result = result.multiply(31).add(str.charCodeAt(i));
	}
	return result.toNumber();
}