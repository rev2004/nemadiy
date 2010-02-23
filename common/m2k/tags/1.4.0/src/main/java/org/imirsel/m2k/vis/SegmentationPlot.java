package org.imirsel.m2k.vis;
import java.awt.*;
import javax.swing.JPanel;
import org.imirsel.m2k.util.Signal;
/**
 * A class for plotting only the segmentation of a time series.
 * @author kris
 */
public class SegmentationPlot extends SignalPlotObject{
    
    //The signal Object containing the transcription and segmentation to plot
    private Signal theSignal;
    //The metadata key used to identify the Segmentation times (onset times)
    private String segmentationColumn;
    
    Color dividerColor = Color.red;
    
    /** Creates a new instance of TranscriptionPlot */
    public SegmentationPlot() {
        super();
    }
    
    /**
     * Sets the Signal Object and data column to plot.
     * @param theSignal_ The Signal Object to plot.
     * @param segmentationColumn_ The data column to plot.
     */
    public void setData(Signal theSignal_, String segmentationColumn_)
    {
        theSignal = theSignal_;
        segmentationColumn = segmentationColumn_;
        repaint();
    }
    
    /* Uses the graphics object to draw the plot, also called when plot is 'repainted'.
     * @param g The graphics object, passed to this method by the frame containing the panel panel
     */
    /**
     * Paint the frame.
     * @param g The graphics Object to use to paint the frame.
     */
    public void paint(Graphics g)
    {
    
    }
    
    private void drawSegmentation() {
        if (this.onsetTimes == null)
        {
            throw new RuntimeException("No segmentation to plot!");
        }
        
        //find first displayable onset
        int startOnset = 0;
        while ((startOnset < this.onsetTimes.length)&&(this.startTime > this.onsetTimes[startOnset]))
        {
            startOnset++;
        }
        
        //find index after last displayable onset
        int endOnset = startOnset;
        while((endOnset < this.onsetTimes.length)&&(this.endTime > this.onsetTimes[endOnset]))
        {
            endOnset++;
        }
        
        for (int i=startOnset;i<endOnset;i++)
        {
            this.setDivider(this.onsetTimes[i]);
        }
        
        repaint();
    }
    
    /**
     * Adds a divider at the specified time index.
     * @param time time index to add a divider at.
     */
    public void setDivider(double time)
    {
        graphics.setColor(this.dividerColor);
        double length = endTime - startTime;
        //float xScale = plotRect.width/(float)nPoints;
        int x = plotRect.x + Math.round(plotRect.width * (float)((time - startTime)/length));
        graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
    }
    
}
