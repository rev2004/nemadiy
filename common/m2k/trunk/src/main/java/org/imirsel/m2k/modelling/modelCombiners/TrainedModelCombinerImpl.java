/*
 * TrainedModelCombinerImpl.java
 *
 * Created on July 29, 2005, 1:44 PM
 *
 */

package org.imirsel.m2k.modelling.modelCombiners;

import org.imirsel.m2k.util.Signal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.imirsel.m2k.util.noMetadataException;
import java.io.Serializable;

/**
 * An abstract base class to extend model combiners from.
 * @author Kris West
 */
public abstract class TrainedModelCombinerImpl  implements TrainedModelCombiner{
    int numClassifiers = -1;
    List classNames = null;
    boolean verbose = true;
    boolean isTrained = false;
    
    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public abstract String getCombinerName();
    
    /**
     * Creates a new instance of TrainedModelCombinerImpl
     */
    public TrainedModelCombinerImpl() {
    }
    
    /**
     * Sets the value of the verbose output flag
     * @param value the value which verbose is set to
     * @see #verbose
     */
    public void setVerbose(boolean value) {
        this.verbose = value;
    }
    
    /**
     * Returns the value of the verbose output flag
     * @return verbose
     * @see #verbose
     */
    public boolean getVerbose() {
        return this.verbose;
    }
    
    /**
     * Returns an ArrayList of the class names found in the data that the classifier
     * was trained on. This ArrayList should be ordered such that the integers returned
     * from the classification methods index the correct class names. Should return 
     * null if untrained.
     * @return an ArrayList containing the class names.
     */
    public List getClassNames()
    {
        return this.classNames;
    }
    
    /**
     * Sets the List of the class names in the data that the classifier
     * will be trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. `
     * @param classNames_ an ArrayList of the class names found in the data that the classifier
     * was trained on. 
     */
    public void setClassNames(List classNames_)
    {
        Collections.sort(classNames_);
        this.classNames = classNames_;
    }
    
    /**
     * Returns an integer indicating the number of classifiers the combiner was 
     * trained to combine the likelihoods of.
     * @return an integer indicating the number of classifiers the combiner was 
     * trained to combine the likelihoods of.
     */
    public int getNumClassifiers()
    {
        return this.numClassifiers;
    }
    
    /**
     * Returns an integer indicating the number of classes the component
     * classifiers were trained to classify. This will be the same dimension
     * as the number of likelihoods output by each classifier.
     * @return an integer indicating the number of classes the component
     * classifiers were trained to classify. This will be the same dimension
     * as the number of likelihoods output by each classifier.
     */
    public int getNumClasses()
    {
        return this.classNames.size();
    }
    
    /**
     * Returns a flag indicating whether a classifier has been trained or not.
     * @return a flag indicating whether a classifier has been trained or not.
     */
    public boolean isTrained()
    {
        return this.isTrained;
    }
    
    /**
     * Sets a flag indicating whether a classifier has been trained or not.
     * @param val a flag indicating whether a classifier has been trained or not.
     */
    public void setIsTrained(boolean val)
    {
        this.isTrained = val;
    }
    
    /**
     * Utility method for calculating covariance matrices
     * @param approxErrors Data to calculate covariance matrix of.
     * @return The covariance metrix of the data
     */
    public double[][] calcCovarianceMatrix(double[][] approxErrors)
    {
        double[][] covar = new double[approxErrors.length][approxErrors.length];
        double[] mean_vec = new double[approxErrors.length];
        
        for(int i=0;i<approxErrors.length;i++)
        {
            mean_vec[i] = 0.0;
            for (int y=0;y<approxErrors[i].length;y++)
            {
                mean_vec[i] += approxErrors[i][y];
            }
            mean_vec[i] /= approxErrors[i].length;
        }
        for (int c1=0; c1<approxErrors.length; c1++)
        {
            for (int c2=0; c2<approxErrors.length; c2++)
            {
                double sum = 0.0;
                for (int y=0; y<approxErrors[0].length; y++)
                {
                    sum += (approxErrors[c1][y] - mean_vec[c1]) * (approxErrors[c2][y] - mean_vec[c2]);
                }
                covar[c1][c2] = sum / approxErrors[0].length;
            }
        }
        return covar;
    }
    
    /**
     * Trains the combiner on an array of Signal objects for each classifier,
     * whose data matrices should contain a single row of likelihoods of class
     * membership.
     * @param numberOfClassifiers_ The number of classes each component classifier 
     *  is trained on.
     * @param likelihoodArrays A 2d Signal array (indexed [num examples][num classifers])
     *  containing the Signal objects from each classifier with likelihoods of class 
     *  membership of some example data. Note each Signal object is expected to 
     *  contain both the real class label of the example and one column of data, 
     *  composed of the likelihoods of class membership for each class, for that example.
     * @throws noMetadataException if required metadata is not found.
     */
    public abstract void train(int numberOfClassifiers_, Signal[][] likelihoodArrays) throws noMetadataException;
    
    /**
     * Applies the trained combiner to an array of Signal Objects (one for each classifier)
     * and returns a single Signal object with classification metadata added.
     * @param decisionProfile an array of Signal Objects (one for each classifier).
     * @return A single Signal object with classification metadata added.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public abstract Signal classify(Signal[] decisionProfile) throws noMetadataException;
    
    /**
     * Applies the trained combiner to a 2d array of Signal Objects (indexed
     * [num examples][num classifers]) and returns a single Signal object with 
     * classification metadata added.
     * @param decisionProfiles a 2D array of Signal Objects (one for each classifier).
     * @return An array of Signal objects with classification metadata added 
     *  (one for each example.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal[] classify(Signal[][] decisionProfiles) throws noMetadataException
    {
        Signal[] output = new Signal[decisionProfiles.length];
        for (int i=0;i<decisionProfiles.length; i++)
        {
            output[i] = this.classify(decisionProfiles[i]);
        }
        
        return output;
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param decisionProfile The Signals object to calculate the probabilities of 
     * class membership for. These probabilities should be ordered such that the indexes
     * match the class names returned by <code>getClassNames</code>.
     * @return An array of the probabilities of class membership
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public abstract double[] probabilities(Signal[] decisionProfile) throws noMetadataException;
}
