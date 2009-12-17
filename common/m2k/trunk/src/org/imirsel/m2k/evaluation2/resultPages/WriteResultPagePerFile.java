/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.resultPages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.io.file.IOUtil;

/**
 *
 * @author kriswest
 */
public class WriteResultPagePerFile {

    /**
     * Writes out a set of result HTML pages. Each page has a single table of
     * evaluation metrics and an image. One additional page is provided providing
     * links to each of the data files.
     *
     * @param pageNames The names for each page. Do not add any file extension.
     * @param CSVFiles Array of CSV files to plt on the result pages
     * @param imagePaths An array of image file paths to plot on the result pages.
     * @param outputDirectory The directory to output the HTML pages to.
     */
    public static void writeResultsHTML(String evaluationName, String[] pageNames, File[] CSVFiles, File[] imagePaths, File outputDirectory) throws IOException{
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

        //do a page per file
        for (int i = 0; i < pageNames.length; i++){
            items = new ArrayList<PageItem>();
            String cleanName = pageNames[i].replaceAll("\\s", "_");

            //add table from CSV files
            String[][] csvData = DeliminatedTextFileUtilities.loadDelimTextData(CSVFiles[i], ",", -1);
            ArrayList<String[]> rows = new ArrayList<String[]>(csvData.length-1);
            for (int j = 1; j < csvData.length; j++){
                rows.add(csvData[j]);
            }
            items.add(new TableItem("evalMetrics", "Evaluation Metrics", csvData[0], rows));

            //add evaluation plot
            items.add(new ImageItem("plot", "Plot", IOUtil.makeRelative(imagePaths[i], outputDirectory)));

            //add the page
            aPage = new Page(cleanName, pageNames[i], items, true);
            resultPages.add(aPage);
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

        ResultPageUtilities.writeResultPages(evaluationName, outputDirectory, resultPages);
    }
}
