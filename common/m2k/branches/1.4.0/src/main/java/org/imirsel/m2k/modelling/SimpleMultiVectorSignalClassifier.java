package org.imirsel.m2k.modelling;

import java.util.List;
import java.util.Collections;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import Jama.Matrix;
import org.imirsel.m2k.modelling.SignalClassifier;

/**
 * An abstract class for a SignalClassifier implementation that can be extended with
 * code for any simple trainable classifier. Offers implemntations of probability 
 * methods based on counts of classified frames and other simple utility methods.
 * @author  Kris West
 */
public abstract class SimpleMultiVectorSignalClassifier implements SignalClassifier {
    
    /** The classifier name. **/
    public String ClassifierName = "simpleMultiVectorSignalClassifier - !!Abstract!!";
    /** A flag indicating whether a classifier has been trained or not. **/
    boolean isTrained = false;
    /** ArrayList of class names. **/
    List classNames;
    
    /**
     * Creates a new instance of SingleGaussian
     */
    public SimpleMultiVectorSignalClassifier() {
        isTrained = false;
        classNames = null;
    }
    
    /**
     * Copy constructor for a SingleGaussian.
     * @param oldClassifier the SingleGaussian to copy.
     */
    public SimpleMultiVectorSignalClassifier(SimpleMultiVectorSignalClassifier oldClassifier) {
        isTrained = oldClassifier.isTrained;
        classNames = oldClassifier.classNames;
    }
    
    /**
     * Returns a String representing the name of the classifier.
     * @return a String representing the name of the classifier
     */
    public abstract String getClassifierName();
    
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
     * Returns the number of classes that the classifier is trained on. Should
     * return -1 if untrained.
     * @return the number of classes that the classifier is trained on.
     */
    public int getNumClasses()
    {
        return this.classNames.size();
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
     * Sets the List of the class namesin the data that the classifier
     * will be trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. `
     * @param classNames_ a List containing the class names.
     */
    public void setClassNames(List classNames_)
    {
        Collections.sort(classNames_);
        this.classNames = classNames_;
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public abstract int classifyVector(double[] input);
    
    /**
     * Classify a single vector and return a class name.
     * @param input Vector to classify
     * @return A String indicating class.
     */
    public String classify(double[] input)
    {
        int classNum = this.classifyVector(input);
        return (String)this.getClassNames().get(classNum);
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param inputSignal The Signal object to calculate the probabilities of 
     * class membership for.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(Signal inputSignal)
    {
        
        int[] votes = new int[this.getNumClasses()];
        double[] probs = new double[this.getNumClasses()];
        //iterate through all rows
        for (int r = 0; r<inputSignal.getNumRows(); r++) {
            double[] vec = new double[inputSignal.getNumCols()];
            for(int c = 0; c < inputSignal.getNumCols(); c++) {
                vec[c] = inputSignal.getData()[c][r];
            }
            //perhaps insert handling for NaN/infinity vectors
            int classNum = this.classifyVector(vec);
            votes[classNum]++;
        }
        
        for (int i=0;i<this.getNumClasses(); i++)
        {
            probs[i] = ((double)votes[i])/((double)inputSignal.getNumRows());
        }
        return probs;
    }
    
    /**
     * Classifies the input Signal object and outputs the label of the output class
     * @param inputSignal The Signal object to classify
     * @return The String label of the output class
     */
    public String classify(Signal inputSignal) {
        if (this.getNumClasses() < 2)
        {
            throw new RuntimeException("Can't perform classification, number of classes is less then 2! (" + this.getNumClasses() + ")");
        }
        //Pick most likely class
        double max = Double.NEGATIVE_INFINITY;
        int outputClass = -1;
        double[] probs = this.probabilities(inputSignal);
        if ((probs.length==0)||(probs.length!=this.getNumClasses()))
        {
            throw new RuntimeException("An inappropiate number of probabilities was returned!\nprobs.length: " + probs.length + ", numClasses: " + this.getNumClasses());
        }
        for(int i = 0; i<probs.length; i++) {
            if (probs[i] >= max) {
                max = probs[i];
                outputClass = i;
            }
        }
        return (String)this.getClassNames().get(outputClass);
    }
    
    /**
     * Trains the classifier model on the array of Signal objects.
     * @param inputData The array of Signal Objects to train the classifier on.
     * @throws noMetadataException Thrown if there is no class metadata to train the classifier with.
     */
    public abstract void train(Signal[] inputData) throws noMetadataException;
    
}

