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

	public static final String INDENT = "\t\t\t";
	public static final DecimalFormat MS_FORMAT = new DecimalFormat("###.# ms");
	public static final DecimalFormat TIMESTAMP_FORMAT = new DecimalFormat("###.###");
	
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
    	System.out.println("generating segmentation plot HTML for: " + getName());
    	String out = "\t<a name=\"" + getName() + "\"></a>\n" +
        "\t<h4>" + getCaption();
		if (topLink){
		    out += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
		}
		out += "</h4>\n";
		
		int height = (3 + 33 * series.size()) + (15 * series.size()) + 20 + 2 + 20;
		
		out += 	"\t<div id=\"center\">\n" + 
		       	"\t\t<div style=\"width: 860px; height: " + height + "px;; padding: 2px; margin: 3px; border-width: 1px; border-color: black; border-style:solid;\">\n";
		
		String functionName = getName() + "_segment_plot";
		String[] seriesNames = series.keySet().toArray(new String[series.size()]);
        String[] seriesVar = new String[seriesNames.length];
        String[] seriesInitMethod = new String[seriesNames.length];
        
		out +=  "\t\t<script type=\"text/javascript+protovis\">\n" +
				"\t\t\tvar " + functionName + " = function() {\n";
		
		//setup data
        
        out +=  INDENT + "var start = " + startTime + ";\n" +
        		INDENT + "var end = " + endTime + ";\n\n" + 
        		
        		INDENT + "var numseries = " + seriesNames.length + ";\n" + 
        		
        		INDENT + "/* Scales and sizing. */\n" + 
			    INDENT + "var w = 810,\n" + 
			    INDENT + "    hOffset = 0,\n" + 
			    INDENT + "    legendOffset = 60,\n" + 
			    INDENT + "    h1 = 3 + 33 * numseries,\n" + 
			    INDENT + "    h2 = 15 * numseries,\n" + 
			    INDENT + "    x = pv.Scale.linear(start, end).range(0, w-legendOffset),\n" + 
			    INDENT + "    i = -1;\n\n" + 
			    
			    INDENT + "/* Root panel. */\n" + 
			    INDENT + "var vis = new pv.Panel()\n" + 
			    INDENT + "    .width(w)\n" + 
			    INDENT + "    .height(h1 + 20 + h2 + hOffset)\n" + 
			    INDENT + "    .bottom(20)\n" + 
			    INDENT + "    .left(30)\n" + 
			    INDENT + "    .right(20)\n" + 
			    INDENT + "    .top(5);\n\n" +
			    INDENT + "vis.render();";
        
        for (int s = 0; s < seriesNames.length; s++) {
        	seriesVar[s] = "data" + s;
			out += INDENT + "var " + seriesVar[s] + " = [\n";
			List<NemaSegment> data = series.get(seriesNames[s]);
			NemaSegment seg = null;
			for (Iterator<NemaSegment> it = data.iterator(); it.hasNext();) {
				seg = it.next();
				out += "{onset: " + TIMESTAMP_FORMAT.format(seg.getOnset()) + ", offset: " + TIMESTAMP_FORMAT.format(seg.getOffset()) + ", label: \"" + seg.getLabel() + "\"}";
				if(it.hasNext()){
					out +=",\n";
				}else{
					out += "\n" + INDENT + "];\n\n";
				}
			}
		}
        
        out +=  INDENT + "var allSeries = [";
        for (int s = 0; s < seriesVar.length-1; s++) {
        	out += seriesVar[s] + ",";
        }
        out += seriesVar[seriesVar.length-1] + "];\n" + 
	    		INDENT + "var seriesNames = [";
        for (int j = 0; j < seriesNames.length; j++) {
			out += "\"" + seriesNames[j] + "\"";
			if (j < seriesNames.length-1){
				out += ",";
			}
		}
        
        out += "];\n" + 
        		INDENT + "var colors = [\"salmon\", \"steelblue\", \"green\", \"pink\", \"navy\"];\n\n" + 
        		
        		INDENT + "return {\n" +
        		INDENT + "plot : function() {\n" + 
				
				INDENT + "/* Interaction state. Focus scales will have domain set on-render. */\n" + 
        	    INDENT + "var i = {x:0, dx:100},\n" + 
        	    INDENT + "    fx = pv.Scale.linear().range(0, w-legendOffset);\n\n" +
        	    
        	    INDENT + "/* Legend area. */\n" + 
        	    INDENT + "var legend = vis.add(pv.Panel)\n" + 
        	    INDENT + "    .left(0)\n" + 
        	    INDENT + "    .width(legendOffset)\n" + 
        	    INDENT + "    .bottom(function() (3 + this.index * 33))\n" + 
        	    INDENT + "    .top(0);\n" + 
        	    INDENT + "legend.add(pv.Label)\n" + 
        	    INDENT + "    .data(seriesNames)\n" + 
        	    INDENT + "    .textAlign(\"right\")\n" + 
        	    INDENT + "    .textBaseline(\"middle\")\n" + 
        	    INDENT + "    .top(function() 18+((numseries - (1+this.index)) * 33)) \n" + 
        	    INDENT + "    .height(10)\n" + 
        	    INDENT + "    .right(0)\n" + 
        	    INDENT + "    .text(function(d) d);\n\n" + 
        	    
        	    INDENT + "/* Focus panel (zoomed in). */\n" + 
        	    INDENT + "var focus = vis.add(pv.Panel)\n" + 
        	    INDENT + "    .left(legendOffset)\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			seriesInitMethod[s] = "init_" + seriesVar[s];
			out += INDENT + "    .def(\"" + seriesInitMethod[s] + "\", function() {\n" + 
        	    INDENT + "        var d1 = x.invert(i.x),\n" + 
        	    INDENT + "            d2 = x.invert(i.x + i.dx),\n" + 
        	    INDENT + "            offsetsearch = pv.search.index("+seriesVar[s]+", d1, function(d) d.offset),\n" + 
        	    INDENT + "            firstvisible = offsetsearch >= 0 ? offsetsearch : -(1+offsetsearch),\n" + 
        	    INDENT + "            onsetsearch = pv.search.index("+seriesVar[s]+", d2, function(d) d.offset),\n" + 
        	    INDENT + "            lastvisible = onsetsearch >= 0 ? onsetsearch : -(1+onsetsearch),\n" + 
        	    INDENT + "	        dd = "+seriesVar[s]+".slice(firstvisible,lastvisible+1);\n" + 
        	    INDENT + "	    fx.domain(d1, d2);\n" + 
        	    INDENT + "	    return dd;\n" + 
        	    INDENT + "      })\n";
		}
	    out +=  INDENT + "    .top(hOffset)\n" + 
        	    INDENT + "    .height(h1);\n\n" + 

        	    INDENT + "/* X-axis ticks. */\n" + 
        	    INDENT + "focus.add(pv.Rule)\n" + 
    	    	INDENT + "    .data(function() fx.ticks())\n" + 
        	    INDENT + "    .left(fx)\n" + 
        	    INDENT + "    .strokeStyle(\"#eee\")\n" + 
        	    INDENT + "  .anchor(\"bottom\").add(pv.Label)\n" + 
        	    INDENT + "    .text(fx.tickFormat);\n\n" + 
        	    
        	    INDENT + "/* Focus area chart. */\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			out += INDENT + "focus.add(pv.Panel)\n" + 
        	    INDENT + "    .overflow(\"hidden\")\n" + 
        	    INDENT + "    .data(function(d) focus." + seriesInitMethod[s] + "())\n" + 
        	    INDENT + "  .add(pv.Bar)\n" +
        	    INDENT + "    .overflow(\"hidden\")\n" + 
        	    INDENT + "    .left(function(d) fx(d.onset))\n" + 
        	    INDENT + "    .width(function(d) fx(d.offset) - fx(d.onset))\n" + 
        	    INDENT + "    .bottom(" + (3 + (33*s)) + ")\n" + 
        	    INDENT + "    .height(30)\n" + 
        	    INDENT + "    .fillStyle(colors[" + s + "])\n" + 
        	    INDENT + "    .strokeStyle(\"black\")\n" + 
        	    INDENT + "    .lineWidth(1)\n" + 
        	    INDENT + "    .title(function(d) d.label)\n" + 
        	    INDENT + "    .anchor(\"left\").add(pv.Label).text(function(d) d.label);\n\n";
		}
		
		out += INDENT + "/* Context panel (zoomed out). */\n" + 
        	    INDENT + "var context = vis.add(pv.Panel)\n" + 
        	    INDENT + "    .left(legendOffset)\n" + 
        	    INDENT + "    .overflow(\"hidden\")\n" + 
        	    INDENT + "    .bottom(0)\n" + 
        	    INDENT + "    .height(h2);\n\n" + 

    	    	INDENT + "/* X-axis ticks. */\n" + 
        	    INDENT + "context.add(pv.Rule)\n" + 
        	    INDENT + "    .data(x.ticks())\n" + 
        	    INDENT + "    .left(x)\n" + 
        	    INDENT + "    .strokeStyle(\"#eee\")\n" + 
        	    INDENT + "  .anchor(\"bottom\").add(pv.Label)\n" + 
        	    INDENT + "    .text(x.tickFormat);\n\n" + 
        	    
        	    INDENT + "/* Y-axis ticks. */\n" + 
        	    INDENT + "context.add(pv.Rule)\n" + 
        	    INDENT + "    .bottom(0);\n\n" + 
		
                INDENT + "/* Context area chart. */\n";
		for (int s = 0; s< seriesNames.length; s++) {
			out += INDENT + "context.add(pv.Panel)\n" + 
        	    INDENT + "    .data(" + seriesVar[s] + ")\n" + 
        	    INDENT + "    .left(function(d) x(d.onset))\n" + 
        	    INDENT + "    .bottom(" + (3 + (13*s)) + ")\n" + 
        	    INDENT + "    .height(10)\n" + 
        	    INDENT + "    .strokeStyle(\"Black\")\n" + 
        	    INDENT + "    .fillStyle(colors[" + s + "])\n" + 
        	    INDENT + "    .lineWidth(1);\n\n";
		}

		out += INDENT + "/* The selectable, draggable focus region. */\n" + 
        	    INDENT + "context.add(pv.Panel)\n" + 
        	    INDENT + "    .data([i])\n" + 
        	    INDENT + "    .cursor(\"crosshair\")\n" + 
        	    INDENT + "    .events(\"all\")\n" + 
        	    INDENT + "    .event(\"mousedown\", pv.Behavior.select())\n" + 
        	    INDENT + "    .event(\"select\", focus)\n" + 
        	    INDENT + "  .add(pv.Bar)\n" + 
        	    INDENT + "    .left(function(d) d.x)\n" + 
        	    INDENT + "    .width(function(d) d.dx)\n" + 
        	    INDENT + "    .fillStyle(\"rgba(255, 128, 128, .4)\")\n" + 
        	    INDENT + "    .cursor(\"move\")\n" + 
        	    INDENT + "    .event(\"mousedown\", pv.Behavior.drag())\n" + 
        	    INDENT + "    .event(\"drag\", focus);\n\n" + 
        	    
        	    INDENT + "vis.render();\n\n" + 
        	    "\t\t\t\t\t}\n" + 
        	    "\t\t\t\t};\n" + 
    	    	"\t\t\t}();\n" + 	
				"\t\t\t</script>\n\n" +
				"\t\t\t</div>\n" + 
				//add button to trigger plot function
				"\t\t\t<div style=\"text-align:left;padding-left:10px;\">\n" +
				"\t\t\t\t<input type=\"button\" value=\"Plot\" id=\"run_" + functionName + "\" onClick=\"" + functionName + ".plot();this.disabled=true;\">\n" +
				"\t\t\t\t<label for=\"srun_" + functionName + "\">Click here to plot the figure</label>\n" +
				"\t\t\t</div>\n" + 
				"\t\t</div>\n";
        
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
