<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,
org.meandre.client.*,
org.meandre.core.repository.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="../diy/login.jsp"></jsp:forward>
</c:if>
<%
// Clone the flow description
//MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
//String uri = request.getParameter("uri");
//QueryableRepository qr = new RepositoryImpl(meandreProxy.getRepository().getAvailableFlowDescriptionsMap().get(uri).getModel());
//session.setAttribute("fd",qr.getAvailableFlowDescriptions().iterator().next());
%>
<jsp:include page="../diy/header.jsp" flush="true" />
<jsp:include page="../diy/webapp-navigation.jsp" flush="true" />
<c:set var="fd" value="${meandreProxy.repository.availableFlowDescriptionsMap[param.uri]}"/>
<div id="mainContainer">	
	<div id="main">
		<p class="information">${username} on ${meandreURL}</p>
		<br/>
		<div id="main-divs-group">
			<form method="post" action="/aux/tune_options.jsp?uri=${fd.flowComponent}">
			<table>
				<thead>
					<tr>
						<th colspan="2">${fd.name}</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Description</td>
						<td>
							${fd.description}
						</td>
					</tr>
					<tr>
						<td>Metadata</td>
						<td>
							Created by <em>${fd.creator}</em> on <em>${fd.creationDate}</em>
							under <em>${fd.rights}</em>
						</td>
					</tr>
					<tr>
						<td>Flow URI</td>
						<td>
							<code>${fd.flowComponent}</code>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<c:forEach var="eci" items="${fd.executableComponentInstances}"> 

							<c:if test='${eci.name == "InputSelector"}'> 
								<br/>
								<table>
									<thead>
										<tr>
											<th colspan="2" align="left">
												${eci.name}
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td colspan="2">
												${eci.description}
											</td>
										</tr>
										<c:set var="ecd" value="${meandreProxy.repository.availableExecutableComponentDescriptionsMap[eci.executableComponent.URI]}" />										
										<c:forEach var="key" items="${ecd.properties.keys}">
											<tr>
												<td>
													${key}
												</td>
												<td>
													<input type="hidden" name="flow_uri" value="${fd.flowComponent}" />
													<input type="hidden" name="flow_component_instance" value="${eci.executableComponentInstance}" />
													<input type="hidden" name="property_name" value="${key}" />
													<c:choose>
														<c:when test="${not empty eci.properties.valueMap[key]}">
															<input type="text" name="property_value" value="${eci.properties.valueMap[key]}" size="60" />
															<td>Description</td>

															
														</c:when>
														<c:otherwise>
															<input type="text" name="property_value" value="${ecd.properties.valueMap[key]}" size="60" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach> 
									</tbody>
								</table>
								</c:if>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="submit" value="Go to Parameter Tuning Page" />
							<input type="reset" value="Clear" />
						</td>
					</tr>
				</tbody>
			</table>
			</form>
		</div>
	</div>
</div>
<jsp:include page="../diy/footer.jsp" flush="true" />