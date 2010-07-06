<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="contributor.add.title" /></title>
</head>
<body >
<h1 >Create New Contributor Profile</h1>
<form:form commandName="contributor">
	<div class="surround"><label class="fixed required">First Name:</label><form:input path="firstname"/></div>
	<div class="surround"><label class="fixed required">Last Name:</label><form:input path="lastname"/></div>
	<div class="surround"><label class="fixed required">Orgnization:</label><form:input path="orgnization"/></div>
	<div class="surround"><label class="fixed ">Department:</label><form:input path="department"/></div>
	<div class="surround"><label class="fixed ">Unit/Lab:</label><form:input path="unit"/></div>
	<div class="surround"><label class="fixed required">URL:</label><form:input path="url" /></div>
	<div class="surround"><label class="fixed required">From(year):</label><form:input path="startYear"/>
		<label class="fixed required">To:</label><form:input path="startYear"/></div>
	<div class="surround"><label class="fixed ">Email:</label><form:input path="email"/></div>
	<div class="surround"><label class="fixed ">Street Address:</label><form:input path="address.steetLine1"/></div>
	<div class="surround"><label class="fixed ">Street Address 2:</label><form:input path="address.steetLine2"/></div>
	<div class="surround"><label class="fixed ">Street Address 3:</label><form:input path="address.steetLine3"/></div>
	<div class="surround"><label class="fixed ">City:</label><form:input path="address.city"/></div>
	<div class="surround"><label class="fixed ">State, Region:</label><form:input path="address.region"/></div>
	<div class="surround"><label class="fixed ">Postal Code:</label><form:input path="address.postcode"/></div>
	<div class="surround"><label class="fixed ">Country:</label><form:input path="address.country"/></div>
</form:form>
</body>

