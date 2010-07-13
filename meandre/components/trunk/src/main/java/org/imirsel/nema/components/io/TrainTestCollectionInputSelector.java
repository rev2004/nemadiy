package org.imirsel.nema.components.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.renderers.CollectionRenderer;
import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/**
 * 
 * @author kris.west@gmail.com
 * 
 * @deprecated Use TaskSelector or TrainTestTaskSelector
 */
@Component(creator = "Mert Bay", description = "Selects a Train / Test dataset from NEMA servers. Outputs 4 objects: " +
			"1) a Feature Extraction list file path 2) Ground-truth file path 3) an array of train file paths " +
			"4) an array of test file paths", name = "CollectionSelector",//resources={"RepositoryProperties.properties"},
				tags = "input, collection, train/test", firingPolicy = Component.FiringPolicy.all)	
public class TrainTestCollectionInputSelector extends NemaComponent {



	@ComponentOutput(description = "String[] that holds the file locations for the feature extraction list file", name = "featExtList")
	public final static String DATA_OUTPUT_FeatExtList = "featExtList";
	
	@ComponentOutput(description = "String[] that holds the ground-truth file locations", name = "groundtruth")
	public final static String DATA_OUTPUT_GT = "groundtruth";
	
	@ComponentOutput(description = "String[] that holds the train list files file location", name = "train_files")
	public final static String DATA_OUTPUT_TRAIN_FILES = "train_files";
		
	@ComponentOutput(description = "String[] that holds the train list files file location", name = "test_files")
	public final static String DATA_OUTPUT_TEST_FILES = "test_files";

	
	@StringDataType(renderer=CollectionRenderer.class)
	@ComponentProperty(defaultValue = "5", description = "", name = "dataset")
	final static String DATA_PROPERTY_DATASET_ID = "dataset";
	private int datasetID = 5;
	
	@StringDataType(hide=true)//valueList={"","96k","128k"})
	@ComponentProperty(defaultValue = "", description = "bit rate for the mp3 files", name = "BitRate")
	final static String DATA_PROPERTY_BIT_RATE = "BitRate";
	private String bitRate = "";
	
	@StringDataType(hide=true)//valueList={"1","2"})
	@ComponentProperty(defaultValue = "1", description = "Number of channels. 1 for mono 2 for stereo", name = "Channels")
	final static String DATA_PROPERTY_CHANNELS = "Channels";
	private String channels = "1";
	
	@StringDataType(hide=true)//valueList={"30","full"})
	@ComponentProperty(defaultValue = "30", description = "30 sec clips or the full audio files", name = "Clip_Type")
	final static String DATA_PROPERTY_CLIP_TYPE = "Clip_Type";
	private String clip_type = "30";
	
	
	@StringDataType(hide=true)//valueList={"mp3","wav"})
	@ComponentProperty(defaultValue = "wav", description = "Encoding type: mp3 or wav", name = "Encoding")
	final static String DATA_PROPERTY_ENCODING = "Encoding";
	private String encoding = "wav";
	
	
	@StringDataType(hide=true)//valueList={"22050","44100"})
	@ComponentProperty(defaultValue = "22050", description = "Sampling rate: 22050 or 44100", name = "Sample_Rate")
	final static String DATA_PROPERTY_SAMPLE_RATE = "Sample_Rate";
	private String sample_rate = "22050";
	
	@StringDataType(hide=true)//valueList={"\t",","})
	@ComponentProperty(defaultValue = "\t", description = "Delimiter for the ground-truth files", name = "Delim")
	final static String DATA_PROPERTY_DELIM = "Delim";


	private String delim = "\t";
	
	

	
	private String[] featExtList;// = {"/data/raid3/collections/audioclassification/monolists/audiomood.all.txt"};
	private String[] groundtruth;// = {"/data/raid3/collections/audioclassification/monolists/audiomood.all.gt.txt"};
	private String[] train_files;//  ={"/data/raid3/collections/audioclassification/monolists/audiomood.train.bc.txt","/data/raid3/collections/audioclassification/monolists/audiomood.train.ac.txt","/data/raid3/collections/audioclassification/monolists/audiomood.train.ab.txt"}; 
	private String[] test_files;// ={"/data/raid3/collections/audioclassification/monolists/audiomood.test.a.txt","/data/raid3/collections/audioclassification/monolists/audiomood.test.b.txt","/data/raid3/collections/audioclassification/monolists/audiomood.test.c.txt"};
	private String processWorkingDirName;
	private File processWorkingDir;

	private List<File[]> traintest_split_files;
	private File[] gt_and_featExt_files;
	private Set<NemaMetadataEntry>  file_encoding_constraint;
	
	public void initialize(ComponentContextProperties ccp) throws  ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		datasetID = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_DATASET_ID));		
		bitRate = String.valueOf(ccp.getProperty(DATA_PROPERTY_BIT_RATE));
		channels = String.valueOf(ccp.getProperty(DATA_PROPERTY_CHANNELS));
		clip_type = String.valueOf(ccp.getProperty(DATA_PROPERTY_CLIP_TYPE));
		encoding = String.valueOf(ccp.getProperty(DATA_PROPERTY_ENCODING));
		sample_rate = String.valueOf(ccp.getProperty(DATA_PROPERTY_SAMPLE_RATE));			
		delim = String.valueOf(ccp.getProperty(DATA_PROPERTY_DELIM));					
		
		try {
			//processWorkingDirName=ArtifactManagerImpl.getInstance()
			//.getAbsoluteProcessWorkingDirectory(
			//		cc.getFlowExecutionInstanceID());				
			
		
			//Debugging 01/26/2010
			//processWorkingDirName=ArtifactManagerImpl.getInstance().getResultLocationForJob(cc.getFlowExecutionInstanceID());//.getProcessWorkingDirectory(cc.getFlowExecutionInstanceID());
			processWorkingDirName=ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory()).getProcessWorkingDirectory(ccp.getFlowExecutionInstanceID());
			
			
		} catch (IOException e1) {
			throw new ComponentExecutionException(e1);
		}
	//	cout.println("Process working dir: " + processWorkingDirName);	
		String datasetidtxt = processWorkingDirName + File.pathSeparator + "datasetID.txt";		
		try
		{
		    processWorkingDir = new File(processWorkingDirName);
		    // Open an output stream
		    FileOutputStream fout;		
		    fout = new FileOutputStream(processWorkingDirName + File.separator + "datasetid.txt");			
		    // Print a line of text
		    new PrintStream(fout).println(datasetID);
		    // Close our output stream
		    fout.close();		
		}
		// Catches any error conditions
		catch (IOException e)
		{
			System.err.println ("Unable to write to files " + processWorkingDirName  + " or " + datasetidtxt );
			System.exit(-1);
		}
		getLogger().info("Dataset ID " + datasetID + " is selected\n" +
				"Dataset properties are:\nbitRate=" + bitRate
				+ "\nChannles=" + channels + "\nClip Type=" + clip_type
				+ "\nEncoding=" + encoding + "\nSample Rate=" + sample_rate);
		
	}
	
	public void dispose(ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
	}
	
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException{
		
		//DatasetListFileGenerator dataset = new DatasetListFileGenerator();
		
	    file_encoding_constraint = DatasetListFileGenerator.buildConstraints(bitRate, channels, clip_type, encoding, sample_rate);		
	    
	    try {
			traintest_split_files = DatasetListFileGenerator.writeOutExperimentSplitFiles(datasetID, delim, processWorkingDir, file_encoding_constraint);									
		} catch (SQLException e) {
			throw new ComponentExecutionException("SQLException in " + this.getClass().getName(),e);
		} 
		
		train_files = new String[traintest_split_files.size()];
		test_files = new String[traintest_split_files.size()];
		
		
		try{
			int ctr = 0;
			for (File[] thisFile:traintest_split_files ){
				//cout.println("Train File: " + thisFile[0].getCanonicalPath());
				//cout.println("Test File: " + thisFile[1].getCanonicalPath());
				train_files[ctr] = thisFile[0].getCanonicalPath();
				test_files[ctr] = thisFile[1].getCanonicalPath();		
				ctr++;
			}
			}
		catch (IOException e){
			e.printStackTrace();
		}
	
		
		
		try {
			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetID, delim, processWorkingDir, file_encoding_constraint);
		} catch (SQLException e) {
			throw new ComponentExecutionException("SQLException in " + this.getClass().getName(),e);
		}
		
		groundtruth = new String[gt_and_featExt_files.length / 2];
		featExtList = new String[gt_and_featExt_files.length / 2];
		getLogger().info("the size of gt " + gt_and_featExt_files.length);
		 try {
			groundtruth[0] = gt_and_featExt_files[0].getCanonicalPath();
			featExtList[0] = gt_and_featExt_files[1].getCanonicalPath();

		} catch (IOException e) {
			throw new ComponentExecutionException("IOException in " + this.getClass().getName(),e);
		}
		
		//Print info about the data
		for (int i=0;i<featExtList.length;i++){
			getLogger().info("Feature Extraction file no." +i +":" + featExtList[i]);		
		}			
		for (int i=0;i<groundtruth.length;i++){
			getLogger().info("Ground-truth file no." +i +":"+ groundtruth[i]);
		}					
		
		String msg = "Train/Test files:\n";
		for (int i=0;i<train_files.length;i++){
			msg += "Train  no." +i +":"+ train_files[i] + "\n";
			msg += "Test  no." +i +":"+ test_files[i] + "\n";			
		}		
		getLogger().info(msg);
		
		//Push the data out
		ccp.pushDataComponentToOutput(DATA_OUTPUT_FeatExtList, featExtList);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_GT, groundtruth);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_TRAIN_FILES, train_files);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_TEST_FILES, test_files);		
	}
	
	
		
}
