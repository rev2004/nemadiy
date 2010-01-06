/*
 * FFTRealClass.java
 *
 * Created on 10 January 2006, 19:22
 *
 */

package org.imirsel.m2k.transforms;

import java.io.Serializable;

/**
 * Computes an the FFT of an input array.
 */
public class FFTRealClass implements Serializable{
    
    private double[] weightFft;
    private double[] weightFftTimesFrom2;
    private boolean inverse = false;
    
    /**
     * Creates a new instance of FFTRealClass
     */
    public FFTRealClass() {
    }
    
    /**
     * Sets inverse flag
     *
     * @param value the inverse flag
     */
    public void setinverse(boolean value) {
        this.inverse = value;
    }
    
    /**
     * Returns the value of inverse flag
     *
     * @return the value of inverse flag
     */
    public boolean getinverse() {
        return this.inverse;
    }
    
    /**
     * Initializes the <b>weightFft[] </b> vector. (i.e. Twiddle factors)
     * <p>
     * <b>weightFft[k] = w ^ k </b>
     * </p>
     * where:
     * <p>
     * <b>w = exp(-2 * PI * i / N) </b>
     * </p>
     * <p>
     * <b>i </b> is a complex number such that <b>i * i = -1 </b> and <b>N </b> is
     * the number of points in the FFT. Since <b>w </b> is complex, this is the
     * same as
     * </p>
     * <p>
     * <b>Re(weightFft[k]) = cos ( -2 * PI * k / N) </b>
     * </p>
     * <p>
     * <b>Im(weightFft[k]) = sin ( -2 * PI * k / N) </b>
     * </p>
     *
     * @param numberFftPoints
     *          number of points in the FFT
     * @param invert
     *          whether it's direct (false) or inverse (true) FFT
     *
     */
    private void createWeightFft(int numFftPoints, boolean invert) {
        /**
         * weightFFT will have numberFftPoints/2 complex elements.
         */
        weightFft = new double[numFftPoints];
        
        /**
         * For the inverse FFT, w = 2 * PI / numberFftPoints;
         */
        double w = -2.0 * Math.PI / numFftPoints;
        if (invert) {
            w = -w;
        }
        
        for (int k = 0; k < (numFftPoints >> 1); k++) {
            weightFft[2 * k] = Math.cos(w * k);
            weightFft[2 * k + 1] = Math.sin(w * k);
        }
    }
    
    /**
     * Establish the recursion. The FFT computation will be computed by as a
     * recursion. Each stage in the butterfly will be fully computed during
     * recursion. In fact, we use the mechanism of recursion only because it's the
     * simplest way of switching the "input" and "output" vectors. The output of a
     * stage is the input to the next stage. The butterfly computes elements in
     * place, but we still need to switch the vectors. We could copy it (not very
     * efficient...) or, in C, switch the pointers. We can avoid the pointers by
     * using recursion.
     *
     * @param input
     *          input sequence
     * @param output
     *          output sequence
     * @param numFftPoints
     *          number of points in the FFT
     * @param invert
     *          whether it's direct (false) or inverse (true) FFT
     *
     */
    private void recurseFft(double[] input, double[] output, int numFftPoints, boolean invert) {
        
        double divisor;
        
        /**
         * The direct and inverse FFT are essentially the same algorithm, except for
         * two difference: a scaling factor of "numberFftPoints" and the signal of
         * the exponent in the weightFft vectors, defined in the method
         * <code>createWeightFft</code>.
         */
        
        if (!invert) {
            divisor = 1.0;
        } else {
            divisor = (double) numFftPoints;
        }
        
        /**
         * Initialize the input and output for the butterfly recursion
         */
        for (int i = 0; i < 2*numFftPoints; i++){
            output[i] = 0.0;
            input[i]= input[i] / divisor;
        }
        
        /**
         * Repeat the recursion log2(numberFftPoints) times, i.e., we have
         * log2(numberFftPoints) butterfly stages.
         */
        butterflyStage(input, output, numFftPoints, numFftPoints >> 1);
        
        return;
    }
    
    private void butterflyStage(double[] from, double[] to, int numFftPoints, int currentDistance) {
        int ndx1From;
        int ndx2From;
        int ndx1To;
        int ndx2To;
        int ndxWeightFft;
        
        if (currentDistance > 0) {
            int twiceCurrentDistance = 2 * currentDistance;
            
            for (int s = 0; s < currentDistance; s++) {
                ndx1From = s;
                ndx2From = s + currentDistance;
                ndx1To = s;
                ndx2To = s + (numFftPoints >> 1);
                ndxWeightFft = 0;
                while (ndxWeightFft < (numFftPoints >> 1)) {
                    /**
                     * <b>weightFftTimesFrom2 = weightFft[k] </b> <b>*from[ndx2From] </b>
                     */
                    weightFftTimesFrom2 = multiplyCmplx(weightFft[2*ndxWeightFft],weightFft[2*ndxWeightFft+1], from[2*ndx2From], from[2*ndx2From+1]);
                    /**
                     * <b>to[ndx1To] = from[ndx1From] </b> <b>+ weightFftTimesFrom2 </b>
                     */
                    to[2*ndx1To] = from[2*ndx1From]+ weightFftTimesFrom2[0];
                    to[2*ndx1To+1] = from[2*ndx1From+1]+ weightFftTimesFrom2[1];
                    
                    /**
                     * <b>to[ndx2To] = from[ndx1From] </b> <b>- weightFftTimesFrom2 </b>
                     */
                    to[2*ndx2To] = from[2*ndx1From] - weightFftTimesFrom2[0];
                    to[2*ndx2To+1] = from[2*ndx1From+1] - weightFftTimesFrom2[1];
                    
                    ndx1From += twiceCurrentDistance;
                    ndx2From += twiceCurrentDistance;
                    ndx1To += currentDistance;
                    ndx2To += currentDistance;
                    ndxWeightFft += currentDistance;
                }
            }
            
            /**
             * This call'd better be the last call in this block, so when it returns
             * we go straight into the return line below.
             *
             * We switch the <i>to </i> and <i>from </i> variables, the total number
             * of points remains the same, and the <i>currentDistance </i> is divided
             * by 2.
             */
            butterflyStage(to, from, numFftPoints, (currentDistance >> 1));
        }
        return;
    }
    
    /**
     * Simple method for doing complex multiplication.  Has 4 inputs, for the real and
     * imaginary parts of the two numbers to be multiplied
     * @param real1
     * @param imag1
     * @param real2
     * @param imag2
     * @return
     */
    double[] multiplyCmplx(double real1, double imag1, double real2, double imag2) {
        double[] output = new double[2];
        output[0] = real1 * real2 - imag1 * imag2;
        output[1] = real1 * imag2 + real2 * imag1;
        return output;
    }
    
    /** Performs the FFT and returns a 2D double array containing the real (0) and
     * imaginary (1) parts of the spectrum. Array is indexed [real/imag][bin].
     * @param dataReal Data to FFT
     * @param truncate Determines whether the whole or only the positive frequency
     * spectrum is returned.
     * @return real and imaginery spectrum
     */
    public double[][] doFFT(double[] dataReal, boolean truncate) {
        /* compute the next power of two */
        double dnn = Math.log((double) dataReal.length) / Math.log(2.0);
        int log2nn = (int) Math.ceil(dnn);
        
        int fftLength = (int) Math.pow(2.0, (double) log2nn);
        
        /* zeropad to next power of 2 and construct a 'complex' data array
         where even elements are the real portion and the adjacent (+1) odd
         elements are the corresponding imaginary portion (i.e. 0). */
        double[] dataCmplx = new double[2 * fftLength];
        for (int k = 0; k < fftLength; k++) {
            if (k < dataReal.length) {
                dataCmplx[2 * k] = dataReal[k];
                dataCmplx[2 * k + 1] = 0.0;
            } else {
                dataCmplx[2 * k] = 0.0;
                dataCmplx[2 * k + 1] = 0.0;
            }
        }
        
        double[] output = new double[fftLength*2];
        if ((weightFft == null)||(weightFft.length != fftLength))
        {
            createWeightFft(fftLength, inverse);
        }
        
        recurseFft(dataCmplx, output, fftLength, inverse);
        
        double[][] outputArr = new double[2][];
        
        if (truncate){
            int length = (fftLength/2) + 1;
            outputArr[0] = new double[length];
            outputArr[1] = new double[length];
            
            if ( (log2nn % 2) == 1) {
                for (int i = 0; i < length; i++) {
                    outputArr[0][i] = output[2*(i+1)];
                    outputArr[1][i] = output[2*(i+1)+1];
                }
            } else {
                for (int i = 0; i < length; i++) {//for (int i = 1; i <= length; i++) {
                    outputArr[0][i] = dataCmplx[2*(i+1)];
                    outputArr[1][i] = dataCmplx[2*(i+1)+1];
                }
            }
        }else{
            outputArr[0] = new double[fftLength];
            outputArr[1] = new double[fftLength];
            
            if ( (log2nn % 2) == 1) {
                for (int i = 0; i < fftLength; i++) {
                    outputArr[0][i] = output[2*i];
                    outputArr[1][i] = output[2*i+1];
                }
            } else {
                for (int i = 0; i < fftLength; i++) {
                    outputArr[0][i] = dataCmplx[2*i];
                    outputArr[1][i] = dataCmplx[2*i+1];
                }
            }
        }
        return outputArr;
    }
    
    public double[] fftMagnitude(double[] input) {
        double[][] complexFFT = doFFT(input, true);
        double[] mag = new double[complexFFT[0].length];
        
        for (int i = 0; i < complexFFT[0].length; i++ ) {
            mag[i] = Math.sqrt(complexFFT[0][i]*complexFFT[0][i]+complexFFT[1][i]*complexFFT[1][i]);
        }
        return mag;
    }
    
    public double[] fftScaledMagnitude(double[] input) {
        double[] data = fftMagnitude(input);
        data[0] /= input.length;
        //data[data.length-1] /= input.length;
        for (int i = 1; i < data.length-1; i++) {
            data[i] = (data[i] * 2) / (double)input.length;
        }
        return data;
    }
    
    public double[] fftPowerSpectrum(double[] input) {
        double[] mag = fftScaledMagnitude(input);
        
        for (int i = 0; i < mag.length; i++ ) {
            mag[i] = Math.pow(mag[i],2);
        }
        return mag;
    }
    
    public double[] fftPhase(double[] input) {
        double[][] complexFFT = doFFT(input, true);
        double[] phase = new double[complexFFT[0].length];
        
        for (int i = 0; i < complexFFT[0].length; i++ ) {
            phase[i] = Math.atan2(complexFFT[1][i],complexFFT[0][i]);
        }
        return phase;
    }
}
