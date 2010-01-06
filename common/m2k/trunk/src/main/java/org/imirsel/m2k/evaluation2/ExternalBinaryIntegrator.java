/*
 * ExternalBinaryIntegrator.java
 *
 * Created on 23 October 2006, 23:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 *
 * @author kw
 */
public interface ExternalBinaryIntegrator {
    
    /** Kills the process representing the command
     */
    public void killProcess();
    
    /**
     * Prints out the buffered input stream
     * @param inputStream the <code>InputStream</code> to be printed out in the console
     * @throws Exception Thrown if the <code>InputStream</code> is unavailable
     */
    void ProcessInputStream(BufferedInputStream inputStream) throws Exception;
    
    
    public void runCommand(final String MainCommand, final String WorkingDir, final String commandFormattingStr, final String[] inputFilenames, final String OutputFilename, String[] envVariables) throws IOException, RuntimeException;
   
}
