/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.m2k.evaluation.tagsClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.m2k.evaluation.DataObj;
import org.imirsel.m2k.evaluation.Evaluator;
import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.util.MatlabCommandlineIntegrationClass;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kris
 */
public class TagClassificationBinaryEvaluator /*implements Evaluator*/ {
	
    private boolean verbose = false;
    private boolean performMatlabStatSigTests = true;
    private String matlabPath = "matlab";

    private static final int FMEASURE_FILE_INDEX = 0;
    private static final int ACCURACY_FILE_INDEX = 1;
    private static final int ACCURACY_PER_FOLD_FILE_INDEX = 5;
    private static final int FMEASURE_PER_TRACK_FILE_INDEX = 8;
    
    private HashSet<String> query_subset = null;
    
    public HashSet<String> getQuery_subset(){
        return query_subset;
    }

    public void setQuery_subset(HashSet<String> query_subset){
        this.query_subset = query_subset;
    }

    public boolean returnsInCSV() {
        return true;
    }
    
    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose_) {
        verbose = verbose_;
    }
    
    public String evaluateResultsAgainstGT(String systemName, DataObj dataToEvaluate, DataObj groundTruth, File outputDir) throws noMetadataException {
        //init report
        String systemReport = "-----------------------------------------------------------\n" +
                              "System name:        " + systemName + "\n" +
                              "Results file:       " + dataToEvaluate.getFile().getAbsolutePath() + "\n" + 
                              "Ground-truth file:  " + groundTruth.getFile().getAbsolutePath() + "\n";

        if (getQuery_subset() != null){
            systemReport += "Result subset size: " + getQuery_subset().size() + "\n";
        }
        //check GT and eval data for existence of right data 
        if (!groundTruth.hasMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP)) {
            throw new noMetadataException("No " + DataObj.TAG_BINARY_RELEVANCE_MAP + " ground-truth metadata found in object, representing " + groundTruth.getFile().getAbsolutePath());
        }
        if (!dataToEvaluate.hasMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP)) {
            File theFile = dataToEvaluate.getFile();
            throw new RuntimeException("No " + DataObj.TAG_BINARY_RELEVANCE_MAP + " evaluation metadata found in object representing " + theFile.getAbsolutePath());
        }
        //get the data to compare
        HashMap<String, HashSet<String>> binaryTagData = (HashMap<String, HashSet<String>>) dataToEvaluate.getMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP);
        HashMap<String, HashSet<String>> GT_binaryTagData = (HashMap<String, HashSet<String>>) groundTruth.getMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP);
        HashSet<String> tagSet = (HashSet<String>)groundTruth.getMetadata(DataObj.TAG_NAME_SET);
        
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
            System.err.println("Evaluating: " + dataToEvaluate.getFile().getAbsolutePath());
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
        
        HashMap<String, AtomicInteger> track2truePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> track2falsePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> track2falseNegative = new HashMap<String, AtomicInteger>();
        HashMap<String, Integer> track2numPositiveExamples = new HashMap<String, Integer>();
        HashMap<String, Integer> track2numNegativeExamples = new HashMap<String, Integer>();
        
        
        int totalTruePositive = 0;
        int totalFalsePositive = 0;
        int totalFalseNegative = 0;
        int totalTrueNegative = 0;

        //result objects
        HashMap<String, Double> tag2Accuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2PosAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2NegAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2Precision = new HashMap<String, Double>();
        HashMap<String, Double> tag2Recall = new HashMap<String, Double>();
        HashMap<String, Double> tag2FMeasure = new HashMap<String, Double>();
        
        HashMap<String, Double> track2Accuracy = new HashMap<String, Double>();
        HashMap<String, Double> track2PosAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> track2NegAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> track2Precision = new HashMap<String, Double>();
        HashMap<String, Double> track2Recall = new HashMap<String, Double>();
        HashMap<String, Double> track2FMeasure = new HashMap<String, Double>();
        
        double totalAccuracy;
        double totalPosAccuracy;
        double totalNegAccuracy;
        double totalPrecision;
        double totalRecall;
        double totalFmeasure;

        HashSet<String> inUse = new HashSet<String>(binaryTagData.keySet());
        if (getQuery_subset() != null){
            inUse.retainAll(getQuery_subset());
        }
        
        
        //step through all paths in the results
        for (Iterator<String> pathIter = inUse.iterator(); pathIter.hasNext();) {
            path = pathIter.next();
            
            returnedSet = binaryTagData.get(path);
            trueSet = GT_binaryTagData.get(path);

            for (Iterator<String> it = returnedSet.iterator(); it.hasNext();) {
                tag = it.next();
                if (trueSet.contains(tag)) {
                    totalTruePositive++;
                    incrementTagCount(tag2truePositive, tag);
                    incrementTagCount(track2truePositive, path);
                } else {
                    totalFalsePositive++;
                    incrementTagCount(tag2falsePositive, tag);
                    incrementTagCount(track2falsePositive, path);
                }
            }
            remainder = (HashSet<String>) trueSet.clone();
            remainder.removeAll(returnedSet);
            for (Iterator<String> it = remainder.iterator(); it.hasNext();) {
                tag = it.next();
                totalFalseNegative++;
                incrementTagCount(tag2falseNegative, tag);
                incrementTagCount(track2falseNegative, path);
            }
        }

        if(verbose){
            System.err.println("\tcomputing per-track statistics");
        }
        
        //compute per track stats
        for (Iterator<String> it = inUse.iterator(); it.hasNext();) {
            path = it.next();
            //fill in any gaps in maps
            if (!track2truePositive.containsKey(path)) {
                track2truePositive.put(path, new AtomicInteger(0));
            }
            if (!track2falsePositive.containsKey(path)) {
                track2falsePositive.put(path, new AtomicInteger(0));
            }
            if (!track2falseNegative.containsKey(path)) {
                track2falseNegative.put(path, new AtomicInteger(0));
            }
            int tp = track2truePositive.get(path).intValue();
            int fp = track2falsePositive.get(path).intValue();
            int fn = track2falseNegative.get(path).intValue();
            int tn = tagSet.size() - (tp + fp + fn);
            
            double accuracy = (double) (tp + tn) / (double) tagSet.size();
            double posAccuracy = (double) tp / (double)(tp+fn);
            double negAccuracy = (double) tn / (double)(fp+tn);
            
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
            track2Accuracy.put(path, accuracy);
            track2PosAccuracy.put(path, posAccuracy);
            track2NegAccuracy.put(path, negAccuracy);
            track2Precision.put(path, precision);
            track2Recall.put(path, recall);
            track2FMeasure.put(path, fMeasure);
            
            int numPositivesExamples = tp + fn;
            int numNegativeExamples = tn + fp;
            track2numPositiveExamples.put(path,numPositivesExamples);
            track2numNegativeExamples.put(path, numNegativeExamples);
        }
        
        if(verbose){
            System.err.println("\tcomputing per-tag statistics");
        }
        
        //compute per tag stats
        for (Iterator<String> it = tagSet.iterator(); it.hasNext();) {
            tag = it.next();

            
            if(!tag2truePositive.containsKey(tag) && !tag2falseNegative.containsKey(tag)){
                //Don't fill in any gaps in maps and don't evaluate
                
            }else{
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
                int tn = inUse.size() - (tp + fp + fn);

                totalTrueNegative += tn;

                double accuracy = (double) (tp + tn) / (double) inUse.size();

                double posAccuracy = (double) tp / (double)(tp+fn);
                double negAccuracy = (double) tn / (double)(fp+tn);

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
                tag2PosAccuracy.put(tag, posAccuracy);
                tag2NegAccuracy.put(tag, negAccuracy);
                tag2Precision.put(tag, precision);
                tag2Recall.put(tag, recall);
                tag2FMeasure.put(tag, fMeasure);

                int numPositivesExamples = tp + fn;
                int numNegativeExamples = tn + fp;
                tag2numPositiveExamples.put(tag,numPositivesExamples);
                tag2numNegativeExamples.put(tag, numNegativeExamples);
            }
        }

        if(verbose){
            System.err.println("\tcomputing total statistics");
        }
        
        //compute total stats
        totalAccuracy = (double) (totalTruePositive + totalTrueNegative) / (double) (totalTruePositive + totalFalsePositive + totalFalseNegative + totalTrueNegative);
        totalPosAccuracy = (double) totalTruePositive / (double) (totalTruePositive + totalFalseNegative);
        totalNegAccuracy = (double) totalTrueNegative / (double) (totalFalsePositive + totalTrueNegative);
        totalPrecision = (double) totalTruePositive / (double) (totalTruePositive + totalFalsePositive);
        totalRecall = (double) totalTruePositive / (double) (totalTruePositive + totalFalseNegative);
        totalFmeasure = (2 * totalRecall * totalPrecision) / (totalRecall + totalPrecision);

        //append results to report
        systemReport += "Overall accuracy:             " + totalAccuracy + "\n";
        systemReport += "Overall +ve example accuracy: " + totalPosAccuracy + "\n";
        systemReport += "Overall -ve example accuracy: " + totalNegAccuracy + "\n";
        systemReport += "Overall precision:            " + totalPrecision + "\n";
        systemReport += "Overall recall:               " + totalRecall + "\n";
        systemReport += "Overall fMeasure:             " + totalFmeasure + "\n";
        
        for (Iterator<String> it = tagSet.iterator(); it.hasNext();) {
            tag = it.next();
            if (tag2FMeasure.containsKey(tag)){
                systemReport += "tag '" + tag + "':\n";
                systemReport += "    accuracy:                 " + tag2Accuracy.get(tag).doubleValue() + "\n";
                systemReport += "    +ve example accuracy:     " + tag2PosAccuracy.get(tag).doubleValue() + "\n";
                systemReport += "    -ve example accuracy:     " + tag2NegAccuracy.get(tag).doubleValue() + "\n";
                systemReport += "    precision:                " + tag2Precision.get(tag).doubleValue() + "\n";
                systemReport += "    recall:                   " + tag2Recall.get(tag).doubleValue() + "\n";
                systemReport += "    fMeasure:                 " + tag2FMeasure.get(tag).doubleValue() + "\n";
            }else{
                systemReport += "tag '" + tag + "': skipped as there were no examples in the test set\n";
            }
        }
//        for (Iterator<String> it = binaryTagData.keySet().iterator(); it.hasNext();) {
//            path = it.next();
//            systemReport += "track '" + path + "':\n";
//            systemReport += "    accuracy:                 " + track2Accuracy.get(path).doubleValue() + "\n";
//            systemReport += "    +ve example accuracy:     " + track2PosAccuracy.get(path).doubleValue() + "\n";
//            systemReport += "    -ve example accuracy:     " + track2NegAccuracy.get(path).doubleValue() + "\n";
//            systemReport += "    precision:                " + track2Precision.get(path).doubleValue() + "\n";
//            systemReport += "    recall:                   " + track2Recall.get(path).doubleValue() + "\n";
//            systemReport += "    fMeasure:                 " + track2FMeasure.get(path).doubleValue() + "\n";
//        }

        
        if(verbose){
            System.err.println("\toutputting");
        }
        //report on files evaluated against
        systemReport += "Number of files tested against: " + inUse.size() + "\n";
//        systemReport += "Test file paths: " + binaryTagData.size() + "\n";
//        String[] paths = binaryTagData.keySet().toArray(new String[binaryTagData.size()]);
//        Arrays.sort(paths);
//        for (int j = 0; j < paths.length; j++) {
//            systemReport += "\t" + paths[j] + "\n";
//        }
        systemReport += "  ---   \n\n";

        //store evaluation data
        dataToEvaluate.setMetadata(DataObj.SYSTEM_RESULTS_REPORT, systemReport);
        
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_ACCURACY_MAP, tag2Accuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_POS_ACCURACY_MAP, tag2PosAccuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_NEG_ACCURACY_MAP, tag2NegAccuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_PRECISION_MAP, tag2Precision);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_RECALL_MAP, tag2Recall);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_FMEASURE_MAP, tag2FMeasure);
        
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_ACCURACY_MAP, track2Accuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_POS_ACCURACY_MAP, track2PosAccuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_NEG_ACCURACY_MAP, track2NegAccuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_PRECISION_MAP, track2Precision);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_RECALL_MAP, track2Recall);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_FMEASURE_MAP, track2FMeasure);
        
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_ACCURACY, totalAccuracy);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_PRECISION, totalPrecision);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_RECALL, totalRecall);
        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_FMEASURE, totalFmeasure);
        
        dataToEvaluate.setMetadata(DataObj.TAG_NUM_POSITIVE_EXAMPLES, tag2numPositiveExamples);
        dataToEvaluate.setMetadata(DataObj.TAG_NUM_NEGATIVE_EXAMPLES, tag2numNegativeExamples);
        
        //serialise out the evaluation data
        //  later this should be changed to use ASCII file format - when Collections are supported by write method of DataObj
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

    public String evaluate(String[] systemNames, DataObj[][] dataToEvaluate, DataObj groundTruth, File outputDir) throws noMetadataException{
        String report = "";

        //evaluate each system
        for (int i = 0; i < dataToEvaluate.length; i++) {
            System.err.println("Evaluating system: " + systemNames[i]);
            report += "System: " + systemNames[i] + "\n";
            //evaluate each fold
            for (int j = 0; j < dataToEvaluate[i].length; j++) {
                String systemReport = evaluateResultsAgainstGT(systemNames[i], dataToEvaluate[i][j], groundTruth, outputDir);
                report += systemReport + DataObj.DIVIDER + "\n";
            }
            report += DataObj.DIVIDER + "\n";
            System.err.println("done.");
        }
        
        System.err.println("Writing out CSV result files");
        File[] CSVResultFiles = writeOutCSVResultFiles(dataToEvaluate, systemNames, outputDir);
        
        System.err.println("Writing out overall report");
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
            System.err.println("Performing significance tests");
            //call matlab and execute Friedman's test with TK HSD for Fmeasure
            System.out.println("\tFriedman f-measure...");
            performFriedmanTestWithFMeasure(outputDir, CSVResultFiles, systemNames);
            System.out.println("\tdone");
            
            //call matlab and execute Friedman's test with TK HSD for Fmeasure per track
            System.out.println("\tFriedman f-measure per track...");
            performFriedmanTestWithFMeasurePerTrack(outputDir, CSVResultFiles, systemNames);
            System.out.println("\tdone");
            
            //call matlab and execute Friedman's test with TK HSD for Accuracy
            System.out.println("\tFriedman accuracy...");
            performFriedmanTestWithAccuracy(outputDir, CSVResultFiles, systemNames);
            System.out.println("\tdone.");
//
////            //  call matlab and execute Beta-Binomial test with accuracy scores
//            System.out.println("\tBeta Binomial...");
//            performBetaBinomialTestWithAccuracy(outputDir, CSVResultFiles, systemNames);
//            System.out.println("\tdone.");
//            
            System.out.println("done.");
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
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForTagsAccuracy";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.friedman.tukeyKramerHSD.png";
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));
//
//            textOut.write("[data, result] = readtext('" + CSVResultFiles[ACCURACY_FILE_INDEX].getAbsolutePath() + "', '\t');");
//            textOut.newLine();
//            textOut.write("algNames = data(1,5:" + (systemNames.length + 4) + ")';");
//            textOut.newLine();
//            textOut.write("[length,width] = size(data);");
//            textOut.newLine();
//            textOut.write("Acc_Scores = cell2mat(data(2:length,5:" + (systemNames.length + 4) + "));");
//            textOut.newLine();
//            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores,1,'on');");
//            textOut.newLine();
//            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
//            textOut.newLine();
//            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
//            textOut.newLine();
//            textOut.write("exit;");
//            textOut.newLine();

            
            textOut.write("[data, result] = readtext('" + CSVResultFiles[ACCURACY_FILE_INDEX].getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,4:" + (systemNames.length + 3) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length,4:" + (systemNames.length + 3) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(Acc_Scores));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores(:,fliplr(sort_idx)),1,'on'); close(gcf)");
            textOut.newLine();
            textOut.write("[c,m,h,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05,'display','off');");
            textOut.newLine();
            textOut.write("fig = figure;");
            textOut.newLine();
            textOut.write("width = (-c(1,3)+c(1,5))/4;");
            textOut.newLine();
            textOut.write("set(gcf,'position',[497   313   450   351])");
            textOut.newLine();
            textOut.write("plot(friedmanStats.meanranks,'ro'); hold on");
            textOut.newLine();
            textOut.write("for i=1:" + systemNames.length + ",");
            textOut.newLine();
            textOut.write("    plot([i i],[-width width]+friedmanStats.meanranks(i));");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[-width -width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[+width +width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("set(gca,'xtick',1:" + systemNames.length + ",'xlim',[0.5 " + systemNames.length + "+0.5])");
            textOut.newLine();
            textOut.write("sortedAlgNames = algNames(fliplr(sort_idx));");
            textOut.newLine();
            textOut.write("set(gca,'xticklabel',sortedAlgNames)");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFiles[ACCURACY_FILE_INDEX].getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("fidFriedman=fopen('" + friedmanTablePath + "','w+');");
            textOut.newLine();
            textOut.write("fprintf(fidFriedman,'%s,%s,%s,%s,%s,%s\\n','*TeamID','TeamID','Lowerbound','Mean','Upperbound','Significance');");
            textOut.newLine();
            textOut.write("for i=1:size(c,1)");
            textOut.newLine();
            textOut.write("        if sign(c(i,3))*sign(c(i,5)) > 0");
            textOut.newLine();
            textOut.write("            tf='TRUE';");
            textOut.newLine();
            textOut.write("        else");
            textOut.newLine();
            textOut.write("            tf='FALSE';");
            textOut.newLine();
            textOut.write("        end");
            textOut.newLine();
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',sortedAlgNames{c(i,1)},sortedAlgNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("fclose(fidFriedman);");
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
        
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        //create an m-file to run the test
        String evalCommand = "performFriedmanForTagsFMeasure";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "binary_FMeasure.friedman.tukeyKramerHSD.png";
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + "binary_FMeasure.friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFiles[FMEASURE_FILE_INDEX].getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,4:" + (systemNames.length + 3) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Fmeasure_Scores = cell2mat(data(2:length,4:" + (systemNames.length + 3) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(Fmeasure_Scores));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Fmeasure_Scores(:,fliplr(sort_idx)),1,'on'); close(gcf)");
            textOut.newLine();
            textOut.write("[c,m,h,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05,'display','off');");
            textOut.newLine();
            textOut.write("fig = figure;");
            textOut.newLine();
            textOut.write("width = (-c(1,3)+c(1,5))/4;");
            textOut.newLine();
            textOut.write("set(gcf,'position',[497   313   450   351])");
            textOut.newLine();
            textOut.write("plot(friedmanStats.meanranks,'ro'); hold on");
            textOut.newLine();
            textOut.write("for i=1:" + systemNames.length + ",");
            textOut.newLine();
            textOut.write("    plot([i i],[-width width]+friedmanStats.meanranks(i));");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[-width -width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[+width +width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("set(gca,'xtick',1:" + systemNames.length + ",'xlim',[0.5 " + systemNames.length + "+0.5])");
            textOut.newLine();
            textOut.write("sortedAlgNames = algNames(fliplr(sort_idx));");
            textOut.newLine();
            textOut.write("set(gca,'xticklabel',sortedAlgNames)");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFiles[FMEASURE_FILE_INDEX].getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("fidFriedman=fopen('" + friedmanTablePath + "','w+');");
            textOut.newLine();
            textOut.write("fprintf(fidFriedman,'%s,%s,%s,%s,%s,%s\\n','*TeamID','TeamID','Lowerbound','Mean','Upperbound','Significance');");
            textOut.newLine();
            textOut.write("for i=1:size(c,1)");
            textOut.newLine();
            textOut.write("        if sign(c(i,3))*sign(c(i,5)) > 0");
            textOut.newLine();
            textOut.write("            tf='TRUE';");
            textOut.newLine();
            textOut.write("        else");
            textOut.newLine();
            textOut.write("            tf='FALSE';");
            textOut.newLine();
            textOut.write("        end");
            textOut.newLine();
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',sortedAlgNames{c(i,1)},sortedAlgNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("fclose(fidFriedman);");
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
    
    private void performFriedmanTestWithFMeasurePerTrack(File outputDir, File[] CSVResultFiles, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        //create an m-file to run the test
        String evalCommand = "performFriedmanForTagsFMeasurePerTrack";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "binary_FMeasure_per_track.friedman.tukeyKramerHSD.png";
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + "binary_FMeasure_per_track.friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFiles[FMEASURE_PER_TRACK_FILE_INDEX].getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,3:" + (systemNames.length + 2) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Fmeasure_Scores = cell2mat(data(2:length,3:" + (systemNames.length + 2) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(Fmeasure_Scores));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Fmeasure_Scores(:,fliplr(sort_idx)),1,'on'); close(gcf)");
            textOut.newLine();
            textOut.write("[c,m,h,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05,'display','off');");
            textOut.newLine();
            textOut.write("fig = figure;");
            textOut.newLine();
            textOut.write("width = (-c(1,3)+c(1,5))/4;");
            textOut.newLine();
            textOut.write("set(gcf,'position',[497   313   450   351])");
            textOut.newLine();
            textOut.write("plot(friedmanStats.meanranks,'ro'); hold on");
            textOut.newLine();
            textOut.write("for i=1:" + systemNames.length + ",");
            textOut.newLine();
            textOut.write("    plot([i i],[-width width]+friedmanStats.meanranks(i));");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[-width -width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[+width +width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("set(gca,'xtick',1:" + systemNames.length + ",'xlim',[0.5 " + systemNames.length + "+0.5])");
            textOut.newLine();
            textOut.write("sortedAlgNames = algNames(fliplr(sort_idx));");
            textOut.newLine();
            textOut.write("set(gca,'xticklabel',sortedAlgNames)");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFiles[FMEASURE_PER_TRACK_FILE_INDEX].getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("fidFriedman=fopen('" + friedmanTablePath + "','w+');");
            textOut.newLine();
            textOut.write("fprintf(fidFriedman,'%s,%s,%s,%s,%s,%s\\n','*TeamID','TeamID','Lowerbound','Mean','Upperbound','Significance');");
            textOut.newLine();
            textOut.write("for i=1:size(c,1)");
            textOut.newLine();
            textOut.write("        if sign(c(i,3))*sign(c(i,5)) > 0");
            textOut.newLine();
            textOut.write("            tf='TRUE';");
            textOut.newLine();
            textOut.write("        else");
            textOut.newLine();
            textOut.write("            tf='FALSE';");
            textOut.newLine();
            textOut.write("        end");
            textOut.newLine();
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',sortedAlgNames{c(i,1)},sortedAlgNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("fclose(fidFriedman);");
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
    
    
    //commented as its heavily affected by the skewed distribution of positive and negative examples
//    private void performBetaBinomialTestWithAccuracy(File outputDir, File[] CSVResultFiles, String[] systemNames) {
//        //make sure readtext is in the working directory for matlab
//        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
//        File BBFile = new File(outputDir.getAbsolutePath() + File.separator + "betaBinomEB.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/betaBinomEB.m", BBFile);
//        File BBreportFile = new File(outputDir.getAbsolutePath() + File.separator + "bbReport.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/bbReport.m", BBreportFile);
//        File drawMultinomFile = new File(outputDir.getAbsolutePath() + File.separator + "drawMultinom.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/drawMultinom.m", drawMultinomFile);
//        File argmaxFile = new File(outputDir.getAbsolutePath() + File.separator + "argmax.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/argmax.m", argmaxFile);
//        
//        File outputPlotDir = new File(outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.BetaBinomial");
//        outputPlotDir.mkdirs();
//        
//        //create an m-file to run the test
//        String evalCommand = "performBBForTagsAccuracy";
//        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
//        String matlabOutputPath = outputDir.getAbsolutePath() + File.separator + "binary_Accuracy.BetaBinomial";
//        try {
//            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));
//
//            textOut.write("bbReport('" +  CSVResultFiles[ACCURACY_PER_FOLD_FILE_INDEX].getAbsolutePath() + "', '" + matlabOutputPath + "')");
//            textOut.newLine();
//            textOut.write("exit;");
//            textOut.newLine();
//            
//            textOut.close();
//        } catch (IOException ex) {
//            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
//        matlabIntegrator.setMatlabBin(matlabPath);
//        matlabIntegrator.setCommandFormattingStr("");
//        matlabIntegrator.setMainCommand(evalCommand);
//        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
//        matlabIntegrator.start();
//        try {
//            matlabIntegrator.join();
//
//            //  call matlab and execute Beta-Binomial test
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//
//        //delete readtext.m
//        readtextMFile.delete();
//    }

    private String[] produceSummaryRowAvgByTag(String metadataType, String measureName, String[] systemNames, int numFolds, DataObj[][] dataToEvaluate, String[][] tagNames) throws noMetadataException {

        String[] csvData = new String[systemNames.length + 1];
        csvData[0] = measureName;
        double[] avg = new double[systemNames.length];
        int totalTags = 0;
        for (int f = 0; f < numFolds; f++) {
            totalTags += tagNames[f].length;
            for (int j = 0; j < tagNames[f].length; j++) {
                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> data = (HashMap<String, Double>) dataToEvaluate[s][f].getMetadata(metadataType);
                    avg[s] += data.get(tagNames[f][j]).doubleValue();
                }
            }
        }

        for (int i = 0; i < avg.length; i++){
            avg[i] /= totalTags;
            csvData[i+1] = "" + avg[i];
        }
        return csvData;
    }

    private void writeCSVResultFile(String metadataType, int totalNumRows, String[] systemNames, int numFolds, DataObj[][] dataToEvaluate, String[][] tagNames, File outputFile) throws noMetadataException {

        String[][] csvData = new String[totalNumRows + 1][systemNames.length + 4];
        csvData[0][0] = "*tag";
        csvData[0][1] = "fold";
        csvData[0][2] = "positive examples";
        csvData[0][3] = "negative examples";

        for (int i = 0; i < systemNames.length; i++) {
            csvData[0][i + 4] = systemNames[i];
        }
        int foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            HashMap<String, Integer> tag2NumPos = (HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(DataObj.TAG_NUM_POSITIVE_EXAMPLES);
            HashMap<String, Integer> tag2NumNeg = (HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(DataObj.TAG_NUM_NEGATIVE_EXAMPLES);
            for (int j = 0; j < tagNames[f].length; j++) {
                csvData[foldOffset + j][0] = tagNames[f][j];
                csvData[foldOffset + j][1] = "" + (f + 1);
                csvData[foldOffset + j][2] = "" + tag2NumPos.get(tagNames[f][j]);
                csvData[foldOffset + j][3] = "" + tag2NumNeg.get(tagNames[f][j]);


                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> data = (HashMap<String, Double>) dataToEvaluate[s][f].getMetadata(metadataType);
                    csvData[foldOffset + j][s + 4] = "" + data.get(tagNames[f][j]).doubleValue();
                }
            }
            foldOffset += tagNames[f].length;
        }
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(outputFile, ",", csvData, false);
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeCSVResultFilePerTrack(String metadataType, int totalNumRows, String[] systemNames, int numFolds, DataObj[][] dataToEvaluate, String[][] pathNames, File outputFile) throws noMetadataException {

        String[][] csvData = new String[totalNumRows + 1][systemNames.length + 2];
        csvData[0][0] = "*Track";
        csvData[0][1] = "Fold";
        
        for (int i = 0; i < systemNames.length; i++) {
            csvData[0][i + 2] = systemNames[i];
        }
        int foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            for (int j = 0; j < pathNames[f].length; j++) {
                csvData[foldOffset + j][0] = pathNames[f][j];
                csvData[foldOffset + j][1] = "" + (f + 1);
                
                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> data = (HashMap<String, Double>) dataToEvaluate[s][f].getMetadata(metadataType);
                    csvData[foldOffset + j][s + 2] = "" + data.get(pathNames[f][j]).doubleValue();
                }
            }
            foldOffset += pathNames[f].length;
        }
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(outputFile, ",", csvData, false);
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeAvgAcrossFoldsCSVResultFile(String metadataType,  String[] systemNames, int numFolds, DataObj[][] dataToEvaluate, HashSet<String> tagNames, File outputFile) throws noMetadataException {

        String[][] csvData = new String[tagNames.size()+1][systemNames.length + 3];
        csvData[0][0] = "*Tag";
        csvData[0][1] = "Positive Examples";
        csvData[0][2] = "Negative Examples";

        for (int i = 0; i < systemNames.length; i++) {
            csvData[0][i + 3] = systemNames[i];
        }
        
        int rowIdx = 1;
        for (Iterator<String> it = tagNames.iterator(); it.hasNext();) {
            String tag = it.next();
            csvData[rowIdx][0] = tag;
            int numPos = 0, numNeg = 0;
            for (int f = 0; f < numFolds; f++) {
                numPos += ((HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(DataObj.TAG_NUM_POSITIVE_EXAMPLES)).get(tag);
                numNeg += ((HashMap<String, Integer>) dataToEvaluate[0][f].getMetadata(DataObj.TAG_NUM_NEGATIVE_EXAMPLES)).get(tag);
            }
            csvData[rowIdx][1] = "" + numPos;
            csvData[rowIdx][2] = "" + numNeg;
            for (int s = 0; s < systemNames.length; s++) {
                double avg = 0.0;
                for (int f = 0; f < numFolds; f++) {
                   avg +=((HashMap<String, Double>) dataToEvaluate[s][f].getMetadata(metadataType)).get(tag).doubleValue();;
                }
                avg /= numFolds;
                csvData[rowIdx][s + 3] = "" + avg;
            }
            
            rowIdx++;
        }
        
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(outputFile, ",", csvData, false);
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private File[] writeOutCSVResultFiles(DataObj[][] dataToEvaluate, String[] systemNames, File outputDir) throws noMetadataException {


        //TODO: stat sig tests
        //  prepare per-tag F-measure and accuracy data files for use in Friedmans's test with TK HSD
        int numFolds = dataToEvaluate[0].length;
        int totalNumRows = 0;
        int totalNumTrackRows = 0;
        String[][] tagNames = new String[numFolds][];
        HashSet<String> tagNamesSet = new HashSet<String>();
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> tag2fmeasureMap = (HashMap<String, Double>) dataToEvaluate[0][i].getMetadata(DataObj.TAG_BINARY_FMEASURE_MAP);
            tagNames[i] = tag2fmeasureMap.keySet().toArray(new String[tag2fmeasureMap.size()]);
            tagNamesSet.addAll(tag2fmeasureMap.keySet());
            totalNumRows += tagNames[i].length;
        }
        
        String[][] paths = new String[numFolds][];
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> track2fmeasureMap = (HashMap<String, Double>) dataToEvaluate[0][i].getMetadata(DataObj.TAG_BINARY_TRACK_FMEASURE_MAP);
            paths[i] = track2fmeasureMap.keySet().toArray(new String[track2fmeasureMap.size()]);
            totalNumTrackRows += paths[i].length;
        }

        File FmeasureFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_Fmeasure.csv");
        writeCSVResultFile(DataObj.TAG_BINARY_FMEASURE_MAP,totalNumRows, systemNames, numFolds, dataToEvaluate, tagNames, FmeasureFile);

        File AccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_Accuracy.csv");
        writeCSVResultFile(DataObj.TAG_BINARY_ACCURACY_MAP,totalNumRows, systemNames, numFolds, dataToEvaluate, tagNames, AccuracyFile);
  
        File PosAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_positive_example_Accuracy.csv");
        writeCSVResultFile(DataObj.TAG_BINARY_POS_ACCURACY_MAP,totalNumRows, systemNames, numFolds, dataToEvaluate, tagNames, PosAccuracyFile);
  
        File NegAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_negative_example_Accuracy.csv");
        writeCSVResultFile(DataObj.TAG_BINARY_NEG_ACCURACY_MAP,totalNumRows, systemNames, numFolds, dataToEvaluate, tagNames, NegAccuracyFile);
  
        
        File TrackFmeasureFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_per_track_Fmeasure.csv");
        writeCSVResultFilePerTrack(DataObj.TAG_BINARY_TRACK_FMEASURE_MAP,totalNumTrackRows, systemNames, numFolds, dataToEvaluate, paths, TrackFmeasureFile);

        File TrackAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_per_track_Accuracy.csv");
        writeCSVResultFilePerTrack(DataObj.TAG_BINARY_TRACK_ACCURACY_MAP,totalNumTrackRows, systemNames, numFolds, dataToEvaluate, paths, TrackAccuracyFile);
  
        File TrackPosAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_per_track_positive_example_Accuracy.csv");
        writeCSVResultFilePerTrack(DataObj.TAG_BINARY_TRACK_POS_ACCURACY_MAP,totalNumTrackRows, systemNames, numFolds, dataToEvaluate, paths, TrackPosAccuracyFile);
  
        File TrackNegAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_per_fold_per_track_negative_example_Accuracy.csv");
        writeCSVResultFilePerTrack(DataObj.TAG_BINARY_TRACK_NEG_ACCURACY_MAP,totalNumTrackRows, systemNames, numFolds, dataToEvaluate, paths, TrackNegAccuracyFile);
  
        
        File AvgFmeasureFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_avg_Fmeasure.csv");
        writeAvgAcrossFoldsCSVResultFile(DataObj.TAG_BINARY_FMEASURE_MAP, systemNames, numFolds, dataToEvaluate, tagNamesSet, AvgFmeasureFile);

        File AvgAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_avg_Accuracy.csv");
        writeAvgAcrossFoldsCSVResultFile(DataObj.TAG_BINARY_ACCURACY_MAP, systemNames, numFolds, dataToEvaluate, tagNamesSet, AvgAccuracyFile);
  
        File AvgPosAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_avg_positive_example_Accuracy.csv");
        writeAvgAcrossFoldsCSVResultFile(DataObj.TAG_BINARY_POS_ACCURACY_MAP, systemNames, numFolds, dataToEvaluate, tagNamesSet, AvgPosAccuracyFile);
  
        File AvgNegAccuracyFile = new File(outputDir.getAbsolutePath() + File.separator + "binary_avg_negative_example_Accuracy.csv");
        writeAvgAcrossFoldsCSVResultFile(DataObj.TAG_BINARY_NEG_ACCURACY_MAP, systemNames, numFolds, dataToEvaluate, tagNamesSet, AvgNegAccuracyFile);


        String[][] summaryData = new String[5][];
        String[] header = new String[systemNames.length+1];
        header[0] = "*Measure";
        for (int i = 0; i < systemNames.length; i++){
            header[i+1] = systemNames[i];
        }
        summaryData[0] = header;
        summaryData[1] = produceSummaryRowAvgByTag(DataObj.TAG_BINARY_FMEASURE_MAP, "Average Tag F-measure", systemNames, numFolds, dataToEvaluate, tagNames);
        summaryData[2] = produceSummaryRowAvgByTag(DataObj.TAG_BINARY_ACCURACY_MAP, "Average Tag Accuracy", systemNames, numFolds, dataToEvaluate, tagNames);
        summaryData[3] = produceSummaryRowAvgByTag(DataObj.TAG_BINARY_POS_ACCURACY_MAP, "Average Positive Tag Accuracy", systemNames, numFolds, dataToEvaluate, tagNames);
        summaryData[4] = produceSummaryRowAvgByTag(DataObj.TAG_BINARY_NEG_ACCURACY_MAP, "Average Negative Tag Accuracy", systemNames, numFolds, dataToEvaluate, tagNames);

        File SummaryFile = new File(outputDir.getAbsolutePath() + File.separator + "summary_binary.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(SummaryFile, ",", summaryData, false);
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new File[]{AvgFmeasureFile,AvgAccuracyFile,AvgPosAccuracyFile,AvgNegAccuracyFile,FmeasureFile,AccuracyFile,PosAccuracyFile,NegAccuracyFile,TrackFmeasureFile,TrackAccuracyFile,TrackPosAccuracyFile,TrackNegAccuracyFile};
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
