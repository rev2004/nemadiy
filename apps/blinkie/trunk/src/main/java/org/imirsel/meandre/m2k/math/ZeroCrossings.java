package org.imirsel.meandre.m2k.math;

//import ncsa.d2k.core.modules.*;
import org.imirsel.m2k.math.Mathematics;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

import org.meandre.core.ComponentContextProperties;
/**
 * Counts the number of zero-crossings in an array. A D2K/M2K module that reads in
 * a double array, and returns the number of zero-crossings between adjecent
 * elements in the inputted array.
 *
 * @author Andreas Ehmann
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann",
           description="count the number of zero-crossings in an array",
           name="ZeroCrossings",
           tags="computation")

public class ZeroCrossings implements ExecutableComponent {
    //extends OrderedReentrantModule {
    @ComponentInput(description="input (1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="number of zero-crossings(double 1x1 array)",
                     name="output")
    final static String DATA_OUTPUT = "output";


	  /**
	   * Returns the name of the module
	   *
	   * @return the module name
	   */
  public String getModuleName() {
    return "ZeroCrossings";
  }

  /**
   * Returns information about the module
   *
   * @return Module information
   */
  public String getModuleInfo() {
    return "<p>Overview: This module counts the number of zero-crossings in an array. " +
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
        return "Input";
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
        return "Input (1-D double array).";
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
        "[D",};
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
        return "Zerocrossings";
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
        return "Number of Zerocrossings";
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

  /**
   * Calcualte the value of a sign function.
   * @param in independent variable of the sign function
   * @return 1.0 if the independent variable is greater or equal than 0
   * 		 -1.0 otherwise
   */
  double signum(double in) {
  	double out;
  	if (in >= 0.0)
  		out = 1.0;
  	else
  		out = -1.0;
  	return out;
  }

   /**
    * Calculate the number of zero-crossings between adjecent
    * elements in the inputted array.
    *
    */
   public void execute(ComponentContext cc)
           throws ComponentExecutionException, ComponentContextException {//doit() {
       //System.out.println("\nZeroCrossings");

    Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
    if (object1 instanceof StreamTerminator){
        cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
    }else{

        double[] input = (double []) object1;

        double[] tdzc = new double[1];
        tdzc[0] = Mathematics.zeroCrossings(input);

        //System.out.println("tdzc[0] = " + tdzc[0]);

        cc.pushDataComponentToOutput(DATA_OUTPUT, tdzc);
        //this.pushOutput(tdzc, 0);
    }
  }

  //for seasr only
  public void initialize() {}
  public void dispose() {}

  public void initialize(ComponentContextProperties ccp) {}
  public void dispose(ComponentContextProperties ccp) {}
}
