<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/>
    <meta name="menu" content="MainMenu"/>
</head>
<body id="MainMenu"/>
<h1>Prepare New Analysis Job</H1>
<form:form commandName="go" method="post" action="jobstype.jsp" onsubmit="return onFormSubmit(this)" id="GoForm">
<input type="submit" name="gobutton" value="GO">
</form:form>
<br>

<H1>Recent Job Activity:</H1>
<TABLE width="100%" class="table">
<Thead>
<TR>
	<Th>Job Name</Th>
	<Th>Job Type</Th>
	<Th>Elapsed Time</Th>
	<Th>Current Status</Th>
</TR>
</Thead>
<TR class="even">
	<TD>1</TD>
	<TD>2</TD>
	<TD>3</TD>
	<TD>4</TD>
</TR>
<TR class="odd">
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
</TR>
<TR class="even">
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
</TR>
</TABLE>
</body>