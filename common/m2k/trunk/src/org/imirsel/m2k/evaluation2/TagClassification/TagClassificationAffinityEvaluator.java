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

    

    class AffinityDataPoint implements Comparable{
        boolean tagApplies;
        double affinity;

        public AffinityDataPoint(boolean tagApplies, double affinity) {
            this.tagApplies = tagApplies;
            this.affinity = affinity;
        }

        public int compareTo(Object o) {
            //sort into descending order
            return -Double.compare(affinity, ((AffinityDataPoint)o).affinity);
        }
    }
    
    private void addROCpoint(ArrayList<double[]> ROCpointSequence, double falsePosRate, double truePosRate){
        ROCpointSequence.add(new double[]{falsePosRate,truePosRate});
    }
    
    private double computeAreaUnderROCCurve(ArrayList<double[]> ROCpointSequence){
        double[] last = ROCpointSequence.get(0);
        double[] curr;
        double area = 0.0;
        for (int i = 1; i < ROCpointSequence.size(); i++) {
            curr = ROCpointSequence.get(1);
            area += trapezoidArea(last[0], curr[0], last[1], curr[1]);
            last = curr;
        }
        return area;
    }
    
    private double trapezoidArea(double x1, double x2, double y1, double y2){
        return ((y1 + y2)/2.0) * (x2 - x1);
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
        HashMap<String, HashMap<String,Double>> path2tag2affinity = (HashMap<String, HashMap<String,Double>>) dataToEvaluate.getMetadata(EvaluationDataObject.TAG_AFFINITY_MAP);
        HashMap<String, HashSet<String>> GT_binaryTagData = (HashMap<String, HashSet<String>>) groundTruth.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        if (!GT_binaryTagData.keySet().containsAll(path2tag2affinity.keySet())) {
            throw new RuntimeException("The groundtruth Object representing " + groundTruth.getFile().getAbsolutePath() + ", does not have ground truth" +
                    "for all the paths specified in " + dataToEvaluate.getFile().getAbsolutePath());
        }
        if (!path2tag2affinity.keySet().containsAll(GT_binaryTagData.keySet())) {
            System.out.println("WARNING: The result object representing: " + groundTruth.getFile().getAbsolutePath() + ", does not have result data for " +
                    "all the paths specified in the ground truth file: " + dataToEvaluate.getFile().getAbsolutePath());
        }
        
        //prepare the output directory
        File systemDirectory = new File(outputDir.getAbsolutePath() + File.separator + systemName);
        systemDirectory.mkdirs();
        File plotDir = new File(systemDirectory.getAbsolutePath() + File.separator + "ROC curve plots");
        plotDir.mkdir();
        
        //get list of all tags
        HashSet<String> tags = new HashSet<String>();
        for (Iterator<String> it = GT_binaryTagData.keySet().iterator(); it.hasNext();) {
            HashSet tagSet = GT_binaryTagData.get(it.next());
            tags.addAll(tagSet);
        }
        
        //result objects
        HashMap<String, Double> tag2AUC_ROC = new HashMap<String, Double>();
        HashMap<String, ArrayList<double[]>> tag2ROCpointSequence = new HashMap<String, ArrayList<double[]>>();
        HashMap<String, ArrayList<AffinityDataPoint>> tag2affinityDataPoints = new HashMap<String, ArrayList<AffinityDataPoint>>();
        for (Iterator<String> it = tags.iterator(); it.hasNext();) {
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
        
        //step through all paths in the results and populate the tag2affinityDataPoints map
        for (Iterator<String> pathIter = path2tag2affinity.keySet().iterator(); pathIter.hasNext();) {
            path = pathIter.next();
            returnedAffinities = path2tag2affinity.get(path);
            trueSet = GT_binaryTagData.get(path);

            for (Iterator<String> it = returnedAffinities.keySet().iterator(); it.hasNext();) {
                tag = it.next();
                tag2affinityDataPoints.get(tag).add(new AffinityDataPoint(trueSet.contains(path), returnedAffinities.get(tag)));
            }
            //fill in missing affinities
            missingAffinities = (HashSet<String>)tags.clone();
            missingAffinities.removeAll(returnedAffinities.keySet());
            for (Iterator<String> it = missingAffinities.iterator(); it.hasNext();) {
                tag = it.next();
                tag2affinityDataPoints.get(tag).add(new AffinityDataPoint(trueSet.contains(path), Double.NEGATIVE_INFINITY));
            }
        }
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_TAG_AFFINITY_DATAPOINTS, tag2affinityDataPoints);

        for (Iterator<String> it = tag2affinityDataPoints.keySet().iterator(); it.hasNext();) {
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
            for (Iterator<AffinityDataPoint> it2 = dataPointList.iterator(); it2.hasNext();) {
                dataPoint = it2.next();
                if (dataPoint.affinity != lastAffinity){
                    addROCpoint(anROCpointSequence, (falsePositives/negatives), (truePositives/positives));
                }
                if (dataPoint.tagApplies){
                    truePositives++;
                }else{
                    falsePositives++;
                }
            }
            addROCpoint(anROCpointSequence, (falsePositives/negatives), (truePositives/positives));
            
            systemReport += "Tag: " + tag + "\n";
            systemReport += "ROC curve (false pos,true pos): ";
            for (Iterator<double[]> it2 = anROCpointSequence.iterator(); it2.hasNext();) {
                ds = it2.next();
                systemReport += ds[0] + "," + ds[1] + "\t";
            }
            
            //plot ROC curve and save
            ROCdomain = new double[anROCpointSequence.size()];
            ROCrange = new double[anROCpointSequence.size()];
            idx = 0;
            for (Iterator<double[]> it2 = anROCpointSequence.iterator(); it2.hasNext();) {
                ds = it2.next();
                ROCdomain[idx] = ds[0];
                ROCrange[idx++] = ds[1];
            }
            SimpleNumericPlot tagROCPlot = new SimpleNumericPlot(false, false, dataToEvaluate.getFile().getName() + "," + tag + " ROC curve", 
                    "tag", null, "True postive rate", "False positive rate", ROCdomain, ROCrange, null);
            File plotFile = new File(plotDir.getAbsolutePath() + File.separator + tag + ".png");
            tagROCPlot.writeChartToFile(plotFile,850,1000);
            
            
            //compute AUC-ROC 
            double auc = computeAreaUnderROCCurve(anROCpointSequence);
            tag2AUC_ROC.put(tag, auc);
        }
        
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_AFFINITY_ROC_DATA,tag2ROCpointSequence);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES, tag2numPositiveExamples);
        dataToEvaluate.setMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES, tag2numNegativeExamples);
        
        
        //report on files evaluated against
        systemReport += "Number of files tested against: " + path2tag2affinity.size() + "\n";
        systemReport += "Test file paths: " + path2tag2affinity.size() + "\n";
        String[] paths = path2tag2affinity.keySet().toArray(new String[path2tag2affinity.size()]);
        Arrays.sort(paths);
        for (int j = 0; j < paths.length; j++) {
            systemReport += "\t" + paths[j] + "\n";
        }
        systemReport += "  ---   \n";

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
            HashMap<String, Double> tag2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[0][i].getMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC);
            tagNames[i] = tag2AUC_ROC.keySet().toArray(new String[tag2AUC_ROC.size()]);
            totalNumRows += tagNames[i].length;
        }
        
        String[][] csvData = new String[totalNumRows+1][systemNames.length + 4];    
        csvData[0][0] = "tag";
        csvData[0][1] = "fold";
        csvData[0][2] = "positive examples";
        csvData[0][3] = "negative examples";
        
        for (int i = 0; i < systemNames.length; i++) {
            csvData[0][i+4] = systemNames[i];
        }
        int foldOffset = 1;
        for (int f = 0; f < numFolds; f++) {
            HashMap<String,Integer> tag2NumPos = (HashMap<String,Integer>)dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_POSITIVE_EXAMPLES);
            HashMap<String,Integer> tag2NumNeg = (HashMap<String,Integer>)dataToEvaluate[0][f].getMetadata(EvaluationDataObject.TAG_NUM_NEGATIVE_EXAMPLES);
            for (int j = 0; j < tagNames[f].length; j++) {
                csvData[foldOffset + j][0] = tagNames[f][j];
                csvData[foldOffset + j][1] = "" + (f + 1);
                csvData[foldOffset + j][2] = "" + tag2NumPos.get(tagNames[f][j]);
                csvData[foldOffset + j][3] = "" + tag2NumNeg.get(tagNames[f][j]);
                
                
                for (int s = 0; s < systemNames.length; s++) {
                    HashMap<String, Double> tag2AUC_ROC =  (HashMap<String, Double>)dataToEvaluate[s][f].getMetadata(EvaluationDataObject.TAG_AFFINITY_AUC_ROC);
                    csvData[foldOffset + j][s+4] = "" + tag2AUC_ROC.get(tagNames[f][j]).doubleValue();
                }
            }
            foldOffset += tagNames[f].length;
        }
        File AUC_ROC_file = new File(outputDir.getAbsolutePath() + File.separator + "affinity_AUC_ROC.csv");
        try {
            DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(AUC_ROC_file, "\t", csvData, true); 
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
            //delete readtext.m
            performFriedManTestWith_tag_AUC_ROC(outputDir, AUC_ROC_file, systemNames);
            
            //  call matlab and execute Beta-Binomial test
            
            
        }
        
        
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

    
    private void performFriedManTestWith_tag_AUC_ROC(File outputDir, File AUC_ROC_file, String[] systemNames) {
        //call matlab and execute Friedman's test with TK HSD
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("org/imirsel/m2k/evaluation2/TagClassification/resources/readtext.m", readtextMFile);
        //create an m-file to run the test
        String evalCommand = "performFriedmanForTags";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "affinity_AUC_ROC.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + AUC_ROC_file.getAbsolutePath() + "', '\t')");
            textOut.newLine();
            textOut.write("AUC_ROC_Scores = data(:,5:" + (systemNames.length + 4) + ");");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(AUC_ROC_Scores,1,'on');");
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
        //delete readtext.m
        readtextMFile.delete();

        //  call matlab and execute Beta-Binomial test
    }
    
}
