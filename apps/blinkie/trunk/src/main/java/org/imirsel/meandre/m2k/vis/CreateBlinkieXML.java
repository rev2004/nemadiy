package org.imirsel.meandre.m2k.vis;

/**
 * <p>Description: visualization of histogram</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: NCSA</p>
 *
 * @author Lily Dong
 * @version 1.0
 */
import java.net.*;
import java.io.*;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.webui.WebUIFragmentCallback;
import org.meandre.webui.WebUIException;

//for annotations
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.imirsel.m2k.io.file.MP3ThreadedAudioPlayer;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.core.logger.KernelLoggerFactory;
import org.meandre.annotations.ComponentOutput;

@Component(creator = "David Tcheng", description = "This component displays the histogram of classificatin.", name = "CreateBlinkieXML", tags = "xml", firingPolicy = Component.FiringPolicy.any)
public class CreateBlinkieXML implements ExecutableComponent {

    public final static String INPUT_1_classNames = "classNames";
    @ComponentInput(description = "class name for each column of likelihoods(vector of storing String[])", name = INPUT_1_classNames)
    public final static String INPUT_2_signal = "signal";
    @ComponentInput(description = "signal object conatining the metadata for the audio files(signal)", name = INPUT_2_signal)
    public final static String INPUT_3_likelihoodFrame = "likelihoodFrame";
    @ComponentInput(description = "classificaiton likelihood frames for the signal object(vector of storing vector)", name = INPUT_3_likelihoodFrame)
    public final static String INPUT_4_ModelFileNames = "ModelFileNames";
    @ComponentInput(description = "model file names", name = INPUT_4_ModelFileNames)
    public final static String OUTPUT_xml_string = "xml_string";
    @ComponentOutput(description = "execute again", name = OUTPUT_xml_string)
    /** The instance ID */
    private String sInstanceID = null;

    //store data
    private Signal signal;
    private Vector<String[]> classNames;
    private Vector<String[]> modelFileNames;
    private Vector<Vector<double[]>> historyVectorOfModelVectorOfClassLikelihoods;
//    private double duration;
    File destAudio; //destination file for copy to public/resources directory

    //true if DATA_INPUT_1 is available, otherwise false
    boolean input1;
    //true if DATA_INPUT_2 is available, otherwise false
    boolean input2;
    //true if DATA_INPUT_3 is available, otherwise false
    boolean input3;
    //true if DATA_INPUT_4 is available, otherwise false
    boolean input4;

    /** This method gets call when a request with no parameters is made to a
     * component WebUI fragment.
     *
     * @param response The response object
     * @throws WebUIException Some problem encountered during execution and something went wrong
     */
    public void emptyRequest(HttpServletResponse response)
            throws WebUIException {
    }

    /** A simple message.
     *
     * @return The HTML containing the page
     */
    private String getXML() throws Exception {

        String fileName = null;
        String location = null;
        try {
            location = (String) signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
        } catch (noMetadataException ex) {
            Logger.getLogger(CreateBlinkieXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        int posOfLastSeperator = location.lastIndexOf(File.separator);
        fileName = location.substring(posOfLastSeperator + 1);

        int numberOfFrames = historyVectorOfModelVectorOfClassLikelihoods.size();
        int numberOfModels = historyVectorOfModelVectorOfClassLikelihoods.elementAt(0).size();

        StringBuffer sb = new StringBuffer();



        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<root>\n");

        String songid = fileName;


        for (int modelIndex = 0; modelIndex < numberOfModels; modelIndex++) {



            String[] modelClassNames = (String[]) classNames.elementAt(modelIndex);


            for (int historyIndex = 0; historyIndex < numberOfFrames; historyIndex++) {

                if (false) {

                    double min = Double.POSITIVE_INFINITY;
                    double max = Double.NEGATIVE_INFINITY;
                    for (int classIndex = 0; classIndex < modelClassNames.length; classIndex++) {

                        double[] theFrame = historyVectorOfModelVectorOfClassLikelihoods.elementAt(historyIndex).elementAt(modelIndex);
                        double value = theFrame[classIndex];

                        if (value < min) {
                            min = value;
                        }
                        if (value > max) {
                            max = value;
                        }
                    }
                    double range = max - min;

                    for (int classIndex = 0; classIndex < modelClassNames.length; classIndex++) {

                        double[] theFrame = historyVectorOfModelVectorOfClassLikelihoods.elementAt(historyIndex).elementAt(modelIndex);
                        theFrame[classIndex] -= min;
                        theFrame[classIndex] /= range;

                    }

                }


                for (int classIndex = 0; classIndex < modelClassNames.length; classIndex++) {

                    double[] theFrame = historyVectorOfModelVectorOfClassLikelihoods.elementAt(historyIndex).elementAt(modelIndex);

                    if (theFrame[classIndex] < 0.0) {

                        theFrame[classIndex] = 0.0;

//                        System.out.println("Error in normalization!  theFrame[classIndex] < 0.0");
//                        throw new Exception();
                    }

                }


                boolean makePDF = true;


                if (makePDF) {


                    double sum = 0.0;
                    for (int classIndex = 0; classIndex < modelClassNames.length; classIndex++) {

                        double[] theFrame = historyVectorOfModelVectorOfClassLikelihoods.elementAt(historyIndex).elementAt(modelIndex);

                        sum += theFrame[classIndex];
                    }

                    for (int classIndex = 0; classIndex < modelClassNames.length; classIndex++) {

                        double[] theFrame = historyVectorOfModelVectorOfClassLikelihoods.elementAt(historyIndex).elementAt(modelIndex);

                        if (sum > 0) {
                            theFrame[classIndex] /= sum;
                        }
                    }

                }

            }




            for (int classIndex = 0; classIndex < modelClassNames.length; classIndex++) {


                for (int historyIndex = 0; historyIndex < numberOfFrames; historyIndex++) {

                    double[] theFrame = historyVectorOfModelVectorOfClassLikelihoods.elementAt(historyIndex).elementAt(modelIndex);
                    double value = theFrame[classIndex];

                    int intValue = (int) (value * 100);

                    String className = modelClassNames[classIndex];

                    className = className.replaceAll("&", "And");

                    int frameOffset = 5;

                    sb.append("<data");
                    sb.append(" songid=\"" + fileName + "\"");
                    sb.append(" frameid=\"" + (historyIndex + frameOffset) + "\"");
                    sb.append(" modelid=\"" + modelFileNames.elementAt(modelIndex) + "\"");
                    sb.append(" fieldid=\"" + className + "\">");

                    sb.append(intValue);

                    sb.append("</data>\n");
                }
            }
        }



        sb.append("<status>end</status>\n");
        sb.append("</root>\n");

//        System.out.println("xml:");
//        System.out.println(sb);
//        System.out.println("");

        return sb.toString();
    }

    /** This method gets called when a call with parameters is done to a given component
     * webUI fragment
     *
     * @param target The target path
     * @param request The request object
     * @param response The response object
     * @throws WebUIException A problem occurred during the call back
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws
            WebUIException {
    }

    /** Call at the end of an execution flow.
     *
     *
     */
    public void dispose(ComponentContextProperties ccp) {
        if (destAudio != null) {
            destAudio.delete();
        }
    }

    /** When ready for execution.
     *
     * @param cc The component context
     * @throws ComponentExecutionException An exeception occurred during execution
     * @throws ComponentContextException Illigal access to context
     */
    public void execute(ComponentContext cc) throws ComponentExecutionException,
            ComponentContextException {


//        log.info("################################ executing CreateBlinkieXML");
        try {
            if (cc.isInputAvailable(INPUT_1_classNames)) {
//                log.info("################################ executing CreateBlinkieXML: got input #1");
                classNames = (Vector<String[]>) cc.getDataComponentFromInput(INPUT_1_classNames);
                input1 = true;
            }

            if (cc.isInputAvailable(INPUT_2_signal)) {
//                log.info("################################ executing CreateBlinkieXML: got input #2");
                signal = (Signal) (cc.getDataComponentFromInput(INPUT_2_signal));
                input2 = true;
                MP3ThreadedAudioPlayer player = null;
                try {
                    player = new MP3ThreadedAudioPlayer(signal.getFile());
                //player.init();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                duration = (int)(player.getDuration()*1000);
            }

            if (cc.isInputAvailable(INPUT_3_likelihoodFrame)) {
//                log.info("################################ executing CreateBlinkieXML: got input #3");

                Object input = cc.getDataComponentFromInput(INPUT_3_likelihoodFrame);

                if (StreamTerminator.isStreamTerminator(input)) {
                    input3 = true;
                } else {
                    historyVectorOfModelVectorOfClassLikelihoods.add((Vector) cc.getDataComponentFromInput(INPUT_3_likelihoodFrame));
                }
            }

            if (cc.isInputAvailable(INPUT_4_ModelFileNames)) {
//                log.info("################################ executing CreateBlinkieXML: got input #4");

                Object input = cc.getDataComponentFromInput(INPUT_4_ModelFileNames);

                modelFileNames = (Vector<String[]>) cc.getDataComponentFromInput(INPUT_4_ModelFileNames);
                input4 = true;
            }

            sInstanceID = cc.getExecutionInstanceID();


            if (input1 && input2 && input3 && input4) {
//                log.info("################################ executing CreateBlinkieXML: got all inputs");

                String xml = getXML();

//                log.info("################################ executing CreateBlinkieXML xml:" + xml);

                cc.pushDataComponentToOutput(OUTPUT_xml_string, xml);
            }


        } catch (Exception e) {
            throw new ComponentExecutionException(e);
        }
    }
    /** Called when a flow is started.
     *
     */
    int frameDurationInMS = 1000;
    protected transient static Logger log = KernelLoggerFactory.getCoreLogger();

    public void initialize() {
        signal = null;
        classNames = null;
        historyVectorOfModelVectorOfClassLikelihoods = new Vector();
        input1 = false;
        input2 = false;
        input3 = false;
    }

    public void initialize(ComponentContextProperties ccp) {
        signal = null;
        classNames = null;
        historyVectorOfModelVectorOfClassLikelihoods = new Vector();
        input1 = false;
        input2 = false;
        input3 = false;
    }
}
