
package org.imirsel.m2k.vis;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JLayeredPane;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Sorts and plots an input array of (positive) values to produce a curve. Originally 
 * implemented to plot curves of how many times a song appeared list of search 
 * results or the scores of a single query. 
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class ResultCurvePlot extends JLayeredPane{
    
    /**
     * Graphics Object to used to paint the frame.
     */
    Graphics graphics;
    /**
     * The rectangle within which to plot.
     */
    Rectangle plotRect;
    /**
     * The amount to inset the plot vertically.
     */
    int vertInset = 30;
    /**
     * The amount to inset the plot horizonatally.
     */
    int horzInset = 28;
    /**
     * Preferred height of the plot.
     */
    int prefHeight = 300;
    /**
     * Preferred width of the plot.
     */
    int prefWidth = 550;
    
    /**
     * Onset times to display on the plot
     */
    double[] theData = null;
    /**
     * 
     */
    int[] theIntegerData = null;
    
    /**
     * For formatting decimal numbers.
     */
    DecimalFormat decimal = null;
    
    /**
     * The colour for the plot line.
     */
    Color plotColor = Color.blue;
    /**
     * Axis colour for the plot.
     */
    Color axisColor = Color.darkGray;
    /**
     * Grid colour for the plot.
     */
    Color gridColor = Color.lightGray;
    /**
     * Divider colour for the plot (usually displays onset times).
     */
    Color dividerColor = Color.red;
    /**
     * Background colour for the plot.
     */
    Color bgColor = Color.white;
    /**
     * Text colour for the plot.
     */
    Color textColor = Color.darkGray;
    /**
     * Font for displaying text on the plot.
     */
    Font textFont = Font.decode("Arial-PLAIN-10");
    /**
     * Font for displaying title text on the plot.
     */
    Font titleFont = Font.decode("Arial-BOLD-11");
    /** Number of points in plots */
    int nPoints = 0;
    /**
     * Maximum value to plot on Y axis.
     */
    double ymax = 2000;
    /**
     * Plot name.
     */
    private String name = "";
    /**
     * Image object for writing out to file.
     */
    BufferedImage image = null;
    
    /**
     * Creates a new instance of the plot Object.
     */
    public ResultCurvePlot(double[] theData_, String name_) {
        //this.setBackground(this.bgColor);
        //this.setOpaque(true);

        setName(name_);
        if (getName() == null)
        {
            setName("");
        }
        
        this.setPreferredSize(new Dimension(this.prefWidth, this.prefHeight));
        this.setSize(new Dimension(this.prefWidth, this.prefHeight));
        
        plotRect = new Rectangle();
        plotRect.setBounds(horzInset,
                vertInset,
                getSize().width - 2*horzInset,
                getSize().height - 2*vertInset);
        theData = theData_;
        decimal = new DecimalFormat();
        decimal.setMaximumFractionDigits(3);
        
        nPoints = theData.length;
        ymax = Double.MIN_VALUE;
        for (int i=0;i<nPoints;i++) {
            if (theData[i] > ymax) {
                ymax = theData[i];
            }
        }

        int min = ((nPoints / 5) * 2);
        if(ymax < min)
        {
            ymax = min;
        }
    }
    
    /**
     * Creates a new instance of the plot Object.
     */
    public ResultCurvePlot(int[] theIntegerData_, String name_) {
        //this.setBackground(this.bgColor);
        //this.setOpaque(true);
        
        setName(name_);
        if (getName() == null)
        {
            setName("");
        }
        
        this.setPreferredSize(new Dimension(this.prefWidth, this.prefHeight));
        this.setSize(new Dimension(this.prefWidth, this.prefHeight));
        
        plotRect = new Rectangle();
        plotRect.setBounds(horzInset,
                vertInset,
                getSize().width - 2*horzInset,
                getSize().height - 2*vertInset);
        theIntegerData = theIntegerData_;
        
        nPoints = theIntegerData_.length;
        ymax = Double.MIN_VALUE;
        for (int i=0;i<nPoints;i++) {
            if (theIntegerData_[i] > ymax) {
                ymax = theIntegerData_[i];
            }
        }

        int min = ((nPoints / 5) * 2);
        if(ymax < min)
        {
            ymax = min;
        }
    }
    
    public void writeImageToPNG(File fileToWriteTo) throws IOException{
        image = new BufferedImage(this.prefWidth, this.prefHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)image.createGraphics();
        doPaint(g2);
        g2.dispose();
        ImageIO.write(image, "png", fileToWriteTo);
    }
    
    public void display(){
        this.setVisible(true);
        this.validate();
    }
    
    public void doPaint(Graphics g){
        this.graphics = g;
        this.setBackground(bgColor); 
        // define the location and size of the plot area:
        
        int h = 0;
       
        //draw bg
        this.graphics.setColor(this.bgColor);
        this.graphics.fillRect(0, 0, getSize().width, getSize().height);
        //draw box
        //this.graphics.setColor(this.gridColor);
        //this.graphics.drawRect(plotRect.x, plotRect.y, plotRect.width, plotRect.height);
        
        drawGrid();
        drawAxisLabel();
        
        plotPoints();
        drawTitle();
        
        super.paint(g);  
    }
    
    /** Set horizontal inset for plot area from canvas boundary
     * @param h horizontal inset for plot area from canvas boundary
     */
    public void setHorzInset(int h) {
        horzInset = h;
    }
    
    /** Set vertical inset for plot area from canvas boundary
     * @param v vertical inset for plot area from canvas boundary
     */
    public void setVertInset(int v) {
        vertInset = v;
    }
    
    /** Get horizontal inset for plot area from canvas boundary
     * @return horizontal inset for plot area from canvas boundary
     */
    public int getHorzInset() {
        return horzInset;
    }
    
    /** Get vertical inset for plot area from canvas boundary
     * @return vertical inset for plot area from canvas boundary
     */
    public int getVertInset() {
        return vertInset;
    }
    
    /** Get plotObject preferred height
     *  @return the preferred height of the plot object
     */
    public int getPlotObjectHeight()
    {
        return prefHeight;
    }
    
     /**
     * Paints the plot frame.
     * @param g Graphics Object to use to paint the frame.
     */
    public void paint(Graphics g)  {
        //System.out.println("paint");
        this.doPaint(g);
        
        super.paint(g); 
    }
    
    private void drawGrid() {
        int xAxisPos = plotRect.y + plotRect.height;
        int horizontalDivisions = 4;
        int verticalDivisions = 3;
        
        int verticalDividerSpacing = (plotRect.width / (verticalDivisions));
        int horizontalDividerSpacing = (plotRect.height / (horizontalDivisions));
        
        graphics.setColor(this.textColor);
        graphics.setFont(textFont);
        
        //draw origin text for x axis
        //graphics.drawString("" + 0, this.horzInset,  xAxisPos + 10);
        
        //draw origin text for y axis
        //graphics.drawString("" + 0, this.horzInset - 10,  xAxisPos);
        
        // vertical gridlines:
        for (int i = 0; i <= verticalDivisions; i++) {
            int pos = plotRect.x + (verticalDividerSpacing*i);
            if (i > 0)
            {
                pos += 1;
            }
            graphics.setColor(this.gridColor);
            graphics.drawLine(pos, plotRect.y, pos, (plotRect.y + plotRect.height) - 1);
            
            graphics.setColor(this.textColor);
            graphics.drawString("" + (int)(Math.floor( ( (double)nPoints / (double)verticalDivisions ) * (double)i )), pos - 10,  xAxisPos + 10);
        }
        
        // horizontal gridlines:
        for (int i = 0; i <= horizontalDivisions; i++) {
            int pos = plotRect.y + (horizontalDividerSpacing*i);
            graphics.setColor(this.gridColor);
            graphics.drawLine(plotRect.x, pos, (plotRect.x + plotRect.width) - 1, pos);
            
            graphics.setColor(this.textColor);
            graphics.drawString("" + (int)(Math.rint((double)ymax / ((double)horizontalDivisions)) * (double)(horizontalDivisions - i)), 2, pos + 3);
            
        }
        
        
        
        
    }
    
    private void drawAxisLabel() {
        int xAxisPos = plotRect.y + plotRect.height;
        
        graphics.setColor(this.textColor);
        graphics.drawString("Track index", plotRect.x + (plotRect.width / 2) - 20, plotRect.y + plotRect.height + 28);
        
    }
    
    /**
     * Draw the plot title.
     */
    public void drawTitle() {
        graphics.setColor(this.textColor);
        graphics.setFont(textFont);
        graphics.drawString(getName(), this.horzInset,  13);
    }
    
    private void plotPoints() {
        if (nPoints != 0) {
            graphics.setColor(plotColor);
            
            int xAxisPos = plotRect.y + plotRect.height;
            
            //System.out.println("start: " + startIdx + " endIdx: " + endIdx);
            float xScale = (float)plotRect.width/(float)(this.nPoints+1);
            float yScale = (float)plotRect.height/(float)ymax;
            
            int[] xCoords = new int[nPoints];
            int[] yCoords = new int[nPoints];
            graphics.setColor(this.plotColor);
                
            //scale data
            float[] scaledData = new float[nPoints];
            
            if (this.theData != null)
            {
                for (int i = 0; i < scaledData.length; i++) {
                    scaledData[i] = (float)theData[i] * yScale;
                }
            }else if(this.theIntegerData != null)
            {
                for (int i = 0; i < scaledData.length; i++) {
                    scaledData[i] = (float)theIntegerData[i] * yScale;
                } 
            }else{
                throw new RuntimeException("No data found to plot!!!");
            }
            
            //sort the data
            Arrays.sort(scaledData);
            
            for (int i = 0; i < nPoints; i++) {
                xCoords[i] = plotRect.x + Math.round(i * xScale);
                yCoords[i] = xAxisPos - Math.round(scaledData[i]);
            }
            graphics.drawPolyline(xCoords, yCoords, nPoints);
        }
    }
    
    //test harness
    public static void main(String[] args) {
        double max = 5000;
        int[] testData = new int[300];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (int)(Math.random() * max);
        }
        
        JFrame theFrame = new JFrame("Test harness frame");
        ResultCurvePlot plot = new ResultCurvePlot(testData, "test harness");
        theFrame.add(plot);
        theFrame.setSize(plot.getWidth() + 20, plot.getHeight() + 40);
        theFrame.validate();
        theFrame.setVisible(true);
        //theFrame.setContentPane(plot);
        //theFrame.validate();
        //theFrame.setVisible(true);
        plot.display();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
