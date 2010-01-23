<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="submissions.title"/></title>
    <meta name="heading" content="${submissions.title}"/>
</head>
Click on your submissions to view details, the associated results or to remove them.

<display:table name="submissionList" cellspacing="0" cellpadding="0" requestURI="" 
    id="submissions" pagesize="5" class="table" export="false" defaultsort="3" defaultorder="descending" sort="list">
      
      <display:column property="name" escapeXml="true" sortable="true" titleKey="submission.name" style="width: 25%"
        url="/get/JobManager.submissionDetail?from=list" paramId="id" paramProperty="id"/>    
   
   
     
      <display:column property="type" escapeXml="true" sortable="true" titleKey="submission.type" style="width: 25%"
        url="/get/JobManager.submissionDetail?from=list" paramId="id" paramProperty="id"/>    
   
   
      <display:column property="dateCreated" escapeXml="true" sortable="true" titleKey="submission.dateCreated" style="width: 25%"
        url="/get/JobManager.submissionDetail?from=list" paramId="id" paramProperty="id"/>    
   
        
 </display:table>


<script type="text/javascript">
    highlightTableRows("submissions");
</script>
