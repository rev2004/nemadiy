package org.imirsel.m2k.modelling.jahmm;

import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import java.util.Collections;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.modelling.SignalClassifier;
import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.learn.*;

/**
 * An ergodic HMM classifier based on Jahmm.
 * @author  Kris West
 */
public class ErgodicHMM implements SignalClassifier{
    
    /** The classifier name. **/
    public String ClassifierName = "simpleMultiVectorSignalClassifier - !!Abstract!!";
    /** A flag indicating whether a classifier has been trained or not. **/
    private boolean isTrained = false;
    /** ArrayList of class names. **/
    private List classNames;
    /** Array of Jahmm Hidden Markov Models. */
    private Hmm[] theHmms = null;
    /** Determines whether debugging information is output to the console. */
    private boolean verbose = true;
    /** Determines the number of states in the HMMs trained. */
    private int numStates = 3;
    /** Determines the number of Kmeans iterations used to initialise the HMM, -1
     *  indicates that Kmeans should continue until a fixed point is reached. */
    private int KmeansIterations = -1;
    /** Determines the number of iterations of BaumWelch reestimation that are
     *  performed, -1 indicates that the default number should be used. */
    private int BWIterations = -1;
    
    /**
     * Creates a new instance of ErgodicHMM
     */
    public ErgodicHMM() {
        isTrained = false;
        classNames = null;
        ClassifierName = "ErgodicHMM - based on Jahmm";
    }
    
    /**
     * Copy constructor for a ErgodicHMM
     * @param oldGaussian The <code>ErgodicHMM</code> to copied
     */
    public ErgodicHMM(ErgodicHMM oldHMM) {
        isTrained = oldHMM.isTrained;
        classNames = oldHMM.classNames;
        ClassifierName = "ErgodicHMM - based on Jahmm";
        if (oldHMM.theHmms != null) {
            this.theHmms = new Hmm[oldHMM.theHmms.length];
            for (int i=0;i<oldHMM.theHmms.length;i++) {
                try{
                    this.theHmms[i] = (Hmm)oldHMM.theHmms[i].clone();
                } catch(CloneNotSupportedException cnse) {
                    throw new RuntimeException("Cloning not supported by HMMs, contact developer!", cnse);
                }
            }
        }
        
    }
    
    /**
     * Sets the value of the verbose output flag
     *
     * @param value the value which verbose is set to
     *
     * @see #verbose
     */
    public void setVerbose(boolean value) {
        this.verbose = value;
    }
    
    /**
     * Returns the value of the verbose output flag
     *
     * @return verbose
     *
     * @see #verbose
     */
    public boolean getVerbose() {
        return this.verbose;
    }
    
    /** Sets the number of states in the HMMs trained.
     *  @param val the number of states in the HMMs trained.
     */
    public void setNumStates(int val) {
        this.numStates = val;
    }
    
    /** Returns the number of states in the HMMs trained.
     *  @return the number of states in the HMMs trained.
     */
    public int getNumStates() {
        return this.numStates;
    }
    
    /** Sets the number of Kmeans iterations used to initialise the HMM, -1
     *  indicates that Kmeans should continue until a fixed point is reached.
     *  @param val the number of Kmeans iterations used to initialise the HMM, -1
     *  indicates that Kmeans should continue until a fixed point is reached.
     */
    public void setKmeansIterations(int val) {
        this.KmeansIterations = val;
    }
    
    /** Returns the number of Kmeans iterations used to initialise the HMM, -1
     *  indicates that Kmeans should continue until a fixed point is reached.
     *  @return the number of Kmeans iterations used to initialise the HMM, -1
     *  indicates that Kmeans should continue until a fixed point is reached.
     */
    public int getKmeansIterations() {
        return this.KmeansIterations;
    }
    
    /** Sets the number of iterations of BaumWelch reestimation that are
     *  performed, -1 indicates that the default number should be used.
     *  @param val the number of iterations of BaumWelch reestimation that are
     *  performed, -1 indicates that the default number should be used.
     */
    public void setBWIterations(int val) {
        this.BWIterations = val;
    }
    
    /** Returns the number of iterations of BaumWelch reestimation that are
     *  performed, -1 indicates that the default number should be used.
     *  @return the number of iterations of BaumWelch reestimation that are
     *  performed, -1 indicates that the default number should be used.
     */
    public int getBWIterations() {
        return this.BWIterations;
    }
    
    /**
     * Returns a String representing the name of the classifier.
     * @return a String representing the name of the classifier
     */
    public String getClassifierName() {
        return ClassifierName;
    }
    
    /**
     * Returns a flag indicating whether a classifier has been trained or not.
     * @return a flag indicating whether a classifier has been trained or not.
     */
    public boolean isTrained() {
        return this.isTrained;
    }
    
    /**
     * Sets a flag indicating whether a classifier has been trained or not.
     * @return a flag indicating whether a classifier has been trained or not.
     */
    public void setIsTrained(boolean val) {
        this.isTrained = val;
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input) {
        int maxIdx = 0;
        double max = Double.NEGATIVE_INFINITY;
        double[] likelihood = new double[this.getNumClasses()];
        
        Vector sequence = new Vector();
        sequence.addElement(new ObservationVector(input));
        
        for (int i=0;i<this.getNumClasses();i++) {
            likelihood[i] = this.theHmms[i].lnProbability(sequence);
            if (likelihood[i] > max) {
                maxIdx = i;
                max = likelihood[i];
            }
        }
        
        if (maxIdx == -1) {
            throw new RuntimeException("Classification failed! Class number -1 returned!");
        }
        return maxIdx;
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return double[] of class likelihoods.
     */
    public double[] probabilities(double[] input) {
        double total = 0.0;
        double[] likelihood = new double[this.getNumClasses()];
        
        Vector sequence = new Vector();
        sequence.addElement(new ObservationVector(input));
        
        for (int i=0;i<this.getNumClasses();i++) {
            likelihood[i] = Math.pow(Math.E,this.theHmms[i].lnProbability(sequence));
            total += likelihood[i];
        }
        
        for (int i=0;i<this.getNumClasses();i++) {
            likelihood[i] /= total;
        }
        
        return likelihood;
    }
    
    /**
     * Classify a single vector and return a class name.
     * @param input Vector to classify
     * @return A String indicating class.
     */
    public String classify(double[] input) {
        int classNum = this.classifyVector(input);
        return (String)this.getClassNames().get(classNum);
    }
    
    /**
     * Returns the number of classes that the classifier is trained on. Should
     * return -1 if untrained.
     * @return the number of classes that the classifier is trained on.
     */
    public int getNumClasses() {
        return this.classNames.size();
    }
    
    /**
     * Returns an ArrayList of the class names found in the data that the classifier
     * was trained on. This ArrayList should be ordered such that the integers returned
     * from the classification methods index the correct class names. Should return
     * null if untrained.
     * @return an ArrayList containing the class names.
     */
    public List getClassNames() {
        return this.classNames;
    }
    
    /**
     * Sets the List of the class namesin the data that the classifier
     * will be trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. `
     * @param classNames a List containing the class names.
     */
    public void setClassNames(List classNames_) {
        Collections.sort(classNames_);
        this.classNames = classNames_;
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
        
        if (this.verbose) {
            System.out.println("Training HMMs");
        }
        int cols = inputData[0].getNumCols();
        Vector[] sequences = new Vector[this.getNumClasses()];
        for (int i=0;i<this.getNumClasses();i++) {
            sequences[i] = new Vector();
        }
        for(int i=0;i<inputData.length;i++) {
            int classNum = this.getClassNames().indexOf(inputData[i].getStringMetadata(Signal.PROP_CLASS));
            Vector sequence = new Vector();
            for (int j=0;j<inputData[i].getNumRows();j++) {
                sequence.addElement(new ObservationVector(inputData[i].getDataRow(j)));
            }
            sequences[classNum].addElement(sequence);
        }
        if (this.verbose) {
            System.out.println("HMM training data summary:");
            for (int i=0;i<this.getNumClasses();i++) {
                System.out.println("\t" + sequences[i].size() + " sequences in class " + (String)this.getClassNames().get(i));
            }
        }
        
        if (this.verbose) {
            System.out.println("Initialising HMMs...");
        }
        theHmms = new Hmm[this.getNumClasses()];
        for (int i=0;i<this.getNumClasses();i++) {
            if (this.verbose) {
                System.out.println("\tInit " + (String)this.getClassNames().get(i) + " HMM");
            }
            KMeansLearner kml = new KMeansLearner(this.numStates, new OpdfMultiGaussianFactory(cols), sequences[i]);
            if (this.KmeansIterations == -1) {
                theHmms[i] = kml.learn();
            } else {
                for (int k=0;k<this.KmeansIterations;k++) {
                    theHmms[i] = kml.iterate();
                    if (kml.isTerminated()) {
                        break;
                    }
                }
            }
        }
        
        if (this.verbose) {
            System.out.println("Re-estimating HMMs...");
        }
        for (int i=0;i<this.getNumClasses();i++) {
            if (this.verbose) {
                System.out.println("\tRe-estimate " + (String)this.getClassNames().get(i) + " HMM");
            }
            BaumWelchLearner BWL = new BaumWelchScaledLearner(this.numStates, new OpdfMultiGaussianFactory(cols), sequences[i]);
            if (this.BWIterations == -1) {
                theHmms[i] = BWL.learn(theHmms[i]);
            } else {
                for (int b=0;b<this.BWIterations;b++) {
                    theHmms[i] = BWL.iterate(theHmms[i]);
                }
            }
        }
        if (this.verbose) {
            System.out.println("Done training HMMs.");
        }
        this.setIsTrained(true);
    }
    
    /**
     * Classifies a whole Signal object using the trained HMMs.
     * @param input Vector to classify
     * @return String indicating class.
     */
    public String classify(Signal input) {
        int maxIdx = 0;
        double max = Double.NEGATIVE_INFINITY;
        double[] likelihood = new double[this.getNumClasses()];
        
        Vector sequence = new Vector();
        for (int j=0;j<input.getNumRows();j++) {
            sequence.addElement(new ObservationVector(input.getDataRow(j)));
        }
        
        for (int i=0;i<this.getNumClasses();i++) {
            likelihood[i] = this.theHmms[i].lnProbability(sequence);
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
     * Calculates the probability of class membership of a Signal Object.
     * @param inputSignal The Signal object to calculate the probabilities of
     * class membership for. This probabilities should be ordered such that the indexes
     * match the class names returned by <code>getClassNames</code>.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(Signal input) {
        double[] likelihood = new double[this.getNumClasses()];
        Vector sequence = new Vector();
        for (int j=0;j<input.getNumRows();j++) {
            sequence.addElement(new ObservationVector(input.getDataRow(j)));
        }
        double total = 0.0;
        for (int i=0;i<this.getNumClasses();i++) {
            likelihood[i] = this.theHmms[i].lnProbability(sequence);
            total += likelihood[i];
        }
        for (int i=0;i<this.getNumClasses();i++) {
            likelihood[i] /= total;
        }
        return likelihood;
    }
}

