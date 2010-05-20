<%@ include file="/common/taglibs.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>fill the arguments</title>
    <script type="text/javascript" src="/scripts/add.js" ></script>
</head>
<body>


<div style="margin-bottom:5px">Specify Arguments for the Executable</div>

<form:form commandName="paramTemplate">
	<fieldset><form:errors path="*" /></fieldset>
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
        <form:select path="memoryOption" items="${javaCommand.memoryOptions}" itemLabel="label" itemValue="code" />
    </fieldset>
	<fieldset>
        <label>System property:</label>
        <input type="button" value="+" onclick="add($('sysMain'),$('sysParam1'),2)" /><br />
        <div id='sysMain'>
            <div id='sysParam1' >
                <input name="sysVar" type="text" /> = <input name="sysValue" type="text" />
                <input type="button" value="-" onclick="removeNode(this,'envParam1')" />
            </div>
        </div>
	</fieldset>	
	<fieldset>
        <label>Environment Variables:</label>
        <input type="button" value="+" onclick="add($('envMain'),$('envParam1'),2)" /><br />
        <div id='envMain'>
            <div id='envParam1' >
                <input name="variable" type="text" /> = <input name="value" type="text" />
                <input type="button" value="-" onclick="removeNode(this,'envParam1')" />
            </div>
        </div>
	</fieldset>
	<fieldset>
        <label>Input Files:</label>
        <input type="button" value="+" onclick="addSelect($('inputMain'),$('inputParam1'))" /><br />
        <div id='inputMain'>
            <div id='inputParam1'>
                <select name="input">
                    <option value="">Select</option>
	                <c:forEach items="${supportedFiles}"  var="file">
	                   <option value="${file.value}">${file.name}</option>
	                </c:forEach>
	             </select>
	             <input type="button" value="-" onclick="removeNode(this,'inputParam1')" />
	        </div>
        </div>
	</fieldset>
	<fieldset>
        <label>Output Files:</label>
        <input type="button" value="+" onclick="addSelect($('outputMain'),$('outputParam1'))" /><br />
        <div id='outputMain'>
            <div id='outputParam1'>
                <select name="output">
                    <option value="">Select</option>
                    <c:forEach items="${supportedFiles}"  var="file">
                        <option value="${file.value}">${file.name}</option>
                    </c:forEach>
                </select>
                <input type="button" value="-" onclick="removeNode(this,'outputParam1')" />
            </div>
        </div>
	</fieldset>
	<fieldset>
       <label>Other Argument Flags:</label>
       <input type="button" value="+" onclick="add($('otherMain'),$('otherParam1'),1)" /><br />
       <div id='otherMain'>
           <div id='otherParam1'>
               <input name="other" type="text" />
               <input type="button" value="-" onclick="removeNode(this,'otherParam1')" />
           </div>
       </div>
    </fieldset>
	<fieldset id="button">
	   <input type="submit" name="_eventId_review" value="review" />
	   <input type="submit" name="_eventId_back" value="back" />
	   <input type="submit" name="_eventId_cancel" value="cancel" />
	</fieldset>

</form:form>
</body>