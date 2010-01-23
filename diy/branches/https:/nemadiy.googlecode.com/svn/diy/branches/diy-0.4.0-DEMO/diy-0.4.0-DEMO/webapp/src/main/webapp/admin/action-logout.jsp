<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*" %>
<%
	String sForward="/";
	session.invalidate();
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>Meandre Flow</title>
	<meta http-equiv="content-type" content="text/html; charset=utf8" />
	<meta http-equiv="content-language" content="EN" />
	<meta name="ROBOTS" content="NOINDEX,NOFOLLOW"/>
	<meta name="description" content="Meandre Flow Execution"/>
	<meta http-equiv="Refresh" content="0; URL=<%= sForward %>" />
	<link rel="stylesheet" href="/themes/meandre/style.css" type="text/css" />
</head>
<body>
If your browser does not redirect to the Meandre page please click <a href="<%= sForward %>">here</a>.
</body>
</html>