/*
 * EvaluationDescriptionImpl.java
 *
 * Created on 23 October 2006, 22:39
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
public abstract class EvaluationDescriptionImpl implements EvaluationDescription{
    
    //File[] trainingFiles = null;
    //File[][] groundTruthFiles = null;
    
    String instanceName = "<no name given>";
    
    /** Creates a new instance of EvaluationDescriptionImpl */
    public EvaluationDescriptionImpl(String instanceName_) {
        instanceName = instanceName_;
    }
    
    public abstract String getEvaluationType();
    
//    public String getEvaluationType(){
//        return this.class.getName();
//    }
    
    public String getEvaluationInstanceName() {
        return instanceName;
    }
    
    public void setEvaluationInstanceName(String name){
        instanceName = name;
    }
    
    //public void setEvaluationFiles(File[] theEvalFiles, File[][] theEvalGroundTruthFiles);
    
    /**
     * Evaluates a set of result files on disk against groundtruth files on disk
     */
    public abstract String runOfflineEvaluation(File[] resultFiles, File[][] theGroundTruthFiles);
    
    /**
     * Run binary code (external to the Java VM) on a set files on disk (probably
     * audio or MIDI files) to produce result files and then evaluates against
     * groundtruth files on disk.
     */
    public abstract String runOnlineEvaluation(File workingDirectory, File resultDirectory, ExternalBinaryIntegrator theExternalCode);
    
}
