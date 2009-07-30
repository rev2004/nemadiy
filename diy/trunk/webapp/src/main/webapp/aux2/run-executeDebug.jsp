<%@ page language="java" contentType="text/plain" import="java.util.*,java.text.*,java.io.*,java.net.*,
com.hp.hpl.jena.rdf.model.*,
org.meandre.client.*,
org.meandre.core.repository.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="/login.jsp"></jsp:forward>
</c:if>
<%
// Clone the flow description

String [] saFlowURIs   = request.getParameterValues("flow_uri");
String [] saECIURIs    = request.getParameterValues("flow_component_instance");
String [] saPropNames  = request.getParameterValues("property_name");

System.out.println("length ECID " + saECIURIs.length);
System.out.println("length PropNam " + saPropNames.length);



String[] saPropValues=new String[saPropNames.length];
for(int i=0; i<saPropNames.length; i++){	
	saPropValues[i]=request.getParameterValues(saECIURIs[i]+saPropNames[i])[0];
}



System.out.println("length PropVal " + saPropValues.length);

// TODO: Check that all have the same length!!!


MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
String uri = saFlowURIs[0];
QueryableRepository rep = meandreProxy.getRepository();
QueryableRepository qr = new RepositoryImpl(rep.getAvailableFlowDescriptionsMap().get(uri).getModel());
FlowDescription fd = qr.getAvailableFlowDescriptions().iterator().next();

System.out.println("length ECID " + saECIURIs.length);
System.out.println("length PropNam " + saPropNames.length);
System.out.println("length PropVal " + saPropValues.length);


for ( int i=0,iMax=saPropValues.length ; i<iMax ; i++ ) {
	String sECID = saECIURIs[i];
	String sProp = saPropNames[i];
	
	String sVal  = saPropValues[i];
	out.println("no: "+ i  + " Prop Name\t" + sProp + "\tValue:\t\t"+ sVal);
	//out.println("no: "+i+ "\t\""+sProp+"\"");
	//System.out.println("Prop val:\t\t" + sVal);
}


//<jsp:forward page="webapp-jobs.jsp"/>




//out.write(sRes);
//out.write("Hello World");

// Forward --> console page <jsp:forward page="/admin/action-change-menu.jsp?menu=Jobs"></jsp:forward>
%>
