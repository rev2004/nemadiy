<%@ include file="/common/taglibs.jsp" %>

<title>Remote Method Invocation Error</title>
<head>
    <meta name="heading" content="Remote Access Failure"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<p>
This is non recoverable error, the NEMA Flow Service seems to be down.
</p>

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
