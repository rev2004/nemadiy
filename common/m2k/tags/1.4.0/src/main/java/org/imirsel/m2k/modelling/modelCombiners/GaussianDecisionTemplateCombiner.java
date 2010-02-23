/*
 * DecisionTemplateCombiner.java
 *
 * Created on 07 July 2005, 20:27
 *
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
import org.imirsel.m2k.modelling.singleGaussian.SingleGaussian;

/**
 * Classifer likelihood combiner based on a Decision Template and a Single Gaussin
 * Distribution (Combining Pattern Classifiers - Kuncheva)
 * Currently only supports classifiers that work on all the classes, not 
 * subsets, although modification is possible
 */
public class GaussianDecisionTemplateCombiner extends TrainedModelCombinerImpl
{
    SingleGaussian decisionTemplates = null;
    /** A flag controlling whether full or diagonal covariance is used. **/
    boolean useFullCovariance = false;
    
    /** Sets a flag controlling whether full or diagonal covariance is used. Note
     *  this flag has no effect if set after training. If inversion of a full
     *  covariance matrix fails during training this flag will be set to false;
     *  @param val a flag controlling whether full or diagonal covariance is used.
     */
    public void setUseFullCovar(boolean val)
    {
        useFullCovariance = val;
    }
    
    /** Creates a new instance of DecisionTemplateCombiner */
    public GaussianDecisionTemplateCombiner()
    {
    }
    
    /**
     * Returns a String representing the name of the combiner.
     * @return a String representing the name of the combiner
     */
    public String getCombinerName()
    {
        return "GaussianDecisionTemplateCombiner";
    }
    
    /** Trains the combiner on an array of Signal objects for each classifier,
     *  whose data matrices should contain a single row of likelihoods of class
     *  membership. 
     *  @param numberOfClassifiers_  The number of classes each component classifier 
     *  is trained on.
     *  @param likelihoodArrays A 2d Signal array (indexed [num examples][num classifers])
     *  containing the Signal objects from each classifier with likelihoods of class 
     *  membership of some example data. Note each Signal object is expected to 
     *  contain both the real class label of the example and one column of data, 
     *  composed of the likelihoods of class membership for each class, for that example.
     *  @throws noMetadataException if required metadata is not found.
     */
    public void train(int numberOfClassifiers_, Signal[][] likelihoodArrays) throws noMetadataException
    {
        numClassifiers = numberOfClassifiers_;
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
        
        //init decision templates
        decisionTemplates = new SingleGaussian();
        decisionTemplates.setUseFullCovar(useFullCovariance);
        String[] colLabels = new String[(this.getNumClasses()*this.getNumClassifiers())];
        for(int k=0;k<this.getNumClasses();k++)
        {
            colLabels[k] = "likelihood " + (String)this.getClassNames().get(k);
        }
        int[] numExamplesPerClass = new int[this.getNumClasses()];
        int totalNumExamples = 0;
        for(int i=0;i<likelihoodArrays.length;i++)
        {
            for (int x=0;x<this.getNumClassifiers();x++)
            {
                if (likelihoodArrays[i][x] != null)
                {
                    int exampleClass = this.getClassNames().indexOf(likelihoodArrays[i][x].getStringMetadata(Signal.PROP_CLASS));
                    numExamplesPerClass[exampleClass]++;
                    totalNumExamples++;
                }
            }
        }
        Signal[] trainingData = new Signal[this.getNumClasses()];
        for(int c=0;c<this.getNumClasses();c++)
        {
            trainingData[c] = new Signal("prototype",(String)this.getClassNames().get(c));
            double[][] likelihoods = new double[this.getNumClasses()][numExamplesPerClass[c]];
            int rowNum = 0;
            for(int i=0;i<likelihoodArrays.length;i++)
            {
                for (int j=0;j<this.getNumClassifiers();j++)
                {
                    if ((likelihoodArrays[i][j] != null)&&(this.getClassNames().indexOf(likelihoodArrays[i][j].getStringMetadata(Signal.PROP_CLASS)) == c))
                    {
                        int likelyCol = likelihoodArrays[i][j].getColumnIndex(Signal.PROP_LIKELIHOODS);
                        for(int k=0;k<this.getNumClasses();k++)
                        {
                            likelihoods[k][rowNum] = likelihoodArrays[i][j].getData()[likelyCol][k];
                        }
                        rowNum++;
                    }
                }
            }
            trainingData[c].appendMatrix(likelihoods, colLabels);
        }
        decisionTemplates.train(trainingData);
        
        this.setIsTrained(true);    
    }
    
    /**
     * Applies the trained combiner to a 2d array of Signal Objects (indexed
     *  [num examples][num classifers]) and returns a single Signal object with 
     *  classification metadata added.
     * @param decisionProfile an array of Signal Objects (indexed [num examples][num classifers], one for 
     * each classifer, for each example).
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     * @return Thrown if required metadata is not found.
     */
    public Signal classify(Signal[] decisionProfile) throws noMetadataException
    {
        Signal output = decisionProfile[0].cloneNoData();
        Signal decisionProfileData = null;
        String[] colLabels = new String[(this.getNumClasses()*this.getNumClassifiers())];
        for(int k=0;k<this.getNumClasses();k++)
        {
            colLabels[k] = "likelihood " + (String)this.getClassNames().get(k);
        }
        decisionProfileData = decisionProfile[0].cloneNoData();
        double[][] likelihoods = new double[this.getNumClasses()][this.getNumClassifiers()];
        int rowNum = 0;
        for (int j=0;j<this.getNumClassifiers();j++)
        {
            int likelyCol = decisionProfile[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for(int k=0;k<this.getNumClasses();k++)
            {
                likelihoods[k][rowNum] = decisionProfile[j].getData()[likelyCol][k];
            }
            rowNum++;
        }
        decisionProfileData.appendMatrix(likelihoods, colLabels);
        String classification = decisionTemplates.classify(decisionProfileData);
        
        //apply classification
        output.setMetadata(Signal.PROP_CLASSIFICATION, classification);
        
        return output;
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @return An array of the probabilities of class membership
     * @param decisionProfile an array of Signal Objects (indexed [num examples][num classifers], one for 
     * each classifer, for each example).
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public double[] probabilities(Signal[] decisionProfile) throws noMetadataException
    {
        Signal decisionProfileData = null;
        String[] colLabels = new String[(this.getNumClasses()*this.getNumClassifiers())];
        for(int k=0;k<this.getNumClasses();k++)
        {
            colLabels[k] = "likelihood " + (String)this.getClassNames().get(k);
        }
        decisionProfileData = decisionProfile[0].cloneNoData();
        double[][] likelihoods = new double[this.getNumClasses()][this.getNumClassifiers()];
        int rowNum = 0;
        for (int j=0;j<this.getNumClassifiers();j++)
        {
            int likelyCol = decisionProfile[j].getColumnIndex(Signal.PROP_LIKELIHOODS);
            for(int k=0;k<this.getNumClasses();k++)
            {
                likelihoods[k][rowNum] = decisionProfile[j].getData()[likelyCol][k];
            }
            rowNum++;
        }
        decisionProfileData.appendMatrix(likelihoods, colLabels);
        double[] probs = decisionTemplates.probabilities(decisionProfileData);
        return probs;
    }
}
