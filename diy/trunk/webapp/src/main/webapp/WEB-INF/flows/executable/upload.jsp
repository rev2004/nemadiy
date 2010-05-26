
<%@ include file="/common/taglibs.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Upload the Executable</title>
        <meta name="heading" content="<fmt:message key='task.executable.upload' />"/>
            <script>

                function updateLabels(val){
                   
                var archiveLabelMessage = 'Archive containing the executable:';
                var uploadedArchiveLabelMessage='Chosen archive containing the executable:';
                var executableLabelMessage='Path to the executable inside the archive:';
                
                if(val ==0){ // JAVA
                archiveLabelMessage = 'Executable JAR or ZIP file containing JARs:';
                uploadedArchiveLabelMessage = 'Chosen executable JAR or ZIP file containing JARs:';
                executableLabelMessage='Main class including the package: ';
                }else if(val==1){// MATLAB
                archiveLabelMessage = 'ZIP archive containing MATLAB script(s):';
                uploadedArchiveLabelMessage = 'Chosen archive containing MATLAB script(s):';
                executableLabelMessage='Path to the MATLAB script: ';
                }else if(val==2){ // C
                archiveLabelMessage = 'ZIP archive containing C executable binary:';
                uploadedArchiveLabelMessage = 'Chosen archive containing C executable binary:';
                executableLabelMessage='Path to the executable binary: ';
                }else if(val==3){ // Shell
                archiveLabelMessage = 'ZIP archive containing shell script(s):';
                uploadedArchiveLabelMessage = 'Chosen archive containing shell script(s):';
                executableLabelMessage='Path to the shell script: ';
                }
                $('archiveLabel').innerHTML=archiveLabelMessage;
                $('uploadedArchiveLabel').innerHTML=uploadedArchiveLabelMessage;
                $('executableNameLabel').innerHTML=executableLabelMessage;

                }
                
              </script>

</head>
<body onLoad="updateLabels(${executable.typeCode});">

    <div style="margin-bottom:5px;">Upload the Executable</div>

<form:form modelAttribute="executable" enctype="multipart/form-data">
    
    <form:errors path="*" cssClass="error"/>

    <c:set var="archiveNotSelectedStyle">
      <c:if test="${executable.fileName ne null}">display:none</c:if>
    </c:set>
    
    <c:set var="archiveSelectedStyle">
      <c:if test="${executable.fileName eq null}">display:none</c:if>
    </c:set>
        
    <fieldset style="${archiveNotSelectedStyle}">
        <label id="executableTypeLabel">Specify the executable type:</label>
        <form:select path="typeCode" onChange="updateLabels(this.options[this.value].value);" items="${executable.typeOptions}" itemLabel="name" itemValue="code" />
    </fieldset>

    <fieldset style="${archiveSelectedStyle}">
        <label>Executable type:</label>
        ${executable.typeName}
    </fieldset>
    
    <fieldset style="${archiveNotSelectedStyle}">
        <label id="archiveLabel">Executable JAR or ZIP file containing JARs:</label>
        <input type="file" name="uploadedFile" />
    </fieldset>
    
    <fieldset style="${archiveSelectedStyle}">
        <label id="uploadedArchiveLabel">Chosen archive:</label>
        ${executable.fileName}
        <input type="submit" name="_eventId_clearArchive" value="Clear Archive" />
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
        <input type="submit" name="_eventId_upload" value="Next" />
        <input type="submit" name="_eventId_cancel" value="Cancel" />
    </fieldset>
</form:form>

</body>