
/**
 * Deterministically generates a password from a string.
 */
 
var UPPERCASE_CHARS = [ 'A','B','C','D','E','F','G','H','I','J','K','L','M',
						'N','O','P','Q','R','S','T','U','V','W','X','Y','Z' ];
var LOWERCASE_CHARS = [ 'a','b','c','d','e','f','g','h','i','j','k','l','m',
						'n','o','p','q','r','s','t','u','v','w','x','y','z' ];
var NUMBER_CHARS    = [ '1','2','3','4','5','6','7','8','9','0' ];
var SYMBOL_CHARS    = [ '@','%','+','!','#','$','?',':','.','(',')','{','}','[',']','-','_' ];

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

	var chars = new Array();
	var result = new Array();
	var rnd = new Random(hash(key));
	var reserved = new Array();
	if ((include & UPPERCASE) == UPPERCASE) {
		chars.push(UPPERCASE_CHARS);
		var i = Math.abs(rnd.next() % length);
		result[i] = UPPERCASE_CHARS[Math.abs(rnd.next() % UPPERCASE_CHARS.length)];
		reserved.push(i);
	}
	if ((include & LOWERCASE) == LOWERCASE) {
		chars.push(LOWERCASE_CHARS);
		var i;
		do i = Math.abs(rnd.next() % length); while (reserved.includes(i));
		result[i] = LOWERCASE_CHARS[Math.abs(rnd.next() % LOWERCASE_CHARS.length)];
		reserved.push(i);
	}
	if ((include & NUMBERS) == NUMBERS) {
		chars.push(NUMBER_CHARS);
		var i;
		do i = Math.abs(rnd.next() % length); while (reserved.includes(i));
		result[i] = NUMBER_CHARS[Math.abs(rnd.next() % NUMBER_CHARS.length)];
		reserved.push(i);
	}
	if ((include & SYMBOLS) == SYMBOLS) {
		chars.push(SYMBOL_CHARS);
		var i;
		do i = Math.abs(rnd.next() % length); while (reserved.includes(i));
		result[i] = SYMBOL_CHARS[Math.abs(rnd.next() % SYMBOL_CHARS.length)];
		reserved.push(i);
	}
	
	for (var i = 0; i < length; ++i) {
		if (!reserved.includes(i)) {
			var charArr = chars[Math.abs(rnd.next() % chars.length)];
			result[i] = charArr[Math.abs(rnd.next() % charArr.length)];
		}
	}
	
	return result.join("");
}

// String hashcode
var table;
function hash(str) {
	if (table == undefined) {
		table = new Array();
		var l = new int(18856579789, 64).multiply(322115732);
		for (var i = 0; i < 256; ++i) {
			for (var j = 0; j < 31; ++j) {
				l = l.uright(7).xor(l);
				l = l.left(11).xor(l);
				l = l.uright(10).xor(l);
			}
			table[i] = l;
		}
	}
	var h = new int(5035555276, 64).multiply(2679553091);
	var m = new int(3620380643276297, 64).multiply(2117);
	for (var i = 0; i < str.length; ++i) {
		var c = new int(str.charCodeAt(i), 16);
		h = h.multiply(m).xor(table[c.and(255)]);
		h = h.multiply(m).xor(table[c.uright(8).and(255)]);
	}
	return h;
}