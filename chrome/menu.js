chrome.runtime.onMessage.addListener(
	function(request, sender, sendResponse) {
		refreshMenu();
	});

refreshMenu();

function refreshMenu() {
	chrome.contextMenus.removeAll();
	
	loadServices(function(services) {
		if (services.length > 0) {
			chrome.contextMenus.create({
				title: "Generate Password", 
				id: "generate",
				contexts: ["editable"]
			});
		}
		
		for (var i = 0; i < services.length; ++i) {
			chrome.contextMenus.create({
				parentId: "generate",
				title: services[i][0],
				id: i.toString(),
				contexts: ["editable"], 
				onclick: showGenerator
			});
		}
	});
}

function showGenerator(info, tab) {
	loadServices(function(services) {
		var service = services[parseInt(info.menuItemId)];
		chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
			chrome.tabs.sendMessage(tabs[0].id, service);
		});
	});
}