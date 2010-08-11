<%@ include file="/common/taglibs.jsp"%>
<head>
<title>
Running Time
</title>
<meta name="heading" content="Running Time"/>
</head>

<body>
<c:forEach items="${taskJobMap}" var="item">
	<c:forEach items="${item.value}" var="flowJob">
	<label class="label">Task ${item.key.id}:${item.key.name}</label><br/>
	<label class="label">Flow ${flowJob.key.id}:${flowJob.key.name}</label>
	<div>${item.key.description }</div>
	<table border="1" >
	<thead>
	<tr>
	<td>Submission Code</td>
	<td>Running Time</td>
	
	</tr>
	</thead>
	<c:forEach items="${flowJob.value}" var="codeJob">
	<tr>
	  <td>${codeJob.value.flow.submissionCode}</td>
	  <td>${duration[codeJob.value]}</td>
	  <td><a href="<c:url value='/get/JobManager.jobDetail?id=${codeJob.value.id}'/>">detail</a></td>
	</tr>
	</c:forEach>
	</table>
	</c:forEach>
</c:forEach>
</body>