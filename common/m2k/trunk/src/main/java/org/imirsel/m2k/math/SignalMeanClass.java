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
public class SignalMeanClass  {
    
    
    /**
     * Calculate means and Variances of input Signal objects
     */
    public static Signal mean(Signal theSig) throws noMetadataException{
        if (theSig.getNumRows() > 3) {
            
            double[][] result = mean(theSig.getData());
                
            String[] oldColLabels = theSig.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
            String[] colLabels = new String[result.length];
            for (int i=0;i<theSig.getNumCols();i++) {
                colLabels[i] = oldColLabels[i] + "_Mean";
            }
            
            Signal newSig = theSig.cloneNoData();
            newSig.appendMatrix(result, colLabels);
            
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
    private static double[][] mean(double[][] input) {
        double[][] result = new double[input.length][1];
        //double[] mean = new double[window[0].length];
        //double[] var = new double[window[0].length];
        for (int j=0;j<input.length;j++) {
            result[j][0] = Mathematics.mean(input[j]);
        }
        
        return result;
    }
    
    
}
