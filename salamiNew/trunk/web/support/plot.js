/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Author: Kris West, Guojun Zhu
 */


var segmentation_colors = ["salmon", "slateblue", "red", "green", "grey", "lightseagreen","gold"];
                    
var playInterval;
                    
/**
 *numseries: number of series, (it is redundant as length of data or seriesNames)
 *data: real segmentation data
 *seriesNames:  Names of algorithms. 
 *                    
 *                    */                    
                   
           
function plot(numseries,data,seriesNames){
    
    var stat=process(data);

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
    fullScale = pv.Scale.linear(start, end).range(0, w-legendOffset),
    interaction = -1;
    
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
    var interaction = {
        x:0, 
        dx:100
    },
    contentScale = pv.Scale.linear().range(0, w-legendOffset);

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
        var d1 = fullScale.invert(interaction.x),
        d2 = fullScale.invert(interaction.x + interaction.dx);
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
        contentScale.domain(d1, d2);
        return out;
    })
    .def("focus_length", function() {
        return "showing: " + fullScale.invert(interaction.x).toFixed(2) + " to " + fullScale.invert(interaction.x + interaction.dx).toFixed(2) + " seconds";
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
        return contentScale.ticks()
    })
    .left(contentScale)
    .strokeStyle("#eee")
    .anchor("bottom").add(pv.Label)
    .text(contentScale.tickFormat);


    //enable the play button in jplayer to play the full song
    function defaultPlay(event){
        playbackTime = event.jPlayer.status.currentTime;
                            
        focus.render();
        context.render();
    }
    jQuery("#jquery_jplayer").bind(jQuery.jPlayer.event.timeupdate,defaultPlay);
    
    
 
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
        return contentScale(d.o) < 0 ? 0 : contentScale(d.o)
    })
    .width(function(d) {
        return contentScale(d.o) < 0 ? contentScale(d.f) : (contentScale(d.f) - contentScale(d.o))
    })
    .bottom(function() {
        return 3 + (33*this.parent.index)
    })
    .height(30)
    .fillStyle(function(d) {
         var color=stat[this.parent.index].color;
        return (d.o<playbackTime-0.1)&&(d.f>playbackTime+0.1) && focus_plot.selection()[1]==this.parent.index ? 
            "steelblue" : 
            pv.color(segmentation_colors[this.parent.index % segmentation_colors.length]).alpha(1-0.8*color[d.l]/color.total);
    })
    /*.event("click", function(d) label.text("selected: " + d.o+ " to " + d.f + " seconds"))*/
    .event("dblclick",function(d){
        console.log("playbackTime:",playbackTime);
        if((d.o<=playbackTime)&&(d.f>=playbackTime) && focus_plot.selection()[1]==this.parent.index){
            handleDeselect();
            focus_plot.selection([-1,-1]);
        }else{
            handleSelect(d,this.parent.index);
            focus_plot.selection([this.index,this.parent.index]);
        }
    })
    .add(pv.Panel)
    .title(function(d) {
        return d.l
    })
    .anchor("left").add(pv.Label).text(function(d) {
        return contentScale(d.o) < 0 ? '...' + d.l : d.l
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
        return contentScale(playbackTime)
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
    .data(fullScale.ticks())
    .left(fullScale)
    .strokeStyle("#eee")
    .anchor("bottom").add(pv.Label)
    .text(fullScale.tickFormat);

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
        return fullScale(d.o)
    })
    .width(function(d) {
        return fullScale(d.f) - fullScale(d.o)
    })
    .bottom(function() {
        return 3 + (13 * this.parent.index)
    })
    .height(10)
    .strokeStyle("Black")
    .lineWidth(1)
    .antialias(false)
    .fillStyle(function(d) {
        var color=stat[this.parent.index].color;
        
        return pv.color(segmentation_colors[this.parent.index % segmentation_colors.length]).alpha(1-0.8*color[d.l]/color.total);
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
        return fullScale(playbackTime)
    })
    .strokeStyle("#000000")
    .lineWidth(2);

    /* The selectable, draggable focus region. */
    context.add(pv.Panel)
    .data([interaction])
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
               
               
    /*
     * Parameter
     *   sign: the segmentation letter (string) "A", "1", "B#"...
     */           
    function play(startTime,endTime,sign){

        playbackTime=startTime+0.01;
        // Local copy of jQuery selectors, for performance.
        if (jPlayerReady) {
                           
                           
            //console.log("play ",startTime," to ",endTime,"(",playInterval,")");
            clearInterval(playInterval);
            
            jQuery("#jquery_jplayer").jPlayer("pause",startTime).unbind(jQuery.jPlayer.event.timeupdate)
            .jPlayer("play",startTime)
            .bind(jQuery.jPlayer.event.timeupdate, function(event){
                playbackTime = event.jPlayer.status.currentTime;
                if (interaction.dx+interaction.x<fullScale(playbackTime)) {
                    interaction.dx=Math.min(fullScale(endTime)-interaction.x+1,fullScale(end)-interaction.x);
                }
                if (playbackTime>endTime){
                    if (jQuery('#next:checked').val()!=null){
                        var nextSegIndex=findNextSegmentIndex(data[focus_plot.selection()[1]],playbackTime,sign);
                        //focus_plot.selection()[0]=nextSegIndex;
                        var nextSeg=data[focus_plot.selection()[1]][nextSegIndex];
                        if ((jQuery('#jplayer_repeat:checked').val()!=null)||(nextSeg.o>playbackTime)){
                            label.text("selected: " + nextSeg.o+ " to " + nextSeg.f + " seconds");
                            endTime=nextSeg.f;
                            startTime=nextSeg.o;
                            if (interaction.x>fullScale(startTime)) interaction.x=Math.max(0,fullScale(startTime)-2);
                            if (interaction.dx+interaction.x<fullScale(endTime)) {
                                interaction.dx=Math.min(fullScale(endTime)-interaction.x+1,fullScale(end)-interaction.x);
                            }
                            jQuery(this).jPlayer("play",startTime); 
                            return;
                        }
                    }else {
                        if (jQuery('#jplayer_repeat:checked').val()!=null)
                        {
                            jQuery(this).jPlayer("play",startTime);
                            return;
                        };
                    };
                    jQuery(this).jPlayer("pause",startTime);
                //endTime=totalTime;
                    
                };
                focus.render();
                context.render();
            });
        }else{
            jQuery("#jplayer_status").text("jPlayer is not ready. Please wait..");
        }
    }

    function handleSelect(d,index){
        label.text("selected: " + d.o+ " to " + d.f + " seconds");
        
        playInterval=setInterval(function(){
            
            play(d.o,d.f,d.l);
        },1000);
        //console.log("playInterval:",playInterval);
        focus.render();
        context.render();
    //do something with player here
    
        jQuery("#lineStat").show();
        jQuery("#lineStatTitle").html("Segments list of "+seriesNames[index]+":");
        var line=data[index].slice(0);
        var $table=jQuery("#lineStatTable");
        line.sort(function(a,b){return a.l>b.l;});
        var sign=line[0].l;
        var content="<tr class='lineStat'><td class='lineStat'>"
                +sign+"</td><td class='lineStat'>:</td><td>";
        var count=1;
        for (var i in line){
            if (line[i].l!=sign){
                sign=line[i].l
               count=1;
               content+="</td></tr>";
               content+="<tr class='lineStat'><td class='lineStat'>"
               content+=sign+"</td><td class='lineStat'>:</td><td>";
               
            };
            content+="<input type='button'  onclick='play(";
            content+=line[i].o+","+line[i].f+",\""+sign+"\"";
            content+=")' value='"+count+"'/>";
            count++;
        }
        content+="</td></tr>";
        jQuery("#lineStatTable").html(content);
    
    }

    function handleDeselect() {
        label.text("nothing selected");
        jQuery("#jquery_jplayer").jPlayer("stop");
        jQuery("#jquery_jplayer").unbind(jQuery.jPlayer.event.timeupdate)
        .bind(jQuery.jPlayer.event.timeupdate,defaultPlay);
        focus.render();
        context.render();
    }

    function findNextSegmentIndex(lineData,currentTime,sign){
        var i = pv.search.index(lineData, currentTime, function(d) {
            return d.f;
        });
        i   =i >= 0 ? i : -(1+i);
        i=i%lineData.length;
        while (lineData[i].l!=sign){
            i++; 
            i=i%lineData.length;
        }
        return i;
    }
    
    function process(data){
        var stat=new Array();
        for (var i in data){
            var line=data[i].slice(0);
            line.sort(function(a,b){return a.l>b.l;});
            var count=0;
            var sign=line[0].l;
            var color=new Object();
            color[sign]=count;
            for (var j in line){
               if (line[j].l!=sign){
                sign=line[j].l
                count++;
                color[sign]=count;
               }
            };
            color.total=count+1;
            stat[i]={line:line, color:color};
        };
        return stat;
    }
    return play;
};