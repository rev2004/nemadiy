<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="Mirex Submission"/>
</head>
<body>
	<form:form modelAttribute="submission" >
	    <fieldset>
	        <label class="label">Name:</label><form:input path="name"/>
	    </fieldset>
		<fieldset>
			<label class="label">Status:</label>
			<form:select path="status" items="${submissionStatusSet}" />
		</fieldset>
		<fieldset >
			<label class="label">Readme:</label><br/>
			<form:textarea path="readme" rows="15" cols="60" htmlEscape="false" />
		</fieldset>
		<fieldset id="button">
            <input type="submit" name="_eventId_review" value="Review" />
			<input type="submit" name="_eventId_clear" value="Clear" />
		</fieldset>
		
	</form:form>
</body>