<%@ page language="java" import="java.util.*,java.text.*,java.io.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
	<c:when test="${empty username}"> 
		<jsp:forward page="/diy/login.jsp"></jsp:forward>
	</c:when>
	<c:otherwise>
		<jsp:forward page="/diy/webapp.jsp"></jsp:forward>
	</c:otherwise>
</c:choose>

