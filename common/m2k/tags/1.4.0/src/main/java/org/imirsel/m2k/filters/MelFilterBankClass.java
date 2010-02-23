package org.imirsel.m2k.filters;

import java.text.DecimalFormat;


/**
 * Passes input through a MelFilterBank. A D2K/M2K module that reads in a double
 * array of Magnitude Spectrum, and returns the bands of the Filter bank.
 *
 * @author Andreas Ehmann & Kris West
 */
public class MelFilterBankClass {

    private int numFilters = 32;
    private double[][] FilterBank = null;    // The FilterBank

    private int oldn = 0;
    private double fl = 0.0;
    private double fh = 0.5;
    private int fs;
    private int mn;                      // FFT bin# corresponding to fl

    private int mx;                      // FFT bin# corresponding to fh

    /** Creates a new instance of MelFilterBankClass */

    public MelFilterBankClass() {
        setNumFilters(32);
        setCurrentSampleRate(44100);
        setLowestFreq(0.0);
        setHighestFreq(0.5);
    }

    public MelFilterBankClass(int numberOfFilters, int sampleRate, double lowestFrequency, double higherstFrequency) {
        setNumFilters(numberOfFilters);
        setCurrentSampleRate(sampleRate);
        setLowestFreq(lowestFrequency);
        setHighestFreq(higherstFrequency);
    }

    public String[] getColNames() {
        String[] names = new String[numFilters];
        for (int i = 0; i < numFilters; i++) {
            names[i] = "MelFilter_" + numFilters + " " + i;
        }
        return names;
    }

    private void createFilterBank(int vectorLength) {
        
        System.out.println("Creating Mel-filterbank for vector length: " + vectorLength);
        int n = 2 * vectorLength;

        int fn2 = (int) Math.floor(n / 2);

        int i = 0;
        int j = 0;
        double f0 = 700.0 / (double) fs;
        double lr = Math.log((f0 + fh) / (f0 + fl)) / ((double) this.numFilters + 1);

        // System.out.println("lr: " + lr);
        int b1 = (int) Math.floor(n * fl);
        int b2 = (int) Math.ceil(n * ((f0 + fl) * Math.exp(lr) - f0)) - 1;
        int b3 = (int) Math.floor(n * ((f0 + fl) * Math.exp(lr * (double) this.numFilters) - f0)) - 1;
        int b4 = (int) Math.ceil(n * ((f0 + fl) * Math.exp(lr * ((double) this.numFilters + 1)) - f0));

        if (b4 > fn2) {
            b4 = fn2;
        }

        b4 = b4 - 2;

        int pflength = b4 - b1 + 1;

        double[] pf = new double[pflength];
        double[] fp = new double[pflength];
        double[] pm = new double[pflength];

        for (i = 0; i < pflength; i++) {
            pf[i] = Math.log((f0 + (double) (i + 1 + b1) / (double) n) / (f0 + fl)) / lr;
            fp[i] = Math.floor(pf[i]);
            pm[i] = pf[i] - fp[i];
        }

        int k2 = b2 - b1;
        int k3 = b3 - b1;
        int k4 = b4 - b1;
        int k2k4dist = k4 - k2 + 1;
        int k1k3dist = k3 + 1;
        int rlength = k2k4dist + k1k3dist;

        int[] r = new int[rlength];               // Filter #
        int[] c = new int[rlength];               // bin #
        double[] v = new double[rlength];

        for (i = 0; i < k2k4dist; i++) {          // Falling Edges
            r[i] = (int) fp[i + k2];
            c[i] = i + k2;
            v[i] = 2 * (1 - pm[i + k2]);
        }

        for (i = k2k4dist; i < rlength; i++) {    // Rising Edges
            r[i] = (int) (1 + fp[i - k2k4dist]);
            c[i] = i - k2k4dist;
            v[i] = 2 * pm[i - k2k4dist];
        }

        FilterBank = new double[this.numFilters][pflength];

        for (i = 0; i < this.numFilters; i++) {
            for (j = 0; j < pflength; j++) {
                FilterBank[i][j] = 0.0;
            }
        }

        /* Populate the sparse FilterBank Matrix */
        for (i = 0; i < rlength; i++) {
            FilterBank[r[i] - 1][c[i]] = v[i];
        }

        mn = b1 + 1;
        mx = b4 + 1;

//        DecimalFormat dec = new DecimalFormat();
//        dec.setMaximumFractionDigits(3);
//        dec.setMinimumFractionDigits(3);
//        System.out.println("Mel-scale Filterbank:");
//        for (int x = 0; x < FilterBank.length; x++) {
//            System.out.print("Filter " + x +": ");
//            for (int y=0; y < FilterBank[x].length; y++) {
//                    System.out.print(dec.format(FilterBank[x][y]) + " ");
//            }
//            System.out.println("");
//        }
//        System.out.println("");
    }

    public double[] filter(double[] fftMagCoeffs) {
        int n = fftMagCoeffs.length;
        /* If FilterBank has not been yet created, create it! */
        if ((FilterBank == null) || (n != oldn)) {
            createFilterBank(n);
            oldn = n;
        }

        double[] MFBOutput = new double[this.numFilters];

        for (int i = 0; i < this.numFilters; i++) {
            for (int j = mn; j < mx; j++) {
                MFBOutput[i] += FilterBank[i][j - mn] * fftMagCoeffs[j];
            }
        }
        return MFBOutput;
    }

    public int getNumFilters() {
        return numFilters;
    }

    public void setNumFilters(int numFilters) {
        if (this.numFilters != numFilters){
            System.out.println("Mel-filterbank setting number of filters: " + numFilters);
            this.numFilters = numFilters;
            this.FilterBank = null;
        }
    }

    public double getLowestFreq() {
        return fl;
    }

    public void setLowestFreq(double fl) {
        if (this.fl != fl){
            System.out.println("Mel-filterbank setting low freq limit: " + fl);
            this.fl = fl;
            this.FilterBank = null;
        }
    }

    public double getHighestFreq() {
        return fh;
    }

    public void setHighestFreq(double fh) {
        if (this.fh != fh){ 
            System.out.println("Mel-filterbank setting high freq limit: " + fh);
            this.fh = fh;
            this.FilterBank = null;
        }
    }

    public int getCurrentSampleRate() {
        return fs;
    }

    public void setCurrentSampleRate(int fs) {
        if (this.fs != fs){
            System.out.println("Mel-filterbank setting sample rate: " + fs);
            this.fs = fs;
            this.FilterBank = null;
        }
    }
}
