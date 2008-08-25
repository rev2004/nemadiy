/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.TagClassification;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.imirsel.m2k.evaluation2.EvaluationDataObject;

/**
 *
 * @author kris
 */
public class MIREXTagClassificationEvalMain {

    public static final String BINARY_FILE_EXTENSION = ".bin";
    public static final String AFFINITY_FILE_EXTENSION = ".aff";
    
    public static final String USAGE = "args: /path/to/GT/file /path/to/output/dir /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    public static void main(String[] args) {
        
        System.out.println("MIREX 2008 Audio Tag classification evaluator\n" +
                "\t\tby Kis West (kris.west@gmail.com\n" +
                "\t\tBeta Binomial test by Michael Mandel (mim@ee.columbia.edu)");
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
        
        System.out.println("---");
        for (int i = 2; i < args.length; i+=2) {
            String systemName = args[i+1];
            File resultsPath = new File(args[i]);
            systemNames.add(systemName);
            resultsDirs.add(resultsPath);
            System.out.println("System " + systemNames.size()+ "; " + systemName + ", " + resultsPath.getAbsolutePath());
        }
        System.out.println("---");
        
        //get each directory of results
        System.out.println("Determining location of binary and affinity evaluation files for each system for each experiment fold...");
        ArrayList<ArrayList<File>> binaryResultsFilesPerSystemPerFold = new ArrayList<ArrayList<File>>();
        ArrayList<ArrayList<File>> affinityResultsFilesPerSystemPerFold = new ArrayList<ArrayList<File>>();
        int numFolds = -1;
        for (Iterator<File> it = resultsDirs.iterator(); it.hasNext();) {
            File dir = it.next();
            File[] files = dir.listFiles();
            //this should sort results consistenly across all submissions, 
            //   if they use the same names for their results files 
            //   (otherwise there is no way to know if they are about the same test across different submissions)
            Arrays.sort(files);
            ArrayList<File> binaryFiles = new ArrayList<File>();
            ArrayList<File> affinityFiles = new ArrayList<File>();
            
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(BINARY_FILE_EXTENSION)){
                    binaryFiles.add(files[i]);
                }else if(files[i].getName().endsWith(AFFINITY_FILE_EXTENSION)){
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
                affinityResultsFilesPerSystemPerFold.add(new ArrayList<File>());
            }else if ((numFolds != 1)&&(affinityFiles.size() != numFolds)){
                
                System.out.println("WARNING: Wrong number (" + affinityFiles.size() + ") of affinity data files detected " +
                        "for result dir: " + dir.getAbsolutePath() + ", should be " + numFolds + "\n" +
                        "skipping affinity evaluation.");
                affinityResultsFilesPerSystemPerFold.add(new ArrayList<File>());
            }else{
                affinityResultsFilesPerSystemPerFold.add(affinityFiles);
            }
        }
        
        //read GT file
        System.out.println("reading ground-truth data file...");
        TagClassificationBinaryFileReader gtReader = new TagClassificationBinaryFileReader();
        EvaluationDataObject GT = gtReader.readFile(gtFile);
        
        System.out.println("reading binary result data files...");
        //read each binary result file and create EvaluationDataObject arrays
        TagClassificationBinaryFileReader binReader = new TagClassificationBinaryFileReader();
        binReader.setMIREX_submissionMode(true);
        EvaluationDataObject[][] resultData = new EvaluationDataObject[systemNames.size()][numFolds];
        for (int i = 0; i < systemNames.size(); i++) {
            ArrayList<File> fileList = binaryResultsFilesPerSystemPerFold.get(i);
            for (int j = 0; j <numFolds; j++) {
                resultData[i][j] = binReader.readFile(fileList.get(j));                
            }
        }
        
        //run binary evaluation
        System.out.println("performing binary relevance evaluation...");
        TagClassificationBinaryEvaluator binEval = new TagClassificationBinaryEvaluator();
        binEval.setVerbose(true);
        binEval.setPerformMatlabStatSigTests(true);
        binEval.evaluate((String[])systemNames.toArray(new String[systemNames.size()]), 
                resultData, GT, rootEvaluationDir);
        
        
        //read each affinity result file and create EvaluationDataObject arrays
        System.out.println("reading affinity result data files...");
        TagClassificationAffinityFileReader affReader = new TagClassificationAffinityFileReader();
        affReader.setMIREX_submissionMode(true);
        ArrayList<EvaluationDataObject[]> affResultData = new ArrayList<EvaluationDataObject[]>();
        for (int i = 0; i < systemNames.size(); i++) {
            ArrayList<File> fileList = affinityResultsFilesPerSystemPerFold.get(i);
            if (fileList.size() == 0){
                //skip system
                systemNames.remove(i);
                continue;
            }
            
            EvaluationDataObject[] affSysResults = new EvaluationDataObject[numFolds];
            for (int j = 0; j < numFolds; j++) {
                affSysResults[j] = affReader.readFile(fileList.get(j));                
            }
            affResultData.add(affSysResults);
        }
        
        //run affinity evaluation
        System.out.println("performing tag affinity evaluation...");
        TagClassificationAffinityEvaluator affEval = new TagClassificationAffinityEvaluator();
        affEval.setVerbose(true);
        affEval.setPerformMatlabStatSigTests(true);
        affEval.evaluate((String[])systemNames.toArray(new String[systemNames.size()]), 
                (EvaluationDataObject[][])affResultData.toArray(new EvaluationDataObject[affResultData.size()][]), 
                GT, rootEvaluationDir);
        
        System.out.println("---exit---");
        
    }
}
