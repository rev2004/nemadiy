/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.resultpages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.model.NemaDataConstants;

/**
 * A PageItem implementation for plotting one of more functions over a set of
 * fixed timesteps. Options are provided to downsample the function plotted.
 * 
 * Implements Javascript rendering using Protovis.
 * 
 * @author kris.west@gmail.com
 */
public class ProtovisFunctionTimestepPlotItem extends PageItem{

	public static final DecimalFormat MS_FORMAT = new DecimalFormat("###.# ms");
	
	private double startTime;
    private double endTime;
    private Map<String,double[][]> series; //indices are 0=x, 1=y
    
    private double resolutionInSecs;

    public ProtovisFunctionTimestepPlotItem(String name, String caption, 
    		double startTime, double endTime, double currentResolutionSecs,
    		double targetResolutionSecs, Map<String,double[][]> series){
    	
        super(name,caption);
        setStartTime(startTime);
        setEndTime(endTime);
        setResolutionInSecs(currentResolutionSecs);
        setSeries(reduceFunctionResolution(getResolutionInSecs(),targetResolutionSecs,series));
    }
    
    private Map<String,double[][]> reduceFunctionResolution(double currentIncrement, double targetIncrement, Map<String,double[][]> series){
    	if(currentIncrement >= targetIncrement){
    		setResolutionInSecs(currentIncrement);
    		System.out.println("Not reducing function from resolution to ");
    		//no need to reduce - should be safe to return original as downstream processing is not expected modify it
    		return series;
    	}else{
    		//Return new map with modified data
    		Map<String,double[][]> out = new HashMap<String, double[][]>(series.size());
    		
    		String[] seriesNames = series.keySet().toArray(new String[series.size()]);
    		for (int s = 0; s < seriesNames.length; s++) {
    			double[][] data = series.get(seriesNames[s]);
	    		
    			/* 
    			 * Set up the 0th-order interpolation to convert to the 
    			 * target time-grid 
    			 */
    			List<Double> interpTimeStamp = new ArrayList<Double>();
    			List<Double> interpValue = new ArrayList<Double>();
    			int nrows = data.length;
	    		
    			/* Indices into the new, interpolated data array-list */
    	        int index = 0;
    	        int oldindex = 0;

    	        /*
    	         *  minDiff and currDiff represent time-stamp differences to make 
    	         *  sure the value we use in the original data is the one 
    	         *  closest-in-time to the desired time-stamp
    	         */
    	        double minDiff = 10000000.0;
    	        double currDiff = 0.0;
    	        
    	        //init with first values
    	        interpTimeStamp.add(data[0][0]);
    	        interpValue.add(data[0][1]);
    	        
    	        /* Loop through original arbitrary time-stamped data */
    	        for (int i = 1; i < nrows; i++) {
    	            index = (int)Math.round(data[i][0]/targetIncrement);
    	            
    	            /* Case where the file's time-step is less than targetIncrement */
    	            if (index == oldindex) {
    	                currDiff = Math.abs(data[i][0] - targetIncrement*(double)index);
    	                if (currDiff < minDiff) {	
    	                	interpValue.set(index, new Double(data[i][1]));
    	                	interpTimeStamp.set(index, new Double(targetIncrement*(double)index));
    	                    minDiff = currDiff;
    	                }
    	            }
    	            
    	         	/*
    	         	 *  Case where the file's time-step is targetIncrement or has 'caught up' if 
    	         	 *  less than target increment and gone on to the next index in the targetIncrement grid
    	         	 */
    	            else if (index == oldindex + 1) {
    	            	interpValue.add(new Double(data[i][1]));
    	            	interpTimeStamp.add(new Double(targetIncrement*(double)index));
    	                minDiff = Math.abs(data[i][0] - targetIncrement*(double)index);
    	            }
    	            
    	            oldindex = index;                                
    	        }   
    			
    	        /*
    	         *  Put the contents of the Time-stamp and values array-lists into a 
    	         *  single 2 column 2d-double array 
    	         */
    	        double[][] interpolatedData = new double[interpValue.size()][2];
    	        for (int i = 0; i < interpolatedData.length; i++) {
    	        	interpolatedData[i][0] = (interpTimeStamp.get(i)).doubleValue();
    	        	interpolatedData[i][1] = (interpValue.get(i)).doubleValue();
    	        }
    			
    	        out.put(seriesNames[s], interpolatedData);
    			
    		}
    		setResolutionInSecs(targetIncrement);
    		return out;
    		
    	}
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
    	String out = "\t<a name=\"" + getName() + "\"></a>\n" +
        "\t<h4>" + getCaption();
		if (topLink){
		    out += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
		}
		out += "</h4>\n";
        out += 	"\t<div id=\"center\">\n" +
        		"\t\t<div style=\"width: 860px; height: 416px;\">\n" +
        		"\t\t\t<div style=\"text-align:right;padding-right:20px;\">\n" +
        		"\t\t\t\t<input checked id=\"scale_" + getName() + "\" type=\"checkbox\" onchange=\"vis.render()\">\n" +
        		"\t\t\t\t<label for=\"scale_" + getName() + "\">Scale to fit</label>\n" +
        		"\t\t\t</div>\n" + 
        		"\t\t\t<script type=\"text/javascript+protovis\">\n";
        
        out +=  "\t\t\tvar " + getName() + " = new function() {\n";
        
        //setup data
        String i = "\t\t\t\t";
        out +=  i + "var start = " + startTime + ";\n" +
        		//round up end time
        		i + "var end = " + (Math.ceil(endTime / resolutionInSecs) * resolutionInSecs) + ";\n\n";
        
        String[] seriesNames = series.keySet().toArray(new String[series.size()]);
        String[] seriesVar = new String[seriesNames.length];
        String[] seriesInitMethod = new String[seriesNames.length];
        
        for (int s = 0; s < seriesNames.length; s++) {
        	seriesVar[s] = "data" + s;
			out += i + "var " + seriesVar[s] + " = [\n";
			double[][] data = series.get(seriesNames[s]);
			for (int j = 0; j < data.length-1; j++) {
				out += "{x: " + data[j][0] + ", y: " + data[j][1] + "},\n";
			}
			out += "{x: " + data[data.length-1][0] + ", y: " + data[data.length-1][1] + "}\n";
			out += i + "];\n\n";
		}
        
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
        		
        out += i + "var colors = [\"salmon\", \"steelblue\", \"khakie\", \"green\", \"navy\"];\n\n";//

		out += i + "/* Scales and sizing. */\n";
		out += i + "var w = 810,\n";
		out += i + "    hOffset = 26,\n";
		out += i + "    h1 = 300,\n";
		out += i + "    h2 = 30,\n";
		out += i + "    x = pv.Scale.linear(start, end).range(0, w),\n";
		//out += i + "    y = pv.Scale.linear(0, pv.max(pv.blend(allSeries), function(d) d.y)).range(0, h2);\n";
		out += i + "    y = pv.Scale.linear(0, pv.max(" + seriesVar[0] + ", function(d) d.y)).range(0, h2);\n";
		out += i + "    i = -1;\n\n";

		out += i + "/* Interaction state. Focus scales will have domain set on-render. */\n";
		out += i + "var i = {x:200, dx:100},\n";
		out += i + "    fx = pv.Scale.linear().range(0, w),\n";
		out += i + "    fy = pv.Scale.linear().range(0, h1);\n\n";

		out += i + "/* Root panel. */\n";
		out += i + "var vis = new pv.Panel()\n";
		out += i + "    .width(w)\n";
		out += i + "    .height(h1 + 20 + h2 + hOffset)\n";
		out += i + "    .bottom(20)\n";
		out += i + "    .left(30)\n";
		out += i + "    .right(20)\n";
		out += i + "    .top(5);\n\n";

		out += i + "/* Focus panel (zoomed in). */\n";
		out += i + "var focus = vis.add(pv.Panel)\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			seriesInitMethod[s] = "init_" + seriesVar[s];
			out += i + "    .def(\"" + seriesInitMethod[s] + "\", function() {\n";
			out += i + "        var d1 = x.invert(i.x),\n";
			out += i + "            d2 = x.invert(i.x + i.dx),\n";
			out += i + "            dd = " + seriesVar[s] + ".slice(\n";
			out += i + "                Math.max(0, pv.search.index(" + seriesVar[s] + ", d1, function(d) d.x) - 1),\n";
			out += i + "                pv.search.index(" + seriesVar[s] + ", d2, function(d) d.x) + 1);\n";
			out += i + "        fx.domain(d1, d2);\n";
			out += i + "        fy.domain(document.getElementById(\"scale_" + getName() + "\").checked ? [0, pv.max(dd, function(d) d.y)] : y.domain());\n";
			out += i + "        return dd;\n";
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

		out += i + "/* Y-axis ticks. */\n";
		out += i + "focus.add(pv.Rule)\n";
		out += i + "    .data(function() fy.ticks(7))\n";
		out += i + "    .bottom(fy)\n";
		out += i + "    .strokeStyle(function(d) d ? \"#aaa\" : \"#000\")\n";
		out += i + "  .anchor(\"left\").add(pv.Label)\n";
		out += i + "    .text(fy.tickFormat);\n\n";

		out += i + "/* Focus area chart. */\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			out += i + "focus.add(pv.Panel)\n";
			out += i + "    .overflow(\"hidden\")\n";
			out += i + "  .add(pv.Line)\n";
			out += i + "    .data(function(d) focus." + seriesInitMethod[s] + "())\n";
			out += i + "    .left(function(d) fx(d.x))\n";
			out += i + "    .bottom(1)\n";
			out += i + "    .height(function(d) fy(d.y))\n";
			out += i + "  .anchor(\"top\").add(pv.Line)\n";
			out += i + "    .fillStyle(null)\n";
			out += i + "    .strokeStyle(colors[" + s + "])\n";
			out += i + "    .lineWidth(2);\n\n";
		}
		
		out += i + "/* Context panel (zoomed out). */\n";
		out += i + "var context = vis.add(pv.Panel)\n";
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
		
		out += i + "//Add a note on resolution\n";
		out += i + "vis.add(pv.Label)\n";
		out += i + "   .right(10)\n";
		out += i + "   .top(12)\n";
		out += i + "   .textAlign(\"right\")\n";
		out += i + "   .text(\"Resolution: " + MS_FORMAT.format(resolutionInSecs*1000.0) + "\");\n\n";
		
		out += i + "// Add the legend\n";
		out += i + "vis.add(pv.Dot)\n";
		out += i + "   .data(seriesNames)\n";
		out += i + "   .left(10)\n";
		out += i + "   .top(function() this.index * 12)\n";
		out += i + "   .height(10)\n";
		out += i + "   .width(10)\n";
		out += i + "   .strokeStyle(null)\n";
		out += i + "   .fillStyle(function() colors[this.index])\n";
		out += i + " .anchor(\"right\").add(pv.Label);\n\n";

		out += i + "/* Context area chart. */\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			out += i + "context.add(pv.Line)\n";
			out += i + "    .data(" + seriesVar[s] + ")\n";
			out += i + "    .overflow(\"hidden\")\n";
			out += i + "    .left(function(d) x(d.x))\n";
			out += i + "    .bottom(1)\n";
			out += i + "    .height(function(d) y(d.y))\n";
			out += i + "  .anchor(\"top\").add(pv.Line)\n";
			out += i + "    .strokeStyle(colors[" + s + "])\n";
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

	public void setSeries(Map<String,double[][]> series) {
		this.series = series;
	}

	public Map<String,double[][]> getSeries() {
		return series;
	}

	public void setResolutionInSecs(double resolutionInSecs) {
		this.resolutionInSecs = resolutionInSecs;
	}

	public double getResolutionInSecs() {
		return resolutionInSecs;
	}


}
