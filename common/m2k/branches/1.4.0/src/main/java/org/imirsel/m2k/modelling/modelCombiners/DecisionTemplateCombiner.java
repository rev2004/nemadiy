/*
 * DecisionTemplateCombiner.java
 *
 * Created on 07 July 2005, 20:27
 *
 * @author Kris West
 */

package org.imirsel.m2k.modelling.modelCombiners;

/**
 * Classifer likelihood combiner based on DecisionTemplates 
 * (Combining Classifiers, Ludmilla Kuncheva page 170)
 * Currently only supports classifiers that work on all the classes, not 
 * subsets, modification would be trivial
 * 
 * @author Kris West
 */
import org.imirsel.m2k.util.Signal;
import java.util.ArrayList;
import java.util.List;
import org.imirsel.m2k.util.noMetadataException;
import java.io.Serializable;

/**
 * Classifer likelihood combiner based on a Decision Template 
 * (Combining Pattern Classifiers - Kuncheva)
 * Currently only supports classifiers that work on all the classes, not 
 * subsets, although modification is possible
 */
public class DecisionTemplateCombiner extends TrainedModelCombinerImpl
{
    double[][] decisionTemplates = null;
    private boolean useCosine = false;
    
    /**
     * Creates a new instance of DecisionTemplateCombiner
     */
    public DecisionTemplateCombiner()
    {
    }
    
    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public String getCombinerName()
    {
        return "DecisionTemplateCombiner";
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
    public void train(int numberOfClassifiers_, Signal[][] likelihoodArrays) throws noMetadataException
    {
        this.numClassifiers = numberOfClassifiers_;
        this.classNames = new ArrayList();
        
        //collect classNames
        for (int i=0;i<likelihoodArrays.length;i++)
        {
            for (int j=0;j<likelihoodArrays[i].length;j++)
            {
                if (likelihoodArrays[i][j] != null)
                {
                    if (!this.classNames.contains(likelihoodArrays[i][j].getStringMetadata(Signal.PROP_CLASS)))
                    {
                        this.classNames.add(likelihoodArrays[i][j].getStringMetadata(Signal.PROP_CLASS));
                    }
                }
            }
        }
        this.setClassNames(classNames);
        
        //init decision templates
        decisionTemplates = new double[this.getNumClasses()][this.getNumClasses() * this.getNumClassifiers()];
        int[][] numExamplesPerClass = new int[this.getNumClassifiers()][this.getNumClasses()];
        for(int i=0;i<likelihoodArrays.length;i++)
        {
            for (int x=0;x<this.getNumClassifiers();x++)
            {
                if (likelihoodArrays[i][x] != null)
                {
                    int exampleClass = this.getClassNames().indexOf(likelihoodArrays[i][x].getStringMetadata(Signal.PROP_CLASS));
                    numExamplesPerClass[x][exampleClass]++;               
                    int likelyIdx = likelihoodArrays[i][x].getColumnIndex(Signal.PROP_LIKELIHOODS);
                    for (int y=0;y<this.getNumClasses();y++)
                    {
                        decisionTemplates[exampleClass][(x * this.getNumClasses()) + y] += likelihoodArrays[i][x].getData()[likelyIdx][y];
                        if (this.verbose)
                        {
                            if (Double.isNaN(likelihoodArrays[i][x].getData()[likelyIdx][y]))
                            {
                                throw new RuntimeException("NaN likelihood found for: " + likelihoodArrays[i][x].getStringMetadata(Signal.PROP_FILE_LOCATION) + "  NaN for class: " + y);
                            }
                        }
                    }
                }
            }
            
        }
        
        if (this.verbose){
            System.out.println("DecisionTemplate:");
        }
        for (int k=0;k<this.getNumClasses();k++)
        {
            if (this.verbose){
                System.out.print("Class " + k + ": ");
            }
            for (int x=0;x<this.getNumClassifiers();x++)
            {   
                for (int y=0;y<this.getNumClasses();y++)
                {
                    decisionTemplates[k][(x * this.getNumClasses()) + y] /= numExamplesPerClass[x][k];
                }
                if (this.verbose){
                    System.out.print(decisionTemplates[k][x] + " ");
                }
            }
            if (this.verbose){
                System.out.println("");
            }
        }
        if (this.verbose){
            System.out.println("");
        }
        this.setIsTrained(true);
        
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param decisionProfile The set of likelihood profiles that will be combined to form the output
     * likelihood profile.
     * @return An array of the probabilities of class membership
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public double[] probabilities(Signal[] decisionProfile) throws noMetadataException
    {
        double[] decisionProfileData = new double[decisionTemplates[0].length];
        if (this.verbose){
            System.out.println("DecisionProfile:");
        }

        for (int j=0;j<this.getNumClassifiers();j++)
        {
            int likelyIdx = decisionProfile[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for (int k=0;k<this.getNumClasses();k++)
            {
                decisionProfileData[j*this.getNumClasses() + k] = decisionProfile[j].getData()[likelyIdx][k];
                if (this.verbose){
                    System.out.print(decisionProfileData[j*this.getNumClasses() + k] + " ");
                }
            }
        }
        if (this.verbose){
            System.out.println("");
        }

        //test similarity
        double[] similarities = new double[this.getNumClasses()];
        if (this.verbose){
            System.out.print("Squared Euclidean Distances: ");
        }
        double total = 0.0;
        for (int x=0;x<this.getNumClasses();x++)
        {
            if (this.useCosine)
            {
                similarities[x] = org.imirsel.m2k.math.Mathematics.cosineDist(decisionTemplates[x], decisionProfileData);
            }else{
                similarities[x] = SquaredEuclideanDistance(decisionTemplates[x], decisionProfileData,  this.getNumClassifiers(), this.getNumClasses());
            }
            if(similarities[x] < 0)
            {
                throw new RuntimeException("Negative simililarity returned!");
            }
            if (this.verbose){
                System.out.print(similarities[x] + " ");
            }
            total += similarities[x];
        }
        for (int x=0;x<this.getNumClasses();x++)
        {
            similarities[x] /= total;
        }
        return similarities;
    }
    
    /**
     * Applies the trained combiner to a 2d array of Signal Objects (indexed
     * [num examples][num classifers]) and returns a single Signal object with 
     * classification metadata added.
     * @param decisionProfile an array of Signal Objects (indexed [num examples][num classifers], one for 
     * each classifer, for each example).
     * @return An array of Signal objects with classification metadata added 
     *  (one for each example.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal classify(Signal[] decisionProfile) throws noMetadataException
    {
        Signal output = decisionProfile[0].cloneNoData();
        double[] decisionProfileData = new double[decisionTemplates[0].length];
        if (this.verbose){
            System.out.println("DecisionProfile:");
        }

        for (int j=0;j<this.getNumClassifiers();j++)
        {
            int likelyIdx = decisionProfile[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for (int k=0;k<this.getNumClasses();k++)
            {
                decisionProfileData[j*this.getNumClasses() + k] = decisionProfile[j].getData()[likelyIdx][k];
                if (this.verbose){
                    System.out.print(decisionProfileData[j*this.getNumClasses() + k] + " ");
                }
            }
        }
        if (this.verbose){
            System.out.println("");
        }

        //test similarity
        double[] similarities = new double[this.getNumClasses()];
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        if (this.verbose){
            if (this.useCosine)
            {
                System.out.print("Cosine Distances: ");
            }else{
                System.out.print("Squared Euclidean Distances: ");
            }
        }
        for (int x=0;x<this.getNumClasses();x++)
        {
            if (this.useCosine)
            {
                similarities[x] = org.imirsel.m2k.math.Mathematics.cosineDist(decisionTemplates[x], decisionProfileData);
            }else{
                similarities[x] = SquaredEuclideanDistance(decisionTemplates[x], decisionProfileData,  this.getNumClassifiers(), this.getNumClasses());
            }
            if (this.verbose){
                System.out.print(similarities[x] + " ");
            }
            if (similarities[x] > max)
            {
                max = similarities[x];
                maxIdx = x;
            }
        }
        if (this.verbose){
            System.out.println("");
        }
        if (maxIdx == -1) {
            maxIdx = (int)Math.rint(Math.random() * (((double)this.getNumClasses()) - 1.0));
            System.out.println("DecisionTemplateCombiner:patched class");
        }
        //apply classification
        output.setMetadata(Signal.PROP_CLASSIFICATION, (String)this.getClassNames().get(maxIdx));
        
        return output;
    }
    
    /**
     * Utility method for calculating the SquaredEuclideanDistance distance matrix,
     * better performance might be gained by using a stronger distance calculation,
     * such as Mahalonobis, Earth Mover's or Cosine distances.
     * @param arr1 The first array, either the decision profile or the decision template for 
     * a particular class
     * @param arr2 The second array, either the decision profile or the decision template for 
     * a particular class
     * @param L The number of classifiers
     * @param c the number of classes
     * @return The distance score.
     */
    public double SquaredEuclideanDistance(double[] arr1, double[] arr2, int L, int c)
    {
        double dist = 0;
        if (arr1.length != arr2.length)
        {
            throw new RuntimeException("DecisionTemplateCombiner: SquaredEuclideanDistance: Array lengths do not match!");
        }
        for (int i=0;i<arr1.length;i++)
        {
            dist += Math.pow(((arr1[i] - arr2[i])),2);//Math.pow(((arr1[i] - arr2[i]) / arr1[i]),2);
        }
        if (dist == 0.0) {
            dist = 1.0;
        } else {
            dist = 1.0 - ((1.0/((double)L * (double)c)) * dist);
        }
        return dist;
    }

    /**
     * Returns a flag that determines whether the squared Euclidean or Cosine distance is 
     * used to assign the final classification
     * @return a flag that determines whether the squared Euclidean or Cosine distance is 
     * used to assign the final classification
     */
    public boolean getUseCosine() {
        return useCosine;
    }

    /**
     * Sets a flag that determines whether the squared Euclidean or Cosine distance is 
     * used to assign the final classification.
     * @param useCosine a flag that determines whether the squared Euclidean or Cosine distance is 
     * used to assign the final classification.
     */
    public void setUseCosine(boolean useCosine) {
        this.useCosine = useCosine;
    }
}
