<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*" %>
<%
	String sUserName = request.getParameter("username");
	String sPassword = request.getParameter("password");
	String sMeandreH = request.getParameter("meandrehost");
	String sMeandreP = request.getParameter("meandreport");

	MeandreProxy meandreProxy = null;
	boolean bOK = true;
	String sErrorMsg = null;
	
	// Initialize the client
	try {
		meandreProxy = new MeandreProxy(sUserName, sPassword, sMeandreH, Integer.parseInt(sMeandreP));
		bOK = meandreProxy.getCallOk();
		if ( !bOK ) {
			sErrorMsg = "Could not login into server!!!";
		}
	}
    catch ( Exception e ) {
    	bOK = false;
    	sErrorMsg = "Permission denied!!!";
    }
	
	if ( bOK ) {
		// Set the session information
		session.setAttribute("username",sUserName);
		session.setAttribute("password",sPassword);
		session.setAttribute("meandreURL","http://"+sMeandreH+":"+sMeandreP+"/");
		session.setAttribute("meandreHost",sMeandreH);
		session.setAttribute("meandrePort",sMeandreP);
		session.setAttribute("meandreProxy",meandreProxy);
		session.setAttribute("menu","Flows");
	}
  	
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>Meandre Flow</title>
	<meta http-equiv="content-type" content="text/html; charset=utf8" />
	<meta http-equiv="content-language" content="EN" />
	<meta name="ROBOTS" content="NOINDEX,NOFOLLOW"/>
	<meta name="description" content="Meandre Flow Execution"/>
	<link rel="stylesheet" href="/themes/meandre/style.css" type="text/css" />
<% 
	if ( bOK ) {
%>
	<meta http-equiv="Refresh" content="0; URL=/" />
<%
	}
	else {
%>
	<meta http-equiv="Refresh" content="0; URL=/login-denied.jsp?errormsg=<%= sErrorMsg %>" />
<%
	}
%>
</head>
<body>
<% 
	if ( bOK ) {
%>
If your browser does not redirect to the Meandre page please click <a href="/">here</a>.
<%
	}
	else {
%>
If your browser does not redirect to the Meandre page please click <a href="/login-denied?errormsg=<%= sErrorMsg %>">here</a>.
<%
	}
%>
</body>
</html>