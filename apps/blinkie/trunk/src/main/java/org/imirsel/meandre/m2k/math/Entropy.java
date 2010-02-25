/*
 * Entropy.java
 *
 * Created on 21 January 2005, 17:29
 */

package org.imirsel.meandre.m2k.math;

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
 * A module that computes the entropy of input arrays, using a histogram of the
 * frame's values to compute probabilities.
 * @author Kris West
 * Modified by Lily Dong
 */

@Component(creator="Kris West",
           description="compute the entropy of input arrays",
           name="Entropy",
           tags="computation")

public class Entropy implements ExecutableComponent {//extends ComputeModule {
    @ComponentInput(description="a frame of function to calculate the entropy(1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="an array containing the entropy calculated from the input array(1-D double array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="20",
                       description="set the number of bins in the histogram used to calculate the probability of each value in the input array",
                       name="histogramBins")
    final static String DATA_PROPERTY_1 = "histogramBins";
    @ComponentProperty(defaultValue="true",
                      description="control whether debugging information is output to the console",
                      name="verbose")
    final static String DATA_PROPERTY_2 = "verbose";

    private boolean verbose = true;

    int histogramBins = 20;

    public void initialize() {
       
    }
    public void dispose() {
        
    }

    public void initialize(ComponentContextProperties ccp) {
        
    }
    public void dispose(ComponentContextProperties ccp) {
        
    }

    /**
     * Calculate entropy
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {
        
        histogramBins = Integer.valueOf(cc.getProperty(DATA_PROPERTY_1));
        verbose = Boolean.valueOf((String)cc.getProperty(DATA_PROPERTY_2));

        Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
        if (object1 instanceof StreamTerminator){
            cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
        }else{

            double[] input = (double []) object1;
            double[] entropy = new double[1];
            entropy[0] = Mathematics.entropy(input, this.histogramBins);

            cc.pushDataComponentToOutput(DATA_OUTPUT, entropy);
        }
    }
}
