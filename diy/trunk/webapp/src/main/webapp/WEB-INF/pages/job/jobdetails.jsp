<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobdetails.title"/></title>
    <meta name="menu" content="jobdetails"/>
</head>
<body id="jobdetails"/>
<p class="breadcrumbs" >
<A class="breadcrumbs" HREF="/mainMenu.html"><fmt:message key="mainMenu.title"/></A>
 > <span class="breadcrumbs"><fmt:message key="jobdetails.title"/></span>
</p>


<h1><fmt:message key="jobdetails.heading"/> for MetalDetector v3</h1>

                    <div id="formcontainer_job">
                        <div class="form_job">
                            <form id="theform" action="action.php" enctype="multipart/form-data" method="post">
                                <fieldset id="pt1">
									<TABLE id="table">
									<TR>
										<TD>Job Name:</TD>
										<TD>Name 1</TD>
									</TR>
									<TR>
										<TD>Job Status:</TD>
										<TD>running</TD>
									</TR>
									<TR>
										<TD>Job Type:</TD>
										<TD>Classification</TD>
									</TR>
									<TR>
										<TD>Start Time:</TD>
										<TD> 10:30 09-08-09</TD>
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

                                <fieldset id="pt2">
                                    <label for="jobdesc">
                                        Job
                                        Description
                                    </label>
									Some Description
                                </fieldset>
                            </form>
                        </div>
                        <div class="form_job">

                            <fieldset id="pt1">
                                <label for="collection">
                                    Choose
                                    Collection
                                </label>
									collection #1
                            </fieldset>
                            <fieldset id="pt2">
                                <label for="featureset">
                                    Choose
                                    Feature Set
                                </label>
								featureset #3
                            </fieldset>
                            <fieldset id="pt3">
                                <label for="groundtruth">
                                    Choose
                                    Ground Truth
                                </label>
								groundtruth #1
                            </fieldset>
                        </div>

                        <div class="form_job">
                            <fieldset id="pt1">
                                <label for="algorithm">
                                    Choose
                                    Algorithm
                                </label>
								algorithm #123
                            </fieldset>
                            <fieldset id="pt2">
                                <label for="param">

                                    Algorithm
                                    Parameter
                                </label>
								param #1
                            </fieldset>
                            <fieldset id="pt3">
                                <label for="paramval">
                                    Enter
                                    Parameter Value
                                </label>
								121.21
                            </fieldset>
                        </div>

                        <div class="form_job">
							<fieldset id="pt1">
							&nbsp;
							</fieldset>
							<fieldset id="pt2">
                                <label for="param2">
                                    Algorithm
                                    Parameter
                                </label>
								param #2
                            </fieldset>
                            <fieldset id="pt3">
                                <label for="paramval">
                                    Enter
                                    Parameter Value
                                </label>
								414
                            </fieldset>

                        </div>

                        <div class="form_job">
                            <fieldset id="pt1">
                                <input id="submitform" tabindex="6" value="Stop This Job" type="submit">
                            </fieldset>
                            <fieldset id="pt2">
                                <input id="submitform" tabindex="6" value="Delete This Job" type="submit" disabled>
                            </fieldset>

                            <fieldset id="pt3">
                                <input id="submitform" tabindex="6" value="View Job Results" type="submit" disabled>
                            </fieldset>
                        </div>

                    </div>
</body>