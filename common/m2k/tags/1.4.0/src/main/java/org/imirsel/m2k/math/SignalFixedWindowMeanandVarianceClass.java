/*
 * SignalWindowedMeanandVariance.java
 *
 * Created on 10 February 2005, 12:31
 */

package org.imirsel.m2k.math;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 * A class that calculates the mean and variance of the data matrix of an
 * input signal object.
 *
 * @author Kris West
 */
public class SignalFixedWindowMeanandVarianceClass  {
    
    /**
     * Calculate means and Variances of input Signal objects
     */
    public static Signal fixedWindowmeanAndVariance(Signal theSig, double windowSizeSecs, double windowOverlap) throws noMetadataException{
        if (theSig.getNumRows() > 3) {
            
            int srate = theSig.getIntMetadata(Signal.PROP_SAMPLE_RATE);
            int frameSize = theSig.getIntMetadata(Signal.PROP_FRAME_SIZE);
            int overlap = theSig.getIntMetadata((Signal.PROP_OVERLAP_SIZE));
            
            int windowSize = (int)((windowSizeSecs * srate) / (frameSize - overlap));
            
            
            double[][] result = fixedWindowmeanAndVariance(theSig.getData(), windowSize, windowOverlap);
                
            
            
            String[] oldColLabels = theSig.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
            String[] colLabels = new String[result.length];
            for (int i=0;i<theSig.getNumCols();i++) {
                colLabels[i] = oldColLabels[i] + "_Mean";
                colLabels[i+result.length/2] = oldColLabels[i] + "_Var";
            }
            
            Signal newSig = theSig.cloneNoData();
            newSig.appendMatrix(result, colLabels);
            
            
            //work out time indices of windows
            int rows = newSig.getNumRows();
            double[] onsetTimes = new double[rows];
            onsetTimes[0] = 0.0;
            double increment = (1.0 - windowOverlap) * windowSize;
            for(int i=1;i<rows;i++){
                onsetTimes[i] = onsetTimes[i-1] + increment;
            }
            newSig.setMetadata(Signal.PROP_ONSET_TIMES, onsetTimes);

            
            return newSig;
        } else {
            throw new RuntimeException("ERROR: Signal too short for means and variances!");
        }
    }
    
    /**
     * Return mean and variance of a window of the Signal
     * @param window The windowed data
     * @return The mean and variances of the window
     */
    private static double[][] fixedWindowmeanAndVariance(double[][] input, int windowSize, double windowOverlap) {
        int cols = input.length;
        int rows = input[0].length;
        int increment = (int)(windowSize * (1.0 - windowOverlap));
        int numOut = 1 + ((rows - windowSize) / increment);
        double[][] result = new double[(((cols * cols) - cols) / 2) + (2 * cols)][numOut];

        int start = 0;
        int end = windowSize;
        for (int i = 0; i < numOut; i++) {
                
            for (int j=0;j<input.length;j++) {
                result[j][0] = mean(input[j],start, end);
                result[j+input.length][0] = variance(input[j],result[j][0],start, end);
            }
            start += increment;
            end += increment;
        }
        System.out.println("fixedWindowmeanAndVariance: returning " + numOut + " fixed window rows from " + rows + " rows of features with a windows size of " + windowSize + " frames");
        return result;
    }
    
    /**
     * Calculates the mean of an input array of doubles
     *
     * @param input the array for which the mean is calculated
     *
     * @return output the mean of the input array
     */
    public static double mean(double[] input, int start, int end) {
        double output = 0.0;
        for(int i = start; i < end; i++) {
            output += input[i];
        }
        output = output/(double)input.length;
        return output;
    }
    
    /**
     * Calculates the variance of an input array of doubles
     *
     * @param input the array for which the variance is calculated
     *
     * @return output the variance of the input array
     */
    public static double variance(double[] input, double mean, int start, int end) {
        double output = 0.0;
        for(int i = start; i < end; i++) {
            output += (input[i] - mean) * (input[i] - mean);
        }
        output = output/(double)input.length;
        return output;
    }
    
}
