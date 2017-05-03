
/**
 * n-bit integer class.
 */

/**
 * Constructs an n-bit integer.
 * number: Number or boolean array
 * size: Number of bits (default 32)
 */
function int(number, size) {
	if (typeof(number) == "number") {
		if (size == undefined) size = 32;
		this.size = size;
		
		this.bits = new Array();
		for (var i = 0; i < size; ++i) {
			this.bits[i] = false;
		}
		
		var neg = false;
		if (number < 0) {
			number *= -1;
			neg = true;
		}
		for (var i = 0; i < size && number > 0; ++i) {
			this.bits[i] = (number % 2)?true:false;
			number = Math.floor(number/2);
		}
		if (neg) {
			for (var i = 0; i < this.size; ++i) {
				this.bits[i] = !this.bits[i];
			}
			this.bits = this.add(1).bits;
		}
	} else if (typeof(number) == "object") {
		for (var i = 0; i < number.length; ++i) {
			if (typeof(number[i]) != "boolean") throw "First argument must be a number or boolean array";
		}
		this.size = number.length;
		this.bits = number;
	}
}

/**
 * Shifts bits to the left.
 * n: Number of positions to shift
 */
int.prototype.left = function(n) {
	var result = new Array();
	for (var i = this.size-1; i >= 0; --i) {
		result[i] = false;
		result[(i+n) % this.size] = this.bits[i];
	}
	return new int(result);
}

/**
 * Shifts bits to the right.
 * n: Number of positions to shift
 * return: Result
 */
int.prototype.right = function(n) {
	var result = new Array();
	for (var i = 0; i < this.size; ++i) {
		result[i] = true;
		var j = i-n;
		while (j < 0) j += this.size;
		result[j] = this.bits[i];
	}
	return new int(result);
}

/**
 * Shifts bits to the right, unsigned.
 * n: Number of positions to shift
 * return: Result
 */
int.prototype.uright = function(n) {
	var result = new Array();
	for (var i = 0; i < this.size; ++i) {
		result[i] = false;
		var j = i-n;
		while (j < 0) j += this.size;
		result[j] = this.bits[i];
	}
	return new int(result);
}

/**
 * Addition.
 * x: Number or integer to add
 * return: Result
 */
int.prototype.add = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var result = new Array();
	var carry = false;
	for (var i = 0; i < x.size; ++i) {
		var a = this.bits[i];
		var b = x.bits[i];
		var s = a+b+carry;
		result[i] = (s%2?true:false);
		carry = (s > 1);
	}
	return new int(result);
}

/**
 * Subtraction.
 * x: Number or integer to subtract
 * return: Result
 */
int.prototype.subtract = function(x) {
	if (typeof(x) != "number") x = x.toNumber();
	return this.add(x*-1);
}

/**
 * Multiplication.
 * x: Number or integer to multiply
 * return: Result
 */
int.prototype.multiply = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var y = this.copy();
	var result = new int(0, x.size);
	while (y.toNumber() != 0) {
		result = result.add(x.left(y.lowestOneBitIndex()));
		y.bits[y.lowestOneBitIndex()] = false;
	}
	return result;
}

/**
 * Bitwise AND.
 * x: Integer to compare
 * return: Result
 */
int.prototype.and = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var result = new Array();
	for (var i = 0; i < x.size; ++i) result[i] = this.bits[i] && x.bits[i];
	return new int(result);
}

/**
 * Bitwise OR.
 * x: Integer to compare
 * return: Result
 */
int.prototype.or = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var result = new Array();
	for (var i = 0; i < x.size; ++i) result[i] = this.bits[i] || x.bits[i];
	return new int(result);
}

/**
 * Bitwise XOR.
 * x: Integer to compare
 * return: Result
 */
int.prototype.xor = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var result = new Array();
	for (var i = 0; i < x.size; ++i) result[i] = (this.bits[i] ^ x.bits[i] ? true:false);
	return new int(result);
}

/**
 * Bitwise NOT.
 * return: Result
 */
int.prototype.not = function() {
	var result = new Array();
	for (var i = 0; i < this.size; ++i) result[i] = !this.bits[i];
	return new int(result);
}

/**
 * Position of lowest (rightmost) 1 in the bit array.
 * return: Index of lowest 1
 */
int.prototype.lowestOneBitIndex = function() {
	for (var i = 0; i < this.size; ++i) {
		if (this.bits[i]) return i;
	}
	return -1;
}

/**
 * Lowest (rightmost) 1 in the bit array.
 * return: Lowest 1
 */
int.prototype.lowestOneBit = function() {
	var i = this.lowestOneBitIndex();
	if (i < 0) return 0;
	return Math.pow(2,i);
}

/**
 * Position of highest (leftmost) 1 in the bit array.
 * return: Index of highest 1
 */
int.prototype.highestOneBitIndex = function() {
	for (var i = this.size-1; i >= 0; --i) {
		if (this.bits[i]) return i;
	}
	return -1;
}

/**
 * Highest (leftmost) 1 in the bit array.
 * return: Highest 1
 */
int.prototype.highestOneBit = function() {
	var i = this.highestOneBitIndex();
	if (i < 0) return 0;
	return Math.pow(2,i);
}

/**
 * Converts the integer into a JavaScript number type.
 * return: Integer as number
 */
int.prototype.toNumber = function(size) {
	if (size == undefined) size = this.size;
	
	var result = 0;
	for (var i = 0; i < size; ++i) {
		var a = this.bits[i];
		if (a) result += Math.pow(2,i);
	}
	if (this.bits[size-1]) result -= Math.pow(2,size);
	return result;
}

/**
 * Copies the integer.
 * return: Copy of this integer
 */
int.prototype.copy = function() {
	return new int(this.toNumber(), this.size);
}

/**
 * Converts the integer to a string.
 * return: Integer string representation
 */
int.prototype.toString = function() {
	return this.toNumber();
}

/**
 * Converts the integer's bit array to a string.
 * return: Integer bit array string representation
 */
int.prototype.toBinaryString = function() {
	var result = "";
	for (var i = this.size-1; i >= 0; --i) {
		var b = this.bits[i];
		result += (b?1:0);
	}
	return result;
}