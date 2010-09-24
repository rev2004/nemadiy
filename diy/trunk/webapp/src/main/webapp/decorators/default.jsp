<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <%@ include file="/common/meta.jsp" %>
        <title><decorator:title/> | <fmt:message key="webapp.name"/></title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />
        <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.4/dijit/themes/tundra/tundra.css"/>
        <script  djConfig="parseOnLoad:true, isDebug:true" type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojo/dojo.xd.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/nemaStatus.js'/>"></script>
        <script type="text/javascript">
             //dojo.addOnLoad(loadNemaStatus("<c:url value='/get/JobManager.getNemaStatus.json'/>"));
             //dojo.addOnLoad(loadNotification("<c:url value='/get/JobManager.getNotification.json'/>"));
        </script>
        <decorator:head/>
    </head>

    <body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" default="tundra" writeEntireProperty="true"/><decorator:getProperty property="body.onload" writeEntireProperty="true" />>

        <div id="page">
            <div id="header" class="clearfix">
                <jsp:include page="/common/header.jsp"/>
            </div>

            <div id="content" class="clearfix">





                <div id="nav" style="z-index:9999;">
                    <c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
                    <!--            <c:if test="${currentMenu == 'AdminMenu'}">-->
                        <!--            <div id="sub">-->
                        <!--                <menu:useMenuDisplayer name="Velocity" config="cssVerticalMenu.vm" permissions="rolesAdapter">-->
                            <!--                    <menu:displayMenu name="AdminMenu"/>-->
                            <!--                </menu:useMenuDisplayer>-->
                            <!--            </div>-->
                            <!--            </c:if>-->

                        <div class="wrapper">
                            <h2 class="accessibility">Navigation</h2>
                        <jsp:include page="/common/menu.jsp"/>
                    </div>
                    <hr/>
                </div><!-- end nav -->


                <div id="main">
                    <%@ include file="/common/messages.jsp" %>
                    <h1><decorator:getProperty property="meta.heading"/></h1>
                    <decorator:body/>
                </div>
                <div id="notification" dojoType="dijit.Dialog" title="Notice">
                    <div style="width: 160px; max-height: 100px; overflow: auto;font-size:0.8em;" id="notificationContent">	</div>
                </div>
            </div>

            <div id="footer" class="clearfix">
                <jsp:include page="/common/footer.jsp"/>
            </div>
        </div>
    </body>
</html>
