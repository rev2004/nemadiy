<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,
org.meandre.client.*,
org.json.*,
org.json.JSONObject,
org.meandre.webapp.HttpClientGet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="../diy/login.jsp"></jsp:forward>
</c:if>
<%
int i=0;
String status;
MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
String sConsole = meandreProxy.getJobConsole(request.getParameter("uri"));
String job_uri = request.getParameter("uri");
//System.out.println("Job Status: "+meandreProxy.getJobStatuses().indexOf(job_uri));

String httpresponse = HttpClientGet.getResponse("http://nema.lis.uiuc.edu:1854/services/jobs/list_jobs_statuses.json");
JSONArray job_statuses = new JSONArray(httpresponse);
while(!job_uri.equals(job_statuses.getJSONObject(i).get("job_id").toString())){
	i++;
}
	
System.out.println("\n\n\nThe status of job is " + job_statuses.getJSONObject(i).get("status").toString()+"\n\n\n");
status = job_statuses.getJSONObject(i).get("status").toString();

if(status.equals("R")){
	response.setHeader("Refresh", "1");
}


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
					<th>Output console for job_id <%=job_uri %></th>
				</thead>
				<tbody>
					<tr>
						<td>
							<pre>
								<% out.write("\n"+sConsole); %>								
							</pre>
						</td>
					</tr>
				</tbody>
			


				<tbody>
					<tr>
						<td>
							<pre>			
								<%if(status.equals("C")){
									 out.write("Your job finished Succesfully.");
								}%>						
							</pre>
						</td>
					</tr>
				</tbody>
											
								
			</table>
		</div>
	</div>
</div>
<jsp:include page="../diy/footer.jsp" flush="true" />

