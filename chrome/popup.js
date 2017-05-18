var UPPERCASE = 0b0001;
var LOWERCASE = 0b0010;
var NUMBERS   = 0b0100;
var SYMBOLS   = 0b1000;

document.addEventListener("DOMContentLoaded", function() {
	var addButton = document.getElementById("addButton");
	addButton.onclick = function() {
		var name = prompt("Enter name of service or website:");
		putService(name, 0b1111);
		refresh();
	};
	refresh();
});

function refresh() {
	loadServices(function(services) {
		var listDiv = document.getElementById("serviceList");
		var template = document.getElementById("serviceTemplate");
		listDiv.innerHTML = "";
		
		var info = document.getElementById("info");
		info.style.display = (services.length == 0 ? "block":"none");
		
		for (var i = 0; i < services.length; ++i) {
			(function () {
				var service = services[i];
				var name = service[0];
				var include = service[1];
				
				var servDiv = template.cloneNode(true);
				servDiv.removeAttribute("id");
				
				var button = servDiv.getElementsByClassName("serviceButton")[0];
				button.value = name;
				button.onclick = function() {
					var newName = prompt("Enter name of service or website:", name);
					removeService(name);
					putService(newName, include);
					refresh();
				};
				
				var removeButton = servDiv.getElementsByClassName("removeButton")[0];
				removeButton.onclick = function() {
					removeService(name);
					refresh();
				};
				
				var uppercase = servDiv.getElementsByClassName("uppercase")[0];
				uppercase.checked = (include & UPPERCASE) == UPPERCASE;
				var lowercase = servDiv.getElementsByClassName("lowercase")[0];
				lowercase.checked = (include & LOWERCASE) == LOWERCASE;
				var numbers = servDiv.getElementsByClassName("numbers")[0];
				numbers.checked = (include & NUMBERS) == NUMBERS;
				var symbols = servDiv.getElementsByClassName("symbols")[0];
				symbols.checked = (include & SYMBOLS) == SYMBOLS;
				
				var changed = function() {
					var newInclude = 0b0000;
					if (uppercase.checked) newInclude |= UPPERCASE;
					if (lowercase.checked) newInclude |= LOWERCASE;
					if (numbers.checked) newInclude |= NUMBERS;
					if (symbols.checked) newInclude |= SYMBOLS;
					putService(name, newInclude);
					refresh();
				};
				uppercase.onchange = changed;
				lowercase.onchange = changed;
				numbers.onchange = changed;
				symbols.onchange = changed;
				
				listDiv.appendChild(servDiv);
			}());
		}
		chrome.runtime.sendMessage("refresh");
	});
}