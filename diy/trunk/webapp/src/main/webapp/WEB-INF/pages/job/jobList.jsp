<%@ include file="/common/taglibs.jsp"%>
<head>
<style type="text/css">
 @import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojox/grid/resources/Grid.css";
  @import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojox/grid/resources/tundraGrid.css";

  .dojoxGrid table {
    margin: 0;
  }
</style>

<title>
<fmt:message key="joblist.title"/>
</title>
<meta name="heading" content="${jobs.title}"/>
</head>
<body>
<h4>
 This page is refreshes every 10 seconds.   <input type="button" value="Click to Refresh Now" onClick="updateJobList();">
</h4>	

<div id="gridNode" style="height:400px;"></div>
<script type="text/javascript">
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.timing._base");
dojo.require("dojox.grid._Events");
function updateJobList(){
	
	dojo.xhrGet( {
			url : "<c:url value='/get/JobManager.getUserJobs.json'/>",
			handleAs : "json",
			load : function(data) {
					var storeData={
						identifier:'id',
						label:'name',
						items:data.jobList
						//items:[{id:3,name:'name1',scheduleTimestamp:"12-3",submitTimestamp:"4-5",endTimestamp:'12 -3 3:20',statusCode:'5'},
						 //     {id:4,name:'name2',another:3,scheduleTimestamp:"12-3",submitTimestamp:"4-2",endTimestamp:'3:20',statusCode:'5'}]
					};
					
					var jsonStore = new dojo.data.ItemFileReadStore({ data:storeData });
					var grid=dijit.byId("grid");
					grid.setStore(jsonStore);
					grid.update();
					
					}
			});
	};
function showJobList(){

	var layout = [
              
				  { field: "id", name: "ID", width: "4em"},    
                  { field: "name", name: "Name", width: "15em"},
                  { field: "scheduleTimestamp", name: "Schedule Time", width: "8em" },
                  { field: "submitTimestamp", name: "Submit Time", width: "8em" },
                  { field: "endTimestamp", name: "End Time", width: "8em" },
                  { field: "host", name: "Host", width: "7em" },
                  { field: "port", name: "port", width: "3em" },
                  { field: "status", name: "Status", width: "5em" }
          			
					];
	var storeData={
		identifier:'id',
		label:'name',
		
		items:[
		      {id:''}]
	};
	
	var jsonStore = new dojo.data.ItemFileReadStore({ data:storeData });
	var grid = new dojox.grid.DataGrid({
						autoheight:20,
	 				  	id: 'grid',
						query: { id: '*' },
						store: jsonStore,
						structure: layout
							},
					document.createElement('div'));

	// append the new grid to the div "gridNode":
	dojo.byId("gridNode").appendChild(grid.domNode); 

	grid.startup();
	
	var	t = new dojox.timing.Timer(10000);
		
	t.onTick = updateJobList;
	t.start();
	
	updateJobList();
	dojo.connect(grid,"onRowClick", grid, function(e){
		var jobId=grid.store.getValue(grid.getItem(e.rowIndex), 'id');
		var href="<c:url value='/get/JobManager.jobDetail?id='/>"+jobId;
		window.location=href;
	});

}
dojo.addOnLoad(showJobList);
</script>


</body>
