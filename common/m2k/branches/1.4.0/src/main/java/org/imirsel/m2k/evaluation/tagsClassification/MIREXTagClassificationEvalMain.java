/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.tagsClassification;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.imirsel.m2k.evaluation.DataObj;

/**
 *
 * @author kris
 */
public class MIREXTagClassificationEvalMain {

    public static final String BINARY_FILE_EXTENSION = ".bin";
    public static final String AFFINITY_FILE_EXTENSION = ".aff";
    
    public static final String USAGE = "args: /path/to/GT/file /path/to/output/dir /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemName [-s /path/to/subsetOfQueriesToEvaluate]";
    public static void main(String[] args) {
        
        System.out.println("MIREX Audio Tag classification evaluatorevaluator\n" +
                "\t\tby Kris West (kris.west@gmail.com");
        System.out.println("");
        
        if (args.length < 4){
            System.out.println("ERROR: Insufficient arguments!\n" + USAGE);
            System.exit(1);
        }
        
        File gtFile = new File(args[0]);
        
        File rootEvaluationDir = new File(args[1]);
        rootEvaluationDir.mkdirs();
        if (args.length % 2 != 0){
            System.out.println("WARNING: an odd number of arguments was specified!\n" + USAGE);
        }
        
        ArrayList<String> systemNames = new ArrayList<String>();
        ArrayList<File> resultsDirs = new ArrayList<File>();
        
        File subsetFile = null;
        
        System.out.println("---");
        for (int i = 2; i < args.length; i+=2) {
            if (args[i].equalsIgnoreCase("-s")){
                subsetFile = new File(args[i+1]);
            }else{
                File resultsPath = new File(args[i]);
                String systemName = args[i+1];
                systemNames.add(systemName);
                resultsDirs.add(resultsPath);
                System.out.println("System " + systemNames.size()+ "; " + systemName + ", " + resultsPath.getAbsolutePath());
            }
        }

        boolean doStatsTests = true;
        if (systemNames.size() == 1){
            System.out.println("Not performing statistical comparison as only one system was specified as an argument");
            doStatsTests = false;
        }
        
        ArrayList<String> affinitySystemNames = (ArrayList<String>)systemNames.clone();
        
        //evaluate a subset of possible queries?
        HashSet<String> subset = null;
        if(subsetFile != null){
            //load subset
            System.out.println("Loading subset of queries to evaluate from: " + subsetFile.getAbsolutePath());
            TagClassificationQuerySubsetReader subsetReader = new TagClassificationQuerySubsetReader();
            subsetReader.setMIREX_submissionMode(true);
            subsetReader.setVerbose(true);
            subset = subsetReader.readFile(subsetFile);
        }else{
            System.out.println("Not loading a query subset, evaluating all available queries");
        }
        
        System.out.println("---");
        
        //get each directory of results
        System.out.println("Determining location of binary and affinity evaluation files for each system for each experiment fold...");
        ArrayList<ArrayList<File>> binaryResultsFilesPerSystemPerFold = new ArrayList<ArrayList<File>>();
        ArrayList<ArrayList<File>> affinityResultsFilesPerSystemPerFold = new ArrayList<ArrayList<File>>();
        int numFolds = -1;
        int systemIdx = 0;
        for (Iterator<File> it = resultsDirs.iterator(); it.hasNext();) {
            File dir = it.next();
            
            if (!dir.exists()){
                System.out.println("ERROR: Result directory (" + dir.getAbsolutePath() + ") did not exist\n" + USAGE);
                System.exit(1);
            }
            
            File[] files = dir.listFiles();
            //this should sort results consistenly across all submissions, 
            //   if they use the same names for their results files 
            //   (otherwise there is no way to know if they are about the same test across different submissions)
            Arrays.sort(files);
            ArrayList<File> binaryFiles = new ArrayList<File>();
            ArrayList<File> affinityFiles = new ArrayList<File>();
            
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(BINARY_FILE_EXTENSION) || files[i].getName().endsWith(BINARY_FILE_EXTENSION + ".txt")){
                    binaryFiles.add(files[i]);
                }else if(files[i].getName().endsWith(AFFINITY_FILE_EXTENSION) || files[i].getName().endsWith(AFFINITY_FILE_EXTENSION + ".txt")){
                    affinityFiles.add(files[i]);
                }else{
                    System.out.println("ignoring unrecognized file: " + files[i].getAbsolutePath());
                }
            }
            binaryResultsFilesPerSystemPerFold.add(binaryFiles);
            
            //check that all systems have the same number of results
            if (numFolds == -1){
                numFolds = binaryFiles.size();
            }else if(numFolds != binaryFiles.size()){
                System.out.println("ERROR: The number of folds (" + binaryFiles.size() + ") detected " +
                        "in directory: " + dir.getAbsolutePath() + " for binary result files is not equal to the number detected " +
                        "for the preceeding systems (" + numFolds + ")!");
                System.exit(1);
            }
            
            if (affinityFiles.size() == 0){
                System.out.println("WARNING: No affinity data files detected for result dir: " + dir.getAbsolutePath());
                //affinityResultsFilesPerSystemPerFold.add(new ArrayList<File>());
                affinitySystemNames.remove(systemIdx);
                systemIdx--;
            }else if ((numFolds != 1)&&(affinityFiles.size() != numFolds)){
                
                System.out.println("WARNING: Wrong number (" + affinityFiles.size() + ") of affinity data files detected " +
                        "for result dir: " + dir.getAbsolutePath() + ", should be " + numFolds + "\n" +
                        "skipping affinity evaluation.");
                //affinityResultsFilesPerSystemPerFold.add(new ArrayList<File>());
                affinitySystemNames.remove(systemIdx);
                systemIdx--;
            }else{
                affinityResultsFilesPerSystemPerFold.add(affinityFiles);
            }
            systemIdx++;
        }
        
        //read GT file
        System.out.println("reading ground-truth data file...");
        TagClassificationBinaryFileReader gtReader = new TagClassificationBinaryFileReader();
        DataObj GT = gtReader.readFile(gtFile);
        
        System.out.println("reading binary result data files...");
        //read each binary result file and create DataObj arrays
        TagClassificationBinaryFileReader binReader = new TagClassificationBinaryFileReader();
        binReader.setMIREX_submissionMode(true);
        binReader.setVerbose(false);
        DataObj[][] resultData = new DataObj[systemNames.size()][numFolds];
        HashSet<String>[] masterPathListPerFold = new HashSet[numFolds];
        for (int i = 0; i < masterPathListPerFold.length; i++) {
            masterPathListPerFold[i] = new HashSet<String>();
            
        }
        for (int i = 0; i < systemNames.size(); i++) {
            ArrayList<File> fileList = binaryResultsFilesPerSystemPerFold.get(i);
            for (int j = 0; j <numFolds; j++) {
                resultData[i][j] = binReader.readFile(fileList.get(j));              
                masterPathListPerFold[j].addAll(((HashMap<String,HashSet<String>>)resultData[i][j].getMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP)).keySet());
            }
        }
        
        //read each affinity result file and create DataObj arrays
        System.out.println("reading affinity result data files...");
        TagClassificationAffinityFileReader affReader = new TagClassificationAffinityFileReader();
        affReader.setMIREX_submissionMode(true);
        affReader.setVerbose(false);
        ArrayList<DataObj[]> affResultData = new ArrayList<DataObj[]>();
        for (int i = 0; i < affinitySystemNames.size(); i++) {
            ArrayList<File> fileList = affinityResultsFilesPerSystemPerFold.get(i);
            
            DataObj[] affSysResults = new DataObj[numFolds];
            for (int j = 0; j < numFolds; j++) {
                affSysResults[j] = affReader.readFile(fileList.get(j));   
                masterPathListPerFold[j].addAll(((HashMap<String,HashMap<String,Double>>)affSysResults[j].getMetadata(DataObj.TAG_AFFINITY_MAP)).keySet());
            }
            affResultData.add(affSysResults);
        }
        
        
        //patch in tracks with missing classifications
        System.out.println("searching for result sets with missing paths (no classifications or affinities)");
        for (int i = 0; i < systemNames.size(); i++) {
            for (int j = 0; j < numFolds; j++) {
                HashSet<String> missing = (HashSet<String>)masterPathListPerFold[j].clone();
                HashMap<String,HashSet<String>> path2tagList = (HashMap<String,HashSet<String>>)resultData[i][j].getMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP);
                missing.removeAll(path2tagList.keySet());
                if (subset != null){
                    missing.retainAll(subset);
                }
                if (!missing.isEmpty()){
                    System.out.println("\tpatching in " + missing.size() + " paths (as no classification were returned) for: " + systemNames.get(i) + ", fold " + (j+1));
                    for (Iterator<String> it = missing.iterator(); it.hasNext();) {
                        path2tagList.put(it.next(), new HashSet<String>());
                    }        
                }
            }
        }
        for (int i = 0; i < affinitySystemNames.size(); i++) {
            for (int j = 0; j < numFolds; j++) {
                HashSet<String> missing = (HashSet<String>)masterPathListPerFold[j].clone();
                HashMap<String,HashMap<String,Double>> path2tagAffList = (HashMap<String,HashMap<String,Double>>)affResultData.get(i)[j].getMetadata(DataObj.TAG_AFFINITY_MAP);
                missing.removeAll(path2tagAffList.keySet());
                if (subset != null){
                    missing.retainAll(subset);
                }
                if (!missing.isEmpty()){
                    System.out.println("\tpatching in " + missing.size() + " paths (as no affinities were returned) for: " + systemNames.get(i) + ", fold " + (j+1));
                    for (Iterator<String> it = missing.iterator(); it.hasNext();) {
                        path2tagAffList.put(it.next(), new HashMap<String,Double>());
                    }        
                }
            }
        }
        
        
        //run binary evaluation
        System.out.println("performing binary relevance evaluation...");
        TagClassificationBinaryEvaluator binEval = new TagClassificationBinaryEvaluator();
        if (subset != null){
            binEval.setQuery_subset(subset);
        }
        binEval.setVerbose(true);
        binEval.setPerformMatlabStatSigTests(doStatsTests);
        binEval.evaluate(systemNames.toArray(new String[systemNames.size()]), 
                resultData, GT, rootEvaluationDir);
        
        //run affinity evaluation
        System.out.println("performing tag affinity evaluation...");
        TagClassificationAffinityEvaluator affEval = new TagClassificationAffinityEvaluator();
        if (subset != null){
            affEval.setQuery_subset(subset);
        }
        affEval.setVerbose(true);
        affEval.setPerformMatlabStatSigTests(doStatsTests);
        affEval.evaluate(affinitySystemNames.toArray(new String[affinitySystemNames.size()]), 
                (DataObj[][])affResultData.toArray(new DataObj[affResultData.size()][]), 
                GT, rootEvaluationDir);
        
        
        System.out.println("Preparing results pages...");
        
//        
//        dataToEvaluate.setMetadata(DataObj.SYSTEM_RESULTS_REPORT, systemReport);
//        
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_ACCURACY_MAP, tag2Accuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_POS_ACCURACY_MAP, tag2PosAccuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_NEG_ACCURACY_MAP, tag2NegAccuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_PRECISION_MAP, tag2Precision);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_RECALL_MAP, tag2Recall);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_FMEASURE_MAP, tag2FMeasure);
//        
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_ACCURACY_MAP, track2Accuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_POS_ACCURACY_MAP, track2PosAccuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_NEG_ACCURACY_MAP, track2NegAccuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_PRECISION_MAP, track2Precision);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_RECALL_MAP, track2Recall);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_TRACK_FMEASURE_MAP, track2FMeasure);
//        
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_ACCURACY, totalAccuracy);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_PRECISION, totalPrecision);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_RECALL, totalRecall);
//        dataToEvaluate.setMetadata(DataObj.TAG_BINARY_OVERALL_FMEASURE, totalFmeasure);
//        
//        dataToEvaluate.setMetadata(DataObj.TAG_NUM_POSITIVE_EXAMPLES, tag2numPositiveExamples);
//        dataToEvaluate.setMetadata(DataObj.TAG_NUM_NEGATIVE_EXAMPLES, tag2numNegativeExamples);
//        
        
        
        
        System.out.println("---exit---");
        
    }
}
