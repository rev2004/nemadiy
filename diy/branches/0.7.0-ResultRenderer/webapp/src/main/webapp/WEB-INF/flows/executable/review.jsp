<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <meta name="heading" content="Create Executable Profile: Step 3 of 3"/>
<title>Review the executable setting</title>
</head>
<body>

<div style="margin-bottom:5px">Review the Executable Profile Settings</div>

<form:form>
	<form:errors path="*" cssStyle="color:OrangeRed" class="error"/>
	<fieldset class="label"><label>
	${executable.typeName}  Executable:</label><label class="name"> ${executable.fileName}</label> </fieldset>
	<fieldset id="pt1">
		<label class="label">Environment Variables:</label>
			<c:if test="${empty executable.environmentVariables}">None</c:if>
			<c:forEach items="${executable.environmentVariables}" var="item">(${item.key}:${item.value}); </c:forEach> 
	</fieldset>

	<fieldset id="pt1">
	     <label class="label">Arguments:</label>
	     ${executable.commandLineFlags}
	</fieldset>

	<fieldset id="button">
	    <input type="submit" name="_eventId_back" value="Back" />
	    <input type="submit" name="_eventId_save" value="Save" />
	    <input type="submit" name="_eventId_cancel" value="Cancel"  style="float:right"/>
	</fieldset>
</form:form>
</body>