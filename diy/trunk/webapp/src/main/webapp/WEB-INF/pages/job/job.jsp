<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="jobdetails.title" /></title>
<meta name="menu" content="jobdetails" />


<script type="text/javascript">

	
     new PeriodicalExecuter(updateJob,10);
 
     var consoleUpdater=new Ajax.PeriodicalUpdater('console', "<c:url value='/get/JobManager.getConsole'/>",
 		  {
 		    method: 'get',
 		    parameters: {jobId:"${job.id}" },
 		    frequency: 5,
 		    decay:2
 		});
    
     
	function updateJob(pe){
  	  new Ajax.Request("<c:url value='/get/JobManager.jobDetail.json'/>",{
        method:'get',
        parameters: {id:"${job.id}" },
        onSuccess: function(fillJob){
               
        	var json=fillJob.responseText.evalJSON();
        	$('job.jobStatus').innerHTML=statusString(json.job.statusCode-0);
        	$('job.flowname').innerHTML=json.job.flow.name;
        	$('job.scheduleTimestamp').innerHTML=checkNull(json.job.scheduleTimestamp);
       		$('job.submitTimestamp').innerHTML=checkNull(json.job.submitTimestamp);
       		$('job.startTimestamp').innerHTML=checkNull(json.job.startTimestamp);
       		$('job.endTimestamp').innerHTML=checkNull(json.job.endTimestamp);
       		$('job.name').innerHTML=json.job.name;
       		$('job.description').innerHTML=json.job.description;
       		if (((json.job.statusCode-0)==3)&&($('resultContent').empty())){
           		$('result').show();           		
           		if (json.resultSet.root!=null){
           			var root = new Element('a', {  target:"_blank",href: json.resultSet.root.url });
           			root.innerHTML=json.resultSet.root.displayString;
           			$('resultContent').insert(root);
           			if (json.resultSet.children!=null){
						var children=new Element('ul');
						$('resultContent').insert({bottom:children});
						for (i=0;i<json.resultSet.children.size();i++){
							child=new Element('a', {  target:"_blank",href: json.resultSet.children[i].url});
							child.innerHTML= json.resultSet.children[i].displayString;
							li=new Element('li'); li.insert(child);
							children.insert(li);
						}
           			}	
           		}
       		}           
       		if (((json.job.statusCode-0)>=3)&&(pe!=null)) {
           		pe.stop();
           		consoleUpdater.stop();
           		$('refresh').hide();
           		new Ajax.Updater('console', "<c:url value='/get/JobManager.getConsole'/>",
              		  {
              		    method: 'get',
              		    parameters: {jobId:"${job.id}" },    		    
              		});
           	}
       		 		
    	}//onSuccess
		
    });
}
    function statusString(code) {
         var name = null;
         switch (code) {
            case -1: {
               name = "Unknown";
               break;
            }
            case 0: {
               name = "Scheduled";
               break;
            }
            case 1: {
               name = "Submitted";
               break;
            }
            case 2: {
               name = "Started";
               break;
            }
            case 3: {
               name = "Finished";
               break;
            }
            case 4: {
                name = "Failed";
                break;
             }
            case 5: {
                name = "Aborted";
                break;
             }
         }
         return name;
      }
     function checkNull(str){
         if (str==null){
             return "Pending...";
         }else {
             return str;
         }
     }

  </script>
</head>
<body id="jobdetails" onload="updateJob();">
<h4 id="refresh">This page autorefreshes every 10 seconds</h4>
<h1 id="job.name">${job.name}</h1>
<div id="job.description" class="surround">${job.description}</div>
<c:if test="${!(empty jobForSubmission)}">
	<div class="actionBox">
	<p class="actionBox"><c:url value="/get/JobManager.getSubmissions" var="submissionUrl"></c:url> <c:choose>
		<c:when test="${jobForSubmission}">
			<fmt:message key="job.forSubmission.success" />
		</c:when>
		<c:otherwise>
			<fmt:message key="job.forSubmission.fail" />
		</c:otherwise>

	</c:choose> Check out <a href="${submissionUrl}">all current submission</a></p>
	</div>
</c:if>

<div id="formcontainer_job">
<div class="form_job">
<fieldset id="pt1">
<table id="outertable">

	<tr>
		<td>


		<table id="table">
			<tr>
				<td><label class="label">Job Status</label></td><td>:</td>
				<td id="job.jobStatus">${job.jobStatus}</td>
			</tr>
			<tr>
				<td><label class="label">Job Type</label></td><td>:</td>
				<td id="job.flowname">${job.flow.name}</td>
			</tr>
			<tr>
				<td><label class="label">Schedule Time</label></td><td>:</td>
				<td id="job.scheduldeTimestamp">${job.scheduleTimestamp}</td>
			</tr>
			<tr>
				<td><label class="label">Submit to Meandre Server</label></td><td>:</td>
				<td id="job.submitTimestamp">${job.submitTimestamp}</td>
			</tr>
			<tr>
				<td><label class="label">Start Time</label></td><td>:</td>
				<td id="job.startTimestamp">${job.startTimestamp}</td>
			</tr>
			<tr>
				<td><label class="label">Finish Time</label></td><td>:</td>
				<td id="job.endTimestamp">${job.endTimestamp}</td>
			</tr>
		</table>
		</td>
		
		
		<td id="result" style="display:none; ">
			<div style="magin-left:10px;">
					Explore Results 
					<div id="resultContent" style="text-align:center"></div>
			</div>
		</td>
	</tr>
</table>
</fieldset>
</div>

<form id="theform" action="<c:url value='/get/JobManager.selectJobForSubmission'/>" method="post"><input
	name="jobId" type="hidden" value="${job.id}" />
<div class="form_job"><c:if test="${job.statusCode==3}">
	<fieldset id="pt1"><input id="submitform" name="submit" tabindex="6" value="Select As Submission"
		type="submit" /></fieldset>
</c:if></div>
</form>


<form id="theform" action="<c:url value='/get/JobManager.jobAction'/>" method="post"><input name="id"
	type="hidden" value="${job.id}" />
<div class="form_job"><c:if test="${job.statusCode==2}">
	<fieldset id="pt1"><input id="submitform" name="submit" tabindex="6" value="Abort This Job" type="submit" />
	</fieldset>
</c:if>
<fieldset id="pt3"><input id="submitform" tabindex="6" name="submit" value="Delete This Job" type="submit"
	enabled /></fieldset>
</div>
</form>


 <textarea id="console" cols='89' rows='100'>(getting console...)</textarea></div>
</body>

