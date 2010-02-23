
package org.imirsel.m2k.transforms;

import java.io.Serializable;

/**
 * Class to support function of DCT.java
 *
 * Computes the Discrete Cosine Transform (DCT) of real numbers. If the number
 * of coefficients (NumCoef) is less than the dimensionality of the input vector, only the
 * first NumCoef values will be outputted.  If numCoefs is greater than the
 * dimensionality of the input vector, the input vector will be zero-padded to the
 * appropriate length.
 *
 * @author Kris West & Andreas Ehmann
 */
public class DCTClass implements Serializable{
    
    private int numCoefs = 13;
    private boolean keepFirstCoef = true;
    
    /**
     * Sets the number of coefficients
     *
     * @param value number of coefficients of DCT
     */
    public void setNumCoefs(int value) {
        this.numCoefs = value;
    }
    
    /**
     * Returns the number of coefficients
     *
     * @return the number of coefficients
     */
    public int getNumCoefs() {
        return this.numCoefs;
    }
    
    /**
     * Sets the keep first coefficent flag
     *
     * @param value keep first coef. flag
     */
    public void setKeepFirstCoef(boolean value) {
        this.keepFirstCoef = value;
    }
    
    /**
     * Returns the keep first coef. flag
     *
     * @return flag
     */
    public boolean getKeepFirstCoef() {
        return this.keepFirstCoef;
    }
    
    /** Creates a new instance of DCTClass */
    public DCTClass() {
        numCoefs = 15;
        keepFirstCoef = true;
    }
    
    /** Creates a new instance of DCTClass */
    public DCTClass(int numCoefs_, boolean keepFirstCoef_) {
        numCoefs = numCoefs_;
        keepFirstCoef = keepFirstCoef_;
    }
    
    private double[][] DCTMatrix = null;
    private int oldp = 0;
    
    
    /**preform the DCT initializing if necessary*/
    public double[] performDCT(double[] Input){
        int p = Input.length;
        
        // Zero Pad if needed
        if (numCoefs > p) {
            double[] ZeroPaddedInput = new double[numCoefs];
            for (int i=0; i<numCoefs; i++){
                if (i < p) {
                    ZeroPaddedInput[i]=Input[i];
                } else {
                    ZeroPaddedInput[i]=0.0;
                }
            }
            Input = ZeroPaddedInput;
            p = numCoefs;
        }
        
        // Initialize DCT Matrix
        if (DCTMatrix == null || p != oldp){
            
            DCTMatrix = new double[numCoefs][p];
            for (int i = 0; i < numCoefs; i++) {
                for (int j = 0; j < p; j++) {
                    DCTMatrix[i][j] = 1/Math.sqrt(p/2)*Math.cos((double)i*((double)j+0.5)*Math.PI/p);
                }
            }
            for (int j = 0; j < p; j++) {
                DCTMatrix[0][j] = DCTMatrix[0][j]/Math.sqrt(2);
            }
        }
        
        // Initialize Ouput
        double[] Output = null;
        
        // Apply DCT
        if (keepFirstCoef == true) {
            Output = new double[numCoefs];
            for (int i = 0; i < numCoefs; i++) {
                for (int j = 0; j < p; j++) {
                    Output[i] += DCTMatrix[i][j]*Input[j];
                }
            }
        }else{
            Output = new double[numCoefs-1];
            for (int i = 0; i < numCoefs-1; i++) {
                for (int j = 0; j < p; j++) {
                    Output[i] += DCTMatrix[i+1][j]*Input[j];
                }
            }
        }
        
        
        oldp = p;
        return Output;
    }
    
}
