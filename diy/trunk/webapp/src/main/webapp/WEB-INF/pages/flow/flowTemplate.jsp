<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="flow.title" /></title>
<meta name="heading" content="${flow.name}" />
</head>

<p>${flow.description}</p>


<div class="actionBox">
<p class="actionBox">Please enter the Job details the name and
description and job parameters to run the flow.</p>
</div>
<br />
<br />

<div id="formcontainer_job">
<div class="form_job">

<form id="theform" action="/get/FlowFormManager.saveFlow"
	enctype="multipart/form-data" method="post">

<fieldset id="pt1"><label>Name</label> <input name="name"
	type="text" value="${flow.name}" /></fieldset>


<fieldset id="pt1"><label>Description</label> <textarea
	name="description" rows="5" cols="50">${flow.description}</textarea></fieldset>




<c:forEach items="${componentPropertyMap}" var="entry">

	<c:if test="${entry.key.hidden==false}">

		<h3>${entry.key.name}</h3>
		<br />

		<c:forEach items="${entry.value}" var="value_entry">

			<fieldset id="pt1"><label for="jobname">
			${value_entry.key} </label> <render:componentproperty roles="${userRoles}"
				component="${entry.key.instanceUri}" value="${value_entry.value}"
				class="cssClass" /> <font color="green">${value_entry.value.description}</font>


			</fieldset>
		</c:forEach>

		<input type="hidden" name="modifiedComponents"
			value="${entry.key.instanceUri}" />
	</c:if>

</c:forEach> <input type="hidden" name="flowTemplateId" value="${flow.id}" /> <input
	type="hidden" name="flowTemplateUri" value="${flow.url}" />
<fieldset id="button"><input id="submitform" tabindex="6"
	value="Run Job" type="submit"></fieldset>
</div>
</form>

</div>