/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.vis;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.TextAnchor;

/**
 * Plotting utility for confusion matrices which writes out PNG files representing the matrix.
 * @author kris.west@gmail.com
 */
public class ConfusionMatrixPlot  {

    String[] seriesNames;
    double[][][] confusion;
    XYTextAnnotation[][] annotations;
    
    private DecimalFormat formatter;

    JFreeChart chart;

    private String title;
    
    /**
     * Constructor.
     * @param title The title for the plot.....
     * @param classNames The vector of class names to use on the plot/.
     * @param confusionDbl The confusion matrix data.
     */
    public ConfusionMatrixPlot(final String title, final String[] classNames, double[][] confusionDbl) {
        this.title = title;
        
        seriesNames = classNames;
        
        confusion = new double[seriesNames.length][3][seriesNames.length];
        
        //int rowCount = 0;
        for (int i = 0; i < seriesNames.length; i++) {
            double seriesSum = 0.0;
            for (int j = 0; j < seriesNames.length; j++) {
                //set X
                confusion[i][0][j] = i+1;
                //set Y
                confusion[i][1][j] = j+1;      
                //set Z
                confusion[i][2][j] = confusionDbl[j][i]; 
                seriesSum += confusionDbl[j][i];;
            }
            for (int j = 0; j < seriesNames.length; j++) {
                confusion[i][2][j] /= seriesSum;
            }
        }

        formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        annotations = new XYTextAnnotation[seriesNames.length][seriesNames.length];
        for (int i = 0; i < confusionDbl.length; i++) {
            for (int j = 0; j < confusionDbl.length; j++) {
                if (confusion[j][2][i] >= 0.005){
                    annotations[i][j] = new XYTextAnnotation(formatter.format(confusion[i][2][j]), i+1, j+1);
                    annotations[i][j].setTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
                    if (confusion[j][2][i] <= 0.4){
                        annotations[i][j].setPaint(Color.WHITE);
                    }
                }
            }

        }

        chart = createChart();
        final ChartPanel panel = new ChartPanel(chart, true, true, true, true, true);
        panel.setPreferredSize(new java.awt.Dimension(900, 850));

    }

    /**
     * Creates a combined chart.
     *
     * @return the combined chart.
     */
    private JFreeChart createChart() {
        Logger.getLogger(ConfusionMatrixPlot.class.getName()).info("Creating plot: " + this.getTitle());
        
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockHeight(1.0);
        renderer.setBlockWidth(1.0);
        renderer.setPaintScale(new GrayPaintScale(0.0, 1.0));
        
        for (int i = 0; i < seriesNames.length; i++) {
            renderer.setSeriesShape(i, null);
            renderer.setSeriesCreateEntities(i, false);
        }

        final NumberAxis axisA = new NumberAxis("True class number");
        final NumberAxis axisB = new NumberAxis("Predicted class number");
        
        axisA.setTickUnit(new NumberTickUnit(1.0));
        axisB.setTickUnit(new NumberTickUnit(1.0));
        axisA.setRange(0.5, seriesNames.length + 0.5);
        axisB.setRange(0.5, seriesNames.length + 0.5);
        
        axisB.setInverted(true);
        final XYPlot aPlot = new XYPlot(this.createDataset(), axisA, axisB, renderer);
        
        aPlot.setOutlinePaint(Color.black);
        
        //add annotations
        for (int i = 0; i < seriesNames.length; i++) {
            for (int j = 0; j < seriesNames.length; j++) {
                if (annotations[i][j] != null){
                    aPlot.addAnnotation(annotations[i][j]);
                }
            }

        }

        //create chart
        JFreeChart chart = new JFreeChart(this.getTitle(),JFreeChart.DEFAULT_TITLE_FONT,aPlot,false);
        
        int numGenresPerRow = 4;
        int numRows = (int)Math.ceil((double)seriesNames.length / (double)numGenresPerRow);
        int remaining = seriesNames.length;
        int idx = 0;
        for (int i = 0; i < numRows; i++) {
            String aRow = "";
            int toUseThisRow = Math.min(numGenresPerRow, remaining);
            for (int j = 0; j < toUseThisRow; j++) {
                if(j>0){
                    aRow += ",    ";
                }
                aRow += (idx+1) + ": " + seriesNames[idx];
                idx++;
                remaining--;
            }
            chart.addSubtitle(i, new TextTitle(aRow));
        }
   
        chart.setBackgroundPaint(Color.white);
        
//        LegendTitle legend = chart.getLegend();
//        
//        LegendItemSource[] sources = legend.getSources();
//        
//        System.out.println("Number of legend sources: " + sources.length);
//        
//        LegendItemCollection legendItems = sources[0].getLegendItems();
//        System.out.println("Number of legend items: " + legendItems.getItemCount());
//        for (Iterator it = legendItems.iterator(); it.hasNext(); ) {
//            LegendItem item = (LegendItem)it.next();
//            //item.
//        }
//        
//        LegendItemSource[] newSources = new LegendItemSource[1];
//        newSources[0] = newtLegendItemSource() 
        
        return chart;
    }

    /**
     * Creates a sample dataset.
     *
     * @return Series 1.
     */
    private XYZDataset createDataset() {
        final DefaultXYZDataset data = new DefaultXYZDataset();
        
        for (int i = 0; i < seriesNames.length; i++) {
            data.addSeries((i+1) + ": " + seriesNames[i], confusion[i]);
        }
        
        return data;
    }
    
    public void writeChartToFile(File fileToWriteTo, int width, int height){
        try
        {
        	Logger.getLogger(ConfusionMatrixPlot.class.getName()).info(
        			"Writing confusion matrix image to: " + fileToWriteTo.getAbsolutePath());
            ChartUtilities.writeChartAsPNG( new FileOutputStream(fileToWriteTo),
                                     chart, width, height);
        }
        catch (IOException ioEx)
        {
            System.err.println("Error writing PNG file " + fileToWriteTo);
        }
    }

    public static void main(String[] args){
        String[] names = new String[]{"class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9"};
        double[][] conf = new double[][]{{3.8,0.2,0.3,0.0,0.2,0.3,0.1,0.2,0.3},{0.4,2.9,0.6,0.4,0.5,0.6,0.4,0.5,0.6},{1.0,3.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
        {0.1,0.2,0.3,3.1,0.2,0.3,0.1,0.2,0.3},{0.4,0.5,0.6,0.4,6.5,0.6,0.4,0.5,0.6},{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
        {0.0,0.2,0.3,0.1,0.2,0.3,0.0,0.2,0.3},{0.4,0.5,0.6,0.4,0.5,0.6,0.4,0.5,0.6},{1.0,1.0,1.0,1.0,1.0,1.0,0.0,1.0,1.0}};
        
        final ConfusionMatrixPlot plot = new ConfusionMatrixPlot("Confusion matrix test",names,conf);
    }

    /**
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }
}
