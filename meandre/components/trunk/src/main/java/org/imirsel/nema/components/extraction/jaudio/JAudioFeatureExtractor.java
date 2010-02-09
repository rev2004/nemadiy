/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 *
 * Copyright (c) 2008+ Amit Kumar
 *
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.components.extraction.jaudio;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Logger;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import jAudioFeatureExtractor.CommandLineThread;
import jAudioFeatureExtractor.DataModel;
import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;

/** This executable component just concatenates the two input strings received
 * pushing it to the output.
 *
 * @author John Doe;
 *
 */
@Component(creator="Andreas F. Ehmann", description="Batch Executor for " +
               "jAudio Feature Extraction",
               name="JAudioFeatureExtractor",
               tags="batch, feature, extractor",
               //resources="mp3plugin.jar,tritonus_remaining-0.3.6.jar,tritonus_share-0.3.6.jar,jhall.jar")
               dependency={"mp3plugin.jar","tritonus_remaining-0.3.6.jar","tritonus_share-0.3.6.jar","jhall.jar"} )
               
               public class JAudioFeatureExtractor implements ExecutableComponent {


       @ComponentInput(description="jAudio Batch Object", name="batchObject")
       final static String DATA_INPUT_1= "batchObject";


       private String featPath = "features.xml";

       // log messages are here
       private Logger _logger;
       java.io.PrintStream cout;
       /** This method is invoked when the Meandre Flow is being prepared for
        * getting run.
        *
        * @param ccp The properties associated to a component context
        */
       public void initialize ( ComponentContextProperties ccp ) {
               this._logger = ccp.getLogger();
               cout = ccp.getOutputConsole();
       }

       /** This method just pushes a concatenated version of the entry to the
        * output.
        *
        * @throws ComponentExecutionException If a fatal condition arises during
        *         the execution of a component, a ComponentExecutionException
        *         should be thrown to signal termination of execution required.
        * @throws ComponentContextException A violation of the component context
        *         access was detected

        */
       public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
               Batch b = (Batch)cc.getDataComponentFromInput(DATA_INPUT_1);
               cout.println("Starting Execution of Feature Extraction Batch");
               cout.flush();
               try {
                       int count = 0;
                       CommandLineThread clt = new CommandLineThread(b);
                       clt.start();
       /*              while(clt.isAlive()){
                               if(cc.isFlowAborting()){
                                       clt.cancel();
                               }
                               clt.join(1000);
                       }
                       */
                       clt.join();
               } catch (Exception e) {
                       cout
                       .println("Error in execution - skipping this batch ("
                                       + b.getName() + ")");
                       e.printStackTrace();
                       cout.flush();
               }
               cout.println();
               cout.println("Execution of Feature Extraction Batch Complete");
               cout.flush();
       }


       /** This method is called when the Menadre Flow execution is completed.
        *
        * @param ccp The properties associated to a component context
        */
       public void dispose ( ComponentContextProperties ccp ) {

       }
}