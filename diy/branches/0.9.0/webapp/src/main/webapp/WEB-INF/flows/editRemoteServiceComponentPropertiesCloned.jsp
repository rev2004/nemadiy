<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>component property editor</title>
 <meta name="heading" content="Edit Component Properties" />
</head>
<body>

<div style="margin-bottom:5px;">Component: ${component.name}</div>

<form:form enctype="multipart/form-data">
  
  <c:forEach items="${formattedProperties}" var="property">
  	<c:if test="${true}"> <%--c:if test="${not fn:startsWith(property.name,'_') }"> --%>
    <fieldset id="pt1">
    <label  class="label">  ${render:displayName(property.name)}: </label>
    <render:property roles="${userRoles}"
            component="${component.instanceUri}" value="${property}"
            class="cssClass" />
    <font color="green">${property.description}</font>
    </fieldset>
    </c:if>
  </c:forEach>
  
     <fieldset id="pt1">
     <div><label class="label">Type Name: </label>${executableMetadata.typeName}</div><br/>
     <div><label class="label">Executable Name: </label>${executableMetadata.executableName}</div><br/>
    <label  class="label"> Command Line: </label>
    <div class="surround">
	<textarea name="commandLine" rows="5" style="width:80%;" >${executableMetadata.commandLineFlags}</textarea>
	</div>
 </fieldset>
   <input type="submit" name="_eventId_save" value="Save" />
  <input type="submit" name="_eventId_cancel" value="Cancel"  style="float:right"/>
</form:form>
</body>
