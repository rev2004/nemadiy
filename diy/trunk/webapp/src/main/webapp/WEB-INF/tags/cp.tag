<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://www.springmodules.org/tags/commons-validator" prefix="v" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ attribute name="component" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="true" type="org.imirsel.nema.model.Property" %>
<%@ tag dynamic-attributes="map"%>

<spring:bind path="value">
  <font color="red">
    <b><c:out value="${status.errorMessage}"/></b>
  </font>

</spring:bind>


<td>${component}</td>
<form:input path="${status.defaultValue}"/>