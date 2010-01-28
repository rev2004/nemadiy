/*
 * multiClassLinearDiscriminantAnalysis.java
 *
 * Created on 08 April 2005, 18:00
 */

package org.imirsel.m2k.modelling.linearDiscriminantAnalysis;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import Jama.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.imirsel.m2k.modelling.SimpleMultiVectorSignalClassifier;
import org.imirsel.m2k.transforms.SignalTrainedTransform;

/**
 * An implementation of a multi-class linear discriminant classifier, built using
 * a one against all strategy, which can be used
 * as a transform or as a classifier. Note that when used as a transformation, it
 * will yield N components, where N is the number of classes in the training data
 * (In contrast to a classical multi-class LDA which yields N-1 components). The
 * components yielded by this transformation are not guarenteed to be independent.
 *
 * @author Kris West
 */
public class OneAgainstAllLinearDiscriminantAnalysis extends SimpleMultiVectorSignalClassifier implements SignalTrainedTransform {
    private int[] class_members;
    private int[] combined_class_members;
    private double[] prior_prob;
    private double[][] transformed_means;
    private double[][] transform_matrix;
    private boolean voting = false;
    
    public static String TRANSFORM_NAME = "One-Against-All Linear Discriminant Analysis (Fisher's Criterion)";
    public String getTransformName(){
        return TRANSFORM_NAME;
    }
    
    /**
     * Returns the flag that controls whether maximum likelihood classification
     * or voting is used.
     * @return the flag that controls whether maximum likelihood classification
     * or voting is used.
     */
    public boolean getVoting() {
        return this.voting;
    }
    
    /**
     * Sets the flag that controls whether maximum likelihood classification
     * or voting is used.
     * @param val the flag that controls whether maximum likelihood classification
     * or voting is used.
     */
    public void setVoting(boolean val) {
        this.voting = val;
    }
    
    /**
     * Creates a new instance of OneAgainstAllLinearDiscriminantAnalysis
     */
    public OneAgainstAllLinearDiscriminantAnalysis() {
        super();
        class_members = null;
        prior_prob = null;
        transformed_means = null;
        transform_matrix = null;
        combined_class_members = null;
        voting = false;
        ClassifierName = "One-Against-All Linear Discriminant Analysis";
    }
    
    /**
     * Copy constructor for a OneAgainstAllLinearDiscriminantAnalysis
     * @param oldLDA The <code>OneAgainstAllLinearDiscriminantAnalysis</code> to copied
     */
    public OneAgainstAllLinearDiscriminantAnalysis(OneAgainstAllLinearDiscriminantAnalysis oldLDA) {
        super(oldLDA);
        class_members = oldLDA.class_members;
        prior_prob = oldLDA.prior_prob;
        transformed_means = oldLDA.transformed_means;
        transform_matrix = oldLDA.transform_matrix;
        combined_class_members = oldLDA.combined_class_members;
        voting = oldLDA.voting;
        ClassifierName = "One-Against-All Linear Discriminant Analysis";
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
                //System.out.println("multiClassLinearDiscriminantAnalysis: Adding class: " + signals[i].getStringMetadata(Signal.PROP_CLASS));
                theClassNames.add(signals[i].getStringMetadata(Signal.PROP_CLASS));
            }
        }
        Collections.sort(theClassNames);
        this.setClassNames(theClassNames);
        int featSize = signals[0].getNumCols();
        int numClasses = this.getNumClasses();
        
        //init mLDA stuff
        class_members = new int[numClasses];
        combined_class_members = new int[numClasses];
        prior_prob = new double[numClasses];
        double[] means_ = new double[featSize];
        
        // init sW, sB and required temps
        double[][][] sW;
        double[][][] sB;
        double[][] class_means_ = new double[numClasses][featSize];
        double[][][] covars_;
        
        sW = new double[numClasses][featSize][featSize];//featSize_,featSize_);
        sB = new double[numClasses][featSize][featSize];
        
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
        for (int y=0;y<class_means_.length;y++) {
            combined_class_members[y] = num_examples - class_members[y];
        }
        //calc class covariance matrices
        covars_ = calculateCovarianceForEachClass(signals, this.getClassNames(), featSize);
        
        for (int x=0;x<this.getNumClasses();x++) {
            prior_prob[x] = (double)class_members[x] / (double)num_examples;
        }
        
        
        //calculate sW and sB for each class
        int zero = 0;
        for (int i=0;i<this.getNumClasses();i++) {
            for (int x=0;x<this.getNumClasses();x++) {
                //pool covariance of 'other' classes
                if (x!=i) {
                    double[][] temp_covar_matrix = (double[][])covars_[x].clone();
                    for (int c1=0; c1< featSize; c1++) {
                        for (int c2=0; c2< featSize; c2++) {
                            sW[i][c1][c2] += temp_covar_matrix[c1][c2];
                        }
                    }
                }
            }
            //divide by numClasses pooled, then add the i-throws class in
            double[][] temp_covar_matrix = (double[][])covars_[i].clone();
            for (int c1=0; c1< featSize; c1++) {
                for (int c2=0; c2< featSize; c2++) {
                    sW[i][c1][c2] /= (this.getNumClasses() - 1);
                    sW[i][c1][c2] += temp_covar_matrix[c1][c2];
                }
            }
        }
        
        //Average means from other classes
        for (int i=0;i<this.getNumClasses();i++) {
            //pool between class scatter from 'other' classes
            for (int x=0;x<this.getNumClasses();x++) {
                if (x!=i) {
                    for (int f1 = 0; f1 < featSize; f1++) {
                        for (int f2 = 0; f2 < featSize; f2++) {
                            sB[i][f1][f2] += (class_means_[x][f1] - means_[f1]) * (class_means_[x][f2] - means_[f2]);
                        }
                    }
                }
            }
            //divide by numClasses pooled, then add the i-throws class in
            for (int f1 = 0; f1 < featSize; f1++) {
                for (int f2 = 0; f2 < featSize; f2++) {
                    sB[i][f1][f2] /= (this.getNumClasses() - 1);
                    sB[i][f1][f2] += (class_means_[i][f1] - means_[f1]) * (class_means_[i][f2] - means_[f2]);
                }
            }
        }
        
        //calc transform matrices
        transform_matrix = new double[this.getNumClasses()][featSize];
        Matrix sW_mat = null;
        Matrix inv_sW_mat = null;
        for (int i=0;i<this.getNumClasses();i++) {
            try {
                sW_mat = new Matrix(sW[i]);//(reverseColRowMajor(sW));
                inv_sW_mat = sW_mat.inverse();
            } catch(RuntimeException rte) {
                System.out.println("multiClassLinearDiscriminantAnalysis: Encountered a Singular matrix, dropping to diagonal covariance.");
                for (int c1=0; c1< featSize; c1++) {
                    for (int c2=0; c2< featSize; c2++) {
                        if (c1!=c2) {
                            sW_mat.set(c1,c2,0.0);
                        }
                    }
                }
                try {
                    inv_sW_mat = sW_mat.inverse();
                } catch(RuntimeException rte2) {
                    rte2.printStackTrace();
                    throw new RuntimeException("multiClassLinearDiscriminantAnalysis: Diagonal covariance also failed!");
                }
            }
            Matrix sB_mat = new Matrix(sB[i]);
            
            Matrix criterion = inv_sW_mat.times(sB_mat);
            EigenvalueDecomposition eig = criterion.eig();
            Matrix eig_mat = eig.getV();
            double[] eigValues = eig.getRealEigenvalues();
            int[] idx = new int[eigValues.length];
            for (int e=0;e<eigValues.length;e++) {
                idx[e] = e;
            }
            double[][] eigArr = reverseColRowMajor(eig_mat.getArray());
            
            //sort eigen values
            boolean done = false;
            while(!done) {
                done = true;
                for (int e=1;e<eigValues.length;e++) {
                    if (eigValues[e-1] < eigValues[e]) {
                        done = false;
                        double tmp = eigValues[e-1];
                        int tmpIdx = idx[e-1];
                        eigValues[e-1] = eigValues[e];
                        idx[e-1] = idx[e];
                        eigValues[e] = tmp;
                        idx[e] = tmpIdx;
                    }
                }
            }
            //keep top dimension for one against all LDA
            transform_matrix[i] = eigArr[idx[0]];
        }
        this.setIsTrained(true);
        
        
        
        //calc transformed class means
        transformed_means = new double[numClasses][2];
        for (int x=0;x<signals.length;x++) {
            //get class
            int theClass = this.getClassNames().indexOf(signals[x].getStringMetadata(Signal.PROP_CLASS));
            if (theClass == -1) {
                throw new RuntimeException("Class not found!");
            }
            //transform data with all models
            for (int y=0;y<signals[x].getNumRows();y++) {
                // add to classes own mean for this class' LDA and to 'other' classes mean for other LDAs
                double[] transformed_vec = this.transform(signals[x].getDataRow(y));
                for (int z=0;z<this.getNumClasses();z++) {
                    if (z == theClass) {
                        transformed_means[z][0] += transformed_vec[z];
                    } else {
                        transformed_means[z][1] += transformed_vec[z];
                    }
                }
            }
        }
        for (int x=0;x<numClasses;x++) {
            transformed_means[x][0] /= (double)this.class_members[x];
            transformed_means[x][1] /= (double)this.combined_class_members[x];
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
     * Get probabilities from each class for an input vector.
     * @param input Input vector
     * @return An array of probabilities of class membership
     */
    public double[] getProbs(double[] input) {
        double[] vec = transform(input); // output should be same length as num classes
        double[] dist = new double[this.getNumClasses()];
        double totalDist = 0.0;
        //get dist from class and from rest, for each class
        for (int i=0;i<this.getNumClasses();i++) {
            double classDist = 1.0 / Math.sqrt(Math.pow((vec[i] - this.transformed_means[i][0]),2));
            double restDist = 1.0 / Math.sqrt(Math.pow((vec[i] - this.transformed_means[i][1]),2));
            //normalise to sum to one (as projections for each class will be on different scales;
            dist[i] = classDist / (classDist + restDist);
            totalDist += dist[i];
        }
        //Normalise to sum to 1.0 across all classes
        for (int i=0;i<this.getNumClasses();i++) {
            dist[i] = dist[i] / totalDist;
        }
        return dist;
    }
    
    /**
     * Get classification probabilities for a single vector.
     * @param input Vector to classify
     * @return double[] with class probabilities.
     */
    public double[] probabilities(double[] input){
        return getProbs(input);
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input) {
        double[] dist = getProbs(input);
        double max = Double.MIN_VALUE;
        int maxIdx = -1;
        for (int i=0;i<this.getNumClasses();i++) {
            if(dist[i] > max) {
                maxIdx = i;
                max = dist[i];
            }
        }
        return maxIdx;
    }
    
    /**
     * Classifies a whole Signal object, either by combining log-likelihoods of
     * class membership of each frame (overrides default behaviour in
     * <code>SimpleMultiVectorSignalClassifier</code> with log likelihood
     * classification), or by using a simple voting system. Voting behaviour is
     * enabled by calling <code>setVotingBehaviour(true)</code>.
     * @param input Vector to classify
     * @return String indicating class.
     */
    public String classify(Signal input) {
        int maxIdx = 0;
        if (this.voting) {
            int[] votes = new int[this.getNumClasses()];
            for (int i=0;i<input.getNumRows();i++) {
                int classification = this.classifyVector(input.getDataRow(i));
                votes[classification]++;
            }
            int max = votes[0];
            for (int i=1;i<votes.length;i++) {
                if (votes[i] > max) {
                    maxIdx = i;
                    max = votes[i];
                }
            }
        } else {
            double[] likelihood = new double[this.getNumClasses()];
            for (int i=0;i<input.getNumRows();i++) {
                double[] prob = this.getProbs(input.getDataRow(i));
                for (int j=0;j<likelihood.length;j++) {
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
        }
        if (maxIdx == -1) {
            throw new RuntimeException("Classification failed! Class number -1 returned!");
        }
        return (String)this.getClassNames().get(maxIdx);
    }
    
    /**
     * Transform a data matrix (<code>Signal[]</code>) using the trained LDAs.
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
                trans_names[j] = "One-Against-all LDA component " + j;
            }
            for(int i=0;i<input.getNumRows();i++) {
                double[] transform_vec = new double[input.getData().length];
                for(int j=0;j<input.getData().length;j++) {
                    transform_vec[j] = input.getData()[j][i];
                }
                transform_vec = this.transform(transform_vec);
                for(int j=0;j<transform_matrix.length;j++) {
                    trans_data[j][i] = transform_vec[j];
                }
            }
            
            out.appendMatrix(trans_data, trans_names);
        } else {
            String[] trans_names = new String[transform_matrix.length];
            for(int j=0;j<transform_matrix.length;j++) {
                trans_names[j] = "One-Against-all LDA component " + j;
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
}