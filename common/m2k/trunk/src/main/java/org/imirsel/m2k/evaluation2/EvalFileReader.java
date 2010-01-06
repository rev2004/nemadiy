/*
 * EvalFileReader.java
 *
 * Created on 23 October 2006, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import java.io.File;
import org.imirsel.m2k.util.Signal;

/**
 *
 * @author kw
 */
public interface EvalFileReader {
    
    public boolean getVerbose();
    
    public void setVerbose(boolean verbose_);
    
    public EvaluationDataObject readFile(File theFile);
    
}
