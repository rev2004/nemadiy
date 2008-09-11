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
            System.err.println("ERROR: Insufficient arguments!\n" + USAGE);
            System.exit(1);
        }
        
        evaluationName = args[0];
        
        gtFile = new File(args[1]);
        
        rootEvaluationDir = new File(args[2]);
        rootEvaluationDir.mkdirs();
        if (args.length % 2 != 1){
            System.err.println("WARNING: an even number of arguments was specified, one may have been ignored!\n" + USAGE);
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
        
        System.err.println("MIREX 2008 Classification evaluator\n" +
                "\t\tby Kris West (kris.west@gmail.com");
        System.err.println("");
        
        MIREXClassificationEvalMain eval = new MIREXClassificationEvalMain();
        eval.parseCommandLineArgs(args);
        
        eval.performEvaluation();
        
        System.err.println("---exit---");
        
    }
    
    public void performEvaluation() {

        //get each directory of results
        System.err.println("Determining location of bresults files for each system for each experiment fold...");
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
                System.err.println("ERROR: The number of folds (" + resultFiles.size() + ") detected " + "in directory: " + dir.getAbsolutePath() + " for result files is not equal to the number detected " + "for the preceeding systems (" + numFolds + ")!");
                System.exit(1);
            }
        }

        System.err.println("reading result data files...");
        //read each binary result file and create EvaluationDataObject arrays
        Signal[][][] resultData = new Signal[systemNames.size()][numFolds][];
        for (int i = 0; i < systemNames.size(); i++) {
            System.err.println("\treading " + systemNames.get(i));
            ArrayList<File> fileList = resultsFilesPerSystemPerFold.get(i);
            for (int j = 0; j < numFolds; j++) {
                resultData[i][j] = ClassificationResultReadClass.readClassificationFileAsSignals(fileList.get(j), gtFile,true);
            }
        }

        System.err.println("Performing individual classification evals...");
        //run SignalArrayAccuracyClass2 on each system
        File[] resultFiles = new File[resultData.length];
        ArrayList<Signal[]> resultSignals = new ArrayList(resultData.length);
        for (int i = 0; i < resultData.length; i++) {
            System.out.println("\tevaluating " + systemNames.get(i));
            File systemOutputDir = new File(rootEvaluationDir.getAbsolutePath() + File.separator + systemNames.get(i));
            systemOutputDir.mkdirs();
            SignalArrayAccuracyClass2 evaluator = new SignalArrayAccuracyClass2(systemNames.get(i), ".eval.txt", ".evalData.ser", systemOutputDir.getAbsolutePath(), null, true);
            Signal[] resultSignalArr = null;
            for (int j = 0; j < numFolds; j++) {
                try {
                    resultSignalArr = evaluator.evaluate(resultData[i][j]);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (noMetadataException ex) {
                    Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            resultSignals.add(resultSignalArr);
            resultFiles[i] = evaluator.getReportFile();
        }


        //plot confusion matrices
        System.err.println("Plotting overall confusion matrices...");
        ParseAndPlotConfusionMatrix confPlotter = new ParseAndPlotConfusionMatrix();
        for (int i = 0; i < resultFiles.length; i++) {
            try {
                confPlotter.loadPlotAndSaveConfusion(resultFiles[i], new File(resultFiles[i].getAbsolutePath() + ".conf.png"));
            } catch (IOException ex) {
                Logger.getLogger(MIREXClassificationEvalMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        System.err.println("Writing out CSV result files over whole task...");
        //prep result test data CSV file over classes and folds
        File perClassCSV = WriteResultFilesClass.prepFriedmanTestDataOverClasses(resultSignals, rootEvaluationDir.getAbsolutePath(), evaluationName, ".csv", true);


        //prep result test data CSV file over folds only
        File perFoldCSV = WriteResultFilesClass.prepFriedmanTestData(resultSignals, rootEvaluationDir.getAbsolutePath(), evaluationName, ".csv", true);

        //write out results summary
        File summaryCSV = WriteResultFilesClass.prepSummaryResultData(resultSignals, rootEvaluationDir.getAbsolutePath(), evaluationName, ".csv", true);

        

        //run friedman test if matlab available?
        if (getPerformMatlabStatSigTests()){
            System.err.println("Performing Friedman's tests in Matlab...");
            String[] systemNamesArr = systemNames.toArray(new String[systemNames.size()]);
            
            performFriedmanTestWithClassAccuracy(rootEvaluationDir, perClassCSV, systemNamesArr);
            performFriedmanTestWithFoldAccuracy(rootEvaluationDir, perFoldCSV, systemNamesArr);
        }
    }
    
    private void performFriedmanTestWithClassAccuracy(File outputDir, File CSVResultFile, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForClassAccuracy";
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + "perClassAccuracy.friedman.tukeyKramerHSD.png";
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + "perClassAccuracy.friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

//            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', ',');");
//            textOut.newLine();
//            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
//            textOut.newLine();
//            textOut.write("[length,width] = size(data);");
//            textOut.newLine();
//            textOut.write("Acc_Scores = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
//            textOut.newLine();
//            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores,1,'on');");
//            textOut.newLine();
//            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
//            textOut.newLine();
//            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
//            textOut.newLine();
//            textOut.write("exit;");
//            textOut.newLine();
            
            
            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
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
            textOut.write("set(gca,'xticklabel',algNames(fliplr(sort_idx)))");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFile.getAbsolutePath() + "')");
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
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',algNames{c(i,1)},algNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
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
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + "perFoldAccuracy.friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

//            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', ',');");
//            textOut.newLine();
//            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
//            textOut.newLine();
//            textOut.write("[length,width] = size(data);");
//            textOut.newLine();
//            textOut.write("Acc_Scores = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
//            textOut.newLine();
//            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores,1,'on');");
//            textOut.newLine();
//            textOut.write("[c,m,fig,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05);");
//            textOut.newLine();
//            textOut.write("saveas(fig,'" + matlabPlotPath + "');");
//            textOut.newLine();
//            textOut.write("exit;");
//            textOut.newLine();

            
            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
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
            textOut.write("set(gca,'xticklabel',algNames(fliplr(sort_idx)))");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFile.getAbsolutePath() + "')");
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
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',algNames{c(i,1)},algNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
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
