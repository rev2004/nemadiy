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
			<label class="label">Status:</label>
			<form:select path="status" />
		</fieldset>
		
		<fieldset id="button">
            <input type="submit" name="_eventId_save" value="Save" />
		</fieldset>
		
	</form:form>
</body>