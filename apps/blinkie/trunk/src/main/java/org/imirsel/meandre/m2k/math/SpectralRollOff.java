package org.imirsel.meandre.m2k.math;

//import ncsa.d2k.core.modules.*;
import org.imirsel.m2k.math.Mathematics;
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
 * Computes the rolloff point of the input array. A D2K/M2K module that reads in
 * a double array of a signal's magnitude distribution, and returns roll off
 * point of the input array.
 *
 * @author Andreas Ehmann
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann",
           description="compute the rolloff point of the input array",
           name="SpectralRollOff",
           tags="computation")


public class SpectralRollOff implements ExecutableComponent {//extends ComputeModule {
    @ComponentInput(description="input(1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="rolloff point(1-D double array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="0.85",
                       description="rolloff point(percent of total)",
                       name="percent")
    final static String DATA_PROPERTY = "percent";


    private double percent = 0.85;

    /**
     * Sets the gain
     *
     * @param value the new gain
     */
    public void setPercent(double value) {
        this.percent = value;
    }

    /**
     * Returns the gain
     *
     * @return gain
     */
    public double getPercent() {
        return this.percent;
    }

    /**
     * Returns an array of description objects for each property of the Module.
     *
     * @return an array of description objects for each property of the Module.
     */
/*    public PropertyDescription[] getPropertiesDescriptions() {
        PropertyDescription[] pds = new PropertyDescription[1];

        pds[0] = new PropertyDescription("percent", "Rolloff point(percent of total)",
                "Sets the percentage, X, below which X% of the magnitude distribution lies");

        return pds;
    }*/

  /**
   * Returns the name of the module
   *
   * @return the module name
   */
  public String getModuleName() {
    return "SpectralRollOff";
  }

  /**
   * Returns information about the module
   *
   * @return Module information
   */
  public String getModuleInfo() {
    return "<p>Overview: This module computes the rolloff point of the input array. " +
    		"</p><p>Detailed Description: This module computes the rolloff point of an" +
    		" input magnitude spectrum.  The rolloff point is defined as the point below which "
        + "X% of the signal's magnitude distribution resides." +
        		"</p><p>Data Handling: the input data is not modified.";
  }

  /**
   * Returns a text name for the indicated input
   *
   * @param i
   *          the index of the input
   *
   * @return the name of the indexed input
   */
  public String getInputName(int i) {
    switch (i) {
    case 0:
      return "Input";
    default:
      return "NO SUCH INPUT!";
    }
  }

  /**
   * Returns a text description for the indicated input
   *
   * @param i
   *          the index of the input
   *
   * @return a text description of the indexed input
   */
  public String getInputInfo(int i) {
    switch (i) {
    case 0:
      return "Input. (Double array).";
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
    String[] types = { "[D", };
    return types;
  }

  /**
   * Returns a text name for the indicated output
   *
   * @param i
   *          the index of the output
   *
   * @return the name of the indexed output
   */
  public String getOutputName(int i) {
    switch (i) {
    case 0:
      return "Rolloff";
    default:
      return "NO SUCH OUTPUT!";
    }
  }

  /**
   * Returns a text description for the indicated output
   *
   * @param i
   *          the index of the output
   *
   * @return the text description for the indicated output
   */
  public String getOutputInfo(int i) {
    switch (i) {
    case 0:
      return "Rolloff point. (Double array).";
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
    String[] types = { "[D" };
    return types;
  }

  /**
   * Computes the rolloff point of the input array of a signal's magnitude
   * distribution, and returns roll off point of the input array. The rolloff
   * point is defined as the point below which 85% of the signal's magnitude
   * distribution resides.
   */

  public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {//doit() {
        //System.out.println("\nSpectralRollOff");

        //for seasr only
        percent = Double.valueOf(cc.getProperty(DATA_PROPERTY));

    Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
    if (object1 instanceof StreamTerminator){
        cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
    }else{

        double[] input = (double[]) object1;

        int len = input.length;

        double[] rolloff = new double[1];
        rolloff[0] = Mathematics.rollOff(input, percent);

        //System.out.println("rolloff[0] = "  + rolloff[0]);

        cc.pushDataComponentToOutput(DATA_OUTPUT, rolloff);
    }
    //this.pushOutput(rolloff, 0);
  }

  //for seasr only
  public void initialize() {}
  public void dispose() {}

  public void initialize(ComponentContextProperties ccp) {}
  public void dispose(ComponentContextProperties ccp) {}
}
