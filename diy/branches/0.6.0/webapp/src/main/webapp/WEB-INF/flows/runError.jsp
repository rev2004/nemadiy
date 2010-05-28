<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="jobdetails.title" /></title>
<meta name="menu" content="Error" />


</head>
<body >

<form:form>
    <form:errors path="*"/>
    <h2>An unexpected system error has occurred.</h2>
	<input type="submit" name="_eventId_retry" value="Try it Again" />
	<input type="submit" name="_eventId_cancel" value="Cancel" />

</form:form> 
</body>

