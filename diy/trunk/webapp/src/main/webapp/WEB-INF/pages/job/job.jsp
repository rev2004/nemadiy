<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="jobdetails.title" /></title>
    <meta name="menu" content="jobdetails" />

    <script type="text/javascript">

	
        new PeriodicalExecuter(updateJob,10);
	
        consoleUpdater=new PeriodicalExecuter(updateConsole,10);
        function updateConsole(){
            new Ajax.Request("<c:url value='/get/JobManager.getConsole'/>",{
                method:'get',
                parameters: {jobId:"${job.id}" },
                onSuccess: function(fillConsole){
                    var text=fillConsole.responseText;
                    var scrollPosition=$('meandreConsole').scrollTop;
                    var oldSize=$('meandreConsole').innerHTML.length;
                    if (text.length-oldSize>10){
                        $('meandreConsole').innerHTML=text;
                        $('meandreConsole').scrollTop=scrollPosition;
                    }
                }
            })//Ajax.Request
        };

        function updateJob(pe){
            new Ajax.Request("<c:url value='/get/JobManager.jobDetail.json'/>",{
            method    :'get',
                parameters: {id:"${job.id}" },
                onSuccess: function(fillJob){
               
                    var json=fillJob.responseText.evalJSON();
                    $('job.jobStatus').innerHTML=json.job.status;
                    $('job.flowname').innerHTML=json.job.flowName;
                    $('job.scheduleTimestamp').innerHTML=checkNull(json.job.scheduleTimestamp);
                    $('job.submitTimestamp').innerHTML=checkNull(json.job.submitTimestamp);
                    $('job.startTimestamp').innerHTML=checkNull(json.job.startTimestamp);
                    $('job.endTimestamp').innerHTML=checkNull(json.job.endTimestamp);
                    $('job.name').innerHTML=json.job.name;
                    if (json.job.description) {$('job.description').innerHTML=json.job.description;}
   
                    if ((json.job.status.toLowerCase()=="finished")&&($('resultContent').empty())){
                        $('result').show();
                        if (json.resultSet.root!=null){
                            var root = new Element('a', {  target:"_blank",href: json.resultSet.root.url });
                            root.innerHTML=json.resultSet.root.displayString;
                            $('resultContent').insert(root);
                            if (json.resultSet.children!=null){
                                var children=new Element('ul');
                                $('resultContent').insert({bottom:children});
                                for (i=0;i<json.resultSet.children.size();i++){
                                    child=new Element('a', {  target:"_blank",href: json.resultSet.children[i].url});
                                    child.innerHTML= json.resultSet.children[i].displayString;
                                    li=new Element('li'); li.insert(child);
                                    children.insert(li);
                                }
                            }
                        }
                    };
                    var jobDone=(json.job.status!=null)&&
                        (	(json.job.status.toLowerCase()=="finished")||
                        (json.job.status.toLowerCase()=="aborted")||
                        (json.job.status.toLowerCase()=="failed"));
                    if (jobDone) {
                        $('deleteButton').show();
                        $('downloadConsoleButton').show();
                        $('abortButton').hide();
                        if (json.job.status.toLowerCase()=="finished") {$('submitForm').show();}
                        if (pe) {
                            pe.stop();
                        }
                        consoleUpdater.stop();
                        updateConsole();
                    };
                }//onSuccess
		
            });
        }
  

        function checkNull(str){
            if (str==null){
                return "Pending...";
            }else {
                return str;
            }
        }

    </script>
</head>
<body id="jobdetails" onload="updateJob();">
    <h1 id="job.name">${job.name}</h1>
    <div id="job.description" class="surround">${job.description}</div>
    <c:if test="${!(empty jobForSubmission)}">
        <div class="actionBox">
            <p class="actionBox"><c:url value="/get/JobManager.getSubmissions" var="submissionUrl"></c:url> <c:choose>
                    <c:when test="${jobForSubmission}">
                        <fmt:message key="job.forSubmission.success" />
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="job.forSubmission.fail" />
                    </c:otherwise>

                </c:choose> Check out <a href="${submissionUrl}">all current submission</a></p>
        </div>
    </c:if>

    <div id="formcontainer_job">
        <div class="form_job">
            <fieldset id="pt1">



                <table id="table" class="myleft">
                    <tr>
                        <td><label class="label">Job Status</label></td><td>:</td>
                        <td id="job.jobStatus">${job.jobStatus}</td>
                    </tr>
                    <tr>
                        <td><label class="label">Job Type</label></td><td>:</td>
                        <td id="job.flowname">${job.flow.name}</td>
                    </tr>
                    <tr>
                        <td><label class="label">Schedule Time</label></td><td>:</td>
                        <td id="job.scheduleTimestamp">${job.scheduleTimestamp}</td>
                    </tr>
                    <tr>
                        <td><label class="label">Submit to Meandre Server</label></td><td>:</td>
                        <td id="job.submitTimestamp">${job.submitTimestamp}</td>
                    </tr>
                    <tr>
                        <td><label class="label">Start Time</label></td><td>:</td>
                        <td id="job.startTimestamp">${job.startTimestamp}</td>
                    </tr>
                    <tr>
                        <td><label class="label">Finish Time</label></td><td>:</td>
                        <td id="job.endTimestamp">${job.endTimestamp}</td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <a target="_blank" href="http://${head.host}:${head.port}/services/jobs/job_console.html?uri=${job.executionInstanceId}">Meandre Console</a>
                        </td>
                    </tr>
                </table>




                <div id="result" style="display:none; margin-top:5em;margin-bottom:auto;text-align:center;" >
					Explore Results 
                    <div id="resultContent" ></div>
                </div>

            </fieldset>
        </div>

        <fieldset >
            <form id="submitForm"  style="display:none;" action="<c:url value='/get/JobManager.selectJobForSubmission'/>" method="post" class="myleft "><input
                    name="jobId" type="hidden" value="${job.id}" />
                <input  name="submit" tabindex="6" value="Select As Submission"	type="submit" />

            </form>

            <form id="theform" action="<c:url value='/get/JobManager.jobAction'/>" method="post">
                <input name="id" type="hidden" value="${job.id}" />
                <input id="downloadConsoleButton" style="display: none;" type="button"
                       value="Download Log"  onclick="window.location.assign('<c:url value='/get/JobManager.getConsole?jobId=${job.id}'/>')"/>
                <input id="abortButton" name="submit" tabindex="6" value="Abort This Job" type="submit" />
                <input type="button" value="Clone"  onclick="window.location.assign('<c:url value='/get/task?flowId=${job.flow.id}&cloned=true'/>')"/>
                <input  id="deleteButton" style="display:none;" tabindex="6" name="submit" value="Delete This Job" type="submit" class="myright"/>
            </form>
        </fieldset>
        <form id="theform" action="<c:url value='/get/JobManager.jobAction'/>" method="post"><input name="id"
                                                                                                    type="hidden" value="${job.id}" />
            <div class="form_job"><c:if test="${job.statusCode==2}">
                    <fieldset id="pt1"><input id="submitform" name="submit" tabindex="6" value="Abort This Job" type="submit" />
                    </fieldset>
                </c:if>
                <fieldset id="pt3"><input id="submitform" tabindex="6" name="submit" value="Delete This Job" type="submit"
                                          enabled /></fieldset>
            </div>
        </form>
        <div class="fixHeightBox" style="max-height:300px;height:300px;" >
            <pre id="meandreConsole">(getting console...)
            </pre>
        </div>
    </div>
</body>

