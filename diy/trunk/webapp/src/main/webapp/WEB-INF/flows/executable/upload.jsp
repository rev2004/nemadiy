<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload the Executable</title>

<script>

function updateLabels(typeCode){
	var val=$(typeCode).options[$(typeCode).value].value;
	var archiveLabelMesage = "Archive containing the executable:";
	var executableLabelMessage="Path to the executable inside the archive:";
	if(val ==0){ // JAVA
		archiveLabelMesage = "Executable Jar or zip file containing jars:";
		executableLabelMessage="Main class including the package: ";
	}else if(val==1){// MATLAB
		archiveLabelMesage = "Archive containing MATLAB script(s):";
		executableLabelMessage="Path to the MATLAB script: ";
	}else if(val==2){ // C
		archiveLabelMesage = "Archive containing C executable binary:";
		executableLabelMessage="Path to the executable binary: ";
	}else if(val==3){ // Shell
		archiveLabelMesage = "Archive containing shell script(s):";
		executableLabelMessage="Path to the shell script: ";
	}
	 $('archiveLabel').innerHTML =archiveLabelMesage;
	 $('executableNameLabel').innerHTML=executableLabelMessage;
}

</script>

</head>
<body>

<div style="margin-bottom:5px;">Upload the Executable</div>

<form:form modelAttribute="executable" enctype="multipart/form-data">
	<fieldset><form:errors path="*" cssStyle="color:OrangeRed"/></fieldset>
	<fieldset>
	    <label id="executableTypeLabel">Specify the executable type:</label>
	    <form:select path="typeCode" onChange="updateLabels(this);" items="${executable.typeOptions}" itemLabel="name" itemValue="code" />
	</fieldset>
	<fieldset>
	    <label id="archiveLabel">Executable Jar or zip file containing jars:</label>
	    <input type="file" name="file" />
	</fieldset>
    <fieldset>
        <label id="executableNameLabel" >Main class including the package:</label>
        <form:input path="executableName" />
    </fieldset>
    <fieldset id="preferredOsLabel">
        <label>Operating system required to run the executable:</label>
        <form:select path="preferredOs" items="${supportedOs}" itemLabel="name" itemValue="value" />
	</fieldset>
	<fieldset>
	    <label id="groupLabel">Select the group:</label>
	    <form:select path="group" items="${supportedGroups}" itemLabel="name" itemValue="value" />
	</fieldset>
	<fieldset id="button">
	    <input type="submit" name="_eventId_upload" value="Upload" />
	    <input type="submit" name="_eventId_cancel" value="Cancel" />
	</fieldset>
</form:form>

</body>