<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Success Adding Executable Bundle</title>
<meta name="heading" content="Executable Profile Saved" />
</head>
<body>

<c:forEach items="${flowRequestContext.messageContext.allMessages}" var="message">
  <c:if test="${message.severity eq 'Info'}">
    <div class="message">
      ${message.text}
    </div>
  </c:if>
</c:forEach>

<form:form>
    
	<fieldset >
	Preferred OS: ${executableBundle.preferredOs}<br />
	Group: ${executableBundle.group }<br />
	Component: ${component.name}<br />

	</fieldset>
	<fieldset ><label class="label">
	${executableBundle.typeName}  Executable:</label> ${executableBundle.fileName} </fieldset>
	<fieldset "><label class="label">environment:</label>
		<code>
			<c:forEach items="${executableBundle.environmentVariables}" var="item">
				(<label class="label">${item.key}</label>:${item.value}); 
			</c:forEach> 
		</code>
		</fieldset>
	<fieldset ><label class="label">arguments:</label>
		<c:out	value="${executableBundle.commandLineFlags}" /></fieldset>

		<fieldset id="button">
      <input type="submit" name="_eventId_next" value="Back to Task Components" />
      <input type="submit" name="_eventId_back" value="Back to Component Properties" />
      </fieldset>
</form:form>
</body>
</html>