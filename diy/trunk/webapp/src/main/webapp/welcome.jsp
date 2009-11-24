<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="welcomeMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='welcomeMenu.heading'/>"/>
    <meta name="menu" content="welcomeMenu"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
</head>
<body id="welcomeMenu"/>

<c:choose>
	<c:when test="${empty pageContext.request.remoteUser}">

		<div style="margin: 15px 5px 5px; z-index: 1000; float: right;">
		
		<form method="post" id="loginForm" action="<c:url value='/j_security_check'/>"
			onsubmit="saveUsername(this);return validateForm(this)">
		<fieldset style="padding-bottom: 0">
		<ul>
		<c:if test="${param.error != null}">
			<li class="error">
				<img src="${ctx}/images/iconWarning.gif" alt="<fmt:message key='icon.warning'/>" class="icon"/>
				<fmt:message key="errors.password.mismatch"/>
				<%--${sessionScope.SPRING_SECURITY_LAST_EXCEPTION_KEY.message}--%>
			</li>
		</c:if>
			<li>
			   <label for="j_username" class="required desc">
					<fmt:message key="label.username"/> <span class="req">*</span>
				</label>
				<input type="text" class="text medium" name="j_username" id="j_username" tabindex="1" />
			</li>

			<li>
				<label for="j_password" class="required desc">
					<fmt:message key="label.password"/> <span class="req">*</span>
				</label>
				<input type="password" class="text medium" name="j_password" id="j_password" tabindex="2" />
			</li>

		<c:if test="${appConfig['rememberMeEnabled']}">
			<li>
				<input type="checkbox" class="checkbox" name="_spring_security_remember_me" id="rememberMe" tabindex="3"/>
				<label for="rememberMe" class="choice"><fmt:message key="login.rememberMe"/></label>
			</li>
		</c:if>
			<li>
				<input type="submit" class="button" name="login" value="<fmt:message key='button.login'/>" tabindex="4" />
				<p style="font-size:10pt"><b>
					<fmt:message key="login.signup">
						<fmt:param><c:url value="/signup.html"/></fmt:param>
					</fmt:message></b>
				</p>
			</li>
		</ul>
		</fieldset>
		</form>

		<%@ include file="/scripts/login.js"%>

		<p><fmt:message key="login.passwordHint"/></p>

		</div>
	</c:when>

	<c:otherwise>
	<div style="margin-left:30px">
	<H3  align="left">NEMA Introduction</H3>
	<p align="left">
	Phase I of the Networked Environment for Music Analysis (NEMA) framework project is a multinational, 
	multidisciplinary cyberinfrastructure project for music information processing that builds upon and 
	extends the music information retrieval research being conducted by the International Music Information
	Retrieval Systems Evaluation Laboratory (IMIRSEL) at the University of Illinois at Urbana-Champaign (UIUC).  
	NEMA brings together the collective projects and the associated tools of six world leaders in the domains
	of music information retrieval (MIR), computational musicology (CM) and e-humanities research. 
	The NEMA team aims to create an open and extensible webservice-based resource framework that facilitates
	the integration of music data and analytic/evaluative tools that can be used 
	by the global MIR and CM research and education communities on a basis independent of time or location.
	
	</p>
	<br>
	<H3  align="left"></H3>
	<p align="left">
	NEMA is being funded through a generous grant from the Scholarly Communications program of the 
	Andrew W. Mellon Foundation.
	The Networked Environment for Music Analysis (NEMA) project was inspired by the lessons learned over the 
	course of the Mellon-funded Music Information Retrieval/Music Digital Library Evaluation Project 
	(2003-2007) being led by Prof. J. Stephen Downie and his IMIRSEL team at UIUC's Graduate School of 
	Library and Information Science (GSLIS). Downie's experience in running the annual Music Information
	 Retrieval Evaluation eXchange (MIREX) on behalf of the MIR community has brought to the fore three 
	 important issues that have a direct impact on the present NEMA project. The automation, distribution and 
	 integration of MIR and CM research tool development, evaluation and use are but some of the important 
	 issues being addressed under the NEMA rubric.
	</p>
	<br>
	<br>
	<br>

</div>
	
	</c:otherwise>
</c:choose>


