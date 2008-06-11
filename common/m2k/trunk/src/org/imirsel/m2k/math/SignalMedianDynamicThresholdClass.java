package org.imirsel.m2k.math;

import java.util.Arrays.*;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.Set;
import java.util.HashSet;

/**
 * Calculates a Median Dynamic Threshold of a column of data, related to a 
 * Signal Object. Any onsets identified by the thresholder are appended to
 * the Signal Object as 'Onset times' metadata (for use in segmentation
 * tempo extraction etc.). The thresholds used may also be appended for
 * plotting purposes.
 * 
 * @author Kris West
 */
public class SignalMedianDynamicThresholdClass {
    
    double thresholdWeight = 0.5;
    double windowLength = 1.0; 
    double peakIsolationWindowLength = 0.1;
    double minimumThreshold = 0.0;
    double forwardPeakIsolationWindowLength = 0.1;
    
    int windowSize = 80;
    int peakIsolationWindow = 16;
    int forwardPeakIsolationWindow = 16;
    
    /** Creates a new instance of SignalMedianDynamicThresholdClass */
    public SignalMedianDynamicThresholdClass(double minimumThreshold_, double windowLength_, double peakIsolationWindowLength_, double forwardPeakIsolationWindowLength_, double thresholdWeight_) {//add on the centreWindow and decayWindow options
        minimumThreshold = minimumThreshold_;
        windowLength = windowLength_;
        peakIsolationWindowLength = peakIsolationWindowLength_;
        thresholdWeight = thresholdWeight_;
        forwardPeakIsolationWindowLength = forwardPeakIsolationWindowLength_;
    }
    
    /**
     * Applies the trheshold to  the specified Signal and data column and appends
     *  the thresholds used as a data column.
     * @param theSignal Signal Object to get metadata from and to add onset times to.
     * @param function The data column to threshold.
     * @throws org.imirsel.m2k.util.noMetadataException 
     */
    public void threshold(Signal theSignal, double[] function) throws noMetadataException
    {
        threshold(theSignal, function,false);
    }
    
    /**
     * Applies the trheshold to  the specified Signal and data column and appends
     *  the thresholds used as a data column.
     * @param theSignal Signal Object to get metadata from and to add onset times to.
     * @param function The data column to threshold.
     * @param appendThresholds Determines whether the thresholds are appended as 
     * a data column for plotting purposes
     * @throws org.imirsel.m2k.util.noMetadataException 
     */
    public void threshold(Signal theSignal, double[] function, boolean appendThresholds) throws noMetadataException
    {
        int[] peaks = new int[function.length];
        
        HashSet windowSet = null;
        int sampleRate = theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE);
        int frameSize = 1;
        int overlapSize = 0;
        try{
            frameSize = theSignal.getIntMetadata(Signal.PROP_FRAME_SIZE);
            overlapSize = theSignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE);
        }catch(noMetadataException nme){
            
        }
        this.windowSize = Math.round(((float)this.windowLength * (float)sampleRate) / (float)(frameSize - overlapSize));
        this.peakIsolationWindow = Math.round(((float)this.peakIsolationWindowLength * (float)sampleRate) / ((float)frameSize - overlapSize));
        this.forwardPeakIsolationWindow = Math.round(((float)this.forwardPeakIsolationWindowLength* (float)sampleRate) / ((float)frameSize - overlapSize));
        
        double[] window = new double[this.windowSize];
        double[] sortedWindow = new double[this.windowSize];
        int medianPoint = (int)Math.floor(((double)windowSize) / 2.0);
        double[] thresholds = new double[function.length];
        
        int numPeaks = 0;
        int windowPosition = 0;
        double silStart = 0.0;
        
        //Do thresholding, no onset in first isolation window
        for (int i=peakIsolationWindow-1; i<(function.length - forwardPeakIsolationWindow)+1; i++)
        {
            window[windowPosition] = function[i-1];
            windowPosition++;
            if (windowPosition >= this.windowSize){
                windowPosition = 0;
            }
            
            
                for(int j=0;j<window.length;j++){
                    sortedWindow[j] = window[j];
                }
                java.util.Arrays.sort(sortedWindow);
                //System.out.println("sortedWindow.length: " + sortedWindow.length + ", medianPoint: " + medianPoint + ", windowSet.size(): " + windowSet.size() + ", winPos: " + winPos);

                thresholds[i] = thresholdWeight * sortedWindow[medianPoint];
                if (thresholds[i] < minimumThreshold)
                {
                    thresholds[i] = minimumThreshold;
                }
                
                if(function[i] > thresholds[i]){
                    //scan backward isolation window to ensure we are picking highest peak
                    boolean isBiggest = true;
                    
                    for (int j=1;j<peakIsolationWindow;j++)
                    {
                        //if ((i-j) >= 0)
                        //{
                            if(peaks[i-j] > 0)
                            {
                                isBiggest = false;
                                break;
                            }
                        //}
                    }
                    
                    for (int j=1;j<forwardPeakIsolationWindow;j++)
                    {
                        //if ((i+j) < function.length)
                        //{
                            if(function[i+j] > function[i])
                            {
                                isBiggest = false;
                                break;
                            }
                        //}
                    }

                    if ((isBiggest == true))
                    {
                        peaks[i] = 1;
                        numPeaks++;
                        silStart = theSignal.getTimeStamp(i);
                    }else if ((theSignal.getTimeStamp(i) - silStart) > 3){//segment silences after 3 secs
                        peaks[i] = 1;
                        numPeaks++;
                        silStart = theSignal.getTimeStamp(i);
                    }
                }
            
        }
        
        int peakCount = 0;
        double[] onsetTimes = new double[numPeaks];
        //Calc onset times and append
        for(int i=0;i<peaks.length;i++)
        {
            if (peaks[i] == 1) 
            {
                onsetTimes[peakCount] = theSignal.getTimeStamp(i);
                peakCount++;
            }
        }
        System.out.println("Found " + peakCount + " onsets");
        theSignal.setMetadata(Signal.PROP_ONSET_TIMES, onsetTimes);
        if (appendThresholds){
            theSignal.appendColumn(thresholds, "Median Dynamic threshold");
        }
    }
}
