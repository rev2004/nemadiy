<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>upload the executable</title>
</head>
<body>
upload the executable.
<form:form modelAttribute="executable" enctype="multipart/form-data">
	<fieldset id="pt1"><label>Please select file type:</label> <form:select
		path="type" items="${typeSet}" itemLabel="label" itemValue="code" />
	</fieldset>
	<fieldset id="pt1"><label>Please select the file for
	upload:</label> <input type="file" name="file" /></fieldset>
	<fieldset id="button"><input type="submit"
		name="_eventId_upload" value="upload" /> <input type="submit"
		name="_eventId_cancel" value="cancel" /></fieldset>

</form:form>

</body>