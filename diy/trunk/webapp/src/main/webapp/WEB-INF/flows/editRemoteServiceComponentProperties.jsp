<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>component property editor</title>
 <meta name="heading" content="Edit Component Properties" />
</head>
<body>

<div style="margin-bottom:5px;">Component: ${component.name}</div>

<form:form>
  
  <c:forEach items="${shownMap}" var="entry">
    <fieldset id="pt1">
    <label for="jobname"> ${entry.key} </label>
    <render:property roles="${userRoles}"
            component="${component.instanceUri}" value="${entry.value}"
            class="cssClass" />
    <font color="green">${entry.value.description}</font>
    </fieldset>
  </c:forEach>
  
     <fieldset id="pt1">
    <label for="jobname"> Profile: </label>
     <c:choose >
      <c:when test="${taskFlowModel.executableMap[component]==null}">
       None  <input type="submit" name="_eventId_next" value="Create" />
      </c:when>
      <c:otherwise>
        ${taskFlowModel.executableMap[component].path}  <input type="submit" name="_eventId_next" value="Edit" />
       </c:otherwise>
    </c:choose>
 </fieldset>
  
  
  <input type="submit" name="_eventId_cancel" value="Cancel" />
</form:form>
</body>
