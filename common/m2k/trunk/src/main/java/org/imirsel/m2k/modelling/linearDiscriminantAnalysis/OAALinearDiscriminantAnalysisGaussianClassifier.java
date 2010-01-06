package org.imirsel.m2k.modelling.linearDiscriminantAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.modelling.SimpleMultiVectorSignalClassifier;
import org.imirsel.m2k.modelling.singleGaussian.SingleGaussian;

/**
 * A classifier based on the training of a single Gaussian classifier in a 
 * one-against-all LDA feature space, both of which are trained on an array of 
 * <code>Signal</code> objects with either diagonal or full covariance.
 * @author  Kris West
 */
public class OAALinearDiscriminantAnalysisGaussianClassifier extends SimpleMultiVectorSignalClassifier{
    
    OneAgainstAllLinearDiscriminantAnalysis transform;
    
    SingleGaussian theGaussian;
    
    /** A flag controlling whether full or diagonal covariance is used. **/
    private boolean useFullCovariance = true;
    
    /**
     * Creates a new instance of OAALinearDiscriminantAnalysisGaussianClassifier
     */
    public OAALinearDiscriminantAnalysisGaussianClassifier() {
        super();
        
        ClassifierName = "One-Against-All LDA Gaussian Classifier";
        transform = null;
        theGaussian = null;
    }
    
    /**
     * Copy constructor for a OAALinearDiscriminantAnalysisGaussianClassifier
     * @param oldClassifier The <code>OAALinearDiscriminantAnalysisGaussianClassifier</code> to copied
     */
    public OAALinearDiscriminantAnalysisGaussianClassifier(OAALinearDiscriminantAnalysisGaussianClassifier oldClassifier) {
        super(oldClassifier);
        transform = new OneAgainstAllLinearDiscriminantAnalysis(oldClassifier.transform);
        theGaussian = new SingleGaussian(oldClassifier.theGaussian);
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
    
    /** Sets a flag controlling whether full or diagonal covariance is used. Note
     *  this flag has no effect if set after training. If inversion of a full
     *  covariance matrix fails during training this flag will be set to false;
     *  @param val a flag controlling whether full or diagonal covariance is used.
     */
    public void setUseFullCovar(boolean val)
    {
        setUseFullCovariance(val);
    }
    
    public boolean isTrained(){
        if (theGaussian == null)
        {
            return false;
        }
        return theGaussian.isTrained();
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
            return theGaussian.classifyVector(theVector);
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
            return theGaussian.probabilities(theVector);
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
        
        transform = new OneAgainstAllLinearDiscriminantAnalysis();
        transform.train(inputData);
        Signal[] transformedData = transform.transform(inputData,false);
        theGaussian = new SingleGaussian();
        theGaussian.setUseFullCovar(this.isUseFullCovariance());
        theGaussian.train(transformedData);
    }

    public boolean isUseFullCovariance() {
        return useFullCovariance;
    }

    public void setUseFullCovariance(boolean useFullCovariance) {
        this.useFullCovariance = useFullCovariance;
    }
}

