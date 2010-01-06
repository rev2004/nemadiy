package org.imirsel.m2k.modelling;

import java.util.Random;
import org.imirsel.m2k.util.Signal;

/**
 * A module that takes as input a Signal array ,
 * splits it randomly into a test and training set and outputs them. The
 * proportion of data to be used for training is controlled by the
 * <code>TrainingProportion</code> parameter. The seed used instantiate
 * the random number generator used to produce the split is taken as a
 * parameter so that splits are reproducible."
 *
 * @author  Kris West
 */
public class TestTrainSplitSignalArrayClass{
    double TrainingProportion = 0.5;
    int seed = 3;
    Random rnd;
    
    /**
     * Returns the proportion of data to be used for training
     * @return the proportion of data to be used for training
     */
    public double getTrainingProportion() {
        return TrainingProportion;
    }
    
    /**
     * Sets the proportion of data to be used for training
     * @param val the proportion of data to be used for training
     */
    public void setTrainingProportion(double val) {
        TrainingProportion = val;
    }
    
    /**
     * Returns seed, the random seed used to produce the cross-validated data sets
     * @return seed, the random seed used to produce the cross-validated data sets
     */
    public int getSeed() {
        return seed;
    }
    
    /**
     * Sets seed, the random seed used to produce the cross-validated data sets
     * @param val seed, the random seed used to produce the cross-validated data sets
     */
    public void setSeed(int val) {
        seed = val;
    }
    
    
    /** Creates a new instance of CreateTestandTrainMatrices */
    public TestTrainSplitSignalArrayClass() {
    }
    
    /**
     * Collects input Signal arrays until a null input is received,
     * splits them into a test and training set and outputs them. Each array is split
     * evenly across the test and training sets. The proportion of data to be used
     * for training is controlled by the <code>TrainingProportion</code> parameter.
     * @throws java.lang.Exception If an error occurs
     */
    protected Signal[][] split(Signal[] signals) throws java.lang.Exception {
        
        System.out.print("Creating test and train sets,");
        //output
        
        int NumExamples = signals.length;
        
        System.out.print(" from " + NumExamples + " examples.\n");
        System.out.flush();
        
        int totalUsed = 0;
        double totalProportionUsed = 0.0;
        int [] partition = new int[NumExamples];
        
        rnd = new Random();
        rnd.setSeed(seed);
        
        while(totalProportionUsed < TrainingProportion) {
            int tempIdx = Math.round((float)(rnd.nextFloat() * (float)(NumExamples-1)));
            if (partition[tempIdx] != 1) {
                partition[tempIdx] = 1;
                totalUsed++;
                totalProportionUsed = (double)totalUsed / (double)NumExamples;
            }
        }
        
        Signal[] trainSet = new Signal[totalUsed];
        Signal[] testSet = new Signal[NumExamples - totalUsed];
        
        int trainCount = 0;
        int testCount = 0;
        for (int j=0;j<NumExamples;j++) {
            if(partition[j] == 1) {
                trainSet[trainCount] = signals[j];
                trainCount++;
            } else {
                testSet[testCount] = signals[j];
                testCount++;
            }
        }
        
        
        System.out.println("Total number of signals: " + NumExamples);
        System.out.println("Outputting training set, " + trainCount + " signals");
        
        return new Signal[][]{trainSet,testSet};
        
    }
    
}
