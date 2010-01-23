<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%
	String sMenu = request.getParameter("menu");

	// Check that is a valid menu entry
	if ( sMenu==null ) sMenu="Flows";
	else if ( sMenu.equals("Flows") ) sMenu="Flows";
	else if ( sMenu.equals("Jobs") )  sMenu="Jobs";
	else sMenu="Flows";
	
	session.setAttribute("menu",sMenu);
  	
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>Meandre Flow</title>
	<meta http-equiv="content-type" content="text/html; charset=utf8" />
	<meta http-equiv="content-language" content="EN" />
	<meta name="ROBOTS" content="NOINDEX,NOFOLLOW"/>
	<meta name="description" content="Meandre Flow Execution"/>
	<meta http-equiv="Refresh" content="0; URL=/" />
	<link rel="stylesheet" href="/themes/meandre/style.css" type="text/css" />
</head>
<body>
</body>
</html>