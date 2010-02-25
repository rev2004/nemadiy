/*
 * SignalToFileObject.java
 *
 * Created on May 11, 2005, 5:45 PM
 */

//import ncsa.d2k.core.modules.*;
package org.imirsel.meandre.m2k.io.file;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContext;

/**
 * A module that pulls a Signal input, extracts file location metadata and
 * creates a File object from the input Signal's file location metadata.
 *
 * @author Andreas Ehmann
 * Mofified by Lily Dong
 */
@Component(creator = "Andreas Ehmann", description = "take a file Object as input and create a signal objec.", name = "FileToSignalObject", tags = "file reader")
public class FileToSignalObject implements ExecutableComponent {
    @ComponentInput(description = "file name", name = "fileName")
    final static String DATA_INPUT = "fileName";
    @ComponentOutput(description = "signal object", name = "signal")
    final static String DATA_OUTPUT = "signal";
    @ComponentProperty(defaultValue = "true", description = "determine whether debugging information is output to console", name = "verbose")
    final static String DATA_PROPERTY = "verbose";
    private boolean verbose = true;

    /**
     * Sets the value of verbose
     *
     * @param value the value which verbose is set to
     *
     * @see #getVerbose
     */
    public void setVerbose(boolean value) {
        this.verbose = value;
    }

    /**
     * Returns the value of verbose
     *
     * @return verbose
     *
     * @see #setVerbose
     */
    public boolean getVerbose() {
        return this.verbose;
    }

    /**
     * Returns an array of description objects for each property of the Module.
     *
     * @return an array of description objects for each property of the Module.
     */
    /*public PropertyDescription[] getPropertiesDescriptions()
    {
    PropertyDescription[] pds = new PropertyDescription[1];
    pds[0] = new PropertyDescription("verbose", "Verbose output",
    "Determines whether debugging information is output to the console.");
    return pds;
    }*/
    /**
     * Returns the name of the module.
     *
     * @return the module name
     */
    public String getModuleName() {
        return "File to Signal Object";
    }

    /**
     * Returns information about the module.
     *
     * @return Module information
     */
    public String getModuleInfo() {
        return "<p>Overview: This module takes a File Object as input and " +
                "creates a Signal object populating the Signal's file location metadata." +
                "</p><p>Data Handling: Input Data is destroyed.</p>";
    }

    /**
     * Returns a text name for the given input.
     *
     * @param i
     *          the index of the input
     *
     * @return the name of the indexed input
     */
    public String getInputName(int i) {
        switch (i) {
            case 0:
                return "File Object";
            default:
                return "NO SUCH INPUT!";
        }
    }

    /**
     * Returns a text description for the indicated input.
     *
     * @param i
     *          the index of the input
     *
     * @return a text description of the indexed input
     */
    public String getInputInfo(int i) {
        switch (i) {
            case 0:
                return "A File Object";
            default:
                return "No such input";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of the input.
     *
     * @return the fully qualified java types for each of the inputs
     */
    public String[] getInputTypes() {
        String[] types = {"java.io.File"};
        return types;
    }

    /**
     * Returns a text name for the given output.
     *
     * @param i
     *          the index of the output
     *
     * @return the name of the indexed output
     */
    public String getOutputName(int i) {
        switch (i) {
            case 0:
                return "Signal Object";
            default:
                return "NO SUCH OUTPUT!";
        }
    }

    /**
     * Returns a text description for the given output.
     *
     * @param i
     *          the index of the output
     *
     * @return the name of the indexed output
     */
    public String getOutputInfo(int i) {
        switch (i) {
            case 0:
                return "A Signal object with the file location metadata set by the input file";
            default:
                return "No such output";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of the outputs.
     *
     * @return the fully qualified java types for each of the outputs.
     */
    public String[] getOutputTypes() {
        String[] types = {"org.imirsel.m2k.util.Signal"};
        return types;
    }

    public void initialize(ComponentContextProperties ccp) {
    }

    public void dispose(ComponentContextProperties ccp) {
    }

    /**
     * Execution method: Pull a Signal input, extract file location metadata and
     * create a File object from the input Signal's file location metadata.
     */
    public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
        //throws noMetadataException {

        Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
        if (object1 instanceof StreamTerminator) {
            cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
        } else {
            String location = (String) object1;

            File inputFile = new File(location);
            System.out.println("FileToSignalObject:location="+location);

            String filePath = inputFile.getAbsolutePath();

            Signal outputSignal = new Signal(filePath);
            if (verbose) {
                try {
                    System.out.println("FileToSignalObject:Outputing: " + outputSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
                } catch (noMetadataException ex) {
                    Logger.getLogger(FileToSignalObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            cc.pushDataComponentToOutput(DATA_OUTPUT, outputSignal);
        }
    }
}



