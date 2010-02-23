/*
 * MultiClassLinearDiscriminantAnalysis.java
 *
 * Created on 08 April 2005, 18:00
 */

package org.imirsel.m2k.modelling.linearDiscriminantAnalysis;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import Jama.*;
import java.util.ArrayList;
import java.util.Collections;
import org.imirsel.m2k.modelling.SimpleMultiVectorSignalClassifier;
import org.imirsel.m2k.transforms.SignalTrainedTransform;

/**
 * An implementation of a multi-class linear discriminant analysis, which can be used
 * as a transform or as a classifier.
 * @author Kris West
 */
public class MClassLinearDiscriminantAnalysis extends SimpleMultiVectorSignalClassifier implements SignalTrainedTransform {
    private int[] class_members;
    private double[] prior_prob;
    private double[][] transformed_means;
    private double[][] transform_matrix;
    
    /**
     * Creates a new instance of MultiClassLinearDiscriminantAnalysis
     */
    public MClassLinearDiscriminantAnalysis() {
        super();
        class_members = null;
        prior_prob = null;
        transformed_means = null;
        transform_matrix = null;
        ClassifierName = "Multi-class Linear Discriminant Analysis";
    }
    
    public static String TRANSFORM_NAME = "Multi-class Linear Discriminant Analysis (Fisher's Criterion)";
    public String getTransformName(){
        return TRANSFORM_NAME;
    }
    
    /**
     * Copy constructor for a MultiClassLinearDiscriminantAnalysis
     * @param oldLDA The <code>MultiClassLinearDiscriminantAnalysis</code> to copied
     */
    public MClassLinearDiscriminantAnalysis(MClassLinearDiscriminantAnalysis oldLDA) {
        super(oldLDA);
        class_members = oldLDA.class_members;
        prior_prob = oldLDA.prior_prob;
        transformed_means = oldLDA.transformed_means;
        transform_matrix = oldLDA.transform_matrix;
        ClassifierName = "Multi-class Linear Discriminant Analysis";
    }
    
    /**
     * Returns a String representing the name of the classifier.
     * @return a String representing the name of the classifier
     */
    public String getClassifierName() {
        return ClassifierName;
    }
    
    /**
     * Trains the tranform/classifier model on the array of Signal objects.
     * @param signals The array of Signal Objects to train the transform/classifier on.
     * @throws noMetadataException Thrown if there is no class metadata to train
     * the classifier/transform with.
     */
    public void train(org.imirsel.m2k.util.Signal[] signals) throws noMetadataException {
        //get classnames
        ArrayList theClassNames = new ArrayList();
        for (int i=0;i<signals.length;i++) {
            if (theClassNames.contains(signals[i].getStringMetadata(Signal.PROP_CLASS)) == false) {
                //System.out.println("MultiClassLinearDiscriminantAnalysis: Adding class: " + signals[i].getStringMetadata(Signal.PROP_CLASS));
                theClassNames.add(signals[i].getStringMetadata(Signal.PROP_CLASS));
            }
        }
        Collections.sort(theClassNames);
        this.setClassNames(theClassNames);
        int featSize = signals[0].getNumCols();
        int numClasses = this.getNumClasses();
        
        //init mLDA stuff
        class_members = new int[numClasses];
        prior_prob = new double[numClasses];
        double[] means_ = new double[featSize];
        
        // init sW, sB and required temps
        double[][] sW;
        double[][] sB;
        double[][] class_means_ = new double[numClasses][featSize];
        double[][][] covars_;
        
        sW = new double[featSize][featSize];//featSize_,featSize_);
        sB = new double[featSize][featSize];
        
        int num_examples = 0;
        for (int x=0;x<signals.length;x++) {
            num_examples += signals[x].getNumRows();
            int theClass = this.getClassNames().indexOf(signals[x].getStringMetadata(Signal.PROP_CLASS));
            if (theClass == -1) {
                throw new RuntimeException("Class not found!");
            }
            class_members[theClass] += signals[x].getNumRows();
            for (int y=0;y<signals[x].getNumRows();y++) {
                for (int z=0;z<featSize;z++) {
                    means_[z] += signals[x].getData()[z][y];
                    class_means_[theClass][z] += signals[x].getData()[z][y];
                }
            }
        }
        
        // divide total by members to get mean
        for (int x=0;x<featSize;x++) {
            means_[x] /= (double)num_examples;
            for (int y=0;y<class_means_.length;y++) {
                class_means_[y][x] /= class_members[y];
            }
        }
        
        covars_ = calculateCovarianceForEachClass(signals, /*this.getClassNames(),*/ featSize);
        
        //calculate sW and sB
        int zero = 0;
        for (int x=0;x<this.getNumClasses();x++) {
            prior_prob[x] = (double)class_members[x] / (double)num_examples;
            for (int c1=0; c1< featSize; c1++) {
                for (int c2=0; c2< featSize; c2++) {
                    sW[c1][c2] += covars_[x][c1][c2];
                }
            }
            
            for (int f1 = 0; f1 < featSize; f1++) {
                //temp_mean_diff[f] -= means_[f];
                for (int f2 = 0; f2 < featSize; f2++) {
                    sB[f1][f2] += prior_prob[x] * 
                            (class_means_[x][f1] - means_[f1]) * 
                            (class_means_[x][f2] - means_[f2]);// * prior_prob[x]);
                }
            }
        }
        
        //calc transform matrix
        transform_matrix = new double[numClasses-1][featSize];
        Matrix sW_mat = null;
        Matrix inv_sW_mat = null;
        try {
            sW_mat = new Matrix(sW);//(reverseColRowMajor(sW));
            inv_sW_mat = sW_mat.inverse();
        } catch(RuntimeException rte) {
            System.out.println("multiClassLinearDiscriminantAnalysis: Encountered a Singular matrix, dropping to diagonal covariance.");
            // dumpMatrix(sW);
            for (int c1=0; c1< featSize; c1++) {
                for (int c2=0; c2< featSize; c2++) {
                    if (c1!=c2) {
                        sW_mat.set(c1,c2,0.0);
                    }
                }
            }
            
                inv_sW_mat = sW_mat.inverse();
                //inv_sW_mat = sW_mat;
            
        }
        Matrix sB_mat = new Matrix(sB);//(reverseColRowMajor(sB));
        
        Matrix criterion = inv_sW_mat.times(sB_mat);
        //System.out.println("Calculating SVD for eigen values...");
        EigenvalueDecomposition eig = criterion.eig();
        //System.out.println("\tdone.");
        Matrix eig_mat = eig.getV();
        double[] eigValues = eig.getRealEigenvalues();
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
        int limit = Math.min(this.getNumClasses()-1,eigArr.length);
        for (int i=0;i<limit;i++) {
            //System.out.println("eigArr.length=" + eigArr.length + " idx.length=" + idx.length + " transform_matrix.length=" + transform_matrix.length);
            transform_matrix[i] = eigArr[idx[i]];
        }
        
        this.setIsTrained(true);
        
        //calc transformed class means
        transformed_means = new double[numClasses][numClasses-1];
        for (int x=0;x<signals.length;x++) {
            int theClass = this.getClassNames().indexOf(signals[x].getStringMetadata(Signal.PROP_CLASS));
            if (theClass == -1) {
                throw new RuntimeException("Class not found!");
            }
            for (int y=0;y<signals[x].getNumRows();y++) {
                double[] transformed_vec = this.transform(signals[x].getDataRow(y));
                for (int z=0;z<transformed_vec.length;z++) {
                    transformed_means[theClass][z] += transformed_vec[z];
                }
            }
        }
        for (int x=0;x<numClasses;x++) {
            for (int y=0;y<numClasses-1;y++) {
                transformed_means[x][y] /= ((double)this.class_members[x]);
            }
        }
        
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
     * Get distances from each class of a vector.
     * @param input Input vector
     * @return An array of distance scores
     */
    public double[] getDistances(double[] input) {
        double[] vec = transform(input);
        double[] dist = new double[this.getNumClasses()];
        for (int i=0;i<this.getNumClasses();i++) {
            for (int j=0;j<vec.length;j++) {
                dist[i] += Math.sqrt(Math.pow((vec[j] - this.transformed_means[i][j]),2));
            }
        }
        return dist;
    }
    
    /**
     * Use getDistances to produce probabilities of class membership for each class.
     * @param input Vector to test
     * @return proababilities of class membership.
     */
    public double[] getProbs(double[] input) {
        double[] dist = getDistances(input);
        double total = 0.0;
        for(int i=0;i<dist.length;i++) {
            total += 1.0 / dist[i];
        }
        double[] probs = new double[dist.length];
        for(int i=0;i<dist.length;i++) {
            probs[i] = (1.0 / dist[i]) / total;
        }
        return probs;
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input) {
        double[] dist = getDistances(input);
        double min = Double.MAX_VALUE;
        int minIdx = -1;
        for (int i=0;i<this.getNumClasses();i++) {
            if(dist[i] < min) {
                minIdx = i;
                min = dist[i];
            }
        }
        return minIdx;
    }
    
    /**
     * Get classification probabilities for a single vector.
     * @param input Vector to classify
     * @return double[] with class probabilities.
     */
    public double[] probabilities(double[] input) {
        if (!this.isTrained()) {
            throw new RuntimeException("Unable to classify, this classifier (" + this.getClassifierName() + ") is not trained!");
        }
        double totalDist = 0;
        double[] distances = new double[this.getNumClasses()];
        distances = getDistances(input);
        for(int i = 0; i<this.getNumClasses(); i++) {
            totalDist += distances[i];
        }
        for(int i = 0; i<this.getNumClasses(); i++) {
            distances[i] = 1.0 - (distances[i] / totalDist);
        }
        
        return distances;
    }
    
    /**
     * Classifies a whole Signal object, by combining log-likelihoods of class membership
     * of each frame. Overrides default behaviour in <code>SimpleMultiVectorSignalClassifier</code>
     * with log likelihood classification.
     * @param input Vector to classify
     * @return String indicating class.
     */
    public String classify(Signal input) {
        int maxIdx = 0;
        double[] likelihood = new double[this.getNumClasses()];
        for (int i=0;i<input.getNumRows();i++) {
            double[] prob = this.getProbs(input.getDataRow(i));
            if (prob.length != likelihood.length){
                throw new RuntimeException("Probability array is different length to likelihood array!");
            }
            for (int j=0;j<prob.length;j++) {
                likelihood[j] += Math.log(prob[j]);
            }
        }
        double max = likelihood[0];
        for (int i=1;i<likelihood.length;i++) {
            if (likelihood[i] > max) {
                maxIdx = i;
                max = likelihood[i];
            }
        }
        
        if (maxIdx == -1) {
            throw new RuntimeException("Classification failed! Class number -1 returned!");
        }
        return (String)this.getClassNames().get(maxIdx);
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
            double[][] trans_data = new double[transform_matrix.length][input.getData()[0].length];
            String[] trans_names = new String[transform_matrix.length];
            for(int j=0;j<transform_matrix.length;j++) {
                trans_names[j] = "Linear discriminent component " + j;
            }
            for(int i=0;i<input.getNumRows();i++) {
                double[] transform_vec = null;/*new double[input.getData().length];
                for(int j=0;j<input.getData().length;j++)
                {
                    transform_vec[j] = input.getData()[j][i];
                }*/
                transform_vec = this.transform(/*transform_vec*/input.getDataRow(i));
                for(int j=0;j<transform_matrix.length;j++) {
                    trans_data[j][i] = transform_vec[j];
                }
            }
            
            out.appendMatrix(trans_data, trans_names);
        } else {
            String[] trans_names = new String[transform_matrix.length];
            for(int j=0;j<transform_matrix.length;j++) {
                trans_names[j] = "Linear discriminent component " + j;
            }
            for(int i=0;i<input.getNumRows();i++) {
                double[] transform_vec = new double[input.getData().length];
                for(int j=0;j<input.getData().length;j++) {
                    transform_vec[j] = input.getData()[j][i];
                }
                transform_vec = this.transform(transform_vec);
                for(int j=0;j<transform_matrix.length;j++) {
                    input.getData()[j][i] = transform_vec[j];
                }
            }
            for(int j=(input.getNumCols()-1);j>=transform_matrix.length;j--) {
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
        if (transform_matrix[0].length != input.length) {
            throw new RuntimeException("Can't transform vector, transform dim and input vec dim are different!\nTransform: " + transform_matrix[0].length + " input: " + input.length);
        }
        if (!this.isTrained()) {
            throw new RuntimeException("The transformation has not been trained!");
        }
        double[] out = new double[transform_matrix.length];
        for (int i=0;i<transform_matrix.length;i++) {
            for (int j=0;j<transform_matrix[i].length;j++) {
                out[i] += input[j] * transform_matrix[i][j];
            }
        }
        return out;
    }
    
    
    /**
     * Calculates covariance matrix of each class of data.
     * @return Covariance matrices [class][col][row].
     * @param sigs Data to calculate classes of.
     * @param theFeatSize The number of features.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public double[][][] calculateCovarianceForEachClass(Signal[] sigs, /*List theClassNames_,*/ int theFeatSize) throws noMetadataException {
        double[][][] out = new double[this.getClassNames().size()][theFeatSize][theFeatSize];
        
        for (int i=0; i<this.getClassNames().size(); i++) {
            //calc class mean
            double[] mean_vec = new double[theFeatSize];
            int rows = 0;
            
            for (int y=0; y<sigs.length; y++) {
                int theClass = this.getClassNames().indexOf(sigs[y].getStringMetadata(Signal.PROP_CLASS));
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
                for (int c2=c1; c2< theFeatSize; c2++) {
                    double sum = 0.0;
                    //calc class covar
                    for (int y=0; y<sigs.length; y++) {
                        int theClass = this.getClassNames().indexOf(sigs[y].getStringMetadata(Signal.PROP_CLASS));
                        if (theClass == i) {
                            for (int r=0; r<sigs[y].getNumRows(); r++) {
                                sum += (sigs[y].getData()[c1][r] - mean_vec[c1]) * (sigs[y].getData()[c2][r] - mean_vec[c2]);
                            }
                        }
                    }
                    out[i][c1][c2] = ((double)sum / (double)rows);
                    out[i][c2][c1] = out[i][c1][c2];
                }
            }
        }
        return out;
    }
    
    /**
     * Dumps a 2D array matrix
     * @param mat  the matrix to dump
     */
    private void dumpMatrix(double[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
    }
}
