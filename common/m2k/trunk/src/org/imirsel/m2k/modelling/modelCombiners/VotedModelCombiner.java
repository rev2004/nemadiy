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
public class VotedModelCombiner {

    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public static String getCombinerName(){
        return "Voted Model Combiner";
    }
    
    /**
     * Creates a new instance of TrainedModelCombinerImpl
     */
    public VotedModelCombiner() {
    }
    
    /**
     * Applies the trained combiner to an array of Signal Objects (one for each classifier)
     * and returns a single Signal object with classification metadata added.
     * @param decisionProfile an array of Signal Objects (one for each classifier).
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
        if (maxIdx == -1){
            maxIdx = 0;
        }
        //apply classification
        Signal output = testData.cloneNoData();
        output.setMetadata(Signal.PROP_CLASSIFICATION, classifiers[0].getClassNames().get(maxIdx));
        return output;
    }
    
    /**
     * Applies the trained combiner to a 2d array of Signal Objects (indexed
     * [num examples][num classifers]) and returns a single Signal object with 
     * classification metadata added.
     * @param decisionProfiles a 2D array of Signal Objects (one for each classifier).
     * @return An array of Signal objects with classification metadata added 
     *  (one for each example.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public static Signal[] classify(Signal[] testData, SignalClassifier[] classifiers) throws noMetadataException
    {
        Signal[] output = new Signal[testData.length];
        for (int i=0;i<testData.length; i++)
        {
            output[i] = VotedModelCombiner.classify(testData[i],classifiers);
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
    public static double[] probabilities(Signal testData, SignalClassifier[] classifiers) throws noMetadataException{
       
        int[] votes = null;
        int numVotes = classifiers.length;
        for(int i=0;i<classifiers.length;i++)
        {
            double[] probs = classifiers[i].probabilities(testData); 
            if (votes == null){
                votes = new int[probs.length];
            }
            double max = Double.NEGATIVE_INFINITY;
            int maxIdx = -1;
            for (int j = 0; j < probs.length; j++) {
                if (probs[j] >= max){
                    max = probs[j];
                    maxIdx = j;
                }
            }
            votes[maxIdx]++;
        }
        
        double[] outputProfile = new double[votes.length];
        
        for (int j=0;j<votes.length;j++)
        {
            outputProfile[j] = (double)votes[j] / (double)numVotes;
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
            output[i] = VotedModelCombiner.classifyWithDiffData(testData[i],classifiers);
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

        
        if (testData.length != classifiers.length){
            System.out.println("WARNING: MeanModelCombiner: Umber of Input Signal objects and number of classifiers are not equal");
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
        
        int[] votes = null;
        int numVotes = classifiers.length;
        for (int i=0;i<likelihoods.length;i++)
        {
            
            likelyIdx = likelihoods[i].getColumnIndex(Signal.PROP_LIKELIHOODS);
            double[] datacol = likelihoods[i].getData()[likelyIdx];
            
            if (votes == null){
                votes = new int[datacol.length];
            }
            
            double max = Double.NEGATIVE_INFINITY;
            int maxIdx = -1;
            for (int j = 0; j < datacol.length; j++) {
                if (datacol[j] >= max){
                    max = datacol[j];
                    maxIdx = j;
                }
            }
            votes[maxIdx]++;
            
        }
        double[] outputProfile = new double[votes.length];
        for (int j=0;j<votes.length;j++)
        {
            outputProfile[j] = (double)votes[j] / (double)numVotes;
        }
        return outputProfile;
    }
}
