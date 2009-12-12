<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="test.title"/></title>
    <meta name="heading" content="<fmt:message key='test.jsp'/>"/>
    <meta name="menu" content="welcomeMenu"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
    <script src="/scripts/prototype.js" type="text/javascript"></script>
    <script type="text/javascript">
    Ajax.Responders.register({
  onSuccess:function(transport){
     var notificationList = transport.responseText.evalJSON();
     if(json!=null){
     var start="<table id=\"notification\">";
     var end="</table>";
     var noteTable="";
     notificationList.each(function(item){
        noteTable=noteTable+"<tr><td>"+item.dateCreated+"</td><td>"+item.message+"</td></tr>\n";
     });
     Element.replace("notification", start+noteTable+end);
     }
   }
	});
    new PeriodicalExecuter(function(){
  
    new Ajax.request("/get/JobManager.getNotification");
    },10);
    </script>
</head>
<body>
<div id="nothing"></div>
<table id="notification" />
</body>


