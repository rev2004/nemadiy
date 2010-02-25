package org.imirsel.meandre.m2k.math;

import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

import org.meandre.core.ComponentContextProperties;

import java.util.Hashtable;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.annotations.ComponentProperty;

/**
 * A module that computes the differences between concurrent frames
 * of data taken as input, which are often termed deltas. Accelerations
 * can be calculated by calculating the differences between the returned
 * frames.
 * @author  Kris West
 * Modified by Lily Dong
 */
@Component(creator = "Kris West", description = "compute the difference between successive frames, F(x) and F(x-1) of data taken as input", name = "DifferenceBetweenFrames", tags = "compute the difference between frames")
public class DifferenceBetweenFrames implements ExecutableComponent {

    @ComponentInput(description = "frame of data to calculate the difference from (1-D double array)", name = "input")
    final static String DATA_INPUT = "input";
    @ComponentOutput(description = "differences between the frame of data and the last(1-D double array)", name = "output")
    final static String DATA_OUTPUT = "output";
    @ComponentProperty(defaultValue = "true", description = "control whether debugging information is output to the console", name = "verbose")
    final static String DATA_PROPERTY = "verbose";
    private boolean verbose = true;

    //The last frame of data
    double[] lastFrame = null;

    //store the number of push out
    int nrOfPushout;

    //store the last frame of data
//    Hashtable frameTable;

    //for seasr only
    public void initialize() {
        lastFrame = null;
        nrOfPushout = 0;
//        frameTable = new Hashtable();
    }

    public void initialize(ComponentContextProperties ccp) {
        lastFrame = null;
        nrOfPushout = 0;
//        frameTable = new Hashtable();
    }

    public void dispose() {
        if (verbose) {
            System.out.println("\n the number of DifferenceBetweenFrames's push out is " + nrOfPushout);
        }
    }

    public void dispose(ComponentContextProperties ccp) {
        if (verbose) {
            System.out.println("\n the number of DifferenceBetweenFrames's push out is " + nrOfPushout);
        }
    }

    /**
     * Computes the differences between concurrent frames
     * of data taken as input, which are often termed deltas.
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {

        ++nrOfPushout;


        Object input = cc.getDataComponentFromInput(DATA_INPUT);
        if (input instanceof StreamTerminator) {
            lastFrame = null;
            cc.pushDataComponentToOutput(DATA_OUTPUT, new StreamTerminator());
        } else {


            double[] frame = (double[]) input;
            int array1NumValues = frame.length;
            if (lastFrame == null) {
                lastFrame = new double[array1NumValues];
//                lastFrame = frame;
//                cc.pushDataComponentToOutput(DATA_OUTPUT, frame);
//                return;
            }
            double[] out = new double[array1NumValues];

            for (int i = 0; i < array1NumValues; i++) {
                out[i] = frame[i] - lastFrame[i];
            }

            lastFrame = frame;

            cc.pushDataComponentToOutput(DATA_OUTPUT, out);


//            cc.pushDataComponentToOutput(DATA_OUTPUT, input);

        }

    }
}
