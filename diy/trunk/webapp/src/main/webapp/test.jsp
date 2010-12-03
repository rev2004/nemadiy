<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>jPlayer Demo</title>
    <meta name="heading" content="jPlayer Demo"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/layout-1col.css'/>" />
    <link rel="stylesheet" href='<c:url value="/scripts/jplayer/skin/jplayer.blue.monday.css" />' type="text/css" media="all" />
    <script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/jplayer/jquery.jplayer.min.js" />"></script>
    <script type="text/javascript">
        jQuery.noConflict();//declare to avoid conflict with Prototype

     
        //prepare the jplayer object
        jQuery(document).ready(function(){
 
            jQuery("#jquery_jplayer").jPlayer({
                ready: function () {
                },
                swfPath:"<c:url value='/scripts/jplayer/' />",
                //oggSupport:true,
                //errorAlerts:true,
                //warningAlerts:true,
                volume: 50,
                preload: 'auto'
            });
        });

        //play the mp3 file from startTime to endTime (in millisecond)
        function play(file,startTime, endTime){
            //display the file name in the bottom of player.
            jQuery("#jplayer_filename").text(file);

            // Local copy of jQuery selectors, for performance.
            var playTimeDiv = jQuery("#jplayer_play_time");
            var totalTimeDiv = jQuery("#jplayer_total_time");

            jQuery("#jquery_jplayer").jPlayer("setFile",file).jPlayer("playHeadTime",startTime)
            .jPlayer("onProgressChange",function(loadPercent, playedPercentRelative, playedPercentAbsolute, playedTime, totalTime) {
                if (playedTime>endTime){this.element.jPlayer("pause");endTime=totalTime;}
                else{
                    playTimeDiv.text(jQuery.jPlayer.convertTime(playedTime));
                    totalTimeDiv.text(jQuery.jPlayer.convertTime(totalTime));
                }
            });
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
    
    <H3  align="left"></H3>
    <p align="left">
	Example of jPlayer
    </p>
    <br>
    <br>
    <br>

</div>

