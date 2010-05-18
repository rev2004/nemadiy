<%@ include file="/common/taglibs.jsp"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>fill the arguments</title>
<script type="text/javascript" src="/scripts/add.js" ></script>

</head>
<body >
<form:form commandName="executable">

		<fieldset id="pt1"><form:errors path="*" /></fieldset>
	<fieldset id="pt1"><label>${executable.type}
	File:</label> <c:out value="${executable.fileName}" /></fieldset>
	<fieldset id="pt1"><label>Environment Variables:</label> <input
		type="button" value="+" onclick="add($('envMain'),$('envParam1'),2)" /><br />
	<div id='envMain'>
	<div id='envParam1' ><input name="variable" type="text" /> = <input
		name="value" type="text" /><input type="button" value="-"
		onclick="removeNode(this,'envParam1')" /></div>
	</div>
	
	</fieldset>
	<fieldset id="pt1"><label>Input Files:</label> <input
		type="button" value="+" onclick="addSelect($('inputMain'),$('inputParam1'))" /><br />
	<div id='inputMain'>
	<div id='inputParam1'><select name="input">
	<option value="">Select</option>
	<c:forEach items="${supportedFiles}"  var="file"><option value="${file.value}">${file.name}</option></c:forEach></select> <input type="button" value="-"
		onclick="removeNode(this,'inputParam1')" /></div>
	</div>
	</fieldset>
	<fieldset id="pt1"><label>Output Files:</label> <input
		type="button" value="+" onclick="addSelect($('outputMain'),$('outputParam1'))" /><br />
	<div id='outputMain'>
	<div id='outputParam1'><select name="output"><option value="">Select</option>
	<c:forEach items="${supportedFiles}"  var="file"><option value="${file.value}">${file.name}</option></c:forEach></select><input type="button" value="-"
		onclick="removeNode(this,'outputParam1')" /></div>
	</div>
	</fieldset>
	<fieldset id="pt1"><label>Other Argument Flags:</label> <input
		type="button" value="+" onclick="add($('otherMain'),$('otherParam1'),1)" /><br />
	<div id='otherMain'>
	<div id='otherParam1'><input name="other" type="text" /><input type="button" value="-"
		onclick="removeNode(this,'otherParam1')" /></div>
	</div></fieldset>
	<fieldset id="button"><input type="submit"
		name="_eventId_review" value="review" /> <input type="submit"
		name="_eventId_back" value="back" /> <input type="submit"
		name="_eventId_cancel" value="cancel" /></fieldset>
</form:form>
</body>