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
public class SignalSegmentedMeanandVarianceClass 
{
    boolean appendSegmentLength = false;
    boolean clone = true;
    String featureSkipList = "";
    
    public SignalSegmentedMeanandVarianceClass(){
        
    }
    
    /**
     * Returns the comma separated list of feature name components to skip the
     * calculation of variances for.
     * @return comma separated list of feature name components
     */
    public String getFeatureSkipList()
    {
        return this.featureSkipList;
    }
    
    /**
     * Sets the comma separated list of feature name components to skip the
     * calculation of variances for.
     * @param val comma separated list of feature name components.
     */
    public void setFeatureSkipList(String val)
    {
        this.featureSkipList = val;
    }
    
    /**
     * Produces an ArrayList of columnNames (containing one or more of the 
     * components from the skip listthat the variance calculation should
     * be skipped for.
     * @param columnNames The Signal's column names.
     * @return The list of columns to be skipped.
     */
    public ArrayList produceSkipList(String[] columnNames)
    {
        ArrayList theList = new ArrayList();
        ArrayList skipList = new ArrayList();
        if(!this.featureSkipList.equals(""))
        {
            String[] theArray = this.featureSkipList.split(",");
            for(int i=0;i<theArray.length;i++)
            {
                String skip = theArray[i].trim().toLowerCase();
                if (!skip.equals(""))
                {
                    theList.add(skip);
                }
            }
            
            for (int i=0;i<columnNames.length;i++)
            {
                Integer colNum = new Integer(i);
                String col = columnNames[i].trim().toLowerCase();
                for(int y=0;y<theList.size();y++)
                {
                    if (col.indexOf((String)theList.get(y)) >= 0)
                    {
                        skipList.add(colNum);
                        break;
                    }
                }
            }
            
            return skipList;
        }
        else
        {
            return skipList;
        }
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
            double lastDiff = 0.0;
            double currentDiff = Math.abs(theSig.getTimeStamp(0) - onsetTimes[onsetNum]);
            double nextDiff = Math.abs(theSig.getTimeStamp(1) - onsetTimes[onsetNum]);
            //increment to a safe onset (very early onsets can cause probs)
            while (nextDiff > currentDiff)
            {
                onsetNum++;
                currentDiff = Math.abs(theSig.getTimeStamp(0) - onsetTimes[onsetNum]);
                nextDiff = Math.abs(theSig.getTimeStamp(1) - onsetTimes[onsetNum]);
            }

            for(int i=1;i<(theSig.getNumRows()-1);i++)
            {
                lastDiff = currentDiff;
                currentDiff = nextDiff;
                nextDiff = Math.abs(theSig.getTimeStamp(i+1) - onsetTimes[onsetNum]);
                if(currentDiff <= nextDiff)
                {
                    segmentationCol[i] = 1;
                    onsetNum++;
                    if (onsetNum >= onsetTimes.length)
                    {
                        break;
                    }else
                    {
                        currentDiff = Math.abs(theSig.getTimeStamp(i) - onsetTimes[onsetNum]);
                        nextDiff = Math.abs(theSig.getTimeStamp(i+1) - onsetTimes[onsetNum]);
                    }
                }else
                {
                    segmentationCol[i] = 0;
                }
            }
        }
        theSkipList = this.produceSkipList(colLabels);
        
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
            result = new double[(theSig.getNumCols()*2) - theSkipList.size()][numOnsets-1];
        }
        else
        {
            result = new double[(theSig.getNumCols()*2) + (1 - theSkipList.size())][numOnsets-1];
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
                    row = meanAndVariance(theSig.getData(),start,end,theSkipList);
                    
                    for (int j=0;j<row.length;j++)
                    {
                        result[j][resultCount] = row[j];
                    }
                    if (appendSegmentLength == true)
                    {
                        result[(result.length - 1)][resultCount] = (double)(end - start);
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
                if (!theSkipList.contains(column))
                {
                    newColLabels[i+theSig.getNumCols()-skipped] = colLabels[i] + "_SegVar";
                }
                else
                {
                    skipped++;
                }
            }
            newColLabels[result.length-1] = "Seg length";
        }
        else
        {
            for (int i=0;i<theSig.getNumCols();i++)
            {
                Integer column = new Integer(i);
                newColLabels[i] = colLabels[i] + "_SegMean";
                if (!theSkipList.contains(column))
                {
                    newColLabels[i+theSig.getNumCols()-skipped] = colLabels[i] + "_SegVar";
                }
                else
                {
                    skipped++;
                }
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
    private double[] meanAndVariance(double[][] window, List skipList)
    {
        double[] result = new double[window[0].length * 2];
        
        int skipped = 0;
        for (int j=0;j<window.length;j++)
        {
            if (skipList.indexOf(new Integer(j)) != -1)
            {
                result[j] = Mathematics.mean(window[j]);
                skipped++;
            }
            else
            {
                result[j] = Mathematics.mean(window[j]);
                result[j+window.length-skipped] = Mathematics.variance(window[j]);
            }
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
    private double[] meanAndVariance(double[][] data, int start, int end, ArrayList skipList)
    {
        if (start > end)
        {
            throw new RuntimeException("The start cannot be after the end!");
        }
        else if(start == end)
        {
            throw new RuntimeException("The start cannot be the same frame as the end!");
        }
        
        double[] result = new double[(data.length * 2)-skipList.size()];
        int skipped = 0;
        for (int j=0;j<data.length;j++)
        {
            Integer column = new Integer(j);
            if (skipList.contains(column))
            {
                for (int i=start;i<end;i++)
                {
                    result[j] += data[j][i];
                }
                result[j] /= end-start;
                skipped++;
            }
            else
            {
                for (int i=start;i<end;i++)
                {
                    try{
                        result[j] += data[j][i];
                        result[(j+data.length)-skipped] += data[j][i] * data[j][i];
                    }
                    catch(java.lang.ArrayIndexOutOfBoundsException aiobe)
                    {
                        aiobe.printStackTrace();
                        throw new RuntimeException("result.length: " + result.length + ", data.length: " + data.length + ", row: " + i + ", col: " + j + ", skipped: " + skipped + ", (j+data.length)-skipped:" + ((j+data.length)-skipped));
                    }
                }
                result[j] /= end-start;
                result[(j+data.length)-skipped] /= end-start;
                result[(j+data.length)-skipped] -= result[j] * result[j];
                
            }
        }
        
        return result;
    }
    
    
}
