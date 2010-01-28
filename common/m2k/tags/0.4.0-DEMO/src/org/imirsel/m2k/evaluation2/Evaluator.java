/*
 * Evaluator.java
 *
 * Created on 23 October 2006, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import java.io.File;
import org.imirsel.m2k.util.noMetadataException;

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
     * Evaluates a 2D array of EvaluationDataObject Objects against an a
     * groundtruth object of the same type. Each EvaluationDataObject from 
     * dataToEvaluate represents the results from a different system, the first
     * index denoting the system and the second denoting the fold. 
     * @param dataToEvaluate The data (EvaluationDataObject Objects) to evaluate.
     * The array is indexed [system][fold].
     * @param groundTruth The groundtruth EvaluationDataObject Object to 
     * evaluate against.
     * @param outputDir Directory to write any plots and other reports to.
     * @return Return a String containing the evaluation report.
     * @throws noMetadataException Thrown if an important metadata Object is 
     * not found in the ground-truth or evaluation data.
     */
    public String evaluate(String[] systemNames, EvaluationDataObject[][] dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException;
    
    
    
    public String evaluateResultsAgainstGT(String systemName, EvaluationDataObject dataToEvaluate, EvaluationDataObject groundTruth, File outputDir) throws noMetadataException;
    
}
