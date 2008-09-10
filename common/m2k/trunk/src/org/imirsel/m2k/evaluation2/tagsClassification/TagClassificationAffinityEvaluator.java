/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.tagsClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.evaluation2.EvaluationDataObject;
import org.imirsel.m2k.evaluation2.Evaluator;
import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.util.MatlabCommandlineIntegrationClass;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.vis.SimpleNumericPlot;

/**
 *
 * @author kris
 */
public class TagClassificationAffinityEvaluator implements Evaluator{
    private boolean verbose = true;
    private boolean performMatlabStatSigTests = true;
    private String matlabPath = "matlab";
    
    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose_) {
        verbose = verbose_;
    }

    public boolean returnsInCSV() {
        return true;
    }

    
    private void addROCpoint(ArrayList<double[]> ROCpointSequence, double falsePosRate, double truePosRate){
        ROCpointSequence.add(new double[]{falsePosRate,truePosRate});
    }
    
    private double computeAreaUnderROCCurve(ArrayList<double[]> ROCpointSequence){
        //System.out.println("computing AUC-ROC for " + ROCpointSequence.size() + " point sequence");
        double[] last = ROCpointSequence.get(0);
        double[] curr;
        double area = 0.0;
        for (int i = 1; i < ROCpointSequence.size(); i++) {
            curr = ROCpointSequence.get(i);
            double trap = trapezoidArea(last[0], curr[0], last[1], curr[1]);
            //System.out.println("\ttrap area: " + trap);
            area += trap;
            last = curr;
        }
        //System.out.println("returning " + area);
        return area;
    }
    
    private double trapezoidArea(double x1, double x2, double y1, double y2){
        //System.out.println("x1: " + x1 + ", x2: " + x2 + ", y1: " + y1 + ", y2: " + y2);
        double area = ((y1 + y2)/2.0) * (x2 - x1);
        //System.out.println("trapezoid area: " + area);
        return area;
    }
    
    public String evaluateResultsAgainstGT(String systemName, EvaluationDataObject dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException {
        //init report
        String systemReport = "-----------------------------------------------------------\n" +
                              "System name:       " + systemName + "\n" +
                              "Results file:      " + dataToEvaluate.getFile().getAbsolutePath() + "\n";
        systemReport =        "Ground-truth file: " + groundTruth.getFile().getAbsolutePath() + "\n";

        //check GT and eval data for existence of right data 
        if (!groundTruth.hasMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP)) {
            throw new noMetadataException("No " + EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP + " ground-truth metadata found in object, representing " + groundTruth.getFile().getAbsolutePath());
        }
        if (!dataToEvaluate.hasMetadata(EvaluationDataObject.TAG_AFFINITY_MAP)) {
            File theFile = dataToEvaluate.getFile();
            throw new RuntimeException("No " + EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP + " evaluation metadata found in object representing " + theFile.getAbsolutePath());
        }
        
        //get the data to compare
        HashMap<String, HashMap<String,Double>> path2tag2affinity = (HashMap<String, HashMap<String,Double>>) dataToEvaluate.getMetadata(EvaluationDataObject.TAG_AFFINITY_MAP);
        HashMap<String, HashSet<String>> GT_binaryTagData = (HashMap<String, HashSet<String>>) groundTruth.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        HashSet<String> tagSet = (HashSet<String>)groundTruth.getMetadata(EvaluationDataObject.TAG_NAME_SET);
        
        if (!GT_binaryTagData.keySet().containsAll(path2tag2affinity.keySet())) {
            HashSet<String> missingData = new HashSet<String>();
            missingData.addAll(path2tag2affinity.keySet());
            missingData.removeAll(GT_binaryTagData.keySet());
            String missingPaths = "";
            for (Iterator<String> it = missingData.iterator(); it.hasNext();) {
                missingPaths += it.next() + ", ";                
            }
            throw new RuntimeException("The groundtruth Object representing " + groundTruth.getFile().getAbsolutePath() + ", does not have ground truth" +
                    "for all the paths specified in " + dataToEvaluate.getFile().getAbsolutePath() + ". Paths without data: " + missingPaths);
        }
        
        
        //prepare the output directory
        File systemDirectory = new File(outputDir.getAbsolutePath() + File.separator + systemName);
        systemDirectory.mkdirs();
        File plotDir = new File(systemDirectory.getAbsolutePath() + File.separator + "ROC curve plots");
        plotDir.mkdir();
        
        //result objects
        HashMap<String, Double> clip2AUC_ROC = new HashMap<String, Double>();
        HashMap<String, ArrayList<double[]>> clip2ROCpointSequence = new HashMap<String, ArrayList<double[]>>();
        
        HashMap<String, Double> tag2AUC_ROC = new HashMap<String, Double>();
        HashMap<String, ArrayList<double[]>> tag2ROCpointSequence = new HashMap<String, ArrayList<double[]>>();
        
        HashMap<String, ArrayList<AffinityDataPoint>> tag2affinityDataPoints = new HashMap<String, ArrayList<AffinityDataPoint>>();
        for (Iterator<String> it = tagSet.iterator(); it.hasNext();) {
            tag2affinityDataPoints.put(it.next(),new ArrayList<AffinityDataPoint>());
        }
        HashMap<String, Integer> tag2numPositiveExamples = new HashMap<String, Integer>();
        HashMap<String, Integer> tag2numNegativeExamples = new HashMap<String, Integer>();
        
        //util references
        String path, tag;
        HashMap<String,Double> returnedAffinities;
        HashSet<String> trueSet,missingAffinities;
        ArrayList<AffinityDataPoint> dataPointList;
        int positives;
        int negatives;
        int truePositives;
        int falsePositives;
        double truePosRate;
        double falsePosRate;
        double lastAffinity = Double.NEGATIVE_INFINITY;
        AffinityDataPoint dataPoint;
        ArrayList<double[]> anROCpointSequence;
        double[] ROCdomain;
        double[] ROCrange;
        double[] ds;
        int idx;
        ArrayList<AffinityDataPoint> tmpClipROCPointList;
        
        //step through all paths in the results and populate the tag2affinityDataPoints map
        for (Iterator<String> pathIter = path2tag2affinity.keySet().iterator(); pathIter.hasNext();) {
            path = pathIter.next();
            //System.out.println("path: " + path);
            returnedAffinities = path2tag2affinity.get(path);
            trueSet = GT_binaryTagData.get(path);
            tmpClipROCPointList = new ArrayList<AffinityDataPoint>();
            
            for (Iterator<String> it = returnedAffinities.keySet().iterator(); it.hasNext();) {
                tag = it.next();
                //System.out.println("\ttag: " + tag);
                tag2affinityDataPoints.get(tag).add(new AffinityDataPoint(trueSet.contains(tag), returnedAffinities.get(tag)));
                tmpClipROCPointList.add(new AffinityDataPoint(trueSet.contains(tag), returnedAffinities.get(tag)));
                
            }
            
            //fill in missing affinities
            missingAffinities = (HashSet<String>)tagSet.clone();
            missingAffinities.removeAll(returnedAffinities.keySet());
            for (Iterator<String> it = missingAffinities.iterator(); it.hasNext();) {
                tag = it.next();
                tag2affinityDataPoints.get(tag).add(new AffinityDataPoint(trueSet.contains(tag), Double.NEGATIVE_INFINITY));
                tmpClipROCPointList.add(new AffinityDataPoint(trueSet.contains(tag), Double.NEGATIVE_INFINITY));
            }
            Collections.sort(tmpClipROCPointList);
            
            //count positives and negatives
            positives = 0;
            negatives = 0;
            for (Iterator<AffinityDataPoint> it2 = tmpClipROCPointList.iterator(); it2.hasNext();) {
                dataPoint = it2.next();
                if(dataPoint.tagApplies){
                    positives++;
                }else{
                    negatives++;
                }
            }
            if (positives == 0){
                System.out.println("no positives!");
            }
            if (negatives == 0){
                System.out.println("no negatives!");
            }
            
            //compute ROC for each clip
            truePositives = 0;
            falsePositives = 0;
            truePosRate = 0.0;
            falsePosRate = 0.0;
            lastAffinity = Double.NEGATIVE_INFINITY;
            anROCpointSequence = new ArrayList<double[]>();
            clip2ROCpointSequence.put(path,anROCpointSequence);
            //addROCpoint(anROCpointSequence, 0.0, 0.0);
            for (Iterator<AffinityDataPoint> it2 = tmpClipROCPointList.iterator(); it2.hasNext();) {
                dataPoint = it2.next();
                if (dataPoint.affinity != lastAffinity){
                    addROCpoint(anROCpointSequence, ((double)falsePositives/(double)negatives), ((double)truePositives/(double)positives));
                }
                if (dataPoint.tagApplies){
                    truePositives++;
                }else{
                    falsePositives++;
                }
            }
            if (anROCpointSequence.size() == 0){
                addROCpoint(anROCpointSequence, 0.0, 0.0);
            }
            addROCpoint(anROCpointSequence, ((double)falsePositives/(double)negatives), ((double)truePositives/(double)positives));
            
            //compute AUC-ROC for each clip 
            double auc = computeAreaUnderROCCurve(anROCpointSequence);
            clip2AUC_ROC.put(path, auc);
            clip2ROCpointSequence.put(path,anROCpointSequence);
        }
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_CLIP_AUC_ROC, clip2AUC_ROC);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_CLIP_ROC_DATA, clip2ROCpointSequence);
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_TAG_AFFINITY_DATAPOINTS, tag2affinityDataPoints);

        //compute AUC-ROC for each tag
        for (Iterator<String> it = tagSet.iterator(); it.hasNext();) {
            tag = it.next();
            dataPointList = tag2affinityDataPoints.get(tag);
            Collections.sort(dataPointList);
            
            //count positives and negatives
            positives = 0;
            negatives = 0;
            for (Iterator<AffinityDataPoint> it2 = dataPointList.iterator(); it2.hasNext();) {
                dataPoint = it2.next();
                if(dataPoint.tagApplies){
                    positives++;
                }else{
                    negatives++;
                }
            }
            
            
            if (positives == 0){
                System.out.println("no positives!");
            }
            if (negatives == 0){
                System.out.println("no negatives!");
            }
            
            tag2numPositiveExamples.put(tag, positives);
            tag2numNegativeExamples.put(tag, negatives);
            
            //compute ROC
            truePositives = 0;
            falsePositives = 0;
            truePosRate = 0.0;
            falsePosRate = 0.0;
            lastAffinity = Double.NEGATIVE_INFINITY;
            anROCpointSequence = new ArrayList<double[]>();
            tag2ROCpointSequence.put(tag,anROCpointSequence);
            
            //addROCpoint(anROCpointSequence, 0.0, 0.0);
            for (Iterator<AffinityDataPoint> it2 = dataPointList.iterator(); it2.hasNext();) {
                dataPoint = it2.next();
                if (dataPoint.affinity != lastAffinity){
                    addROCpoint(anROCpointSequence, ((double)falsePositives/(double)negatives), ((double)truePositives/(double)positives));
                }
                if (dataPoint.tagApplies){
                    truePositives++;
                }else{
                    falsePositives++;
                }
            }
            if (anROCpointSequence.size() == 0){
                addROCpoint(anROCpointSequence, 0.0, 0.0);
            }
            addROCpoint(anROCpointSequence, ((double)falsePositives/negatives), ((double)truePositives/(double)positives));
            
            systemReport += "Tag: " + tag + "\n";
//            systemReport += "ROC curve (false pos,true pos): ";
//            for (Iterator<double[]> it2 = anROCpointSequence.iterator(); it2.hasNext();) {
//                ds = it2.next();
//                systemReport += ds[0] + "," + ds[1] + "\t";
//            }
//            systemReport += "\n";
            
            //plot ROC curve and save
//            System.out.println("ROC sequence length: " + anROCpointSequence.size());
//            
//            System.out.println("ROC curve (false pos,true pos): ");
//            for (Iterator<double[]> it2 = anROCpointSequence.iterator(); it2.hasNext();) {
//                ds = it2.next();
//                System.out.print(ds[0] + "," + ds[1] + "\t");
//            }
//            System.out.println("");
            
            ROCdomain = new double[anROCpointSequence.size()];
            ROCrange = new double[anROCpointSequence.size()];
            idx = 0;
            for (Iterator<double[]> it2 = anROCpointSequence.iterator(); it2.hasNext();) {
                ds = it2.next();
                ROCdomain[idx] = ds[0];
                ROCrange[idx++] = ds[1];
            }
            SimpleNumericPlot tagROCPlot = new SimpleNumericPlot(false, false, dataToEvaluate.getFile().getName() + ", " + tag + " ROC curve", 
                    "tag", null, "True postive rate", "False positive rate", ROCdomain, ROCrange, null);
//            tagROCPlot.validate();
//            tagROCPlot.setVisible(true);
            
            
            File plotFile = new File(plotDir.getAbsolutePath() + File.separator + tag + ".png");
            tagROCPlot.writeChartToFile(plotFile,600,600);
            
            
            //compute AUC-ROC 
            double auc = computeAreaUnderROCCurve(anROCpointSequence);
            systemReport += "Area under the ROC curve (AUC-ROC): " + auc + "\n";
            tag2AUC_ROC.put(tag, auc);
        }
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC,tag2AUC_ROC);
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_ROC_DATA,tag2ROCpointSequence);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES, tag2numPositiveExamples);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES, tag2numNegativeExamples);
        
        
        //report on files evaluated against
        systemReport += "Number of files tested against: " + path2tag2affinity.size() + "\n";
//        systemReport += "Test file paths: " + path2tag2affinity.size() + "\n";
//        String[] paths = path2tag2affinity.keySet().toArray(new String[path2tag2affinity.size()]);
//        Arrays.sort(paths);
//        for (int j = 0; j < paths.length; j++) {
//            systemReport += "\t" + paths[j] + "\n";
//        }
        systemReport += "  ---   \n\n";

        //store evaluation report
        dataToEvaluate.setMetadata(EvaluationDataObject.SYSTEM_RESULTS_REPORT, systemReport);
        
        //serialise out the evaluation data
        //  later this should be changed to use ASCII file format - when Collections are supported by write method of EvaluationDataObject
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

        if (verbose){
            System.out.println(systemReport);
        }
        return systemReport;
    }

    public String evaluate(String[] systemNames, EvaluationDataObject[][] dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException{
        String report = "";

        //evaluate each system
        for (int i = 0; i < dataToEvaluate.length; i++) {
            System.err.println("Evaluating system: " + systemNames[i]);
            report += "System: " + systemNames[i] + "\n";
            //evaluate each fold
            for (int j = 0; j < dataToEvaluate[i].length; j++) {
                String systemReport = evaluateResultsAgainstGT(systemNames[i], dataToEvaluate[i][j], groundTruth, outputDir);
                report += systemReport + EvaluationDataObject.DIVIDER + "\n";
            }
            report += EvaluationDataObject.DIVIDER + "\n";
            System.err.println("done.");
        }

        
        //  prepare per-tag tag and clip AUC-ROC data for use in Friedmans's test with TK HSD
        int numFolds = dataToEvaluate[0].length;
        int totalNumRows = 0;
        
        System.err.println("Writing out tag AUC-ROC data");
        //Write out tag AUC-ROC data for significance testing
        String[][] tagNames = new String[numFolds][];
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> tag2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[0][i].getMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC);
            tagNames[i] = tag2AUC_ROC.keySet().toArray(new String[tag2AUC_ROC.size()]);
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
            HashMap<String,Integer> tag2NumPos = (HashMap<String,Integer>)dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES);
            HashMap<String,Integer> tag2NumNeg = (HashMap<String,Integer>)dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES);
            for (int j = 0; j < tagNames[f].length; j++) {
                csvData[foldOffset + j][0] = tagNames[f][j];
                csvData[foldOffset + j][1] = "" + (f + 1);
                
                
                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> tag2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC);
                    csvData[foldOffset + j][s+2] = "" + tag2AUC_ROC.get(tagNames[f][j]).doubleValue();
                }
            }
            foldOffset += tagNames[f].length;
        }
        File AUC_ROC_fold_file = new File(outputDir.getAbsolutePath() + File.separator + "affinity_tag_fold_AUC_ROC.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(AUC_ROC_fold_file, "\t", csvData, false); 
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.err.println("Writing out tag AUC-ROC data averaged across folds");
        //Write out tag AUC-ROC data for significance testing
        HashSet<String> tagNamesSet = new HashSet<String>();
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> tag2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[0][i].getMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC);
            tagNamesSet.addAll(tag2AUC_ROC.keySet());
        }
        
        String[][] csvDataAveraged = new String[tagNamesSet.size()+1][systemNames.length + 1];    
        csvDataAveraged[0][0] = "tag";
        for (int i = 0; i < systemNames.length; i++) {
            csvDataAveraged[0][i+1] = systemNames[i];
        }
        int tagIdx = 1;
        for (Iterator<String> it = tagNamesSet.iterator(); it.hasNext();) {
            String tag = it.next();
            csvDataAveraged[tagIdx][0] = tag;
            for (int s = 0; s < systemNames.length; s++) {
                double avg = 0.0;
                for (int f = 0; f < numFolds; f++) {
                    HashMap<String, Double> tag2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC);
                    avg += tag2AUC_ROC.get(tag);
                }
                avg /= numFolds;
                csvDataAveraged[tagIdx][s+1] = "" + avg;
            }
            tagIdx++;
        }

        File AUC_ROC_file = new File(outputDir.getAbsolutePath() + File.separator + "affinity_tag_AUC_ROC.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(AUC_ROC_file, "\t", csvDataAveraged, false); 
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.err.println("Writing out per-track AUC-ROC data");
        //Write out clip AUC-ROC data for significance testing
        String[][] clipNames = new String[numFolds][];
        totalNumRows = 0;
        for (int i = 0; i < numFolds; i++) {
            HashMap<String, Double> clip2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[0][i].getMetadata(EvaluationDataObject.TAG_AFFINITY_CLIP_AUC_ROC);
            clipNames[i] = clip2AUC_ROC.keySet().toArray(new String[clip2AUC_ROC.size()]);
            totalNumRows += clipNames[i].length;
        }
        
        csvData = new String[totalNumRows+1][systemNames.length + 2];    
        csvData[0][0] = "clip";
        csvData[0][1] = "fold";

        for (int i = 0; i < systemNames.length; i++) {
            csvData[0][i+2] = systemNames[i];
        }
        foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            for (int c = 0; c < clipNames[f].length; c++) {
                csvData[foldOffset + c][0] = clipNames[f][c];
                csvData[foldOffset + c][1] = "" + (f + 1);
                
                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> clip2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_AFFINITY_CLIP_AUC_ROC);
                    if(!clip2AUC_ROC.containsKey(clipNames[f][c])){
                        System.out.println("Error! Results for system: " + systemNames[s] + " did not contain results for fold " + (f+1) + " clip " + clipNames[f][c] + "!");
                    }
                    
                    csvData[foldOffset + c][s+2] = "" + clip2AUC_ROC.get(clipNames[f][c]).doubleValue();
                }
            }
            foldOffset += clipNames[f].length;
        }
        File clip_AUC_ROC_file = new File(outputDir.getAbsolutePath() + File.separator + "affinity_clip_AUC_ROC.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(clip_AUC_ROC_file, "\t", csvData, false); 
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.err.println("Writing out overall report");
        //write overall report to file
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(outputDir.getAbsolutePath() + File.separator + "tag_affinity_report.txt"));
            textOut.write(report);
            textOut.newLine();
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        if (performMatlabStatSigTests){
            performFriedmanTestWithTag_AUC_ROC(outputDir, AUC_ROC_file, systemNames);
            performFriedmanTestWithTrack_AUC_ROC(outputDir,clip_AUC_ROC_file, systemNames);
        }

        System.err.println("done.");
        return report;
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

    
    private void performFriedmanTestWithTag_AUC_ROC(File outputDir,File AUC_ROC_file, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForAUC_ROC_TAG";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "affinity.AUC_ROC_TAG.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + AUC_ROC_file.getAbsolutePath() + "', '\t');");
            textOut.newLine();
            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("AUCROC_TAG = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(AUCROC_TAG));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(AUCROC_TAG(:,fliplr(sort_idx)),1,'on'); close(gcf)");
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
            textOut.write("set(gca,'xticklabel',algNames(fliplr(sort_idx)))");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + AUC_ROC_file.getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();

            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(matlabPath);
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void performFriedmanTestWithTrack_AUC_ROC(File outputDir,File AUC_ROC_file, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForAUC_ROC_TRACK";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "affinity.AUC_ROC_TRACK.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));
//
//            textOut.write("[data, result] = readtext('" + AUC_ROC_file.getAbsolutePath() + "', '\t');");
//            textOut.newLine();
//            textOut.write("algNames = data(1,3:" + (systemNames.length + 2) + ")';");
//            textOut.newLine();
//            textOut.write("[length,width] = size(data);");
//            textOut.newLine();
//            textOut.write("AUCROC_TRACK = cell2mat(data(2:length,3:" + (systemNames.length + 2) + "));");
//            textOut.newLine();
//            textOut.write("[P,friedmanTable,friedmanStats] = friedman(AUCROC_TRACK,1,'on');");
//            textOut.newLine();
//            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
//            textOut.newLine();
//            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
//            textOut.newLine();
//            textOut.write("exit;");
//            textOut.newLine();

            textOut.write("[data, result] = readtext('" + AUC_ROC_file.getAbsolutePath() + "', '\t');");
            textOut.newLine();
            textOut.write("algNames = data(1,3:" + (systemNames.length + 2) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("AUCROC_TAG = cell2mat(data(2:length,3:" + (systemNames.length + 2) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(AUCROC_TAG));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(AUCROC_TAG(:,fliplr(sort_idx)),1,'on'); close(gcf)");
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
            textOut.write("set(gca,'xticklabel',algNames(fliplr(sort_idx)))");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + AUC_ROC_file.getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();
            
            
            
            
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(matlabPath);
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
//    private void performFriedManTestWith_tag_AUC_ROC(File outputDir, File AUC_ROC_file, String[] systemNames) {
//        //call matlab and execute Friedman's test with TK HSD
//        //make sure readtext is in the working directory for matlab
//        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
//        //create an m-file to run the test
//        String evalCommand = "performFriedmanForTags";
//        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
//        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "affinity_AUC_ROC.friedman.tukeyKramerHSD.png";
//        try {
//            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));
//
//            textOut.write("[data, result] = readtext('" + AUC_ROC_file.getAbsolutePath() + "', '\t')");
//            textOut.newLine();
//            textOut.write("AUC_ROC_Scores = data(:,5:" + (systemNames.length + 4) + ");");
//            textOut.newLine();
//            textOut.write("[P,friedmanTable,friedmanStats] = friedman(AUC_ROC_Scores,1,'on');");
//            textOut.newLine();
//            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
//            textOut.newLine();
//            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
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
//        //delete readtext.m
//        readtextMFile.delete();
//    }
    
//    private void performFriedManTestWith_clip_AUC_ROC(File outputDir, File AUC_ROC_file, String[] systemNames) {
//        //call matlab and execute Friedman's test with TK HSD
//        //make sure readtext is in the working directory for matlab
//        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
//        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
//        //create an m-file to run the test
//        String evalCommand = "performFriedmanForClips";
//        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
//        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "affinity_clip_AUC_ROC.friedman.tukeyKramerHSD.png";
//        try {
//            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));
//
//            textOut.write("[data, result] = readtext('" + AUC_ROC_file.getAbsolutePath() + "', '\t')");
//            textOut.newLine();
//            textOut.write("clip_AUC_ROC_Scores = data(:,5:" + (systemNames.length + 4) + ");");
//            textOut.newLine();
//            textOut.write("[P,friedmanTable,friedmanStats] = friedman(clip_AUC_ROC_Scores,1,'on');");
//            textOut.newLine();
//            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
//            textOut.newLine();
//            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
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
//        //delete readtext.m
//        readtextMFile.delete();
//    }
    
    
    public static void main(String[] args) {
        String systemName = args[0];
        File resultFile = new File(args[1]);
        File gtFile = new File(args[2]);
        File outputDirectory = new File(args[3]);
        
        TagClassificationBinaryFileReader binReader = new TagClassificationBinaryFileReader();
        TagClassificationAffinityFileReader affReader = new TagClassificationAffinityFileReader();
        TagClassificationAffinityEvaluator evaluator = new TagClassificationAffinityEvaluator();
        evaluator.setVerbose(true);
        evaluator.evaluateResultsAgainstGT(systemName, affReader.readFile(resultFile), binReader.readFile(gtFile), outputDirectory);
    }
}
