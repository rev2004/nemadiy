/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.vis;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author kriswest
 */
public class SimpleNumericPlot extends ApplicationFrame {

    boolean logDomainScale = false;
    boolean logRangeScale = false;
    
    String range1Name;
    String range2Name;
    String rangeAxisName;
    String domainAxisName;
    double[] domain;
    double[] range1;
    double[] range2;
    
    JFreeChart chart;
            
    /**
     * 
     * @param logDomainScale_
     * @param logRangeScale_
     * @param title
     * @param range1Name_
     * @param range2Name_
     * @param rangeAxisName_
     * @param domainAxisName_
     * @param domain_
     * @param range1_
     * @param range2_
     */
    public SimpleNumericPlot(final boolean logDomainScale_, final boolean logRangeScale_, final String title, final String range1Name_, final String range2Name_, final String rangeAxisName_, final String domainAxisName_, final double[] domain_, final double[] range1_, final double[] range2_) {

        super(title);
        
        logDomainScale = logDomainScale_;
        logRangeScale = logRangeScale_;
    
        range1Name = range1Name_;
        range2Name = range2Name_;
        rangeAxisName = rangeAxisName_;
        domainAxisName = domainAxisName_;
        domain = domain_;
        range1 = range1_;
        range2 = range2_;
        
        final JFreeChart chart = createChart();
        final ChartPanel panel = new ChartPanel(chart, true, true, true, true, true);
        panel.setPreferredSize(new java.awt.Dimension(600, 400));
        setContentPane(panel);

    }

    /**
     * Creates a combined chart.
     *
     * @return the combined chart.
     */
    private JFreeChart createChart() {

        System.out.println("Creating plot: " + this.getTitle());
        
        final XYDataset data1 = createDataset();
        final XYItemRenderer renderer1 = new StandardXYItemRenderer();
        
        final JFreeChart chart = ChartFactory.createXYLineChart(
            this.getTitle(),          // chart title
            domainAxisName,               // domain axis label
            rangeAxisName,                  // range axis label
            data1,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,
            false
        );

        final XYPlot plot = chart.getXYPlot();
        if (logDomainScale){
            final NumberAxis domainAxis = new LogarithmicAxis(domainAxisName);
            //domainAxis.setAutoRangeIncludesZero(true);
            //domainAxis.setRange(domain[0],domain[domain.length-1]);
            plot.setDomainAxis(domainAxis);
        }else{
            final NumberAxis domainAxis = new NumberAxis(domainAxisName);
            //domainAxis.setAutoRangeIncludesZero(true);
            //domainAxis.setRange(domain[0],domain[domain.length-1]);
            plot.setDomainAxis(domainAxis);
        }   
        if (logRangeScale){
            final NumberAxis rangeAxis = new LogarithmicAxis(rangeAxisName);
            plot.setRangeAxis(rangeAxis);
        }else{
            final NumberAxis rangeAxis = new NumberAxis(rangeAxisName);
            plot.setRangeAxis(rangeAxis);
        }
        
        chart.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.black);
        return chart;
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
    
    /**
     * Creates a sample dataset.
     *
     * @return Series 1.
     */
    private XYDataset createDataset() {

        // create dataset 1...
        final XYSeries series1 = new XYSeries(range1Name);
        
        final XYSeriesCollection collection = new XYSeriesCollection();
        
        double max = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;
        double min = Double.POSITIVE_INFINITY;
        int minIdx = -1;
        for (int i = 0; i < domain.length; i++) {
            if(Double.isNaN(domain[i])){
                throw new RuntimeException("Domain " + i + " was NaN");
            }
            series1.add(domain[i], range1[i]);
            if (range1[i] > max){
                max = range1[i];
                maxIdx = i;
            }
            if (range1[i] < min){
                min = range1[i];
                minIdx = i;
            }
        }
        collection.addSeries(series1);
        
        System.out.println("\t" + range1Name + " min (" + minIdx + "): " + min + ", max (" + maxIdx + "): " + max);

        if (range2 != null){
            final XYSeries series2 = new XYSeries(range2Name);

            max = Double.NEGATIVE_INFINITY;
            maxIdx = -1;
            min = Double.POSITIVE_INFINITY;
            minIdx = -1;
            for (int i = 0; i < domain.length; i++) {
                if(Double.isNaN(range2[i])){
                    throw new RuntimeException(range2Name + " " + i + " was NaN");
                }
                if(Double.isInfinite(range2[i])){
                    throw new RuntimeException(range2Name + " " + i + " was Inifite!");
                }
                series2.add(domain[i], range2[i]);
                if (range2[i] > max){
                    max = range2[i];
                    maxIdx = i;
                }
                if (range2[i] < min){
                    min = range2[i];
                    minIdx = i;
                }
            }
            collection.addSeries(series2);
            System.out.println("\t" + range2Name + " min (" + minIdx + "): " + min + ", max (" + maxIdx + "): " + max);
        }
        
        
        return collection;

    }

}
