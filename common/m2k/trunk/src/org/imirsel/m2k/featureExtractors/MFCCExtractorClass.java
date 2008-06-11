/*
 * MFCCExtractorClass.java
 *
 * Created on 23 April 2007, 08:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.featureExtractors;

import org.imirsel.m2k.filters.MelFilterBankClass;
import org.imirsel.m2k.math.Mathematics;
import org.imirsel.m2k.transforms.DCTClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kw
 */
public class MFCCExtractorClass implements FeatureExtractorInterface{
    
    MelFilterBankClass filters;
    DCTClass transform;
    private int numCoefs = 15;
    private boolean keep0 = false;
    private double low = 0.0;
    int numFilters = 40;

    public MelFilterBankClass getFilters() {
        return filters;
    }
    private double high = 0.5;
    
    /** Creates a new instance of MFCCExtractorClass */
    public MFCCExtractorClass(int numCoefs_, boolean keep0_) {
        transform = new DCTClass(numCoefs_, keep0_);    
    }
    
    /** Creates a new instance of MFCCExtractorClass */
    public MFCCExtractorClass() {
        transform = new DCTClass(numCoefs, keep0);    
    }
    
    public void init(Signal aSignal){
        int SampleRate = -1;
        try {
            SampleRate = aSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE);
        } catch (noMetadataException ex) {
            ex.printStackTrace();
        }
        filters = new MelFilterBankClass(numFilters,SampleRate,low,high);
    }
    
    public double[] extractMFCCsFromSpectra(double[] FFTMagnitudes){
        double[] filterCoeffs = filters.filter(FFTMagnitudes);
        double[] dctCoeffs = transform.performDCT(filterCoeffs);
        return Mathematics.logN(dctCoeffs,0);
    }

    public int getNumCoefs() {
        return numCoefs;
    }

    public void setNumCoefs(int numCoefs) {
        this.numCoefs = numCoefs;
    }

    public boolean isKeep0() {
        return keep0;
    }

    public void setKeep0(boolean keep0) {
        this.keep0 = keep0;
    }
    
    public String[] getColNames(){
        int numFeats = this.numCoefs;
        if (!keep0){
            numFeats--;
        }
        String[] names = new String[numFeats];
        if (keep0){
            for (int i = 0; i < names.length; i++) {
                names[i] = "MFCC" + i;
            }
        }else{
            for (int i = 0; i < names.length; i++) {
                names[i] = "MFCC" + (i + 1);
            }
        }
        return names;
    }
    
}
