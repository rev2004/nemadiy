package org.imirsel.m2k.modelling.weka.mi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.imirsel.m2k.io.weka.Signal2Instance;
import org.imirsel.m2k.modelling.SignalClassifier;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import weka.classifiers.mi.MISMO;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author byunggil
 */
public class WekaMISMOClassifier implements SignalClassifier {
    
    weka.classifiers.mi.MISMO theRule = null;
    String[] classnames = null;
    private String MISMOOptions = "-D";
    Attribute classAttribute = null;
    
    private boolean verbose = true;
    
    /*
    -D
      If set, classifier is run in debug mode and
      may output additional info to the console
    -no-checks
      Turns off all checks - use with caution!
      Turning them off assumes that data is purely numeric, doesn't
      contain any missing values, and has a nominal class. Turning them
      off also means that no header information will be stored if the
      machine is linear. Finally, it also assumes that no instance has
      a weight equal to 0.
      (default: checks on)
    -C <double>
      The complexity constant C. (default 1)
    -N
      Whether to 0=normalize/1=standardize/2=neither.
      (default 0=normalize)
    -I
      Use MIminimax feature space.
    -L <double>
      The tolerance parameter. (default 1.0e-3)
    -P <double>
      The epsilon for round-off error. (default 1.0e-12)
    -M
      Fit logistic models to SVM outputs.
    -V <double>
      The number of folds for the internal cross-validation.
      (default -1, use training data)
    -W <double>
      The random number seed. (default 1)
    -K <classname and parameters>
      The Kernel to use.
      (default: weka.classifiers.functions.supportVector.PolyKernel)
     
    Options specific to kernel weka.classifiers.mi.supportVector.MIPolyKernel:
    -D
      Enables debugging output (if available) to be printed.
      (default: off)
    -no-checks
      Turns off all checks - use with caution!
      (default: checks on)
    -C <num>
      The size of the cache (a prime number).
      (default: 250007)
    -E <num>
      The Exponent to use.
      (default: 1.0)
    -L
      Use lower-order terms.
      (default: no)
     */
    
    /**
     * Creates a new instance of TrainWekaClassifier
     */
    public WekaMISMOClassifier() {
    }
    
    /**
     * Creates a new instance of TrainWekaClassifier
     * @param options optional arguments
     */
    public WekaMISMOClassifier(String options) {
        MISMOOptions = options;
    }
    
    /**
     * Returns a String representing the name of the classifier.
     *
     * @return a String representing the name of the classifier
     */
    public String getClassifierName() {
        return weka.classifiers.mi.MISMO.class.getName();
    }
    
    
    /**
     * Returns a flag indicating whether the classifier has been trained or not.
     *
     * @return a flag indicating whether the classifier has been trained or not.
     */
    public boolean isTrained() {
        return theRule != null;
    }
    
    /**
     * Returns the number of classes that the classifier is trained on. Should
     * return -1 if untrained.
     *
     * @return the number of classes that the classifier is trained on.
     */
    public int getNumClasses() {
        if (!isTrained()) {
            return -1;
        } else {
            return this.classnames.length;
        }
    }
    
    /**
     * Returns a List of the class names found in the data that the classifier
     * was trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. Should return
     * null if untrained.
     *
     * @return a List containing the class names.
     */
    public List getClassNames() {
        return Arrays.asList(this.classnames);
    }
    
    /**
     * Sets the List of the class names in the data that the classifier
     * will be trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. `
     *
     * @param classNames a List containing the class names.
     */
    public void setClassNames(List classNames) {
        //ignore
    }
    
    /**
     * Trains the classifier on the array of Signal objects. Implementations of
     * this method should also produce an ordered list of the class names which
     * can be returned with the <code>getClassNames</code> method.
     *
     * @param inputData the Signal array that the model should be trained on.
     * @throws noMetadataException Thrown if there is no class metadata to train the Gaussian model with
     */
    public void train(Signal[] inputData) {
        
        List classNamesList = new ArrayList();
        for (int i = 0; i < inputData.length; i++) {
            try {
                String className = inputData[i].getStringMetadata(Signal.PROP_CLASS);
                if ((className != null) && (!classNamesList.contains(className))) {
                    classNamesList.add(className);
                }
            } catch (noMetadataException ex) {
                throw new RuntimeException("No class metadata found to train model on!", ex);
            }
        }
        Collections.sort(classNamesList);
        classnames = (String[])classNamesList.toArray(new String[classNamesList.size()]);
        
        FastVector classValues = new FastVector(classnames.length);
        for (int i = 0; i < classnames.length; i++) {
            classValues.addElement(classnames[i]);
        }
        classAttribute = new Attribute(Signal.PROP_CLASS, classValues);
        Instances trainingDataSet = new Instances(Signal2Instance.convert(inputData[0], classAttribute));
        
        if (inputData.length > 1) {
            for (int i = 1; i < inputData.length; i++) {
                Instances aSignalInstance = Signal2Instance.convert(inputData[i], classAttribute);
                for (int j = 0; j < aSignalInstance.numInstances(); j++)
                    trainingDataSet.add(aSignalInstance.instance(j));
            }
        }
        
        trainingDataSet.setClass(classAttribute);
        
        inputData = null;
        theRule = new MISMO();
        
        // parse options
        StringTokenizer stOption = new StringTokenizer(this.MISMOOptions, " ");
        String[] options = new String[stOption.countTokens()];
        for (int i = 0; i < options.length; i++) {
            options[i] = stOption.nextToken();
        }
        
        try {
            theRule.setOptions(options);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to set MISMO classifier options!", ex);
        }
        try {
            theRule.buildClassifier(trainingDataSet);
            System.out.println("WEKA: outputting MISMO classifier; " + theRule.globalInfo());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to train classifier!", ex);
        }
    }
    
    /**
     * Classify a single vector. A RuntimeException should be thrown if the classifier is untrained.
     *
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input) {
        Instance in = new Instance(1.0, input);
        try {
            return (int) theRule.classifyInstance(in);
        } catch (Exception ex) {
            System.out.println("Exception occured when classifying a vector!\n" + ex.getMessage());
            ex.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Classify a single vector and return a class name.
     *
     * @param input Vector to classify
     * @return A String indicating class.
     */
    public String classify(double[] input) {
        return this.classnames[classifyVector(input)];
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     *
     * @param inputSignal The Signal object to calculate the probabilities of
     *                    class membership for. This probabilities should be ordered such that the indexes
     *                    match the class names returned by <code>getClassNames</code>.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(Signal inputSignal) {
        
        Instances theData = Signal2Instance.convert(inputSignal, this.classAttribute);
        
        double[] probsAccum = new double[this.getNumClasses()];
        for (int i = 0; i < theData.numInstances(); i++) {
            double[] temp;
            try {
                temp = theRule.distributionForInstance(theData.instance(i));
//                if(this.verbose){
//                    System.out.print("distribution:" );
//                    for (int j=0;j<this.getNumClasses();j++) {
//                        System.out.print(" " + temp[j]);
//                    }
//                    System.out.println("");
//                }
                
            } catch (Exception ex) {
                System.out.println("Exception occured when calculating distribution for a vector!\n" + ex.getMessage());
                ex.printStackTrace();
                return null;
            }
            for (int j = 0; j < this.getNumClasses(); j++) {
                probsAccum[j] += Math.log(temp[j]);
            }
        }
        
//        if(this.verbose){
//            System.out.print("Log Probs:" );
//            for (int j=0;j<this.getNumClasses();j++) {
//                System.out.print(" " + probsAccum[j]);
//            }
//            System.out.println("");
//        }
        
        // normalise to range 0:1
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (int j = 0; j < this.getNumClasses(); j++) {
            if (probsAccum[j] > max) {
                max = probsAccum[j];
            }
            if (probsAccum[j] < min) {
                min = probsAccum[j];
            }
        }
        for (int j = 0; j < this.getNumClasses(); j++) {
            probsAccum[j] -= min;
            probsAccum[j] /= (max - min);
        }
        // normalise to sum to 1
        double total = 0.0;
        for (int j = 0; j < this.getNumClasses(); j++) {
            total += probsAccum[j];
        }
        if (verbose) {
            DecimalFormat dec = new DecimalFormat();
            dec.setMaximumFractionDigits(3);
            if (this.verbose) {
                System.out.print("Probabilities:");
            }
            
            for (int j = 0; j < this.getNumClasses(); j++) {
                probsAccum[j] /= total;
                if (verbose) {
                    System.out.print(" " + dec.format(probsAccum[j]));
                }
            }
            if (verbose) {
                try {
                    System.out.println("\t" + inputSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
                } catch (noMetadataException nme) {
                    nme.printStackTrace(System.out);
                }
            }
        }
        
        return probsAccum;
    }
    
    /**
     * Calculates the probability of class membership of a single data vector.
     *
     * @param input The data vector to calculate the probabilities of
     *              class membership for. This probabilities should be ordered such that the indexes
     *              match the class names returned by <code>getClassNames</code> and should sum to 1.0.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(double[] input) {
        Instance in = new Instance(1.0, input);
        try {
            return theRule.distributionForInstance(in);
        } catch (Exception ex) {
            System.out.println("Exception occured when calculating distribution for a vector!\n" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Classifies the input Signal object and outputs the label of the output class
     *
     * @param inputSignal The Signal object to classify
     * @return The String label of the output class
     */
    public String classify(Signal inputSignal) {
        
        double[] probs = probabilities(inputSignal);
        int maxidx = -1;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > max) {
                max = probs[i];
                maxidx = i;
            }
        }
        
        return this.classnames[maxidx];
    }
    
    public String getMISMOOptions() {
        return MISMOOptions;
    }
    
    public void setMISMOOptions(String MISMOOptions) {
        this.MISMOOptions = MISMOOptions;
    }
    
    public boolean getVerbose() {
        return verbose;
    }
    
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}