<%@ include file="/common/taglibs.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>fill the arguments</title>
    <script type="text/javascript" src="/scripts/add.js" ></script>
    <meta name="heading" content="Create Executable Profile: Step 2 of 3"/>
</head>
<body>


<div style="margin-bottom:5px">Specify the Arguments for the Java Process</div>

<form:form   commandName="javaTemplate">
	<form:errors path="*" cssStyle="color:OrangeRed" class="error"/>
	<fieldset>
	   <label>Uploaded Executable Archive:</label> ${executable.fileName}
	</fieldset>
	<fieldset>
        <label>Verbose Execution GC: </label> 
        yes<form:radiobutton path="verboseExecutionGC" value="true" title="yes" /> 
        no<form:radiobutton path="verboseExecutionGC" title="no" value="false" />
    </fieldset>
	<fieldset>
        <label>Verbose Execution Class: </label>
        yes<form:radiobutton path="verboseExecutionClass" value="true" title="yes" />
        no<form:radiobutton path="verboseExecutionClass" title="no" value="false" />
    </fieldset>
	<fieldset>
        <label>Verbose Execution JNI: </label>
        yes<form:radiobutton path="verboseExecutionJNI" value="true" title="yes" />
        no<form:radiobutton path="verboseExecutionJNI" title="no" value="false" />
    </fieldset>
	<fieldset>
        <label>System Assertions: </label>
        enable<form:radiobutton path="enableSystemAssertions" value="true" title="yes" />
        disable<form:radiobutton path="enableSystemAssertions" title="no" value="false" />
    </fieldset>
	<fieldset>
        <label>Enable Assertion Packages:</label>
        <form:input path="enableAssertionPackages" />
    </fieldset>
	<fieldset>
        <label>Disable Assertion Packages:</label>
        <form:input path="disableAssertionPackages" />
    </fieldset>
	<fieldset>
        <label>Memory:</label>
        <form:select path="memoryOption" items="${javaTemplate.memoryOptions}" itemLabel="label" itemValue="code"  />
    </fieldset>
	<fieldset>
        <label>System Properties:</label>
        <input type="button" value="+" onclick="add($('sysMain'),$('sysParam1'),2)" /><br />
        <div id='sysMain'>
        <c:forEach	items="${javaTemplate.properties}" var="item">
		<div id='sysParam0'>
			<input name="sysVar" type="text"	value="${item.name}" /> = 
			<input name="sysValue" type="text"	value="${item.value}" />
			<input type="button" value="-"	onclick="removeNode(this,'sysParam1')" />
		</div>
	</c:forEach>
            <div id='sysParam1' >
                <input name="sysVar" type="text" /> = <input name="sysValue" type="text" />
                <input type="button" value="-" onclick="removeNode(this,'sysParam1')" />
            </div>
        </div>
	</fieldset>	
	

	<n:argumentsTemplate supportFiles="${supportFiles}" niceParams="${niceParams}" plainTemplate="${javaTemplate}"></n:argumentsTemplate>
   
    <n:argumentPagesSubmitButtons/>

</form:form>
</body>