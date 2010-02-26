/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.util.resultpages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.model.*;
import org.imirsel.nema.analytics.evaluation.classification.WriteClassificationResultFiles;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.analytics.util.io.IOUtil;

/**
 *
 * @author kris.west@gmail.com
 */
public class WriteResultPagePerFile {

    /**
     * Writes out a set of result HTML pages. Each page has a single table of
     * evaluation metrics and an image. One additional page is provided 
     * providing links to each of the data files.
     *
     * @param evaluationName The name of the evaluation to put in the result
     * page headers.
     * @param pageNames The names for each page. Do not add any file extension.
     * @param CSVFiles Array of CSV files to plt on the result pages
     * @param imagePaths An array of image file paths to plot on the result
     * pages.
     * @param outputDirectory The directory to output the HTML pages to.
     * @throws IOException Thrown if there is a problem reading the CSV files.
     */
    public static void writeResultsHTML(NemaTask task, NemaDataset dataset, String[] pageNames, File[] CSVFiles, File[] imagePaths, File outputDirectory) throws IOException{
        //create result pages
        System.err.println("Creating result HTML files...");

        if ((pageNames.length != CSVFiles.length)||(CSVFiles.length != imagePaths.length)){
            throw new IllegalArgumentException("The arrays of page names, CSV files and imagePaths must be the same length!\n" +
                    "pageNames length  = " + pageNames.length + "\n" +
                    "CSVFiles length   = " + CSVFiles.length + "\n" +
                    "imagePaths length = " + imagePaths.length);
        }
        List<Page> resultPages = new ArrayList<Page>();
        List<PageItem> items;
        Page aPage;

        //do intro page
        items = new ArrayList<PageItem>();
        Table descriptionTable = WriteClassificationResultFiles.prepTaskTable(task,dataset);
        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
        aPage = new Page("intro", "Introduction", items, false);
        resultPages.add(aPage);
        
        //do a page per file
        String[][][] csvData = new String[pageNames.length][][];
        for (int i = 0; i < pageNames.length; i++){
            items = new ArrayList<PageItem>();
            String cleanName = pageNames[i].replaceAll("\\s", "_");

            //add table from CSV files
            csvData[i] = DeliminatedTextFileUtilities.loadDelimTextData(CSVFiles[i], ",", -1);
            ArrayList<String[]> rows = new ArrayList<String[]>(csvData[i].length-1);
            for (int j = 1; j < csvData[i].length; j++){
                rows.add(csvData[i][j]);
            }
            items.add(new TableItem("evalMetrics", "Evaluation Metrics", csvData[i][0], rows));

            //add evaluation plot
            items.add(new ImageItem("plot", "Plot", IOUtil.makeRelative(imagePaths[i], outputDirectory)));

            //add the page
            aPage = new Page(cleanName, pageNames[i], items, true);
            resultPages.add(aPage);
        }

        //do mean results page
        if(csvData.length > 1){
            items = new ArrayList<PageItem>();
            
            try{
                //average tables
                double[] averages = new double[csvData[0].length-1];
                for (int i = 0; i < csvData.length; i++){
                    for (int j = 0; j < averages.length; j++){
                        averages[j] += Double.parseDouble(csvData[i][j+1][1]);
                    }
                }
                for (int i = 0; i < averages.length; i++){
                    averages[i] /= csvData.length;
                }
                
                ArrayList<String[]> rows = new ArrayList<String[]>(averages.length);
                
                for (int r = 0; r < averages.length; r++){
                    rows.add(new String[]{csvData[0][r+1][0],""+averages[r]});
                }
                
                items.add(new TableItem("meanEvalMetrics", "Mean Evaluation Metrics", csvData[0][0], rows));

                //add the page
                aPage = new Page("mean_scores", "Mean scores", items, false);
                resultPages.add(aPage);
            }catch(Exception e){
                Logger.getLogger(WriteResultPagePerFile.class.getName()).log(Level.WARNING, "Was unable to produce mean scores from second column of CCSV tables!",e);
            }
            
            
        }


        //do files page
        {
            items = new ArrayList<PageItem>();

            //CSVs
            List<String> CSVPaths = new ArrayList<String>(CSVFiles.length);
            for (int i = 0; i < CSVFiles.length; i++){
                 CSVPaths.add(IOUtil.makeRelative(CSVFiles[i],outputDirectory));
            }
            items.add(new FileListItem("dataCSVs", "CSV result files", CSVPaths));

            //Friedman's tables and plots
            List<String> images = new ArrayList<String>(imagePaths.length);
            for (int i = 0; i < imagePaths.length; i++){
                 images.add(IOUtil.makeRelative(imagePaths[i],outputDirectory));
            }
            items.add(new FileListItem("plots", "Evaluation plots", images));

            aPage = new Page("files", "All data files", items, true);
            resultPages.add(aPage);
        }

        Page.writeResultPages(task.getName(), outputDirectory, resultPages);
    }
}
