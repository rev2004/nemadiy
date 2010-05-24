
<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload the Executable</title>
		<meta name="heading" content="
			<fmt:message key='task.executable.upload' />
			"/>
			<script>

				function updateLabels(typeCode){
				var val=$(typeCode).options[$(typeCode).value].value;
				var archiveLabelMesage = "Archive containing the executable:";
				var executableLabelMessage="Path to the executable inside the archive:";
				if(val ==0){ // JAVA
				archiveLabelMesage = "Executable JAR or ZIP file containing JARs:";
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

		<form:errors path="*" cssStyle="color:OrangeRed" class="error" />

		<fieldset>
			<label id="executableTypeLabel">Specify the executable type:</label>
			<form:select path="typeCode" onChange="updateLabels(this);" items="${executable.typeOptions}" itemLabel="name"
				itemValue="code" />
		</fieldset>
		<fieldset>
			<label id="archiveLabel">Executable Jar or zip file containing jars:</label>
			<input type="file" name="uploadedFile" />
		</fieldset>
		<fieldset>
			<label id="executableNameLabel">Main class including the package:</label>
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
		<c:if test="${not empty executable.fileName}">
			<fieldset id="pt1">
				<label>
					${executable.typeName} Executable:</label>
				${executable.fileName}
			</fieldset>
			<fieldset id="pt1">
				<label>environment:</label>				
					<c:forEach items="${executable.environmentVariables}" var="item">(${item.key}:${item.value}); </c:forEach>
			</fieldset>
			<fieldset id="pt1">
				<label>arguments:</label>
				<c:out value="${executable.commandLineFlags}" />
			</fieldset>
		</c:if>
		<fieldset id="button">
			<input type="submit" name="_eventId_upload" value="Upload" />
			<input type="submit" name="_eventId_cancel" value="Cancel" />
		</fieldset>
	</form:form>

</body>