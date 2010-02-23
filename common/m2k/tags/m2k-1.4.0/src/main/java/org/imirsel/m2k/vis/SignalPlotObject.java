
package org.imirsel.m2k.vis;
import java.awt.*;
import javax.swing.JLayeredPane;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.text.DecimalFormat;
/**
 * An abstract class defining the methods of a plot object.
 * @author kris
 */
public abstract class SignalPlotObject extends JLayeredPane{
    /** The signal Object containing the data to plot */

    protected Signal theSignal;
    /**
     * Indicates the first time index to display.
     */
    protected double startTime;
    /**
     * Indicates the last time index to display.
     */
    protected double endTime;
    /**
     * Indicates the first array index to display.
     */
    protected int startIdx;
    /**
     * Indicates the first array index to display.
     */
    protected int endIdx;
    /**
     * Graphics Object to used to paint the frame.
     */
    protected Graphics graphics;
    /**
     * The rectangle within which to plot.
     */
    protected Rectangle plotRect;
    /**
     * The amount to inset the plot vertically.
     */
    protected int vertInset = 20;
    /**
     * The amount to inset the plot horizonatally.
     */
    protected int horzInset = 20;
    /**
     * Preferred height of the plot.
     */
    protected int prefHeight = 125;
    /**
     * Onset times to display on the plot
     */
    protected double[] onsetTimes;
    /**
     * For formatting decimal numbers.
     */
    protected DecimalFormat decimal;
    /**
     * For formatting times upto millisecond precision.
     */
    protected DecimalFormat millisec;
    
    /**
     * The colour for the plot line.
     */
    protected Color plotColor = Color.blue;
    /**
     * Axis colour for the plot.
     */
    protected Color axisColor = Color.lightGray;
    /**
     * Grid colour for the plot.
     */
    protected Color gridColor = Color.lightGray;
    /**
     * Divider colour for the plot (usually displays onset times).
     */
    protected Color dividerColor = Color.red;
    /**
     * Ground-truth divider colour for the plot (usually displays onset times).
     */
    protected Color GTDividerColor = Color.green;
    /**
     * Background colour for the plot.
     */
    protected Color bgColor   = Color.white;
    /**
     * Text colour for the plot.
     */
    protected Color textColor = Color.darkGray;
    /**
     * Font for displaying text on the plot.
     */
    protected Font textFont = Font.decode("Arial-PLAIN-12");
    /**
     * Font for displaying time stamps on the plot.
     */
    protected Font timeFont = Font.decode("Arial-PLAIN-10");
    /**
     * Valid grid sizes.
     */
    protected double[] gridLengthRange =   {0.5,   1,    5,    10,  100, 1000, 10000, 100000};
    /**
     * Valid grid time intervals.
     */
    protected double[] gridTimeIntervals = {0.025, 0.05, 0.25, 0.5, 5,   50,   500,   5000};
    
    
    /**
     * Creates a new instance of the plot Object.
     */
    public SignalPlotObject() {
        //this.setBackground(this.bgColor);

        //this.setOpaque(true);

        plotRect = new Rectangle();
        startTime = 0.0;
        endTime = Double.MAX_VALUE;
        theSignal = null;
        onsetTimes = null;
        decimal = new DecimalFormat();
        decimal.setMaximumFractionDigits(3);
        millisec = new DecimalFormat();
        millisec.setMaximumFractionDigits(3);
    }
    
    /** Set horizontal inset for plot area from canvas boundary
     * @param h horizontal inset for plot area from canvas boundary
     */
    public void setHorzInset(int h) {
        horzInset = h;
    }
    
    /** Set vertical inset for plot area from canvas boundary
     * @param v vertical inset for plot area from canvas boundary
     */
    public void setVertInset(int v) {
        vertInset = v;
    }
    
    /** Get horizontal inset for plot area from canvas boundary
     * @return horizontal inset for plot area from canvas boundary
     */
    public int getHorzInset() {
        return horzInset;
    }
    
    /** Get vertical inset for plot area from canvas boundary
     * @return vertical inset for plot area from canvas boundary
     */
    public int getVertInset() {
        return vertInset;
    }
    
    /** Get plotObject preferred height
     *  @return the preferred height of the plot object
     */
    public int getPlotObjectHeight()
    {
        return prefHeight;
    }
    
    /* Determines which segment of the available data will be plotted when paint is called.
     * theSignal will have to be set by a class that extends this class before this 
     * method is called.
     * @param start The first time index to be plotted.
     * @param end The end time index to be plotted.
     */
    /**
     * Sets the start and end time indices to display.
     * @param start Start time index
     * @param end End time index.
     */
    public void setPlotTimeIndices(double start, double end) {
        System.out.println("Calling setPlotTimeIndices with start: " + start + " and end: " + end);
        
        if (theSignal == null)
        {
            throw new RuntimeException( "No data has been set, unable to set plot indices!");
        }
        startTime = start;
        endTime = end;
        
        
        //set inclusive start and end indices
        startIdx = 0;
        endIdx = -1;
        try{
            int i=0;
            while ((theSignal.getTimeStamp(i) - startTime) < 0.0){
                i++;
            }
            startIdx = i;
//            for (int i=0;i<theSignal.getNumRows();i++)
//            {
//                if ((theSignal.getTimeStamp(i) - startTime) >= 0.0)
//                {
//                    startIdx = i;
//                    break;
//                }
//            }
//            if (startIdx == -1)
//            {
//                throw new RuntimeException("Passed end of data before start index found!");
//            }
            i=0;
            while ((theSignal.getTimeStamp(i) - endTime) < 0.0){
                i++;
            }
            endIdx = i;
//            for (int i=startIdx;i<theSignal.getNumRows();i++)
//            {
//                if ((theSignal.getTimeStamp(i) - endTime) >= 0.0)
//                {
//                    endIdx = i;
//                    break;
//                }
//            }
//            if (endIdx == -1)
//            {
//                endIdx = theSignal.getNumRows();
//                endTime = theSignal.getTimeStamp(endIdx);
//            }
            if (endIdx == 0){
                System.out.println("WARNING: returning end index of 0!");
            }
        }catch (noMetadataException nme)
        {
            throw new RuntimeException("Unable to retrieve metadata required  to calculate time indices!",nme);
        }
        
//        try {
//            
//                System.out.println("DEBUG setPlotTimeIndicies");
//                System.out.println("Sample rate: " + theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE));
//                System.out.println("Frame size: " + theSignal.getIntMetadata(Signal.PROP_FRAME_SIZE));
//                System.out.println("Overlap size: " + theSignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE));
//        } catch (noMetadataException ex) {
//            throw new RuntimeException("Missing metadata in Signal!",ex);
//        }
        
        System.out.println("set start index: " + startIdx + ", time: " + startTime + ", end index: " + endIdx + ", time: " + endTime);
    }
    
    /* Determines which segment of the available data will be plotted when paint is called.
     * theSignal will have to be set by a class that extends this class before this 
     * method is called.
     * @param start The first data row to be plotted.
     * @param end The last data row to be plotted.
     */
    /**
     * Sets the start and end array indices to display.
     * @param start Start array index.
     * @param end End array index.
     */
    public void setPlotTimeIndices(int start, int end) {
        if (theSignal == null)
        {
            throw new RuntimeException( "No data has been set, unable to set plot indices!");
        }
        
        //set inclusive start and end indices
        startIdx = start;
        endIdx = end;
        try{
            startTime = theSignal.getTimeStamp(startIdx);
            endTime = theSignal.getTimeStamp(endIdx);
        }catch (noMetadataException nme)
        {
            throw new RuntimeException("Unable to retrieve metadata required  to calculate time indices!",nme);
        }
        System.out.println("set start (int) index: " + startIdx + ", time: " + startTime + ", end index: " + endIdx + ", time: " + endTime);
    }
    
    /**
     * Sets onset times to display on the plot.
     * @param onsetTimes_ onset times to display on the plot.
     */
    public void setSegmentation(double[] onsetTimes_)
    {
        onsetTimes = onsetTimes_;
    }
    
    /* Uses the graphics object to draw the plot, also called when plot is 'repainted'.
     * @param g The graphics object, passed to this method by the frame containing the panel panel
     */
    //public abstract void paint(Graphics g);
}
