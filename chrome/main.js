
function load() {
	var name = document.getElementById("name");
	var key = document.getElementById("key");
	var password = document.getElementById("password");

	var generate = function() {
		password.value = getPassword(name.value.toLowerCase() + key.value);
	}
	
	name.oninput = function() {
		generate();
	}

	key.oninput = function() {
		generate();
	}
}