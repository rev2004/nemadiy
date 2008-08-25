/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.m2k.evaluation2.TagClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.evaluation2.EvaluationDataObject;
import org.imirsel.m2k.evaluation2.Evaluator;
import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.util.MatlabCommandlineIntegrationClass;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kris
 */
public class TagClassificationBinaryEvaluator implements Evaluator {

    private boolean verbose = false;
    private boolean performMatlabStatSigTests = true;
    private String matlabPath = "matlab";

    private static final int FMEASURE_FILE_INDEX = 0;
    private static final int ACCURACY_FILE_INDEX = 1;
    
    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose_) {
        verbose = verbose_;
    }

    public boolean returnsInCSV() {
        return true;
    }
    
    

    public String evaluateResultsAgainstGT(String systemName, EvaluationDataObject dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException {
        //init report
        String systemReport = "System name:       " + systemName + "\n" +
                              "Results file:      " + dataToEvaluate.getFile().getAbsolutePath() + "\n";
        systemReport =        "Ground-truth file: " + groundTruth.getFile().getAbsolutePath() + "\n";

        //check GT and eval data for existence of right data 
        if (!groundTruth.hasMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP)) {
            throw new noMetadataException("No " + EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP + " ground-truth metadata found in object, representing " + groundTruth.getFile().getAbsolutePath());
        }
        if (!dataToEvaluate.hasMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP)) {
            File theFile = dataToEvaluate.getFile();
            throw new RuntimeException("No " + EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP + " evaluation metadata found in object representing " + theFile.getAbsolutePath());
        }
        //get the data to compare
        HashMap<String, HashSet<String>> binaryTagData = (HashMap<String, HashSet<String>>) dataToEvaluate.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        HashMap<String, HashSet<String>> GT_binaryTagData = (HashMap<String, HashSet<String>>) groundTruth.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        if (!GT_binaryTagData.keySet().containsAll(binaryTagData.keySet())) {
            HashSet<String> missingData = new HashSet<String>();
            missingData.addAll(binaryTagData.keySet());
            missingData.removeAll(GT_binaryTagData.keySet());
            String missingPaths = "";
            for (Iterator<String> it = missingData.iterator(); it.hasNext();) {
                missingPaths += it.next() + ", ";                
            }
            throw new RuntimeException("The groundtruth Object representing " + groundTruth.getFile().getAbsolutePath() + ", does not have ground truth" +
                    "for all the paths specified in " + dataToEvaluate.getFile().getAbsolutePath() + ". Paths without data: " + missingPaths);
        }
        
        if(verbose){
            System.out.println("Evaluating: " + dataToEvaluate.getFile().getAbsolutePath());
        }
        
        //util references
        String path, tag;
        HashSet<String> trueSet;
        HashSet<String> returnedSet;
        HashSet<String> remainder;
        
        HashMap<String, AtomicInteger> tag2truePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> tag2falsePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> tag2falseNegative = new HashMap<String, AtomicInteger>();
        HashMap<String, Integer> tag2numPositiveExamples = new HashMap<String, Integer>();
        HashMap<String, Integer> tag2numNegativeExamples = new HashMap<String, Integer>();
        
        int totalTruePositive = 0;
        int totalFalsePositive = 0;
        int totalFalseNegative = 0;
        int totalTrueNegative = 0;

        //result objects
        HashMap<String, Double> tag2Accuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2Precision = new HashMap<String, Double>();
        HashMap<String, Double> tag2Recall = new HashMap<String, Double>();
        HashMap<String, Double> tag2FMeasure = new HashMap<String, Double>();
        double totalAccuracy;
        double totalPrecision;
        double totalRecall;
        double totalFmeasure;

        //step through all paths in the results
        for (Iterator<String> pathIter = binaryTagData.keySet().iterator(); pathIter.hasNext();) {
            path = pathIter.next();
            returnedSet = binaryTagData.get(path);
            trueSet = GT_binaryTagData.get(path);

            for (Iterator<String> it = returnedSet.iterator(); it.hasNext();) {
                tag = it.next();
                if (trueSet.contains(tag)) {
                    totalTruePositive++;
                    incrementTagCount(tag2truePositive, tag);
                } else {
                    totalFalsePositive++;
                    incrementTagCount(tag2falsePositive, tag);
                }
            }
            remainder = (HashSet<String>) trueSet.clone();
            remainder.removeAll(returnedSet);
            for (Iterator<String> it = remainder.iterator(); it.hasNext();) {
                tag = it.next();
                totalFalseNegative++;
                incrementTagCount(tag2falseNegative, tag);
            }
        }

        //get full set of tags
        HashSet<String> tags = new HashSet<String>(tag2truePositive.keySet());
        tags.addAll(tag2falsePositive.keySet());
        tags.addAll(tag2falseNegative.keySet());

        if(verbose){
            System.out.println("\tcomputing per-tag statistics");
        }
        
        //compute per tag stats
        for (Iterator<String> it = tags.iterator(); it.hasNext();) {
            tag = it.next();
            //fill in any gaps in maps
            if (!tag2truePositive.containsKey(tag)) {
                tag2truePositive.put(tag, new AtomicInteger(0));
            }
            if (!tag2falsePositive.containsKey(tag)) {
                tag2falsePositive.put(tag, new AtomicInteger(0));
            }
            if (!tag2falseNegative.containsKey(tag)) {
                tag2falseNegative.put(tag, new AtomicInteger(0));
            }
            int tp = tag2truePositive.get(tag).intValue();
            int fp = tag2falsePositive.get(tag).intValue();
            int fn = tag2falseNegative.get(tag).intValue();
            int tn = binaryTagData.size() - (tp + fp + fn);
            
            totalTrueNegative += tn;
            
            double accuracy = (double) (tp + tn) / (double) binaryTagData.size();
            double precision = (double) tp / (double) (tp + fp);
            if ((tp + fp)==0){
                precision = 0.0;
            }
            double recall = (double) tp / (double) (tp + fn);
            if ((tp + fn)==0){
                recall = 0.0;
            }
            double fMeasure = (2 * recall * precision) / (recall + precision);
            if ((precision == 0.0)||(recall == 0)){
                fMeasure = 0.0;
            }
            tag2Accuracy.put(tag, accuracy);
            tag2Precision.put(tag, precision);
            tag2Recall.put(tag, recall);
            tag2FMeasure.put(tag, fMeasure);
            
            int numPositivesExamples = tp + fn;
            int numNegativeExamples = tn + fp;
            tag2numPositiveExamples.put(tag,numPositivesExamples);
            tag2numNegativeExamples.put(tag, numNegativeExamples);
        }

        if(verbose){
            System.out.println("\tcomputing total statistics");
        }
        
        //compute total stats
        totalAccuracy = (double) (totalTruePositive + totalTrueNegative) / (double) (totalTruePositive + totalFalsePositive + totalFalseNegative + totalTrueNegative);
        totalPrecision = (double) totalTruePositive / (double) (totalTruePositive + totalFalsePositive);
        totalRecall = (double) totalTruePositive / (double) (totalTruePositive + totalFalseNegative);
        totalFmeasure = (2 * totalRecall * totalPrecision) / (totalRecall + totalPrecision);

        //append results to report
        systemReport += "Overall accuracy: " + totalAccuracy + "\n";
        systemReport += "Overall precision: " + totalPrecision + "\n";
        systemReport += "Overall recall:    " + totalRecall + "\n";
        systemReport += "Overall fMeasure:  " + totalFmeasure + "\n";
        for (Iterator<String> it = tags.iterator(); it.hasNext();) {
            tag = it.next();
            systemReport += "tag '" + tag + "':\n";
            systemReport += "    accuracy: " + tag2Accuracy.get(tag).doubleValue() + "\n";
            systemReport += "    precision: " + tag2Precision.get(tag).doubleValue() + "\n";
            systemReport += "    recall:    " + tag2Recall.get(tag).doubleValue() + "\n";
            systemReport += "    fMeasure:  " + tag2FMeasure.get(tag).doubleValue() + "\n";
        }

        
        if(verbose){
            System.out.println("\toutputting");
        }
        //report on files evaluated against
        systemReport += "Number of files tested against: " + binaryTagData.size() + "\n";
        systemReport += "Test file paths: " + binaryTagData.size() + "\n";
        String[] paths = binaryTagData.keySet().toArray(new String[binaryTagData.size()]);
        Arrays.sort(paths);
        for (int j = 0; j < paths.length; j++) {
            systemReport += "\t" + paths[j] + "\n";
        }
        systemReport += "  ---   \n";

        //store evaluation data
        dataToEvaluate.setMetadata(EvaluationDataObject.SYSTEM_RESULTS_REPORT, systemReport);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_ACCURACY_MAP, tag2Accuracy);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_PRECISION_MAP, tag2Precision);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_RECALL_MAP, tag2Recall);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP, tag2FMeasure);
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_OVERALL_ACCURACY, totalAccuracy);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_OVERALL_PRECISION, totalPrecision);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_OVERALL_RECALL, totalRecall);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_OVERALL_FMEASURE, totalFmeasure);
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES, tag2numPositiveExamples);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES, tag2numNegativeExamples);
        
        //serialise out the evaluation data
        //  later this should be changed to use ASCII file format - when Collections are supported by write method of EvaluationDataObject
        File systemDirectory = new File(outputDir.getAbsolutePath() + File.separator + systemName);
        systemDirectory.mkdir();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(systemDirectory.getAbsolutePath() + File.separator + dataToEvaluate.getFile().getName() + ".evalData.ser");
            ObjectOutputStream objectOutputStream;
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(dataToEvaluate);
            fileOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        //write out report
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(systemDirectory.getAbsolutePath() + File.separator + dataToEvaluate.getFile().getName() + ".report.txt"));
            textOut.write(systemReport);
            textOut.newLine();
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } 

        if(verbose){
            System.out.println("\n\n" + systemReport);
        }
        
        return systemReport;
    }

    public String evaluate(String[] systemNames, EvaluationDataObject[][] dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException{
        String report = "";

        //evaluate each system
        for (int i = 0; i < dataToEvaluate.length; i++) {
            report += "System: " + systemNames[i] + "\n";
            //evaluate each fold
            for (int j = 0; j < dataToEvaluate.length; j++) {
                String systemReport = evaluateResultsAgainstGT(systemNames[i], dataToEvaluate[i][j], groundTruth, outputDir);
                report += systemReport + EvaluationDataObject.DIVIDER + "\n";
            }
            report += EvaluationDataObject.DIVIDER + "\n";
        }
        
        File[] CSVResultFiles = writeOutCSVResultFiles(dataToEvaluate, systemNames, outputDir);
        
        
        //write overall report to file
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(outputDir.getAbsolutePath() + File.separator + "tag_binary_relevance_report.txt"));
            textOut.write(report);
            textOut.newLine();
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        if (performMatlabStatSigTests){
            //call matlab and execute Friedman's test with TK HSD for Fmeasure
            performFriedmanTestWithFMeasure(outputDir, CSVResultFiles, systemNames);
            
            //call matlab and execute Friedman's test with TK HSD for Accuracy
            performFriedmanTestWithAccuracy(outputDir, CSVResultFiles, systemNames);
            
            //  call matlab and execute Beta-Binomial test with F-measure
            performBetaBinomialTestWithFMeasure(outputDir, CSVResultFiles, systemNames);
            
            //  call matlab and execute Beta-Binomial test with accuracy scores
            performBetaBinomialTestWithAccuracy(outputDir, CSVResultFiles, systemNames);
            
        }
        return report;
    }
    
    private void incrementTagCount(HashMap<String, AtomicInteger> map, String tag) {
        if (!map.containsKey(tag)) {
            map.put(tag, new AtomicInteger(1));
        } else {
            map.get(tag).incrementAndGet();
        }
    }

    public boolean getPerformMatlabStatSigTests() {
        return performMatlabStatSigTests;
    }

    public void setPerformMatlabStatSigTests(boolean performMatlabStatSigTests) {
        this.performMatlabStatSigTests = performMatlabStatSigTests;
    }

    public String getMatlabPath() {
        return matlabPath;
    }

    public void setMatlabPath(String matlabPath) {
        this.matlabPath = matlabPath;
    }

    private void performFriedmanTestWithAccuracy(File outputDir, File[] CSVResultFiles, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForTagsAccuracy";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFiles[ACCURACY_FILE_INDEX].getAbsolutePath() + "', '\t')");
            textOut.newLine();
            textOut.write("Fmeasure_Scores = data(:,5:" + (systemNames.length + 4) + ");");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Fmeasure_Scores,1,'on');");
            textOut.newLine();
            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();

            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(matlabPath);
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();
            //  call matlab and execute Beta-Binomial test
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void performFriedmanTestWithFMeasure(File outputDir, File[] CSVResultFiles, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
        //create an m-file to run the test
        String evalCommand = "performFriedmanForTagsFMeasure";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "binary_FMeasure.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFiles[FMEASURE_FILE_INDEX].getAbsolutePath() + "', '\t')");
            textOut.newLine();
            textOut.write("Fmeasure_Scores = data(:,5:" + (systemNames.length + 4) + ");");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Fmeasure_Scores,1,'on');");
            textOut.newLine();
            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();

            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(matlabPath);
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();

            //  call matlab and execute Beta-Binomial test
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    
    private void performBetaBinomialTestWithFMeasure(File outputDir, File[] CSVResultFiles, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
        File BBFile = new File(outputDir.getAbsolutePath() + File.separator + "betaBinomEB.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/betaBinomEB.m", BBFile);
        File BBreportFile = new File(outputDir.getAbsolutePath() + File.separator + "bbReport.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/bbReport.m", BBreportFile);
        
        //create an m-file to run the test
        String evalCommand = "performBBForTagsFMeasure";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabOutputPath = outputDir.getAbsolutePath() + File.separator + "binary_FMeasure.BetaBinomial";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("bbReport('" +  CSVResultFiles[FMEASURE_FILE_INDEX].getAbsolutePath() + "', '" + matlabOutputPath + "')");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();
            
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(matlabPath);
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();

            //  call matlab and execute Beta-Binomial test
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    private void performBetaBinomialTestWithAccuracy(File outputDir, File[] CSVResultFiles, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
        File BBFile = new File(outputDir.getAbsolutePath() + File.separator + "betaBinomEB.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/betaBinomEB.m", BBFile);
        File BBreportFile = new File(outputDir.getAbsolutePath() + File.separator + "bbReport.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/bbReport.m", BBreportFile);
        
        //create an m-file to run the test
        String evalCommand = "performBBForTagsAccuracy";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabOutputPath = outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.BetaBinomial";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("bbReport('" +  CSVResultFiles[ACCURACY_FILE_INDEX].getAbsolutePath() + "', '" + matlabOutputPath + "')");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();
            
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(matlabPath);
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();

            //  call matlab and execute Beta-Binomial test
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        //delete readtext.m
        readtextMFile.delete();
    }

    private File[] writeOutCSVResultFiles(EvaluationDataObject[][] dataToEvaluate, String[] systemNames, File outputDir) throws noMetadataException {


        //TODO: stat sig tests
        //  prepare per-tag F-measure and accuracy data files for use in Friedmans's test with TK HSD
        int numFolds = dataToEvaluate[0].length;
        int totalNumRows = 0;

        String[][] tagNames = new String[numFolds][];
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> tag2fmeasureMap = (HashMap<String, Double>) dataToEvaluate[0][i].getMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP);
            tagNames[i] = tag2fmeasureMap.keySet().toArray(new String[tag2fmeasureMap.size()]);
            totalNumRows += tagNames[i].length;
        }


        String[][] fMeasureCsvData = new String[totalNumRows + 1][systemNames.length + 4];
        fMeasureCsvData[0][0] = "tag";
        fMeasureCsvData[0][1] = "fold";
        fMeasureCsvData[0][2] = "positive examples";
        fMeasureCsvData[0][3] = "negative examples";

        for (int i = 0; i < systemNames.length; i++) {
            fMeasureCsvData[0][i + 4] = systemNames[i];
        }
        int foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            HashMap<String, Integer> tag2NumPos = (HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES);
            HashMap<String, Integer> tag2NumNeg = (HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES);
            for (int j = 0; j < tagNames[f].length; j++) {
                fMeasureCsvData[foldOffset + j][0] = tagNames[f][j];
                fMeasureCsvData[foldOffset + j][1] = "" + (f + 1);
                fMeasureCsvData[foldOffset + j][2] = "" + tag2NumPos.get(tagNames[f][j]);
                fMeasureCsvData[foldOffset + j][3] = "" + tag2NumNeg.get(tagNames[f][j]);


                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> tag2fmeasureMap = (HashMap<String, Double>) dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP);
                    fMeasureCsvData[foldOffset + j][s + 4] = "" + tag2fmeasureMap.get(tagNames[f][j]).doubleValue();
                }
            }
            foldOffset += tagNames[f].length;
        }
        File FmeasureFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_Fmeasure.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(FmeasureFile, ",", fMeasureCsvData, true);
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[][] accuracyCsvData = new String[totalNumRows + 1][systemNames.length + 4];
        accuracyCsvData[0][0] = "tag";
        accuracyCsvData[0][1] = "fold";
        accuracyCsvData[0][2] = "positive examples";
        accuracyCsvData[0][3] = "negative examples";

        for (int i = 0; i < systemNames.length; i++) {
            accuracyCsvData[0][i + 4] = systemNames[i];
        }
        foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            HashMap<String, Integer> tag2NumPos = (HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES);
            HashMap<String, Integer> tag2NumNeg = (HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES);
            for (int j = 0; j < tagNames[f].length; j++) {
                accuracyCsvData[foldOffset + j][0] = tagNames[f][j];
                accuracyCsvData[foldOffset + j][1] = "" + (f + 1);
                accuracyCsvData[foldOffset + j][2] = "" + tag2NumPos.get(tagNames[f][j]);
                accuracyCsvData[foldOffset + j][3] = "" + tag2NumNeg.get(tagNames[f][j]);


                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> tag2accuracyMap = (HashMap<String, Double>) dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP);
                    accuracyCsvData[foldOffset + j][s + 4] = "" + tag2accuracyMap.get(tagNames[f][j]).doubleValue();
                }
            }
            foldOffset += tagNames[f].length;
        }
        File AccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(AccuracyFile, ",", accuracyCsvData, true);
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  prepare per-tag F-measure and Accuracy for use in Beta-Binomial test
        return new File[]{FmeasureFile,AccuracyFile};
    }

    public static void main(String[] args) {
        String systemName = args[0];
        File resultFile = new File(args[1]);
        File gtFile = new File(args[2]);
        File outputDirectory = new File(args[3]);
        
        TagClassificationBinaryFileReader binReader = new TagClassificationBinaryFileReader();
        TagClassificationBinaryEvaluator evaluator = new TagClassificationBinaryEvaluator();
        evaluator.setVerbose(true);
        evaluator.evaluateResultsAgainstGT(systemName, binReader.readFile(resultFile), binReader.readFile(gtFile), outputDirectory);
        
    }
}
