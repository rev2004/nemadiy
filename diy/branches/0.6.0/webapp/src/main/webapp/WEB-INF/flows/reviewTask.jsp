<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="Preview Job Settings"/>
</head>
<body>
	<form:form>
	    <fieldset>
	        <div style="margin-bottom:5px;"><label style="font-weight:bold;">Job Name:</label> ${taskFlowModel.name}</div>
	    	<div style="margin-bottom:5px;"><label style="font-weight:bold;">Job Description:</label> ${taskFlowModel.description}</div>
	    </fieldset>
		<c:forEach var="component" items="${componentList}">
		 <c:if test="${(!component.hidden)&&(not empty datatypeMaps[component])}">
			<fieldset>
				<table>
					<thead>
						<h4>${component.name}:</h4>
					</thead>
					<c:forEach var="property" items="${datatypeMaps[component]}">
					<c:if test="${not fn:startsWith(property.key,'_') }">
						<tr>
							<td>
								<c:out value="${property.key}" />
							</td>
							<td>:</td>
							<td>
								<c:out value="${property.value.value}" />
							</td>
						</tr>
						</c:if>
					</c:forEach>
				</table>
			</fieldset>
			</c:if>
		</c:forEach>
		<fieldset id="button">
            <input type="submit" name="_eventId_back" value="Back" />
			<input type="submit" name="_eventId_run" value="Run Job" />
		</fieldset>
	</form:form>
</body>