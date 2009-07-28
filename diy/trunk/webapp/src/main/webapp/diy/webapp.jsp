<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
	<c:when test="${empty username}"> 
		<jsp:forward page="/div/login.jsp"></jsp:forward>
	</c:when>
	<c:otherwise>
		<jsp:include page="header.jsp" flush="true" />
		<jsp:include page="webapp-navigation.jsp" flush="true" />
		<div id="mainContainer">
			<jsp:include page="webapp-main.jsp" flush="true" />
		</div>
		<jsp:include page="footer.jsp" flush="true" />
	</c:otherwise>
</c:choose>
