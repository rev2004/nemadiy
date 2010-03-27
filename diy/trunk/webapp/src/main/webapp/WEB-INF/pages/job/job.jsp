<%@ include file="/common/taglibs.jsp"%>
<head>
   <title><fmt:message key="jobdetails.title" /></title>
   <meta name="menu" content="jobdetails" />
   
   <c:if test="${(job.statusCode==2)||(job.statusCode==1)||(job.statusCode==0)}">
     <meta http-equiv="refresh" content="10" />
  </c:if>
</head>
<body id="jobdetails">
  <c:if test="${(job.statusCode==2)||(job.statusCode==1)||(job.statusCode==0)}" >
 
<h4>This page autorefreshes every 10 seconds</h4>
</c:if>
<h1>${job.name}</h1>
<p>${job.description}</p>
<c:if test="${!(empty jobForSubmission)}">
<div class="actionBox">
<p class="actionBox">
<c:url value="/get/JobManager.getSubmissions" var="submissionUrl"></c:url>

 <c:choose>
 	<c:when test="${jobForSubmission}"> <fmt:message key="job.forSubmission.success"/></c:when>
 	<c:otherwise><fmt:message key="job.forSubmission.fail"/></c:otherwise>
 	
 </c:choose>
 Check out <a href="${submissionUrl}">all current submission</a>
 

</p>
</div>
</c:if>

<div id="formcontainer_job">
  <div class="form_job">
    <fieldset id="pt1">
    <table id="outertable">
  
  	<tr><td>
    <table id="table">
      <tr>
        <td>Job Status:</td>
        <td>${job.jobStatus}</td>
      </tr>
      <tr>
        <td>Job Type:</td>
        <td>${job.flow.name}</td>
      </tr>
      <tr>
        <td>Submit Time:</td>
        <td>${job.submitTimestamp}</td>
      </tr>
      <tr>
        <td>Start Time:</td>
        <td>${job.startTimestamp}</td>
      </tr>
      <tr>
        <td>End Time:</td>
        <td>${job.endTimestamp}</td>
      </tr>
    </table>
    </td>
    <td>
    
    <table>
    
    <tr>
    <td>
   
    <c:if test="${job.done}">
    <p>Explore Results <br/>
    <a target="_blank" href="${resultSet.root.url}"><c:out value="${resultSet.root.displayString}" escapeXml="false"/></a>
    <ul>
    <c:forEach var="result" items="${resultSet.children}" >
     <li><a target="_blank" href="${result.url}"><c:out value="${result.displayString}" escapeXml="false"/></a></li>
    </c:forEach>
    </ul>
    </c:if>
    </p>
    </td>
    
    </tr>
    
    
    
    </table>
    
  
    
    </td>
    
    </tr>
    
    
    </table>
    
    
    </fieldset>
  </div>
  
    <form id="theform" action="<c:url value='/get/JobManager.selectJobForSubmission'/>" method="post">
    <input name="jobId" type="hidden" value="${job.id}" />
    <div class="form_job">
      <c:if test="${job.statusCode==3}">
        <fieldset id="pt1">
           <input id="submitform" name="submit" tabindex="6" value="Select As Submission" type="submit" />
        </fieldset>
      </c:if>
    </div>
  </form>
  
  
  <form id="theform" action="<c:url value='/get/JobManager.jobAction'/>" method="post">
    <input name="id" type="hidden" value="${job.id}" />
    <div class="form_job">
      <c:if test="${job.statusCode==2}">
        <fieldset id="pt1">
           <input id="submitform" name="submit" tabindex="6" value="Abort This Job" type="submit" />
        </fieldset>
      </c:if>
      <fieldset id="pt3">
         <input id="submitform" tabindex="6" name="submit" value="Delete This Job" type="submit" enabled />
      </fieldset>
    </div>
  </form>
  

    <c:if test="${job.statusCode==2}">
  	<script>
    new Ajax.PeriodicalUpdater('console', "<c:url value='/get/JobManager.getConsole'/>",
		  {
		    method: 'get',
		    parameters: {jobId:"${job.id}" },
		    frequency: 5,
		    decay:2
		});
    </script>
  </c:if>
  <textarea id="console" cols='90' rows='100'>(getting console...)</textarea>
  
  
</div>
</body>

