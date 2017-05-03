
/**
 * JavaScript port of java.util.Random, stripped down to what's needed for the password generator.
 */

function Random(seed) {
	this.seed = new int(0,64);
	this.setSeed(seed);
}

Random.prototype.setSeed = function(seed) {
	if (typeof(seed) == "number") seed = new int(seed,64);
	if (seed.size != 64) throw "Seed must be 64-bit";
	this.seed = seed.xor(25214903917).and(new int(1,64).left(48).subtract(1));
}

Random.prototype.next = function()
{
	this.seed = this.seed.multiply(25214903917).add(11).and(new int(1,64).left(48).subtract(1));
	return this.seed.uright(16).toNumber(32);
}