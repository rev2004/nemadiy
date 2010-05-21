<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>component property editor</title>
</head>
<body>
Flow: ${flow.name}
<br />
Component: ${component.name}
<br />
<label>--${component.description}</label>
<form:form>
  
  <c:forEach items="${datatypeMap}" var="entry">
    <fieldset id="pt1">
    <label for="jobname"> ${entry.key} </label>
    <render:property roles="${userRoles}"
            component="${component.instanceUri}" value="${entry.value}"
            class="cssClass" />
    <font color="green">${entry.value.description}</font>
    </fieldset>
  </c:forEach>
  <input type="submit" name="_eventId_save" value="Save" />
  <input type="submit" name="_eventId_cancel" value="Cancel" />
</form:form>
</body>
