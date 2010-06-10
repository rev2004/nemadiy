/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.resultpages;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.structure.NemaSegment;

/**
 * A PageItem implementation for plotting one or more segmentations of a 
 * time-line.
 * Implements Javascript rendering using Protovis.
 * 
 * @author kris.west@gmail.com
 */
public class ProtovisSegmentationPlotItem extends PageItem{

	public static final DecimalFormat MS_FORMAT = new DecimalFormat("###.# ms");
	
	private double startTime;
    private double endTime;
    private Map<String,List<NemaSegment>> series; //indices are 0=x, 1=y
    
    public ProtovisSegmentationPlotItem(String name, String caption, 
    		double startTime, double endTime, 
    		Map<String,List<NemaSegment>> series){
    	
        super(name,caption);
        setStartTime(startTime);
        setEndTime(endTime);
        setSeries(series);
    }
    

    public String getHeadStaticDeclarations(){
    	//for javascript debugging use: protovis-d3.2.js
    	//String out = "<script type=\"text/javascript\" src=\"protovis-d3.2.js\"></script>\n" +
		
    	String out = "<script type=\"text/javascript\" src=\"protovis-r3.2.js\"></script>\n";
		return out;
    }
    
    
    @Override
    public String getHeadData(){
    	return "";
    }

    @Override
    public String getBodyData(boolean topLink){
    	System.out.println("generating plot HTML for: " + getName());
    	String out = "\t<a name=\"" + getName() + "\"></a>\n" +
        "\t<h4>" + getCaption();
		if (topLink){
		    out += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
		}
		out += "</h4>\n";
		
		int height = (3 + 33 * series.size()) + (15 * series.size()) + 20 + 2 + 20;
		
		
        out += 	"\t<div id=\"center\">\n" +
        		"\t\t<div style=\"width: 860px; height: " + height + "px;\">\n" +
        		"\t\t\t<script type=\"text/javascript+protovis\">\n";
        
        out +=  "\t\t\tvar " + getName() + "_segmentationplot = new function() {\n";
        
        //setup data
        String i = "\t\t\t\t";
        out +=  i + "var start = " + startTime + ";\n" +
        		i + "var end = " + endTime + ";\n\n";
        
        
        String[] seriesNames = series.keySet().toArray(new String[series.size()]);
        String[] seriesVar = new String[seriesNames.length];
        String[] seriesInitMethod = new String[seriesNames.length];
        
        for (int s = 0; s < seriesNames.length; s++) {
        	System.out.println("\tgenerating series: " + seriesNames[s]);
        	seriesVar[s] = "data" + s;
			out += i + "var " + seriesVar[s] + " = [\n";
			List<NemaSegment> data = series.get(seriesNames[s]);
			NemaSegment seg = null;
			for (Iterator<NemaSegment> it = data.iterator(); it.hasNext();) {
				seg = it.next();
				out += "{onset: " + seg.getOnset() + ", offset: " + seg.getOffset() + ", label: \"" + seg.getLabel() + "\"}";
				if(it.hasNext()){
					out +=",\n";
				}else{
					out += "\n" + i + "];\n\n";
				}
			}
		}
        
        out += i + "var numseries = " + seriesNames.length + ";\n";        
        out += i + "var allSeries = [";
        for (int s = 0; s < seriesVar.length-1; s++) {
        	out += seriesVar[s] + ",";
        }
        out += seriesVar[seriesVar.length-1] + "];\n";
        out += i + "var seriesNames = [";
        for (int j = 0; j < seriesNames.length; j++) {
			out += "\"" + seriesNames[j] + "\"";
			if (j < seriesNames.length-1){
				out += ",";
			}
		}
        out += "];\n";
        		
        out += i + "var colors = [\"salmon\", \"steelblue\", \"green\", \"pink\", \"navy\"];\n\n";//

        out += i + "/* Scales and sizing. */\n";
		out += i + "var w = 810,\n";
		out += i + "    hOffset = 0,\n";
		out += i + "    legendOffset = 0,\n";
		out += i + "    h1 = 3 + 33 * numseries,\n";
		out += i + "    h2 = 15 * numseries,\n";
		out += i + "    x = pv.Scale.linear(start, end).range(0, w),\n";
		out += i + "    i = -1;\n\n";

		out += i + "/* Interaction state. Focus scales will have domain set on-render. */\n";
		out += i + "var i = {x:0, dx:200},\n";
		out += i + "    fx = pv.Scale.linear().range(0, w);\n\n";

		out += i + "/* Root panel. */\n";
		out += i + "var vis = new pv.Panel()\n";
		out += i + "    .width(w)\n";
		out += i + "    .height(h1 + 20 + h2 + hOffset)\n";
		out += i + "    .bottom(20)\n";
		out += i + "    .left(30)\n";
		out += i + "    .right(20)\n";
		out += i + "    .top(5);\n\n";

		out += i + "/* Legend area. */\n";
		out += i + "var legend = vis.add(pv.Panel)\n";
		out += i + "    .left(0)\n";
		out += i + "    .width(legendOffset)\n";
		out += i + "    .bottom(function() (3 + this.index * 33))\n";
		out += i + "    .top(0);\n";
		out += i + "legend.add(pv.Label)\n";
		out += i + "    .data(seriesNames)\n";
		out += i + "    .textAlign(\"right\")\n";
		out += i + "    .textBaseline(\"middle\")\n";
		out += i + "    .top(function() 18+((numseries - (1+this.index)) * 33)) \n";
		out += i + "    .height(10)\n";
		out += i + "    .right(0)\n";
		out += i + "    .text(function(d) d);\n\n";

		
		
		out += i + "/* Focus panel (zoomed in). */\n";
		out += i + "var focus = vis.add(pv.Panel)\n";
		out += i + "    .left(legendOffset)\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			seriesInitMethod[s] = "init_" + seriesVar[s];
			out += i + "    .def(\"" + seriesInitMethod[s] + "\", function() {\n";
			out += i + "        var d1 = x.invert(i.x),\n";
			out += i + "            d2 = x.invert(i.x + i.dx),\n";
			out += i + "            offsetsearch = pv.search.index("+seriesVar[s]+", d1, function(d) d.offset),\n";
			out += i + "            firstvisible = offsetsearch >= 0 ? offsetsearch : -(1+offsetsearch),\n";
			out += i + "            onsetsearch = pv.search.index("+seriesVar[s]+", d2, function(d) d.offset),\n";
			out += i + "            lastvisible = onsetsearch >= 0 ? onsetsearch : -(1+onsetsearch),\n";
			out += i + "	        dd = "+seriesVar[s]+".slice(firstvisible,lastvisible+1);\n";
			out += i + "	    fx.domain(d1, d2);\n";
			out += i + "	    return dd;\n";
			out += i + "      })\n";
		}
	    out += i + "    .top(hOffset)\n";
	    out += i + "    .height(h1);\n\n";
		

		out += i + "/* X-axis ticks. */\n";
		out += i + "focus.add(pv.Rule)\n";
		out += i + "    .data(function() fx.ticks())\n";
		out += i + "    .left(fx)\n";
		out += i + "    .strokeStyle(\"#eee\")\n";
		out += i + "  .anchor(\"bottom\").add(pv.Label)\n";
		out += i + "    .text(fx.tickFormat);\n\n";

		out += i + "/* Focus area chart. */\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			out += i + "focus.add(pv.Panel)\n";
			out += i + "    .overflow(\"hidden\")\n";
			out += i + "    .data(function(d) focus." + seriesInitMethod[s] + "())\n";
			out += i + "  .add(pv.Bar)\n";
			out += i + "    .left(function(d) fx(d.onset))\n";
			out += i + "    .width(function(d) fx(d.offset) - fx(d.onset))\n";
			out += i + "    .bottom(" + (3 + (33*s)) + ")\n";
			out += i + "    .height(30)\n";
			out += i + "    .fillStyle(colors[" + s + "])\n";
			out += i + "    .strokeStyle(\"black\")\n";
			out += i + "    .lineWidth(1)\n";
			out += i + "    .title(function(d) d.label)\n";
			out += i + "    .anchor(\"center\").add(pv.Label).text(function(d) d.label);\n\n";
		}
		
		out += i + "/* Context panel (zoomed out). */\n";
		out += i + "var context = vis.add(pv.Panel)\n";
		out += i + "    .left(legendOffset)\n";
		out += i + "    .overflow(\"hidden\")\n";
	    out += i + "    .bottom(0)\n";
		out += i + "    .height(h2);\n\n";

		out += i + "/* X-axis ticks. */\n";
		out += i + "context.add(pv.Rule)\n";
		out += i + "    .data(x.ticks())\n";
		out += i + "    .left(x)\n";
		out += i + "    .strokeStyle(\"#eee\")\n";
		out += i + "  .anchor(\"bottom\").add(pv.Label)\n";
		out += i + "    .text(x.tickFormat);\n\n";

		out += i + "/* Y-axis ticks. */\n";
		out += i + "context.add(pv.Rule)\n";
		out += i + "    .bottom(0);\n\n";
		
		out += i + "/* Context area chart. */\n";
		for (int s = 0; s< seriesNames.length; s++) {
			out += i + "context.add(pv.Panel)\n";
			out += i + "    .data(" + seriesVar[s] + ")\n";
			out += i + "    .left(function(d) x(d.onset))\n";
			out += i + "    .bottom(" + (3 + (13*s)) + ")\n";
			out += i + "    .height(10)\n";
			out += i + "    .strokeStyle(\"Black\")\n";
			out += i + "    .fillStyle(colors[" + s + "])\n";
			out += i + "    .lineWidth(1);\n\n";
		}

		out += i + "/* The selectable, draggable focus region. */\n";
		out += i + "context.add(pv.Panel)\n";
		out += i + "    .data([i])\n";
		out += i + "    .cursor(\"crosshair\")\n";
		out += i + "    .events(\"all\")\n";
		out += i + "    .event(\"mousedown\", pv.Behavior.select())\n";
		out += i + "    .event(\"select\", focus)\n";
		out += i + "  .add(pv.Bar)\n";
		out += i + "    .left(function(d) d.x)\n";
		out += i + "    .width(function(d) d.dx)\n";
		out += i + "    .fillStyle(\"rgba(255, 128, 128, .4)\")\n";
		out += i + "    .cursor(\"move\")\n";
		out += i + "    .event(\"mousedown\", pv.Behavior.drag())\n";
		out += i + "    .event(\"drag\", focus);\n\n";

		out += i + "vis.render();\n\n";

		out +=  "\t\t\t};\n";		
		
        out +=  "\t\t\t</script>\n" +
        		"\t\t</div>\n" +
        		"\t</div>\n";

		out +=  "\t<br><br>\n";
		System.out.println("done.");
        return out;
    }

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setSeries(Map<String,List<NemaSegment>> series) {
		this.series = series;
	}

	public Map<String,List<NemaSegment>> getSeries() {
		return series;
	}

}
