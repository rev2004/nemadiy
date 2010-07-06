<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="Review Mirex Submission"/>
</head>
<body>

	<form:form modelAttribute="submission" >
	    <fieldset>
	        <label class="label">Name: </label>${submission.name}
	    </fieldset>
		<fieldset>
			<label class="label">Status: </label>${submission.status}
		</fieldset>
		<fieldset >
			<label class="label">Readme: </label><br/>
			<textarea readonly  rows="15" cols="60"  style="text-align:left;">
			 ${submission.readme}
			</textarea>
		</fieldset>
	
		<fieldset id="button">
            <input type="submit" name="_eventId_review" value="Review" />
			<input type="submit" name="_eventId_edit" value="Edit" />
		</fieldset>
		
	</form:form>
</body>