<%@ include file="/common/taglibs.jsp"%>

    <head>
    <title><fmt:message key="flow.type"/></title>
    <meta name="heading" content="<fmt:message key='flowtype.heading'/>"/>
</head>

<body id="jobstype"/>

<form:form commandName="taskSelection">
<form:select path="id" items="${flowSet}" itemLabel="name" itemValue="id" ></form:select>
<input type="submit" name="_eventId_show" value="GO"/>
</form:form>

                
</body>
