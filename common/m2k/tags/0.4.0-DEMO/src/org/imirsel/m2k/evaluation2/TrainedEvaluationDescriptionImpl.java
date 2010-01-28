/*
 * TrainedEvaluationDescriptionImpl.java
 *
 * Created on 23 October 2006, 22:40
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
public abstract class TrainedEvaluationDescriptionImpl extends EvaluationDescriptionImpl implements TrainedEvaluationDescription{
    
//    File[] trainingFiles = null;
//    File[][] trainingGroundTruthFiles = null;
//    
    
    /** Creates a new instance of TrainedEvaluationDescriptionImpl */
    public TrainedEvaluationDescriptionImpl(String name) {
        super(name);
    }
    
    /** Run the training phase of the evaluation.
     *  @param trainingFiles The files to train the system to be evaluated on.
     *  @param trainingGroundTruthFiles The groundtruth files to train the system 
     *  to be evaluated on.
     *  @param workingDirectory The directory that the experment being evaluated
     *  should use to store any model or feature files that will be needed by
     *  the test phase of the evaluation.
     *
     *  @return A boolean indicating whether the training process was successful.
     */
    public abstract boolean runTrainingPhase(File workingDirectory, File[] trainingFiles, File[][] trainingGroundTruthFiles, ExternalBinaryIntegrator theExternalCode);
           
}
