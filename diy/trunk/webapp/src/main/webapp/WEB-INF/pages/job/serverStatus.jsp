<%@ include file="/common/taglibs.jsp"%>
<head>
<style type="text/css">
 @import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojox/grid/resources/Grid.css";
  @import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojox/grid/resources/tundraGrid.css";

  .dojoxGrid table {
    margin: 0;
  }
  </style>
<title><fmt:message key="serverStatus.title" /></title>
<meta name="heading" content="${serverStatus.title}"/>
<script type="text/javascript"> 
console.log('start the grid from the begining');
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.timing._base");
dojo.require("dojox.grid._Events");
//dojo.require("dojo.style");
console.log('here comes the sciprt');
function updateServerStatus(){
	console.log("update server status");
	dojo.xhrGet( {
			url : "<c:url value='/get/JobManager.getServerStatus.json'/>",
			handleAs : "json",
			load : function(data) {
					

					var serverStoreData={
							items:data.workers
						};
						
					var workersJsonStore = new dojo.data.ItemFileReadStore({ data:serverStoreData });
					var workerGrid=dijit.byId("workerGrid");
					workerGrid.setStore(workersJsonStore);
					workerGrid.update();



					jobGridContainer=dojo.byId('jobGridNode');
					if (data.scheduledJobs.length==0){
						dojo.style('noJobInQueue','display',"" );
						dojo.style('jobGridNode','display',"none" );
					} else {
						dojo.style('noJobInQueue','display',"none" );
						dojo.style('jobGridNode','display',"" );
						var jobStoreData={
							identifier:'id',
							label:'name',
							items:data.scheduledJobs
							};
					
						var jobJsonStore = new dojo.data.ItemFileReadStore({ data:jobStoreData });

						var jobGrid=dijit.byId("jobGrid");
						jobGrid.setStore(jobJsonStore);
						jobGrid.update();
						dojo.byId("queueText").innerHTML="Total Count: <i>"+data.scheduledJobs.length+"</i>";
					};
					}
			});
	};
var serverStoredata={items:[{host:"..."}]};	
//var workersJsonStore = new dojo.data.ItemFileReadStore({ data:serverStoreData });
function startGrids(){
    console.log("start the dojo script");
     var layout=[
				  { field: "id", name: "ID", width: "4em"},    
                  { field: "name", name: "Name", width: "20em"},
                  { field: "scheduleTimestamp", name: "Schedule Time", width: "18em" },
                  { field: "host", name: "Host", width: "8em" },
                  { field: "port", name: "Port", width: "4em" }
                  //,{field:"description",name:"description", width:"10em"}
          			
					];
	var jobStoreData={
		identifier:'id',
		label:'name',
		items:[{id:''}]
	};
	
	var jobJsonStore = new dojo.data.ItemFileReadStore({ data:jobStoreData });
	var jobGrid = new dojox.grid.DataGrid({
						title:'Jobs in Queue',
						autoheight:10,
	 				  	id: 'jobGrid',
						query: { id: '*' },
						store: jobJsonStore,
						structure: layout,
						style:{height:'auto',
								borderStyle:'groove'}
							},
						
					dojo.create('div',{style:{height:'80%'}}));
	
	// append the new grid to the div "gridNode":
	//dojo.byId("jobGridNode").appendChild(jobGrid.domNode); 
	jobGrid.placeAt("queueStatus","before");
	jobGrid.startup();
	
	var	t = new dojox.timing.Timer(10000);
		
	t.onTick = updateServerStatus;
	t.start();
	console.log("set up the timer");
	updateServerStatus();

}
dojo.addOnLoad(startGrids);
</script>
</head>
<body >
<h4 id="refresh">This page autorefreshes every 10 seconds</h4>
 <label class="label">Head Server</label>: ${head.host}:${head.port}
<p></p>

<div id="serverGridNode" style="width:45em;text-align:center;" class="mycenter">
<label class="label center">Computation Nodes</label><p></p>
 <table dojoType="dojox.grid.DataGrid"  autoHeight="8" id="workerGrid" style="width: 100%; border-style:groove;" >
            <thead>
                <tr>
                    <th width="10em" field="host">
                        host
                    </th>
                    <th width="5em" field="port" >
                        Port
                    </th>
                
                    <th field="maxConcurrentJobs" width="16em">
                        Max Concurrent Jobs
                    </th>
                    <th field="numRunning" width="10em">Running Jobs</th>
					<th field="numAborting" width="10em">Aborting Jobs</th>
                </tr>
            </thead>
        </table>
 </div>
<div style="height:auto;"><p>        </p>
 <div id="noJobInQueue" style="width:70%;right-margin:auto;left-margin:auto;">No Jobs are in Queue.</div>
<div id="jobGridNode" style="width:45em;text-align:center;" class="mycenter">

	<label class="label ">Jobs in Queue</label>
	<p>   </p>
	<div id="queueStatus"  class="myright"	>
		<p></p><label class="label" id="queueText"></label>
		<p style="clear:both;"> </p>
	</div>
</div>
</div>
</body>

