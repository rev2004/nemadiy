/*
 * SecondOrderButterworthFilterClass.java
 *
 * Created on 26 July 2006, 11:34
 *
 */

package org.imirsel.m2k.filters;

/**
 *
 * @author kris
 */
public class SecondOrderButterworthFilterClass {
    
    /** Number of poles. */
    private static final int NZEROS = 2;
    /** Number of zeros. */
    private static final int NPOLES = 2;
    
    /** feedback coefficent 1. */
    double recurCoef1 = -0.2970501108;
    /** feedback coefficent 2. */
    double recurCoef2 = -0.1872146844;
    
    /** filter gain. */
    double gain = 2.694936923;

    
    private double[] xv;
    private double[] yv;
    
    private int shift = 2;
    
    /** Creates a new instance of SecondOrderButterworthFilterClass */
    public SecondOrderButterworthFilterClass() {
        xv = new double[NZEROS+1];
        yv = new double[NPOLES+1];
    }
    
    /** Creates a new instance of SecondOrderButterworthFilterClass */
    public SecondOrderButterworthFilterClass(double gain_, double recurCoef1_, double recurCoef2_) {
        xv = new double[NZEROS+1];
        yv = new double[NPOLES+1];
        recurCoef1 = recurCoef1_;
        recurCoef2 = recurCoef2_;
        gain = gain_;
    }
    
    public double[] filter(double[] input)
    {
        double[] output = new double[input.length];
        
        for (int i = 0; i < input.length; i++ ) {
            xv[0] = xv[1]; 
            xv[1] = xv[2]; 
            xv[2] = input[i] / this.gain;
            yv[0] = yv[1]; 
            yv[1] = yv[2]; 
            yv[2] = (xv[0] + xv[2]) + 2 * xv[1]
                  + ( this.recurCoef2 * yv[0]) + (  this.recurCoef1 * yv[1]);
            if (i>=shift){
                output[i-shift] = yv[2];
            }
        }
        return output;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
    
}
