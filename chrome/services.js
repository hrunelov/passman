var UPPERCASE = 0b0001;
var LOWERCASE = 0b0010;
var NUMBERS   = 0b0100;
var SYMBOLS   = 0b1000;

function loadServices(callback) {
	var services = new Array();
	chrome.storage.sync.get(null, function(items) {
		var keys = Object.keys(items);
		for (var i = 0; i < keys.length; ++i) {
			var item = new Array();
			item[0] = keys[i];
			item[1] = items[keys[i]];
			services[i] = item;
		}
		services.sort(function(a,b) {
			return a[0].localeCompare(b[0]);
		});
		callback(services);
	});
}

function putService(name, include) {
	removeService(name);
	if (name == null || name == "") return;
	
	var item = new Object();
	item[name] = include;
	chrome.storage.sync.set(item);
}

function removeService(name) {
	chrome.storage.sync.remove(name);
}