/*
 * PCA.java
 *
 * Created on 09 February 2005, 15:03
 */

package org.imirsel.m2k.transforms.principalComponentsAnalysis;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.Vector;
import java.text.NumberFormat;
import Jama.Matrix;
import org.imirsel.m2k.transforms.SignalTrainedTransform;
import java.util.List;
import java.util.ArrayList;


/**
 * Calculates a Principal Components Analysis of the input Signal Arrays.
 *
 * Either the EigenValue decomposition or the SingularValue decomposition
 * can be used to perform the PCA.
 *
 * The EigenValues are used to calculate the percentage of the variance in
 * the original signal, encoded in each component. A percentage of the total
 * variance may be set and the largest N components will be returned, that
 * encode at least that percentage of the variance.
 *
 * @author Kris West
 */

public class PCA implements SignalTrainedTransform {
    /** A flag indicating whether the EigenValue decomposition or the SingularValue decomposition
     * should be used to perform the PCA. */
    private boolean useSVD = false;
    /** The percentage of the variance (calculated from the eigen values) that
     * will be used to determine how many components should be kept after the
     * transformation */
    private double percentageVariance = 99.0;
    /** The basis vectors for the transform */
    private double[][] basisVectors = null;
    /** A flag indicating whether a covariance matrix should be calculated for each class
     * and averaged (normalising contribution from each class) or whether a global covariance
     * matrix should be calculated. Requires "class" metadata to be set. */
    private boolean usePerClassTransform = false;
    /** A flag indicating whether the transform has been trained or not. **/
    boolean isTrained = false;
    boolean verbose = true;
    
    public static String TRANSFORM_NAME = "Principal Components Analysis";
    public String getTransformName(){
        return TRANSFORM_NAME;
    }
    
    /**Sets the flag of verbose output to console
     *@param value verbose flag
     */
    public void setVerbose(boolean value){
        verbose = value;
    }
    
    /**Gets the flag of verbose
     *@return flag of verbose
     */
    public boolean getVerbose(){
        return verbose;
    }
    
    /**
     * Returns a flag indicating whether the transform has been trained or not.
     * @return a flag indicating whether the transform has been trained or not.
     */
    public boolean isTrained() {
        return this.isTrained;
    }
    
    /**
     * Sets a flag indicating whether the transform has been trained or not.
     * @return a flag indicating whether the transform has been trained or not.
     */
    public void setIsTrained(boolean val) {
        this.isTrained = val;;
    }
    
    /**
     * Sets the useExistingTransform flag which controls new basis vactors for the
     * transform are calculated or whether they are taken as input
     *
     * @param value the useExistingTransform flag
     */
    public void setUseSVD(boolean value) {
        this.useSVD = value;
    }
    
    /**
     * Returns the useExistingTransform flag which controls new basis vactors for the
     * transform are calculated or whether they are taken as input
     *
     * @return the useExistingTransform flag
     */
    public boolean getUseSVD() {
        return this.useSVD;
    }
    
    /**
     * Returns the percentage of the total variance that will be included
     * in the transformed output matrix.
     * @return The percentage of the total variance that will be included
     * in the transformed output matrix.
     */
    public double getPercentageVariance() {
        return percentageVariance;
    }
    
    /**
     * Sets the percentage of the total variance that will be included
     * in the transformed output matrix.
     * @param value the percentage of the total variance that will be included
     * in the transformed output matrix.
     */
    public void setPercentageVariance(double value) {
        percentageVariance = value;
    }
    
    /**
     * Sets the usePerClassTransform flag which controls new basis vactors for the
     * transform are calculated or whether they are taken as input
     *
     * @param value the usePerClassTransform flag
     */
    public void setUsePerClassTransform(boolean value) {
        this.usePerClassTransform = value;
    }
    
    /**
     * Returns the usePerClassTransform flag which controls new basis vactors for the
     * transform are calculated or whether they are taken as input
     *
     * @return the usePerClassTransform flag
     */
    public boolean getUsePerClassTransform() {
        return this.usePerClassTransform;
    }
    
    /**
     * Trains the transform on the array of Signal objects.
     * @param inputData The array of Signal Objects to train the transform on.
     * @throws noMetadataException Thrown if there is no class metadata to train
     * the transform with.
     */
    public void train(org.imirsel.m2k.util.Signal[] signals) throws noMetadataException {
        //Calculate basis vectors
        int cols = signals[0].getNumCols();
        Jama.Matrix covarMatrix;
        
        if (usePerClassTransform == true) {
            //get classnames
            ArrayList theClassNames = new ArrayList();
            for (int i=0;i<signals.length;i++) {
                if (theClassNames.contains(signals[i].getStringMetadata(Signal.PROP_CLASS)) == false) {
                    //System.out.println("multiClassLinearDiscriminantAnalysis: Adding class: " + signals[i].getStringMetadata(Signal.PROP_CLASS));
                    theClassNames.add(signals[i].getStringMetadata(Signal.PROP_CLASS));
                }
            }
            if(this.verbose)
            {
                System.out.println("Calculating covariance matrix (using class labels)");
            }
            double[][][] covar = calculateCovarianceForEachClass(signals, theClassNames, cols);
            //multiply each by 1/numClasses and sum
            double[][] combinedCovar = new double[cols][cols];
            for (int i=0; i < cols; i++) {
                for (int j=0; j < cols; j++) {
                    for (int k=0; k < theClassNames.size(); k++) {
                        combinedCovar[i][j] += covar[k][i][j];
                    }
                    combinedCovar[i][j] /= (double)theClassNames.size();
                }
            }
            
            covarMatrix = new Jama.Matrix(combinedCovar);
        } else{
            if(this.verbose)
            {
                System.out.println("Calculating covariance matrix (not using class labels)");
            }
            double[][] covar = calculateCovarianceMatrixForAllClasses(signals, cols);
            covarMatrix = new Jama.Matrix(covar);
        }
        
        if(this.verbose)
        {
            System.out.println("Covariance matrix to be used in PCA:");
            for (int i=0;i<covarMatrix.getArray().length;i++)
            {
                for (int j=0;j<covarMatrix.getArray()[i].length;j++)
                {
                    System.out.print(covarMatrix.getArray()[i][j] + "\t");
                }
                System.out.println("");
            }
        }
        
        Jama.Matrix eig_mat;
        double[] eigValues;
        
        //calc eigenvectors
        if (useSVD == false) {
            if(this.verbose)
            {
                System.out.println("Calculating EigenValue decomposition.");
            }
            
            Jama.EigenvalueDecomposition eig = covarMatrix.eig();
            eig_mat = eig.getV();
            eigValues = eig.getRealEigenvalues();
        } else {
            if(this.verbose)
            {
                System.out.println("Calculating Singular Value decomposition.");
            }
            //covarMatrix = new Jama.Matrix(covar);
            Jama.SingularValueDecomposition eig = covarMatrix.svd();
            eig_mat = eig.getV();
            eigValues = eig.getSingularValues();
        }
        
        int[] idx = new int[eigValues.length];
        for (int i=0;i<eigValues.length;i++) {
            idx[i] = i;
        }
        double[][] eigArr = reverseColRowMajor(eig_mat.getArray());
        
        //sort eigen values
        boolean done = false;
        while(!done) {
            done = true;
            for (int i=1;i<eigValues.length;i++) {
                if (eigValues[i-1] < eigValues[i]) {
                    done = false;
                    double tmp = eigValues[i-1];
                    int tmpIdx = idx[i-1];
                    eigValues[i-1] = eigValues[i];
                    idx[i-1] = idx[i];
                    eigValues[i] = tmp;
                    idx[i] = tmpIdx;
                }
            }
        }
        if(this.verbose)
        {
            System.out.println("Eigen Values of transform matrix: \n");
        }
        double total = 0.0;
        for (int i=0;i<eigValues.length;i++) {
            if(this.verbose)
            {
                System.out.print(java.text.DecimalFormat.getInstance().format(eigValues[i])+ "\t");
            }
            total += eigValues[i];
        }
        if(this.verbose)
        {
            System.out.println("");
            System.out.println("% Variance: \n");
        }
        
        
        double pct = 0.0;
        int componentsToOutput = -1;
        for (int i=0; i<eigValues.length;i++) {
            pct += (eigValues[i]/total)*100;
            if(this.verbose)
            {
                System.out.print(java.text.DecimalFormat.getInstance().format(pct) + "%\t");
            }
            if ((pct > this.percentageVariance)&&(componentsToOutput == -1)) {
                componentsToOutput = i+1;
            }
        }
        if(this.verbose)
        {
            System.out.println("");
        }
        
        if (componentsToOutput == -1) {
            componentsToOutput = eigValues.length;
        }
        
        basisVectors = new double[componentsToOutput][];
        for (int i=0;i<componentsToOutput;i++) {
            basisVectors[i] = eigArr[idx[i]];
        }
        if(this.verbose)
        {
            System.out.println("\tKeeping top " + componentsToOutput + " eigenvectors of " + eigValues.length + ".");
        }
        
        
        this.setIsTrained(true);
    }
    
    /**
     * Reverse row and column ordering in double[][].
     * @param mat array to reverse ordering on.
     * @return Reverse ordered array.
     */
    public double[][] reverseColRowMajor(double[][] mat) {
        double[][] rev = new double[mat[0].length][mat.length];
        for(int i=0;i<mat.length;i++) {
            for(int j=0;j<mat[0].length;j++) {
                rev[j][i] = mat[i][j];
            }
        }
        return rev;
    }
    
    /**
     * Transform a data matrix (<code>Signal[]</code>) using the trained LDA.
     * @param input Signal[] to transform.
     * @param inPlace A flag indicating whether the transform should be performed
     * 'in place' or on a clone of the object.
     * @return Transformed array of Signal objects.
     */
    public Signal[] transform(Signal[] input, boolean inPlace) {
        Signal[] output = new Signal[input.length];
        for (int i=0;i<input.length;i++) {
            output[i] = this.transform(input[i], inPlace);
        }
        return output;
    }
    
    /**
     * Tranforms a single Signal object using the trained LDA
     * @param input Signal to be transformed
     * @param inPlace A flag indicating whether the transform should be performed
     * 'in place' or on a clone of the <code>Signal</code> Object.
     * @return Transformed Signal Object
     */
    public Signal transform(Signal input, boolean inPlace) {
        Signal out = null;
        if (!inPlace) {
            out = input.cloneNoData();
            double[][] trans_data = new double[basisVectors.length][input.getData()[0].length];
            String[] trans_names = new String[basisVectors.length];
            for(int j=0;j<basisVectors.length;j++) {
                trans_names[j] = "Principal component " + j;
            }
            for(int i=0;i<input.getNumRows();i++) {
                double[] transform_vec = new double[input.getData().length];
                for(int j=0;j<input.getData().length;j++) {
                    transform_vec[j] = input.getData()[j][i];
                }
                transform_vec = this.transform(transform_vec);
                for(int j=0;j<basisVectors.length;j++) {
                    trans_data[j][i] = transform_vec[j];
                }
            }
            
            out.appendMatrix(trans_data, trans_names);
        } else {
            String[] trans_names = new String[basisVectors.length];
            for(int j=0;j<basisVectors.length;j++) {
                trans_names[j] = "Principal component " + j;
            }
            for(int i=0;i<input.getNumRows();i++) {
                double[] transform_vec = new double[input.getData().length];
                for(int j=0;j<input.getData().length;j++) {
                    transform_vec[j] = input.getData()[j][i];
                }
                transform_vec = this.transform(transform_vec);
                for(int j=0;j<basisVectors.length;j++) {
                    input.getData()[j][i] = transform_vec[j];
                }
            }
            for(int j=(input.getNumCols()-1);j>=basisVectors.length;j--) {
                input.deleteColumn(j);
            }
            out = input;
        }
        return out;
    }
    
    /**
     * Transforms a single vector of data using the trained LDA.
     * @param input Vector to transform.
     * @return Transformed vector.
     */
    public double[] transform(double[] input) {
        if (basisVectors[0].length != input.length) {
            throw new RuntimeException("Can't transform vector, transform dim and input vec dim are different!\nTransform: " + basisVectors[0].length + " input: " + input.length);
        }
        if (!this.isTrained()) {
            throw new RuntimeException("The transformation has not been trained!");
        }
        double[] out = new double[basisVectors.length];
        for (int i=0;i<basisVectors.length;i++) {
            for (int j=0;j<basisVectors[i].length;j++) {
                out[i] += input[j] * basisVectors[i][j];
            }
        }
        return out;
    }
    
    /**
     * Calculates covariance matrix of each class of data.
     * @param sigs Data to calculate classes of.
     * @param theClassNames The class names in the data.
     * @param theFeatSize The number of features.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     * @return Covariance matrices [class][col][row].
     */
    public double[][][] calculateCovarianceForEachClass(Signal[] sigs, List theClassNames, int theFeatSize) throws noMetadataException {
        double[][][] out = new double[theClassNames.size()][theFeatSize][theFeatSize];
        
        for (int i=0; i<theClassNames.size(); i++) {
            //calc class mean
            double[] mean_vec = new double[theFeatSize];
            int rows = 0;
            
            for (int y=0; y<sigs.length; y++) {
                int theClass = theClassNames.indexOf(sigs[y].getStringMetadata(Signal.PROP_CLASS));
                if (theClass == -1) {
                    throw new RuntimeException("Class not found!");
                }
                
                if (theClass == i) {
                    rows += sigs[y].getNumRows();
                    for (int c1=0; c1< theFeatSize; c1++) {
                        for (int r=0; r < sigs[y].getNumRows(); r++) {
                            mean_vec[c1] += sigs[y].getData()[c1][r];
                        }
                    }
                }
            }
            for (int c1=0; c1< theFeatSize; c1++) {
                mean_vec[c1] /= (double)rows;
            }
            
            for (int c1=0; c1< theFeatSize; c1++) {
                for (int c2=0; c2< theFeatSize; c2++) {
                    double sum = 0.0;
                    //calc class covar
                    for (int y=0; y<sigs.length; y++) {
                        int theClass = theClassNames.indexOf(sigs[y].getStringMetadata(Signal.PROP_CLASS));
                        if (theClass == i) {
                            for (int r=0; r<sigs[y].getNumRows(); r++) {
                                sum += (sigs[y].getData()[c1][r] - mean_vec[c1]) * (sigs[y].getData()[c2][r] - mean_vec[c2]);
                            }
                        }
                    }
                    out[i][c1][c2] = ((double)sum / (double)rows);
                }
            }
        }
        return out;
    }
    
    /**
     * Calculates covariance matrix for all classes of data.
     * @param sigs Data to calculate classes of.
     * @param theFeatSize The number of features.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     * @return Covariance matrix [col][row].
     */
    public double[][] calculateCovarianceMatrixForAllClasses(Signal[] sigs, int theFeatSize) {
        double[][] out = new double[theFeatSize][theFeatSize];
        //calc mean
        double[] mean_vec = new double[theFeatSize];
        int rows = 0;
        
        for (int y=0; y<sigs.length; y++) {
            rows += sigs[y].getNumRows();
            for (int c1=0; c1< theFeatSize; c1++) {
                for (int r=0; r < sigs[y].getNumRows(); r++) {
                    mean_vec[c1] += sigs[y].getData()[c1][r];
                }
            }
        }
        for (int c1=0; c1< theFeatSize; c1++) {
            mean_vec[c1] /= (double)rows;
        }
        
        for (int c1=0; c1< theFeatSize; c1++) {
            for (int c2=0; c2< theFeatSize; c2++) {
                double sum = 0.0;
                //calc class covar
                for (int y=0; y<sigs.length; y++) {
                    for (int r=0; r<sigs[y].getNumRows(); r++) {
                        sum += (sigs[y].getData()[c1][r] - mean_vec[c1]) * (sigs[y].getData()[c2][r] - mean_vec[c2]);
                    }
                }
                out[c1][c2] = ((double)sum / (double)rows);
            }
        }
        return out;
    }
}
