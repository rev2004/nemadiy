<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="experimentMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='experimentMenu.heading'/>"/>
    <meta name="menu" content="ExperimentMenu"/>
    
</head>

<p><fmt:message key="experimentMenu.message"/></p>

<div class="separator"></div>

<ul class="glassList">
    <li>
        <a href="<c:url value='/featureExtraction.html'/>"> <fmt:message key="experimentMenu.featureExtractor"/>        
    </li>
    <li>
         <a href="<c:url value='/classification.html'/>"> <fmt:message key="experimentMenu.classifier"/>       
    </li>
    <li>
         <a href="<c:url value='/evaluation.html'/>"> <fmt:message key="experimentMenu.evaluation"/>       
    </li>
    
    
    
</ul>
