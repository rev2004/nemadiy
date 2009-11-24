<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobconfirm_class.title"/></title>
    <meta name="menu" content="jobconfirm_class"/>
</head>
<body id="jobconfirm_class"/>
<p class="breadcrumbs" >
<A class="breadcrumbs" HREF="/mainMenu.html"><fmt:message key="mainMenu.title"/></A>
 > <A class="breadcrumbs" HREF="jobstype.jsp"><fmt:message key="jobstype.title"/></A>
 > <A class="breadcrumbs" HREF="jobclassification.jsp"><fmt:message key="jobclassification.title"/></A>
 > <span class="breadcrumbs"><fmt:message key="jobconfirm_class.title"/></span> 
</p>

<h1><fmt:message key="jobconfirm_class.title"/></h1>

                    <div id="formcontainer_job">
                        <div class="form_job">
                            <form id="theform" action="action.php" enctype="multipart/form-data" method="post">
                                <fieldset id="pt1">
								   <label for="jobname">
									Job Name</label>
										Name 1
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

<form id="theform" action="jobclassification.jsp" enctype="multipart/form-data" method="post">				
                        <div class="form_job">
                            <fieldset id="pt1">
                                <input id="submitform" tabindex="1" value="Go back to Edit Properties" type="submit">
                            </fieldset>
</form>
                            <fieldset id="pt2">
							&nbsp;
							</fieldset>
<form id="theform" action="" enctype="multipart/form-data" method="post">			
                            <fieldset id="pt3">
                                <input id="submitform" tabindex="2" value="Submit Job" type="submit" >
                            </fieldset>
</form>
                        </div>

                    </div>

</body>