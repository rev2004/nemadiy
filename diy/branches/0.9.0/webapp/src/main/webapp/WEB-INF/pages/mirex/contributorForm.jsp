<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="contributor.add.title" /></title>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/tasks.css'/>" />
</head>
<body >
<h1 >Create New Contributor Profile</h1>
<form:form commandName="contributor">
	<div class="surround"><label class="fixed required">First Name:</label><form:input path="firstname"/></div>
	<div class="surround"><label class="fixed required">Last Name:</label><form:input path="lastname"/></div>
	<div class="surround"><label class="fixed required">Orgnization:</label><form:input path="organization"/></div>
	<div class="surround"><label class="fixed ">Department:</label><form:input path="department" /></div>
	<div class="surround"><label class="fixed ">Unit/Lab:</label><form:input path="unit"/></div>
	<div class="surround"><label class="fixed required">URL:</label><form:input path="url" size="25" /></div>
	<div class="surround"><label class="required">From(year):</label><form:input path="startYear" size="5"/>
		<label class="required">To:</label><form:input path="endYear" size="5"/></div>
	<div class="surround"><label class="fixed ">Email:</label><form:input path="email"/></div>
	<div class="surround"><label class="fixed ">Phone:</label><form:input path="phone"/></div>
	<div class="surround"><label class="fixed ">Street Address:</label><form:input path="address.streetLine1"/></div>
	<div class="surround"><label class="fixed ">Street Address 2:</label><form:input path="address.streetLine2"/></div>
	<div class="surround"><label class="fixed ">Street Address 3:</label><form:input path="address.streetLine3"/></div>
	<div class="surround"><label class="fixed ">City:</label><form:input path="address.city"/></div>
	<div class="surround"><label class="fixed ">State, Region:</label><form:input path="address.region"/></div>
	<div class="surround"><label class="fixed ">Postal Code:</label><form:input path="address.postcode"/></div>
	<div class="surround"><label class="fixed ">Country:</label><form:input path="address.country"/></div>
	<div><input type="submit" value="Add"/></div>
</form:form>
</body>

