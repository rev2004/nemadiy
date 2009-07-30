<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
	<c:forEach var="fd" items="${meandreProxy.repository.availableFlowDescriptions}">  
		<thead>
			<th colspan="2">${fd.name}</th>
		</thead>
		<tbody>
			<tr>
				<td>Description</td>
				<td>
					${fd.description}
				</td>
			</tr>
			<tr>
				<td>Metadata</td>
				<td>
					Created by <em>${fd.creator}</em> on <em>${fd.creationDate}</em>
					under <em>${fd.rights}</em>
				</td>
			</tr>
			<tr>
				<td>Flow URI</td>
				<td>
					<code>${fd.flowComponent}</code>
				</td>
			</tr>
			<tr>
				<td>Actions</td>
				<td>
					<a href="/aux2/tune_options_hover.jsp?uri=${fd.flowComponent}">Go to Options</a>
				</td>
			</tr>
		</tbody>
	</c:forEach>
</table>