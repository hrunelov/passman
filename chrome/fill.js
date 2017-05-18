chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	var key = prompt("Enter Master Password:", "");
	if (key == null || key.length == 0) return;
	var textField = document.activeElement;
	textField.value = getPassword(request[0].toLowerCase() + key, request[1]);
});