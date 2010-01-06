/*
 * TrainedModelCombiner.java
 *
 * Created on July 26, 2005, 3:27 PM
 *
 * An interface defining the methods of trained systems intended to combine 
 * likelihood outputs of Classifiers that operate on <code>Signal[]</code>s.
 * These classifiers should operate on the full set of classes in the data.
 *
 *@author Kris West
 */

package org.imirsel.m2k.modelling.modelCombiners;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.io.Serializable;
import java.util.List;

/**
 * An interface defining the methods of trained systems intended to combine 
 * likelihood outputs of Classifiers that operate on <code>Signal[]</code>s.
 * These classifiers should operate on the full set of classes in the data.
 * @author Kris West
 */
public interface TrainedModelCombiner extends Serializable {
    
    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public String getCombinerName();
    
    /**
     * Returns an integer indicating the number of classifiers the combiner was 
     * trained to combine the likelihoods of.
     * @return an integer indicating the number of classifiers the combiner was 
     * trained to combine the likelihoods of.
     */
    public int getNumClassifiers();
    
    /**
     * Returns an integer indicating the number of classes the component
     * classifiers were trained to classify. This will be the same dimension
     * as the number of likelihoods output by each classifier.
     * @return an integer indicating the number of classes the component
     * classifiers were trained to classify. This will be the same dimension
     * as the number of likelihoods output by each classifier.
     */
    public int getNumClasses();
    
    /**
     * Returns a List of the class names found in the data that the classifier
     * was trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. Should return 
     * null if untrained.
     * @return a List containing the class names.
     */
    public List getClassNames();
    
    /**
     * Sets the List of the class names in the data that the classifier
     * will be trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. `
     * @param classNames a List containing the class names.
     */
    public void setClassNames(List classNames);
    
    /**
     * Returns a flag indicating whether the model combiner has been trained or not.
     * @return a flag indicating whether the model combiner been trained or not.
     */
    public boolean isTrained();
    
    /**
     * Trains the combiner on an array of Signal objects for each classifier,
     * whose data matrices should contain a single row of likelihoods of class
     * membership.
     * @param numberOfClassifiers_ The number of classes each component classifier 
     *  is trained on.
     * @param likelihoodArrays A 2d Signal array (indexed [classifier num][number of examples])
     *  containing the Signal objects from each classifier with likelihoods of class 
     *  membership of some example data. Note each Signal object is expected to 
     *  contain both the real class label of the example and one column of data, 
     *  composed of the likelihoods of class membership for each class, for that example.
     * @throws noMetadataException if required metadata is not found.
     */
    public void train(int numberOfClassifiers_, Signal[][] likelihoodArrays) throws noMetadataException;
    
    /**
     * Applies the trained combiner to an array of Signal Objects (one for each classifier)
     * and returns a single Signal object with classification metadata added.
     * @param decisionProfile an array of Signal Objects (one for each classifier).
     * @return A single Signal object with classification metadata added.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal classify(Signal[] decisionProfile) throws noMetadataException;
    
    /**
     * Applies the trained combiner to a 2d array of Signal Objects (indexed
     * [num examples][num classifers]) and returns a single Signal object with 
     * classification metadata added.
     * @param decisionProfiles a 2D array of Signal Objects (one for each classifier).
     * @return An array of Signal objects with classification metadata added 
     *  (one for each example.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal[] classify(Signal[][] decisionProfiles) throws noMetadataException;
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param decisionProfile The Signals object to calculate the probabilities of 
     * class membership for. These probabilities should be ordered such that the indexes
     * match the class names returned by <code>getClassNames</code>.
     * @return An array of the probabilities of class membership
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public double[] probabilities(Signal[] decisionProfile) throws noMetadataException;
}
