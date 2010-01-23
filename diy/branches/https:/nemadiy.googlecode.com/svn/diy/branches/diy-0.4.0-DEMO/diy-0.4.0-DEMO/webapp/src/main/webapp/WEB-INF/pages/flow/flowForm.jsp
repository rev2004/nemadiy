<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="flow.title"/></title>
    <meta name="heading" content="${flow.name}"/>
</head>

<p>
${flow.description}
</p>


					<div class="actionBox">
                        <p class="actionBox">
                      	Please enter the Task details the name, the description and the collection information
                      	to run the flow 
                         </p>
                    </div>

                    <div id="formcontainer_job">
                        <div class="form_job">
                            <form id="theform" action="<c:url value='/runjob.html'/>" enctype="multipart/form-data" method="post">
                                <fieldset id="pt1">
                                    <label for="jobname">
                                        Job name
                                    </label>
                                    <input id="name" name="name" tabindex="1" type="text">
                                </fieldset>

                                <fieldset id="pt2">
                                    <label for="jobdesc">
                                        Job Description
                                    </label>
                                    <input id="description" name="description" tabindex="2" style="height: 4em;" type="text">
                                </fieldset>
                            <fieldset id="pt1">
                                <label for="collection">
                                    Choose Collection
                                    
                                </label>
								<select name="collection">
									<option value="0" selected>select one
									<option value="1">collection #1
									<option value="2">collection #2
								</select>
                            </fieldset>
                            <fieldset id="pt2">
                                <label for="featureset">
                                    Choose Feature Set
                                </label>
								<select name="featureset">
									<option value="0" selected>select one
									<option value="1">Featureset #1
									<option value="2">Featureset #2
								</select>
                            </fieldset>
                       <c:if test="${flow.id ==4}">
                            <fieldset id="pt3">
                                <label for="groundtruth">
                                    Choose Ground Truth
                                    
                                </label>
								<select name="groundtruth">
									<option value="0" selected>select one
									<option value="1">Groundtruth #1
									<option value="2">Groundtruth #2
								</select>
                            </fieldset>
                       </c:if>
                       
                       <c:if test="${flow.id ==3}">
                            <fieldset id="pt3">
                                <label for="trainingdata">
                                    Choose Training Data
                                    
                                </label>
								<select name="trainingdata">
									<option value="0" selected>select one
									<option value="1">Training Data #1
									<option value="2">Training Data #2
								</select>
                            </fieldset>
                       </c:if>
                       
                       	<input type="hidden" name="flowId" value="${flow.id}"/>
                       
                            <fieldset id="button">
                                <input id="submitform" tabindex="6" value="Go to Job Submission Page" type="submit">
                            </fieldset>

                        </div>
					</form>

                    </div>
