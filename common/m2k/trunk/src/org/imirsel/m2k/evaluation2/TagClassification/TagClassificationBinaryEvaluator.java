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
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kris
 */
public class TagClassificationBinaryEvaluator implements Evaluator {

    private boolean verbose = true;

    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose_) {
        verbose = verbose_;
    }

    public boolean returnsInCSV() {
        return false;
    }

    public String evaluateResultsAgainstGT(String systemName, EvaluationDataObject dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException {
        //init report
        String systemReport = "Results file:      " + dataToEvaluate.getFile().getAbsolutePath() + "\n";
        systemReport = "Ground-truth file: " + groundTruth.getFile().getAbsolutePath() + "\n";

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
            throw new RuntimeException("The groundtruth Object representing " + groundTruth.getFile().getAbsolutePath() + ", does not have ground truth" +
                    "for all the paths specified in " + dataToEvaluate.getFile().getAbsolutePath());
        }
        
        //util references
        String path, tag;
        HashSet<String> trueSet;
        HashSet<String> returnedSet;
        HashSet<String> remainder;
        
        HashMap<String, AtomicInteger> tag2truePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> tag2falsePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> tag2falseNegative = new HashMap<String, AtomicInteger>();

        int totalTruePositive = 0;
        int totalFalsePositive = 0;
        int totalFalseNegative = 0;

        //result objects
        HashMap<String, Double> tag2Accuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2Precision = new HashMap<String, Double>();
        HashMap<String, Double> tag2Recall = new HashMap<String, Double>();
        HashMap<String, Double> tag2FMeasure = new HashMap<String, Double>();
        double totalAccuracy;
        double totalPrecision;
        double totalRecall;
        double totalFmeasure;

        //step through all paths in teh results
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
            double accuracy = (double) tp / (double) (tp + fp + fn);
            double precision = (double) tp / (double) (tp + fp);
            double recall = (double) tp / (double) (tp + fn);
            double fMeasure = (2 * recall * precision) / (recall + precision);
            tag2Accuracy.put(tag, accuracy);
            tag2Precision.put(tag, precision);
            tag2Recall.put(tag, recall);
            tag2FMeasure.put(tag, fMeasure);
        }

        //compute total stats
        totalAccuracy = (double) totalTruePositive / (double) (totalTruePositive + totalFalsePositive + totalFalseNegative);
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
            systemReport += "    precision: " + tag2Accuracy.get(tag).doubleValue() + "\n";
            systemReport += "    precision: " + tag2Precision.get(tag).doubleValue() + "\n";
            systemReport += "    recall:    " + tag2Recall.get(tag).doubleValue() + "\n";
            systemReport += "    fMeasure:  " + tag2FMeasure.get(tag).doubleValue() + "\n";
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

        
        //TODO: stat sig tests
        //  prepare per-tag F-measure and precison data file for use in Friedmans's test with TK HSD
        int numFolds = dataToEvaluate[0].length;
        int totalNumRows = 0;
        
        String[][] tagNames = new String[numFolds][];
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> tag2fmeasureMap =  (HashMap<String, Double>)dataToEvaluate[0][i].getMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP);
            tagNames[i] = tag2fmeasureMap.keySet().toArray(new String[tag2fmeasureMap.size()]);
            totalNumRows += tagNames[i].length;
        }
        
        String[][] csvData = new String[totalNumRows+1][systemNames.length + 2];    
        csvData[0][0] = "tag";
        csvData[0][1] = "fold";
        
        for (int i = 0; i < systemNames.length; i++) {
            csvData[0][i+2] = systemNames[i];
        }
        int foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            for (int j = 0; j < tagNames[f].length; j++) {
                csvData[foldOffset + j][0] = tagNames[f][j];
                csvData[foldOffset + j][1] = "" + (f + 1);
                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> tag2fmeasureMap =  (HashMap<String, Double>)dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP);
                    csvData[foldOffset + j][s+2] = "" + tag2fmeasureMap.get(tagNames[f][j]).doubleValue();
                }
            }
            foldOffset += tagNames[f].length;
        }
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(new File(outputDir.getAbsolutePath() + File.separator + "friedman.csv"), ",", csvData, true); 
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //  prepare per-tag F-measure and precision for use in Beta-Binomial test
        
        //  call matlab and execute Friedman's test with TK HSD
        //    write results to file
        //  call matlab and execute Beta-Binomial test
        //    write results to file

        
        //write overall report to file
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(outputDir.getAbsolutePath() + File.separator + "overall_report.txt"));
            textOut.write(report);
            textOut.newLine();
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
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

    
}
