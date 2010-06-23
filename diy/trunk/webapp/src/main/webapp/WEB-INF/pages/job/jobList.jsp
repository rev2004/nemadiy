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
<!-- meta http-equiv="refresh" content="10" /-->
<meta name="heading" content="${jobs.title}"/>
</head>
<body>
<h4>
 This page is refreshes every 10 seconds.   <input type="button" value="Click to Refresh Now" onClick="window.location.reload()">
</h4>	

<display:table name="jobList" cellspacing="0" cellpadding="0" requestURI="" sort="list" defaultsort="4" 
defaultorder="descending" id="jobs"  class="table" export="false" pagesize="10">
  <display:column property="name" escapeXml="true" sortable="true" titleKey="job.name"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="description" escapeXml="true" sortable="true" titleKey="job.description"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id" maxLength="25"/>
  <display:column property="jobStatus" escapeXml="true" sortable="true" titleKey="job.jobStatus"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="scheduleTimestamp" escapeXml="true" sortable="true" titleKey="job.scheduleTimestamp"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="submitTimestamp" escapeXml="true" sortable="true" titleKey="job.submitTimestamp"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="endTimestamp" escapeXml="true" sortable="true" titleKey="job.endTimestamp"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="host" escapeXml="true" sortable="true" titleKey="job.host"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id" />
  <display:column property="port" escapeXml="true" sortable="true" titleKey="job.port"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
</display:table>
<script type="text/javascript">
    highlightTableRows("jobs");
</script>
<div id="gridNode" style="height:400px;"></div>
<script type="text/javascript">
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dojox.grid.DataGrid");
function addGrid(){
dojo.xhrGet( {
			url : "<c:url value='/get/JobManager.getUserJobs.json'/>",
			handleAs : "json",
			load : function(data) {
					var storeData={
						identifier:'id',
						label:'name',
						//items:data.jobList
						items:[{id:3,name:'name1',scheduleTimestamp:"12-3",submitTimeStamp:'4:55',endTimeStamp:'3:20',statusCode:'5'},
						       {id:4,name:'name1',scheduleTimestamp:"12-3",submitTimeStamp:'4:55',endTimeStamp:'3:20',statusCode:'5'}]
					};
					var layout = [
				      
                        { field: "name", name: "Name", width: "25em" },
                        { field: "scheduleTimestamp", name: "Schedule", 
                                width: "auto" },
                        { field: "submitTimestamp", name: "submit", 
                                width: "50px" },
                        { field: "endTimestamp", name: "End Time", width: "50px" },
                        { field: "statusCode", name: "Status", width: "6em" }
                			
						];
					
					var jsonStore = new dojo.data.ItemFileReadStore({ data:storeData });
					var grid = new dojox.grid.DataGrid({
     						   	id: 'grid',
        						query: { id: '*' },
        						store: jsonStore,
        						structure: layout
							},
							document.createElement('div'));

				            // append the new grid to the div "gridNode":
				            dojo.byId("gridNode").appendChild(grid.domNode); 
				            
							grid.startup();
					}
			});
};
dojo.addOnLoad(addGrid);
</script>


</body>
