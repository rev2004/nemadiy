<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobstype.title"/></title>
    <meta name="menu" content="jobstype"/>
</head>
<body id="jobstype"/>
<p class="breadcrumbs" >
<A class="breadcrumbs" HREF="/welcome.jsp"><fmt:message key="mainMenu.title"/></A>
 > <span class="breadcrumbs"><fmt:message key="jobstype.title"/></span>
</p>

<h1><fmt:message key="jobstype.title"/></h1>


                    <div class="actionBox">
                        <h2 class="actionBox">Classification</h2>
						<form method="post" action="<c:url value='jobclassification.jsp'/>" id="GoForm1">
						<input type="submit" name="go1" value="GO">
						</form>

                        <p class="actionBox">
                            Click "Go!" to set up
                            all of the properties and parameters for a
                            new NEMA processing job. You will be able
                            to review your selections before submitting
                            the job for processing.
                        </p>

                    </div>
                    <div class="actionBox">
                        <h2 class="actionBox">Extraction</h2>
						
						<form method="post" action="<c:url value='jobextraction.jsp'/>" id="GoForm2">
						<input type="submit" name="go2" value="GO">
						</form>

                        <p class="actionBox">
                            Click "Go!" to set up
                            all of the properties and parameters for a
                            new NEMA processing job. You will be able
                            to review your selections before submitting
                            the job for processing.
                        </p>
                    </div>
                    <div class="actionBox">

                        <h2 class="actionBox">Evaluation</h2>

						<form method="post" action="<c:url value='jobevaluation.jsp'/>" id="GoForm3">
						<input type="submit" name="go3" value="GO">
						</form>

                        <p class="actionBox">
                            Click "Go!" to set up
                            all of the properties and parameters for a
                            new NEMA processing job. You will be able
                            to review your selections before submitting
                            the job for processing.
                        </p>

                    </div>

</body>
