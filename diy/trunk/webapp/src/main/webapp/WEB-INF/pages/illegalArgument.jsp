<%@ include file="/common/taglibs.jsp" %>

<title>Data Access Error</title>
<head>
    <meta name="heading" content="Illegal Argument"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<p>
    <c:out value="${requestScope.exception.message}"/>
</p>

<!--
<% 
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace(new java.io.PrintWriter(out)); 
%>
-->

<a href="welcome.jsp" onclick="history.back();return false">&#171; Back</a>
