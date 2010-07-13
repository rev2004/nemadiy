<%@ include file="/common/taglibs.jsp"%>
<head>
<title>
<fmt:message key="joblist.title"/>
</title>
<meta http-equiv="refresh" content="10" />
<meta name="heading" content="${mirexSubmissions.title}"/>
</head>


<display:table name="submissions" cellspacing="0" cellpadding="0" requestURI="" sort="list" defaultsort="4" 
defaultorder="descending" id="submissions"  class="table" export="false" pagesize="10">
  <display:column property="name" escapeXml="true" sortable="true" titleKey="mirexSubmission.name"
        url="/get/mirex" paramId="id" paramProperty="id"/>
  <display:column property="hashcode" escapeXml="true" sortable="true" titleKey="mirexSubmission.code"
        url="/get/mirex" paramId="id" paramProperty="id" maxLength="25"/>
  <display:column property="status" escapeXml="true" sortable="true" titleKey="mirexSubmission.status"
        url="/get/mirex" paramId="id" paramProperty="id"/>
 <display:column property="createTime" sortable="true" titleKey="mirexSubmission.createTime"
 		url="/get/mirex" paramId="id" paramProperty="id"/>
 	<display:column property="updateTime" sortable="true" titleKey="mirexSubmission.updateTime"
 		url="/get/mirex" paramId="id" paramProperty="id"/>
</display:table>

