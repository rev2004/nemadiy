package org.imirsel.m2k.vis;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.ArrayList;
/**
 * A class for plotting segmented transcriptions.
 * @author kris
 */
public class TranscriptionPlot extends SignalPlotObject{
    
    /** The metadata key used to identify the Transcription symbols */
    private String metadataKey;
    /** The transcription data*/
    private String[] metadataColumn;
    /** Constant definition for the offset used to separate the transcription label from the tick mark */
    private static final int LABEL_OFFSET = 5;
    /** Constant definition for number of different heights to draw labels at (so they don't overlap) */
    private static final int NUM_HEIGHTS = 4;
    
    
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
    private final static int legendInsetUnit = 75;
    /** legend size */
    private int legendInset = legendInsetUnit;
    
    /** Creates a new instance of TranscriptionPlot */
    public TranscriptionPlot() {
        super();
        
        this.prefHeight = 125;
        this.colorTable[0] = Color.blue; 
        this.colorTable[1] = Color.magenta;
        this.colorTable[2] = Color.orange;
        this.colorTable[3] = Color.green;
        this.colorTable[4] = Color.darkGray;
        this.colorTable[5] = Color.yellow;
        this.colorTable[6] = Color.lightGray;
        this.colorTable[7] = Color.cyan;
        this.colorTable[8] = Color.pink;
        this.colorTable[9] = Color.red;
        this.colorTable[10] = Color.gray;
        this.colorTable[11] = Color.black;
        legendRect = new Rectangle();CheckLegend = new JCheckBox("legend", true);
        CheckBoxListener myListener = new CheckBoxListener();
        CheckLegend.addItemListener(myListener);
        CheckLegend.setBounds(legendRect.x + 1, legendRect.y + 1, legendRect.width - 1, 19);
        CheckLegend.setVisible(true);
        CheckLegend.setBackground(this.bgColor);
        CheckLegend.setFont(labelFont);
        this.add(CheckLegend, this.PALETTE_LAYER);
        this.moveToFront(CheckLegend);
    }
    
    /**
     * Sets the data for a transcription plot. A Signal Object, a metadata key 
     * corresponding to the transcription and a double array of the onset times to use
     * to plot are taken as input.
     * @param theSignal_ Signal Object containing the transcription.
     * @param metadataKey_ The key corresponding to the transcription (String[]) in the Signal Object.
     * @param segmentationColumn_ Onset times to use to produce the plot.
     */
    public void setData(Signal theSignal_, String metadataKey_, double[] segmentationColumn_)
    {
        theSignal = theSignal_;
        metadataKey = metadataKey_;
        try{
            metadataColumn = theSignal.getStringArrayMetadata(metadataKey);
        }catch(noMetadataException nme)
        {
            throw new RuntimeException("Transcription metadata is not valid!\n" + nme.getLocalizedMessage(), nme);
        }
        this.onsetTimes = segmentationColumn_;
        
        labels = new ArrayList();
        for (int i=0;i<metadataColumn.length;i++)
        {
            if (!labels.contains(metadataColumn[i]))
            {
                labels.add(metadataColumn[i]);
            }
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
    }
    
    /* Uses the graphics object to draw the plot, also called when plot is 'repainted'.
     * @param g The graphics object, passed to this method by the frame containing the panel panel
     */
    /**
     * Paint the frame.
     * @param g Graphics Object to use to paint the frame.
     */
    public void paint(Graphics g)
    {
        this.graphics = g;
        //super.paint(g); 
        // define the location and size of the plot area:
        plotRect.setBounds(horzInset,
                vertInset,
                (getSize().width - 2*horzInset),
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
        this.graphics.setColor(this.bgColor);
        this.graphics.fillRect(0, 0, getSize().width, getSize().height);
        //this.graphics.fillRect(plotRect.x, plotRect.y, plotRect.width, plotRect.height);
        this.graphics.setColor(this.gridColor);
        this.graphics.drawRect(plotRect.x, plotRect.y, plotRect.width, plotRect.height);
        drawAxes();
        drawGrid();
        drawLabels();
        drawTitle();
        if (this.legendInset != 0)
        {
            drawLegend();
        }else
        {
            drawSegmentation();
        }
        super.paint(g); 
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
    }
    
    /**
     * Sets a divider at the specified time index.
     * @param time Time index to add a divider at.
     */
    public void setDivider(double time)
    {
        graphics.setColor(this.dividerColor);
        double length = endTime - startTime;
        //float xScale = plotRect.width/(float)nPoints;
        int x = plotRect.x + Math.round(plotRect.width * (float)((time - startTime)/length));
        graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
    }
    
    /**
     * Sets a flag that determines whether the full legend box is displayed.
     * @param val a flag that determines whether the full legend box is displayed.
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
    
    /**
     * Draw the labels.
     */
    public void drawLabels()
    {
        if (this.metadataColumn == null)
        {
            throw new RuntimeException("No labels to plot!");
        }
        //find first displayable onset
        int startOnset = 0;
        while ((startOnset < this.onsetTimes.length-1)&&(this.startTime > this.onsetTimes[startOnset]))
        {
            startOnset++;
        }
        
        //find index after last displayable onset
        int endOnset = startOnset;
        while((endOnset < this.onsetTimes.length-1)&&(this.endTime >= this.onsetTimes[endOnset]))
        {
            endOnset++;
        }

        double length = endTime - startTime;
        int count = 1;
        int x = -1;
        int nextX = -1;
        //if necessary, plot segment carried over from previous segment
        if ((this.startTime < this.onsetTimes[startOnset])&&(startOnset != 0)){
            graphics.setColor(this.colorTable[this.labels.indexOf(this.metadataColumn[startOnset-1])]);
            
            nextX = plotRect.x + Math.round(((float)plotRect.width) * (float)((this.onsetTimes[0] - startTime)/length));
            graphics.fillRect(plotRect.x, plotRect.y, x-plotRect.x, plotRect.height);
        }
        for (int i=startOnset;i<endOnset;i++)
        {
            graphics.setColor(this.colorTable[this.labels.indexOf(this.metadataColumn[i])]);
            //int x = plotRect.x + Math.round(plotRect.width * (float)((this.onsetTimes[i] - startTime)/length)) + LABEL_OFFSET;
            if (nextX == -1){
                x = plotRect.x + Math.round(((float)plotRect.width) * (float)((this.onsetTimes[i] - startTime)/length));
            }else
            {
                x = nextX;
            }
            nextX = plotRect.x + Math.round(((float)plotRect.width) * (float)((this.onsetTimes[i+1] - startTime)/length));
            if (nextX > (plotRect.x + plotRect.width))
            {
                nextX = (plotRect.x + plotRect.width);
            }
            
            if (this.legendInset != 0){
                graphics.fillRect(x, plotRect.y, nextX - x, plotRect.height);

            }else
            {
                graphics.setColor(this.labelColor);
                graphics.setFont(this.labelFont);
                int y = plotRect.y + Math.round((((float)plotRect.height) / ((float)this.NUM_HEIGHTS+1)) * ((float)count));
                count++;
                if (count > this.NUM_HEIGHTS+1){
                    count = 1;
                }
                graphics.drawString(this.metadataColumn[i], x, y);
            }
        }
        //draw black rectangles around segments to improve boundary visibility
        if (this.legendInset != 0){
            graphics.setColor(Color.black);
            x = -1;
            nextX = -1;
            for (int i=startOnset;i<endOnset;i++)
            {
                if (nextX == -1){
                    x = plotRect.x + Math.round(((float)plotRect.width) * (float)((this.onsetTimes[i] - startTime)/length));
                }else
                {
                    x = nextX;
                }
                nextX = plotRect.x + Math.round(((float)plotRect.width) * (float)((this.onsetTimes[i+1] - startTime)/length));
                if (nextX > (plotRect.x + plotRect.width))
                {
                    nextX = (plotRect.x + plotRect.width);
                }
                graphics.drawRect(x, plotRect.y, nextX - x, plotRect.height);
            }
        }
    }
    
    /**
     * Draw the plot title.
     */
    public void drawTitle() {
        graphics.setColor(this.textColor);
        graphics.setFont(textFont);
        try{
            graphics.drawString("File: " + this.theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION) + ", Transcription: " + this.metadataKey + ", start: " + decimal.format(this.startTime) + " s, end: " + decimal.format(this.endTime) + " s", this.horzInset,  13);
        }catch(noMetadataException nme) {
            graphics.drawString("File: UNKNOWN!" + ", Transcription: " + this.metadataKey + ", start: " + decimal.format(this.startTime) + " s, end: " + decimal.format(this.endTime) + " s", this.horzInset, 13);
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
            graphics.setColor(gridColor);
            graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
            graphics.setColor(gridColor);
            graphics.drawString(this.millisec.format(timeIndex), x - 8, plotRect.y + plotRect.height + 14);
        }
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
