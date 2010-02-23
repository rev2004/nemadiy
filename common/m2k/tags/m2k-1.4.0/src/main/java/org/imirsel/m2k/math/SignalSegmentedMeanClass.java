/*
 * SignalMeanandVariance.java
 *
 * Created on 10 February 2005, 12:31
 */

package org.imirsel.m2k.math;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that calculates the mean and variance of the data matrix of an
 * input signal object. Onsets times in the Signal object are used to segment the features
 * and the mean and variance of the segment will be calculated.
 *
 * @author Kris West
 */
public class SignalSegmentedMeanClass 
{
    boolean appendSegmentLength = false;
    boolean clone = true;
    
    public SignalSegmentedMeanClass(){
        
    }
    
    /**
     * Returns the value of the flag controlling whether the segment length is
     * appended to the feature vector when onset detection data is
     * used to segment the data.
     * @return the value of the appendSegmentLength flag.
     */
    public boolean getAppendSegmentLength()
    {
        return appendSegmentLength;
    }
    
    /**
     * Sets the value of the flag controlling whether the segment length is
     * appended to the feature vector when onset detection data is
     * used to segment the data.
     * @param val the value of the appendSegmentLength flag
     */
    public void setAppendSegmentLength(boolean val)
    {
        appendSegmentLength = val;
    }
    
    /**
     * Calculate means and Variances of input Signal objects
     */
    public Signal summarise(Signal theSig) throws noMetadataException {
        
        if (theSig.getNumRows() < 3)
        {
            throw new RuntimeException("SignalMeanandVarianceClass: There must be at least three rows of data to calculate a mean and variance. Found " + theSig.getNumRows() + " in " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION));
        }
        
        if (theSig.getNumCols() < 1)
        {
            throw new RuntimeException("SignalMeanandVarianceClass: There must be at least one column of data to calculate a mean and variance. Found " + theSig.getNumCols()+ " in " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION));
        }
        try {
            
            theSig = (Signal)theSig.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        
        //find onset metadata and columns to skip variances of
        double[] onsetTimes = theSig.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES);
        
        String[] colLabels = null;
        ArrayList theSkipList;
        int[] segmentationCol = new int[theSig.getNumRows()];
        
        
        if (onsetTimes.length > theSig.getNumRows())
        {
            throw new RuntimeException("SignalMeanandVarianceClass: Unable to process file: " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION) + " as it has more onsets (" + onsetTimes.length + ") than rows (" + theSig.getNumRows() + ")!");
        }
        
        try
        {
            colLabels = theSig.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
        }
        catch (org.imirsel.m2k.util.noMetadataException ex)
        {
            throw new RuntimeException("ERROR! No column labels",ex);
        }
        
        int onsetNum = 0;
        if (onsetTimes.length > 0)
        {
            //double lastDiff = 0.0;
            //double currentDiff = Math.abs(theSig.getTimeStamp(0) - onsetTimes[onsetNum]);
            double timestamp = theSig.getTimeStamp(1);
            System.out.println("onset time: " + onsetTimes[onsetNum] + ", starting timestamp: " + timestamp);
            double nextDiff = timestamp - onsetTimes[onsetNum];
            
            //increment to a safe onset (very early onsets can cause probs)
            //while (nextDiff > currentDiff)
            while (nextDiff >= 0)
            {
                System.out.println("onset time: " + onsetTimes[onsetNum]);
                onsetNum++;
                //currentDiff = Math.abs(theSig.getTimeStamp(0) - onsetTimes[onsetNum]);
                nextDiff = timestamp - onsetTimes[onsetNum];
            }

            for(int i=1;i<(theSig.getNumRows()-1);i++)
            {
                //lastDiff = currentDiff;
                //currentDiff = nextDiff;
                nextDiff = theSig.getTimeStamp(i+1) - onsetTimes[onsetNum];
                if(nextDiff >= 0)
                {
                    segmentationCol[i] = 1;
                    onsetNum++;
                    if (onsetNum >= onsetTimes.length)
                    {
                        break;
                    }else
                    {
                        //currentDiff = Math.abs(theSig.getTimeStamp(i) - onsetTimes[onsetNum]);
                        nextDiff = theSig.getTimeStamp(i+1) - onsetTimes[onsetNum];
                    }
                }else
                {
                    segmentationCol[i] = 0;
                }
            }
        }
        
        //Count onsets, ignoring first row of matrix (can't be an onset)'
        int numOnsets = 0;
        for (int i=1;i<theSig.getNumRows();i++)
        {
            if (segmentationCol[i] == 1)
            {
                numOnsets++;
            }
        }
        
        if (numOnsets < 3)
        {
            System.out.println("SignalMeanandVarianceclass: Less than 3 onsets for: " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION) + "\n" + onsetTimes.length + " were in the input Signal Object and " + onsetNum + " were processed. The mean and variance across the whole file will be output.");
            numOnsets = 2;
            segmentationCol = new int[theSig.getNumRows()];
            segmentationCol[1] = 1;
            segmentationCol[segmentationCol.length-1] = 1;
        }
        
        double[][] result;
        if (appendSegmentLength == false)
        {
            result = new double[theSig.getNumCols()][numOnsets-1];
        }
        else
        {
            result = new double[theSig.getNumCols() + 1][numOnsets-1];
        }
        
        int start = -1, end = -1;
        int resultCount = 0;
        for (int i=1;i<theSig.getNumRows();i++)
        {
            if (segmentationCol[i] == 1)
            {
                if (end != -1)
                {
                    start = end;
                    end = i;
                    double[] row;
                    row = mean(theSig.getData(),start,end);
                    
                    for (int j=0;j<row.length;j++)
                    {
                        result[j][resultCount] = row[j];
                    }
                    if (appendSegmentLength == true)
                    {
                        result[(result.length - 1)][resultCount] = theSig.getTimeStamp(end-1) - theSig.getTimeStamp(start);//(double)(end - start);
                    }
                    resultCount++;
                }
                else
                {
                    end = i;
                }
            }
        }
        
        String[] newColLabels = new String[result.length];
        int skipped = 0;
        if (appendSegmentLength == true)
        {
            for (int i=0;i<theSig.getNumCols();i++)
            {
                Integer column = new Integer(i);
                newColLabels[i] = colLabels[i] + "_SegMean";
            }
            newColLabels[result.length-1] = "Seg length";
        }
        else
        {
            for (int i=0;i<theSig.getNumCols();i++)
            {
                Integer column = new Integer(i);
                newColLabels[i] = colLabels[i] + "_SegMean";
            }
        }
        
        theSig.deleteData();
        theSig.appendMatrix(result, newColLabels);
        
        return theSig;
    }
    
    /**
     * Return mean and variance of a window of the Signal
     * @param window The windowed data
     * @return The mean and variances of the window
     */
    private double[] mean(double[][] window)
    {
        double[] result = new double[window[0].length];
        
        for (int j=0;j<window.length;j++)
        {
            result[j] = Mathematics.mean(window[j]);
        }
        
        return result;
    }
    
    /**
     * Return the mean and variance of subset of the data matrx rows.
     * @param data The double [][] of data
     * @param start Start row index
     * @param end End row index
     * @return Mean and variance of window
     */
    private double[] mean(double[][] data, int start, int end)
    {
        if (start > end)
        {
            throw new RuntimeException("The start cannot be after the end!");
        }
        else if(start == end)
        {
            throw new RuntimeException("The start cannot be the same frame as the end!");
        }
        
        double[] result = new double[data.length];
        int range = end-start;
        for (int j=0;j<data.length;j++)
        {
            for (int i=start;i<end;i++)
            {
                result[j] += data[j][i];
            }
            result[j] /= range;
        }
        return result;
    }
    
    
}
