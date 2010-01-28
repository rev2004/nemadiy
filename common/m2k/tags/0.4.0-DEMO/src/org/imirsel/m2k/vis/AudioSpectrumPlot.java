package org.imirsel.m2k.vis;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.awt.*;
import java.util.ArrayList;
import org.imirsel.m2k.io.file.AudioFileReadClass;
import org.imirsel.m2k.transforms.FFTRealClass;

/**
 * A class for plotting spectrograms.
 * @author kris
 */
public class AudioSpectrumPlot extends SignalPlotObject{


    
    //private int nFrames = 0;
    //private int nBins = 0;
    private boolean inColour = true;
    private boolean showSegmentation = false;
    /** Groundtruth onset times */
    private double[] GTonsetTimes;
    private Image spectrumImage = null;
    private Image scaledImage = null;
    private int scaledStartIndex = -1;
    private int scaledEndIndex = -1;
    private ColourMap cmap;
    
    
    /** Creates a new instance of SpectrumPlot */
    public AudioSpectrumPlot() {
        super();
        this.prefHeight = 296;
        cmap = new JetColourMap();
        dividerColor = Color.red;
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
     */
    public void setData(Signal theSignal_)
    {
        try {
            System.out.println("Setting plot data with Signal: " + theSignal_.getFile().getAbsolutePath());
            
            //this.repaint();
        } catch (noMetadataException ex) {
            System.out.println("!!! No file location for plot Signal!");
        }
        
        theSignal = theSignal_;
        this.setVisible(true);
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
        
        if (this.spectrumImage == null){
            System.out.println("Producing hi-res image of spectrum");
            produceHighResSpectrumImage();
        }
        this.setBackground(bgColor); 
        // define the location and size of the plot area:
        plotRect.setBounds(horzInset,
                vertInset,
                this.getSize().width - 2*horzInset,
                this.getSize().height - 2*vertInset);
        //draw bg
        this.graphics.setColor(this.bgColor);
        this.graphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
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
        //int xAxisPos = plotRect.y + plotRect.height;

//        if (this.spectrumImage == null)
//        {
//            System.out.println("Producing hi-res image of spectrum");
//            produceHighResSpectrumImage();
//        }

        //setup an image to store the scaled and clipped spectrum image
        if ((scaledImage == null) || 
                (scaledImage.getWidth(null) != plotRect.width) || 
                (scaledImage.getHeight(null) !=  plotRect.height) ||
                (scaledStartIndex != this.startIdx) ||
                (scaledEndIndex != this.endIdx)) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            scaledImage = gc.createCompatibleImage(plotRect.width, plotRect.height);
            System.out.println("using startIdx: " + startIdx + ", endIdx: " + endIdx + " to scale image");
            
            scaledImage.getGraphics().drawImage(spectrumImage, 0, 0, plotRect.width, plotRect.height+1, this.startIdx, 0, this.endIdx, spectrumImage.getHeight(null), null);
            //scaledImage.getGraphics().drawRect(0, 0, plotRect.width, plotRect.height);
            
            
            scaledStartIndex = this.startIdx;
            scaledEndIndex = this.endIdx;

            try {
            
                System.out.println("DEBUG plotPoints - rebuild scaled image");
                System.out.println("Sample rate: " + theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE));
                System.out.println("Frame size: " + theSignal.getIntMetadata(Signal.PROP_FRAME_SIZE));
                System.out.println("Overlap size: " + theSignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE));
                System.out.println("Start index: " + this.startIdx);
                System.out.println("End Index: " + this.endIdx);
            } catch (noMetadataException ex) {
                throw new RuntimeException("Missing metadata in Signal!",ex);
            }
        }
        graphics.drawImage(scaledImage, plotRect.x, plotRect.y, null);
    }

    private void produceHighResSpectrumImage() throws RuntimeException {
        try {
            
            System.out.println("Extracting spectrum for " + theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
            //Setup reader
            AudioFileReadClass reader = new AudioFileReadClass();
            reader.setDownmix(true);
            int frameSize = 512;
            double frameLen = (double)frameSize/22050.0;
            reader.setOutputFrameLength(frameLen);
            reader.setOverlapPercent(0.5);
            reader.setReadFrameSize(40960);
            reader.setLimitAudioFrames(false);
            //reader.setMaxOutFrames(25000);
            reader.setVerbose(true);
            

            this.theSignal = reader.initialise(theSignal);

            int srate = theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE);

//            if (srate >= 44100)
//            {
//                reader.setOutputFrameLength((double)(frameSize * 2) / (double)srate);
//                theSignal = reader.initialise(theSignal);
//            }
            
            System.out.println("Using sample rate: " + srate);
            System.out.println("DEBUG produceHighResSpectrumImage start");
            System.out.println("Sample rate: " + srate);
            frameSize = theSignal.getIntMetadata(Signal.PROP_FRAME_SIZE);
            System.out.println("Frame size: " + frameSize);
            System.out.println("Overlap size: " + theSignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE));

            //System.out.flush();
            
            //setup FFT and window
            FFTRealClass fft = new FFTRealClass();
            
            boolean downsample = false;
            int downSampleFrameSize = frameSize;
            if (srate >= 44100){
                downsample = true;
                downSampleFrameSize = frameSize / 2;
            }
            
            double[] win;
            win = new double[downSampleFrameSize];
            for (int j = 0; j < downSampleFrameSize; j++) {
                win[j] = (0.54 - 0.46 * Math.cos(2 * Math.PI * j / ((downSampleFrameSize/2)-1)));
            }

            ArrayList<float[]> spectrumFrames = new ArrayList<float[]>(30000);
            //read and FFT all data
            double[][] readFrame = null;
            double[] downSampleFrame = null;
            //int increment = (int)((double)frameSize * (1.0 - reader.getOverlapPercent()));
            //int done = 0;
            //double dur = 0.0;
//            try {
//                dur = theSignal.getTimeStamp(theSignal.getNumRows()-1);
//            } catch (noMetadataException ex) {
//                ex.printStackTrace();
//            }
//            int todo = ((int)(dur * srate) - (frameSize - increment)) / increment;
            float ymax = Float.NEGATIVE_INFINITY;
            float ymin = Float.POSITIVE_INFINITY;
            

            boolean cont = true;
            while((spectrumFrames.size() < 30000)&&cont)
            {
                readFrame = reader.getDataFrame();//downmixing to mono so ignore right channel
                if(readFrame != null)
                {
                    downSampleFrame = new double[downSampleFrameSize];
                    if (downsample){
                        //hamming window
                        for (int j = 0; j < downSampleFrameSize; j++) {
                            downSampleFrame[j] = win[j] * ((readFrame[0][j*2] + readFrame[0][(j*2)+1]) / 2);
                        }
                    }
                    else{
                        //skip downsampling
                        
                        //hamming window
                        for (int j = 0; j < downSampleFrameSize; j++) {
                            downSampleFrame[j] = win[j] * readFrame[0][j];
                        }
                    }
                    //fft magnitudes
                    double[] mag = fft.fftScaledMagnitude(downSampleFrame);
                    float[] floatMag = new float[mag.length];
                    for (int i = 0; i < floatMag.length; i++) {
                        if (mag[i] > 0.0f)
                        {
                            floatMag[i] = 10.0f * (float)Math.log10(mag[i] / 10e-12);
                            if (floatMag[i] > ymax) {
                                ymax = floatMag[i];
                            }else if (floatMag[i] < ymin) {
                                ymin = floatMag[i];
                            }
                        }else{
                            floatMag[i] = Float.MIN_VALUE;
                        }
//                        floatMag[i] = (float)mag[i];
                        
                        
                    }
                    spectrumFrames.add(floatMag);
                }else{
                    //System.out.println("produced " + numFrames + " frames");
                    cont = false;
                }
            }
            System.out.println("done spectra extraction");
            System.out.println("\tymin: " + ymin + ", ymax: " + ymax);

            reader = null;
            readFrame = null;
            downSampleFrame = null;
            fft = null;
            win = null;

            if (spectrumFrames.size() < 2){
                throw new RuntimeException("insufficient audio frames, " + spectrumFrames.size() + " were returned to plot a spectrum!");
            }
            
            int length = spectrumFrames.size();
            int freqPoints = spectrumFrames.get(0).length;
            System.out.println("producing hi-res spectrum image from " + length + " frames and " + freqPoints + " frequency points");
            
            int xSize = 1;
            int ySize = 1;

            // Coordinates of the time-frequency points
            int[] xCoords = new int[length];
            int[] yCoords = new int[freqPoints];
            
            //float range = ymax - ymin;
            GraphicsConfiguration gc = getGraphicsConfiguration();
            if (gc == null)
            {
                throw new RuntimeException("Graphics configuration was null!");
            }
            spectrumImage = gc.createCompatibleImage(length, freqPoints);
            if (spectrumImage == null)
            {
                throw new RuntimeException("created image as null!");
            }
            
            float range = ymax - ymin;
//    GraphicsConfiguration gc = getGraphicsConfiguration();
//    if (gc == null)
//    {
//        throw new RuntimeException("Graphics configuration was null!");
//    }
//    spectrumImage = gc.createCompatibleImage(320, 128);
//    if (spectrumImage == null)
//    {
//        throw new RuntimeException("created image as null!");
//    }

            
            
            
            Graphics2D gImg = (Graphics2D)spectrumImage.getGraphics();
            
            for (int i = 0; i < length; i++) {
                float[] row = spectrumFrames.remove(0);
                    
                for (int j = 0; j < freqPoints; j++){
                    xCoords[i] = i;
//                    int ymod = j;
//                    int ymod2 = j+1;
                    yCoords[j] = freqPoints - j;
                    
//                    if (row[j] < 0.0f){//ymin ){
//                       row[j] = 0.0f;
//                    }else{
//                        //row[j] = (row[j] - ymin)/range;//ymax;
//                        row[j] = row[j]/ymax;
//                    }
//                    row[j] = (row[j] - ymin) / range;
                    if (row[j] == Float.MIN_VALUE){
                        row[j] = 0.0f;
                    }else{
                        row[j] = (row[j] - ymin)/range;
                    }
                    
                    Color c;
                    if (inColour)
                    {
                        c = cmap.mapToColor(row[j]);
                    }else{
                        c = new Color((1.0f-row[j]), (1.0f-row[j]), (1.0f-row[j]));
                    }
                    gImg.setColor(c);
                    gImg.fillRect(xCoords[i], yCoords[j], xSize,  ySize);
                }
            }

            //gImg.dispose();
            
            System.out.println("DEBUG produceHighResSpectrumImage end");
            System.out.println("Sample rate: " + theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE));
            System.out.println("Frame size: " + theSignal.getIntMetadata(Signal.PROP_FRAME_SIZE));
            System.out.println("Overlap size: " + theSignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE));
            
        } catch (noMetadataException ex) {
            throw new RuntimeException("NoMetadataException occured when producing plot!",ex);
        }
        this.setPlotTimeIndices(startTime, endTime);
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
