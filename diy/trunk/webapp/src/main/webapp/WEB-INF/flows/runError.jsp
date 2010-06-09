<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="jobdetails.title" /></title>
<meta name="menu" content="Error" />


</head>
<body >


    <h2>An unexpected system error has occurred.</h2>
    
	${flowExecutionException}
	<label>Root: exception</label>
	${rootCauseException}
</body>

