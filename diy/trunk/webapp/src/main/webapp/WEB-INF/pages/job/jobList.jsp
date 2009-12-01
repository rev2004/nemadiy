<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="joblist.title"/></title>
    <meta name="heading" content="${jobs.title}"/>
</head>

<display:table name="jobList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="jobs" pagesize="25" class="table" export="false" decorator="org.imirsel.nema.webapp.taglib.JobDecorator">
    <display:column property="name" escapeXml="true" sortable="true" titleKey="job.name" style="width: 25%"
        url="/jobdetail.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="description" escapeXml="true" sortable="true" titleKey="job.description" style="width: 25%"
        url="/jobdetail.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="jobStatus" escapeXml="true" sortable="true" titleKey="job.status" style="width: 25%"
        url="/jobdetail.html?from=list" paramId="id" paramProperty="id"/>
        
    <display:column property="submitTimestamp" escapeXml="true" sortable="true" titleKey="job.submitTimestamp" style="width: 25%"
        url="/jobdetail.html?from=list" paramId="id" paramProperty="id"/>
        <display:column property="abort" titleKey="job.abort"/>
 </display:table>


<script type="text/javascript">
    highlightTableRows("jobs");
</script>
