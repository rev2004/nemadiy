<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="/login.jsp"></jsp:forward>
</c:if>
<%
MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
String sConsole = meandreProxy.getJobConsole(request.getParameter("uri"));
%>
<jsp:include page="/header.jsp" flush="true" />
<jsp:include page="/webapp-navigation.jsp" flush="true" />
<div id="mainContainer">	
	<div id="main">
		<p class="information">${username} on ${meandreURL}</p>
		<br/>
		<div id="main-divs-group">
			<table>
				<thead>
					<th>Output console for ${params.uri}</th>
				</thead>
				<tbody>
					<tr>
						<td>
							<pre>
								<% out.write("\n"+sConsole); %>
							</pre>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<jsp:include page="/footer.jsp" flush="true" />