package org.imirsel.meandre.m2k.modelling;

import java.util.HashMap;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;

import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import org.imirsel.m2k.modelling.SignalClassifier;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.core.logger.KernelLoggerFactory;
import org.meandre.annotations.ComponentProperty;

/**
 * Applys a <code>SignalClassifier</code> model to novel inputs in the form of
 * an array of Signal objects. The Signal objects have classification metadata
 * added to them.
 * @author  Kris West
 * Modified by Lily Dong on Feb. 27, 2008
 */
@Component(creator = "David Tcheng & Kris West", 
		description = "apply an array classifier to noval inputs in the form of an array of doubles ", 
		name = "ApplySignalClassifiersToVectors", 
		tags = "classification", firingPolicy = Component.FiringPolicy.any)
public class ApplySignalClassifiersToVectors implements ExecutableComponent {

    public final static String INPUT_1_classifiers = "classifiers";
    @ComponentInput(description = "classifier(SignalClassifier)", name = INPUT_1_classifiers)
    public final static String INPUT_2_dataVector = "dataVector";
    @ComponentInput(description = "the data vector to be classified(1-D double array)", name = INPUT_2_dataVector)
    public final static String OUTPUT_1_classNamesVector = "classNamesVector";
    @ComponentOutput(description = "a vector of class names(object)", name = OUTPUT_1_classNamesVector)
    public final static String OUTPUT_2_likelihoods = "likelihoods";
    @ComponentOutput(description = "a vector of classification likelihoods(1-D double array)", name = OUTPUT_2_likelihoods)
    public final static String PROPERTY_verbose = "verbose";
    @ComponentProperty(defaultValue = "true", description = "control whether debugging information is output to the console", name = PROPERTY_verbose)
    private boolean verbose = true;
    Vector<SignalClassifier> classifiers = null;
    Vector<String[]> classNamesVector = null;
    //store the number of push out
    int nrOfPushout;

    public ApplySignalClassifiersToVectors() {
    }
//    ConcurrentLinkedQueue[] inputBuffers = null;
    String[] inputNames = null;
    String[] outputNames = null;
    HashMap<String, ConcurrentLinkedQueue> inputNameToQueue = null;
    int numInputs = -1;
    int numOutputs = -1;
    protected transient static Logger log = KernelLoggerFactory.getCoreLogger();

    public void initialize(ComponentContextProperties ccp) {

        // get property values

        // create input buffers

        inputNames = ccp.getInputNames();
        outputNames = ccp.getOutputNames();

        numInputs = inputNames.length;
        numOutputs = outputNames.length;

        System.out.println("numInputs  = " + numInputs);
        System.out.println("numOutputs = " + numOutputs);

        inputNameToQueue = new HashMap();

//        inputBuffers = new ConcurrentLinkedQueue[numInputs];

        for (int i = 0; i < numInputs; i++) {
            inputNameToQueue.put(inputNames[i], new ConcurrentLinkedQueue());
        }



        // component specific initialization

        classifiers = null;
        nrOfPushout = 0;

    }

    public boolean isReady() {
        if (this.classifiers != null) {
            if (!inputNameToQueue.get(INPUT_2_dataVector).isEmpty()) {
                return true;
            }
        } else {
            if (!inputNameToQueue.get(INPUT_1_classifiers).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void updateInputBuffers(ComponentContext cc) throws ComponentContextException {

        for (int i = 0; i < numInputs; i++) {

            if (cc.isInputAvailable(inputNames[i])) {
                Object object = cc.getDataComponentFromInput(inputNames[i]);
                inputNameToQueue.get(inputNames[i]).add(object);
            }

        }

    }

    public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {


        // update input buffers //

        updateInputBuffers(cc);


        // fire until input queues
        while (isReady()) {

            doit(cc);

        }
    }

    public void dispose(ComponentContextProperties ccp) {
        if (verbose) {
            System.out.println("\n the number of ApplySignalClassifiersToVectors's push out is " + nrOfPushout);
        }
    }
    /**
     * Applys a <code>SignalClassifier</code> model to novel inputs in the form of
     * an array of Signal objects. The Signal objects have classification metadata
     * added to them.
     * @throws ComponentExecutionException, ComponentContextException if an error occurs.
     */
    int numClassifiers;
    Vector<SignalClassifier> signalClassifiers;

    public void doit(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {
        verbose = Boolean.valueOf(cc.getProperty(PROPERTY_verbose));

        if (this.classifiers == null) {
            if (!inputNameToQueue.get(INPUT_1_classifiers).isEmpty()) {
                System.out.println("ApplySignalClassifierToVector: getting classifer");

                Object in = (Object) (inputNameToQueue.get(INPUT_1_classifiers).remove());


                signalClassifiers = (Vector<SignalClassifier>) in;

                numClassifiers = signalClassifiers.size();


                classifiers = new Vector();

                for (int i = 0; i < numClassifiers; i++) {
                    classifiers.add(signalClassifiers.elementAt(i));
                }

                classNamesVector = new Vector();


                for (int i = 0; i < numClassifiers; i++) {
                    SignalClassifier classifier = classifiers.elementAt(i);
                    String[] modelClassNamesArray = (String[]) (classifier.getClassNames().toArray(new String[classifier.getNumClasses()]));
                    classNamesVector.add(modelClassNamesArray);
                }

                cc.pushDataComponentToOutput(OUTPUT_1_classNamesVector, classNamesVector);


            }
        } else {

            if (!inputNameToQueue.get(INPUT_2_dataVector).isEmpty()) {
                System.out.println("ApplySignalClassifierToVector: getting data vector");
                Object in = inputNameToQueue.get(INPUT_2_dataVector).remove();

                if (in instanceof StreamTerminator) {
                    cc.pushDataComponentToOutput(OUTPUT_2_likelihoods, in);

                } else {

                    boolean printValues = false;

                    double[] featureValues = (double[]) in;


                    if (printValues) {
                        for (int f = 0; f < featureValues.length; f++) {
                            log.info("featureValues[" + f + "] = " + featureValues[f]);
                            System.out.println("featureValues[" + f + "] = " + featureValues[f]);
                        }
                    }

                    Vector<double[]> likelihoodsVector = new Vector();

                    for (int i = 0; i < numClassifiers; i++) {
                        
                        SignalClassifier classifier = classifiers.elementAt(i);

                        double[] likelihoods = classifier.probabilities(featureValues);

                        if (printValues) {
                            for (int l = 0; l < likelihoods.length; l++) {
                                log.info("likelihoods[" + i + "][" + l + "] = " + likelihoods[l]);
                                System.out.println("likelihoods[" + i + "][" + l + "] = " + likelihoods[l]);
                            }
                        }

                        likelihoodsVector.add(likelihoods);
                    }

                    cc.pushDataComponentToOutput(OUTPUT_2_likelihoods, likelihoodsVector);



                }
                ++nrOfPushout;
            }
        }

    }
}
