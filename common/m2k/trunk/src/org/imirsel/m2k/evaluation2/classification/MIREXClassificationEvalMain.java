/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification;

import org.imirsel.m2k.evaluation2.tagsClassification.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.imirsel.m2k.util.Signal;

/**
 *
 * @author kris
 */
public class MIREXClassificationEvalMain {

    
    public static final String USAGE = "args: /path/to/GT/file /path/to/output/dir /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    public static void main(String[] args) {
        
        System.out.println("MIREX 2008 Classification evaluator\n" +
                "\t\tby Kis West (kris.west@gmail.com");
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
        ArrayList<ArrayList<File>> resultsFilesPerSystemPerFold = new ArrayList<ArrayList<File>>();
        int numFolds = -1;
        for (Iterator<File> it = resultsDirs.iterator(); it.hasNext();) {
            File dir = it.next();
            File[] files = dir.listFiles();
            //this should sort results consistenly across all submissions, 
            //   if they use the same names for their results files 
            //   (otherwise there is no way to know if they are about the same test across different submissions)
            Arrays.sort(files);
            ArrayList<File> resultFiles = new ArrayList<File>();
            
            for (int i = 0; i < files.length; i++) {
                resultFiles.add(files[i]);
            }
            resultsFilesPerSystemPerFold.add(resultFiles);
            
            //check that all systems have the same number of results
            if (numFolds == -1){
                numFolds = resultFiles.size();
            }else if(numFolds != resultFiles.size()){
                System.out.println("ERROR: The number of folds (" + resultFiles.size() + ") detected " +
                        "in directory: " + dir.getAbsolutePath() + " for result files is not equal to the number detected " +
                        "for the preceeding systems (" + numFolds + ")!");
                System.exit(1);
            }
        }
        
        System.out.println("reading result data files...");
        //read each binary result file and create EvaluationDataObject arrays
        Signal[][][] resultData = new Signal[systemNames.size()][numFolds][/*numExamples for that fold*/];
        for (int i = 0; i < systemNames.size(); i++) {
            ArrayList<File> fileList = resultsFilesPerSystemPerFold.get(i);
            for (int j = 0; j <numFolds; j++) {
                resultData[i][j] = ClassificationResultReadClass.readClassificationFileAsSignals(fileList.get(j), gtFile);                
            }
        }
        
        //run SignalArrayAccuracyClass2 on each system
        
        //plot confusion matrices
        
        //prep friedman test data
        
        //run friedman est if matlab available?
        
        
        
        
        
//        //run binary evaluation
//        System.out.println("performing binary relevance evaluation...");
//        TagClassificationBinaryEvaluator binEval = new TagClassificationBinaryEvaluator();
//        binEval.setVerbose(true);
//        binEval.setPerformMatlabStatSigTests(true);
//        binEval.evaluate((String[])systemNames.toArray(new String[systemNames.size()]), 
//                resultData, GT, rootEvaluationDir);
//        
        
        System.out.println("---exit---");
        
    }
}
