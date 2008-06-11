/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.vis;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author kriswest
 */
public class StatisticalSignificanceMatrixPlot extends ApplicationFrame {

    String[] seriesNames;
    String[] categoryNames;
    double[][][] plotPoints;
    XYTextAnnotation[][] annotations;
    
    private DecimalFormat formatter;

    JFreeChart chart;
    
    /**
     * 
     * @param title
     * @param algorithmNames
     * @param plotPoints
     */
    public StatisticalSignificanceMatrixPlot(final String title, final String[] algorithmNames, double[][] sigLevelsDbl) {

        super(title);
        
        seriesNames = algorithmNames;
        categoryNames = algorithmNames;
        
        plotPoints = new double[seriesNames.length][3][seriesNames.length];
        
        //int rowCount = 0;
        for (int i = 0; i < seriesNames.length; i++) {
            for (int j = 0; j < seriesNames.length; j++) {
                //set X
                plotPoints[i][0][j] = i+1;
                //set Y
                plotPoints[i][1][j] = j+1;      
                //set Z
                if(sigLevelsDbl[j][i] > 0.05){
                    //not significant
                    plotPoints[i][2][j] = 0.0;
                }else if(sigLevelsDbl[j][i] > 0.01){
                    //significant at 5%
                    plotPoints[i][2][j] = 0.5;
                }else{
                    //significant at 1%
                    plotPoints[i][2][j] = 1.0;
                }
                //rowCount++;
            }
        }

        formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        annotations = new XYTextAnnotation[seriesNames.length][seriesNames.length];
        for (int i = 0; i < seriesNames.length; i++) {
            for (int j = 0; j < seriesNames.length; j++) {
                if ((sigLevelsDbl[j][i] > 0.00)){
                    if(sigLevelsDbl[j][i] > 0.05){
                        annotations[i][j] = new XYTextAnnotation("F (" + formatter.format(sigLevelsDbl[j][i]) + ")", i+1, j+1);
                        annotations[i][j].setFont(new Font("Arial", Font.PLAIN, 24));
                        annotations[i][j].setPaint(Color.WHITE);
                    }else{
                        annotations[i][j] = new XYTextAnnotation("T (" + formatter.format(sigLevelsDbl[j][i]) + ")", i+1, j+1);
                        annotations[i][j].setFont(new Font("Arial", Font.PLAIN, 24));
                        annotations[i][j].setPaint(Color.BLACK);
                    }
                    annotations[i][j].setTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
                }                
            }

        }
        
        chart = createChart();
        
        TextTitle textTitle = chart.getTitle();
        textTitle.setFont(new Font("Arial", Font.BOLD, 36));
        chart.setTitle(textTitle);

        final ChartPanel panel = new ChartPanel(chart, true, true, true, true, true);
        panel.setPreferredSize(new java.awt.Dimension(600, 600));
        setContentPane(panel);

    }

    /**
     * Creates a combined chart.
     *
     * @return the combined chart.
     */
    private JFreeChart createChart() {

        System.out.println("Creating plot: " + this.getTitle());
        
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockHeight(1.0);
        renderer.setBlockWidth(1.0);
        renderer.setPaintScale(new GrayPaintScale(0.0, 1.0));
        
        //renderer.setBaseSeriesVisible(true);
        //renderer.setBaseItemLabelPaint(Color.WHITE);
        //renderer.setBaseItemLabelGenerator(new ConfMatLabelGenerator());
        //renderer.setBaseItemLabelsVisible(true);
        
        //renderer.setBaseSeriesVisibleInLegend(false);
        for (int i = 0; i < seriesNames.length; i++) {
            renderer.setSeriesShape(i, null);
            renderer.setSeriesCreateEntities(i, false);
        }

        final NumberAxis axisA = new NumberAxis("Algorithm number");
        final NumberAxis axisB = new NumberAxis("Algorithm number");
        
        axisA.setTickUnit(new NumberTickUnit(1.0));
        axisB.setTickUnit(new NumberTickUnit(1.0));
        
        axisA.setRange(0.5, seriesNames.length + 0.5);
        axisB.setRange(0.5, seriesNames.length + 0.5);
        
        axisA.setTickLabelFont(new Font("Arial", Font.PLAIN, 24));
        axisB.setTickLabelFont(new Font("Arial", Font.PLAIN, 24));
        
        axisA.setLabelFont(new Font("Arial", Font.BOLD, 24));
        axisB.setLabelFont(new Font("Arial", Font.BOLD, 24));
        
        axisB.setInverted(true);
        final XYPlot aPlot = new XYPlot(this.createDataset(), axisA, axisB, renderer);
        
        //set legend item generator and label generator
        
        aPlot.setOutlinePaint(Color.black);
        //aPlot.addAnnotation(annotation)
        
        
        
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
        
//        for (int i = 0; i < seriesNames.length; i++) {
//            chart.addSubtitle(i, new TextTitle((i+1) + ": " + seriesNames[i]));
//
//        }
        int numSystemsPerRow = 2;
        int numRows = (int)Math.ceil((double)seriesNames.length / (double)numSystemsPerRow);
        int remaining = seriesNames.length;
        int idx = 0;
        for (int i = 0; i < numRows; i++) {
            String aRow = "";
            int toUseThisRow = Math.min(numSystemsPerRow, remaining);
            for (int j = 0; j < toUseThisRow; j++) {
                if(j>0){
                    aRow += ",       ";
                }
                aRow += (idx+1) + ": " + seriesNames[idx];
                idx++;
                remaining--;
            }
            
            TextTitle textTitle = new TextTitle(aRow);
            textTitle.setFont(new Font("Arial", Font.BOLD, 24));
            chart.addSubtitle(i, textTitle);
            
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
            data.addSeries((i+1) + ": " + seriesNames[i], plotPoints[i]);
        }
        
        return data;

    }
    
    public void writeChartToFile(File fileToWriteTo, int width, int height){
        try
        {
            ChartUtilities.writeChartAsPNG( new FileOutputStream(fileToWriteTo),
                                     chart, width, height);
        }
        catch (IOException ioEx)
        {
            System.err.println("Error writing PNG file " + fileToWriteTo);
        }
    }

    public static void main(String[] args){
        String[] names = new String[]{"system 1","system 2","system 3"};
        double[][] conf = new double[][]{{0.002,0.04,0.21},{0.4,0.5,0.6},{1.0,1.0,1.0}};
        
        
        final StatisticalSignificanceMatrixPlot plot = new StatisticalSignificanceMatrixPlot("Statistical significance matrix test",names,conf);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
        
        
    }
}
