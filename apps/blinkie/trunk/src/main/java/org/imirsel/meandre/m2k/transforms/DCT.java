package org.imirsel.meandre.m2k.transforms;

import org.imirsel.m2k.transforms.DCTClass;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

import org.meandre.core.ComponentContextProperties;

/**
 * Computes the Discrete Cosine Transform (DCT) of real numbers. A D2K/M2K module that reads in an array of
 * real numbers, and returns an array of transformed values.
 *
 * @author Andreas Ehmann & Kris West
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann & Kris West",
           description="perform a discrete cosine transform of a 1D input double array",
           name="DCT",
           tags="compute the DCT of the input")

public class DCT implements ExecutableComponent {
    @ComponentInput(description="real part(1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="dct(1-D double array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="13",
                      description="set the number of DCT coefficients to be output by the module",
                      name="numCoefs")
    final static String DATA_PROPERTY_1 = "numCoefs";
    @ComponentProperty(defaultValue="true",
                       description="flag to set if the 0th DCT coefficient is output(useful for MFCCs)",
                       name="keepFirstCoef")
    final static String DATA_PROPERTY_2 = "keepFirstCoef";


    private int numCoefs = 13;
    private boolean keepFirstCoef = true;
    private DCTClass theDCT = null;

    /**
     * Sets the number of coefficients
     *
     * @param value number of coefficients of DCT
     */
    public void setNumCoefs(int value) {
        this.numCoefs = value;
    }

    /**
     * Returns the number of coefficients
     *
     * @return the number of coefficients
     */
    public int getNumCoefs() {
        return this.numCoefs;
    }

    /**
     * Sets the keep first coefficent flag
     *
     * @param value keep first coef. flag
     */
    public void setKeepFirstCoef(boolean value) {
        this.keepFirstCoef = value;
    }

    /**
     * Returns the keep first coef. flag
     *
     * @return flag
     */
    public boolean getKeepFirstCoef() {
        return this.keepFirstCoef;
    }

    /**
     * Returns an array of description objects for each property of
     * the Module.
     *
     * @return an array of description objects for each property of
     * the Module.
     */
/*    public PropertyDescription[] getPropertiesDescriptions() {
        PropertyDescription[] pds = new PropertyDescription[2];

        pds[0] = new PropertyDescription("numCoefs", "Number of Coefficents",
                "Sets the number of DCT coefficents to be output by the module");
        pds[1] = new PropertyDescription("keepFirstCoef", "Output 0th Coefficent",
                "a flag to set if the 0th DCT coefficent is output (useful for MFCCs)." +
                "  When set to false, the output length will be Number of Coefficients - 1.");

        return pds;
    }*/

    /**
     * Returns the name of the module
     *
     * @return the module name
     */
    public String getModuleName() {
        return "DCT";
    }

    /**
     * Returns information about the module
     *
     * @return Module information
     */
    public String getModuleInfo() {
        return "<p>Overview: This module computes the DCT of the input." +
                "</p><p>Detailed Description:  This module performs a Discrete Cosine transform of" +
                "a 1-dimensional input double array.  The number of " +
                "DCT coefficients can be set in the numCoefs field.  If the " +
                "number of coefficients is less than the dimensionality of the input vector," +
                "only the first NumCoef coefficents will be outputted.  If numCoefs is greater" +
                "than the dimensionality of the input vector, the input vector will be zero-padded" +
                "to the appropriate length.  In addition, a Ouput 0th coefficent flag sets whether the" +
                "0th DCT coefficent is output (useful for MFCCs)." +
                "</p><p>Data Handling: The input data is not modified.";
    }

    /**
     * Returns a text name for the indicated input
     *
     * @param i the index of the input
     *
     * @return the name of the indexed input
     */
    public String getInputName(int i) {
        switch (i) {
            case 0:
                return "Real part";
            default:
                return "NO SUCH INPUT!";
        }
    }

    /**
     * Returns a text description for the indicated input
     *
     * @param i the index of the input
     *
     * @return a text description of the indexed input
     */
    public String getInputInfo(int i) {
        switch (i) {
            case 0:
                return "Input";
            default:
                return "No such input";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of
     * the input.
     *
     * @return the fully qualified java types for each of the inputs
     */
    public String[] getInputTypes() {
        String[] types = {
            "[D"};
        return types;
    }

    /**
     * Returns a text name for the indicated output
     *
     * @param i the index of the output
     *
     * @return the name of the indexed output
     */
    public String getOutputName(int i) {
        switch (i) {
            case 0:
                return "DCT";
            default:
                return "NO SUCH OUTPUT!";
        }
    }

    /**
     * Returns a text description for the indicated output
     *
     * @param i the index of the output
     *
     * @return the text description for the indicated output
     */
    public String getOutputInfo(int i) {
        switch (i) {
            case 0:
                return "DCT (Double array)";
            default:
                return "No such output";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of
     * the outputs.
     *
     * @return the fully qualified java types for each of the outputs.
     */
    public String[] getOutputTypes() {
        String[] types = {
            "[D"};
        return types;
    }


    /** Preapres for execution*/
    public void initialize(ComponentContextProperties ccp) {
        theDCT = new DCTClass(this.numCoefs, this.keepFirstCoef);
    }

    /** Cleans up after execution*/
    public void dispose(ComponentContextProperties ccp) {
        theDCT = null;
    }

    /**
     * Computes the Discrete Cosine Transform (DCT) of real numbers. If the number
     * of coefficients (NumCoef) is less than the dimensionality of the input vector, only the
     * first NumCoef values will be outputted.  If numCoefs is greater than the
     * dimensionality of the input vector, the input vector will be zero-padded to the
     * appropriate length.
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {//doit() {
        
        //for seasr only
        numCoefs = Integer.valueOf(cc.getProperty(DATA_PROPERTY_1));
        keepFirstCoef = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_2));

        theDCT.setNumCoefs(numCoefs);
        theDCT.setKeepFirstCoef(keepFirstCoef);

        Object input = cc.getDataComponentFromInput(DATA_INPUT);
        if (input instanceof StreamTerminator){
            cc.pushDataComponentToOutput(DATA_OUTPUT, input);
        }else{
        //Object object1 = (Object) this.pullInput(0);
        

        /*double[] tmp = (double[])object1;
        for(int i=0; i<tmp.length; i++)
            System.out.println("tmp[" + i + "] = " + tmp[i]);*/

            cc.pushDataComponentToOutput(DATA_OUTPUT,
                                     this.theDCT.performDCT((double[])input));
        //this.pushOutput(this.theDCT.performDCT((double[])object1),0);
        }
    }
}
