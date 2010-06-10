<%@ include file="/common/taglibs.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>component property editor</title>
 <meta name="heading" content="Edit Component Properties" />
</head>
<body>

<div style="margin-bottom:5px;">Component: ${component.name}</div>

<form:form enctype="multipart/form-data">
  
  <c:forEach items="${formattedProperties}" var="entry">
  	<c:if test="${not fn:startsWith(entry.key,'_') }">
    <fieldset id="pt1">
    <label for="jobname" class="label"> <render:displayName>${entry.value.name}:</render:displayName>  </label>
    <render:property roles="${userRoles}"
            component="${component.instanceUri}" value="${entry.value}"
            class="cssClass" />
    <font color="green">${entry.value.description}</font>
    </fieldset>
    </c:if>
  </c:forEach>
  
     <fieldset id="pt1">
    <label for="jobname" class="label"> Executable Profile: </label>
     <c:choose >
      <c:when test="${taskFlowModel.executableMap[component]==null}">
       None  <input type="submit" name="_eventId_next" value="Create" />
      </c:when>
      <c:otherwise>
        ${taskFlowModel.executableMap[component].path} 
        <div style="margin-top: 5px">
         <input type="submit" name="_eventId_next" value="Edit" />
          <input type="submit" name="_eventId_remove" value="Remove" />
          </div>
       </c:otherwise>
    </c:choose>
 </fieldset>
   <input type="submit" name="_eventId_save" value="Save" />
  <input type="submit" name="_eventId_cancel" value="Cancel"  style="float:right"/>
</form:form>
</body>
