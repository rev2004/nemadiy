<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>Invitation</title>
    <meta name="heading" content="<fmt:message key='login.heading'/>"/>
    <meta name="menu" content="Login"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
    <link rel="stylesheet" href='<c:url value="/scripts/jplayer/skin/jplayer.blue.monday.css" />' type="text/css" media="all" />
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/jplayer/jquery.jplayer.min.js" />"></script>
    <script type="text/javascript">
        jQuery.noConflict();
        // Local copy of jQuery selectors, for performance.
        

        jQuery(document).ready(function(){
             //var jpPlayTime = jQuery("#jplayer_play_time");
             //var jpTotalTime = jQuery("#jplayer_total_time");

 
            jQuery("#jquery_jplayer").jPlayer({
                ready: function () {
                    this.element.jPlayer("setFile", "test.mp3", "http://www.miaowmusic.com/audio/ogg/Miaow-02-Hidden.ogg").
                        jPlayer("playHeadTime", 100000);
                },
                swfPath:"<c:url value='/scripts/jplayer/' />",
                //oggSupport:true,
                errorAlerts:true,
                warningAlerts:true,
                volume: 50,
                preload: 'auto'
            })
            .jPlayer("onProgressChange", function(loadPercent, playedPercentRelative, playedPercentAbsolute, playedTime, totalTime) {
                if (playedTime>120000){this.element.jPlayer("pause");}
                else{
                    //jpPlayTime.text(jQuery.jPlayer.convertTime(playedTime));
                    //jpTotalTime.text(jQuery.jPlayer.convertTime(totalTime));
                     jQuery("#jplayer_play_time").text(jQuery.jPlayer.convertTime(playedTime));
                    //jpTotalTime.text(jQuery.jPlayer.convertTime(totalTime));
                }
            })
            .jPlayer("onSoundComplete", function() {
                this.element.jPlayer("play");
            });
        });
        function play(file,startTime, endTime){
//            jQuery("#jplayer_filename").text(file);
//            var playTimeDiv = jQuery("#jplayer_play_time");
//            var totalTimeDiv = jQuery("#jplayer_total_time");
//
//            jQuery("#jquery_jplayer").jPlayer("setFile",file).jPlayer("playHeadTime",startTime)
//            .jPlayer("onProgressChange",function(loadPercent, playedPercentRelative, playedPercentAbsolute, playedTime, totalTime) {
//                if (playedTime>endTime){this.element.jPlayer("pause");}
//                else{
//                    playTimeDiv.text(jQuery.jPlayer.convertTime(playedTime));
//                    totalTimeDiv.text(jQuery.jPlayer.convertTime(totalTime));
//                }
//            });
//
        }
    </script>

</head>
<body id="welcomeMenu"/>

<div style="margin-left:30px">
    <H3  align="left">Music Analysis Made Simpler</H3>
    <div id="jquery_jplayer"></div>

    <div class="jp-single-player">
        <div class="jp-interface">
            <ul class="jp-controls">
                <li><a href="#" id="jplayer_play" class="jp-play" tabindex="1">play</a></li>
                <li><a href="#" id="jplayer_pause" class="jp-pause" tabindex="1">pause</a></li>
                <li><a href="#" id="jplayer_stop" class="jp-stop" tabindex="1">stop</a></li>

                <li><a href="#" id="jplayer_volume_min" class="jp-volume-min" tabindex="1">min volume</a></li>
                <li><a href="#" id="jplayer_volume_max" class="jp-volume-max" tabindex="1">max volume</a></li>
            </ul>
            <div class="jp-progress">
                <div id="jplayer_load_bar" class="jp-load-bar">
                    <div id="jplayer_play_bar" class="jp-play-bar"></div>
                </div>
            </div>

            <div id="jplayer_volume_bar" class="jp-volume-bar">
                <div id="jplayer_volume_bar_value" class="jp-volume-bar-value"></div>
            </div>
            <div id="jplayer_play_time" class="jp-play-time"></div>
            <div id="jplayer_total_time" class="jp-total-time"></div>
        </div>
        <div id="jplayer_playlist" class="jp-playlist">
            <ul>
                <li id="jplayer_filename">testing</li>

            </ul>
        </div>

    </div>
    <input type="button" onclick="play('test.mp3',20000,30000);" name="play1" value="test"/>
    <input type="button" onclick="play('test.mp3',40000,50000);" name="play2" value="test 2"/>
    <p align="left">
	Phase I of the Networked Environment for Music Analysis (NEMA) framework project is a multinational, 
	multidisciplinary cyberinfrastructure project for music information processing that builds upon and 
	extends the music information retrieval research being conducted by the International Music Information
	Retrieval Systems Evaluation Laboratory (IMIRSEL) at the University of Illinois at Urbana-Champaign (UIUC).  
	NEMA brings together the collective projects and the associated tools of six world leaders in the domains
	of music information retrieval (MIR), computational musicology (CM) and e-humanities research. 
	The NEMA team aims to create an open and extensible webservice-based resource framework that facilitates
	the integration of music data and analytic/evaluative tools that can be used 
	by the global MIR and CM research and education communities on a basis independent of time or location.

    </p>
    <br>
    <H3  align="left"></H3>
    <p align="left">
	NEMA is being funded through a generous grant from the Scholarly Communications program of the 
	Andrew W. Mellon Foundation.
	The Networked Environment for Music Analysis (NEMA) project was inspired by the lessons learned over the 
	course of the Mellon-funded Music Information Retrieval/Music Digital Library Evaluation Project 
	(2003-2007) being led by Prof. J. Stephen Downie and his IMIRSEL team at UIUC's Graduate School of 
	Library and Information Science (GSLIS). Downie's experience in running the annual Music Information
	 Retrieval Evaluation eXchange (MIREX) on behalf of the MIR community has brought to the fore three 
	 important issues that have a direct impact on the present NEMA project. The automation, distribution and 
	 integration of MIR and CM research tool development, evaluation and use are but some of the important 
	 issues being addressed under the NEMA rubric.
    </p>
    <br>
    <br>
    <br>

</div>

