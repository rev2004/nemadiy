<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table>
	<thead>
		<h4>Parameters:</h4>
	</thead>
	<c:forEach var="property" items="${parameterMap}">
		<tr>
			<td><c:out value="${property.key}" /></td>
			<td><c:out value="${property.value}" /></td>
		</tr>
	</c:forEach>
</table>
<form:form>
	<input type="submit" name="_eventId_test" value="test" />
	<input type="submit" name="_eventId_run" value="run" />
	<input type="submit" name="_eventId_edit" value="edit"/>
	<input type="submit" name="_eventId_cancel" value="cancel" />
	
</form:form>
</body>
