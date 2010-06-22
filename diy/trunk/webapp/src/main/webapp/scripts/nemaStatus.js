dojo.require("dojox.timing._base");


// set up the periodic update at the footer for the server status
function loadNemaStatus(sourceUrl) {
	function updateNemaStatus() {
		dojo.xhrGet( {
			url : sourceUrl,
			handleAs : "json",
			load : function(data) {
				dojo.byId("nemaLoad").innerHTML= data.load*100.0;
				dojo.byId("jobsInQueue").innerHTML = data.jobsInQueue;
			}
		});

	}
	var f = function() {
	var	t = new dojox.timing.Timer(2000);
		
		t.onTick = updateNemaStatus;
		t.start();
	};
	updateNemaStatus();
	return f;
}


// show the notification dialog box

dojo.require("dijit.Dialog");

function loadNotification(sourceUrl) {
	
	function updateNotification() {
		dojo.xhrGet( {
			url : sourceUrl,
			handleAs : "json",
			load : function(data) {
				var old=dijit.byId("notification");
				if (old!=null){
					old.destroyRecursive();
				}
				if (data.notifications!=null){
					var div=dojo.create("div", { style: { width:"120px",maxHeight:"160px",overflow:"auto" } });
					var ul = dojo.create("ul", null,div);
					dojo.forEach(data.notifications, function(notification){
					  dojo.create("li", { innerHTML: notification.message }, ul);
					});
					
					var	foo = new dijit.Dialog(
							{ 
								title: "Notification", 
								//autofocus:false,
								draggable:true,
								refocus:true,
								closable:true,
								content: div,
								class:"notification",
								id:"notification"
							});
					//dojo.byId("content").appendChild(foo.domNode);
					//foo.startup();
					foo.placeAt("copyright","before");
					foo.show();
				}
			}
		});
	}
	
	var f = function() {
	var	t = new dojox.timing.Timer(6000);

		t.onTick = updateNotification;
		t.start();
	};
	return f;
}