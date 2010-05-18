<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>fill the arguments</title>
</head>
<body>
<form:form commandName="matlabCommand">
	<fieldset id="pt1"><form:errors path="*" /></fieldset>
	<fieldset id="pt1"><label>${executable.type}
	File:</label> <c:out value="${executable.fileName}" /></fieldset>
	<fieldset id="pt1"><label>jvm: </label> yes<form:radiobutton
		path="jvm" value="true" title="yes" /> no<form:radiobutton path="jvm"
		title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>timing: </label> yes<form:radiobutton
		path="timing" value="true" title="yes" /> no<form:radiobutton
		path="timing" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>splash: </label> yes<form:radiobutton
		path="splash" value="true" title="yes" /> no<form:radiobutton
		path="splash" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>display: </label> yes<form:radiobutton
		path="display" value="true" title="yes" /> no<form:radiobutton
		path="display" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>debug: </label> enable<form:radiobutton
		path="debug" value="true" title="yes" /> disable<form:radiobutton
		path="debug" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>logfile: </label> enable<form:radiobutton
		path="logfile" value="true" title="yes" /> disable<form:radiobutton
		path="logfile" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>log name: </label><form:input
		path="log" /> (ignored if logfile is disabled)</fieldset>
		
	<fieldset id="button"><input type="submit"
		name="_eventId_review" value="review" /> <input type="submit"
		name="_eventId_back" value="back" /> <input type="submit"
		name="_eventId_cancel" value="cancel" /></fieldset>

</form:form>
</body>