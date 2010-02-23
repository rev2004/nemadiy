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
public class SignalFixedWindowMeanandCovarianceClass {

    /**
     * Calculate means and Variances of input Signal objects
     */
    public static Signal fixedWindowMeanAndCovariance(Signal theSig, double windowSizeSecs, double windowOverlap) throws noMetadataException {
        if (theSig.getNumRows() > 3) {

            int srate = theSig.getIntMetadata(Signal.PROP_SAMPLE_RATE);
            int frameSize = theSig.getIntMetadata(Signal.PROP_FRAME_SIZE);
            int overlap = theSig.getIntMetadata((Signal.PROP_OVERLAP_SIZE));

            int windowSize = 1 + (int) (((windowSizeSecs * (double)srate) - frameSize) / (double) (frameSize - overlap));

            int cols = theSig.getNumCols();
            double[][] result = fixedWindowMeanAndCovariance(theSig.getData(), windowSize, windowOverlap);

            String[] oldColLabels = theSig.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
            String[] colLabels = new String[result.length];
            for (int i = 0; i < theSig.getNumCols(); i++) {
                colLabels[i] = oldColLabels[i] + "_Mean";
                colLabels[i + cols] = oldColLabels[i] + "_Var";
            }
            int covarCount = 0;
            for (int i = 0; i < cols; i++) {
                for (int j = i + 1; j < cols; j++) {
                    colLabels[(2 * cols) + covarCount] = oldColLabels[i] + "_Covar_" + oldColLabels[j];
                    covarCount++;
                }
            }


            Signal newSig = theSig.cloneNoData();
            newSig.appendMatrix(result, colLabels);
            
            //work out time indices of windows
            int rows = newSig.getNumRows();
            
            System.out.println("Summarising windows for audio file: " + newSig.getMetadata(Signal.PROP_FILE_LOCATION));
            System.out.println("\trows of features: " + theSig.getNumRows() + ", summary vectors: " + result[0].length);
            System.out.println("\tcols of summary features: " + newSig.getNumCols() + " from " + cols + " raw features");
            
            System.out.println("\tNumber of frames per window: " + windowSize);
            System.out.println("\trows appended: " + rows);
            double[] onsetTimes = new double[rows];
            onsetTimes[0] = 0.0;
            double increment = (1.0 - windowOverlap) * windowSizeSecs;
            for(int i=1;i<rows;i++){
                onsetTimes[i] = onsetTimes[i-1] + increment;
            }
            System.out.print("\tonset times: ");
            for (int i = 0; i < onsetTimes.length; i++) {
                System.out.print("\t" + onsetTimes[i]);
            }

            newSig.setMetadata(Signal.PROP_ONSET_TIMES, onsetTimes);

            return newSig;
        } else {
            throw new RuntimeException("ERROR: Signal too short for means and variances!");
        }
    }

    /**
     * Return mean and covariance of all the windows of the Signal
     * @param window The windowed data
     * @return The mean and variances of the window
     */
    private static double[][] fixedWindowMeanAndCovariance(double[][] data, int windowSize, double windowOverlap) {
        int cols = data.length;
        int rows = data[0].length;
        int increment = (int)(windowSize * (1.0 - windowOverlap));
        int numOut = 1 + ((rows - windowSize) / increment);
        int numCols = (((cols * cols) - cols) / 2) + (2 * cols);
        
        double[][] result = new double[numCols][numOut];

        
        System.out.println("fixedWindowMeanAndCovariance producing " + numCols + " columns of summary features from " + cols + " raw features and " + numOut + " windows from " + rows + " raw feature frames");
        int start = 0;
        int end = windowSize;
        for (int n = 0; n < numOut; n++) {
            

            for (int i = 0; i < cols; i++) {
                //calc means
                for (int j = start; j < end; j++) {
                    result[i][n] += data[i][j];
                }
                result[i][n] /= windowSize;
                //calc variances
                for (int j = start; j < end; j++) {
                    result[i + cols][n] += Math.pow(data[i][j] - result[i][n], 2.0);
                }
                result[i + cols][n] /= windowSize;
            }

            int covarCount = 0;
            for (int i = 0; i < cols; i++) {
                //calculate covariances
                for (int j = i + 1; j < cols; j++) {
                    int covarCol = (2 * cols) + covarCount;
                    for (int k = start; k < end; k++) {
                        //note data is column major
                        result[covarCol][n] += ((data[i][k] - result[i][n]) * (data[j][k] - result[j][n]));
                    }
                    result[covarCol][n] /= windowSize;
                    covarCount++;
                }
            }
            start += increment;
            end += increment;
        }

        return result;
    }
}
