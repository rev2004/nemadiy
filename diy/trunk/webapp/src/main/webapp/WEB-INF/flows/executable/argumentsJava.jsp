<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>fill the arguments</title>
</head>
<body>
<form:form commandName="javaCommand">
	<fieldset id="pt1"><form:errors path="*" /></fieldset>
	<fieldset id="pt1"><label>${executable.type}
	File:</label> <c:out value="${executable.fileName}" /></fieldset>

	<fieldset id="pt1"><label>Main Class:</label><form:input
		path="mainClass" /></fieldset>
	
	<fieldset id="pt1"><label>Verbose Execution GC: </label> yes<form:radiobutton
		path="verboseExecutionGC" value="true" title="yes" /> no<form:radiobutton
		path="verboseExecutionGC" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>Verbose Execution Class: </label>
	yes<form:radiobutton path="verboseExecutionClass" value="true"
		title="yes" /> no<form:radiobutton path="verboseExecutionClass"
		title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>Verbose Execution JNI: </label>
	yes<form:radiobutton path="verboseExecutionJNI" value="true"
		title="yes" /> no<form:radiobutton path="verboseExecutionJNI"
		title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>System Assertions: </label> enable<form:radiobutton
		path="enableSystemAssertions" value="true" title="yes" /> disable<form:radiobutton
		path="enableSystemAssertions" title="no" value="false" /></fieldset>
	<fieldset id="pt1"><label>Enable Assertion Packages:</label><form:input
		path="enableAssertionPackages" /></fieldset>
	<fieldset id="pt1"><label>Disable Assertion
	Packages:</label><form:input path="disableAssertionPackages" /></fieldset>
	<fieldset id="pt1"><label>Memory:</label><form:select
		path="memoryOption" items="${javaCommand.memoryOptions}" itemLabel="label" itemValue="code" /></fieldset>

	<fieldset id="button"><input type="submit"
		name="_eventId_review" value="review" /> <input type="submit"
		name="_eventId_back" value="back" /> <input type="submit"
		name="_eventId_cancel" value="cancel" /></fieldset>

</form:form>
</body>