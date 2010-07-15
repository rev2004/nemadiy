<%@ include file="/common/taglibs.jsp"%>
<head>
  <title><fmt:message key="flow.title" /></title>
  
 <script type="text/javascript">
function setIdx(idx)
{   
	$('idx').value=idx;
}
</script>
<script type="text/javascript">
 	var DELINEATOR="--";
	function appendSuffix( name, suffix){
		if (name==null) {
			return DELINEATOR+suffix;
		}else{
			var position=name.lastIndexOf(DELINEATOR);
			if ((position<0)||(name.length-position>8)){
				return name+" "+DELINEATOR+suffix;
			}else{
				return name.substring(0,position)+DELINEATOR+suffix;
			}
		}
	};
	function modifyNameDesc(){
		var submissionCodeNode=dojo.byId("submissionCode");
		var suffix=submissionCodeNode.options[submissionCodeNode.selectedIndex].value;
		var nameNode=dojo.byId("name");
		nameNode.value=appendSuffix(nameNode.value,suffix);
		descNode=dojo.byId("description");
		descNode.value=appendSuffix(descNode.value,suffix);
	}
	var taskIdDesc;
	function changeTaskDesc(taskField){
		if (taskIdDesc) {taskIdDesc.hide();}
		var id=taskField.options[taskField.selectedIndex].value;
		taskIdDesc=dojo.byId("taskIdDesc"+id);
		taskIdDesc.show();
	}
</script> 
<meta name="heading" content="Edit Task Properties: ${flow.name}" />
</head>
<body onLoad="changeTaskDesc(dojo.byId('taskIdSelect'));">
<p>${flow.description}</p>

<div class="actionBox">
  Please enter the Job details, and edit the properties of the task components accordingly.
</div>
${messageContext.allMessages}
<c:if test="${not empty messageContext.allMessages}">
	<div class="message">
		<c:forEach items="${messageContext.allMessages}" var="message"><p >${message}</p></c:forEach>
	</div>
</c:if>
<c:if test="${cloned}">
    <div class="message">This is a CLONED job of ${flow.name}.</div>
</c:if>

<br/>
<div id="formcontainer_job">
  <div class="form_job">
    <form:form commandName="jobForm" id="myForm">
    
   	 <fieldset id="pt1">
        <label class="label">Mirex Submission Code:</label>
        <form:select id="submissionCode" path="mirexSubmissionCode" onchange="modifyNameDesc();" items="${mirexSubmissions}" itemLabel="hashcode" itemValue="hashcode"  cssStyle="margin-bottom:5px"/>
 
      </fieldset>
      <fieldset >
        <label class="label">Enter the Job Name:</label>
        <form:input id="name" path="name" cssStyle="width:200px;"/>
      </fieldset>
      <fieldset id="pt1">
        <label class="label">Enter the Job Description:</label><br/>
        <form:textarea rows="4" id="description" path="description" cols="70"/>
      </fieldset>
 
       <fieldset>
        <label class="label">Task Collection:</label>
        <form:select path="taskId" id="taskIdSelect" 
        	items="${taskIds}" itemLabel="name" itemValue="id" onchange="changeTaskDesc(this);"/>
        <div id="taskIdDesc"></div>
        <c:forEach items="${taskIds}" var="task"> 
			<div id="taskIdDesc${task.id}" style="display:none;">${task.description};</div>
		</c:forEach>
      </fieldset>
      <c:forEach items="${componentList}" var="component" varStatus="status">
        <c:if test="${(!component.hidden)&&(not empty componentMap[component])}">
          <fieldset id="${component.name}">
          <label class="label" style="font:italic;">Component Name</label>: <label class="name">${component.name}</label>
          <br/>
          <label class="label">Description</label>: ${component.description}          
         <div style="margin-top: 5px">  <input type="submit" name="_eventId_edit"  onclick="setIdx(${status.index})" value="Edit Properties" /></div>
          
          </fieldset>
        </c:if>
      </c:forEach>
      <input type="hidden" name="flowTemplateId" value="${flow.id}" />
      <input type="hidden" name="flowTemplateUri" value="${flow.uri}" />
      <input type="hidden" name="idx" value="0" id="idx"/>
      <fieldset id="button">
      <input type="submit" name="_eventId_review" value="Review Job and Task Settings" />
      <input type="submit" name="_eventId_cancel" value="Cancel"  style="float:right"/>
      </fieldset>
    </form:form>
  </div>
</div>
</body>