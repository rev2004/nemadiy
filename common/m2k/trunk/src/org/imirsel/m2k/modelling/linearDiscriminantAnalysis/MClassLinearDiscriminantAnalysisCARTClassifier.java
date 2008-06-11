package org.imirsel.m2k.modelling.linearDiscriminantAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.modelling.SimpleMultiVectorSignalClassifier;
import org.imirsel.m2k.modelling.cart.CART;

/**
 * A single Gaussian classifier that is trained on an array of <code>Signal</code> 
 * objects with either diagonal or full covariance.
 * @author  Kris West
 */
public class MClassLinearDiscriminantAnalysisCARTClassifier extends SimpleMultiVectorSignalClassifier{
    
    MClassLinearDiscriminantAnalysis transform;
    
    CART theCART;
    
    /** Determines whether any probabilities output are normalised by the prior probabilites (useful for variable length segments */
    private boolean normaliseProbsByPriors = true;
    
    /** The minimum percentage of the input data that must be present at a node in order to split it **/
    private double thresholdPercent = 0.0001;
    
    /** Splitting criteria used to build tree */
    private int splittingCriteria = CART.ENTROPY;
    
        /** determines whether debug info is output */
    private boolean verbose = false;
    
    /**
     * Creates a new instance of SingleGaussian
     */
    public MClassLinearDiscriminantAnalysisCARTClassifier() {
        super();
        
        ClassifierName = "LDA Gaussian Classifier";
        transform = null;
        theCART = null;
    }
    
    /**
     * Copy constructor for a SingleGaussian
     * @param oldGaussian The <code>SingleGaussian</code> to copied
     */
    public MClassLinearDiscriminantAnalysisCARTClassifier(MClassLinearDiscriminantAnalysisCARTClassifier oldClassifier) {
        super(oldClassifier);
        transform = new MClassLinearDiscriminantAnalysis(oldClassifier.transform);
        theCART = new CART(oldClassifier.theCART);
        ClassifierName = oldClassifier.ClassifierName;
    }
    
    /**
     * Returns a String representing the name of the classifier.
     * @return a String representing the name of the classifier
     */
    public String getClassifierName()
    {
        return ClassifierName;
    }
    
    public boolean isTrained(){
        if (theCART == null)
        {
            return false;
        }
        return theCART.isTrained();
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input)
    {
        if (!this.isTrained())
        {
            throw new RuntimeException("Unable to classify, this classifier (" + this.getClassifierName() + ") is not trained!");
        }else{
            double[] theVector = this.transform.transform(input);
            return theCART.classifyVector(theVector);
        }
    }
    
    /**
     * Get classification probabilities for a single vector.
     * @param input Vector to classify
     * @return double[] with class probabilities.
     */
    public double[] probabilities(double[] input)
    {
        if (!this.isTrained())
        {
            throw new RuntimeException("Unable to classify, this classifier (" + this.getClassifierName() + ") is not trained!");
        }else{
            double[] theVector = this.transform.transform(input);
            return theCART.probabilities(theVector);
        }
    }
    
    /**
     * Trains the Gaussian model on the array of Signal objects.
     * @param inputData The array of Signal Objects to train the Gaussian on.
     * @throws noMetadataException Thrown if there is no class metadata to train the Gaussian model with
     */
    public void train(Signal[] inputData) throws noMetadataException {
        ArrayList tempClassNames = new ArrayList();
        
        for (int i = 0; i < inputData.length; i++) {
            if (tempClassNames.contains(inputData[i].getStringMetadata(Signal.PROP_CLASS)) == false) {
                tempClassNames.add(inputData[i].getStringMetadata(Signal.PROP_CLASS));
            }
        }
        Collections.sort(tempClassNames);
        this.setClassNames(tempClassNames);
        
        transform = new MClassLinearDiscriminantAnalysis();
        transform.train(inputData);
        Signal[] transformedData = transform.transform(inputData,false);
        theCART = new CART(splittingCriteria, thresholdPercent, verbose);
        theCART.setNormaliseProbsByPriors(isNormaliseProbsByPriors());
        theCART.train(transformedData);
    }

    public boolean isNormaliseProbsByPriors() {
        return normaliseProbsByPriors;
    }

    public void setNormaliseProbsByPriors(boolean normaliseProbsByPriors) {
        this.normaliseProbsByPriors = normaliseProbsByPriors;
    }

    public double getThresholdPercent() {
        return thresholdPercent;
    }

    public void setThresholdPercent(double thresholdPercent) {
        this.thresholdPercent = thresholdPercent;
    }

    public int getSplittingCriteria() {
        return splittingCriteria;
    }

    public void setSplittingCriteria(int splittingCriteria) {
        this.splittingCriteria = splittingCriteria;
    }

    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    
}

