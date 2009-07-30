<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="../diy/login.jsp"></jsp:forward>
</c:if>
<%
MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
String sConsole = meandreProxy.getJobConsole(request.getParameter("uri"));
%>
<jsp:include page="../diy/header.jsp" flush="true" />
<jsp:include page="../diy/webapp-navigation.jsp" flush="true" />
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
<jsp:include page="../diy/footer.jsp" flush="true" />