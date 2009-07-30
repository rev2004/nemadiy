<%@ page language="java" import="java.util.*,java.text.*,java.io.*,java.net.*,org.meandre.client.*,org.meandre.core.repository.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty username}"> 
	<jsp:forward page="/login.jsp"></jsp:forward>
</c:if>
<%
// Clone the flow description
//MeandreProxy meandreProxy = (MeandreProxy)session.getAttribute("meandreProxy");
//String uri = request.getParameter("uri");
//QueryableRepository qr = new RepositoryImpl(meandreProxy.getRepository().getAvailableFlowDescriptionsMap().get(uri).getModel());
//session.setAttribute("fd",qr.getAvailableFlowDescriptions().iterator().next());

///if test = '${eci.name != "InputSelector"}'
// <input type="${dataType[key]}" name="property_value" value="${eci.properties.valueMap[key]}" size="60" />
HashMap<String,String> dataType = new HashMap<String,String>(10);
// select,check,radio,input
dataType.put("MFCC","checkbox");
pageContext.setAttribute("dataType",dataType);
%>
<jsp:include page="/header.jsp" flush="true" />
<jsp:include page="/webapp-navigation.jsp" flush="true" />
<c:set var="fd" value="${meandreProxy.repository.availableFlowDescriptionsMap[param.uri]}"/>
<div id="mainContainer">	
	<div id="main">
		<p class="information">${username} on ${meandreURL}</p>
		<br/>
		<div id="main-divs-group">
			<form method="post" action="/aux2/run-executeDebug.jsp">
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
						<td colspan="3">
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
										
										<c:set var="key" value="listFilePath"/>
										
											<tr>
												<td>
													${key} <br>
													Description
												</td>
												<td>
													<input type="hidden" name="flow_uri" value="${fd.flowComponent}" />
													<input type="hidden" name="flow_component_instance" value="${eci.executableComponentInstance}" />
													<input type="hidden" name="property_name" value="${key}" />
													<c:set var="property_value" value="${eci.executableComponentInstance}${key}" />
													
													<select name="${property_value}">
														<option value="http://nema.lis.uiuc.edu/example_wavs/mirex05FileList_short.xml">4 Files from  Mirex 2005 Audio Genre Classification Dataset</option>
														<option value="http://nema.lis.uiuc.edu/example_wavs/mirex05FileList.xml">8 Files from  Mirex 2005 Audio Genre Classification Dataset</option>
													</select>																																	
													
												</td>		
												
														
								
											</tr>											
							
											

											
											<tr>
												<td>
													Desription
												</td>
												<td>
													${ecd.properties.descriptionMap[key]}
												</td>																											
											</tr>
											
										<c:set var="key" value="individualFile"/>
										
											<tr>
												<td>
													${key}
												</td>
												<td>
													<input type="hidden" name="flow_uri" value="${fd.flowComponent}" />
													<input type="hidden" name="flow_component_instance" value="${eci.executableComponentInstance}" />
													<input type="hidden" name="property_name" value="${key}" />
													<c:set var="property_value" value="${eci.executableComponentInstance}${key}" />
													<c:choose>
														<c:when test="${not empty eci.properties.valueMap[key]}">															
															<input type="text" name="${property_value}" value="${eci.properties.valueMap[key]}" size="60" />														
														</c:when>
														<c:otherwise>
															<input type="text" name="${property_value}" value="${ecd.properties.valueMap[key]}" size="60" />
														</c:otherwise>
													</c:choose>
												</td>				
								
											</tr>
											<tr>
												<td>
													Desription
												</td>
												<td>
													${ecd.properties.descriptionMap[key]}
												</td>																											
											</tr>

									</tbody>
								</table>
																
								
							</c:if>
							</c:forEach>
							
							<c:forEach var="eci" items="${fd.executableComponentInstances}">							
							<c:if test='${eci.name == "JAudioOptionsSelector"}'> 
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


							
							
							
							
							
							
							
							

							</c:if>
							</c:forEach>
													
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="submit" value="Run it!" />
							<input type="reset" value="Clear" />
						</td>
					</tr>
				</tbody>
			</table>
			</form>
		</div>
	</div>
</div>
<jsp:include page="/footer.jsp" flush="true" />