<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="serverStatus.title" /></title>




</head>
<body  onload="">
<h4 id="refresh">This page autorefreshes every 10 seconds</h4>
 <label class="label">Header Server</label>: ${head.host}:${head.port}
<display:table name="workers" cellspacing="0" cellpadding="0" requestURI="" sort="list" defaultsort="4" 
defaultorder="descending" id="workers"  class="table" export="false" pagesize="10">
  
</display:table>

<display:table name="scheduledJobs" cellspacing="0" cellpadding="0" requestURI="" sort="list" defaultsort="4" 
defaultorder="descending" id="jobs"  class="table" export="false" pagesize="10">
  <display:column property="name" escapeXml="true" sortable="true" titleKey="job.name"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="description" escapeXml="true" sortable="true" titleKey="job.description"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id" maxLength="25"/>
 
  <display:column property="scheduledTimestamp" escapeXml="true" sortable="true" titleKey="job.endTimestamp"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="host" escapeXml="true" sortable="true" titleKey="job.host"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id" />
  <display:column property="port" escapeXml="true" sortable="true" titleKey="job.port"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
</display:table>
</body>

