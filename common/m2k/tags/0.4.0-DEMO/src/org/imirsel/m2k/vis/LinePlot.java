package org.imirsel.m2k.vis;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 * A simple Signal plotter (1d function plotter), supports plotting of multiple 
 * Signals on one axis.
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class LinePlot extends SignalPlotObject{
    /** String Keys for data columns to plot */
    private String[] columnID;
    /** Number of points in plots */
    int nPoints = 0;
    
    //float xmax = 0.0f;
    float ymax = Float.MIN_VALUE;
    float ymin = Float.MAX_VALUE;
    
    /** Rectangle to hold legend */
    private Rectangle legendRect;
    /** ArrayList to hold list of labels */
    private ArrayList labels;
    /** Boolean controlling whether full legend box is displayed or only enabling checkbox */
    private boolean showFullLegend = true;
    /** Checkbox for enabling or disabling legend display */
    private JCheckBox CheckLegend;
    /** Constant definition for legend box size */
    private static final int LEGEND_SQUARE_SZ = 12;
    /** Label color */
    Color labelColor = Color.blue;
    /** Label font*/
    Font labelFont = Font.decode("Arial-PLAIN-11");
    /** Plot color table */
    Color[] colorTable = new Color[12];
    /** Constant definition for legend size units */
    private final static int legendInsetUnit = 110;
    /** legend size */
    private int legendInset = legendInsetUnit;
    /** Groundtruth onset times */
    private double[] GTonsetTimes;
    
    /**
     * Creeates a new instance of LinePlot.
     */
    public LinePlot(){
        super();
        this.prefHeight = 250;
        this.colorTable[0] = Color.blue; 
        this.colorTable[1] = Color.magenta;
        this.colorTable[2] = Color.orange;
        this.colorTable[3] = Color.darkGray;
        this.colorTable[4] = Color.yellow;
        this.colorTable[5] = Color.lightGray;
        this.colorTable[6] = Color.cyan;
        this.colorTable[7] = Color.pink;
        this.colorTable[8] = Color.red;
        this.colorTable[9] = Color.gray;
        this.colorTable[10] = Color.black;
        this.colorTable[11] = Color.green;
        legendRect = new Rectangle();
        CheckLegend = new JCheckBox("legend", true);
        CheckBoxListener myListener = new CheckBoxListener();
        CheckLegend.addItemListener(myListener);
        CheckLegend.setBounds(legendRect.x + 1, legendRect.y + 1, legendRect.width - 1, 19);
        CheckLegend.setVisible(true);
        CheckLegend.setBackground(this.bgColor);
        CheckLegend.setFont(labelFont);
        this.add(CheckLegend, this.PALETTE_LAYER);
        this.moveToFront(CheckLegend);
        GTonsetTimes = null;
    }
    
    /**
     * Sets the ground truth segmentation to display.
     * @param GTtimes An array of doubles indicating the time of ground-truth onsets.
     */
    public void setGTSegmentation(double[] GTtimes){
        GTonsetTimes = GTtimes;
    }
    
    /**
     * Sets the Signal Object and data column to plot.
     * @param theSignal_ the Signal Object to plot.
     * @param columnID_ the data column to plot.
     */
    public void setData(Signal theSignal_, String[] columnID_) {
        theSignal = theSignal_;
        columnID = columnID_;
        try{
            nPoints = theSignal.getNumRows();
            for (int i=0;i<nPoints;i++) {
                for (int j=0;j<columnID.length;j++){
                    if (theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i] > ymax) {
                        ymax = (float)theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i];
                    }
                    if (theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i] < ymin) {
                        ymin = (float)theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i];
                    }
                }
                
            }
            if (Math.abs(ymin) > ymax){
                ymax = Math.abs(ymin);
            }
        } catch(noMetadataException nme) {
            throw new RuntimeException("Column: \"" + columnID + "\" not found!", nme);
        }
        labels = new ArrayList();
        for (int i=0;i<columnID.length;i++)
        {
            labels.add(columnID[i]);
        }
        if (labels.size() <=6)
        {
            this.legendInset = this.legendInsetUnit;
        }else if(labels.size() <=12)
        {
            this.legendInset = this.legendInsetUnit * 2;
        }else if (labels.size() > 12)
        {
            this.legendInset = 0;
            System.out.println("WARNING: TranscriptionPlot currently only supports 12 labels or less when producing color plots, text will be used instead!");
        }
        this.setVisible(true);
        this.validate();
        this.repaint();
    }
    
    /* Uses the graphics object to draw the plot, also called when plot is 'repainted'.
     * @param g The graphics object, passed to this method by the frame containing the panel
     */
    /**
     * Paints the plot frame.
     * @param g Graphics Object to use to paint the frame.
     */
    public void paint(Graphics g)  {
        //System.out.println("paint");
        this.graphics = g;
        this.setBackground(bgColor); 
        // define the location and size of the plot area:
        plotRect.setBounds(horzInset,
                vertInset,
                getSize().width - 2*horzInset,
                getSize().height - 2*vertInset);
        int h = 0;
        if (labels.size() < 6)
        {
            h = 20 + ((5+this.LEGEND_SQUARE_SZ)*labels.size()) + 2;
        }else 
        {
            h = 20 + ((5+this.LEGEND_SQUARE_SZ)*6) + 2;
        }
        legendRect.setBounds(getSize().width - (legendInset + 2), 
                2, 
                legendInset, 
                //getSize().height - 4);
                h);
        //draw bg
        this.graphics.setColor(this.bgColor);
        this.graphics.fillRect(0, 0, getSize().width, getSize().height);
        //draw box
        this.graphics.setColor(this.gridColor);
        this.graphics.drawRect(plotRect.x, plotRect.y, plotRect.width, plotRect.height);
        try{
            int length = theSignal.getNumRows();
            if (startIdx != -1) {
//                if (endIdx == -1){
//                    endIdx = length;
//                }
//                if ((endIdx - startIdx) <= length){
                    length = endIdx - startIdx;
//                }else{
//                    length = length - startIdx;
//                    endIdx = length;
//                }
//            }else
//            {
//                startIdx = 0;
            }
//            this.setPlotTimeIndices(startIdx, endIdx);
            ymax = Float.NEGATIVE_INFINITY;
            ymin = Float.POSITIVE_INFINITY;
            for (int i = 0; i < Math.min(length, theSignal.getNumRows() - startIdx); i++) {
                for (int j=0;j<columnID.length;j++){
                    if (theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i+startIdx] > ymax) {
                        ymax = (float)theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i+startIdx];
                    }
                    if (theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i+startIdx] < ymin) {
                        ymin = (float)theSignal.getData()[theSignal.getColumnIndex(columnID[j])][i+startIdx];
                    }
                }

            }
            if (Math.abs(ymin) > ymax){
                ymax = Math.abs(ymin);
            }
        }catch(noMetadataException nme)
        {
            throw new RuntimeException("",nme);
        }
        drawAxes();
        drawGrid();
        if (onsetTimes != null) {
            drawSegmentation();
        }
        plotPoints();
        drawTitle();
        if (this.legendInset != 0)
        {
            drawLegend();
        }
        super.paint(g); 
    }
    
    /**
     * Enables display of the legend box.
     * @param val flag to enable or disbale display of the legend box.
     */
    public void setDisplayFullLegend(boolean val)
    {
        this.showFullLegend = val;
        this.repaint();
    }
    
    /**
     * Draw the legend box.
     */
    public void drawLegend()
    {
        if (this.showFullLegend){
            graphics.setFont(this.labelFont);
            graphics.setColor(Color.white);
            graphics.fillRect(legendRect.x, legendRect.y, legendRect.width, legendRect.height);
            CheckLegend.setBounds(legendRect.x + 1, legendRect.y + 1, legendRect.width - 1, 19);
            graphics.setColor(this.gridColor);
            graphics.drawRect(legendRect.x, legendRect.y, legendRect.width, legendRect.height);
            
            int numLabels = labels.size();
            //int increment = this.LEGEND_SQUARE_SZ; 
            int position = 20;
            int i;
            for (i=0;((i<labels.size())&&(i < 6));i++)
            {
                position += 5;
                graphics.setColor(this.colorTable[i]);
                //indent rectangles 2 pixels from edge of box
                graphics.fillRect(legendRect.x + 2, legendRect.y + position, LEGEND_SQUARE_SZ, LEGEND_SQUARE_SZ);
                graphics.setColor(this.textColor);
                //indent text 5 pixels from edge of rectangle
                graphics.drawString((String)labels.get(i), legendRect.x + LEGEND_SQUARE_SZ + 5,  (legendRect.y + position + (LEGEND_SQUARE_SZ - 5))); 
                position += LEGEND_SQUARE_SZ;
            }
            if (labels.size() > 6)
            {
                
                graphics.setColor(Color.white);
                graphics.fillRect(legendRect.x + (legendRect.width / 2), legendRect.y, (legendRect.width / 2), legendRect.height);
                position = 20;
                for (i=6;((i<labels.size())&&(i < 12));i++)
                {
                    position += 5;
                    graphics.setColor(this.colorTable[i]);
                    //indent rectangles 2 pixels from edge of box
                    graphics.fillRect(legendRect.x + 2 + (this.legendInset/2), legendRect.y + position, LEGEND_SQUARE_SZ, LEGEND_SQUARE_SZ);
                    graphics.setColor(this.textColor);
                    //indent text 3 pixels from edge of rectangle
                    graphics.drawString((String)labels.get(i), legendRect.x + 5 + LEGEND_SQUARE_SZ + (this.legendInset/2),  (legendRect.y + position + (LEGEND_SQUARE_SZ - 5))); 
                    position += LEGEND_SQUARE_SZ;
                }
            }
            
        }else{
            graphics.setFont(this.labelFont);
            graphics.setColor(Color.white);
            graphics.fillRect(legendRect.x, legendRect.y, legendRect.width, 20);
            CheckLegend.setBounds(legendRect.x + 1, legendRect.y + 1, legendRect.width - 1, 19);
            graphics.setColor(this.gridColor);
            graphics.drawRect(legendRect.x, legendRect.y, legendRect.width, 20);
            
        }
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
            graphics.setColor(gridColor);
            graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
            graphics.setColor(gridColor);
            graphics.drawString(this.millisec.format(timeIndex), x - 8, plotRect.y + plotRect.height + 14);
        }
        
    }
    
    private void drawAxes() {
        int xAxisPos = 0;
        if (ymin >= 0.0) {
            xAxisPos = plotRect.y + plotRect.height;
        }else{
            
            xAxisPos = plotRect.y + plotRect.height / 2;
        }
        graphics.setColor(axisColor);
        // vertical axis:
        graphics.drawLine(plotRect.x, plotRect.y,
                plotRect.x, plotRect.y + plotRect.height);
        // horizontal axis:
        graphics.drawLine(plotRect.x, xAxisPos,
                plotRect.x + plotRect.width, xAxisPos);
    }
    
    private void plotPoints() {
        if (nPoints != 0) {
            graphics.setColor(plotColor);
            
            int xAxisPos = 0;
            if (ymin >= 0.0) {
                // x axis at top of plot
                xAxisPos = plotRect.y + plotRect.height;
            }else{
                // x axis in middle of plot
                xAxisPos = plotRect.y + plotRect.height / 2;
            }
            
            //see SignalPlotObject for how start and end indices are set, note indices are inclusive
            int length = theSignal.getNumRows();
            if (startIdx != -1) {
//                if (endIdx == -1){
//                    endIdx = length;
//                }
//                if ((endIdx - startIdx) <= length){
                    length = endIdx - startIdx;
//                }else{
//                    length = length - startIdx;
//                    endIdx = length;
//                }
//            }else
//            {
//                startIdx = 0;
            }
            //System.out.println("start: " + startIdx + " endIdx: " + endIdx);
            float xScale = plotRect.width/(float)length;
            float yScale = 0.0f;
            if (ymin >= 0.0) {
                yScale = plotRect.height/ymax;
            }else{
                yScale = 0.5f*plotRect.height/ymax;
            }
            
            int[] xCoords = new int[length];
            int[] yCoords = new int[length];
            for (int c=0;c<columnID.length;c++)
            {
                graphics.setColor(this.colorTable[c]);
                try{
                    int numDataRows = theSignal.getNumRows() - startIdx;
                    for (int i = 0; i < length; i++) {
                        xCoords[i] = plotRect.x + Math.round(i * xScale);
                        if ((i+startIdx) < numDataRows){
                            int ymod = Math.round((float)theSignal.getData()[theSignal.getColumnIndex(columnID[c])][i+startIdx] * yScale);
                            yCoords[i] = xAxisPos - ymod;
                        }else{
                            yCoords[i] = xAxisPos;
                        }
                        //System.out.println("ymod: " + ymod );
                    }
                    graphics.drawPolyline(xCoords, yCoords, length);
                } catch(noMetadataException nme) {
                    throw new RuntimeException("Column: \"" + columnID + "\" not found!", nme);
                }
            }
        }
    }
    
    private void drawSegmentation() {
        if (this.onsetTimes == null) {
            throw new RuntimeException("No segmentation to plot!");
        }
        
        //find first displayable onset
        int startOnset = 0;
        while ((startOnset < this.onsetTimes.length)&&(this.startTime > this.onsetTimes[startOnset])) {
            startOnset++;
        }
        
        //find index after last displayable onset
        int endOnset = startOnset;
        while((endOnset < this.onsetTimes.length)&&(this.endTime > this.onsetTimes[endOnset])) {
            endOnset++;
        }
        
        for (int i=startOnset;i<endOnset;i++) {
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
    
    /**
     * Draw the plot title.
     */
    public void drawTitle() {
        graphics.setColor(this.textColor);
        graphics.setFont(textFont);
        String cols = new String(columnID[0]);
        for (int i=1;i<columnID.length;i++){
            cols += ", " + columnID[i];
        }
        try{
            
            graphics.drawString("File: " + this.theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION) + ", Column: " + cols + ", start time: " + decimal.format(this.startTime) + ", end time: " + decimal.format(this.endTime), this.horzInset,  13);
        }catch(noMetadataException nme) {
            graphics.drawString("File: UNKNOWN!" + ", Column: " + cols + ", start time: " + decimal.format(this.startTime) + ", end time: " + decimal.format(this.endTime), this.horzInset, 13);
        }
    }
    
    /**
     * Creates a single divider on the plot, with the specified colour.
     * @param time The time stamp at which the divider should appear.
     * @param divColour The colour of the divider.
     */
    public void setDivider(double time, Color divColour) {
        graphics.setColor(divColour);
        double length = endTime - startTime;
        int x = plotRect.x + Math.round(plotRect.width * (float)((time - startTime)/length));
        graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
    }
    
    class CheckBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            
            Object source = e.getSource();
            if (source == CheckLegend){
                if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    //Disable main legend box
                    setDisplayFullLegend(false);
                }else if(e.getStateChange() == ItemEvent.SELECTED)
                {
                    //Enable main legend box
                    setDisplayFullLegend(true);
                }
            }
        }
    }
}
