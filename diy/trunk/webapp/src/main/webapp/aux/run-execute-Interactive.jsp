<%@ page language="java" contentType="text/plain" import="java.util.*,java.text.*,java.io.*,java.net.*,
com.hp.hpl.jena.rdf.model.*,
org.meandre.client.*,
org.meandre.core.repository.*,
org.meandre.core.engine.execution.*,
org.meandre.configuration.*,
org.meandre.webapp.*,
org.json.*,
org.meandre.webapp.HttpClientGet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="/login.jsp"></jsp:forward>
</c:if>
<%
MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
//String sConsole = meandreProxy.getJobConsole(request.getParameter("uri"));
//String[] job_uri = request.getParameterValues("uri");
//String job_uri = request.getParameter("uri");

%>
<jsp:include page="../diy/header.jsp" flush="true" />
<jsp:include page="../diy/webapp-navigation.jsp" flush="true" />
<div id="mainContainer">	
	<div id="main">
		<p class="information">${username} on ${meandreURL}</p>
		<br/>
		<div id="main-divs-group">
			<table>
				<thead>
					<th>This is the interactive output console for ></th>
				</thead>
				<tbody>
					<tr>
						<td>
							<pre>
								<% out.write("Hello World"); %>
							</pre>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<jsp:include page="../diy/footer.jsp" flush="true" />

<%
String httpresponse;
Random r = new Random(109876L);

final String  job_token = Integer.toString(r.nextInt());
//final String  job_token = InteractiveExecution.createUniqueExecutionFlowID(uri,base_port);
System.out.println("\n\n\nHere is your token: " + job_token + "\n\n\n");

Thread t = new Thread(new Runnable() {
	public void run () {
	 	//mpClone.runRepository(mod);
	 	String flows = HttpClientGet.getResponse("http://nema.lis.uiuc.edu:1854/services/execute/flow.txt?statistics=true&uri=http://nema.lis.uiuc.edu/testconsoleout/&token="+job_token);
	}
});
t.start();
String url = "http://nema.lis.uiuc.edu:1854/services/execute/uri_flow.json?token="+job_token;
httpresponse = HttpClientGet.getResponse(url);
System.out.println("\n\n\nHere is your JSON response"+httpresponse+"\n\n\n");
JSONArray uri_flow = new JSONArray(httpresponse);
//out.write("\n\n\nURI_FLOW: " +uri_flow.getJSONObject(0).get("uri"));
final String job_uri = uri_flow.getJSONObject(0).get("uri").toString();
System.out.println("\n\n\n Here is your job uri:"+job_uri);
//http://nema.lis.uiuc.edu/testconsoleout/80AE9A4A73E/1245036742443/871921227/
//<c:set var="job_key" value="${job_uri}"/>
//<jsp:forward page="/aux/console_interactive.jsp?uri=${job_key}"></jsp:forward>

%>

