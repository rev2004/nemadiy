package org.imirsel.nema.test;

import java.io.IOException;

import org.imirsel.nema.annotations.BooleanDataType;
import org.imirsel.nema.annotations.DoubleDataType;
import org.imirsel.nema.annotations.IntegerDataType;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.renderers.DoubleRenderer;
import org.imirsel.nema.renderers.FileRenderer;
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
 * @author Amit Kumar
 *
 */
@Component(creator="Amit Kumar", description="Test Test Test...",
		name="DataTypeTestComponent",
		tags="data type component")
		public class DataTypeTestComponent implements ExecutableComponent {



	@StringDataType()
	@ComponentInput(description="String filename", name="filename")
	final static String DATA_INPUT_1= "filename";

	@ComponentOutput(description="file name out", name="fileout")
	final static String DATA_OUTPUT_1= "fileout";

	@StringDataType()
	@ComponentProperty(defaultValue="jAudioFiles/features.xml",
			description="jAudio features.xml path, contains what features extractors are available",
			name="Features List File")
			final static String DATA_PROPERTY_FFILE = "Features List File";
	private String fFile = "jAudioFiles/features.xml";

	@DoubleDataType( min=Double.MIN_VALUE, max=Double.MAX_VALUE, renderer=DoubleRenderer.class)
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

	@DoubleDataType(min=0.0,max=1.0)
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

	@IntegerDataType(valueList={0,1})
	@ComponentProperty(defaultValue="0",
			description="Output format (0: ACE XML, 1: ARFF)",
			name="OutputType")
			final static String DATA_PROPERTY_OT = "OutputType";
	private int outputType = 0;

	@StringDataType(renderer=FileRenderer.class)
	@ComponentProperty(defaultValue="Feature_Values_1.xml",
			description="Feature Values Ouput File: the features",
			name="Feature Values Output File")
			final static String DATA_PROPERTY_FVFILE = "Feature Values Output File";
	private String fvFile = "Feature_Values_1.xml";

	@StringDataType(renderer=FileRenderer.class)
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

	@DoubleDataType(min=0.0,max=1.0,renderer=DoubleRenderer.class)
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


		String filename = (String)cc.getDataComponentFromInput(DATA_INPUT_1);
		fFile = String.valueOf(cc.getProperty(DATA_PROPERTY_FFILE));


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
		centroid = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_CENTROID));
		rolloff = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_ROLLOFF));
		rolloffVal = String.valueOf(cc.getProperty(DATA_PROPERTY_ROLLOFFV));
		flux = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_FLUX));
		compactness = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_COMPACTNESS));
		variability = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_VARIABILITY));
		rms = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_RMS));
		fraction = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_FRACTION));
		zcr = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_ZCR));
		mfcc = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_MFCC));
		mfccVal = String.valueOf(cc.getProperty(DATA_PROPERTY_MFCCV));
		mfccd = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_MFCCD));
		moments = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_MOMENTS));
		sb = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_SB));
		ssb = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_SSB));

		cc.pushDataComponentToOutput(DATA_OUTPUT_1, filename);
	}


	public void dispose ( ComponentContextProperties ccp ) {

	}
	
	public DataTypeTestComponent(){
		System.out.println("Came here - Data Type Component" );
	}
	
}
