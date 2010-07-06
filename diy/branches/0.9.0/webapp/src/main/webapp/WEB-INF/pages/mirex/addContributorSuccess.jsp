<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
 window.opener.addContributor(${contributor.id});
</script></head>
<body>

 <input type="button" onclick="window.location.href='submissions.contributor.search.php';" value="Search for other contributors"/>
	<input type="button" onclick="window.location.href='submissions.contributor.create.php';" value="Create a new contributor record"/>
	<input type="button" onclick="window.close()" value="Close Window"/>
</body>
</html>