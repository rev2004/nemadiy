<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="heading" content="Mirex Submission"/>
	<script type="text/javascript">
	  function removeContributor(index){
		  node=dojo.byId("contributor"+index);
		  node.parentNode.removeChild(node);
	  };
	  
	  addContributor = function (id) {
			var x = dojo.byId("newContributor");
			x.value=id;
			myform=document.forms['myform'];
			dojo.create('input',{type:'hidden',name:'_eventId_refresh',value:'refresh'},myform);
			myform.submit();
		}

		

			  
	</script>
</head>
<body>
	<form:form modelAttribute="submission" id="myform">
	    <fieldset>
	        <div>
	        	<label class="label">Name:</label><form:input path="name"/>
	        </div>
	        <div > 
	        	<label class="label">Mirex Tasks: </label>
	        	<select name="mirexTask">
	        	 	<c:forEach items="${mirexTaskSet}" var="task">
	        	 	  <option value="${task.id}">${task.fullname}</option>
	        	 	</c:forEach> 
	        	</select>
	        </div>
	    </fieldset>
		<fieldset id="contributor">
			<c:forEach items="${submission.contributors}" var="contributor" varStatus="status">
			 	<div id="contributor${status.index}" class="surround">
				   ${status.index+1}, ${contributor.lastname},${contributor.firstname} 
			   		<img src=<c:url value="/images/delete.png"/> onclick="removeContributor(${status.index})"></img>
			   		<input type="hidden" value="${contributor.id}" name="contributor"/>
			   	</div>
			</c:forEach> 
			<input type="button" value="Add Contributor" onclick="window.open('<c:url value='/get/subpage/contributor'/>', 'searchpop', 'width=475');return false;">
			<input type="hidden" name="contributor" id="newContributor"/>		
		</fieldset>
		<fieldset >
			<label class="label">Readme:</label><br/>
			<form:textarea path="readme" rows="15" cols="60" htmlEscape="false" />
		</fieldset>
		<fieldset>
			<label class="label">Status:</label>${submission.status}
		</fieldset>
		<c:if test="${not empty submission.publicNote}">
		<fieldset >
			<label class="label">Note: </label><br/>
			<textarea readonly  rows="15" cols="60"  style="text-align:left;">
			 ${submission.publicNote}
			</textarea>
		</fieldset>
		</c:if>
		<fieldset id="button">
           
            <input type="submit" name="_eventId_save" value="Save" />
			<input type="submit" name="_eventId_clear" value="Clear" />
		</fieldset>
		
	</form:form>
</body>