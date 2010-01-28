/*
 * ConstrainedRegressionCombiner.java
 *
 * Created on 07 July 2005, 20:27
 *
 * @author Kris West
 */

package org.imirsel.m2k.modelling.modelCombiners;

import org.imirsel.m2k.util.Signal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.imirsel.m2k.util.noMetadataException;
import java.io.Serializable;

/**
 * Classifer likelihood combiner based on ConstrainedRegression. (Combining Pattern
 * Classifiers - Kuncheva). Uses a constrained regression to find the optimal 
 * set of weights to combine input likelihood profiles to produce a final 
 * classification.
 * 
 * Currently only supports classifiers that work on all the classes, not 
 * subsets, although modification is possible
 * @author Kris West
 */
public class ConstrainedRegressionCombiner extends TrainedModelCombinerImpl
{
    double[][] classifierWeights = null;
    
    /**
     * Creates a new instance of ConstrainedRegressionCombiner.
     */
    public ConstrainedRegressionCombiner()
    {
        
    }
    
    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public String getCombinerName()
    {
        return "ConstrainedRegressionCombiner";
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
            if (!this.classNames.contains(likelihoodArrays[i][0].getStringMetadata(Signal.PROP_CLASS)))
            {
                this.classNames.add(likelihoodArrays[i][0].getStringMetadata(Signal.PROP_CLASS));
            }
        }
        this.setClassNames(classNames);
        
        //init approximation errors
        double[][][] approximationErrors = new double[this.getNumClasses()][this.getNumClassifiers()][likelihoodArrays.length];
        if (this.verbose){
            java.text.DecimalFormat dec = new java.text.DecimalFormat();
            dec.setMaximumFractionDigits(2);
            System.out.println("Input training Likelihoods: ");
        }
        
        //Not appropriate for bagging
        //need to handle special case of less examples for last iteration
        //Probably needs to be handled in covariance matrix calculation
        for(int i=0;i<likelihoodArrays.length;i++)
        {
            if (this.verbose){
                System.out.print(i + ": ");
            }
            int exampleClass = this.getClassNames().indexOf(likelihoodArrays[i][0].getStringMetadata(Signal.PROP_CLASS));
            for (int j=0;j<numClassifiers;j++)
            {
                if (likelihoodArrays[i][j] != null)
                {
                    //find likelihood col
                    int likelyCol = likelihoodArrays[i][j].getColumnIndex(Signal.PROP_LIKELIHOODS);
                    for (int k=0;k<this.getNumClasses();k++)
                    {
                        if (this.verbose){
                            System.out.print(likelihoodArrays[i][j].getData()[likelyCol][k] + " ");
                        }
                        if(k == exampleClass)
                        {
                            approximationErrors[k][j][i] = 1.0 - likelihoodArrays[i][j].getData()[likelyCol][k];
                        }
                        else
                        {
                            approximationErrors[k][j][i] = 0.0 - likelihoodArrays[i][j].getData()[likelyCol][k];
                        }
                    }
                    if (this.verbose){
                        System.out.print(" | ");
                    } 
                }
            }
            if (this.verbose){
                System.out.println("");
            }
        }
        
        //calculate covariance matrices of approximation errors
        double[][][] errorCovarianceMatrices = new double[this.getNumClasses()][][];
        
        for (int i=0;i<this.getNumClasses();i++)
        {
            errorCovarianceMatrices[i] = calcCovarianceMatrix(approximationErrors[i]);
        }
        
        //calculate classifier weights 
        //Check the matrix indexing...
        classifierWeights = new double[this.getNumClasses()][this.getNumClassifiers()];
        double[][] oneColVec = new double[numClassifiers][1];
        for (int i=0;i<numClassifiers;i++)
        {
            oneColVec[i][0] = 1.0;
        }
        Jama.Matrix oneColVecMat = new Jama.Matrix(oneColVec);
        Jama.Matrix oneRowVecMat = oneColVecMat.transpose();
        for (int i=0;i<this.getNumClasses();i++)
        {
            if(this.verbose)
            {
                System.out.print("Class " + i + " weights: ");
            }
            Jama.Matrix covarMat = new Jama.Matrix(errorCovarianceMatrices[i]);
            Jama.Matrix invCovarMat = covarMat.inverse();
            Jama.Matrix right = (oneRowVecMat.times(invCovarMat)).times(oneColVecMat);
            Jama.Matrix invRight = right.inverse();
            Jama.Matrix left = invCovarMat.times(oneColVecMat);
            Jama.Matrix weights = left.times(invRight);
            for (int j=0;j<numClassifiers;j++)
            {
                classifierWeights[i][j] = weights.getArray()[j][0];
                System.out.print(" " + classifierWeights[i][j] + " ");
            }
            System.out.println("");
        }
        this.setIsTrained(true);
        
    }
    
    /**
     * Applies the trained combiner to an array of Signal Objects (one for each classifier)
     * and returns a single Signal object with classification metadata added.
     * @return A single Signal object with classification metadata added.
     * @param decisionProfile an array of Signal Objects (one for each classifier).
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal classify(Signal[] decisionProfile) throws noMetadataException
    {
        if (this.isTrained() == false)
        {
            throw new RuntimeException("The model trainer has not been trained!");
        }
        Signal output = decisionProfile[0].cloneNoData();
        double[] decisionProfileData = new double[this.getNumClassifiers() * this.getNumClasses()];
        /*if (this.verbose){
            System.out.println("DecisionProfile:");
        }*/

        for (int j=0;j<this.getNumClassifiers();j++)
        {
            int likelyCol = decisionProfile[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for (int k=0;k<this.getNumClasses();k++)
            {
                decisionProfileData[(j * this.getNumClasses()) + k] = decisionProfile[j].getData()[likelyCol][k];
                /*if (this.verbose){
                    System.out.print(decisionProfile[j * this.getNumClasses() + k] + " ");
                }*/
            }
        }
        if (this.verbose){
            System.out.println("");
        }

        //combine likelihoods
        double[] combinedLikelihoods = new double[this.getNumClasses()];
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int y=0;y<this.numClassifiers; y++)
        {
            int likelyCol = decisionProfile[y].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for (int x=0;x<this.getNumClasses();x++)
            {
                combinedLikelihoods[x] += classifierWeights[x][y] * decisionProfile[y].getData()[likelyCol][x];
            }
        }
        if (this.verbose){
            System.out.print("Combined Likelihoods: ");
        }
        for (int x=0;x<this.getNumClasses();x++)
        {
            if (combinedLikelihoods[x] > max)
            {
                max = combinedLikelihoods[x];
                maxIdx = x;
            }
            if (this.verbose){
                System.out.print(combinedLikelihoods[x] + " ");
            }
        }

        if (this.verbose){
            System.out.println("");
        }
        //apply classification
        output.setMetadata(Signal.PROP_CLASSIFICATION, (String)this.getClassNames().get(maxIdx));
        return output;
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @return An array of the probabilities of class membership
     * @param decisionProfile The set of likelihood profiles that will be combined to form the output
     * likelihood profile.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public double[] probabilities(Signal[] decisionProfile) throws noMetadataException
    {
        if (this.isTrained() == false)
        {
            throw new RuntimeException("The model trainer has not been trained!");
        }
        Signal output = decisionProfile[0].cloneNoData();
        double[] decisionProfileData = new double[this.getNumClassifiers() * this.getNumClasses()];

        for (int j=0;j<this.getNumClassifiers();j++)
        {
            int likelyCol = decisionProfile[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for (int k=0;k<this.getNumClasses();k++)
            {
                decisionProfileData[(j * this.getNumClasses()) + k] = decisionProfile[j].getData()[likelyCol][k];
            }
        }

        //combine likelihoods
        double[] combinedLikelihoods = new double[this.getNumClasses()];
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        for (int y=0;y<this.numClassifiers; y++)
        {
            int likelyCol = decisionProfile[y].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for (int x=0;x<this.getNumClasses();x++)
            {
                combinedLikelihoods[x] += classifierWeights[x][y] * decisionProfile[y].getData()[likelyCol][x];
            }
        }
        if (this.verbose){
            System.out.print("Combined Likelihoods: ");
            for (int x=0;x<this.getNumClasses();x++)
            {
                System.out.print(combinedLikelihoods[x] + " ");
            }
            System.out.println("");
        }

        return combinedLikelihoods;
    }
}
