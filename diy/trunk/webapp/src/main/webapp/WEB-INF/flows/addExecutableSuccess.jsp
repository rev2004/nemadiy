<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Success Adding Executable Bundle</title>
<meta name="heading" content="${taskFlowModel.name}.${component.name}" />
</head>
<body>
<div class="message">
<h2>You have already uploaded the executable bundle to the content repository.</h2>
</div>

<c:forEach items="${flowRequestContext.messageContext.allMessages}" var="message">
  <c:if test="${message.severity eq 'Error'}">
    <div class="message">
      <div class="portlet-msg-alert">${message.text}</div>
    </div>
  </c:if>
</c:forEach>

<form:form>
    
	<fieldset id="pt1">
	Preferred OS: ${executableBundle.preferredOs}<br />
	Group: ${executableBundle.group }<br />
	Component: ${component.name}<br />

	</fieldset>
	<fieldset id="pt1"><label>
	${executableBundle.typeName}  Executable:</label> ${executableBundle.fileName} </fieldset>
	<fieldset id="pt1"><label>environment:</label><code><c:forEach items="${executableBundle.environmentVariables}" var="item">(${item.key}:${item.value}); </c:forEach> </code></fieldset>
	<fieldset id="pt1"><label>arguments:</label><c:out
		value="${executableBundle.commandLineFlags}" /></fieldset>

		<fieldset id="button">
      <input type="submit" name="_eventId_next" value="Next" />
      <input type="submit" name="_eventId_back" value="Revise" />
      </fieldset>
</form:form>
</body>
</html>