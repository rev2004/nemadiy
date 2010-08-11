<%@ include file="/common/taglibs.jsp"%>
<head>
<title>
<fmt:message key="joblist.title"/>
</title>
<meta name="heading" content="Console Dump Result"/>
</head>
<body>
  	
		<c:forEach items="${dumpResult}" var="dump">
			<fieldset title="${dump.key.name}" >
			 	<legend>${dump.key.name}</legend>
					${dump.value}
			</fieldset>	  
		</c:forEach>
</body>

