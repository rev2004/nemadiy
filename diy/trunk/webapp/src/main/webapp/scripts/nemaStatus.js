
dojo.require("dojox.timing");

function loadNemaStatus(sourceUrl) {
	t = new dojox.timing.Timer(2000);

	t.onTick = function() {
		dojo.xhrGet( {
			url : sourceUrl,
			handleAs : "json",
			load : function(data) {
				dojo.byId("nemaLoad") = data.load;
				dojo.byId("jobsInQueue") = data.jobInQueue;
			}
		});

	}
	t.start();
}
