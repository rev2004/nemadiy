package org.imirsel.meandre.m2k.filters;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;

import org.meandre.annotations.ComponentProperty;

import java.util.Vector;
import org.imirsel.m2k.filters.MelFilterBankClass;
import org.imirsel.meandre.m2k.StreamTerminator;

/**
 * Passes input through a MelFilterBank. A D2K/M2K module that reads in a double
 * array of Magnitude Spectrum, and returns the bands of the Filter bank.
 *
 * @author Andreas Ehmann
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann",
           description="input magnitude spectrums and sample rate and output bands of the filter bank",
           name="MelFilterBank",
           tags="mel filterbank",
           firingPolicy = Component.FiringPolicy.any)

public class MelFilterBank implements ExecutableComponent {
    //for seasr only
    @ComponentInput(description="magnitude spectrums(1-D double array)",
                    name= "magnitude")
    final static String DATA_INPUT_1 = "magnitude";
    @ComponentInput(description="sample rate",
                   name= "rate")
    final static String DATA_INPUT_2 = "rate";

    @ComponentOutput(description="bands of filter bank(1-D double array)",
                     name="bands")
    final static String DATA_OUTPUT = "bands";

    @ComponentProperty(defaultValue="32",
                       description="Number of filters in the Mel-scale filter bank",
                       name="numFilters")
    final static String DATA_PROPERTY_1 = "numFilters";
    @ComponentProperty(defaultValue="0.0",
                       description="Low frequency edge as ratio of sample rate",
                       name="lowestFreq")
    final static String DATA_PROPERTY_2 = "lowestFreq";
    @ComponentProperty(defaultValue="0.5",
                       description="High frequency edge as ratio of sample rate",
                       name="highestFreq")
    final static String DATA_PROPERTY_3 = "highestFreq";


    private int numFilters = 32;
    private double lowestFreq = 0.0;
    private double highestFreq = 0.5;
    Integer fss;
    MelFilterBankClass theFilter;

    //store coming data
    Vector data;

    /**
     * Clears variables before execution of any itinery containing this module
     */
    public void initialize(ComponentContextProperties ccp) {
        theFilter = null;
        fss = new Integer(-1);
        data = new Vector();
    }


    /**
     * Computes bands of the filter bank. If the filter bank has not been created,
     * create it before calculating the bands.
     *
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {
        
        //for seasr only
        numFilters = Integer.valueOf(cc.getProperty(DATA_PROPERTY_1));
        lowestFreq = Double.valueOf(cc.getProperty(DATA_PROPERTY_2));
        highestFreq = Double.valueOf(cc.getProperty(DATA_PROPERTY_3));

        //if (theFilter == null) {
        if(cc.isInputAvailable("rate")){
            fss = (Integer) (cc.getDataComponentFromInput(DATA_INPUT_2));
            //fss = (Integer) this.pullInput(1);

            System.out.println("fss = " + fss);

            theFilter = new MelFilterBankClass(this.numFilters,
                                               fss.intValue(),
                                               this.lowestFreq,
                                               this.highestFreq);

            if(data.size() != 0) {
                for (int i = 0; i < data.size(); i++) {
                    double[] MagSpectrumInput = (double[]) data.elementAt(i);
                    cc.pushDataComponentToOutput(DATA_OUTPUT,
                                                 theFilter.filter(MagSpectrumInput));
                }
                data.clear();
            }
        }

        //} else {

        
        if(cc.isInputAvailable("magnitude")) {
            Object input = cc.getDataComponentFromInput(DATA_INPUT_1);
            if (input instanceof StreamTerminator){
                cc.pushDataComponentToOutput(DATA_OUTPUT, input);
            }else{
            
                double[] MagSpectrumInput = (double[]) input;

                /*for(int i=0; i<MagSpectrumInput.length; i++)
                    System.out.println("MagSpectrumInput[" + i + "] = " + MagSpectrumInput[i]);*/

                if(fss.intValue() != -1)
                    cc.pushDataComponentToOutput(DATA_OUTPUT, theFilter.filter(MagSpectrumInput));
                else
                    data.add(MagSpectrumInput);
            }
        }
        //this.pushOutput(theFilter.filter(MagSpectrumInput), 0);
        //}
    }

    /**
     * Clears variables after execution of any itinery containing this module
     */
    public void dispose(ComponentContextProperties ccp) {
        theFilter = null;
    }


    //~--- get methods --------------------------------------------------------

    /**
     * Returns the highest frequency.
     *
     * @return the highest frequency
     */
    public double getHighestFreq() {
        return this.highestFreq;
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
                return "Magnitude Spectrum  (1-D double array).";

            case 1:
                return "Sample Rate (Integer)";

            default:
                return "No such input";
        }
    }

    /**
     * Returns a text name for the indicated input.
     *
     * @param i
     *          the index of the input
     *
     * @return the name of the indexed input
     */
    public String getInputName(int i) {
        switch (i) {
            case 0:
                return "Input Magnitude Spectrum";

            case 1:
                return "Sample Rate";

            default:
                return "NO SUCH INPUT!";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of the input.
     *
     * @return the fully qualified java types for each of the inputs
     */
    public String[] getInputTypes() {
        String[] types = {"[D", "java.lang.Integer"};

        return types;
    }

    /**
     * Returns the lowest frequency.
     *
     * @return the lowest frequency
     */
    public double getLowestFreq() {
        return this.lowestFreq;
    }

    /**
     * Returns information about the module.
     *
     * @return Module information
     */
    public String getModuleInfo() {
        return "<p>Overview: " + "This module takes as input, a magnitude or power spectrum in the range from omega = [0, pi] " + "and filters the spectrum through Mel-spaced triangular overlapping filters." + "</p><p>Detailed Description: A spectrum on the range omegae [0, pi] represented as a " + "1 dimensional double array is taken as input, as well as the sample rate.  Low and high frequency " + "bounds are set as parameters as a ratio to the sample rate.  Therefore, these values should be in " + "the range of [0.0, 0.5], to represent DC and the half sample rate, respectively.  A Mel filterbank " + "with a settable number of filters is constructed and applied to the inputs, producing a 1-dimensional " + "double array output, whose dimensionality is the number of filters used." + "</p><p>Data Handling: The input data is not modified.";
    }

    /**
     * Returns the name of the module.
     *
     * @return the module name
     */
    public String getModuleName() {
        return "MelFilterBank";
    }

    /**
     * Returns the number of filters.
     *
     * @return the number of filters
     */
    public int getNumFilters() {
        return this.numFilters;
    }

    /**
     * Returns a text description for the indicated output.
     *
     * @param i
     *          the index of the output
     *
     * @return the text description for the indicated output
     */
    public String getOutputInfo(int i) {
        switch (i) {
            case 0:
                return "The bands of the Filter bank (1-D double array).";

            default:
                return "No such output";
        }
    }

    /**
     * Returns a text name for the indicated output.
     *
     * @param i
     *          the index of the output
     *
     * @return the name of the indexed output
     */
    public String getOutputName(int i) {
        switch (i) {
            case 0:
                return "Mel Filter Ouputs";

            default:
                return "NO SUCH OUTPUT!";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of the outputs.
     *
     * @return the fully qualified java types for each of the outputs.
     */
    public String[] getOutputTypes() {
        String[] types = {"[D"};

        return types;
    }

    /**
     * Returns an array of description objects for each property of
     * the Module.
     *
     * @return an array of description objects for each property of
     * the Module.
     */
/*    public PropertyDescription[] getPropertiesDescriptions() {
        PropertyDescription[] pds = new PropertyDescription[3];

        pds[0] = new PropertyDescription("numFilters", "Number of filters in the Mel-scale filter bank",
                "Sets the number of triangular Mel spaced filters to be used in the filterbank");
        pds[1] = new PropertyDescription("lowestFreq", "Low frequency edge as ratio of sample rate",
                "Sets the lowest frequency edge of the lowest filter in the filterbank.  " + "The scale is in terms of ratio to sample rate, i.e 0.0 being DC and 0.5 " + "being the Nyquist frequency (half sample rate).  Default is 0.");
        pds[2] = new PropertyDescription("highestFreq", "High frequency edge as ratio of sample rate",
                "Sets the highest frequency edge of the highest filter in the filterbank.  " + "The scale is in terms of ratio to sample rate, i.e 0.0 being DC and 0.5 " + "being the Nyquist frequency (half sample rate).  Default is 0.5 .");

        return pds;
    }*/

    /**
     * Controls whether the module is able to run based on the input flags and a
     * flag indicating whether the module is ready to receive data.
     */
/*    public boolean isReady() {
        if ((theFilter == null) && (this.getFlags()[1] > 0)) {
            return true;
        } else {
            if ((theFilter != null) && (this.getFlags()[0] > 0)) {
                return true;
            } else {
                return false;
            }
        }
    }*/

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the highest frequency.
     *
     * @param value
     *          of the highest frequency
     */
    public void setHighestFreq(double value) {
        this.highestFreq = value;
    }

    /**
     * Sets the lowest frequency.
     *
     * @param value
     *          of the lowest frequency
     */
    public void setLowestFreq(double value) {
        this.lowestFreq = value;
    }

    /**
     * Sets the number of filters.
     *
     * @param value the number of filters
     */
    public void setNumFilters(int value) {
        this.numFilters = value;
    }
}
