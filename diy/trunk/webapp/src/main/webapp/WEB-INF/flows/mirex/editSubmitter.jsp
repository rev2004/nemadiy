<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="Mirex Submission"/>
	<script type="text/javascript" src="<c:url value='/scripts/switch.js'/>" ></script>
</head>
<body>
	<form:form modelAttribute="submission" id="myform">
	    <fieldset>
	        <div>
	        	<label class="label">Name:</label>${submission.name }
	        </div>
	        <div > 
	        	<label class="label">Mirex Tasks: </label>
	        	${submission.mirexTask.name}
	        </div>
	    </fieldset>
		<fieldset id="contributor">
			<c:forEach items="${submission.contributors}" var="contributor" varStatus="status">
			 	<div id="contributor${status.index}" class="surround">
				   ${status.index+1}, ${contributor.lastname},${contributor.firstname} 
			   	</div>
			</c:forEach> 
		</fieldset>
		<fieldset >
			<label class="label">Readme:</label><br/>
			<div >${submission.readme }
			</div>
		</fieldset>
		<fieldset>
			<label class="label">Status:</label>${submission.status}
		</fieldset>
		<c:forEach items="${submission.notes}" var="note" varStatus="status">
				<fieldset>
				<div id="shortNote${status.index}">
					<div class="surround">by: ${note.author.username} 
					 ${note.createTime }
					 <img src="<c:url value='/images/plus.gif'/>" 
					 	onclick="switch('shortNote${status.index}','longNote${status.index}');"/>
					 </div>
					<div style="border-style:1;"  class="fixHeightBox">
						<c:out value="${render:shorten(note.content,30)}" escapeXml="true"/>
					</div>
				</div>
				<div id="longNote${status.index}" style="display:none;">
					<div class="surround">by: ${note.author.username} 
					 ${note.createTime }
					 <img src="<c:url value='/images/minus.gif'/>" 
					 onclick="switch('longNote${status.index}','shortNote${status.index}')"/>
					 </div>
					<div style="border-style:1;" class="fixHeightBox">
						<pre><c:out value="${note.content}" escapeXml="true" /></pre>
					</div>
				</div>
				</fieldset>

			
		</c:forEach>
		<fieldset>
			<legend>Note:</legend>
		<textarea name="mirexNote" style="width:80%;" rows="10"></textarea>
		</fieldset>
		<fieldset id="button">
            <input type="submit" name="_eventId_save" value="Save" />
		</fieldset>
		
	</form:form>
</body>