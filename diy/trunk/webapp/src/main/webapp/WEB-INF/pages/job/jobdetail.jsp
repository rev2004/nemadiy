<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobdetails.title"/></title>
    <meta name="menu" content="jobdetails"/>
</head>
<body id="jobdetails"/>

<h1><fmt:message key="${job.name}"/></h1>
<p>
${job.description}
</p>

                    <div id="formcontainer_job">
                        <div class="form_job">
                            <form id="theform" action="jobaction.html" enctype="multipart/form-data" method="post">
                                <fieldset id="pt1">
									<TABLE id="table">
									<TR>
										<TD>Job Name:</TD>
										<TD>${job.name}</TD>
									</TR>
									<TR>
										<TD>Job Status:</TD>
										<TD>${job.statusCode}</TD>
									</TR>
									<TR>
										<TD>Job Type:</TD>
										<TD>${job.flow.name}</TD>
									</TR>
									<TR>
										<TD>Start Time:</TD>
										<TD>${job.submitTimestamp}</TD>
									</TR>
									<TR>
										<TD>End Time:</TD>
										<TD> 10:30 09-08-09</TD>
									</TR>
									<TR>
										<TD>Elapsed Time:</TD>
										<TD> 10:30 09-08-09</TD>
									</TR>
									</TABLE>

                                </fieldset>

								<input name="id" type="hidden" value="${job.id}"/>
                            </form>
                        </div>
                       

                        <div class="form_job">
                            <fieldset id="pt1">
                                <input id="submitform" name="submit" tabindex="6" value="Stop This Job" type="submit">
                            </fieldset>
                            <fieldset id="pt2">
                                <input id="submitform" tabindex="6" name="submit" value="Delete This Job" type="submit" disabled>
                            </fieldset>

                            <fieldset id="pt3">
                                <input id="submitform" tabindex="6" name="submit" value="View Job Results" type="submit" disabled>
                            </fieldset>
                        </div>

                    </div>
</body>