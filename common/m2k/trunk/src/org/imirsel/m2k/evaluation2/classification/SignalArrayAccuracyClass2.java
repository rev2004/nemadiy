/*
 * SignalArrayAccuracyClass.java
 *
 * Created on 11 February 2006, 11:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification;
import java.util.ArrayList;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.io.*;
import org.imirsel.m2k.evaluation2.tagsClassification.TagClassificationGroundTruthFileReader;

/**
 *
 * @author kris
 */
public class SignalArrayAccuracyClass2 {
    private String resultFileExt = ".eval";
    private String signalFileExt = ".asciiEvalSignal";
    
    private static final int COL_WIDTH = 7;
    private ArrayList<String> classNames = null;
    private ArrayList<Signal> resultSignals = null;
    private ArrayList<String[]> hierarchies = null;
    private ArrayList<String> hierachiesKey = null;
    private boolean usingAHierachy = false;
    
    private ArrayList<Double> StoredAccuracies = null;
    private ArrayList<Double> StoredNormalisedAccuracies = null;
    private ArrayList<Double> StoredDiscountedAccuracies = null;
    private ArrayList<Double> StoredNormalisedDiscountedAccuracies = null;
    
    private ArrayList<String> indivIterationOutput = null;
    
    private boolean useNormalisedEvalMetricForRanking = false;
    
    private String ModelName = "Model";
    private String storageDirectory = "evalTest";
    private String classHierarchyFile = "";
    
    private String bigDivider =    "================================================================================\n";
    private String littleDivider = "--------------------------------------------------------------------------------\n";
    
    private DecimalFormat dec = new DecimalFormat("0.00");
    
    private int[][] storedConfusion = null;
    private double[][] storedDiscountedConfusion = null;
    
    private int numberOfResultSets = 0;
    private int totalNumberOfExamples = 0;
    
    private boolean verbose = false;
    
    private File reportFile = null;
    
    /** Creates a new instance of SignalArrayAccuracyClass
     * @param ModelName_
     * @param resultFileExt_
     * @param signalFileExt_
     * @param storageDirectory_
     * @param classHierarchyFile_
     * @param verbose_
     */
    public SignalArrayAccuracyClass2(String ModelName_, String resultFileExt_, String signalFileExt_, String storageDirectory_, String classHierarchyFile_, boolean verbose_) {
        this.classNames = null;
        this.resultSignals = null;
        this.storedConfusion = null;
        this.hierarchies = null;
        this.hierachiesKey = null;
        this.usingAHierachy = false;
        this.numberOfResultSets = 0;
        this.totalNumberOfExamples = 0;
        this.StoredAccuracies = null;
        this.StoredNormalisedAccuracies = null;
        this.StoredDiscountedAccuracies = null;
        this.StoredNormalisedDiscountedAccuracies = null;
        this.indivIterationOutput = null;
        
        this.ModelName = ModelName_;
        this.resultFileExt = resultFileExt_;
        this.signalFileExt = signalFileExt_;
        this.storageDirectory = storageDirectory_;
        this.classHierarchyFile = classHierarchyFile_;
        
        this.verbose = verbose_;
        
    }
    
    public Signal[] evaluate(Signal[] theSignals) throws IllegalArgumentException, IllegalArgumentException, IOException, noMetadataException {
        numberOfResultSets++;
        
        if (classNames == null) {
            classNames = new ArrayList<String>();
        }
        if ((classHierarchyFile != null) && ((classHierarchyFile.trim()).compareTo("") != 0 )) {
            usingAHierachy = true;
        }
        
        if(this.usingAHierachy) {
            initHierachy(classHierarchyFile);
        }
        
        //Prepare for output
        File StorageDir = new File(this.storageDirectory);
        String modelFile = this.getModelName().replace(" ", "_");
        File resultFile = new File(StorageDir.getCanonicalPath() + File.separator + modelFile + this.resultFileExt);
        if(verbose) {
            System.out.println("Output path: " + resultFile.getPath());
        }
        StorageDir.mkdirs();
//        if (!StorageDir.exists()) {
//            if (!StorageDir.mkdirs()) {
//                throw new RuntimeException("Could not create the output folder.");
//            }
//        }
        //resultFile.mkdirs();
        resultFile.createNewFile();
        
        if (resultFile == null) {
            throw new IllegalArgumentException("File should not be null, there was a fault in file creation.");
        }
        
        if (resultFile.canWrite() == false) {
            throw new IllegalArgumentException("File cannot be written: " + resultFile);
        }
        
        //declared here only to make visible to finally clause; generic reference
        Writer output = null;
        String outBufferString = "";
        
        
//EVALUATE THIS ITERATION
        
        totalNumberOfExamples += theSignals.length;
        if(verbose) {
            System.out.println("Total number of examples this iteration is = " + theSignals.length);
            if (numberOfResultSets > 1) {
                System.out.println("Total number of examples over all iterations is = " + totalNumberOfExamples);
            }
        }
        
        for (int i = 0; i < theSignals.length; i++) {
            if (classNames.contains(theSignals[i].getStringMetadata(Signal.PROP_CLASS)) == false) {
                classNames.add(theSignals[i].getStringMetadata(Signal.PROP_CLASS));
            }
            if (classNames.contains(theSignals[i].getStringMetadata(Signal.PROP_CLASSIFICATION)) == false) {
                classNames.add(theSignals[i].getStringMetadata(Signal.PROP_CLASSIFICATION));
            }
        }

        Collections.sort(classNames);
        
        // initialize mcnemar's array
        int[] mcnemars = new int[theSignals.length];
        for (int k = 0; k < mcnemars.length; k++ ) {
            mcnemars[k] = 0;
        }
        
        int errors = 0;
        int[][] confusion = new int[classNames.size()][classNames.size()];
        double[][] discountedConfusion = null;
        if(this.usingAHierachy) {
            discountedConfusion = new double[classNames.size()][classNames.size()];
        }
        
        //double totalScore = 0.0;
        
        for(int x=0; x<theSignals.length; x++) {
            //Do simple evaluation
            int classification = classNames.indexOf(theSignals[x].getStringMetadata(Signal.PROP_CLASSIFICATION));
            String classificationString = theSignals[x].getStringMetadata(Signal.PROP_CLASSIFICATION);
            int truth = classNames.indexOf(theSignals[x].getStringMetadata(Signal.PROP_CLASS));
            String truthString = theSignals[x].getStringMetadata(Signal.PROP_CLASS);
            
            confusion[classification][truth]++;
            if((this.usingAHierachy)&&(truthString.equals(classificationString)))
            {
                discountedConfusion[truth][truth] += 1.0;
            }
            if (!truthString.equals(classificationString)) {
                errors++;
                
                // do hierarchical discounting of confusions if necessary
                if(this.usingAHierachy) {
                    ArrayList trueHierachies = (ArrayList)this.hierarchies.clone();
                    ArrayList trueKeys = (ArrayList)this.hierachiesKey.clone();
                    
                    double highestDiscountScore = 0.0;
                    
                    int trueIndex = trueKeys.indexOf(truthString);
                    while(trueIndex != -1)
                    {
                        double discountScore = 0.0;
                        ArrayList classifiedHierachies = (ArrayList)this.hierarchies.clone();
                        ArrayList classifiedKeys = (ArrayList)this.hierachiesKey.clone();
                        int classifiedIndex = classifiedKeys.indexOf(classificationString);
                    
                        trueKeys.remove(trueIndex);
                        String[] tempTrue = (String[])trueHierachies.remove(trueIndex);
                        ArrayList<String> truePath = new ArrayList<String>();
                        for(int i=0;i<tempTrue.length;i++) {
                            truePath.add(tempTrue[i]);
                        }
                        while(classifiedIndex != -1)
                        {
                            classifiedKeys.remove(classifiedIndex);
                            String[] tempClassification = (String[])classifiedHierachies.remove(classifiedIndex);
                            ArrayList<String> classifiedPath = new ArrayList<String>();
                            for(int i=0;i<tempClassification.length;i++) {
                                classifiedPath.add(tempClassification[i]);
                            }
                            for (int i=0;i<classifiedPath.size();i++) {
                                if (truePath.indexOf(classifiedPath.get(i)) != - 1) {
                                    discountScore += 1.0 / ((double)truePath.size());
                                }
                            }
                            
                            if (discountScore > highestDiscountScore){
                                highestDiscountScore = discountScore;
                            }
                            classifiedIndex = classifiedKeys.indexOf(classificationString);                    
                        }
                        trueIndex = trueKeys.indexOf(truthString);
                    }
                
                    discountedConfusion[truth][truth] += highestDiscountScore;
                }
            } else {
                mcnemars[x] = 1;
                //totalScore = totalScore + 1.0;
            }
        }
        //int theSignalsLength = theSignals.length;
        
        
        //Store confusion matrices
        if (this.storedConfusion == null) {
            this.storedConfusion = new int[classNames.size()][classNames.size()];
        }
        for(int x=0; x<classNames.size(); x++) {
            for(int y=0; y<classNames.size(); y++) {
                storedConfusion[x][y] += confusion[x][y];
            }
        }
        
        //If necessary, store discounted confusion mmatrices
        if(this.usingAHierachy) {
            if (this.storedDiscountedConfusion == null) {
                this.storedDiscountedConfusion = new double[classNames.size()][classNames.size()];
            }
            for(int x=0; x<classNames.size(); x++) {
                for(int y=0; y<classNames.size(); y++) {
                    storedDiscountedConfusion[x][y] += discountedConfusion[x][y];
                }
            }
        }
        
        //calculate percentage confusion matrix and, if necessary, discounted confusion matrix for this iteration
        double[][] percentConfusion = new double[classNames.size()][classNames.size()];
        double[][] percentDiscountedConfusion = null;
        if (this.usingAHierachy) {
            percentDiscountedConfusion = new double[classNames.size()][classNames.size()];
        }
        for(int y=0; y<classNames.size(); y++) {
            int tot = 0;
            for(int x=0; x<classNames.size(); x++) {
                tot += confusion[x][y];
            }
            if(verbose) {
                System.out.println("Total num examples for class: " + classNames.get(y) + " for this iteration is = " + tot);
            }
            for(int x=0; x<classNames.size(); x++) {
                if (tot > 0) {
                    percentConfusion[x][y] = (double)confusion[x][y] / (double)tot;
                    if(this.usingAHierachy) {
                        percentDiscountedConfusion[x][y] = discountedConfusion[x][y] / (double)tot;
                    }
                } else {
                    percentConfusion[x][y] = 0.0;
                    if(this.usingAHierachy) {
                        percentDiscountedConfusion[x][y] = 0.0;
                    }
                }
            }
        }
        
        if (this.StoredAccuracies == null)
        {
            this.StoredAccuracies = new ArrayList<Double>();
            this.StoredDiscountedAccuracies = new ArrayList<Double>();
            this.StoredNormalisedAccuracies = new ArrayList<Double>();
            this.StoredNormalisedDiscountedAccuracies = new ArrayList<Double>();
        }
        
        //Calculate accuracy as diagonal sum of confusion matrix divided by total number of examples
        double Accuracy = 0.0;//(double)(theSignalsLength - errors) / (double)(theSignalsLength);
        double DiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            Accuracy += confusion[i][i];
        }
        Accuracy /= (double)theSignals.length;
        this.StoredAccuracies.add(new Double(Accuracy));
        
        if (this.usingAHierachy) {
            //Calculate accuracy as diagonal sum of discounted confusion matrix divided by total number of examples
            for (int i=0;i<classNames.size(); i++) {
                DiscountedAccuracy += discountedConfusion[i][i];
            }
            DiscountedAccuracy /= (double)theSignals.length;
            this.StoredDiscountedAccuracies.add(new Double(DiscountedAccuracy));
        }
        
        //Calculate Normalised accuracy as mean of percentage confusion matrix diagonal
        double NormalisedAccuracy = 0.0;
        double NormalisedDiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            NormalisedAccuracy += percentConfusion[i][i];
        }
        NormalisedAccuracy /= classNames.size();
        this.StoredNormalisedAccuracies.add(new Double(NormalisedAccuracy));
        
        if (this.usingAHierachy) {
            //Calculate Normalised accuracy as mean of percentage discounted confusion matrix diagonal
            for (int i=0;i<classNames.size(); i++) {
                NormalisedDiscountedAccuracy += percentDiscountedConfusion[i][i];
            }
            NormalisedDiscountedAccuracy /= (double)classNames.size();
            this.StoredNormalisedDiscountedAccuracies.add(new Double(NormalisedDiscountedAccuracy));
        }
        theSignals = null;
//END EVALUATE THIS ITERATION
        
        
        
//WRITE OUTPUT FOR THIS ITERATION
        if (this.indivIterationOutput == null) {
            this.indivIterationOutput = new ArrayList<String>();
        }
        String bufferString = this.littleDivider;
        bufferString += "Iteration " + this.numberOfResultSets + "\n";
        bufferString += "Accuracy: " + dec.format(Accuracy * 100) + "%\n";
        bufferString += "Accuracy (normalised for class sizes): " + dec.format(NormalisedAccuracy * 100) + "%\n";
        
        if(this.usingAHierachy) {
            bufferString += "Hierachical Accuracy: " + dec.format(DiscountedAccuracy * 100) + "%\n";
            bufferString += "Hierachical Accuracy (normalised for class sizes): " + dec.format(NormalisedDiscountedAccuracy * 100) + "%\n";
        }
        
        bufferString += "Raw Confusion Matrix:\n";
        bufferString += writeIntConfusionMatrix(confusion, this.classNames);
        bufferString += "\nConfusion Matrix percentage:\n";
        bufferString += writePercentageConfusionMatrix(percentConfusion, this.classNames);
        bufferString += writeMatrixKey(this.classNames);
            
        if (this.usingAHierachy)
        {
            bufferString += "\nDiscounted Confusion Matrix:\n";
            bufferString += writeDoubleConfusionMatrix(discountedConfusion, this.classNames);
            bufferString += "\nDiscounted Confusion Matrix percentage:\n";
            bufferString += writeDoubleConfusionMatrix(percentDiscountedConfusion, this.classNames);
        }
        bufferString += this.littleDivider;
        this.indivIterationOutput.add(bufferString);
        
//END WRITE OUTPUT FOR THIS ITERATION
        
//WRITE FINAL OUTPUT HEADER
        outBufferString = this.bigDivider;
        outBufferString += "Model name: " + this.getModelName() + "\n";
        outBufferString += "Number of iterations = " + numberOfResultSets + "\n";
        System.out.println("Number of iterations = " + numberOfResultSets + "\n");
        for (int i=0;i<this.numberOfResultSets;i++) {
            outBufferString += this.indivIterationOutput.get(i);
        }
        outBufferString += this.bigDivider;
//END WRITE FINAL OUTPUT HEADER
        
        
//REPEAT EVALUATION OVER ALL ITERATIONS
        //calculate percentage confusion matrix and, if necessary, discounted confusion matrix for this iteration
        double[][] finalPercentConfusion = new double[classNames.size()][classNames.size()];
        double[][] finalPercentDiscountedConfusion = null;
        if (this.usingAHierachy) {
            finalPercentDiscountedConfusion = new double[classNames.size()][classNames.size()];
        }
        for(int y=0; y<classNames.size(); y++) {
            int finalTot = 0;
            for(int x=0; x<classNames.size(); x++) {
                finalTot += this.storedConfusion[x][y];
            }
            if(verbose) {
                System.out.println("Total num examples for class: " + classNames.get(y) + " over all iterations is = " + finalTot);
            }
            for(int x=0; x<classNames.size(); x++) {
                if (finalTot > 0) {
                    finalPercentConfusion[x][y] = (double)this.storedConfusion[x][y] / (double)finalTot;
                    if(this.usingAHierachy) {
                        finalPercentDiscountedConfusion[x][y] = this.storedDiscountedConfusion[x][y] / (double)finalTot;
                    }
                } else {
                    finalPercentConfusion[x][y] = 0.0;
                    if(this.usingAHierachy) {
                        finalPercentDiscountedConfusion[x][y] = 0.0;
                    }
                }
            }
        }
        
        //Calculate final accuracy as diagonal sum of confusion matrix divided by total number of examples
        double finalAccuracy = 0.0;//(double)(theSignalsLength - errors) / (double)(theSignalsLength);
        double finalDiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            finalAccuracy += storedConfusion[i][i];
        }
        finalAccuracy /= (double)this.totalNumberOfExamples;
        
        if (this.usingAHierachy) {
            //Calculate accuracy as diagonal sum of discounted confusion matrix divided by total number of examples
            for (int i=0;i<classNames.size(); i++) {
                finalDiscountedAccuracy += this.storedDiscountedConfusion[i][i];
            }
            finalDiscountedAccuracy /= (double)this.totalNumberOfExamples;
        }
        
        //Calculate Normalised accuracy as mean of percentage confusion matrix diagonal
        double finalNormalisedAccuracy = 0.0;
        double finalNormalisedDiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            finalNormalisedAccuracy += finalPercentConfusion[i][i];
        }
        finalNormalisedAccuracy /= classNames.size();
        
        if (this.usingAHierachy) {
            //Calculate Normalised accuracy as mean of percentage discounted confusion matrix diagonal
            for (int i=0;i<classNames.size(); i++) {
                finalNormalisedDiscountedAccuracy += finalPercentDiscountedConfusion[i][i];
            }
            finalNormalisedDiscountedAccuracy /= (double)classNames.size();
        }
        
        double stdDeviationAccuracy = 0.0;
        double stdDeviationDiscountedAccuracy = 0.0;
        double stdDeviationNormalisedAccuracy = 0.0;
        double stdDeviationNormalisedDiscountedAccuracy = 0.0;
        
        for (int i=0; i< numberOfResultSets;i++) {
            double varianceAccuracy = 0.0;
            double varianceDiscountedAccuracy = 0.0;
            double varianceNormalisedAccuracy = 0.0;
            double varianceNormalisedDiscountedAccuracy = 0.0;
            
            varianceAccuracy = (this.StoredAccuracies.get(i)).doubleValue() - finalAccuracy;
            varianceNormalisedAccuracy = (this.StoredNormalisedAccuracies.get(i)).doubleValue() - finalNormalisedAccuracy;
                
            if(this.usingAHierachy) {
                varianceDiscountedAccuracy = (this.StoredDiscountedAccuracies.get(i)).doubleValue() - finalDiscountedAccuracy;
                varianceNormalisedDiscountedAccuracy = (this.StoredNormalisedDiscountedAccuracies.get(i)).doubleValue() - finalNormalisedDiscountedAccuracy;
            }
            
            stdDeviationAccuracy += (varianceAccuracy * varianceAccuracy);
            stdDeviationNormalisedAccuracy += (varianceNormalisedAccuracy * varianceNormalisedAccuracy);
                
            if(this.usingAHierachy) {
                stdDeviationDiscountedAccuracy += (varianceDiscountedAccuracy * varianceDiscountedAccuracy);
                stdDeviationNormalisedDiscountedAccuracy += (varianceNormalisedDiscountedAccuracy * varianceNormalisedDiscountedAccuracy);
            }
        }
        
        stdDeviationAccuracy = Math.sqrt(stdDeviationAccuracy);
        stdDeviationAccuracy /= (double)numberOfResultSets;
        stdDeviationNormalisedAccuracy = Math.sqrt(stdDeviationNormalisedAccuracy);
        stdDeviationNormalisedAccuracy /= (double)numberOfResultSets;
            
        if(this.usingAHierachy) {
            stdDeviationDiscountedAccuracy = Math.sqrt(stdDeviationDiscountedAccuracy);
            stdDeviationDiscountedAccuracy /= (double)numberOfResultSets;
            stdDeviationNormalisedDiscountedAccuracy = Math.sqrt(stdDeviationNormalisedDiscountedAccuracy);
            stdDeviationNormalisedDiscountedAccuracy /= (double)numberOfResultSets;
        }
//END REPEAT EVALUATION OVER ALL ITERATIONS
        
//WRITE OUTPUT FOR ALL ITERATIONS
        outBufferString += "Overall Evaluation for "  + this.getModelName() + "\n" + this.littleDivider;
        
        outBufferString += this.littleDivider;
        outBufferString += "Overall Accuracy " + this.getModelName() + ": " 
            + dec.format(finalAccuracy * 100) + "% Standard Deviation: " 
            + dec.format(stdDeviationAccuracy * 100) + "\n";
        outBufferString += "Overall Accuracy (normalised for class sizes) " + this.getModelName() + ": "
            + dec.format(finalNormalisedAccuracy * 100) + "% Standard Deviation: " 
            + dec.format(stdDeviationNormalisedAccuracy * 100) + "\n";
        
        if(this.usingAHierachy) {
            outBufferString += "Hierachical Accuracy: " + dec.format(finalDiscountedAccuracy * 100) 
                + "% Standard Deviation: " + dec.format(stdDeviationDiscountedAccuracy * 100) + "\n";
            outBufferString += "Hierachical Accuracy (normalised for class sizes): " 
                + finalNormalisedDiscountedAccuracy +"% Standard Deviation: " 
                    + dec.format(stdDeviationNormalisedDiscountedAccuracy * 100) + "\n";
        }
        
        outBufferString += "Raw Confusion Matrix:\n";
        outBufferString += writeIntConfusionMatrix(this.storedConfusion, this.classNames);
        outBufferString += "\nConfusion Matrix percentage:\n";
        outBufferString += writePercentageConfusionMatrix(finalPercentConfusion, this.classNames);
        outBufferString += writeMatrixKey(this.classNames);
            
        if(this.usingAHierachy) {
            outBufferString += "\nDiscounted Confusion Matrix:\n";
            outBufferString += writeDoubleConfusionMatrix(this.storedDiscountedConfusion, this.classNames);
            outBufferString += "\nDiscounted Confusion Matrix percentage:\n";
            outBufferString += writeDoubleConfusionMatrix(finalPercentDiscountedConfusion, this.classNames);
        }
        outBufferString += this.littleDivider;
//END WRITE OUTPUT FOR ALL ITERATIONS
        
        Signal evalSignal = new Signal();
        
        
        evalSignal.setMetadata(Signal.PROP_MCNEMAR, mcnemars);
        
        evalSignal.setMetadata(("Un-normalised accuracy " + this.numberOfResultSets), new Double(finalAccuracy));
        
        evalSignal.setMetadata(("Normalised accuracy " + this.numberOfResultSets), new Double(finalNormalisedAccuracy));
        if (this.usingAHierachy) {
            evalSignal.setMetadata(("Normalised hierachical accuracy " + this.numberOfResultSets), new Double(finalDiscountedAccuracy));
            evalSignal.setMetadata(("Un-normalised hierachical accuracy " + this.numberOfResultSets), new Double(finalNormalisedDiscountedAccuracy));
        }
        
        if (!this.usingAHierachy) {
            if(this.useNormalisedEvalMetricForRanking) {
                evalSignal.setMetadata(Signal.PROP_PERF, new Double(NormalisedAccuracy));
            } else {
                evalSignal.setMetadata(Signal.PROP_PERF, new Double(Accuracy));
            }



        } else {
            if(this.useNormalisedEvalMetricForRanking) {
                evalSignal.setMetadata(Signal.PROP_PERF, new Double(NormalisedDiscountedAccuracy));
            } else {
                evalSignal.setMetadata(Signal.PROP_PERF, new Double(DiscountedAccuracy));
            }
        }
        
        evalSignal.setMetadata(Signal.PROP_PERF_ACC, new Double(Accuracy));
        evalSignal.setMetadata(Signal.PROP_PERF_NORM_ACC, new Double(NormalisedAccuracy));
        if (this.usingAHierachy) {
            evalSignal.setMetadata(Signal.PROP_PERF_DISCOUNTED_ACC, new Double(DiscountedAccuracy));
            evalSignal.setMetadata(Signal.PROP_PERF_NORM_DISCOUNTED_ACC, new Double(NormalisedDiscountedAccuracy));
        }

        evalSignal.setMetadata(Signal.PROP_ALG_NAME, this.getModelName());
        
        evalSignal.setMetadata(Signal.PROP_CLASSES, classNames.toArray(new String[classNames.size()]));




        double[] perClassAccuracy = new double[classNames.size()];
        for (int i = 0; i < perClassAccuracy.length; i++) {
            perClassAccuracy[i] = percentConfusion[i][i];
            
        }
        evalSignal.setMetadata(Signal.PROP_PERF_PER_CLASS, perClassAccuracy);
        evalSignal.setMetadata(Signal.PROP_PERF_ACC_PER_CLASS, perClassAccuracy);

        if (usingAHierachy){
            double[] perClassDiscountedAccuracy = new double[classNames.size()];
            for (int i = 0; i < perClassDiscountedAccuracy.length; i++) {
                perClassDiscountedAccuracy[i] = percentDiscountedConfusion[i][i];

            }
            evalSignal.setMetadata(Signal.PROP_PERF_PER_CLASS, perClassDiscountedAccuracy);
            evalSignal.setMetadata(Signal.PROP_PERF_DISCOUNTED_ACC_PER_CLASS, perClassDiscountedAccuracy);
        }




        
        if (resultSignals == null) {
            resultSignals = new ArrayList<Signal>();
        }
        resultSignals.add(evalSignal);
        
        Signal[] resultSignalArray = new Signal[resultSignals.size()];
        
        for (int m = 0; m < resultSignals.size(); m++) {
            resultSignalArray[m] = resultSignals.get(m);
        }
        
        if(verbose) {
            System.out.println(outBufferString);
        }
        
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(resultFile,false)); //rewrite the file
            output.write(outBufferString);
        } finally {
            //flush and close both "output" and its underlying FileWriter
            if (output != null){
                output.flush();
                output.close();
            }
        }
        File copyToDirFile = new File(storageDirectory);
        if (!copyToDirFile.isDirectory()) {
            if (!copyToDirFile.mkdirs()) {
                throw new RuntimeException("Could not create the output folder.");
            }
        }
        
        //output signal array
        File theDir = new File(this.storageDirectory + File.separator + "evalData");
        if (!theDir.isDirectory()) {
            if (!theDir.mkdirs()) {
                throw new RuntimeException("Could not create the output folder.");
            }
        }
        for (int i=0;i<resultSignalArray.length;i++)
        {
            File theFile = new File(theDir.getAbsolutePath() + File.separator + this.getModelName() + this.signalFileExt + "." + i); //rewrite the file
            try {
                resultSignalArray[i].setMetadata(Signal.PROP_ALG_NAME, this.getModelName());
                resultSignalArray[i].write(theFile);
                
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        reportFile = resultFile;
        
        
        return resultSignalArray;
    }

    public void initHierachy(String inFile) {
        //Initialise Hierachy scoring stuff
        System.out.println("reading hierarchy file: " + inFile);
        this.hierarchies = new ArrayList<String[]>();
        this.hierachiesKey = new ArrayList<String>();
        File hierarchyFile = new File(inFile);
        BufferedReader textBuffer = null;
        String[] dataLine = {"init1", "init2"};
        try {
            //use buffering
            //this implementation reads one line at a time
            textBuffer = new BufferedReader( new FileReader(hierarchyFile) );
            String line = null; //not declared within while loop
            while (( line = textBuffer.readLine()) != null) {
                line = line.trim();
                if(!line.equals("")){
                    dataLine = line.split("\t");
                    for (int i = 0; i < dataLine.length; i++){
                        dataLine[i] = TagClassificationGroundTruthFileReader.cleanTag(dataLine[i]);
                    }
                    this.hierarchies.add(dataLine);
                    this.hierachiesKey.add(dataLine[0]);
                    System.out.print("Adding");
                    for (int i = 0; i < dataLine.length; i++){
                        System.out.print("\t" + dataLine[i]);
                    }
                    System.out.println("");
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (textBuffer!= null) {
                    //flush and close both "input" and its underlying FileReader
                    textBuffer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Writes an integer confusion matrix to a file.
     * @param matrix The matrix to be written.
     * @param classNames The class names.
     * @return
     */
    public String writeIntConfusionMatrix(int[][] matrix, ArrayList classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        bufferString += "\nClassification\n";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t\t";
            for(int y=0; y<classNames.size(); y++) {
                bufferString += fmtInt(matrix[x][y]) + "\t";
            }
            bufferString += "\n";
        }
        return bufferString;
    }
    
    /**
     * Writes a double confusion matrix to a file.
     * @param matrix The matrix to be written.
     * @param classNames The class names.
     * @return
     */
    public String writeDoubleConfusionMatrix(double[][] matrix, ArrayList classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        bufferString += "\nClassification\n";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t\t";
            for(int y=0; y<classNames.size(); y++) {
                bufferString += fmtDec(matrix[x][y]) + "\t";
            }
            bufferString += "\n";
        }
        return bufferString;
    }
    
    /**
     * Writes a double confusion matrix to a file.
     * @param matrix The matrix to be written.
     * @param classNames The class names.
     * @return 
     */
    public String writePercentageConfusionMatrix(double[][] matrix, ArrayList classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        bufferString += "\nClassification\n";
        for(int x=0; x<classNames.size(); x++) {
            //bufferString += (String)classNames.get(x) + "\t\t";
            bufferString += getKey(x) + "\t\t";
            for(int y=0; y<classNames.size(); y++) {
                bufferString += fmtPercent(matrix[x][y] * 100.0) + "\t";
            }
            bufferString += "\n";
        }
        return bufferString;
    }


    /** 
     * Outputs the confusion matrix key
     * @param classNames
     * @return the key
     */
    public String writeMatrixKey(ArrayList classNames) {
        StringBuffer sb = new StringBuffer();
        sb.append("Matrix Key:\n");
        for(int x=0; x<classNames.size(); x++) {
            sb.append("   ");
            sb.append(getKey(x));
            sb.append(": "); 
            sb.append((String)classNames.get(x));
            sb.append("\n");
        }
        return sb.toString();
    }
    
        /** 
     *  Returns a two character key for a classname based upon
     * its index
     * @param keyIndex  the class name index
     * @return  a two character key
     */
    private String getKey(int keyIndex) {
        StringBuffer label = new StringBuffer(); 
        if (keyIndex >= 26) {
            label.append((char) ('A' + (keyIndex / 26) - 1));
            keyIndex = keyIndex % 26;
        } else {
            label.append(' ');
        }
        label.append((char) ('A' + keyIndex));
        return pad(label.toString() + "  ", COL_WIDTH);
    }

    /** 
     * Format a decimal number for column output
     * @param val the value to format
     * @return the formatted value
     */
    private String fmtDec(double val) {
        return pad(dec.format(val), COL_WIDTH);
    }

    /** 
     * Format an int for column output
     * @param val the value to format
     * @return the formatted output
     */
    private String fmtInt(int val) {
        return pad(Integer.toString(val), COL_WIDTH);
    }

    /** 
     * Format a percentage value for output
     * @param val the value to format
     * @return the formatted value
     */
    private String fmtPercent(double val) {
        return pad(dec.format(val) + "%", COL_WIDTH);
    }

    /** 
     *  Pad the given string to the given length
     * @param v  the string to pad
     * @param padLength  the length to pad
     * @return the padded string
     */
    private String pad(String v, int padLength) {
        String paddedString = "                                                    " + v;
        return paddedString.substring(
            paddedString.length() - padLength);
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public

    File getReportFile() {
        return reportFile;
    }

    
}
