<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="joblist.title"/></title>
    <meta name="heading" content="${jobs.title}"/>
</head>

<display:table name="jobList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="4" id="jobs" pagesize="5" class="table" export="false" >
    <display:column property="name" escapeXml="true" sortable="true" titleKey="job.name" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
    <display:column property="description" escapeXml="true" sortable="true" titleKey="job.description" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
   
   <display:column property="jobStatus" escapeXml="true" sortable="true" titleKey="job.jobStatus" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
 
    <display:column property="submitTimestamp" escapeXml="true" sortable="true" titleKey="job.submitTimestamp" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  
  <display:column property="startTimestamp" escapeXml="true" sortable="true" titleKey="job.startTimestamp" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  
  
  <display:column property="endTimestamp" escapeXml="true" sortable="true" titleKey="job.endTimestamp" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
  
    <display:column property="host" escapeXml="true" sortable="true" titleKey="job.host" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
     
       <display:column property="port" escapeXml="true" sortable="true" titleKey="job.port" style="width: 25%"
        url="/get/JobManager.jobDetail?from=list" paramId="id" paramProperty="id"/>
     
    
       
        
 </display:table>


<script type="text/javascript">
    highlightTableRows("jobs");
</script>
