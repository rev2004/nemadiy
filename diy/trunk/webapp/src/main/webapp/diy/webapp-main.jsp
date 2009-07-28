<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="main">
	<p class="information">${username} on ${meandreURL}</p>
	<br/>
	<div id="main-divs-group">
		<c:choose>
			<c:when test="${menu=='Flows'}">
				<jsp:include page="webapp-flows.jsp"/>
			</c:when>
			<c:when test="${menu=='Jobs'}">
				<jsp:include page="webapp-jobs.jsp"/>
			</c:when>
		</c:choose>
	</div>
</div>


  
