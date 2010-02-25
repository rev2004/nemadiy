package org.imirsel.meandre.m2k.math;

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
import org.meandre.annotations.ComponentProperty;

/**
 * Calculates the centroid of real numbers.  A Meandre/M2K module that reads in a double array of
 *real numbers, and returns the centroid of them.
 *
 * @author Andreas Ehmann
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann",
           description="compute the centroid of a 1-D double array",
           name="Centroid",
           tags="centroid")


public class Centroid implements ExecutableComponent {
    @ComponentInput(description="input(1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="the centroid of the input array(1-D double array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="true",
                      description="control whether debugging information is output to the console",
                      name="verbose")
    final static String DATA_PROPERTY= "verbose";

    private boolean verbose = true;

    //store the number of push out
    private int nrOfPushout;

    /**
     * Calculates the centroid of input numbers, and puts the centroid value in
     * a double array
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {//doit() {
        verbose = Boolean.valueOf((String)cc.getProperty(DATA_PROPERTY));

        Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
        if (object1 instanceof StreamTerminator){
            cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
        }else{

            double[] input = (double []) object1;
            double[] cent = new double[1];
            cent[0] = Mathematics.centroid(input);

            cc.pushDataComponentToOutput(DATA_OUTPUT, cent);
        }
    }

    

    public void initialize(ComponentContextProperties ccp) {
       
    }
    public void dispose(ComponentContextProperties ccp) {
        
    }
}
