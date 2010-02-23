/*
 * SignalWindowedMeanandVariance.java
 *
 * Created on 10 February 2005, 12:31
 */
package org.imirsel.m2k.math;

import java.util.ArrayList;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 * A class that calculates the mean and variance of the data matrix of an
 * input signal object.
 *
 * @author Kris West
 */
public class StreamFixedWindowMeanandCovarianceClass {
    private double windowSizeSecs = 10.0;
    private double windowOverlap = 0.5;
    
    private int windowSize;
    private int increment;
    
    private ArrayList<double[]> receivedFrames = null;
    
    public StreamFixedWindowMeanandCovarianceClass(double windowSizeSecs_, double windowOverlap_){
        windowSizeSecs = windowSizeSecs_;
        windowOverlap = windowOverlap_;
    }
    
    public Signal setSignalSampleRateAndCalcWindowSize(Signal theSig){
        try{
            int srate = theSig.getIntMetadata(Signal.PROP_SAMPLE_RATE);
            int frameSize = theSig.getIntMetadata(Signal.PROP_FRAME_SIZE);
            int overlap = theSig.getIntMetadata((Signal.PROP_OVERLAP_SIZE));
        
            windowSize = 1 + (int) (((windowSizeSecs * (double)srate) - frameSize) / (double) (frameSize - overlap));
            increment = (int)((1.0 - windowOverlap) * windowSize);
            receivedFrames = new ArrayList<double[]>();
            
            System.out.println("StreamFixedWindowMeanandCovarianceClass - setting up for " + windowSize + " vector windows with increment of " + increment + " samples");
            
            double[] onsetTimes = new double[500];
            for(int i=0;i<onsetTimes.length;i++){
                onsetTimes[i] = i * (1.0 - windowOverlap) * windowSizeSecs;
            }
            theSig.setMetadata(Signal.PROP_ONSET_TIMES, onsetTimes);
            return theSig;
        }catch(noMetadataException nme){
            throw new RuntimeException("Required metadata not found!",nme);
        }
    }
    
    /** Receives a frame of features. When sufficient frames have been received
     * a summary is output otherwise null is output.
     * 
     * @param aFrame
     * @return Null or a double[] of summary feautres if sufficient frames have
     * been received.
     */
    public double[] receiveFrame(double[] aFrame){
        receivedFrames.add(aFrame);

        if (receivedFrames.size() == windowSize){
            return fixedWindowMeanAndCovariance();
        }else{
            return null;
        }
    }
    

    /**
     * Return mean and covariance of all the windows of the Signal
     * @param window The windowed data
     * @return The mean and variances of the window
     */
    private double[] fixedWindowMeanAndCovariance() {
        int cols = receivedFrames.get(0).length;
        
        double[] result = new double[(((cols * cols) - cols) / 2) + (2 * cols)];
        
        for (int i = 0; i < cols; i++) {
            //calc means
            for (int j = 0; j < receivedFrames.size(); j++) {
                result[i] += receivedFrames.get(j)[i];
            }
            result[i] /= windowSize;
            //calc variances
            for (int j = 0; j < receivedFrames.size(); j++) {
                result[i + cols] += Math.pow(receivedFrames.get(j)[i] - result[i], 2.0);
            }
            result[i + cols] /= windowSize;
        }

        int covarCount = 0;
        for (int i = 0; i < cols; i++) {
            //calculate covariances
            for (int j = i + 1; j < cols; j++) {
                int covarCol = (2 * cols) + covarCount;
                for (int k = 0; k < receivedFrames.size(); k++) {
                    double[] frame = receivedFrames.get(k);
                    result[covarCol] += ((frame[i] - result[i]) * (frame[j] - result[j]));
                }
                result[covarCol] /= windowSize;
                covarCount++;
            }
        }
        
        
        for (int i = 0; i < increment; i++) {
            receivedFrames.remove(0);
        }

        return result;
    }

    public double getWindowSizeSecs() {
        return windowSizeSecs;
    }

    public void setWindowSizeSecs(double windowSizeSecs) {
        this.windowSizeSecs = windowSizeSecs;
    }

    public double getWindowOverlap() {
        return windowOverlap;
    }

    public void setWindowOverlap(double windowOverlap) {
        this.windowOverlap = windowOverlap;
    }
}
