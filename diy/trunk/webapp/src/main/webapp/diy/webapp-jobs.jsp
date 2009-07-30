<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
	<thead>

		<th>Server ID</th>
		<th>Status</th>
		<th>Time stamp</th>
		<th>Output console</th>
	</thead>
	<tbody>
		<c:forEach var="job" items="${meandreProxy.jobStatuses}">  
			<tr>

				<td>${job.server_id}</td>
				<td>${job.status}</td>
				<td>${job.ts}</td>
				<td><a href="/aux2/console.jsp?uri=${job.job_id}">${job.job_id}</a></td>
			</tr>
		</c:forEach>	
	</tbody>
</table>
