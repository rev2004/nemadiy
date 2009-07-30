<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,com.hp.hpl.jena.rdf.model.*,org.meandre.client.*,org.meandre.core.repository.*,org.meandre.core.engine.execution.*,org.meandre.configuration.*,org.meandre.webapp.*,org.json.JSONArray,org.json.JSONObject,org.meandre.webapp.HttpClientGet" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="/login.jsp"></jsp:forward>
</c:if>
<%
MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
String [] saFlowURIs   = request.getParameterValues("flow_uri");
//String sConsole = meandreProxy.getJobConsole(request.getParameter("uri"));
//String[] job_uri = request.getParameterValues("uri");
//String job_uri = request.getParameter("uri");
String flow_uri = saFlowURIs[0];
int base_port = 1854;
String httpresponse;
long delay = 2000;
Random r = new Random(109876L);

//final String  job_token = Integer.toString(r.nextInt());
final String  job_token = InteractiveExecution.createUniqueExecutionFlowID(flow_uri,base_port);

//final String  job_token = InteractiveExecution.createUniqueExecutionFlowID(uri,base_port);
System.out.println("\n\n\nHere is your token: " + job_token + "\n\n\n");

Thread t = new Thread(new Runnable() {
	public void run () {
	 	//mpClone.runRepository(mod);
	 	//String httprequest = "http://nema.lis.uiuc.edu:1854/services/execute/flow.txt?statistics=true&uri=http://nema.lis.uiuc.edu/jaudiofeatureextractor/&token="+job_token;
	 	//System.out.println("Here is your execute request " + httprequest);
	 	String flows = HttpClientGet.getResponse("http://nema.lis.uiuc.edu:1854/services/execute/flow.txt?statistics=true&uri=http://nema.lis.uiuc.edu/jaudiofeatureextractor/&token="+job_token);
	}
});
t.start();
System.out.println("\n\n\nSleeping for couple seconds ...");
Thread.sleep(delay);
System.out.println("\n\n\nAllirigh, I am back !!");


String url = "http://nema.lis.uiuc.edu:1854/services/execute/uri_flow.json?token="+job_token;
System.out.println("\n\n\nHere is your uri request with the token" + url);


httpresponse = HttpClientGet.getResponse(url);
//while (httpresponse.contains("<html>")){
//	System.out.println("Server JSON response failed, trying again !");
//	System.out.println("Missing token, trying again !");
//	httpresponse = HttpClientGet.getResponse(url);
//}
System.out.println("\n\n\nHere is your JSON response"+httpresponse+"\n\n\n");
JSONArray uri_flow = new JSONArray(httpresponse);
while (uri_flow.getJSONObject(0).get("token").toString().equals("MissinToken")){
	System.out.println("Missing token, trying again !");
	httpresponse = HttpClientGet.getResponse(url);
	uri_flow = new JSONArray(httpresponse);
}
	
String job_uri = uri_flow.getJSONObject(0).get("uri").toString();
System.out.println("\n\n\n Here is your job uri:"+job_uri);
//<c:set var="job_key" value="${job_uri}"/>
//<jsp:forward page="/aux2/console_interactive.jsp?uri=${job_key}"></jsp:forward>

%>
<jsp:include page="/header.jsp" flush="true" />
<jsp:include page="/webapp-navigation.jsp" flush="true" />
<div id="mainContainer">	
	<div id="main">
		<p class="information">${username} on ${meandreURL}</p>
		<br/>
		<div id="main-divs-group">
			<table>
				<thead>
					<th>Your job with uri <%=job_uri%> </th>
				</thead>
				<tbody>
					<tr>
						<td>
							<pre>						
								<a href="/aux2/console_interactive.jsp?uri=<%=job_uri%>" target="_blank">Go to Console OUTPUT</a>
								<a href="http://nema.lis.uiuc.edu/nema_results/results/<%=job_uri.substring(25,job_uri.length())%>" target="_blank">Go to Results Directory</a>
								
							</pre>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<jsp:include page="/footer.jsp" flush="true" />