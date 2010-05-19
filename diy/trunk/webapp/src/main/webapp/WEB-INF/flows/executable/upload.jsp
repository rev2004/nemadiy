<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload the Executable</title>
</head>
<body>

<div style="margin-bottom:5px">Upload the executable</div>

<form:form modelAttribute="executable" enctype="multipart/form-data">
	<fieldset><form:errors path="*"/></fieldset>
	<fieldset>
	    <label>Specify the executable type:</label>
	    <form:select path="typeCode" items="${executable.typeOptions}" itemLabel="name" itemValue="code" />
	</fieldset>
	<fieldset>
	    <label>Archive containing the executable:</label>
	    <input type="file" name="file" />
	</fieldset>
    <fieldset>
        <label>Operating system required to run the executable:</label>
        <form:select path="preferredOs" items="${supportedOs}" itemLabel="name" itemValue="value" />
	</fieldset>
	<fieldset>
	    <label>Select the group:</label>
	    <form:select path="group" items="${supportedGroups}" itemLabel="name" itemValue="value" />
	</fieldset>
	<fieldset id="button">
	    <input type="submit" name="_eventId_upload" value="Upload" />
	    <input type="submit" name="_eventId_cancel" value="Cancel" />
	</fieldset>
</form:form>

</body>