
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
	}
	 
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


      	<fieldset>
			<label class="label">Environment Variables:</label>
			<input type="button" value="+" onclick="add($('envMain'),$('envParam1'),2)" />
			<br />
			<div id='envMain'>
				<c:forEach items="${matlabTemplate.environmentMap}" var="item">
					<div id='envParam0'>
						<input name="variable" type="text" value="${item.key}" />
						=
						<input name="value" type="text" value="${item.value}" />
						<input type="button" value="-" onclick="removeNode(this,'envParam1')" />
					</div>
				</c:forEach>
				<div id='envParam1'>
					<input name="variable" type="text" />
					=
					<input name="value" type="text" />
					<input type="button" value="-" onclick="removeNode(this,'envParam1')" />
				</div>
			</div>

		</fieldset>
			<fieldset id="pt1">
			<label class="label fixed">Other Argument Flags:</label>
			<input type="button" value="+" onclick="add($('otherMain'),$('otherParam1'),1)" />
			<br />
			<div id='otherMain'>
				<c:forEach items="${niceParams.others}" var="item">
					<div id='otherParam0'>
						<input name="other" type="text" value="${item}" />
						<input type="button" value="-" onclick="removeNode(this,'otherParam1')" />
					</div>
				</c:forEach>
				<div id='otherParam1'>
					<input name="other" type="text" />
					<input type="button" value="-" onclick="removeNode(this,'otherParam1')" />
				</div>
			</div>
		</fieldset>

		<fieldset>
			<c:set var="nums">1,2,3,4</c:set>
			<div>
				<label class="label fixed">Input Files:</label>
					<select name="inputNo" id='inputParam'>
						<option value="">Select</option>
						<c:forEach items="${supportedInputFiles}" var="file">
							<option value="${file.value}">${file.name}</option>
						</c:forEach>
					</select>
				<label class="label "> Index:</label>		
					<select name="inputOrder" id='inputParamOrder'>
						<c:forEach items="${nums}" var="num">
							<option value="${num}">${num}</option>
						</c:forEach>
					</select>
			<input type="button" value="+" onclick="insertAtCursor($('functionCall'),'$i',$('inputParam'),$('inputParamOrder'));" />
			</div>
			<div>
				<label class="label">Output Files:</label>
					<select name="outputNo" id='outputParam'>
						<option value="">Select</option>
						<c:forEach items="${supportedOutputFiles}" var="file">
							<option value="${file.value}">${file.name}</option>
						</c:forEach>
					</select>
				<label class="label "> Index:</label>		
					<select name="outputOrder" id='outputParamOrder'>
						<c:forEach items="${nums}" var="num">
							<option value="${num}">${num}</option>
						</c:forEach>
					</select>
					
			<input type="button" value="+" onclick="insertAtCursor($('functionCall'),'$o',$('outputParam'),$('outputParamOrder'));" />
			</div>
			<div><label class="label">Function Call: </label></div>
			<form:textarea path="functionCall" id="functionCall" cols="70" rows="20"/>
			<div><input type="button" value="Clear" onclick="$('functionCall').value='';"/></div>
		</fieldset>
        <n:argumentPagesSubmitButtons/>

	</form:form>
</body>