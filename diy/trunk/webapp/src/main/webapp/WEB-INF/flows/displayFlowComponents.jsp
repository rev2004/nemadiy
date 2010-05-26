<%@ include file="/common/taglibs.jsp"%>
<head>
  <title><fmt:message key="flow.title" /></title>
  
 <script type="text/javascript">
function setIdx(idx)
{   
	$('idx').value=idx;
}
</script> 
<meta name="heading" content="${flow.name}" />
</head>

<p>${flow.description}</p>

<div class="actionBox">
  Please enter the Task details, the name and
    description and the parameters to run the flow.
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
        <label>Please enter Task Name:</label>
        <form:input path="name" />
      </fieldset>
      <fieldset id="pt1">
        <label>Please enter Task Description:</label>
        <form:textarea path="description" rows="5" cols="50" />
      </fieldset>
      <c:forEach items="${componentList}" var="component" varStatus="status">
        <c:if test="${!component.hidden}">
          <fieldset id="${component.name}">
          Component Name: ${component.name}
          <br/>
          <label>Description: ${component.description}</label>          
         <div style="margin-top: 5px">  <input type="submit" name="_eventId_edit"  onclick="setIdx(${status.index})" value="Edit Properties" /></div>
           
   
          </fieldset>
        </c:if>
      </c:forEach>
      <input type="hidden" name="flowTemplateId" value="${flow.id}" />
      <input type="hidden" name="flowTemplateUri" value="${flow.uri}" />
      <input type="hidden" name="idx" value="0" id="idx"/>
      <fieldset id="button">
      <input type="submit" name="_eventId_review" value="Review Task" />
      <input type="submit" name="_eventId_cancel" value="Cancel" />
      </fieldset>
    </form:form>
  </div>
</div>
