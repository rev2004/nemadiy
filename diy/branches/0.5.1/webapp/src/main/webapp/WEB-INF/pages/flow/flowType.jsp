<%@ include file="/common/taglibs.jsp"%>

    <head>
    <title><fmt:message key="flow.type"/></title>
    <meta name="heading" content="<fmt:message key='flowtype.heading'/>"/>
</head>

<body id="jobstype"/>

<p>
The flows of type ${flowType} available to execute are:
</p>

<c:forEach items="${flowList}" var="flow">
                    <div class="actionBox">
                        <h2 class="actionBox"><c:out value="${flow.name}"/></h2>
						<form  action="<c:url value='/get/FlowFormManager.getFlowTemplate'/>" id="GoForm1">
						<input type="submit" name="go1" value="GO"/>
						<input type="hidden" id="id" name="id" value="${flow.id}"/>
						</form>

                       <br/>
                       	<i>Keywords:</i> <c:out value="${flow.keyWords}"/><br/><br/>
                       <i>Description:</i> <c:out value="${flow.description}"/>
                        </div>
</c:forEach>
                
</body>
