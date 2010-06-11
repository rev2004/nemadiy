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

	public static final String INDENT = "\t\t\t";
	public static final DecimalFormat MS_FORMAT = new DecimalFormat("###.# ms");
	public static final DecimalFormat TIMESTAMP_FORMAT = new DecimalFormat("###.###");
	
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
    	System.out.println("generating line plot HTML for: " + getName());
    	String out = "\t<a name=\"" + getName() + "\"></a>\n" +
        "\t<h4>" + getCaption();
		if (topLink){
		    out += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
		}
		out += "</h4>\n";
		
		String functionName = getName() + "_line_plot";
        String[] seriesNames = series.keySet().toArray(new String[series.size()]);
        String[] seriesVar = new String[seriesNames.length];
        String[] seriesInitMethod = new String[seriesNames.length];
        
        out += 	"\t<div id=\"center\">\n" +
        		"\t\t<div style=\"width: 864px; height: 370px; padding: 2px; margin: 3px; border-width: 1px; border-color: black; border-style:solid;\">\n" +
        		"\t\t\t<script type=\"text/javascript+protovis\">\n";
        
        out +=  "\t\t\tvar " + functionName + " = new function() {\n";
        
        //setup data
        out +=  INDENT + "var start = " + startTime + ";\n" +
        		//round up end time
        		INDENT + "var end = " + (Math.ceil(endTime / resolutionInSecs) * resolutionInSecs) + ";\n\n";
        
        out +=  INDENT + "/* Scales and sizing. */\n" + 
        		INDENT + "var w = 810,\n" + 
        		INDENT + "    hOffset = 26,\n" + 
        		INDENT + "    h1 = 250,\n" + 
        		INDENT + "    h2 = 30;\n\n" +
        		
        		INDENT + "/* Root panel. */\n" + 
        		INDENT + "var vis = new pv.Panel()\n" + 
        		INDENT + "    .width(w)\n" + 
        		INDENT + "    .height(h1 + 20 + h2 + hOffset)\n" + 
        		INDENT + "    .bottom(20)\n" + 
        		INDENT + "    .left(30)\n" + 
        		INDENT + "    .right(20)\n" + 
        		INDENT + "    .top(5);\n\n" +
			    INDENT + "vis.render();\n\n";

        
        for (int s = 0; s < seriesNames.length; s++) {
        	seriesVar[s] = "data" + s;
			out += INDENT + "var " + seriesVar[s] + " = [\n";
			double[][] data = series.get(seriesNames[s]);
			for (int j = 0; j < data.length-1; j++) {
				out += "{x: " + TIMESTAMP_FORMAT.format(data[j][0]) + ", y: " + data[j][1] + "},\n";
			}
			out += "{x: " + data[data.length-1][0] + ", y: " + data[data.length-1][1] + "}\n" + 
				   INDENT + "];\n\n";
		}
        
        out += INDENT + "var allSeries = [";
        for (int s = 0; s < seriesVar.length-1; s++) {
        	out += seriesVar[s] + ",";
        }
        out += seriesVar[seriesVar.length-1] + "];\n";
        
        out += INDENT + "var seriesNames = [";
        for (int j = 0; j < seriesNames.length; j++) {
			out += "\"" + seriesNames[j] + "\"";
			if (j < seriesNames.length-1){
				out += ",";
			}
		}
        out += "];\n";
        		
        out +=  INDENT + "var colors = [\"salmon\", \"steelblue\", \"khakie\", \"green\", \"navy\"];\n\n" + 
        		INDENT + "var scaleToFit = true;\n\n" + 
        
        
        		INDENT + "return {\n" + 
        		INDENT + "toggleScaling : function() { scaleToFit = !scaleToFit;vis.render(); },\n" + 
        		INDENT + "plot : function() {\n" + 
        		INDENT + "/* Context scales. . */\n" + 
        		INDENT + "    x = pv.Scale.linear(start, end).range(0, w),\n" + 
        		INDENT + "    y = pv.Scale.linear(0, pv.max(" + seriesVar[0] + ", function(d) d.y)).range(0, h2);\n" +  
        		
        		INDENT + "/* Interaction scales. Focus scales will have domain set on-render. */\n" + 
        		INDENT + "var i = {x:0, dx:100},\n" + 
        		INDENT + "    fx = pv.Scale.linear().range(0, w),\n" + 
        		INDENT + "    fy = pv.Scale.linear().range(0, h1);\n\n" + 
        		
        		INDENT + "/* Focus panel (zoomed in). */\n" + 
        		INDENT + "var focus = vis.add(pv.Panel)\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			seriesInitMethod[s] = "init_" + seriesVar[s];
			out +=  INDENT + "    .def(\"" + seriesInitMethod[s] + "\", function() {\n" + 
	        		INDENT + "        var d1 = x.invert(i.x),\n" + 
	        		INDENT + "            d2 = x.invert(i.x + i.dx),\n" + 
	        		INDENT + "            dd = " + seriesVar[s] + ".slice(\n" + 
	        		INDENT + "                Math.max(0, pv.search.index(" + seriesVar[s] + ", d1, function(d) d.x) - 1),\n" + 
	        		INDENT + "                pv.search.index(" + seriesVar[s] + ", d2, function(d) d.x) + 1);\n" + 
	        		INDENT + "        fx.domain(d1, d2);\n" + 
	        		INDENT + "        fy.domain(scaleToFit ? [0, pv.max(dd, function(d) d.y)] : y.domain());\n" + 
	        		INDENT + "        return dd;\n" + 
	        		INDENT + "      })\n";
		}
	    out +=  INDENT + "    .top(hOffset)\n" + 
        		INDENT + "    .height(h1);\n\n";
		

		out +=  INDENT + "/* X-axis ticks. */\n" + 
        		INDENT + "focus.add(pv.Rule)\n" + 
        		INDENT + "    .data(function() fx.ticks())\n" + 
        		INDENT + "    .left(fx)\n" + 
        		INDENT + "    .strokeStyle(\"#eee\")\n" + 
        		INDENT + "  .anchor(\"bottom\").add(pv.Label)\n" + 
        		INDENT + "    .text(fx.tickFormat);\n\n";

		out +=  INDENT + "/* Y-axis ticks. */\n" + 
        		INDENT + "focus.add(pv.Rule)\n" + 
        		INDENT + "    .data(function() fy.ticks(7))\n" + 
        		INDENT + "    .bottom(fy)\n" + 
        		INDENT + "    .strokeStyle(function(d) d ? \"#aaa\" : \"#000\")\n" + 
        		INDENT + "  .anchor(\"left\").add(pv.Label)\n" + 
        		INDENT + "    .text(fy.tickFormat);\n\n";

		out +=  INDENT + "/* Focus area chart. */\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			out +=  INDENT + "focus.add(pv.Panel)\n" + 
	        		INDENT + "    .overflow(\"hidden\")\n" + 
	        		INDENT + "  .add(pv.Line)\n" + 
	        		INDENT + "    .data(function(d) focus." + seriesInitMethod[s] + "())\n" + 
	        		INDENT + "    .left(function(d) fx(d.x))\n" + 
	        		INDENT + "    .bottom(1)\n" + 
	        		INDENT + "    .height(function(d) fy(d.y))\n" + 
	        		INDENT + "  .anchor(\"top\").add(pv.Line)\n" + 
	        		INDENT + "    .fillStyle(null)\n" + 
	        		INDENT + "    .strokeStyle(colors[" + s + "])\n" + 
	        		INDENT + "    .lineWidth(2);\n\n";
		}
		
		out += INDENT + "/* Context panel (zoomed out). */\n" + 
        		INDENT + "var context = vis.add(pv.Panel)\n" + 
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
        		
        		INDENT + "//Add a note on resolution\n" + 
        		INDENT + "vis.add(pv.Label)\n" + 
        		INDENT + "   .right(10)\n" + 
        		INDENT + "   .top(12)\n" + 
        		INDENT + "   .textAlign(\"right\")\n" + 
        		INDENT + "   .text(\"Resolution: " + MS_FORMAT.format(resolutionInSecs*1000.0) + "\");\n\n" + 
        		
        		INDENT + "// Add the legend\n" + 
        		INDENT + "vis.add(pv.Dot)\n" + 
        		INDENT + "   .data(seriesNames)\n" + 
        		INDENT + "   .left(10)\n" + 
        		INDENT + "   .top(function() this.index * 12)\n" + 
        		INDENT + "   .height(10)\n" + 
        		INDENT + "   .width(10)\n" + 
        		INDENT + "   .strokeStyle(null)\n" + 
        		INDENT + "   .fillStyle(function() colors[this.index])\n" + 
        		INDENT + " .anchor(\"right\").add(pv.Label);\n\n" + 
        		
        		INDENT + "/* Context area chart. */\n";
		
		for (int s = 0; s< seriesNames.length; s++) {
			out += INDENT + "context.add(pv.Line)\n" + 
        		INDENT + "    .data(" + seriesVar[s] + ")\n" + 
        		INDENT + "    .overflow(\"hidden\")\n" + 
        		INDENT + "    .left(function(d) x(d.x))\n" + 
        		INDENT + "    .bottom(1)\n" + 
        		INDENT + "    .height(function(d) y(d.y))\n" + 
        		INDENT + "  .anchor(\"top\").add(pv.Line)\n" + 
        		INDENT + "    .strokeStyle(colors[" + s + "])\n" + 
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
				"\t\t\t\t<label for=\"run_" + functionName + "\">Click here to plot the figure</label>\n" +
				"\t\t\t\t<input checked id=\"scale_" + getName() + "\" type=\"checkbox\" onMouseUp=\"" + functionName + ".toggleScaling()\">\n" +
        		"\t\t\t\t<label for=\"scale_" + getName() + "\">Scale to fit</label>\n" +
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
