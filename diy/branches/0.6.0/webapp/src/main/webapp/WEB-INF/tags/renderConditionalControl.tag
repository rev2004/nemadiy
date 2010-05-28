<%@ tag body-content="tagdependent" isELIgnored="false" %>
<%@ attribute name="readOnly" required="true" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="className" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://www.springmodules.org/tags/commons-validator" prefix="v" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<c:if test="${empty type}">
<c:set var="type" value="text" scope="page" />
</c:if>

<spring:bind path="${path}">
	
    <c:choose>
        <c:when test="${readOnly}">
            <span class="readOnly">${status.value}</span>
        </c:when>
        <c:otherwise>
           <input type="${type}" id="${status.expression}" name="${status.expression}"
                    value="${status.value}" class="${className}" />
        </c:otherwise>
    </c:choose>
</spring:bind>
