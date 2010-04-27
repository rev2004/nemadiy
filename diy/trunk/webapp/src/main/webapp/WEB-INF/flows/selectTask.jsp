<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="flow.type"/></title>
<meta name="heading" content="<fmt:message key='flowtype.heading'/>"/>
</head>
<body id="jobstype"/>
<form:form commandName="taskFlowModel">
  <h2>Select Task</h2>
  Please select a task to create your job for:
  <form:select path="id" items="${flowSet}" itemLabel="name" itemValue="id" ></form:select>
  <input type="submit" name="_eventId_show" value="GO"/>
</form:form>
</body>
