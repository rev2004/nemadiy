<%-- 
    Document   : plot
    Created on : Jun 6, 2011, 4:24:26 PM
    Author     : gzhu1
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>SALAMI: Structural Segmentation Experiments - Segmentation Plots ${param.plot}</title>

        <LINK REL=StyleSheet HREF="<c:url value='/support/menu.css'/>" TYPE="text/css" >
        <LINK REL=StyleSheet HREF="<c:url value='/support/tableblue.css'/>" TYPE="text/css" >

        <script type="text/javascript" src="<c:url value='/support/protovis-r3.2.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/support/jquery.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/support/jplayer/jquery.jplayer.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/support/jplayer/jquery.jplayer.inspector.js'/>"></script>
        <link rel="stylesheet" href="<c:url value='/support/jplayer/jplayer.blue.monday.css'/>" type="text/css" media="all" />
        <c:url value='/data/segments${param.plot}.js' var="dataUrl"/>
        <script type="text/javascript" src="${dataUrl}"></script>
        <script type="text/javascript" src="<c:url value='/support/plot.js'/>"></script>
        <script type="text/javascript">
                   
            jQuery.noConflict();//declare to avoid conflict with Prototype
            var jPlayerReady=false;
            jQuery(document).ready(function(){
                jQuery("#artist").text(artist);
                jQuery("#track").text(track);
                plot( 7,data,seriesNames);
                /* setup player */
                jQuery("#jquery_jplayer").jPlayer({
                    //warningAlerts:true,
                    //errorAlerts:true,
                    ready: function () {
                        jQuery("#jquery_jplayer").jPlayer("setMedia",{oga:"http://www.music-ir.org/diy-demo/music/${param.plot}.ogg"});
                        songLoaded=true;
                        jPlayerReady=true;},
                    supplied:"oga",
                    volume: 50,
                    preload: 'auto',
                    swfPath: '../jplayer'
                            
                });
                jQuery("#jplayer_inspector").jPlayerInspector({jPlayer:jQuery("#jquery_jplayer")});
            });
        </script>
    </head>

    <body>



        <br><a name="top"></a>

        <div id="content">
            <h4>Segmentation Plot: Structural Segmentation -${param.plot}</h4>
            <h5>Artist: <label id="artist"/></h5>
            <h5>Track: <label id="track"/></h5>
            <div id="center">
                <div id="plot" style="width: 860px; height: 421px; padding: 2px; margin: 3px; border-width: 1px; border-color: black; border-style:solid;">
                </div>

                <div id="jquery_jplayer" class="jp-player"></div>
                <div class="jp-audio">
                    <div class="jp-type-single">
                        <div id="jp_interface_1" class="jp-interface">
                            <ul class="jp-controls">
                                <li><a href="#" class="jp-play" tabindex="1">play</a></li>
                                <li><a href="#" class="jp-pause" tabindex="1">pause</a></li>
                                <li><a href="#" class="jp-stop" tabindex="1">stop</a></li>
                                <li><a href="#" class="jp-mute" tabindex="1">mute</a></li>
                                <li><a href="#" class="jp-unmute" tabindex="1">unmute</a></li>
                            </ul>

                            <div class="jp-volume-bar">
                                <div class="jp-volume-bar-value"></div>
                            </div>
                            <div class="jp-repeat" style="left:252px;position:absolute;top:52px;"><input id="jplayer_repeat"  type="checkbox" checked="true" name="repeat"/><label>repeat</label></div>


                            <!--
                            <div class="jp-video-play"></div><div class="jp-seek-bar"></div><div class="jp-current-time"></div><div class="jp-duration"></div><div class="jp-play-bar"></div>-->
                        </div>

                    </div>
                </div>
                
                <div id="jplayer_status"></div>
                <br/>
                <h4 style="font-weight: bold;">Download JSON Structure Data File <button onclick="window.open('${dataUrl}');">Download</button></h4>
                    
                <br/>
                <h4>Self Similarity Images:</h4>
                <a href="http://www.music-ir.org/diy-demo/music/selfSimImg/${param.plot}.png" ><img height="400px" src="http://www.music-ir.org/diy-demo/music/selfSimImg/${param.plot}.png"/></a>
                <div id="jplayer_inspector"></div>
            </div>
            <br>

        </div>
    </body>
</html>
