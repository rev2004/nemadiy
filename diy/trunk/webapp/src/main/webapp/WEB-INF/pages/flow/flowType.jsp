<%@ include file="/common/taglibs.jsp"%>

    <head>
    <title><fmt:message key="flow.type"/></title>
    <meta name="heading" content="<fmt:message key='flowtype.heading'/>"/>
</head>

<body id="jobstype"/>
<p class="breadcrumbs" >
<A class="breadcrumbs" HREF="/mainMenu.html"><fmt:message key="mainMenu.title"/></A>
  <span class="breadcrumbs"><fmt:message key="flow.type"/></span>
</p>

<h1><fmt:message key="flow.type"/></h1>
<c:forEach items="${flowList}" var="flow"}>
                    <div class="actionBox">
                        <h2 class="actionBox"><c:out value="${flow.name}"/></h2>
						<form  action="flowtemplate.html" id="GoForm1">
						<input type="submit" name="go1" value="GO"/>
						<input type="hidden" id="id" value="${flow.id}"/>"
						</form>

                        <p class="actionBox">
                            <c:out value="{flow.description}"/>
                        </p>
                        </div>
</c:forEach>
                
</body>