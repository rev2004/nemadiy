<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*" %>
<%
	String sMenu = (String)session.getAttribute("menu");
%>
<div id="navigation">
<img src="http://nema.lis.uiuc.edu/nema_logo.png" />
<a <%= sMenu.equals("Flows")?"class=\"selected\"":"" %> href="/admin/action-change-menu.jsp?menu=Flows">...flows</a>
<a <%= sMenu.equals("Jobs")?"class=\"selected\"":"" %>href="/admin/action-change-menu.jsp?menu=Jobs">...jobs</a>
<a href="/admin/action-logout.jsp" onclick="return confirm('Are you sure you want to exit')">...logout</a>
</div>