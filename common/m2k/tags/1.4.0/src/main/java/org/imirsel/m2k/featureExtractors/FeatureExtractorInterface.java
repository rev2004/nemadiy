/*
 * FeatureExtractorInterface.java
 *
 * Created on 23 April 2007, 09:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.featureExtractors;

import org.imirsel.m2k.util.Signal;

/**
 *
 * @author kw
 */
public interface FeatureExtractorInterface {
    
    public void init(Signal aSignal);
    
    public double[] computeFeaturesFromSpectra(double[] FFTMagnitudes);
    
    public String[] getColNames();
     
}
