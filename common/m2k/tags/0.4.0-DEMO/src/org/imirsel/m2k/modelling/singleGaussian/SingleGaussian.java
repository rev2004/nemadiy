package org.imirsel.m2k.modelling.singleGaussian;

import java.util.ArrayList;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import Jama.Matrix;
import java.util.Collections;
import org.imirsel.m2k.modelling.SimpleMultiVectorSignalClassifier;

/**
 * A single Gaussian classifier that is trained on an array of <code>Signal</code> 
 * objects with either diagonal or full covariance.
 * @author  Kris West
 */
public class SingleGaussian extends SimpleMultiVectorSignalClassifier{
    
    /** A flag controlling whether full or diagonal covariance is used. **/
    boolean useFullCovariance = true;
    /** Vector of variances for each class. **/
    double[][] vars;
    /** Square matrix of covariances for each class. **/
    double[][][] covars;
    /** Vector of means for each class. **/
    double[][] means;
    
    /**
     * Creates a new instance of SingleGaussian
     */
    public SingleGaussian() {
        super();
        vars = null;
        covars = null;
        means = null;
        useFullCovariance = true;
        ClassifierName = "Single Gaussian";
    }
    
    /**
     * Copy constructor for a SingleGaussian
     * @param oldGaussian The <code>SingleGaussian</code> to copied
     */
    public SingleGaussian(SingleGaussian oldGaussian) {
        super(oldGaussian);
        vars = oldGaussian.vars;
        covars = oldGaussian.covars;
        means = oldGaussian.means;
        useFullCovariance = oldGaussian.useFullCovariance;
        ClassifierName = "Single Gaussian";
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
        useFullCovariance = val;
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
        }
        double minDist = Double.POSITIVE_INFINITY;
        double[] distances = new double[this.getNumClasses()];
        int min = -1;
        for(int i = 0; i<this.getNumClasses(); i++) {
            distances[i] = mahalanobis(input, means[i], covars[i]);
            if (distances[i] <= minDist) {
                minDist = distances[i];
                min = i;
            }
        }
        if (min == -1)
        {
            System.out.println("!!Warning SignalGaussian.classifyVector() returned invalid class index!!\nnumClasses: " + this.getNumClasses() + "Minimum distance:" + minDist + "\n");
            System.out.print("Input vector: ");
            for (int i=0;i<input.length;i++)
            {
                System.out.print(input[i] + " ");
            }
            System.out.println("");
            min = (int)Math.rint(Math.random() * (((double)this.getNumClasses()) - 1.0));
        }
        return min;
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
        }
        double totalDist = 0;
        double[] distances = new double[this.getNumClasses()];
        for(int i = 0; i<this.getNumClasses(); i++) {
            distances[i] = mahalanobis(input, means[i], covars[i]);
            totalDist += distances[i];
        }
        for(int i = 0; i<this.getNumClasses(); i++) {
            distances[i] = 1.0 - (distances[i] / totalDist);
        }
        
        return distances;
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
        
        this.vars = new double[this.getNumClasses()][inputData[0].getNumCols()];
        this.covars = new double[this.getNumClasses()][inputData[0].getNumCols()][inputData[0].getNumCols()];
        this.means = new double[this.getNumClasses()][inputData[0].getNumCols()];
        
        // calculate means and vars
        int[] classSizes = new int[this.getNumClasses()];
        for (int i = 0; i < inputData.length; i++) {
            int classNum = this.getClassNames().indexOf(inputData[i].getStringMetadata(Signal.PROP_CLASS));
            classSizes[classNum] += inputData[i].getNumRows();
            
            for (int j = 0; j < inputData[i].getNumCols(); j++) {
                for (int k = 0; k < inputData[i].getNumRows(); k++) {
                    this.means[classNum][j] += (inputData[i].getData())[j][k];
                    this.vars[classNum][j] += ((inputData[i].getData())[j][k] * (inputData[i].getData())[j][k]);
                }
            }
        }
        for (int i = 0; i < this.getNumClasses(); i++) {
            for (int j = 0; j < inputData[0].getNumCols(); j++) {
                this.means[i][j] /= classSizes[i];
                this.vars[i][j] /= classSizes[i];
            }
        }
        for (int i = 0; i < this.getNumClasses(); i++) {
            for (int j = 0; j < inputData[0].getNumCols(); j++) {
                double mean = this.means[i][j];
                this.vars[i][j] -= (mean * mean);
            }
        }
        
        if (useFullCovariance == true) {
            // Calculate full covariance
            for (int j = 0; j < inputData[0].getNumCols(); j++) {
                for (int k = 0; k < inputData[0].getNumCols(); k++) {
                    for (int i = 0; i < inputData.length; i++) {
                        int classNum = this.getClassNames().indexOf(inputData[i].getStringMetadata(Signal.PROP_CLASS));
                        
                        for (int r = 0; r < inputData[i].getNumRows(); r++) {
                            this.covars[classNum][j][k] += (inputData[i].getData()[j][r] - this.means[classNum][j]) * (inputData[i].getData()[k][r] - this.means[classNum][k]);
                        }
                    }
                    for (int i = 0; i < this.getNumClasses(); i++) {
                        this.covars[i][j][k] /= classSizes[i];
                    }
                }
            }
            //invert covariance matrix
            try {
                for (int i = 0; i < this.getNumClasses(); i++) {
                    Matrix cov = new Matrix(this.covars[i]);
                    cov = cov.inverse();
                    this.covars[i] = cov.getArray();
                }
            } catch(Exception e) {
                System.out.println("Failed training full covariance Gaussian, dropping to diagonal covariance! " + e);
                this.setUseFullCovar(false);
                for (int i = 0; i < this.getNumClasses(); i++) {
                    for (int j = 0; j < inputData[0].getNumCols(); j++) {
                        if (this.vars[i][j] < 0.0000001) { // little or no variance
                            this.covars[i][j][j] = 1.0;
                        } else {
                            this.covars[i][j][j] = 1.0 / this.vars[i][j];
                        }
                    }
                }
            }
        } else {
            //Calculate diagonal covariance
            for (int i = 0; i < this.getNumClasses(); i++) {
                this.covars[i] = new double[inputData[0].getNumCols()][inputData[0].getNumCols()];
                for (int j = 0; j < inputData[0].getNumCols(); j++) {
                    if (vars[i][j] < 0.0000001) { // little or no variance
                        this.covars[i][j][j] = 1.0;
                    } else {
                        this.covars[i][j][j] = 1.0 / vars[i][j];
                    }
                }
            }
        }
        
        this.setIsTrained(true);
    }
    
    /**
     * Calculates the <code>mahalanobis</code> distance of the input vector from
     * the means and inverted covariances of the Gaussian model supplied
     * @param vec The vector to calculate the <code>mahalanobis</code> distance for
     * @param means The means of the Gaussian model to test against
     * @param covars The inverted covariances of the Gaussian model to test against
     * @return The <code>mahalanobis</code> distance
     */
    public double mahalanobis(double[] vec, double[] means, double[][] covars) {
        double[][] vec1 = new double[1][vec.length];
        double[][] mean1 = new double[1][vec.length];
        for (int i = 0; i<vec.length; i++) {
            vec1[0][i] = vec[i];
            mean1[0][i] = means[i];
        }
        Matrix vector = new Matrix(vec1);
        Matrix meanvec = new Matrix(mean1);
        Matrix diff = vector.minus(meanvec);
        Matrix covar = new Matrix(covars);
        
        Matrix temp = diff.times(covar);
        Matrix squareSum = temp.times(diff.transpose());
        
        return squareSum.get(0,0);
    }
}

