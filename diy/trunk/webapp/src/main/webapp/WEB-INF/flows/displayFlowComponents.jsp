<%@ include file="/common/taglibs.jsp"%>
<head>
  <title><fmt:message key="flow.title" /></title>
  <meta name="heading" content="${flow.name}" />
</head>

<p>${flow.description}</p>

<div class="actionBox">
  <p class="actionBox">Please enter the Task details, the name and
    description and the parameters to run the flow.</p>
</div>

<br />
<br />

<div id="formcontainer_job">
  <div class="form_job">
    <form:form commandName="taskFlowModel">
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
          <c:out value="${component.name}" />
          <br/>
          <label>${component.description}</label>
          <a href="${flowExecutionUrl}&idx=${status.index}&_eventId=edit">edit</a>
          </fieldset>
        </c:if>
      </c:forEach>
      <input type="hidden" name="flowTemplateId" value="${flow.id}" />
      <input type="hidden" name="flowTemplateUri" value="${flow.uri}" />
      <fieldset id="button">
      <input type="submit" name="_eventId_test" value="review" />
      <input type="submit" name="_eventId_cancel" value="cancel" />
      <input type="submit" name="_eventId_clear" value="clear" />
      </fieldset>
    </form:form>
  </div>
</div>
