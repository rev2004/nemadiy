<%@ include file="/common/taglibs.jsp"%>
<html>
<head>

<script type="text/javascript">
var count=1;
function add(){
var parentDiv = $('main');
var templateDiv = $('param1');
var clone = $(templateDiv.cloneNode(true));
count++;
clone.id="param"+count;
$(templateDiv).insert({after: clone}); 
}


function removeNode(node){
	var parent =  $(node).parentNode;
	if($(parent).id=="param1"){
	alert("Cannot delete the element");
	}else{
		$(parent).remove();
	}
}

</script>
</head>

<body>
<%


String submitVal = request.getParameter("submit");

if(submitVal!=null){
if(submitVal.equalsIgnoreCase("display")){
	String paramValues[] = request.getParameterValues("argument");
	
	for(String p:paramValues){
		out.println("====> " + p);
	}
	
}
}

%>



Testing multiple params:
<form method="get" action="">

<div id="main">

<input type="button" value="+" onclick="add()"/>

<div id="param1">
<input type="text" name="argument"/>
<input type="button" value ="-" onclick="removeNode(this)"/>
</div>
</div>

<input name="submit" value="display" type="submit"/>
</form>


</body>


</html>