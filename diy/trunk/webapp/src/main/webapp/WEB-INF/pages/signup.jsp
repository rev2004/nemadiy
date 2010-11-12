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

    <div class="info">
        <fmt:message key="signup.message"/>
    </div>


    <div class="surround"><label class="fixed required">OpenID:</label>${USER_OPENID_CREDENTIAL}  </div>
    <div style="clear:both;"><form:hidden path="username" value="${USER_OPENID_CREDENTIAL}" htmlEscape="false"/></div>
    <br/>
    <div class="surround"><label class="fixed required">First Name:</label><form:input path="profile.firstname"/></div>
    <div class="surround"><label class="fixed required">Last Name:</label><form:input path="profile.lastname"/></div>
    <div class="surround"><label class="fixed required">Email:</label><form:input path="profile.email"/></div>
    <div class="surround"><label class="fixed required">Organization:</label><form:input path="profile.organization"/></div>
    <div class="surround"><label class="fixed ">Department:</label><form:input path="profile.department" /></div>
    <div class="surround"><label class="fixed ">Unit/Lab:</label><form:input path="profile.unit"/></div>
    <div class="surround"><label class="fixed ">Home page:</label><form:input path="profile.url" size="25" /></div>
    <div class="surround"><label class="fixed ">From(year):</label><form:input path="profile.startYear" size="5"/>
        <label class="">To:</label><form:input path="profile.endYear" size="5"/></div>
    <div class="surround"><label class="fixed ">Phone:</label><form:input path="profile.phone"/></div>
    <div class="surround"><label class="fixed ">Street Address:</label><form:input path="profile.address.streetLine1"/></div>
    <div class="surround"><label class="fixed ">Street Address 2:</label><form:input path="profile.address.streetLine2"/></div>
    <div class="surround"><label class="fixed ">Street Address 3:</label><form:input path="profile.address.streetLine3"/></div>
    <div class="surround"><label class="fixed ">City:</label><form:input path="profile.address.city"/></div>
    <div class="surround"><label class="fixed ">State, Region:</label><form:input path="profile.address.region"/></div>
    <div class="surround"><label class="fixed ">Zip (postal) code:</label><form:input path="profile.address.postcode"/></div>
    <div class="surround"><label class="fixed ">Country:</label><form:input path="profile.address.country"/></div>
    <div class="buttonBar bottom">
        <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register"/>"/>
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
    </div>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('signupForm'));
    highlightFormElements();
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>


