package org.imirsel.nema.components.extraction.jaudio;

import jAudioFeatureExtractor.DataModel;
import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;
import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.imirsel.nema.annotations.BooleanDataType;
import org.imirsel.nema.annotations.DoubleDataType;
import org.imirsel.nema.annotations.IntegerDataType;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.renderers.DoubleRenderer;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/** Selects options for a jAudio Feature extraction Job
 *
 *
 * @author Andreas F. Ehmann;
 *
 */
@Component(creator="Andreas F. Ehmann", description="This module takes a list of files and " +
               "allows the user to select the features and their properties for " +
               "feature extraction. The filelist and feature properties are embedded in a " +
               "jAudio Batch object and passed off for execution.",
               name="JAudioOptionsSelector",
               tags="jAudio Options Features")
               public class JAudioOptionsSelector implements ExecutableComponent {



       @ComponentInput(description="Input file list of audio files, String[][]", name="FileList")
       final static String DATA_INPUT_1= "FileList";

       @ComponentOutput(description="jAudio Batch Object", name="BatchObjectOut")
       final static String DATA_OUTPUT_1= "BatchObjectOut";

       //TODO make this a resource - we don't support anything other than default plugins
       @StringDataType(hide=true)
       @ComponentProperty(defaultValue="jAudioFiles/features.xml",
                       description="jAudio features.xml path, contains what features extractors are available",
                       name="Features List File")
                       final static String DATA_PROPERTY_FFILE = "Features List File";
       private String fFile = "jAudioFiles/features.xml";

       @IntegerDataType()
       @ComponentProperty(defaultValue="16000.0",
                       description="Analysis Sample Rate (Hz)",
                       name="SampleRate")
                       final static String DATA_PROPERTY_SR = "SampleRate";
       private double sampleRate = 16000.0;

       @IntegerDataType()
       @ComponentProperty(defaultValue="512",
                       description="Window Length (Samples)",
                       name="WindowLength")
                       final static String DATA_PROPERTY_WL = "WindowLength";
       private int windowLength = 512;

       @DoubleDataType(min=0,max=1,renderer=DoubleRenderer.class)
       @ComponentProperty(defaultValue="0.0",
                       description="Window Overlap Ratio [0.0-1.0)",
                       name="WindowOverlap")
                       final static String DATA_PROPERTY_WOL = "WindowOverlap";
       private double windowOverlap = 0.0;

       @BooleanDataType()
       @ComponentProperty(defaultValue="false",
                       description="Normalize the windows (true/false)",
                       name="Normalize")
                       final static String DATA_PROPERTY_N = "Normalize";
       private boolean normalize = false;

       @BooleanDataType()
       @ComponentProperty(defaultValue="false",
                       description="Save features for each window (true/false)",
                       name="SaveWindows")
                       final static String DATA_PROPERTY_SW = "SaveWindows";
       private boolean saveWindow = false;

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Save overall feature vector for each file (true/false)",
                       name="SaveOverall")
                       final static String DATA_PROPERTY_SO = "SaveOverall";
       private boolean saveOverall = false;


       @IntegerDataType()
       @ComponentProperty(defaultValue="0",
                       description="Output format (0: ACE XML, 1: ARFF)",
                       name="OutputType")
                       final static String DATA_PROPERTY_OT = "OutputType";
       private int outputType = 0;

       @StringDataType()
       @ComponentProperty(defaultValue="Feature_Values_1.xml",
                       description="Feature Values Ouput File: the features",
                       name="Feature Values Output File")
                       final static String DATA_PROPERTY_FVFILE = "Feature Values Output File";
       private String fvFile = "Feature_Values_1.xml";

       @StringDataType()
       @ComponentProperty(defaultValue="Feature_Definitions_1.xml",
                       description="Feature Definitions Output File: Description of Features",
                       name="Feature Definitions Output File")
                       final static String DATA_PROPERTY_FDFILE = "Feature Definitions Output File";
       private String fdFile = "Feature_Definitions_1.xml";
       ////////////////////////////////////////////////////////////////////////
       /*
        *  The Feature On/Offs and Attribute Values!
        */
       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Spectral Centroid (true/false)",
                       name="Spectral Centroid")
                       final static String DATA_PROPERTY_CENTROID = "Spectral Centroid";
       private boolean centroid = true;

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Spectral Rolloff Point (true/false)",
                       name="Spectral Rolloff Point")
                       final static String DATA_PROPERTY_ROLLOFF = "Spectral Rolloff Point";
       private boolean rolloff = true;

       @BooleanDataType()
       @ComponentProperty(defaultValue="0.85",
                       description="Spectral Rolloff Point Cutoff (0-1)",
                       name="Spectral Rolloff Point Value")
                       final static String DATA_PROPERTY_ROLLOFFV = "Spectral Rolloff Point Value";
       private String rolloffVal = "0.85";

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Spectral Flux (true/false)",
                       name="Spectral Flux")
                       final static String DATA_PROPERTY_FLUX = "Spectral Flux";
       private boolean flux = true;

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Compactness (true/false)",
                       name="Compactness")
                       final static String DATA_PROPERTY_COMPACTNESS = "Compactness";
       private boolean compactness = true;
       
       // Spectral Variability
       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Spectral Variability (true/false)",
                       name="Spectral Variability")
                       final static String DATA_PROPERTY_VARIABILITY = "Spectral Variability";
       private boolean variability = true;
       
       // Root Mean Square
       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Root Mean Square (true/false)",
                       name="Root Mean Square")
                       final static String DATA_PROPERTY_RMS = "Root Mean Square";
       private boolean rms = true;
       
       // Fraction Of Low Energy Windows
       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Fraction Of Low Energy Windows (true/false)",
                       name="Fraction Of Low Energy Windows")
                       final static String DATA_PROPERTY_FRACTION = "Fraction Of Low Energy Windows";
       private boolean fraction = true;
       
       //Zero Crossings
       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Zero Crossing Rate (true/false)",
                       name="Zero Crossings")
                       final static String DATA_PROPERTY_ZCR = "Zero Crossings";
       private boolean zcr = true;
       
       //MFCC       
       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include MFCC's (true/false)",
                       name="MFCC")
                       final static String DATA_PROPERTY_MFCC = "MFCC";
       private boolean mfcc = true;

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include MFCC Deltas (true/false)",
                       name="Derivative of MFCC")
                       final static String DATA_PROPERTY_MFCCD = "Derivative of MFCC";
       private boolean mfccd = true;

       @IntegerDataType()
       @ComponentProperty(defaultValue="13",
                       description="MFCC number of coefficents",
                       name="MFCC Number of Coefficients")
                       final static String DATA_PROPERTY_MFCCV = "MFCC Number of Coefficients";
       private String mfccVal = "13";

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Method of Moments (magnitude spectral sum, mean, centroid, skewness, kurtosis) (true/false)",
                       name="Method of Moments")
                       final static String DATA_PROPERTY_MOMENTS = "Method of Moments";
       private boolean moments = true;

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Strongest Beat in the Beat Histogram in BPM (true/false)",
                       name="Strongest Beat")
                       final static String DATA_PROPERTY_SB = "Strongest Beat";
       private boolean sb = true;

       @BooleanDataType()
       @ComponentProperty(defaultValue="true",
                       description="Include Strength of Strongest Beat relative to other beats (true/false)",
                       name="Strength Of Strongest Beat")
                       final static String DATA_PROPERTY_SSB = "Strength Of Strongest Beat";
       private boolean ssb = true;
       private String processResultsDir;
       /////////////////////////////////////////////////////////////////////////
       java.io.PrintStream cout;
       // log messages are here

       /** This method is invoked when the Meandre Flow is being prepared for
        * getting run.
        *
        * @param ccp The properties associated to a component context
        */
       public void initialize ( ComponentContextProperties ccp ) throws  ComponentExecutionException {

               cout = ccp.getOutputConsole();
       		try {
    			processResultsDir = ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory()).getResultLocationForJob(ccp.getFlowExecutionInstanceID());
    			
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			throw new ComponentExecutionException(e1);
    		}
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



               Batch b = new Batch();

               cout.println("Adding File list to jAudio Feature Extraction Batch");
               cout.flush();
               // Pull input, create the list of files to populate Batch object with
               String[] fileLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
               RecordingInfo[] recording_info = new RecordingInfo[fileLists.length];
               File[] names = new File[fileLists.length];
               for (int i = 0; i < names.length; i++) {
                       names[i] = new File(fileLists[i]);
               }
               // Go through the files one by one
               for (int i = 0; i < names.length; i++) {
                       // Assume file is invalid as first guess
                       recording_info[i] = new RecordingInfo(names[i].getName(), names[i]
                                                                                       .getPath(), null, false);
               }// for i in names

               
               
               // Get location of features.xml for building the datamodel and
               // initializing features on/off and attributes hashmaps;
               fFile = String.valueOf(cc.getProperty(DATA_PROPERTY_FFILE));
               DataModel dm = new DataModel(fFile,null);
               FeatureDefinition[] definitions = dm.featureDefinitions;
               FeatureExtractor[] fe = dm.features;
               String[] feAttributes = new String[] {};
               HashMap<String, Boolean> active = new HashMap<String, Boolean>();
               HashMap<String, String[]> attribute = new HashMap<String, String[]>();
               // populate the hashmap with a default of everything off

               for (int i = 0; i < dm.defaults.length; i++) {
                       Boolean defa = new Boolean(false);
                       active.put(definitions[i].name, defa);
                       feAttributes = fe[i].getFeatureDefinition().attributes;
                       if (feAttributes.length > 0) {
                               String[] feAttributeValues = new String[feAttributes.length];
                               for (int j = 0; j < feAttributes.length; j++) {
                                       try {
                                               feAttributeValues[j] = fe[i].getElement(j);
                                       } catch(Exception e){
                                               cout.println("ERROR retrieving attribute value " +
                                                               " for " + definitions[i].name);
                                               feAttributeValues[j] = "";
                                       }
                               }
                               attribute.put(definitions[i].name, feAttributeValues);
                       } else{
                               String[] feAttributeValues = new String[] {};
                               attribute.put(definitions[i].name, feAttributeValues);
                       }
               }


               // Retrieve Analysis Parameters
               sampleRate = Double.valueOf(cc.getProperty(DATA_PROPERTY_SR));
               windowLength = Integer.valueOf(cc.getProperty(DATA_PROPERTY_WL));
               windowOverlap = Double.valueOf(cc.getProperty(DATA_PROPERTY_WOL));
               normalize = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_N));

               // Retrieve Save Settings Parameters
               saveWindow = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_SW));
               saveOverall = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_SO));
               outputType = Integer.valueOf(cc.getProperty(DATA_PROPERTY_OT));
               fvFile = String.valueOf(cc.getProperty(DATA_PROPERTY_FVFILE));
               fdFile = String.valueOf(cc.getProperty(DATA_PROPERTY_FDFILE));

               //String fid = cc.getPublicResourcesDirectory()+ File.separator + "results" + File.separator + cc.getFlowExecutionInstanceID().substring(25);
               String fid = processResultsDir;
       File outDir = new File(fid);
       if (!outDir.exists())
       {
           boolean done = outDir.mkdirs();
           if (!done){
               throw new RuntimeException("Could not create the output directory");
           }
       }else if (!outDir.isDirectory()){
           throw new RuntimeException(outDir + " No such Parent directory");
       }

               String fdout = fid + File.separator + fdFile;
               String fvout = fid + File.separator + fvFile;

          //     cout.println("\nThe results will be in http://nema.lis.uiuc.edu:1814/public/resources/"+fid.substring(22)+fvFile);
               cout.flush();
               // Retrieve Feature Extraction Attribute Settings/Parameters
               // centroid and set in the hashmaps
               centroid = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_CENTROID));
               active.put(DATA_PROPERTY_CENTROID, new Boolean(centroid));
               //rolloff
               rolloff = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_ROLLOFF));
               active.put(DATA_PROPERTY_ROLLOFF, new Boolean(rolloff));
               rolloffVal = String.valueOf(cc.getProperty(DATA_PROPERTY_ROLLOFFV));
               attribute.put(DATA_PROPERTY_ROLLOFF, new String[] {rolloffVal});
               //flux
               flux = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_FLUX));
               active.put(DATA_PROPERTY_FLUX, new Boolean(flux));
               //compactness
               compactness = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_COMPACTNESS));
               active.put(DATA_PROPERTY_COMPACTNESS, new Boolean(compactness));
               // Spectral Variability
               variability = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_VARIABILITY));
               active.put(DATA_PROPERTY_VARIABILITY, new Boolean(variability));
               // Root Mean Square
               rms = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_RMS));
               active.put(DATA_PROPERTY_RMS, new Boolean(rms));
               // Fraction Of Low Energy Windows
               fraction = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_FRACTION));
               active.put(DATA_PROPERTY_FRACTION, new Boolean(fraction));
               //Zero Crossings
               zcr = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_ZCR));
               active.put(DATA_PROPERTY_ZCR, new Boolean(zcr));
               // MFCC
               mfcc = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_MFCC));
               active.put(DATA_PROPERTY_MFCC, new Boolean(mfcc));
               mfccVal = String.valueOf(cc.getProperty(DATA_PROPERTY_MFCCV));
               attribute.put(DATA_PROPERTY_MFCC, new String[] {mfccVal});
               mfccd = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_MFCCD));
               active.put(DATA_PROPERTY_MFCCD, new Boolean(mfccd));
               attribute.put(DATA_PROPERTY_MFCCD, new String[] {mfccVal});
               // Moments
               moments = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_MOMENTS));
               active.put(DATA_PROPERTY_MOMENTS, new Boolean(moments));
               // Strongest Beat
               sb = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_SB));
               active.put(DATA_PROPERTY_SB, new Boolean(sb));
               ssb = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_SSB));
               active.put(DATA_PROPERTY_SSB, new Boolean(ssb));

               // Set AGGREGATORS
               String[] aggNames = {"Standard Deviation", "Mean" };
               String[][] aggFeatures = {{}, {}};
               String[][] aggParameters = {{}, {}};

               OutputStream destinationFK = null;
               OutputStream destinationFV = null;
               try {
                       destinationFK = new FileOutputStream(new File(fdout));
                       destinationFV = new FileOutputStream(new File(fvout));
               } catch(Exception e) {}
               cout.println("Adding Extraction Options and Parameters to jAudio Feature Extraction Batch");
               dm.featureKey = destinationFK;
               dm.featureValue = destinationFV;
               b.setDataModel(dm);
               b.setWindowSize(windowLength);
               b.setWindowOverlap(windowOverlap);
               b.setSamplingRate(sampleRate);
               b.setNormalise(normalize);
               b.setPerWindow(saveWindow);
               b.setOverall(saveOverall);
               b.setRecording(recording_info);
               b.setOutputType(outputType);
               b.setFeatures(active,attribute);
               b.setAggregators(aggNames,aggFeatures,aggParameters);
               // OUTPUT
               cout.println("Outputting jAudio Batch job for execution");
               cout.flush();
               cc.pushDataComponentToOutput(DATA_OUTPUT_1, b);
       }


       /** This method is called when the Menadre Flow execution is completed.
        *
        * @param ccp The properties associated to a component context
        */
       public void dispose ( ComponentContextProperties ccp ) {

       }
}
