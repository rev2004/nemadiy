/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.vis.ConfusionMatrixPlot;
import org.jfree.ui.RefineryUtilities;

/**
 * A command line applicaiton for parsing th .eval files produced by 
 * SignalArrayAccuracy and plotting confusion matrices which are saved to PNG
 * files on disk in teh same directory.
 * 
 * @author kriswest
 */
public class ParseAndPlotConfusionMatrix {

    public static final String USAGE = "== MIREX confusion matrix plotter ==\n" +
                                       "------------------------------------\n" +
                                       "Plots confusion matrices read from a\n" +
                                       "directory of .eval files produced by\n" +
                                       "the MIREX classification evaluator. \n" +
                                       "Plots are written out to the same   \n" +
                                       "dir as the .eval files.             \n" +
                                       "------------------------------------\n" +
                                       "args: /dir/of/.eval/files/to/plot   \n";
                                       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println(USAGE);
            System.exit(1);
        }
        try {
            File dir = new File(args[0]);
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".eval")){
                    loadPlotAndSaveConfusion(files[i], new File(files[i].getAbsolutePath() + "_conf.png"));
                }
            }

            // TODO code application logic here
            //loadOverallConfusionAndPlot(new File("/Users/kriswest/Desktop/thesisResultsClass/events-MVCART0.001_TFIDF_SMOPoly2.eval"));
        } catch (IOException ex) {
            Logger.getLogger(ParseAndPlotConfusionMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loadPlotAndSaveConfusion(File resultFile, File plotFile) throws IOException{
        ConfusionMatrixPlot plot = loadOverallConfusionAndPlot(resultFile);
        plot.writeChartToFile(plotFile, 900, 850);
    }
    
    
    public static ConfusionMatrixPlot loadOverallConfusionAndPlot(File resultFile) throws IOException{
        
        BufferedReader textBuffer;
        ArrayList rowData = new ArrayList();
        try
        {
            textBuffer = new BufferedReader( new FileReader(resultFile) );
        }
        catch(java.io.FileNotFoundException fnfe)
        {
            throw new RuntimeException("The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n\tFile: " + resultFile.getPath());
        }
        String line = null;
        ArrayList<String> classNames = null;
        ArrayList<String> confusionLines = null;
        String algorithmName = null;
        try{
            line = textBuffer.readLine();

            //find start of overall results
            int count = 0;
            while(!line.startsWith("Overall Accuracy ")){
                //read data
                count++;
                line = textBuffer.readLine();
            }
            if(line == null){
                throw new RuntimeException("End of file reached while seeking overall results!");
            }
            line = line.substring(17, line.length());//replaceAll("Overall Accuracy ", "");
            String[] comps = line.split(":");
            algorithmName = comps[0];

            boolean usingHierarchy = false;

            //read and discard 4 more lines to get to start of conf mat
            line = textBuffer.readLine();
            line = textBuffer.readLine();
            if(line.startsWith("Hierachical Accuracy")){
                line = textBuffer.readLine();
                line = textBuffer.readLine();
            }
            line = textBuffer.readLine();
            line = textBuffer.readLine();

            confusionLines = new ArrayList<String>();
            line = textBuffer.readLine();
            while(!line.trim().equals("")){
                confusionLines.add(line.trim());
                line = textBuffer.readLine();
            }
            //System.err.println("found " + confusionLines.size() + " lines of confusion matrix");

            //skip another 3 lines, skip % confusion matrix, skip one further line
            for (int i = 0; i < confusionLines.size()+4; i++) {
                line = textBuffer.readLine();
            }

            //get classname lines
            classNames = new ArrayList<String>();
            for (int i = 0; i < confusionLines.size(); i++) {
                line = textBuffer.readLine();
                classNames.add(line.trim().split(":")[1].trim());
            }
        }catch(Exception e){
            throw new RuntimeException("Exception occured, current line = '" + line + "'",e);
        }



//        System.err.println("Class names:");
//        for (int i = 0; i < classNames.size(); i++) {
//            System.err.println("\t" + classNames.get(i));
//        }

        //parse confusion matrix into double[][]
        double[][] conf = new double[classNames.size()][classNames.size()];
        for (int i = 0; i < confusionLines.size(); i++) {
            String aLine = confusionLines.get(i);
            String[] lineComps = aLine.split("[\\s]++");
//            String out = "number of comps in confusion line " + i + ": " + lineComps.length;
//            if (lineComps.length == classNames.size()+1){
//                out += " (correct)";
//            }else{
//                out += " (mismatch with number of class names)";
//            }
//            System.out.println(out);
            for (int j = 1; j <= classNames.size(); j++) {
                conf[i][j-1] = Double.valueOf(lineComps[j]);
            }
        }

        final ConfusionMatrixPlot plot = new ConfusionMatrixPlot("Confusion matrix - " + algorithmName,classNames.toArray(new String[classNames.size()]),conf);
//        plot.pack();
//        RefineryUtilities.centerFrameOnScreen(plot);
//        plot.setVisible(true);
//        
        return plot;
    }
    
    
    
}
