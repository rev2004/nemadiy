<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="test.title"/></title>
    <meta name="heading" content="<fmt:message key='test.jsp'/>"/>
    <meta name="menu" content="welcomeMenu"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
    <script src="/scripts/prototype.js" type="text/javascript"></script>
    <script type="text/javascript">

    new PeriodicalExecuter(function(){
  
    new Ajax.Request("/get/JobManager.getNotification",{
    
      onSuccess:function(transport){
     var notificationList = transport.responseText.evalJSON();
     
     if(notificationList!=null){
     var start="<table id=\"notification\" border=\"1\">";
     var end="</table>";
     var noteTable="";
     
     notificationList.list.each(function(item){
        noteTable=noteTable+"<tr><td>"+item.dateCreated.$+"</td><td>"+item.message+"</td></tr>\n";
     });
     $('notification').replace( start+noteTable+end);
     }
   }});
    },10);
    </script>
</head>
<body>
<div id="nothing"></div>
<table id="notification" />
</body>


