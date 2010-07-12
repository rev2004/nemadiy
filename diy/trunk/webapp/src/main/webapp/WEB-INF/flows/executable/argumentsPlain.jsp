
<%@ include file="/common/taglibs.jsp"%>

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>fill the arguments</title>
		<script type="text/javascript" src="<c:url value='/scripts/add.js'/>"></script>
 <meta name="heading" content="Create Executable Profile: Step 2 of 3"/>
</head>
<body>

<div style="margin-bottom:5px">Specify the Arguments for the ${executable.typeName} Process</div>

	<form:form commandName="plainTemplate">
		<form:errors path="*" cssStyle="color:OrangeRed" class="error"/>
		<fieldset id="pt1">
			<label>${executable.type} File:</label>
			<c:out value="${executable.fileName}" />
		</fieldset>

	
		
		<n:argumentsTemplateFunctionCall supportedInputFiles="${supportedInputFiles}" supportedOutputFiles="${supportedOutputFiles}" niceParams="${niceParams}" plainTemplate="${plainTemplate}"/>
		
        <n:argumentPagesSubmitButtons/>
        
	</form:form>
</body>