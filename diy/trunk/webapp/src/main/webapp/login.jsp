<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="login.title"/></title>
    <meta name="heading" content="<fmt:message key='login.heading'/>"/>
    <meta name="menu" content="Login"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
    <link rel="stylesheet" href='<c:url value="/scripts/jquery/openid-selector/css/openid.css" />' type="text/css" media="all" />
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.min.js" />"></script>
    <script type="text/javascript">
        imgPath= "<c:url value='/scripts/jquery/openid-selector/images/'/>";
    </script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery/openid-selector/js/openid-jquery.js" />"></script>

</head>
<body id="login">
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery(document).ready(function(){
            openid.img_path=imgPath;
            openid.init("openid_identifier");
            jQuery("#openid_identifier").focus();
        });
    </script>
    <form method="post" id="loginForm" action="<c:url value='/openlogin'/>">
        <fieldset>
            <div id="openid_choice">
                <label for="openid_btns">Please select your account provider:</label>
                <div id="openid_btns"></div>
            </div>
            <div id="openid_input_area">
            </div>
            <p>
                <label for="openid_identifier">Or manually enter your OpenID URL:</label><br/>
                <input id="openid_identifier" name="openid_identifier" class="openid-identifier" />
                <input id="openid_submit" type="submit" value="Sign In"/>
            </p>
        </fieldset>
    </form>
</body>