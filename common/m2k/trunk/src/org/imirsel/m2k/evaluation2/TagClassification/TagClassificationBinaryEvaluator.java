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

    private String evaluateResultsAgainstGT(EvaluationDataObject dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException {
        //init report
        String systemReport = "Results file:      " + dataToEvaluate.getFile().getAbsolutePath() + "\n";
        systemReport = "Ground-truth file: " + groundTruth.getFile().getAbsolutePath() + "\n";

        //the data to compare
        HashMap<String, HashSet<String>> binaryTagData = (HashMap<String, HashSet<String>>) dataToEvaluate.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        HashMap<String, HashSet<String>> GT_binaryTagData = (HashMap<String, HashSet<String>>) groundTruth.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);

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
        HashMap<String, Double> tag2Precision = new HashMap<String, Double>();
        HashMap<String, Double> tag2Recall = new HashMap<String, Double>();
        HashMap<String, Double> tag2FMeasure = new HashMap<String, Double>();
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
            double precision = (double) tp / (double) (tp + fp);
            double recall = (double) tp / (double) (tp + fn);
            double fMeasure = (2 * recall * precision) / (recall + precision);
            tag2Precision.put(tag, precision);
            tag2Recall.put(tag, recall);
            tag2FMeasure.put(tag, fMeasure);
        }

        //compute total stats
        totalPrecision = (double) totalTruePositive / (double) (totalTruePositive + totalFalsePositive);
        totalRecall = (double) totalTruePositive / (double) (totalTruePositive + totalFalseNegative);
        totalFmeasure = (2 * totalRecall * totalPrecision) / (totalRecall + totalPrecision);

        //append results to report
        systemReport += "Overall precision: " + totalPrecision + "\n";
        systemReport += "Overall recall:    " + totalRecall + "\n";
        systemReport += "Overall fMeasure:  " + totalFmeasure + "\n";
        for (Iterator<String> it = tags.iterator(); it.hasNext();) {
            tag = it.next();
            systemReport += "tag '" + tag + "':\n";
            systemReport += "    precision: " + tag2Precision.get(tag).doubleValue() + "\n";
            systemReport += "    recall:    " + tag2Recall.get(tag).doubleValue() + "\n";
            systemReport += "    fMeasure:  " + tag2FMeasure.get(tag).doubleValue() + "\n";
        }

        //store overall results
        tag2Precision.put("OVERALL", totalPrecision);
        tag2Recall.put("OVERALL", totalRecall);
        tag2FMeasure.put("OVERALL", totalFmeasure);

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
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_PRECISION_MAP, tag2Precision);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_RECALL_MAP, tag2Recall);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_BINARY_FMEASURE_MAP, tag2FMeasure);

        //serialise out the evaluation data
        //  later this should be changed to use ASCII file format - when Collections are supported by write method of EvaluationDataObject
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputDir.getAbsolutePath() + File.separator + dataToEvaluate.getFile().getName() + ".evalData.ser");
            ObjectOutputStream objectOutputStream;
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(dataToEvaluate);
            fileOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        //write out report
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(outputDir.getAbsolutePath() + File.separator + dataToEvaluate.getFile().getName() + ".report.txt"));
            textOut.write(systemReport);
            textOut.newLine();
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return systemReport;
    }

    private void incrementTagCount(HashMap<String, AtomicInteger> map, String tag) {
        if (!map.containsKey(tag)) {
            map.put(tag, new AtomicInteger(1));
        } else {
            map.get(tag).incrementAndGet();
        }
    }

    public String evaluate(EvaluationDataObject[] dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException{
        String report = "";

        //check GT and eval data for existence of right data 
        if (!groundTruth.hasMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP)) {
            throw new noMetadataException("No " + EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP + " ground-truth metadata found in object, representing " + groundTruth.getFile().getAbsolutePath());
        }
        HashMap<String, HashSet<String>> GT_binaryTagData = (HashMap<String, HashSet<String>>) groundTruth.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        for (int i = 0; i < dataToEvaluate.length; i++) {
            if (!dataToEvaluate[i].hasMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP)) {
                File theFile = dataToEvaluate[i].getFile();
                throw new RuntimeException("No " + EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP + " evaluation metadata found in object " + i + ", representing " + theFile.getAbsolutePath());
            }
            HashMap<String, HashSet<String>> binaryTagData = (HashMap<String, HashSet<String>>) dataToEvaluate[i].getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
            if (!GT_binaryTagData.keySet().containsAll(binaryTagData.keySet())) {
                throw new RuntimeException("The groundtruth Object, representing " + groundTruth.getFile().getAbsolutePath() + ", does not have ground truth" +
                        "for all the paths specified in " + dataToEvaluate[i].getFile().getAbsolutePath());
            }

        }

        //evaluate each dataToEvaluate entry
        for (int i = 0; i < dataToEvaluate.length; i++) {
            String systemReport = evaluateResultsAgainstGT(dataToEvaluate[i], groundTruth, outputDir);
            report += systemReport + EvaluationDataObject.DIVIDER + "\n";
        }


    
    //TODO: stat sig tests
    //over all objects
    //  prepare per-tag F-measure and accuracy for use in Friedmans's test with TK HSD
    //  prepare per-tag F-measure and accuracy for use in Beta-Binomial test

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
}
