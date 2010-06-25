<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="serverStatus.title" /></title>

<meta name="heading" content="${serverStatus.title}"/>
<meta http-equiv="refresh" content="10" />
</head>
<body >
<h4 id="refresh">This page autorefreshes every 10 seconds</h4>
 <label class="label">Head Server</label>: ${head.host}:${head.port}
 <c:if test="${not empty workers}">
<table border="1">
	<thead>
		<tr>
			<th>Server</th>
			<th>Max Concurrent Jobs</th>
			<th>Running Jobs</th>
			<th>Aborting Jobs</th>
		</tr>
	</thead>
	<c:forEach items="${workers}" var="worker">
		
		<tr>
			<td>${worker.key.host}:${worker.key.port}</td>
			<td>${worker.key.maxConcurrentJobs}</td>
			<td>${worker.value.numRunning}</td>
			<td>${worker.value.numAborting}</td>
		</tr>
	</c:forEach>
</table>
</c:if>
<c:choose>
<c:when test="${not empty scheduledJobs}">
<display:table name="scheduledJobs" cellspacing="0" cellpadding="0" requestURI="" sort="list" defaultsort="3" 
defaultorder="descending" id="jobs"  class="table" export="false" pagesize="10">
  <display:column property="name" escapeXml="true" sortable="true" titleKey="job.name"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="description" escapeXml="true" sortable="true" titleKey="job.description"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id" maxLength="25"/>
  <display:column property="scheduleTimestamp" escapeXml="true" sortable="true" titleKey="job.scheduleTimestamp"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="host" escapeXml="true" sortable="true" titleKey="job.host"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id" />
  <display:column property="port" escapeXml="true" sortable="true" titleKey="job.port"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
</display:table>
</c:when>
<c:otherwise><label class="label">No jobs in queue.</label></c:otherwise>
</c:choose>
</body>

