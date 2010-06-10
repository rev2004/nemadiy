<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="jobdetails.title" /></title>
<meta name="menu" content="Error" />

<script type="text/javascript">
var stackTraceOn=false;
function change(){
	
	if (stackTraceOn) {
		$('changeButton').writeAttribute("value","Show Stack Trace");
		$('stackTrace').hide();
		$('rootException').show();
		
	}else{
		$('changeButton').writeAttribute("value","Hide Stack Trace");
		$('stackTrace').show();
		$('rootException').hide();
	}
	stackTraceOn=!stackTraceOn;
}

</script>

</head>
<body>


    <h2 class="error">An unexpected system error has occurred.</h2>
    
	<div class="error">${flowExecutionException}</div>
	
	
	<form>
	 <input type="button"  value="Show Stack Trace" onclick="change();" id="changeButton"/>
	 </form>
	 <div id="rootException">
	  <label>Root: exception</label>
	${rootCauseException}
	</div>
	 <div id="stackTrace" style="display:none">
	
	<code>
	
<% 
Exception ex = (Exception) request.getAttribute("flowExecutionException");
ex.printStackTrace(new java.io.PrintWriter(out)); 
%>
	</code>
	</div>
	
</body>

