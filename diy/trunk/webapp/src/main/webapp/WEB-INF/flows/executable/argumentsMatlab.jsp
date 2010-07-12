
<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>fill the arguments</title>
		<script type="text/javascript" src="<c:url value='/scripts/add.js'/>"></script>
 <meta name="heading" content="Create Executable Profile: Step 2 of 3"/>
 <script type="text/javascript">
 //from phpMyAdmin
 function insertAtCursor(myField, prefix,myValueField,orderField) {
	 myValue=myValueField.options[myValueField.selectedIndex].value;
	 orderValue=orderField.options[orderField.selectedIndex].value;
	 if (myValue!="") {
		 myValue="'"+prefix+orderValue+"{"+myValue+"}'";
	 }
	 insertStrAtCursor(myField,myValue);
	};
function insertStrAtCursor(myField,myValue) {
		 
		//IE support
		if (document.selection) {
		myField.focus();
		sel = document.selection.createRange();
		sel.text = myValue;
		}
		//MOZILLA/NETSCAPE support
		else if (myField.selectionStart || myField.selectionStart == '0') {
		var startPos = myField.selectionStart;
		var endPos = myField.selectionEnd;
		myField.value = myField.value.substring(0, startPos)
		+ myValue
		+ myField.value.substring(endPos, myField.value.length);
		} else {
		myField.value += myValue;
		}
		myField.focus();
		};
	 
 </script>
</head>
<body>

    <div style="margin-bottom:5px">Specify the Arguments for the MATLAB Process</div>

	<form:form commandName="matlabTemplate">
		<form:errors path="*" cssStyle="color:OrangeRed" class="error"/>
		<fieldset id="pt1">
			<label class="label">${executable.type}
				File:</label>
			<c:out value="${executable.fileName}" />
		</fieldset>

		<fieldset id="pt1">
			<label class="label">jvm: </label>
			yes
			<form:radiobutton path="jvm" value="true" title="yes" />
			no
			<form:radiobutton path="jvm" title="no" value="false" />
		</fieldset>
		<fieldset id="pt1">
			<label class="label">timing: </label>
			yes
			<form:radiobutton path="timing" value="true" title="yes" />
			no
			<form:radiobutton path="timing" title="no" value="false" />
		</fieldset>
		<fieldset id="pt1">
			<label class="label">splash: </label>
			yes
			<form:radiobutton path="splash" value="true" title="yes" />
			no
			<form:radiobutton path="splash" title="no" value="false" />
		</fieldset>
		<fieldset id="pt1">
			<label class="label">display: </label>
			yes
			<form:radiobutton path="display" value="true" title="yes" />
			no
			<form:radiobutton path="display" title="no" value="false" />
		</fieldset>
		<fieldset id="pt1">
			<label class="label">debug: </label>
			enable
			<form:radiobutton path="debug" value="true" title="yes" />
			disable
			<form:radiobutton path="debug" title="no" value="false" />
		</fieldset>
		<fieldset id="pt1">
			<label class="label">logfile: </label>
			enable
			<form:radiobutton path="logfile" value="true" title="yes" />
			disable
			<form:radiobutton path="logfile" title="no" value="false" />
		</fieldset>
		<fieldset >
			<label class="label">log name: </label>
			<form:input path="log" />
			(ignored if logfile is disabled)
		</fieldset>


	<n:argumentsTemplateFunctionCall supportedInputFiles="${supportedInputFiles}" 
		supportedOutputFiles="${supportedOutputFiles}" niceParams="${niceParams}" plainTemplate="${matlabTemplate}"/>
        <n:argumentPagesSubmitButtons/>

	</form:form>
</body>