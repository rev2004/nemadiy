<%@ page language="java" contentType="text/plain" import="java.util.*,java.text.*,java.io.*,java.net.*,
com.hp.hpl.jena.rdf.model.*,
org.meandre.client.*,
org.meandre.core.repository.*,
org.meandre.core.engine.execution.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="/login.jsp"></jsp:forward>
</c:if>
<%
// Clone the flow description

String [] saFlowURIs   = request.getParameterValues("flow_uri");
String [] saECIURIs    = request.getParameterValues("flow_component_instance");
String [] saPropNames  = request.getParameterValues("property_name");
//String [] saPropValues = request.getParameterValues("property_value");
//Added by MB
String[] saPropValues=new String[saPropNames.length];
for(int i=0; i<saPropNames.length; i++){	
	saPropValues[i]=request.getParameterValues(saECIURIs[i]+saPropNames[i])[0];
}

// TODO: Check that all have the same length!!!
MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
String uri = saFlowURIs[0];
QueryableRepository rep = meandreProxy.getRepository();
QueryableRepository qr = new RepositoryImpl(rep.getAvailableFlowDescriptionsMap().get(uri).getModel());
FlowDescription fd = qr.getAvailableFlowDescriptions().iterator().next();

for ( int i=0,iMax=saFlowURIs.length ; i<iMax ; i++ ) {
	String sECID = saECIURIs[i];
	String sProp = saPropNames[i];
	String sVal  = saPropValues[i];
	
	ExecutableComponentInstanceDescription ecid = fd.getExecutableComponentInstanceDescription(sECID);
	ecid.getProperties().add(sProp,sVal);
}

// Create the model
final Model mod = fd.getModel();

// Packing required components together
for ( ExecutableComponentInstanceDescription ecid:fd.getExecutableComponentInstances() ) {
	ExecutableComponentDescription ecd = rep.getExecutableComponentDescription(ecid.getExecutableComponent());
	mod.add(ecd.getModel());
}

// Run
//AsynchCall asc = new AsynchCall(meandreProxy, mod);
//asc.run();
// Create a dedicated proxy
final MeandreProxy mpClone = new MeandreProxy(
		session.getAttribute("username").toString(),
		session.getAttribute("password").toString(),
		session.getAttribute("meandreHost").toString(),
		Integer.parseInt(session.getAttribute("meandrePort").toString()));

//String sRes = mpClone.runRepository(mod);
Thread t = new Thread(new Runnable() {
	public void run () {
	 	mpClone.runRepository(mod);
	}
});
t.start();
//<jsp:forward page="webapp-jobs.jsp"/>



//out.write(sRes);
//out.write("Hello World");

// Forward --> console page
%>
<jsp:forward page="/admin/action-change-menu.jsp?menu=Jobs"></jsp:forward>