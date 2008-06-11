/*
 * Evaluator.java
 *
 * Created on 23 October 2006, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import org.imirsel.m2k.util.Signal;

/**
 *
 * @author kw
 */
public interface Evaluator {
    
    public boolean getVerbose();
    
    public void setVerbose(boolean verbose_);
    
    /**
     * Returns true if the Evaluator instance returns reports in CSV or raw 
     * text format.
     * @return true if the Evaluator instance returns reports in CSV or raw 
     * text format.
     */
    public boolean returnsInCSV();
    
    /**
     * Evaluates an array of Signal Objects against a 2D array of 
     * groundtruth objects. The first index of the groundtruth array 
     * should be of the same length as the dataToEvaluate array, i.e.
     * dataToEvaluate[n] & groundTruth[n][]. Thus there can be a 
     * variable number of groundtruth Objects (1 or more than 1) for 
     * each Object to be evaluated.
     * @param dataToEvaluate The data (Signal Objects) to evaluate.
     * @param groundTruth The groundtruth Signal Objects to evaluate against.
     * @return Return a String containing the evaluation report.
     */
    public String evaluate(Signal[] dataToEvaluate, Signal[][] groundTruth);
    
}
