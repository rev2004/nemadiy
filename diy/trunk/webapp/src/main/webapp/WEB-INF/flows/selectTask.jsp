<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="flow.type"/></title>
<meta name="heading" content="<fmt:message key='flowtype.heading'/>"/>
</head>
<body id="jobstype"/>
<form:form commandName="taskFlowModel">
<c:choose>
<c:when test="${not empty flowList}">
  <h2>Select Task</h2>
  Please select a task to create your job for:
  <form:select path="id" items="${flowList}" itemLabel="name" itemValue="id" ></form:select>
  <input type="submit" name="_eventId_show" value="GO"/>

</c:when>
<c:otherwise><div class="error"> Sorry, no template exists!</div></c:otherwise>
</c:choose>
</form:form>
</body>
