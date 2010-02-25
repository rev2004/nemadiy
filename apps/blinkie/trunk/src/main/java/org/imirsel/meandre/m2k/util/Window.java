package org.imirsel.meandre.m2k.util;

//~--- non-JDK imports --------------------------------------------------------

//import ncsa.d2k.core.modules.*;
//import ncsa.d2k.core.modules.CustomModuleEditor;
//import ncsa.d2k.core.modules.PropertyDescription;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.imirsel.m2k.util.WindowClass;
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

//~--- classes ----------------------------------------------------------------

/**
 * Applies a specified window to an array. A D2K/M2K module that reads in
 * a double array, and returns the array processed by a window function.
 * The choices of different window functions are: Rectangular, Bartlett,
 * Hanning, and Hamming.
 *
 * @author Andreas Ehmann & Kris West
 */

@Component(creator="Andreas Ehmann & Kris West",
           description="Overview: This module applies the specified window to an array.  The choices are: "
               + "Rectangular, Bartlett, Blackman, Hanning, and Hamming."
               + "Data Handling: The input data is not modified.",
           name="Window",
           tags="apply a specified window to an array")


public class Window implements ExecutableComponent {
    @ComponentInput(description="input values(1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="output(1-D double array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="Rectangular",
                       description="The name of the window function which can be Rectangular, Bartlett, Blackman, Hanning, Hamming.",
                       name="WindowName")
    final static String DATA_PROPERTY = "WindowName";


    /**
     * Window type to use.
     */
    private String WindowName = "Rectangular";
    private WindowClass theWindow = null;
    //~--- methods ------------------------------------------------------------

    /**Initialises variables before execution */
    public void initialize(ComponentContextProperties ccp) {
        theWindow = new WindowClass(WindowName);
    }

    public void dispose(ComponentContextProperties ccp) {}

    /**
     * Applies a specified window to the input array. Currently, four window functions
     * are implemented. Rectangular, Bartlett, Hanning, and Hamming.
     *
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {
        
        WindowName = (String)(cc.getProperty(DATA_PROPERTY));
        theWindow.setWindowName(WindowName); //reset parameters

        
        Object input = cc.getDataComponentFromInput(DATA_INPUT);
        if (input instanceof StreamTerminator){
            cc.pushDataComponentToOutput(DATA_OUTPUT, input);
        }else{
            double[] double1DArray = (double[])(input);

            /*for(int i=0; i<double1DArray.length; i++)
                System.out.println("double1DArray[" + i + "] = " + double1DArray[i]);*/

            double[] output = theWindow.applyWindow(double1DArray);

            /*System.out.println("\nWindow");
            for(int i=0; i<output.length; i++)
                System.out.println("output[" + i + "] = " + output[i]);*/

            cc.pushDataComponentToOutput(DATA_OUTPUT, output);
            
            
        }
    }
}