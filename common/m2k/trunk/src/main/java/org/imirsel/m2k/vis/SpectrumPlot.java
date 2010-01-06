package org.imirsel.m2k.vis;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.awt.*;
import javax.swing.JPanel;

/**
 * A class for plotting spectrograms.
 * @author kris
 */
public class SpectrumPlot extends SignalPlotObject{
    /** String Keys for data columns to plot */
    private String[] columnIDs;
    /** Columns indices to plot */
    private int[]  columnInds;
    private Color dividerColor = Color.red;
    private int nFrames = 0;
    private int nBins = 0;
    private float ymax = 0.0f;
    private float ymin = 0.0f;
    private boolean interp = false;
    private boolean wasInterp = false;
    private boolean logScale = false;
    private boolean wasLogScale = false;
    private boolean inColour = true;
    private boolean wasInColour = false;
    private boolean showSegmentation = false;
    /** Groundtruth onset times */
    private double[] GTonsetTimes;
    private Image spectrumImage = null;
    private Image scaledImage = null;
    private int scaledStartIndex = -1;
    private int scaledEndIndex = -1;
    private ColourMap cmap;
    
    /** Creates a new instance of SpectrumPlot */
    public SpectrumPlot() {
        super();
        this.prefHeight = 296;
        cmap = new JetColourMap();
    }
    
    /**
     * Sets a flag indicating whether a log scale is to be used on the spectrum plot.
     * @param val a flag indicating whether a log scale is to be used on the spectrum plot.
     */
    public void setLogScale(boolean val)
    {
        this.logScale = val;
        repaint();
    }
    
    /**
     * Sets a flag indicating whether interpolation should be used to smooth the plot.
     * Currently interpolation doesn't work so disabled.
     * @param val a flag indicating whether interpolation should be used to smooth the plot.
     */
    public void setInterp(boolean val)
    {
        this.interp = val;
        repaint();
    }
    
    /**
     * Sets a flag that determines whether the spectrum is plotted in colour.
     * @param val a flag that determines whether the spectrum is plotted in colour.
     */
    public void setInColour(boolean val)
    {
        this.inColour = val;
        repaint();
    }
    
    /**
     * Sets a flag that determines whether the segmentation is plotted.
     * @param val a flag that determines whether the segmentation is plotted.
     */
    public void setShowSegmentation(boolean val){
        showSegmentation = val;
        repaint();
    }
    
    /**
     * Sets the ground truth segmentation times.
     * @param GTtimes the ground truth segmentation times.
     */
    public void setGTSegmentation(double[] GTtimes){
        GTonsetTimes = GTtimes;
    }
    
    /**
     * Sets the Signal Object and data columns that will be plotted as a Spectrogram.
     * @param theSignal_ the Signal Object that will be plotted.
     * @param columnIDs_ the data columns that will be plotted as a Spectrogram.
     */
    public void setData(Signal theSignal_, String[] columnIDs_)
    {
        try {
            theSignal = (Signal)theSignal_.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        if (theSignal.getNumRows() <= 3)
        {
            try {
                //few vectors so fix metadata
                double duration = theSignal_.getDoubleMetadata(Signal.PROP_DURATION);
                //set 100Hz resolution
                theSignal.setMetadata(Signal.PROP_SAMPLE_RATE,new Integer(100));
                theSignal.setMetadata(Signal.PROP_FRAME_SIZE, new Integer(Math.round(((float)(duration * 100)))));
                theSignal.setMetadata(Signal.PROP_OVERLAP_SIZE, null);
                
            } catch (noMetadataException ex) {
                ex.printStackTrace();
            }
        }
        columnIDs = columnIDs_;
        columnInds = new int[columnIDs.length];
        nFrames = theSignal.getNumRows();
        nBins = columnIDs.length;
            
/*        try{
            nFrames = theSignal.getNumRows();
            nBins = columnIDs.length;
            for (int i=0;i<columnIDs.length;i++) {
                columnInds[i] = theSignal.getColumnIndex(columnIDs[i]);
                for (int j=0; j < nFrames; j++) {
                    if (theSignal.getData()[theSignal.getColumnIndex(columnIDs[i])][j] > ymax) {
                        ymax = (float)theSignal.getData()[columnInds[i]][j];
                    }
                    if (theSignal.getData()[theSignal.getColumnIndex(columnIDs[i])][j] < ymin) {
                        ymin = (float)theSignal.getData()[columnInds[i]][j];
                    }
                }
            }
        } catch(noMetadataException nme) {
            throw new RuntimeException("Columns not found!", nme);
        }
*/      this.setVisible(true);
        this.validate();
        //this.repaint();
    }
    
    /* Uses the graphics object to draw the plot, also called when plot is 'repainted'.
     * @param g The graphics object, passed to this method by the frame containing the panel panel
     */
    /**
     * Paint the frame.
     * @param g Graphics Object to be used to paint the frame.
     */
    public void paint(Graphics g)
    {
        this.graphics = g;
        this.setBackground(bgColor); 
        // define the location and size of the plot area:
        plotRect.setBounds(horzInset,
                vertInset,
                getSize().width - 2*horzInset,
                getSize().height - 2*vertInset);
        //draw bg
        this.graphics.setColor(this.bgColor);
        this.graphics.fillRect(0, 0, getSize().width, getSize().height);
        //draw box
        this.graphics.setColor(this.gridColor);
        this.graphics.drawRect(plotRect.x, plotRect.y, plotRect.width, plotRect.height);
        
        plotPoints();
        if ((onsetTimes != null)&&(showSegmentation)) {
            drawSegmentation();
        }
        drawAxes();
        drawGrid();
        drawTitle();
        //super.paint(g);
    }
    
    
    private void plotPoints() {
        if (nFrames != 0) {
            
            int xAxisPos = plotRect.y + plotRect.height;

            //setup an image to store the spectrum
            if ((spectrumImage == null)||(wasLogScale != logScale)||(wasInterp != interp)||(wasInColour != inColour)){
                //see SignalPlotObject for how start and end indices are set, note indices are inclusive
                wasLogScale = logScale;
                wasInterp = interp;
                wasInColour = inColour;
                int length = theSignal.getNumRows();
                //if (startIdx != -1) {
                //    length = (endIdx) - startIdx;
                //}
                int height = columnIDs.length;

                GraphicsConfiguration gc = getGraphicsConfiguration();
                spectrumImage = gc.createCompatibleImage(length, height);
                Graphics2D gImg = (Graphics2D)spectrumImage.getGraphics();
                
                try
                {
                    ymax = Float.NEGATIVE_INFINITY;
                    ymin = Float.POSITIVE_INFINITY;
                    //double mean = 0.0;
                    int skipped = 0;
                    for (int i=0;i<height;i++) {
                        columnInds[i] = theSignal.getColumnIndex(columnIDs[i]);
                        for (int j=0; j < length; j++) {
                            
                            
                            double val = theSignal.getData()[columnInds[i]][j];
                            
                            if (this.logScale)
                            {
                                if (val > 0.0){
                                    val = Math.log10(val);
                                }else{
                                    val = -5.0;
                                }
                            }
                            if (val > ymax) {
                                ymax = (float)val;
                            }
                            if (val < ymin) {
                                ymin = (float)val;
                            }
                        }
                    }
                    
                } catch(noMetadataException nme) {
                    throw new RuntimeException("Columns not found!", nme);
                }

                int xSize = 1;
                int ySize = 1;

                // Coordinates of the time-frequency points
                int[] xCoords = new int[length];
                int[] yCoords = new int[height];

                // Coordinates of time-frequency points for a full resolution resampled/interpolated image
                
                //The loop
                Color white = Color.white;
                float range = ymax - ymin;
                //System.out.println("range: " + range);
                int zero = 0;
                for (int i = 0; i < length; i++) {
                    for (int j = 0; j < height; j++){

                        xCoords[i] = i;
//                        int ymod = j;
//                        int ymod2 = j+1;
                        yCoords[j] = height - j;
                        //ySize = 1;
                        
                        double data = theSignal.getData()[columnInds[j]][i];
                        
                        if (this.logScale)
                        {
                            if (data > 0.0){
                                data = Math.log10(data);
                            }else{
                                data = -5.0;
                            }
                        }
                        float dataPoint = (float)data;
                        
                        
//                        if (dataPoint < ymin ){
//                           dataPoint = ymin;
//                           zero++;
//                        }else{
                        dataPoint = (dataPoint - ymin)/range;//ymax;
//                        }
                        
                        Color c;
                        if (inColour)
                        {
                            /*float r=1.0f;
                            float g=1.0f;
                            float b=1.0f;
                            if (dataPoint <= 0.0f)
                            {
                                
                            }else if (dataPoint <= 0.3333f){
                                b = dataPoint/0.3333f;
                            }else if(dataPoint <= 0.6666f)
                            {
                                b = (0.3333f - (dataPoint - 0.3333f)) * 3;
                                g = (dataPoint - 0.3333f)/0.3333f;
                            }else// if(dataPoint <= 0.6666f)
                            {
                                g = (0.3333f - (dataPoint - 0.6666f)) * 3;
                                r = (dataPoint - 0.6666f)/0.3333f;
                            }
                            c = new Color(r, g, b);*/
                            c = cmap.mapToColor(dataPoint);
                        }else{
                            c = new Color((1.0f-dataPoint), (1.0f-dataPoint), (1.0f-dataPoint));
                        }
                        gImg.setColor(c);
                        
                        
                        gImg.fillRect(xCoords[i], yCoords[j]-1, xSize,  ySize);

                    }
                }
                //System.out.println("Zero-ed: " + zero);

                gImg.dispose();
                //save some memory
                for (int i=1;i<theSignal.getNumCols();i++)
                {
                    theSignal.deleteColumn(i);
                }
            }
            //setup an image to store the scaled and clipped spectrum image
            if ((scaledImage == null) || 
                    (scaledImage.getWidth(null) != plotRect.width) || 
                    (scaledImage.getHeight(null) !=  plotRect.height) ||
                    (scaledStartIndex != this.startIdx) ||
                    (scaledEndIndex != this.endIdx)) {
                GraphicsConfiguration gc = getGraphicsConfiguration();
                scaledImage = gc.createCompatibleImage(plotRect.width, plotRect.height);
                scaledImage.getGraphics().drawImage(spectrumImage,0,0,scaledImage.getWidth(null),scaledImage.getHeight(null),startIdx,0,endIdx,spectrumImage.getHeight(null),null);
                
            }
            graphics.drawImage(scaledImage, plotRect.x, plotRect.y, null);
            
        }
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
            this.setDivider(this.onsetTimes[i], this.dividerColor);
        }
        
        if (GTonsetTimes != null)
        {
            //find first displayable onset
            int startGTOnset = 0;
            while ((startGTOnset < this.GTonsetTimes.length)&&(this.startTime > this.GTonsetTimes[startGTOnset])) {
                startGTOnset++;
            }

            //find index after last displayable onset
            int endGTOnset = startGTOnset;
            while((endGTOnset < this.GTonsetTimes.length)&&(this.endTime > this.GTonsetTimes[endGTOnset])) {
                endGTOnset++;
            }
            for (int i=startGTOnset;i<endGTOnset;i++) {
                this.setDivider(this.GTonsetTimes[i], GTDividerColor);
                System.out.println("Setting GT divider.");
            }
        }
    }
    
    
    private void drawAxes() { 
        int xAxisPos = plotRect.y + plotRect.height;
        
        graphics.setColor(axisColor);
        // vertical axis:
        graphics.drawLine(plotRect.x, plotRect.y,
                plotRect.x, plotRect.y + plotRect.height);
        // horizontal axis:
        graphics.drawLine(plotRect.x, xAxisPos,
                plotRect.x + plotRect.width, xAxisPos);
    }
    
    private void drawGrid() {
        double length = endTime - startTime;
        double c = 0.0;
        int x, y;
        graphics.setFont(this.timeFont);
        
        //set grid time interval
        int i;
        for (i=0;this.gridLengthRange[i]<length;i++)
        {}
        double gridInt = this.gridTimeIntervals[i];

        // vertical grid lines and time indices
        double timeIndex = startTime;
        while (c < length) {
            c += gridInt;
            timeIndex += gridInt;
            x = plotRect.x + Math.round(plotRect.width * (float)(c/length));
            //no lines across spectrum
            //graphics.setColor(gridColor);
            //graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
            //draw time labels
            graphics.setColor(gridColor);
            graphics.drawString(this.millisec.format(timeIndex), x - 8, plotRect.y + plotRect.height + 14);
        }
        
    }
    
    /**
     * Draw the plot title.
     */
    public void drawTitle() {
        graphics.setColor(this.textColor);
        graphics.setFont(textFont);
        try{
            graphics.drawString("File: " + this.theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION) + ", start time: " + decimal.format(this.startTime) + ", end time: " + decimal.format(this.endTime), this.horzInset,  13);
        }catch(noMetadataException nme) {
            graphics.drawString("File: UNKNOWN!" +  ", start time: " + decimal.format(this.startTime) + ", end time: " + decimal.format(this.endTime), this.horzInset, 13);
        }
    }
    
    /**
     * Set a signle divider in the specified colour at the specified time index.
     * @param time Time index for divider.
     * @param divColor Colour to display divider in.
     */
    public void setDivider(double time, Color divColor) {
        graphics.setColor(divColor);
        double length = endTime - startTime;
        int x = plotRect.x + Math.round(plotRect.width * (float)((time - startTime)/length));
        graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
    }
}
