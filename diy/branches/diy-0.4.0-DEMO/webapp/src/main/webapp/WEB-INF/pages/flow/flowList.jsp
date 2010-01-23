<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="flowList.title"/></title>
    <meta name="heading" content="<fmt:message key='flowList.heading'/>"/>
</head>
<p>
Click on the flow that you want to execute.
</p>
<display:table name="flowList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="flows" pagesize="25" class="table" export="false">
    <display:column property="name" escapeXml="true" sortable="true" titleKey="flow.name" style="width: 25%"
        url="<c:url value='/get/FlowFormManager.getFlowTemplate?from=list'/>" paramId="id" paramProperty="id"/>
    <display:column property="description" escapeXml="true" sortable="true" titleKey="flow.description" style="width: 75%"
        url="<c:url value='/get/FlowFormManager.getFlowTemplate?from=list'/>" paramId="id" paramProperty="id"/>
 </display:table>


<script type="text/javascript">
    highlightTableRows("flows");
</script>
