<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobclassification.title"/></title>
    <meta name="menu" content="jobclassification"/>
</head>
<body id="jobclassification"/>
<p class="breadcrumbs" >
<A class="breadcrumbs" HREF="/mainMenu.html"><fmt:message key="mainMenu.title"/></A>
 > <A class="breadcrumbs" HREF="jobstype.jsp"><fmt:message key="jobstype.title"/></A>
 > <span class="breadcrumbs"><fmt:message key="jobclassification.title"/></span> 
</p>

<h1><fmt:message key="jobclassification.title"/></h1>

					<div class="actionBox">
                        <p class="actionBox">
                            Use this form to enter your information and parameters for a new Classification job. 
							You'll be able to review your job parameters on the Job Submission page.
                        </p>
                    </div>

                    <div id="formcontainer_job">
                        <div class="form_job">
                            <form id="theform" action="jobconfirm_classification.jsp" enctype="multipart/form-data" method="post">
                                <fieldset id="pt1">
                                    <label for="jobname">
                                        Job Name
                                    </label>
                                    <input id="jobname" tabindex="1" type="text">
                                </fieldset>

                                <fieldset id="pt2">
                                    <label for="jobdesc">
                                        Job
                                        Description
                                    </label>
                                    <input id="jobdesc" tabindex="2" style="height: 4em;" type="text">
                                </fieldset>
                        </div>
                        <div class="form_job">

                            <fieldset id="pt1">
                                <label for="collection">
                                    Choose
                                    Collection
                                </label>
								<SELECT NAME="collection">
									<OPTION VALUE="0" SELECTED>Select one
									<OPTION VALUE="1">collection #1
									<OPTION VALUE="2">collection #2
								</SELECT>
                            </fieldset>
                            <fieldset id="pt2">
                                <label for="featureset">
                                    Choose
                                    Feature Set
                                </label>
								<SELECT NAME="featureset">
									<OPTION VALUE="0" SELECTED>Select one
									<OPTION VALUE="1">featureset #1
									<OPTION VALUE="2">featureset #2
								</SELECT>
                            </fieldset>
                            <fieldset id="pt3">
                                <label for="groundtruth">
                                    Choose
                                    Ground Truth
                                </label>
								<SELECT NAME="groundtruth">
									<OPTION VALUE="0" SELECTED>Select one
									<OPTION VALUE="1">groundtruth #1
									<OPTION VALUE="2">groundtruth #2
								</SELECT>
                            </fieldset>
                        </div>

                        <div class="form_job">
                            <fieldset id="pt1">
                                <label for="algorithm">
                                    Choose
                                    Algorithm
                                </label>
								<SELECT NAME="algorithm">
									<OPTION VALUE="0" SELECTED>Select one
									<OPTION VALUE="1">algorithm #1
									<OPTION VALUE="2">algorithm #2
								</SELECT>
                            </fieldset>
                            <fieldset id="pt2">
                                <label for="param">

                                    Algorithm
                                    Parameter
                                </label>
								<SELECT NAME="param">
									<OPTION VALUE="0" SELECTED>Select one
									<OPTION VALUE="1">param #1
									<OPTION VALUE="2">param #2
								</SELECT>
                            </fieldset>
                            <fieldset id="pt3">
                                <label for="paramval">
                                    Enter
                                    Parameter Value
                                </label>
                                <input id="param1val" tabindex="4" type="text">
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
								<SELECT NAME="param">
									<OPTION VALUE="0" SELECTED>Select one
									<OPTION VALUE="1">param #1
									<OPTION VALUE="2">param #2
								</SELECT>
                            </fieldset>
                            <fieldset id="pt3">
                                <label for="paramval">
                                    Enter
                                    Parameter Value
                                </label>
                                <input id="param2val" tabindex="4" type="text">
                            </fieldset>

                        </div>

                        <div class="form_job">

                            <fieldset id="button">
                                <input id="submitform" tabindex="6" value="Go to Job Submission Page" type="submit">
                            </fieldset>

                        </div>
					</form>

                    </div>
</body>