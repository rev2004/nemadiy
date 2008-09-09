/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.evaluation2.tagsClassification.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.util.MatlabCommandlineIntegrationClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kris
 */
public class MIREXClassificationEvalMain {

    

    private boolean performMatlabStatSigTests = true;
    private String matlabPath = "matlab";
    private String evaluationName;
    private File gtFile;
    private File rootEvaluationDir;
    private ArrayList<String> systemNames;
    private ArrayList<File> resultsDirs;
    
    public MIREXClassificationEvalMain(boolean performMatlabStatSigTests_,
            String matlabPath_,
            String evaluationName_,
            File gtFile_,
            File rootEvaluationDir_,
            ArrayList<String> systemNames_,
            ArrayList<File> resultsDirs_){
        
        performMatlabStatSigTests = performMatlabStatSigTests_;
        matlabPath = matlabPath_;
        evaluationName = evaluationName_;
        gtFile = gtFile_;
        rootEvaluationDir = rootEvaluationDir_;
        systemNames = systemNames_;
        resultsDirs = resultsDirs_;
    }
    
    public MIREXClassificationEvalMain(){
        
    }
    
    public void parseCommandLineArgs(String[] args){
        if (args.length < 5){
            System.out.println("ERROR: Insufficient arguments!\n" + USAGE);
            System.exit(1);
        }
        
        evaluationName = args[0];
        
        gtFile = new File(args[1]);
        
        rootEvaluationDir = new File(args[2]);
        rootEvaluationDir.mkdirs();
        if (args.length % 2 != 1){
            System.out.println("WARNING: an even number of arguments was specified, one may have been ignored!\n" + USAGE);
        }
        
        systemNames = new ArrayList<String>();
        resultsDirs = new ArrayList<File>();
        
        System.out.println("---");
        for (int i = 3; i < args.length; i+=2) {
            String systemName = args[i+1];
            File resultsPath = new File(args[i]);
            systemNames.add(systemName);
            resultsDirs.add(resultsPath);
            System.out.println("System " + systemNames.size()+ "; " + systemName + ", " + resultsPath.getAbsolutePath());
        }
        System.out.println("---");
    }
    
    public static final String USAGE = "args: evaluationName /path/to/GT/file /path/to/output/dir /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    public static void main(String[] args) {
        
        System.out.println("MIREX 2008 Classification evaluator\n" +
                "\t\tby Kris West (kris.west@gmail.com");
        System.out.println("");
        
        MIREXClassificationEvalMain eval = new MIREXClassificationEvalMain();
        eval.parseCommandLineArgs(args);
        
        eval.performEvaluation();
        
        System.out.println("---exit---");
        
    }
    
    public void performEvaluation() {

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
            if (numFolds == -1) {
                numFolds = resultFiles.size();
            } else if (numFolds != resultFiles.size()) {
                System.out.println("ERROR: The number of folds (" + resultFiles.size() + ") detected " + "in directory: " + dir.getAbsolutePath() + " for result files is not equal to the number detected " + "for the preceeding systems (" + numFolds + ")!");
                System.exit(1);
            }
        }

        System.out.println("reading result data files...");
        //read each binary result file and create EvaluationDataObject arrays
        Signal[][][] resultData = new Signal[systemNames.size()][numFolds][];
        for (int i = 0; i < systemNames.size(); i++) {
            ArrayList<File> fileList = resultsFilesPerSystemPerFold.get(i);
            for (int j = 0; j < numFolds; j++) {
                resultData[i][j] = ClassificationResultReadClass.readClassificationFileAsSignals(fileList.get(j), gtFile);
            }
        }

        System.out.println("Performing individual classification evals...");
        //run SignalArrayAccuracyClass2 on each system
        File[] resultFiles = new File[resultData.length];
        ArrayList<Signal[]> resultSignals = new ArrayList(resultData.length);
        for (int i = 0; i < resultData.length; i++) {
            SignalArrayAccuracyClass2 evaluator = new SignalArrayAccuracyClass2(systemNames.get(i), ".eval.txt", ".evalData.ser", rootEvaluationDir.getAbsolutePath(), null, true);
            for (int j = 0; j < numFolds; j++) {
                try {
                    resultSignals.add(evaluator.evaluate(resultData[i][j]));
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (noMetadataException ex) {
                    Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            resultFiles[i] = evaluator.getReportFile();
        }


        //plot confusion matrices
        System.out.println("Plotting overall confusion matrices...");
        ParseAndPlotConfusionMatrix confPlotter = new ParseAndPlotConfusionMatrix();
        for (int i = 0; i < resultFiles.length; i++) {
            try {
                confPlotter.loadPlotAndSaveConfusion(resultFiles[i], new File(resultFiles[i].getAbsolutePath() + ".conf.png"));
            } catch (IOException ex) {
                Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        System.out.println("Writing out CSV result files over whole task...");
        //prep result test data CSV file over classes and folds
        File perClassFoldCSV = PrepClassificationFriedmanTestDataClass.prepFriedmanTestDataOverClasses(resultSignals, rootEvaluationDir.getAbsolutePath(), evaluationName, ".csv", true);


        //prep result test data CSV file over folds only
        File perFoldCSV = PrepClassificationFriedmanTestDataClass.prepFriedmanTestData(resultSignals, rootEvaluationDir.getAbsolutePath(), evaluationName, ".csv", true);


        //run friedman test if matlab available?
        if (getPerformMatlabStatSigTests()){
            System.out.println("Performing Friedman's tests in Matlab...");
            String[] systemNamesArr = systemNames.toArray(new String[systemNames.size()]);
            
            performFriedmanTestWithClassFoldAccuracy(rootEvaluationDir, perClassFoldCSV, systemNamesArr);
            performFriedmanTestWithFoldAccuracy(rootEvaluationDir, perFoldCSV, systemNamesArr);
        }
    }
    
    private void performFriedmanTestWithClassFoldAccuracy(File outputDir, File CSVResultFile, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForClassFoldAccuracy";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "perClassAndFoldAccuracy.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', '\t');");
            textOut.newLine();
            textOut.write("algNames = data(1,5:" + (systemNames.length + 4) + ")';");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length(data),5:" + (systemNames.length + 4) + "));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores,1,'on');");
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
        matlabIntegrator.setMatlabBin(getMatlabPath());
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
    
    private void performFriedmanTestWithFoldAccuracy(File outputDir, File CSVResultFile, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForFoldAccuracy";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "perFoldAccuracy.friedman.tukeyKramerHSD.png";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', '\t');");
            textOut.newLine();
            textOut.write("algNames = data(1,5:" + (systemNames.length + 4) + ")';");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length(data),5:" + (systemNames.length + 4) + "));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores,1,'on');");
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
        matlabIntegrator.setMatlabBin(getMatlabPath());
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
}
