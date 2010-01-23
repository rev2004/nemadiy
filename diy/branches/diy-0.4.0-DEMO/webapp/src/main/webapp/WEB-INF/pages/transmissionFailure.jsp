<%@ include file="/common/taglibs.jsp" %>

<title>Transmission Error</title>
<head>
    <meta name="heading" content="Transmission Failure"/>
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

<a href="/get/JobManager.getUserJobs" onclick="history.back();return false">&#171; Back</a>
