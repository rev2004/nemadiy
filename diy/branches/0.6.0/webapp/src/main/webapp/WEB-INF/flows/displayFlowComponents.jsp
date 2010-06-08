<%@ include file="/common/taglibs.jsp"%>
<head>
  <title><fmt:message key="flow.title" /></title>
  
 <script type="text/javascript">
function setIdx(idx)
{   
	$('idx').value=idx;
}
</script> 
<meta name="heading" content="Edit Task Properties: ${flow.name}" />
</head>

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

<br/>
<div id="formcontainer_job">
  <div class="form_job">
    <form:form commandName="taskFlowModel" id="myForm">
      <fieldset id="pt1">
        <label class="label">Enter the Job Name:</label>
        <form:input path="name" cssStyle="width:200px;"/>
      </fieldset>
      <fieldset id="pt1">
        <label class="label">Enter the Job Description:</label>
        <form:input path="description" cssStyle="width:300px;"/>
      </fieldset>
      <c:forEach items="${componentList}" var="component" varStatus="status">
        <c:if test="${(!component.hidden)&&(not empty datatypeMaps[component])}">
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
