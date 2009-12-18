<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="submissions.title"/></title>
    <meta name="heading" content="${submissions.title}"/>
</head>

Below are all the submissions for the selected user


<display:table name="submissionList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="submissions" pagesize="5" class="table" export="false" >
      
      <display:column property="name" escapeXml="true" sortable="true" titleKey="submission.name" style="width: 25%"
        url="/get/JobManager.submissionDetail?from=list" paramId="id" paramProperty="id"/>    
   
   
     
      <display:column property="type" escapeXml="true" sortable="true" titleKey="submission.type" style="width: 25%"
        url="/get/JobManager.submissionDetail?from=list" paramId="id" paramProperty="id"/>    
   
   
      <display:column property="dateCreated" escapeXml="true" sortable="true" titleKey="submission.dateCreated" style="width: 25%"
        url="/get/JobManager.submissionDetail?from=list" paramId="id" paramProperty="id"/>    
   
        
 </display:table>
 
 
<form id="theform" action="<c:url value='/get/JobManager.getAllSubmissions'/>" method="post">
<select name="userId">
<option value="">All</option>
<c:forEach var="user" items="${userList}">
<option <c:if test="${user.id == userForm}">selected</c:if> value="${user.id}">${user.username}</option>
</c:forEach>
</select>
<input type="submit" name="submit" value="submit"/>
</form>
 


<script type="text/javascript">
    highlightTableRows("submissions");
</script>
