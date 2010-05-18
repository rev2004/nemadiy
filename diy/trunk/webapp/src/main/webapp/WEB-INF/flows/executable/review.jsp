<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Review the executable setting</title>
</head>
<body>
<form:form>
	<fieldset id="pt1"><form:errors path="*"/></fieldset>
	<fieldset id="pt1"><label>
	File:</label> ${bundle.fileName} ${bundle.typeName} /></fieldset>
	<fieldset id="pt1"><label>environment:</label><code><c:forEach items="${bundle.environmentVariables}" var="item">(${item.key}:${item.value}); </c:forEach> </code></fieldset>
	<fieldset id="pt1"><label>arguments:</label><c:out
		value="${bundle.commandLineFlags}" /></fieldset>

	<fieldset id="button"><input type="submit"
		name="_eventId_save" value="save" /> <input type="submit"
		name="_eventId_back" value="back" /> <input type="submit"
		name="_eventId_cancel" value="cancel" /></fieldset>

</form:form>
</body>