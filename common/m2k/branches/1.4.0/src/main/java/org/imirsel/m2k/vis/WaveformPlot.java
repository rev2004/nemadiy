package org.imirsel.m2k.vis;

import java.awt.*;
import java.util.ArrayList;

/**
 * A simple waveform plot.
 * *Deprecated*
 */
public class WaveformPlot extends Canvas {

    /**
     * Constant definition for plot style.
     */
  public static final int SIGNAL = 1;
    /**
     * Constant definition for plot style.
     */
  public static final int SPECTRUM = 2;

  Graphics graphics;
  Rectangle plotRect = new Rectangle();
  Color plotColor = Color.blue;
  Color axisColor = Color.lightGray;
  Color gridColor = Color.lightGray;
  Color dividerColor = Color.red;
  Color bgColor   = Color.white;
  int plotStyle = SIGNAL;
  boolean tracePlot = true;
  //boolean logScale = false;
  int vertInset = 20;
  int horzInset = 20;
  int xAxisPos;
  //int vertIntervals = 8;
  //int horzIntervals = 10;
  double gridTimeIntervals = 0.1; // 100 ms
  int nPoints = 0;
  float xmax = 0.0f;
  float ymax = 0.0f;
  double startTime = -1.0;
  double endTime = -1.0;
  private float[] plotValues;
  private ArrayList dividers;

    /**
     * Creates a new instance of Waveform plot.
     */
  public WaveformPlot() {
      dividers = new ArrayList();
      this.setPreferredSize(new Dimension(800, 250));
  }

    /**
     * Sets the plot colour.
     * @param c the plot colour.
     */
  public void setPlotColor(Color c) {
    if (c != null) plotColor = c;
  }

    /**
     * 
     * Returns the plot colour.
     * @return the plot colour.
     */
  public Color getPlotColor() {
    return plotColor;
  }

    /**
     * Sets the Axis colour.
     * @param c the Axis colour.
     */
  public void setAxisColor(Color c) {
    if (c != null) axisColor = c;
  }

    /**
     * 
     * Returns the Axis colour.
     * @return the Axis colour.
     */
  public Color getAxisColor() {
    return axisColor;
  }

    /**
     * Sets the grid colour.
     * @param c the grid colour.
     */
  public void setGridColor(Color c) {
    if (c != null) gridColor = c;
  }

    /**
     * Returns the grid colour.
     * @return the grid colour.
     */
  public Color getGridColor() {
    return gridColor;
  }

    /**
     * Sets the background colour.
     * @param c the background colour.
     */
  public void setBgColor(Color c) {
    if (c != null) bgColor = c;
  }

    /**
     * Returns the background colour.
     * @return the background colour.
     */
  public Color getBgColor() {
    return bgColor;
  }

    /**
     * Set the plot style.
     * @param pst the plot style.
     */
  public void setPlotStyle(int pst) {
    plotStyle = pst;
  }

    /**
     * Returns the plot style.
     * @return the plot style.
     */
  public int getPlotStyle() {
    return plotStyle;
  }

    /**
     * Sets the plot style to a line rather than bar plot.
     * @param b Sets the plot style to a line rather than bar plot.
     */
  public void setTracePlot(boolean b) {
    tracePlot = b;
  }

    /**
     * Determines whether the plot style is set to a line plot (trace) or bar plot.
     * @return A flag indicating whether the plot style is set to a line plot (trace) or bar plot.
     */
  public boolean isTracePlot() {
    return tracePlot;
  }

/*  public void setLogScale(boolean b) {
    logScale = b;
  }

  public boolean isLogScale() {
    return logScale;
  }
*/
    /**
     * Sets the horizontal inset amount.
     * @param h the horizontal inset amount.
     */
  public void setHorzInset(int h) {
    // horizontal inset for plot area from canvas boundary
    horzInset = h;
  }

    /**
     * Sets the vertical inset amount.
     * @param v the vertical inset amount.
     */
  public void setVertInset(int v) {
    // vertical inset for plot area from canvas boundary
    vertInset = v;
  }

    /**
     * Returns the vertical inset amount.
     * @return the vertical inset amount.
     */
  public int getVertInset() {
    return vertInset;
  }

    /**
     * Returns the horizontal inset amount.
     * @return the horizontal inset amount.
     */
  public int getHorzInset() {
    return horzInset;
  }

    /**
     * Returns the length of the X axis.
     * @return the length of the X axis.
     */
  public int getXAxisLength() {
    return plotRect.getSize().width;
  }

    /**
     * Returns the position of the X axis.
     * @return the position of the X axis.
     */
  public int getXAxisPos() {
    return xAxisPos;
  }

    /**
     * Returns the length of the Y axis.
     * @return the length of the Y axis.
     */
  public int getYAxisLength() {
    return plotRect.getSize().height;
  }

    /**
     * Returns the dimensions of the plot.
     * @return the dimensions of the plot.
     */
  public Dimension getPlotSize() {
    return plotRect.getSize();
  }

    /**
     * Returns the plot rectangle.
     * @return the plot rectangle.
     */
  public Rectangle getPlotRect() {
    return plotRect;
  }

  /*public int getVertIntervals() {
    return vertIntervals;
  }

  public void setVertIntervals(int i) {
    vertIntervals = i;
  }

  public int getHorzIntervals() {
    return horzIntervals;
  }

  public void setHorzIntervals(int i) {
    horzIntervals = i;
  }
*/
    /**
     * Sets the maximum height of the plot object.
     * @param m the maximum height of the plot object.
     */
  public void setYmax(float m) {
    ymax = m;
  }

    /**
     * Returns the maximum height of the plot object.
     * @return the maximum height of the plot object.
     */
  public float getYmax() {
    return ymax;
  }
  
    /**
     * Sets a divider at the specified time index.
     * @param time Time index to create a divider at.
     */
  public void setDivider(double time)
  {
      double length = endTime - startTime;
      //float xScale = plotRect.width/(float)nPoints;
      int x = plotRect.x + Math.round(plotRect.width * (float)((time - startTime)/length));
      dividers.add(new Integer(x));
      repaint();
  }

    /**
     * Sets the plot values and start/end time.
     * @param values The plot values.    
     * @param start Start time index
     * @param end End time index.
     */
  public void setPlotValues(float[] values, double start, double end) {
      this.startTime = start;
      this.endTime = end;
      nPoints = values.length;
      plotValues = new float[nPoints];
      plotValues = values;
      dividers = new ArrayList();
      repaint();
  }

    /**
     * Draw the grid.
     */
  public void drawGrid() {
      double length = endTime - startTime;
      double c = 0.0;
    int x, y;
    graphics.setColor(gridColor);
    
    // vertical grid lines
    while (c < length)
    {
        c += this.gridTimeIntervals;
        x = plotRect.x + Math.round(plotRect.width * (float)(c/length));
        graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
    }
    
    /*for (int i = 0; i <= vertIntervals; i++) {
      x = plotRect.x + i*plotRect.width/vertIntervals;
      graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
    }
     **/
    // horizontal grid lines
    /*for (int i = 0; i <= horzIntervals; i++) {
      y = plotRect.y + i*plotRect.height/horzIntervals;
      graphics.drawLine(plotRect.x, y, plotRect.x + plotRect.width, y);
    }*/
  }

    /**
     * Draw the axes
     */
  public void drawAxes() {
    xAxisPos = plotRect.y + plotRect.height / 2;
    if (plotStyle == SPECTRUM) xAxisPos = plotRect.y + plotRect.height;
    //if (logScale) xAxisPos = plotRect.y;
    graphics.setColor(axisColor);
     // vertical axis:
    graphics.drawLine(plotRect.x, plotRect.y,
                        plotRect.x, plotRect.y + plotRect.height);
    // horizontal axis:
    graphics.drawLine(plotRect.x, xAxisPos,
                        plotRect.x + plotRect.width, xAxisPos);
  }
  
    /**
     * Draw the dividers.
     */
  public void drawDividers() {
      graphics.setColor(dividerColor);
      for (int i=0;i<dividers.size();i++)
      {
          int x = ((Integer)dividers.get(i)).intValue();
          graphics.drawLine(x, plotRect.y, x, plotRect.y + plotRect.height);
      }
  }
  


    /**
     * PLot the data points
     */
  public void plotPoints() {
    if (nPoints != 0) {
      graphics.setColor(plotColor);
      // default plot type has x axis in middle of plot with + and - y axes:
      xAxisPos = plotRect.y + plotRect.height / 2;
      float xScale = plotRect.width/(float)nPoints;
      float yScale = 0.5f*plotRect.height/ymax;
      if (plotStyle == SPECTRUM) {
        xAxisPos = plotRect.y + plotRect.height; // x axis at bottom of plot
        yScale = plotRect.height/ymax; // vertical scale based on full plot height
      }
      //if (logScale) xAxisPos = plotRect.y; // x axis at top of plot (0 dB)
      int[] xCoords = new int[nPoints];
      int[] yCoords = new int[nPoints];
      for (int i = 0; i < nPoints; i++) {
         xCoords[i] = plotRect.x + Math.round(i*xScale);
         yCoords[i] = xAxisPos - Math.round(plotValues[i]*yScale);
      }
      if (tracePlot)
        graphics.drawPolyline(xCoords, yCoords, nPoints);
      else { // bar plot
        for (int i = 0; i < nPoints; i++)
          graphics.drawLine(xCoords[i], xAxisPos, xCoords[i], yCoords[i]);
      }
    }
  }

    /**
     * Paint the canvas.
     * @param g The graphics Object to paint with.
     */
  public void paint(Graphics g) {
    this.graphics = g;
    this.setBackground(bgColor);
    // define the location and size of the plot area:
    plotRect.setBounds(horzInset,
                       vertInset,
                       getSize().width - 2*horzInset,
                       getSize().height - 2*vertInset);
    drawAxes();
    drawGrid();
    plotPoints();
    drawDividers();
  }
}
