/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var segmentation_colors = ["lightsalmon", "lightblue", "lightgoldenrodyellow", "lightgreen", "lightgrey", "beige"];
                    
var playInterval;
                    
                    
                   
           
function plot(numseries,data,seriesNames){

    var tmpSeg=data[data.length-1],
        start=0,
        end=tmpSeg[tmpSeg.length-1].f;
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
    var vis = new pv.Panel().canvas("plot")
    .width(w)
    .height(h1 + 20 + h2 + hOffset + hSep)
    .bottom(25)
    .left(30)
    .right(20)
    .top(10);

    vis.render();

    /*update this value to move playback position (seconds)*/
    var playbackTime = 0;



    /* Interaction state. Focus scales will have domain set on-render. */
    var i = {
        x:0, 
        dx:100
    },
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
    .top(function() {
        return 33 +((numseries - (1+this.index)) * 33);
    })
    .height(10)
    .right(0)
    .text(function(d) {
        return d;
    });
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
        for(s=0;s<numseries;s=s+1){
            ;
            offsetsearch = pv.search.index(data[s], d1, function(d) {
                return d.f;
            });
            firstvisible = offsetsearch >= 0 ? offsetsearch : -(1+offsetsearch);
            onsetsearch = pv.search.index(data[s], d2, function(d){
                return d.f;
            });
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
    .data(function() {
        return fx.ticks()
        })
    .left(fx)
    .strokeStyle("#eee")
    .anchor("bottom").add(pv.Label)
    .text(fx.tickFormat);

    function defaultPlay(event){
        playbackTime = event.jPlayer.status.currentTime;
                            
        focus.render();
        context.render();
    }
    jQuery("#jquery_jplayer").bind(jQuery.jPlayer.event.timeupdate,defaultPlay);
    
    function play(startTime,endTime){

        // Local copy of jQuery selectors, for performance.
        if (jPlayerReady) {
                                        
            clearInterval(playInterval);
            jQuery("#jquery_jplayer").jPlayer("pause",startTime).unbind(jQuery.jPlayer.event.timeupdate)
            .jPlayer("play",startTime)
            .bind(jQuery.jPlayer.event.timeupdate, function(event){
                playbackTime = event.jPlayer.status.currentTime;
                if (playbackTime>endTime){
                    if (jQuery('#jplayer_repeat:checked').val()!=null)
                    {
                        jQuery(this).jPlayer("play",startTime);
                    }
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
        playInterval=setInterval(function(){
            play(d.o,d.f);
        },1000);
        focus.render();
        context.render();
    //do something with player here
    }

    function handleDeselect() {
        label.text("nothing selected");
        jQuery("#jquery_jplayer").jPlayer("stop");
        jQuery("#jquery_jplayer").unbind(jQuery.jPlayer.event.timeupdate)
            .bind(jQuery.jPlayer.event.timeupdate,defaultPlay);
        focus.render();
        context.render();
    }

    /* Focus area chart. */
    var focus_plot = focus.add(pv.Panel)
    .overflow("hidden")
    .data(function() {
        return focus.init_data()
        })
    .def("selection",[-1,-1]);
    focus_plot.add(pv.Panel)
    .fillStyle("#FFFFFF")
    .data(function(array) {
        return array
        })
    .strokeStyle("black")
    .lineWidth(1)
    .antialias(false)
    .left(function(d) {
        return fx(d.o) < 0 ? 0 : fx(d.o)
        })
    .width(function(d) {
        return fx(d.o) < 0 ? fx(d.f) : (fx(d.f) - fx(d.o))
        })
    .bottom(function() {
        return 3 + (33*this.parent.index)
        })
    .height(30)
    .fillStyle(function(d) {
        return focus_plot.selection()[0]==this.index && focus_plot.selection()[1]==this.parent.index ? "steelblue" : pv.color(segmentation_colors[this.parent.index % segmentation_colors.length]).alpha(d.a % 2 == 0 ? 1 : 0.6)
        })
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
    .title(function(d) {
        return d.l
        })
    .anchor("left").add(pv.Label).text(function(d) {
        return fx(d.o) < 0 ? '...' + d.l : d.l
        }).width(function() {
        return this.parent.width()
        });

    /* Playback position indicator. */
    focus_plot.add(pv.Line)
    .data([0, h1-12])
    .bottom(function(d) {
        return d
        })
    .left(function() {
        return fx(playbackTime)
        })
    .strokeStyle("#000000")
    .lineWidth(2);

    /* focus region label */
    focus.add(pv.Label)
    .right(10)
    .top(12)
    .textAlign("right")
    .text(function() {
        return focus.focus_length()
        });

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
    .data(function(array) {
        return  array
        })
    .left(function(d) {
        return x(d.o)
        })
    .width(function(d) {
        return x(d.f) - x(d.o)
        })
    .bottom(function() {
        return 3 + (13 * this.parent.index)
        })
    .height(10)
    .strokeStyle("Black")
    .lineWidth(1)
    .antialias(false)
    .fillStyle(function(d) {
        return pv.color(segmentation_colors[this.parent.index % segmentation_colors.length]).alpha(d.a % 2 == 0 ? 1 : 0.6)
        })
    .title(function(d) {
        return d.l
        });

    /* Context area playback indicator */
    context.add(pv.Line)
    .data([0, h2])
    .bottom(function(d) {
        return d
        })
    .left(function() {
        return x(playbackTime)
        })
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
    .left(function(d) {
        return d.x
        })
    .width(function(d) {
        return d.dx
        })
    .fillStyle("rgba(255, 128, 128, .4)")
    .strokeStyle("rgb(255, 128, 128)")
    .lineWidth(1)
    .antialias(false)
    .title("drag to move focus region")
    .cursor("move")
    .event("mousedown", pv.Behavior.drag())
    .event("drag", focus);

    vis.render();
                            
};