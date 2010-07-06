/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.resultpages;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * A PageItem implementation for plotting one or more sequences of onset times
 * on a timeline.
 * 
 * Implements Javascript rendering using Protovis.
 * 
 * @author kris.west@gmail.com
 * @since 0.3.0
 */
public class ProtovisOnsetPlotItem extends PageItem{

	public static final String INDENT = "\t\t\t";
	public static final DecimalFormat MS_FORMAT = new DecimalFormat("###.# ms");
	public static final DecimalFormat TIMESTAMP_FORMAT = new DecimalFormat("###.###");
	
	private double startTime;
    private double endTime;
    private Map<String,double[]> series;
    private List<String> seriesNames; 
    
    public ProtovisOnsetPlotItem(String name, String caption, 
    		double startTime, double endTime, 
    		Map<String,double[]> series, List<String> seriesNames){
    	
        super(name,caption);
        setStartTime(startTime);
        setEndTime(endTime);
        setSeries(series);
        setSeriesNames(seriesNames);
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
    	System.out.println("generating onset plot HTML for: " + getName());
    	String out = "\t<a name=\"" + getName() + "\"></a>\n" +
        "\t<h4>" + getCaption();
		if (topLink){
		    out += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
		}
		out += "</h4>\n";
		
		int height = (3 + 12 * series.size()) + (15 + 5 * series.size()) + 20 + 4;
		
		out += 	"\t<div id=\"center\">\n" + 
		       	"\t\t<div style=\"width: 860px; height: " + height + "px; padding: 2px; margin: 3px; border-width: 1px; border-color: black; border-style:solid;\">\n";
		
		String functionName = getName() + "_onset_plot";
		String[] seriesNames = this.seriesNames.toArray(new String[series.size()]);

        
		out +=  "\t\t<script type=\"text/javascript+protovis\">\n" +
				"\t\t\tvar " + functionName + " = function() {\n";
		
		//setup data
        
        out +=  INDENT + "var start = " + startTime + ";\n" +
        		INDENT + "var end = " + endTime + ";\n" + 
        		INDENT + "var numseries = " + seriesNames.length + ";\n" + 
        		
        		INDENT + "/* Scales and sizing. */\n" + 
			    INDENT + "var w = 810,\n" + 
			    INDENT + "    hOffset = 0,\n" + 
			    INDENT + "    legendOffset = 60,\n" + 
			    INDENT + "    h1 = 3 + 12 * numseries,\n" + 
			    INDENT + "    h2 = 15 + 5 * numseries,\n" +
			    INDENT + "    totalHeight = h1 + 20 + h2 + hOffset;\n\n" + 
			    
			    INDENT + "/* Root panel. */\n" + 
			    INDENT + "var vis = new pv.Panel()\n" + 
			    INDENT + "    .width(w)\n" + 
			    INDENT + "    .height(totalHeight)\n" + 
			    INDENT + "    .bottom(20)\n" + 
			    INDENT + "    .left(30)\n" + 
			    INDENT + "    .right(20)\n" + 
			    INDENT + "    .top(5);\n\n" +
			    INDENT + "vis.render();\n\n";
        
        	out += INDENT + "var data = [\n";
        	for (int s = 0; s < seriesNames.length; s++) {
        		out += "[";
    			double[] data = series.get(seriesNames[s]);
				for (int i=0;i<data.length;i++) {
					out += TIMESTAMP_FORMAT.format(data[i]);
					if(i<data.length-1){
						out +=", ";
					}
				}
				if(s<seriesNames.length-1){
					out +="],\n";
				}else{
					out +="]\n";
				}
        	}
        	out += "];\n\n" + 
        
        	
	    		INDENT + "var seriesNames = [";
        for (int j = 0; j < seriesNames.length; j++) {
			out += "\"" + seriesNames[j] + "\"";
			if (j < seriesNames.length-1){
				out += ",";
			}
		}
        
        out += "];\n" + 
				INDENT + "var predictionColor = \"salmon\";\n" + 
				INDENT + "var gtColor = \"steelblue\";\n\n" + 
        		
				INDENT + "function get_color(i){\n" + 
				INDENT + "    if(i==0){\n" + 
				INDENT + "        return predictionColor;\n" + 
				INDENT + "    }else{\n" + 
				INDENT + "        return gtColor;\n" + 
				INDENT + "    }\n" + 
				INDENT + "}\n\n" + 
				
        		INDENT + "return {\n" +
        		INDENT + "plot : function() {\n" + 
        		
				INDENT + "/* Interaction state. Focus scales will have domain set on-render. */\n" + 
        	    INDENT + "var x = pv.Scale.linear(start, end).range(0, w-legendOffset),\n" + 
        	    INDENT + "    i = {x:0, dx:100},\n" + 
        	    INDENT + "    fx = pv.Scale.linear().range(0, w-legendOffset);\n\n" +
        	    
        	  

        	    INDENT + "/* Legend area. */\n" + 
        	    INDENT + "var legend = vis.add(pv.Panel)\n" + 
        	    INDENT + "    .left(0)\n" + 
        	    INDENT + "    .width(legendOffset)\n" + 
        	    INDENT + "    .height(totalHeight)\n" + 
        	    INDENT + "    .top(0);\n\n" + 
        	    INDENT + "legend.add(pv.Label)\n" + 
        	    INDENT + "    .data(seriesNames)\n" + 
        	    INDENT + "    .textAlign(\"right\")\n" + 
        	    INDENT + "    .textBaseline(\"middle\")\n" + 
        	    INDENT + "    .top(function() 8+((numseries - (1+this.index)) * 12)) \n" + 
        	    INDENT + "    .height(10)\n" + 
        	    INDENT + "    .right(0)\n" + 
        	    INDENT + "    .text(function(d) d);\n\n" + 
        	    INDENT + "legend.add(pv.Label)\n" + 
        	    INDENT + "    .text('time (secs)')\n" + 
        	    INDENT + "    .textAlign(\"right\")\n" + 
        	    INDENT + "    .height(10)\n" + 
        	    INDENT + "    .right(5)\n" + 
        	    INDENT + "    .textBaseline(\"top\")\n" + 
        	    INDENT + "    .bottom(15);\n\n" + 
        		INDENT + "legend.add(pv.Label)\n" + 
        		INDENT + "    .text('time (secs)')\n" + 
        		INDENT + "    .textAlign(\"right\")\n" + 
        		INDENT + "    .height(10)\n" + 
        		INDENT + "    .right(5)\n" + 
        		INDENT + "    .textBaseline(\"top\")\n" + 
        		INDENT + "    .top(hOffset + h1);\n\n" + 
        	    
        	    
        	    INDENT + "/* Focus panel (zoomed in). */\n" + 
        	    INDENT + "var focus = vis.add(pv.Panel)\n" + 
        	    INDENT + "    .left(legendOffset)\n" +
        	    INDENT + "    .top(hOffset)\n" +
        	    INDENT + "    .height(h1)\n" +
        	    INDENT + "    .def(\"init_data\", function() {\n" + 
        	    INDENT + "        var out = new Array(numseries);\n" + 
        	    INDENT + "        var d1 = x.invert(i.x),\n" + 
        	    INDENT + "            d2 = x.invert(i.x + i.dx);\n" + 
        	    INDENT + "        for(s=0;s<numseries;s=s+1){\n" + 
        	    INDENT + "            offsetsearch = pv.search.index(data[s], d1, function(d) d),\n" + 
        	    INDENT + "            firstvisible = offsetsearch >= 0 ? offsetsearch : -(1+offsetsearch),\n" + 
        	    INDENT + "            onsetsearch = pv.search.index(data[s], d2, function(d) d),\n" + 
        	    INDENT + "            lastvisible = onsetsearch >= 0 ? onsetsearch : -(1+onsetsearch),\n" + 
        	    INDENT + "	          out[s] = data[s].slice(firstvisible,lastvisible+1);\n" + 
        	    INDENT + "	      }\n" + 
        	    INDENT + "	      fx.domain(d1, d2);\n" + 
        	    INDENT + "	      return out;\n" + 
        	    INDENT + "    });\n" + 
		
        
        	    INDENT + "/* X-axis ticks. */\n" + 
        	    INDENT + "focus.add(pv.Rule)\n" + 
    	    	INDENT + "    .data(function() fx.ticks())\n" + 
        	    INDENT + "    .left(fx)\n" + 
        	    INDENT + "    .strokeStyle(\"#eee\")\n" + 
        	    INDENT + "  .anchor(\"bottom\").add(pv.Label)\n" + 
        	    INDENT + "    .text(fx.tickFormat);\n\n" + 
        	    
        	    INDENT + "/* Focus area chart. */\n" + 
		
		
        	    INDENT + "focus.add(pv.Panel)\n" + 
        	    INDENT + "    .overflow(\"hidden\")\n" + 
        	    INDENT + "    .data(function(d) focus.init_data())\n" + 
        	    INDENT + "  .add(pv.Dot)\n" +
        	    INDENT + "    .data(function(array) array)\n" + 
        	    INDENT + "    .overflow(\"hidden\")\n" + 
        	    INDENT + "    .left(function(d) fx(d))\n" + 
        	    INDENT + "    .bottom(function() 7 + (12*this.parent.index))\n" + 
        	    INDENT + "    .size(3)\n" + 
        	    INDENT + "    .strokeStyle(function() get_color(this.parent.index))\n" + 
        	    INDENT + "    .fillStyle(function() this.strokeStyle().alpha(.2))\n" + 
        	    INDENT + "    .title(function(d) d.toFixed(2));\n\n" + 
		
		
        	    INDENT + "/* Context panel (zoomed out). */\n" + 
        	    INDENT + "var context = vis.add(pv.Panel)\n" + 
        	    INDENT + "    .left(legendOffset)\n" + 
        	    INDENT + "    .bottom(0)\n" + 
        	    INDENT + "    .height(h2);\n\n" + 

    	    	INDENT + "/* X-axis ticks. */\n" + 
        	    INDENT + "context.add(pv.Rule)\n" + 
        	    INDENT + "    .data(x.ticks())\n" + 
        	    INDENT + "    .bottom(15)\n" + 
        	    INDENT + "    .left(x)\n" + 
        	    INDENT + "    .strokeStyle(\"#eee\")\n" + 
        	    INDENT + "  .anchor(\"bottom\").add(pv.Label)\n" + 
        	    INDENT + "    .text(x.tickFormat);\n\n" + 
        	    
        	    INDENT + "/* Y-axis ticks. */\n" + 
        	    INDENT + "context.add(pv.Rule)\n" + 
        	    INDENT + "    .bottom(15);\n\n" + 
        	    
        	    INDENT + "focus.add(pv.Rule)\n" +
        	    INDENT + "    .bottom(0);\n" +
		
                INDENT + "/* Context area chart. */\n" + 
			    INDENT + "context.add(pv.Panel)\n" + 
			    INDENT + "    .data(data)\n" + 
			    INDENT + "    .add(pv.Dot)\n" + 
			    INDENT + "        .data(function(array) array)\n" + 
        	    INDENT + "        .left(function(d) x(d))\n" + 
        	    INDENT + "        .bottom(function(s) 18 + (this.parent.index * 5))\n" + 
        	    INDENT + "        .height(5)\n" + 
        	    INDENT + "        .strokeStyle(function() get_color(this.parent.index))\n" + 
        	    INDENT + "        .size(1);\n\n" + 
		

		        INDENT + "/* The selectable, draggable focus region. */\n" + 
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

	public void setSeries(Map<String,double[]> series) {
		this.series = series;
	}

	public Map<String,double[]> getSeries() {
		return series;
	}


	public void setSeriesNames(List<String> seriesNames) {
		this.seriesNames = seriesNames;
	}


	public List<String> getSeriesNames() {
		return seriesNames;
	}

}
