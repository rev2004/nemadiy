<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Invitation</title>
    <meta name="heading" content="<fmt:message key='login.heading'/>"/>
    <meta name="menu" content="Login"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
    <link rel="stylesheet" href='<c:url value="/scripts/jquery/openid-selector/css/openid.css" />' type="text/css" media="all" />
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.min.js" />"></script>
    <script type="text/javascript">
        imgPath= "<c:url value='/scripts/jquery/openid-selector/images/'/>";
    </script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery/openid-selector/js/openid-jquery.js" />"></script>
     <script type="text/javascript" src="<c:url value="/scripts/jquery/openid-selector/js/openid-jquery-en.js" />"></script>

</head>
<body id="login">
   
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery(document).ready(function(){
            openid.img_path="<c:url value='/scripts/jquery/openid-selector/images/'/>";
            openid.init("openid_identifier");
            jQuery("#openid_identifier").focus();
        });
    </script>
    <form method="post" id="openid_form" action="<c:url value='/openlogin'/>">
        <fieldset>
            <div id="openid_choice">
                <label for="openid_btns">Please select your <a href="http://openid.net/">OpenID</a> account provider:
                   <p style="font-size: small; font-weight: bold;">OpenID is provided by various sites, one can just use your own Google, Yahoo, AOL, or several other popular sites' account to log in. </p>
                </label>
                <div id="openid_btns"></div>
            </div>
            <div id="openid_input_area">
            <p>
                <label for="openid_identifier">Or manually enter your OpenID URL:</label><br/>
                <input id="openid_identifier" name="openid_identifier" class="openid-identifier" />
                <input id="openid_submit" type="submit" value="Sign In"/>
            </p>
            </div>
        </fieldset>
    </form>
</body>