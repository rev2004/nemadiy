<%-- 
    Document   : song1
    Created on : Jun 2, 2011, 12:58:28 PM
    Author     : gzhu1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Segmentation Plot Results - Segmentation Plots</title>

        <LINK REL=StyleSheet HREF="menu.css" TYPE="text/css" >
            <LINK REL=StyleSheet HREF="tableblue.css" TYPE="text/css" >

                <script type="text/javascript" src="protovis-r3.2.js"></script>
                <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
                <script type="text/javascript" src='../jplayer/jquery.jplayer.min.js'></script>
                <script type="text/javascript" src='../jplayer/jquery.jplayer.inspector.js'></script>
                <link rel="stylesheet" href='../jplayer/jplayer.blue.monday.css' type="text/css" media="all" />
                <script type="text/javascript">
                    var segmentation_colors = ["lightsalmon", "lightblue", "lightgoldenrodyellow", "lightgreen", "lightgrey", "beige"];
                    var jPlayerReady=false;
                    var playInterval;
                    var songLoaded=false;
                    
                    jQuery.noConflict();//declare to avoid conflict with Prototype
                    function loadScript(url, plot){
                        var loadedscript = document.createElement('script');
                        loadedscript.setAttribute("type","text/javascript");

                        if (loadedscript.readyState){  //IE
                            loadedscript.onreadystatechange = function(){
                                if (loadedscript.readyState == "loaded" ||
                                    loadedscript.readyState == "complete"){
                                    loadedscript.onreadystatechange = null;
                                    plot.setLoaded();
                                }
                            };
                        } else {  //Others
                            loadedscript.onload = function(){
                                plot.setLoaded();
                            };
                        }
                        loadedscript.setAttribute("src", url);
                        document.body.appendChild(loadedscript);
                    }

                    function init_segmentation_plot(start,end,numseries){
                        /* Scales and sizing. */
                        var w = 810,
                        hOffset = 0,
                        hSep = 15,
                        legendOffset = 60,
                        h1 = 15 + 3 + 33 * numseries,
                        h2 = 15 * numseries,
                        totalHeight = h1 + 20 + h2 + 15 + hOffset + hSep,
                        x = pv.Scale.linear(start, end).range(0, w-legendOffset),
                        i = -1;

                        /* Root panel. */
                        var vis = new pv.Panel()
                        .width(w)
                        .height(h1 + 20 + h2 + hOffset + hSep)
                        .bottom(25)
                        .left(30)
                        .right(20)
                        .top(10);

                        vis.render();

                        var loaded = false;

                        return {
                            setLoaded : function(){loaded = true;},
                            isLoaded : function(){return loaded;},
                            plot : function(track_url,data,seriesNames){

                                jQuery.noConflict();//declare to avoid conflict with Prototype

                                /*update this value to move playback position (seconds)*/
                                var playbackTime = 0;



                                /* Interaction state. Focus scales will have domain set on-render. */
                                var i = {x:0, dx:100},
                                fx = pv.Scale.linear().range(0, w-legendOffset);

                                /* Legend area. */
                                var legend = vis.add(pv.Panel)
                                .left(0)
                                .width(legendOffset)
                                .height(totalHeight)
                                .top(0);
                                legend.add(pv.Label)
                                .data(seriesNames)
                                .textAlign("right")
                                .textBaseline("middle")
                                .top(function() {return 33 +((numseries - (1+this.index)) * 33);})
                                .height(10)
                                .right(0)
                                .text(function(d) {return d;});
                                legend.add(pv.Label)
                                .text('time (secs)')
                                .textAlign("right")
                                .height(10)
                                .right(5)
                                .textBaseline("top")
                                .bottom(15);
                                legend.add(pv.Label)
                                .text('time (secs)')
                                .textAlign("right")
                                .height(10)
                                .right(5)
                                .textBaseline("top")
                                .top(hOffset + h1);

                                /* Focus panel (zoomed in). */
                                var focus = vis.add(pv.Panel)
                                .left(legendOffset)
                                .def("init_data", function() {
                                    var d1 = x.invert(i.x),
                                    d2 = x.invert(i.x + i.dx);
                                    var out = new Array(numseries);
                                    for(s=0;s<numseries;s=s+1){;
                                        offsetsearch = pv.search.index(data[s], d1, function(d) {return d.f;});
                                        firstvisible = offsetsearch >= 0 ? offsetsearch : -(1+offsetsearch);
                                        onsetsearch = pv.search.index(data[s], d2, function(d){return d.f;});
                                        lastvisible = onsetsearch >= 0 ? onsetsearch : -(1+onsetsearch);
                                        out[s] = data[s].slice(firstvisible,lastvisible+1);
                                    }
                                    fx.domain(d1, d2);
                                    return out;
                                })
                                .def("focus_length", function() {
                                    return "showing: " + x.invert(i.x).toFixed(2) + " to " + x.invert(i.x + i.dx).toFixed(2) + " seconds";
                                })
                                .top(hOffset)
                                .height(h1);

                                /* Context panel (zoomed out). */
                                var context = vis.add(pv.Panel)
                                .left(legendOffset)
                                .bottom(0)
                                .height(h2);

                               
                                /* X-axis ticks. */
                                focus.add(pv.Rule)
                                .data(function() {return fx.ticks()})
                                .left(fx)
                                .strokeStyle("#eee")
                                .anchor("bottom").add(pv.Label)
                                .text(fx.tickFormat);

                                function play(startTime,endTime){

                                    // Local copy of jQuery selectors, for performance.
                                    if (jPlayerReady) {
                                        
                                        clearInterval(playInterval);
                                        if (!songLoaded){
                                            jQuery("#jquery_jplayer").jPlayer("setMedia",{mp3:track_url});
                                            songLoaded=true;
                                        }
                                        jQuery("#jquery_jplayer").jPlayer("stop").unbind(jQuery.jPlayer.event.timeupdate)
                                        .jPlayer("play",startTime)
                                        .bind(jQuery.jPlayer.event.timeupdate, function(event){
                                            playbackTime = event.jPlayer.status.currentTime;
                                            if (playbackTime>endTime){
                                                if (jQuery('#jplayer_repeat:checked').val()!=null)
                                                {jQuery(this).jPlayer("play",startTime);}
                                                else {
                                                    jQuery(this).jPlayer("pause");
                                                    //endTime=totalTime;
                                                }
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

                                /* Focus area chart. */
                                var focus_plot = focus.add(pv.Panel)
                                .overflow("hidden")
                                .data(function() {return focus.init_data()})
                                .def("selection",[-1,-1]);
                                focus_plot.add(pv.Panel)
                                .fillStyle("#FFFFFF")
                                .data(function(array) {return array})
                                .strokeStyle("black")
                                .lineWidth(1)
                                .antialias(false)
                                .left(function(d) {return fx(d.o) < 0 ? 0 : fx(d.o)})
                                .width(function(d) {return fx(d.o) < 0 ? fx(d.f) : (fx(d.f) - fx(d.o))})
                                .bottom(function() {return 3 + (33*this.parent.index)})
                                .height(30)
                                .fillStyle(function(d) {return focus_plot.selection()[0]==this.index && focus_plot.selection()[1]==this.parent.index ? "steelblue" : pv.color(segmentation_colors[this.parent.index % segmentation_colors.length]).alpha(d.a % 2 == 0 ? 1 : 0.6)})
                                /*.event("click", function(d) label.text("selected: " + d.o+ " to " + d.f + " seconds"))*/
                                .event("click",function(d){
                                    if(focus_plot.selection()[0]==this.index && focus_plot.selection()[1]==this.parent.index){
                                        handleDeselect();
                                        focus_plot.selection([-1,-1]);
                                    }else{
                                        handleSelect(d);
                                        focus_plot.selection([this.index,this.parent.index]);
                                    }
                                })
                                .add(pv.Panel)
                                .title(function(d) {return d.l})
                                .anchor("left").add(pv.Label).text(function(d) {return fx(d.o) < 0 ? '...' + d.l : d.l}).width(function() {return this.parent.width()});

                                /* Playback position indicator. */
                                focus_plot.add(pv.Line)
                                .data([0, h1-12])
                                .bottom(function(d) {return d})
                                .left(function() {return fx(playbackTime)})
                                .strokeStyle("#000000")
                                .lineWidth(2);

                                /* focus region label */
                                focus.add(pv.Label)
                                .right(10)
                                .top(12)
                                .textAlign("right")
                                .text(function() {return focus.focus_length()});

                                /* selected label */
                                var label = focus.add(pv.Label)
                                .right(180)
                                .top(12)
                                .textAlign("right")
                                .text("nothing selected");



                                /* X-axis ticks. */
                                context.add(pv.Rule)
                                .data(x.ticks())
                                .left(x)
                                .strokeStyle("#eee")
                                .anchor("bottom").add(pv.Label)
                                .text(x.tickFormat);

                                context.add(pv.Rule)
                                .bottom(0);
                                context.add(pv.Rule)
                                .left(0);

                                focus.add(pv.Rule)
                                .bottom(0);
                                focus.add(pv.Rule)
                                .left(0);



                                /* Context area chart. */
                                context.add(pv.Panel)
                                .data(data)
                                .add(pv.Bar)
                                .data(function(array) {return  array})
                                .left(function(d) {return x(d.o)})
                                .width(function(d) {return x(d.f) - x(d.o)})
                                .bottom(function() {return 3 + (13 * this.parent.index)})
                                .height(10)
                                .strokeStyle("Black")
                                .lineWidth(1)
                                .antialias(false)
                                .fillStyle(function(d) {return pv.color(segmentation_colors[this.parent.index % segmentation_colors.length]).alpha(d.a % 2 == 0 ? 1 : 0.6)})
                                .title(function(d) {return d.l});

                                /* Context area playback indicator */
                                context.add(pv.Line)
                                .data([0, h2])
                                .bottom(function(d) {return d})
                                .left(function() {return x(playbackTime)})
                                .strokeStyle("#000000")
                                .lineWidth(2);

                                /* The selectable, draggable focus region. */
                                context.add(pv.Panel)
                                .data([i])
                                .bottom(0)
                                .cursor("crosshair")
                                .events("all")
                                .event("mousedown", pv.Behavior.select())
                                .event("select", focus)
                                .title("click and drag to select new focus region")
                                .add(pv.Bar)
                                .left(function(d) {return d.x})
                                .width(function(d) {return d.dx})
                                .fillStyle("rgba(255, 128, 128, .4)")
                                .strokeStyle("rgb(255, 128, 128)")
                                .lineWidth(1)
                                .antialias(false)
                                .title("drag to move focus region")
                                .cursor("move")
                                .event("mousedown", pv.Behavior.drag())
                                .event("drag", focus);

                                vis.render();
                            }
                        };
                    }
                    jQuery(document).ready(function(){
            
                        /* setup player */
                        jQuery("#jquery_jplayer").jPlayer({
                            warningAlerts:true,
                            //errorAlerts:true,
                            ready: function () {
                                jPlayerReady=true;},
                            volume: 50,
                            preload: 'auto',
                            swfPath: '../jplayer'
                            
                        });
                        jQuery("#jplayer_inspector").jPlayerInspector({jPlayer:jQuery("#jquery_jplayer")});
                        jQuery("#segmentsstructmrx09000000_button").removeAttr('disabled');
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
                                <div id="center">
                                    <div style="width: 860px; height: 421px; padding: 2px; margin: 3px; border-width: 1px; border-color: black; border-style:solid;">
                                        <script type="text/javascript">
                                            var segmentsstructmrx09000000_segment_plot = init_segmentation_plot(0.0, 141.087, 7);
                                            
                                            var segmentsstructmrx09000000_interval;
                                            function segmentsstructmrx09000000_serviceInterval(){
                                                if(segmentsstructmrx09000000_segment_plot.isLoaded()){
                                                    clearInterval(segmentsstructmrx09000000_interval);
                                                    segmentsstructmrx09000000_segment_plot.plot(segmentsstructmrx09000000_track_url,segmentsstructmrx09000000_data,segmentsstructmrx09000000_seriesNames);
                                                    //document.getElementById("segmentsstructmrx09000000_button").setAttribute("value","done.");
                                                    jQuery("#segmentsstructmrx09000000_button").attr('value','plot').attr('disabled','disabled');
                                                }
                                            }
                                        </script>

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
                                    <div id="jplayer_inspector"></div>
                                    <div id="jplayer_status"></div>
                                    <div style="text-align:left;padding-left:10px;">
                                        <input type="button" value="Plot" id="segmentsstructmrx09000000_button" onClick="
                                            this.value='loading data...';
                                            this.disabled=true;
                                            loadScript('segmentsstructmrx09000000.js',segmentsstructmrx09000000_segment_plot);
                                            segmentsstructmrx09000000_interval = setInterval('segmentsstructmrx09000000_serviceInterval()',500);
                                               ">
                                            <label for="segmentsstructmrx09000000_button">Click here to plot the figure</label>
                                            &nbsp;&nbsp;<a href="segmentsstructmrx09000000.js">download JSON data file</a>
                                    </div>


                                </div>
                                <br>

                                    </div>
                                    </body>
                                    </html>
