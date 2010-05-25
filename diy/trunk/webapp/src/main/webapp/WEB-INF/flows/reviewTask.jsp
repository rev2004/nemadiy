<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="${taskFlowModel.name}"/>
</head>
<body>
	<form:form>
	    <fieldset id="pt1">
	    	<h4>Description:</h4>
	        ${taskFlowModel.description}
	    </fieldset>
		<c:forEach var="component" items="${componentList}">
			<fieldset id="pt1">
				<table>
					<thead>
						<h4>${component.name}:</h4>
					</thead>
					<c:forEach var="property" items="${datatypeMaps[component]}">
						<tr>
							<td>
								<c:out value="${property.key}" />
							</td>
							<td>
								<c:out value="${property.value.value}" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</fieldset>
		</c:forEach>
		<fieldset id="button">
            <input type="submit" name="_eventId_back" value="Back" />
			<input type="submit" name="_eventId_run" value="Run Task" />
		</fieldset>
	</form:form>
</body>