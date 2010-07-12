<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Contributor</title>
</head>
<body>

  <div style="padding:10px">
<h2>Search</h2>

<input type="text" id="contrib-search" value="search for contributors"/>
<div id="search-results"></div>
<p>Can't find the person you're looking for? <a href="<c:url value='/get/MirexManager.addContributor.frag'/>">Create a new Contributor Profile</a> for them.</p>
<input type="button" onclick="window.close()" value="Close Window"/>
</div>
</body>
</html>