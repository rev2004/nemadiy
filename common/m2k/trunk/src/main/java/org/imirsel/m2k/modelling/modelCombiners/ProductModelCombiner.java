/*
 * TrainedModelCombinerImpl.java
 *
 * Created on July 29, 2005, 1:44 PM
 *
 */

package org.imirsel.m2k.modelling.modelCombiners;

import org.imirsel.m2k.modelling.SignalClassifier;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 * An abstract base class to extend model combiners from.
 * @author Kris West
 */
public class ProductModelCombiner {

    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public static String getCombinerName(){
        return "Product Model Combiner";
    }
    
    /**
     * Creates a new instance of ProductModelCombiner
     */
    public ProductModelCombiner() {
    }
    
    /**
     * Applies the combiner to a Signal Object (one for all classifiesr)
     * and returns a single Signal object with classification metadata added.
     * @param testData The Signal object to classify, will be passed to all classifiers
     * @param classifiers The array of classifiers to be applied to the Signal
     * @return A single Signal object with classification metadata added.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static Signal classify(Signal testData, SignalClassifier[] classifiers) throws noMetadataException{
        double[] probs = probabilities(testData,classifiers);
        int maxIdx = -1;
        double maxProb = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb)
            {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        //apply classification
        Signal output = testData.cloneNoData();
        output.setMetadata(Signal.PROP_CLASSIFICATION, classifiers[0].getClassNames().get(maxIdx));
        return output;
    }
    
    /**
     * Applies the combiner to an array of Signal Objects (one for each example for all classifiesr)
     * and returns a Signal object array with classification metadata added to each signal.
     * @param testData The array of Signal objects to classify, will be passed to all classifiers
     * @param classifiers The array of classifiers to be applied to the Signal
     * @return A Signal object array of same length as input with classification metadata added.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static Signal[] classify(Signal[] testData, SignalClassifier[] classifiers) throws noMetadataException
    {
        Signal[] output = new Signal[testData.length];
        for (int i=0;i<testData.length; i++)
        {
            output[i] = MeanModelCombiner.classify(testData[i],classifiers);
        }
        
        return output;
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param testData  The Signal object to classify, will be passed to all classifiers
     * @param classifiers  The array of classifiers to be applied to the Signal
     * @return An array of the probabilities of class membership
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static double[] probabilities(Signal testData, SignalClassifier[] classifiers) throws noMetadataException{
       
        Signal[] likelihoods = new Signal[classifiers.length];
        for(int i=0;i<classifiers.length;i++)
        {
            likelihoods[i] = testData.cloneNoData();
            double[] probs = classifiers[i].probabilities(testData); 
            likelihoods[i].appendColumn(probs, Signal.PROP_LIKELIHOODS);
            likelihoods[i].setMetadata(Signal.PROP_CLASSES, classifiers[i].getClassNames().toArray(new String[classifiers[i].getClassNames().size()]));
        }
        int likelyIdx = likelihoods[0].getColumnIndex(Signal.PROP_LIKELIHOODS);
        double[] outputProfile = new double[likelihoods[0].getData()[likelyIdx].length];
        for (int j=0;j<likelihoods.length;j++)
        {
            likelyIdx = likelihoods[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            double[] datacol = likelihoods[j].getData()[likelyIdx];
            for (int k=0;k<outputProfile.length;k++)
            {
                outputProfile[k] += Math.log(datacol[k] + 0.0001);
            }
        }
        double sum = 0.0;
        for (int i = 0; i < outputProfile.length; i++) {
            //outputProfile[i] /= likelihoods.length;
            outputProfile[i] = Math.exp(outputProfile[i]);
            sum += outputProfile[i];
        }
        for (int i = 0; i < outputProfile.length; i++) {
            outputProfile[i] /= sum;
        }
        return outputProfile;
    }
    
    
    /**
     * Applies the combiner to an array of Signal Objects (one for each classifier)
     * and returns a single Signal object with classification metadata added.
     * @param testData The Signal objects to classify, one per classifier
     * @param classifiers The array of classifiers to be applied to the Signal
     * @return A single Signal object with classification metadata added.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static Signal classifyWithDiffData(Signal[] testData, SignalClassifier[] classifiers) throws noMetadataException{
        double[] probs = probabilitiesWithDiffData(testData,classifiers);
        int maxIdx = -1;
        double maxProb = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb)
            {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        //apply classification
        Signal output = testData[0].cloneNoData();
        output.setMetadata(Signal.PROP_CLASSIFICATION, classifiers[0].getClassNames().get(maxIdx));
        return output;
    }
    
    /**
     * Applies the combiner to a 2d array of Signal Objects (indexed
     * [num examples][num classifers]) and returns a single Signal object with 
     * classification metadata added.
     * @param testData The Signal objects to classify, one per classifier
     * @param classifiers The array of classifiers to be applied to the Signal
     * @return An array of Signal objects with classification metadata added
     *  (one for each example.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static Signal[] classifyWithDiffData(Signal[][] testData, SignalClassifier[] classifiers) throws noMetadataException
    {
        Signal[] output = new Signal[testData.length];
        for (int i=0;i<testData.length; i++)
        {
            output[i] = MeanModelCombiner.classifyWithDiffData(testData[i],classifiers);
        }
        
        return output;
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param testData The Signal objects to classify, one per classifier
     * @param classifiers The array of classifiers to be applied to the Signal
     * @return An array of the probabilities of class membership
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static double[] probabilitiesWithDiffData(Signal[] testData, SignalClassifier[] classifiers) throws noMetadataException{
       
        String fileLoc = testData[0].getStringMetadata(Signal.PROP_FILE_LOCATION);
        for (int i = 1; i < testData.length; i++) {
            if (!fileLoc.equals(testData[i].getStringMetadata(Signal.PROP_FILE_LOCATION))){
                throw new RuntimeException("Trying to combine Signals representing differenet files!");
            }
        }

        
        Signal[] likelihoods = new Signal[classifiers.length];
        for(int i=0;i<classifiers.length;i++)
        {
            likelihoods[i] = testData[i].cloneNoData();
            double[] probs = classifiers[i].probabilities(testData[i]); 
            likelihoods[i].appendColumn(probs, Signal.PROP_LIKELIHOODS);
            likelihoods[i].setMetadata(Signal.PROP_CLASSES, classifiers[i].getClassNames().toArray(new String[classifiers[i].getClassNames().size()]));
        }
        int likelyIdx = likelihoods[0].getColumnIndex(Signal.PROP_LIKELIHOODS);
        double[] outputProfile = new double[likelihoods[0].getData()[likelyIdx].length];
        for (int j=0;j<likelihoods.length;j++)
        {
            likelyIdx = likelihoods[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            double[] datacol = likelihoods[j].getData()[likelyIdx];
            for (int k=0;k<outputProfile.length;k++)
            {
                outputProfile[k] += Math.log(datacol[k] + 0.000001);
            }
        }
        double sum = 0.0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < outputProfile.length; i++) {
            //outputProfile[i] /= likelihoods.length;
            //outputProfile[i] = Math.exp(outputProfile[i]);
            if (outputProfile[i] < min){
                min = outputProfile[i];
            }
        }
        for (int i = 0; i < outputProfile.length; i++) {
            outputProfile[i] -= min;
            sum += outputProfile[i];
        }
        for (int i = 0; i < outputProfile.length; i++) {
            outputProfile[i] /= sum;
        }
        return outputProfile;
    }
}
