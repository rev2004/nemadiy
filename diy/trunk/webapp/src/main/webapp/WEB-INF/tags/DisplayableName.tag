<%@ tag isELIgnored="false" %>
<%@ attribute name="name" required="true" type="java.util.String"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
<%=org.imirsel.nema.webapp.util.StringUtil.displayNameConvert((String)pageContext.getAttribute("name"))%>