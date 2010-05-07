<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>fill the arguments</title>
</head>
<body>
<form:form commandName="executable">

    <fieldset id="pt1"><label>${executable.fileType} File:</label> <c:out value="${executable.file.name}"/></fieldset>
	<fieldset id="pt1"><label>Environment String:</label>  <form:textarea path="environment" rows="5" cols="50" /></fieldset>
	<fieldset id="pt1"><label>Arguments String:</label> <form:textarea path="args" rows="5" cols="50" /></fieldset>
	<fieldset id="button"><input type="submit"
		name="_eventId_review" value="review" /> <input type="submit"
		name="_eventId_back" value="back" /> <input type="submit"
		name="_eventId_cancel" value="cancel" /></fieldset>

</form:form>
</body>