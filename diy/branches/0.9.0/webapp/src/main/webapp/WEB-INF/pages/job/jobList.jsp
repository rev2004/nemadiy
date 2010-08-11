<%@ include file="/common/taglibs.jsp"%>
<head>
<title>
<fmt:message key="joblist.title"/>
</title>
<meta http-equiv="refresh" content="10" />
<meta name="heading" content="${jobs.title}"/>
</head>


<display:table name="jobList" cellspacing="0" cellpadding="0" requestURI="" sort="list" defaultsort="5" 
defaultorder="descending" id="jobs"  class="table" export="false" pagesize="10" 
decorator="org.imirsel.nema.webapp.taglib.JobDecorator">
  <display:column property="name" escapeXml="true" sortable="true" titleKey="job.name"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  <display:column property="flow.submissionCode" escapeXml="true" sortable="true" title="Submission ID"
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
   <display:column property="duration" escapeXml="false" sortable="true"  title="Duration"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
</display:table>
<script type="text/javascript">
    highlightTableRows("jobs");
</script>
