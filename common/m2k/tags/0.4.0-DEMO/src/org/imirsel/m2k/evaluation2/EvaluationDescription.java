/*
 * EvaluationDescription.java
 *
 * Created on 23 October 2006, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import java.io.File;

/**
 *
 * @author kw
 */
public interface EvaluationDescription {
    
    public String getEvaluationType();
    
    public String getEvaluationInstanceName();
    
    public void setEvaluationInstanceName(String name);
    
   //public void setEvaluationFiles(File[] theEvalFiles, File[][] theEvalGroundTruthFiles);
    
    /**
     * Evaluates a set of result files on disk against groundtruth files on disk
     */
    public String runOfflineEvaluation(File[] resultFiles, File[][] theGroundTruthFiles);
    
    /**
     * Run binary code (external to the Java VM) on a set files on disk (probably 
     * audio or MIDI files) to produce result files and then evaluates against
     * groundtruth files on disk.
     */
    public String runOnlineEvaluation(File workingDirectory, File resultDirectory, File[] evalFiles, File[][] theGroundTruthFiles, ExternalBinaryIntegrator theExternalCode);
    
}
