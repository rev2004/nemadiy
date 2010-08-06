<%@ include file="/common/taglibs.jsp"%>
<head>
<title>
Running Time
</title>
<meta name="heading" content="Running Time"/>
</head>

<body>
<c:forEach items="${taskJobMap}" var="item">
	<label class="label">Task ${item.key.id}:${item.key.name}</label>
	<div>${item.key.description }</div>
	<table border="1" >
	<thead>
	<tr>
	<td>Submission Code</td>
	<td>Running Time</td>
	
	</tr>
	</thead>
	<c:forEach items="${item.value}" var="job">
	<tr>
	  <td>${job.flow.submissionCode}</td>
	  <td>${duration[job]}</td>
	  <td><a href="<c:url value='/get/JobManager.jobDetail?id=${job.id}'/>">detail</a></td>
	</tr>
	</c:forEach>
	<table></table>
</c:forEach>
</body>