package org.imirsel.meandre.m2k.math;

//import ncsa.d2k.core.modules.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.imirsel.m2k.math.StreamFixedWindowMeanandCovarianceClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;

@Component(creator = "David Tcheng & KW", description = "Compute windowed means and covariances of a feature stream, outputting whenever a window is full", name = "StreamFixedWindowMeanAndCovariance", tags = "Stream Fixed Window Mean Covariance", firingPolicy = Component.FiringPolicy.any)
public class StreamFixedWindowMeanAndCovariance implements ExecutableComponent /* OrderedReentrantModule */ {

    public final static String INPUT_0_INPUT = "input";
    @ComponentInput(description = "Input vector (Double array)", name = INPUT_0_INPUT)
    //
    public final static String INPUT_1_SIGNAL = "signal";
    @ComponentInput(description = "Signal Object containing the signal processing information used to produce the feature vectors", name = INPUT_1_SIGNAL)
    //
    public final static String OUTPUT_0_SIGNAL = "signal";
    @ComponentOutput(description = "Signal Objects with window onset times", name = OUTPUT_0_SIGNAL)
    //
    public final static String OUTPUT_1_MEAN_AND_COVARIANCE_VECTORS = "meanAndCovariaceVectors";
    @ComponentOutput(description = "The Mean and covariance of a window of the input vector stream. (Double array).", name = OUTPUT_1_MEAN_AND_COVARIANCE_VECTORS)
    //
    public final static String PROPERTY_0_WINDOW_SIZE = "windowSize";
    @ComponentProperty(defaultValue = "10.0", description = "the length of audio signal over which the windows should be calculated", name = PROPERTY_0_WINDOW_SIZE)
    //
    public final static String PROPERTY_1_WINDOW_OVERLAP = "windowOverlap";
    @ComponentProperty(defaultValue = "0.5", description = "the percentage overlap of concurrent windows", name = PROPERTY_1_WINDOW_OVERLAP)
    //




    StreamFixedWindowMeanandCovarianceClass summariser = null;
    double windowSize = 10.0;
    double windowOverlap = 0.5;

    /**
     * Returns the name of the module
     * 
     * @return the module name
     */
//    public String getModuleName() {
//        return "StreamFixedWindowMeanAndCovariance";
//    }
    /**
     * Returns information about the module
     * 
     * @return Module information
     */
//    public String getModuleInfo() {
//        return "<p>Overview: This module computes the mean and covariance of fixed " +
//                "windows of a stream of vectors(1D Double array)." + "</p><p>Data Handling: The input data is not modified.";
//    }
    /**
     * Returns an array of strings containing the Java data types of the input.
     * 
     * @return the fully qualified java types for each of the inputs
     */
//    public String[] getInputTypes() {
//        String[] types = {"[D", "org.imirsel.m2k.util.Signal"};
//        return types;
//    }
//    public String[] getOutputTypes() {
//        String[] types = {"org.imirsel.m2k.util.Signal", "[D"};
//        return types;
//    }
    public boolean isReady() {
        if (!receivingData) {
            if (!inputBuffers[1].isEmpty()) {
                return true;
            }
        } else {
            if (!inputBuffers[0].isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public void updateInputBuffers(ComponentContext cc) throws ComponentContextException {
    
        for (int i = 0; i < numInputs; i++) {
            
            if (cc.isInputAvailable(inputNames[i])) {
                Object object =  cc.getDataComponentFromInput(inputNames[i]);
                inputBuffers[i].add(object);
            }

        }

        
    }

    public void doit(ComponentContext cc) throws ComponentContextException {
        
        if (!receivingData) {
            Object in = (Object) (inputBuffers[1].remove());
            if (in instanceof StreamTerminator) {
            	cc.pushDataComponentToOutput(OUTPUT_1_MEAN_AND_COVARIANCE_VECTORS, new StreamTerminator());
                return;
            }
            summariser = new StreamFixedWindowMeanandCovarianceClass(windowSize, windowOverlap);
            Signal aSig = (Signal) in;
            Signal out = summariser.setSignalSampleRateAndCalcWindowSize(aSig);
            cc.pushDataComponentToOutput(OUTPUT_0_SIGNAL, out);
            receivingData = true;
        } else {
            Object in = (Object) (inputBuffers[0].remove());
            if (in instanceof StreamTerminator) {
                receivingData = false;
                cc.pushDataComponentToOutput(OUTPUT_1_MEAN_AND_COVARIANCE_VECTORS, new StreamTerminator());
            } else {
                double[] out = summariser.receiveFrame((double[]) in);
                if (out != null) {
                    cc.pushDataComponentToOutput(OUTPUT_1_MEAN_AND_COVARIANCE_VECTORS, out);
                }
            }
        }

    }

    /**
     * Calculates the mean
     */
    public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
        
        
        // update input buffers //
        
        updateInputBuffers(cc);

        
        // fire until input queues
        while (isReady()) {
            
            doit(cc);

        }


        

    }
    private boolean receivingData = false;
    ConcurrentLinkedQueue[] inputBuffers = null;
    String [] inputNames = null;
    String [] outputNames = null;
    int numInputs = -1;
    int numOutputs = -1;

    public void initialize(ComponentContextProperties ccp) {
        
        // get property values
        windowSize = Double.valueOf(ccp.getProperty(PROPERTY_0_WINDOW_SIZE));
        windowOverlap = Double.valueOf(ccp.getProperty(PROPERTY_1_WINDOW_OVERLAP));
        
        
        // create input buffers

        inputNames = ccp.getInputNames();
        outputNames = ccp.getOutputNames();
        
        numInputs = inputNames.length;
        numOutputs = outputNames.length;

//        System.out.println("numInputs  = " + numInputs);
//        System.out.println("numOutputs = " + numOutputs);

        inputBuffers = new ConcurrentLinkedQueue[numInputs];
        
        for (int i = 0; i < numInputs; i++) {
            inputBuffers[i] = new ConcurrentLinkedQueue();
        }


        
        
        // component specific initialization
        
        receivingData = false;

    }

    public void dispose(ComponentContextProperties ccp) {
        summariser = null;
        inputBuffers = null;
    }

    public void dispose() {
        summariser = null;
    }
}
