<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobdetails.title"/></title>
    <meta name="menu" content="jobdetails"/>
</head>
<body id="jobdetails"/>

<h1>${submission.name}</h1>

<p>
${job.description}
</p>

                    <div id="formcontainer_job">
                        <div class="form_job">
                            
                                <fieldset id="pt1">
									<table id="table">
									<tr>
										<td>Job Status:</td>
										<td>
										<c:if test="${job.statusCode==-1}">
											<c:out value="Scheduled"/>
										</c:if>
										
										
										<c:if test="${job.statusCode==0}">
											<c:out value="Unknown"/>
										</c:if>
										
										
										<c:if test="${job.statusCode==1}">
											<c:out value="Submitted"/>
										</c:if>
										
										
											
										<c:if test="${job.statusCode==2}">
											<c:out value="Started"/>
										</c:if>
										
										
										
										<c:if test="${job.statusCode==3}">
											<c:out value="Ended"/>
										</c:if>
										
										<c:if test="${job.statusCode==4}">
											<c:out value="Failed"/>
										</c:if>
										
										<c:if test="${job.statusCode==5}">
											<c:out value="Aborted"/>
										</c:if>
										
										
										
										</td>
									</tr>
									<tr>
										<td>Job Type:</td>
										<td>${job.flow.name}</td>
									</tr>
									<tr>
										<td>Submit Time:</td>
										<td>${job.submitTimestamp}</td>
									</tr>
									
									<tr>
										<td>Start Time:</td>
										<td>${job.startTimestamp}</td>
									</tr>
									<tr>
										<td>End Time:</td>
										<td>${job.endTimestamp}</td>
									</tr>
									
									</table>

                                </fieldset>

								
                            </form>
                        </div>
                        
		<form id="theform" action="<c:url value='/get/JobManager.submissionAction'/>" method="post">
				<input name="id" type="hidden" value="${submission.id}"/>
                        <div class="form_job">
                            <fieldset id="pt3">
                                <input id="submitform" tabindex="6" name="submit" value="Remove This From Submission" type="submit"/>
                            </fieldset>
						
                        </div>
						
					</form>
                    </div>
</body>
