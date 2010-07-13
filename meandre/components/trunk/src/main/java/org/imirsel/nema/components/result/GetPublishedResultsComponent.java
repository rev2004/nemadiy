package org.imirsel.nema.components.result;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaPublishedResult;
import org.imirsel.nema.renderers.CollectionRenderer;
import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;


@Component(creator = "Mert Bay", description = "Takes the collection ID and pushes the names, paths for the publised results for the collection and associated groundtruth", name = "GetPublishedResultsComponent",resources={"../../../../RepositoryProperties.properties"},
		tags = "dataset, published, results", firingPolicy = Component.FiringPolicy.all)
	
	public class GetPublishedResultsComponent extends NemaComponent {

	@StringDataType(renderer=CollectionRenderer.class)
	@ComponentProperty(description = "integer datasetID", name = "datasetID", defaultValue = "5")
	public final static String DATA_INPUT_ID = "datasetID";
	private int datasetid = 5;

	
	@ComponentOutput(description = "String[] that holds the file names of subs published for the collection", name = "Names")
	public final static String DATA_OUTPUT_NAMES = "Names";
	
	@ComponentOutput(description = "String[] that holds the file location for the results published for the collection", name = "Results")
	public final static String DATA_OUTPUT_RESULTS = "Results";
	

	@ComponentOutput(description = "String[] that holds the file paths for the groundtruth for the collection", name = "CollectionGroundtruth")
	public final static String DATA_OUTPUT_GROUNDTRUTH = "CollectionGroundtruth";

	
	private java.io.PrintStream cout;
	private String[] names;
	private String[] paths;
	private File[] gt_and_featExt_files;
	private String[] groundtruth ;
	private Set<NemaMetadataEntry>  file_encoding_constraint;
	private String processWorkingDirName;
	private File processWorkingDir;

	private final String bitRate = "";
	private final String channels = "1";	
	private final String clip_type = "30";
	private final String encoding = "wav";
	private final String sample_rate = "22050";
	private final String delim = "\t";

	
	
	
	
	public void initialize(ComponentContextProperties cc) throws  ComponentExecutionException {
		cout = cc.getOutputConsole();		
		try {
			processWorkingDirName=ArtifactManagerImpl.getInstance(cc.getPublicResourcesDirectory()).getProcessWorkingDirectory(cc.getFlowExecutionInstanceID());
		} catch (IOException e1) {
			throw new ComponentExecutionException(e1);
		}
		processWorkingDir = new File(processWorkingDirName);
		getLogger().info("Process working dir name: "  + processWorkingDir.getAbsolutePath());
		datasetid = Integer.parseInt(String.valueOf(cc.getProperty(DATA_INPUT_ID))) ;



	}
	
	public void dispose(ComponentContextProperties ccp) {

	}
	
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException {
		
       // String ID = (String)ccp.getDataComponentFromInput(DATA_INPUT_ID);
     //   int datasetID = Integer.parseInt(ID);
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
	
		List<NemaPublishedResult> resultList = null;
		RepositoryClientInterface client =null;
		try{
		   client = pool.getFromPool();
		   resultList = client.getPublishedResultsForDataset(datasetid);
		   names = new String[resultList.size()];
		   paths = new String[resultList.size()];
		   int ctr =0;
			for (NemaPublishedResult thisResult:resultList ){
				//cout.println("Train File: " + thisFile[0].getCanonicalPath());
				//cout.println("Test File: " + thisFile[1].getCanonicalPath());
				paths[ctr]=thisResult.getResult_path();
				names[ctr]=thisResult.getName();
				System.out.println("Name " + ctr + ":" + names[ctr]);
				System.out.println("Path " + ctr + ":" + paths[ctr]);				
				ctr++;
			}	
		   
		} catch (SQLException e) {
			throw new ComponentExecutionException("SQLException in " + this.getClass().getName(),e);
		}
		finally{
			if(client!=null){
			pool.returnToPool(client);
			}
		}

	    file_encoding_constraint = DatasetListFileGenerator.buildConstraints(bitRate, channels, clip_type, encoding, sample_rate);		
		try {
			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetid, delim, processWorkingDir, file_encoding_constraint);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		groundtruth = new String[gt_and_featExt_files.length / 2];
		 try {
				groundtruth[0] = gt_and_featExt_files[0].getCanonicalPath();
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
			
		 ccp.pushDataComponentToOutput(DATA_OUTPUT_NAMES, names);
		 ccp.pushDataComponentToOutput(DATA_OUTPUT_RESULTS, paths);
		 ccp.pushDataComponentToOutput(DATA_OUTPUT_GROUNDTRUTH, groundtruth);
		
		
	
	}
	
	
		
}
