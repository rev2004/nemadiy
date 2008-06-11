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
public class SignalMeanDynamicThresholdClass {
    
    double thresholdWindowLength = 0.1;
    //double forwardThresholdWindowLength = 0.05;
    double minimumThreshold = 0.0;
    double peakIsolationWindowLength = 0.05;
    
    int thresholdWindow = 16;
    int forwardThresholdWindow = 16;
    int peakIsolationWindow = 3;
    
    /** Creates a new instance of SignalMedianDynamicThresholdClass */
    public SignalMeanDynamicThresholdClass(double minimumThreshold_, double thresholdWindowLength_, double peakIsolationWindowLength_) {//add on the centreWindow and decayWindow options
        minimumThreshold = minimumThreshold_;
        thresholdWindowLength = thresholdWindowLength_;
        peakIsolationWindowLength = peakIsolationWindowLength_;
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
        threshold(theSignal, function, false);
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
        this.thresholdWindow = Math.round(((float)this.thresholdWindowLength * (float)sampleRate) / ((float)frameSize - overlapSize));
        //this.forwardThresholdWindow = Math.round(((float)this.forwardThresholdWindowLength * (float)sampleRate) / ((float)frameSize - overlapSize));
        this.peakIsolationWindow = Math.round(((float)this.peakIsolationWindowLength * (float)sampleRate) / ((float)frameSize - overlapSize));
        
        double[] thresholds = new double[function.length];
        
        int numPeaks = 0;
        int windowPosition = 0;
        double silStart = 0.0;
        double thesholdSum = 0.0;
        double[] meanWin = new double[thresholdWindow + 1];
        int winPos = 0;
        
        
        int start = thresholdWindow;
        
        //populate mean window
        for (int i = 0; i < meanWin.length; i++) {
            meanWin[i] = function[i];
        }
        winPos = thresholdWindow;
        
        //Do thresholding, no onset in first isolation window
        //for (int i=thresholdWindow + 1; i<(function.length - forwardThresholdWindow); i++)
        int s = 0;
        if ((thresholdWindow + 1)<peakIsolationWindow)
        {
            s=peakIsolationWindow;
        }else{
            s=thresholdWindow + 1;
        }
        
        int e = function.length - peakIsolationWindow;
        
        for (int i=s; i<e; i++)
        {
            meanWin[winPos] = function[i];
            winPos++;
            if (winPos >= meanWin.length){
                winPos = 0;
            }

            thresholds[i] = Mathematics.mean(meanWin) + this.minimumThreshold;

            if(function[i] > thresholds[i]){
                //scan backward isolation window to ensure we are picking a peak
                boolean isBiggest = true;

                /*for (int j=1;j<forwardThresholdWindow;j++)
                {
                    if (function[i-j] > function[i]){
                        isBiggest = false;
                        break;
                    }
                    if(function[i+j] > function[i])
                    {
                        isBiggest = false;
                        break;
                    }
                }*/
                
                for (int j = 1; j <= peakIsolationWindow; j++) {
                    if (function[i-j] >= function[i]){
                        isBiggest = false;
                        break;
                    }
                    if(function[i+j] > function[i])
                    {
                        isBiggest = false;
                        break;
                    }
                }
                
                if ((isBiggest == true))
                {
                    peaks[i] = 1;
                    numPeaks++;
                    silStart = theSignal.getTimeStamp(i);
                }
            }
            if ((theSignal.getTimeStamp(i) - silStart) > 3){//segment silences after 3 secs
                peaks[i] = 1;
                numPeaks++;
                silStart = theSignal.getTimeStamp(i);
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
        //System.out.println("Found " + peakCount + " onsets");
        theSignal.setMetadata(Signal.PROP_ONSET_TIMES, onsetTimes);
        if (appendThresholds){
            theSignal.appendColumn(thresholds, "Mean Dynamic threshold");
        }
    }
}
