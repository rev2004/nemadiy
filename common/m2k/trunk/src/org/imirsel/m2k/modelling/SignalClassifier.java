/*
 * SignalClassifier.java
 *
 * Created on July 19, 2005, 3:35 PM
 *
 * @author Kris West
 */

package org.imirsel.m2k.modelling;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.List;
import java.io.Serializable;

/**
 * This interface defines the methods of an M2K classifier that operates directly 
 * on a <code>org.imirsel.m2k.util.Signal</code> Object.
 *
 * @author Kris West
 */
public interface SignalClassifier extends Serializable{
  
    /**
     * Returns a String representing the name of the classifier.
     * @return a String representing the name of the classifier
     */
    public String getClassifierName();
    
    
    /**
     * Returns a flag indicating whether the classifier has been trained or not.
     * @return a flag indicating whether the classifier has been trained or not.
     */
    public boolean isTrained();
    
    /**
     * Returns the number of classes that the classifier is trained on. Should
     * return -1 if untrained.
     * @return the number of classes that the classifier is trained on.
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
     * Trains the classifier on the array of Signal objects. Implementations of 
     * this method should also produce an ordered list of the class names which 
     * can be returned with the <code>getClassNames</code> method.
     * @see getClassNames
     * @param inputData the Signal array that the model should be trained on.
     * @throws noMetadataException Thrown if there is no class metadata to train the Gaussian model with
     */
    public void train(Signal[] inputData) throws noMetadataException;
    
    /**
     * Classify a single vector. A RuntimeException should be thrown if the classifier is untrained.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input);
    
    /**
     * Classify a single vector and return a class name.
     * @param input Vector to classify
     * @return A String indicating class.
     */
    public String classify(double[] input);
    
    /**
     * Calculates the probability of class membership of a Signal Object.  
     * @param inputSignal The Signal object to calculate the probabilities of 
     * class membership for. This probabilities should be ordered such that the indexes
     * match the class names returned by <code>getClassNames</code>.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(Signal inputSignal);
    
    /**
     * Calculates the probability of class membership of a single data vector.  
     * @param input The data vector to calculate the probabilities of 
     * class membership for. This probabilities should be ordered such that the indexes
     * match the class names returned by <code>getClassNames</code> and should sum to 1.0.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(double[] input);
    
    /**
     * Classifies the input Signal object and outputs the label of the output class
     * @param inputSignal The Signal object to classify
     * @return The String label of the output class
     */
    public String classify(Signal inputSignal);
    
}
