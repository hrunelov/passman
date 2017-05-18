
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
		if (size == undefined) {
			this.size = number.length;
			this.bits = number;
		} else {
			this.size = size;
			if (size > number.length) {
				this.bits = number;
				for (var i = number.length; i < size; ++i) {
					this.bits[i] = false;
				}
			} else this.bits = number.slice(0, size);
		}
	}
}

/**
 * Shifts bits to the left.
 * n: Number of positions to shift
 * return: this << n
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
 * return: this >> n
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
 * return: this >>> n
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
 * return: this + x
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
	return new int(result, this.size);
}

/**
 * Subtraction.
 * x: Number or integer to subtract
 * return: this - x
 */
int.prototype.subtract = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	return this.add(x.not().add(1)).copy(this.size);
}

/**
 * Multiplication.
 * x: Number or integer to multiply
 * return: this * x
 */
int.prototype.multiply = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var y = this.copy();
	var result = new int(0, x.size);
	while (y.toNumber() != 0) {
		result = result.add(x.left(y.lowestOneBitIndex()));
		y.bits[y.lowestOneBitIndex()] = false;
	}
	return result.copy(this.size);
}

/**
 * Bitwise AND.
 * x: Integer to compare
 * return: this & x
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
 * return: this | x
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
 * return: this ^ x
 */
int.prototype.xor = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	var result = new Array();
	for (var i = 0; i < x.size; ++i) result[i] = (this.bits[i] ^ x.bits[i] ? true:false);
	return new int(result);
}

/**
 * Bitwise NOT.
 * return: !this
 */
int.prototype.not = function() {
	var result = new Array();
	for (var i = 0; i < this.size; ++i) result[i] = !this.bits[i];
	return new int(result);
}

/**
 * Greater Than comparison.
 * return: this > x
 */
int.prototype.greaterThan = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	if (this.bits[this.size-1]) {
		var y = this.copy();
		y.bits[y.size-1] = false;
		return y.lessThan(i);
	}
	if (x.bits[x.size-1]) {
		var y = x.copy();
		y.bits[y.size-1] = false;
		return y.greaterThan(this);
	}
	for (var i = Math.max(this.size, x.size)-1; i >= 0; --i) {
		var a = this.bits[i];
		var b = x.bits[i];
		if (a == undefined && b) return false;
		if (b == undefined && a) return true;
		if (a != b) return a;
	}
	return false;
}

/**
 * Greater Than Or Equal To comparison.
 * return: this >= x
 */
int.prototype.greaterThanOrEquals = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	if (this.bits[this.size-1]) {
		var y = this.copy();
		y.bits[y.size-1] = false;
		return y.lessThanOrEquals(i);
	}
	if (x.bits[x.size-1]) {
		var y = x.copy();
		y.bits[y.size-1] = false;
		return y.greaterThanOrEquals(this);
	}
	for (var i = Math.max(this.size, x.size)-1; i >= 0; --i) {
		var a = this.bits[i];
		var b = x.bits[i];
		if (a == undefined && b) return false;
		if (b == undefined && a) return true;
		if (a != b) return a;
	}
	return true;
}

/**
 * Less Than comparison.
 * return: this < x
 */
int.prototype.lessThan = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	
	return x.greaterThan(this);
}

/**
 * Less Than Or Equal To comparison.
 * return: this <= x
 */
int.prototype.lessThanOrEquals = function(x) {
	if (typeof(x) == "number") x = new int(x, this.size);
	return x.greaterThanOrEquals(this);
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
	
	if (this.bits[size-1]) return -this.not().toNumber(size)-1;
	
	var result = 0;
	for (var i = 0; i < size; ++i) {
		var a = this.bits[i];
		if (a) result += Math.pow(2,i);
	}
	return result;
}

/**
 * Copies the integer.
 * return: Copy of this integer
 */
int.prototype.copy = function(size) {
	if (size == undefined) size = this.size;
	
	return new int(this.bits, size);
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