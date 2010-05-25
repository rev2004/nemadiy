<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <meta name="heading" content="<fmt:message key='task.executable.review'/>"/>
<title>Review the executable setting</title>
</head>
<body>
<form:form>
	<form:errors path="*" cssStyle="color:OrangeRed" class="error"/>
	<fieldset id="pt1"><label>
	${executable.typeName}  Executable:</label> ${executable.fileName} </fieldset>
	<fieldset id="pt1"><label>Environment Variables:</label><code><c:forEach items="${executable.environmentVariables}" var="item">(${item.key}:${item.value}); </c:forEach></code></fieldset>
	<fieldset id="pt1"><label>Arguments:</label><c:out
		value="${executable.commandLineFlags}" /></fieldset>

	<fieldset id="button"><input type="submit"
		name="_eventId_save" value="Save" /> <input type="submit"
		name="_eventId_back" value="Back" /> <input type="submit"
		name="_eventId_cancel" value="Cancel" /></fieldset>

</form:form>
</body>