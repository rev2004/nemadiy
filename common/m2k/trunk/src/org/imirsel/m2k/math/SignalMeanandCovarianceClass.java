/*
 * SignalMeanandVariance.java
 *
 * Created on 10 February 2005, 12:31
 */

package org.imirsel.m2k.math;

import Jama.Matrix;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.io.Serializable;

/**
 * A class that calculates the mean and flattened covariance of the data matrix of an
 * input signal object. 
 * @author Kris West
 */
public class SignalMeanandCovarianceClass implements Serializable
{
    private boolean verbose = false;
    
    public SignalMeanandCovarianceClass(){
        
    }

    /**
     * Calculate means and flattened covariances of input Signal objects
     */
    public Signal summarise(Signal theSig) throws noMetadataException {
        
        if (theSig.getNumRows() < 3)
        {
            throw new RuntimeException("SignalMeanandCovarianceClass: There must be at least three rows of data to calculate a mean and covariance. Found " + theSig.getNumRows() + " in " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION));
        }
        
        if (theSig.getNumCols() < 1)
        {
            throw new RuntimeException("SignalMeanandCovarianceClass: There must be at least one column of data to calculate a mean and covariance. Found " + theSig.getNumCols()+ " in " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION));
        }
                
        String[] colLabels = null;
        
        try
        {
            colLabels = theSig.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
        }
        catch (org.imirsel.m2k.util.noMetadataException ex)
        {
            throw new RuntimeException("ERROR! No column labels",ex);
        }
        
        String[] oldColNames = theSig.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
        double[][] result = this.meanAndCovariance(theSig.getData());
        String[] newColNames = getNewColNames(oldColNames);
        
        theSig.deleteData();
        theSig.appendMatrix(result, newColNames);
        
        return theSig;
    }
    
    public static Matrix[] reconstructMeanAndCovarianceMatrices(Signal aMeanAndCovarSignal){
        try {
            String[] colNames = aMeanAndCovarSignal.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
            int numColNames = 0;
            //get means
            while(colNames[numColNames].startsWith("Mean_")){
                numColNames++;
            }
            double[][] theData = aMeanAndCovarSignal.getData();
            double[][] means = new double[numColNames][1];
            for (int i = 0; i < numColNames; i++) {
                means[i][0] = theData[i][0];
            }
            
            Matrix meansMat = new Matrix(means);
            
            double[][] covariance = new double[numColNames][numColNames];
            for (int i = 0; i < numColNames; i++) {
                covariance[i][i] = theData[i + numColNames][0];
            }
            
            int offset = 2 * numColNames;
            int covarCount = 0;
            for (int i = 0; i < numColNames; i++) {
                for (int j = i+1; j < numColNames; j++) {
                    covariance[i][j] = theData[offset + covarCount][0];
                    covariance[j][i] = theData[offset + covarCount][0];
                    covarCount++;
                }
            }
            
            Matrix covarMat = new Matrix(covariance);
            
            Matrix[] out = new Matrix[2];
            out[0] = meansMat;
            out[1] = covarMat;
            return out;
        } catch (noMetadataException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Return mean and variance of a window of the Signal
     * @param window The windowed data
     * @return The mean and variances of the window
     */
    private double[][] meanAndCovariance(double[][] data)
    {
        int cols = data.length;
        int rows = data[0].length;
        int numNewCols = (((cols * cols) - cols) / 2) + (2 * cols);
        if (getVerbose()){
            System.out.println("Calculating means and covariances of " + cols + " columns and " + rows + " rows to produce " + numNewCols + " summary features");
        }
        double[][] result = new double[numNewCols][1];
        
        for (int i=0;i<cols;i++)
        {
            //calc means
            for (int j = 0; j < rows; j++) {
                result[i][0] += data[i][j];
            }
            result[i][0] /= data[i].length;
            //calc variances
            for (int j = 0; j < rows; j++) {
                result[i + cols][0] += Math.pow(data[i][j] - result[i][0],2.0);
            }
            result[i + cols][0] /= data[i].length;
        }
        
        int covarCount = 0;
        for (int i=0;i<cols;i++)
        {
            //calculate covariances
            for (int j = i+1; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    result[(2*cols) + covarCount][0] += ((data[i][k] - result[i][0]) * (data[j][k] - result[j][0]));
                }
                result[(2*cols) + covarCount][0] /= data[i].length;
                covarCount++;
            }
        }
        
        return result;
    }
    
    private String[] getNewColNames(String[] colNames){
        int cols = colNames.length;
        String[] newNames = new String[(((cols * cols) - cols) / 2) + (2 * cols)];
        
        int covarCount = 0;
        for (int i = 0; i < colNames.length; i++) {
            newNames[i] = "Mean_" + colNames[i];
            newNames[i + cols] = "Var_" + colNames[i];
            for (int j = i+1; j < colNames.length; j++) {
                newNames[(2*cols) + covarCount] = "Covar_" + colNames[i] + "_" + colNames[j];
                covarCount++;
            }
        }
        return newNames;
    }

    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    
}
