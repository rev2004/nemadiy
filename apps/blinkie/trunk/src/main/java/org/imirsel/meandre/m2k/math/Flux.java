package org.imirsel.meandre.m2k.math;

//import ncsa.d2k.core.modules.*;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.ComponentProperty;
import java.util.Hashtable;
import org.imirsel.m2k.math.Mathematics;
import org.imirsel.meandre.m2k.StreamTerminator;
/**
 * Computes the flux of an array stream. A D2K/M2K module that reads in an array of
 * real numbers, and returns the flux of the array stream. The flux is defined as the
 * difference between two successive normalized arrays squared
 *
 * @author Andreas Ehmann
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann",
           description="compute the flux of an array stream",
           name="Flux",
           tags="computation")

public class Flux implements ExecutableComponent {
    @ComponentInput(description="input of streaming arrays(double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="the flux of successive arrays(double array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="true",
                     description="control whether debugging information is output to the console",
                     name="verbose")
    final static String DATA_PROPERTY= "verbose";

    private boolean verbose = true;

    double[] old_frame;

    //store the last frame of data
    Hashtable frameTable;

    //store the number of push out
    int nrOfPushout;

    //for seasr only
    public void initialize() {
        old_frame = null;
        nrOfPushout = 0;
        frameTable = new Hashtable();
    }

    public void initialize(ComponentContextProperties ccp) {
        old_frame = null;
        nrOfPushout = 0;
        frameTable = new Hashtable();
    }

    public void dispose() {
        if(verbose)
            System.out.println("\n the number of Flux's push out is " + nrOfPushout);
    }

    public void dispose(ComponentContextProperties ccp) {
        if(verbose)
            System.out.println("\n the number of Flux's push out is " + nrOfPushout);
    }

    /**
     * Computes the flux of an array stream.  First, the array is normalized,
     * then the difference between this normalized array and the previous one is
     * calculated and squared.
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {
        ++nrOfPushout;

        Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
        if (object1 instanceof StreamTerminator){
            cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
        }else{
            
        

            double[] input = (double []) object1;

            // RMS normalization of the frame
            double norm_factor = Mathematics.norm(input, 2.0);
            if (norm_factor == 0) {
                norm_factor = 1.0;
            }
            double[] new_frame = Mathematics.scaleArray(input,  1.0/norm_factor);


            old_frame = new_frame;

            /*if (old_frame == null) {
                old_frame = new_frame;
            }*/

            /* Calculate flux */
            double[] frameDiff = Mathematics.elementSubtract(new_frame, old_frame);
            double[] flux = new double[1];
            flux[0] = Mathematics.innerProduct(frameDiff, frameDiff);

            old_frame = new_frame;
            
            cc.pushDataComponentToOutput(DATA_OUTPUT, flux);
        }
    }
}
