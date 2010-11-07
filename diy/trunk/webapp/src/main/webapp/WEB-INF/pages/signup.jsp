<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="signup.title"/></title>
    <meta name="heading" content="<fmt:message key='signup.heading'/>"/>
</head>

<body id="signup"/>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
        <div class="error">
            <c:forEach var="error" items="${status.errorMessages}">
                <img src="<c:url value="/images/iconWarning.gif"/>"
                     alt="<fmt:message key="icon.warning"/>" class="icon" />
                <c:out value="${error}" escapeXml="false"/><br />
            </c:forEach>
        </div>
    </c:if>
</spring:bind>

<div class="separator"></div>

<form:form commandName="user" method="post" action="signup.html" onsubmit="return validateUser(this)" id="signupForm">
    <ul>
        <li class="info">
            <fmt:message key="signup.message"/>
        </li>
        <li>
            <appfuse:label styleClass="desc" key="user.username"/>
            <form:hidden path="username" value="${USER_OPENID_CREDENTIAL}" htmlEscape="false"/>
       </li>
 

        <li class="buttonBar bottom">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register"/>"/>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </li>
    </ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('signupForm'));
    highlightFormElements();
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>


