<%-- 
    Document   : song1
    Created on : Jun 2, 2011, 12:58:28 PM
    Author     : gzhu1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Segmentation Plot Results - Segmentation Plots</title>

        <LINK REL=StyleSheet HREF="menu.css" TYPE="text/css" >
            <LINK REL=StyleSheet HREF="tableblue.css" TYPE="text/css" >

                <script type="text/javascript" src="protovis-r3.2.js"></script>
                <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
                <script type="text/javascript" src='<c:url value="/jplayer/jquery.jplayer.min.js"/>'></script>
                <script type="text/javascript" src='<c:url value="/jplayer/jquery.jplayer.inspector.js"/>'></script>
                <link rel="stylesheet" href='<c:url value="/jplayer/jplayer.blue.monday.css"/>' type="text/css" media="all" />
                <script type="text/javascript">
                    var segmentation_colors = ["lightsalmon", "lightblue", "lightgoldenrodyellow", "lightgreen", "lightgrey", "beige"];
                    var jPlayerReady=false;
                    var playInterval;
                    
                    jQuery.noConflict();//declare to avoid conflict with Prototype
                    var track_url="../struct_mrx_09_000000.wav.mp3";

                                function play(startTime,endTime){

                                    // Local copy of jQuery selectors, for performance.
                                    if (jPlayerReady) {
                                        
                                        clearInterval(playInterval);//sto
                                        jQuery("#jquery_jplayer").jPlayer("setMedia",{mp3:track_url})
                                        .jPlayer("play",startTime)
                                        .bind(jQuery.jPlayer.event.timeupdate, function(event){
                                            playbackTime = event.jPlayer.status.currentTime;
                                            if (playbackTime>endTime){
                                                if (jQuery('#jplayer_repeat:checked').val()!=null)
                                                {jQuery(this).jPlayer("play",startTime);}
                                                else {jQuery(this).jPlayer("pause");endTime=totalTime;}
                                            };
                                            focus.render();
                                            context.render();
                                        });
                                    }else{
                                        jQuery("#jplayer_status").text("jPlayer is not ready. Please wait..");
                                    }
                                }

                                function handleSelect(d){
                                    label.text("selected: " + d.o+ " to " + d.f + " seconds");
                                    playInterval=setInterval(function(){play(d.o,d.f);},1000);
                                    focus.render();
                                    context.render();
                                    //do something with player here
                                }

                                function handleDeselect() {
                                    label.text("nothing selected");
                                    jQuery("#jquery_jplayer").jPlayer("stop");
                                    jQuery("#jquery_jplayer").unbind(jQuery.jPlayer.event.timeupdate);
                                    focus.render();
                                    context.render();
                                }

   
                    jQuery(document).ready(function(){
            
                        /* setup player */
                        jQuery("#jquery_jplayer").jPlayer({
                            //warningAlerts:true,
                            //errorAlerts:true,
                            ready: function () {
                                jQuery(this).jPlayer("setMedia", {
				mp3: "http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3"
			}).jPlayer("play");
                                jPlayerReady=true;},
                            volume: 50,
                            preload: 'auto',
                            supplied: "mp3",
                            swfPath: '../jplayer'
                            
                        });
                        jQuery("#jplayer_inspector").jPlayerInspector({jPlayer:jQuery("#jquery_jplayer")});
                        
                    });
                </script>
                </head>

                <body>

                    <table id="h2table">
                        <tr>
                            <td><img src="logo.png" width="160"></td>
                            <td><h2>Segmentation Plot Results</h2></td>
                        </tr>
                    </table>

                    <div id="tabs">
                        <ul>
                            <li><a href="index.html" title="struct_mrx_09_000000"><span>struct_mrx_09_000000</span></a></li>
                            <li><a href="struct_mrx_09_000001.html" title="struct_mrx_09_000001"><span>struct_mrx_09_000001</span></a></li>
                            <li><a href="struct_mrx_09_000002.html" title="struct_mrx_09_000002"><span>struct_mrx_09_000002</span></a></li>
                            <li><a href="struct_mrx_09_000003.html" title="struct_mrx_09_000003"><span>struct_mrx_09_000003</span></a></li>
                            <li><a href="struct_mrx_09_000004.html" title="struct_mrx_09_000004"><span>struct_mrx_09_000004</span></a></li>
                        </ul>
                    </div>

                    <br><a name="top"></a>

                        <div id="content">
                            <h3>Segmentation Plots</h3>
                            <ul>
                                <li><a href="#segmentsstructmrx09000000">Segmentation Plot: Structural Segmentation for track struct_mrx_09_000000</a></li>
                            </ul>
                            <br>

                                <a name="segmentsstructmrx09000000"></a>
                                <h4>Segmentation Plot: Structural Segmentation for track struct_mrx_09_000000</h4>


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
                                                <div class="jp-progress">
                                                    <div class="jp-seek-bar">
                                                        <div class="jp-play-bar"></div>
                                                    </div>
                                                </div>
                                                <div class="jp-volume-bar">
                                                    <div class="jp-volume-bar-value"></div>
                                                </div>
                                                <div class="jp-repeat" style="left:252px;position:absolute;top:52px;"><input id="jplayer_repeat"  type="checkbox" checked="true" name="repeat"/><label>repeat</label></div>
                                                <div class="jp-current-time"></div>
                                                <div class="jp-duration"></div>
                                            </div>
                                            <div id="jp_playlist_1" class="jp-playlist">
                                                <ul>
                                                    <li>Bubble</li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="jplayer_inspector"></div>
                                    <div id="jplayer_status"></div>
                                    <div style="text-align:left;padding-left:10px;">
                                        <input type="button" value="Plot" id="segmentsstructmrx09000000_button" onClick="
                                            play(10,100);">
                                            <label for="segmentsstructmrx09000000_button">Click here to plot the figure</label>
                                            &nbsp;&nbsp;<a href="segmentsstructmrx09000000.js">download JSON data file</a>
                                    </div>


                              
                                <br>

                                    </div>
                                    </body>
                                    </html>
