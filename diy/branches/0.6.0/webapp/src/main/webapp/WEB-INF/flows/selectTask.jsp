<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="flow.type"/></title>
<meta name="heading" content="Prepare a New Job"/>
<script type="text/javascript">
 var name="";
 function change(){
	 if ((name!=null)&&(name!="")) $(name).hide();	 
	 name="flow"+$('select').value; 
	 $(name).show();
 }
</script>
</head>
<body id="jobstype" onload="change()"/>
<form:form commandName="taskFlowModel">
<c:choose>
<c:when test="${not empty flowList}">
  <h2>Select Task</h2>
  <div style="margin-bottom:5px">Please select a task to create your job for:</div>
  <form:select id="select" path="id" items="${flowList}" itemLabel="name" itemValue="id" onchange="change();" cssStyle="margin-bottom:5px"></form:select>
  <c:forEach items="${flowList}" var="flow">
  	<div style="display:none" id="flow${flow.id}">
	  <fieldset style="padding-bottom:15px">
 	  	<div><span style="font-weight:bold">Keywords:</span> ${flow.keyWords}</div>
  		<div><span style="font-weight:bold">Description:</span> ${flow.description}</div>
  	  </fieldset>
  	</div>
  </c:forEach>
  <input type="submit" name="_eventId_show" value="GO"/>
   
</c:when>
<c:otherwise><div class="error"> Sorry, no template exists!</div></c:otherwise>
</c:choose>
</form:form>
</body>
