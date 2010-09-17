<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="Review Mirex Submission"/>
</head>
<body>

	<form:form  >
	    <fieldset>
	        <label class="label">Name: </label>${submission.name}
	    </fieldset>
		<fieldset>
			<label class="label">Status: </label>${submission.status}
		</fieldset>
		<fieldset >
			<label class="label">Readme: </label><br/>
			<div class="surround">
				<textarea readonly  rows="15" style="text-align:left;width:80%;">
			 		${submission.readme}
				</textarea>
			</div>
		</fieldset>
	
		<fieldset id="button">
            <input type="submit" name="_eventId_save" value="Save" />
			<input type="submit" name="_eventId_edit" value="Edit" />
		</fieldset>
		
	</form:form>
</body>