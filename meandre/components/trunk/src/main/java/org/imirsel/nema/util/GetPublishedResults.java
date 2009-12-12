package org.imirsel.nema.util;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.imirsel.nema.renderers.CollectionRenderer;
import org.imirsel.service.ArtifactManagerImpl;
import org.imirsel.nema.annotations.*;
import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.imirsel.nema.repository.NEMAMetadataEntry;
import org.imirsel.nema.repository.PublishedResult;

import java.sql.SQLException;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;


@Component(creator = "Mert Bay", description = "Outputs the published results for a dataset", name = "PublishedResults",resources={"../../../../RepositoryProperties.properties"},
					tags = "dataset, published, results", firingPolicy = Component.FiringPolicy.all)
	
	public class GetPublishedResults implements ExecutableComponent {

	@ComponentOutput(description = "integer datasetID", name = "datasetID")
	public final static String DATA_INPUT_ID = "datasetID";
	
	
	@ComponentOutput(description = "String[] that holds the file names of subs published for the collection", name = "Names")
	public final static String DATA_OUTPUT_NAMES = "Names";
	
	@ComponentOutput(description = "String[] that holds the file location for the results published for the collection", name = "Results")
	public final static String DATA_OUTPUT_RESULTS = "Results";
	

	@ComponentOutput(description = "String[] that holds the file location for the groundtruth for the collection", name = "CollectionGroundtruth")
	public final static String DATA_OUTPUT_GROUNDTRUTH = "CollectionGroundtruth";

	
	private java.io.PrintStream cout;
	private String[] names;
	private String[] paths;
	private File[] gt_and_featExt_files;
	private String[] groundtruth ;
	private Set<NEMAMetadataEntry>  file_encoding_constraint;
	private String processWorkingDirName;
	private File processWorkingDir;

	
	public void initialize(ComponentContextProperties cc) throws  ComponentExecutionException {
		cout = cc.getOutputConsole();		
		try {
			processWorkingDirName=ArtifactManagerImpl.getInstance()
			.getProcessWorkingDirectory(
					cc.getFlowExecutionInstanceID());				
		} catch (IOException e1) {
			throw new ComponentExecutionException(e1);
		}
		processWorkingDir = new File(processWorkingDirName);


	}
	
	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub
	}
	
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException {
		
        String ID = (String)ccp.getDataComponentFromInput(DATA_INPUT_ID);
        int datasetID = Integer.parseInt(ID);
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
		RepositoryClientImpl client = pool.getFromPool();
		List<PublishedResult> resultList = null;
		try{
		   resultList = client.getPublishedResultsForDataset(datasetID);
		   names = new String[resultList.size()];
		   paths = new String[resultList.size()];
		   int ctr =0;
			for (PublishedResult thisResult:resultList ){
				//cout.println("Train File: " + thisFile[0].getCanonicalPath());
				//cout.println("Test File: " + thisFile[1].getCanonicalPath());
				names[ctr]=thisResult.getResult_path();
				paths[ctr]=thisResult.getName();
				cout.println("Name " + ctr + ":" + names[ctr]);
				cout.println("Path " + ctr + ":" + paths[ctr]);				
				ctr++;
			}	
		   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		   pool.returnToPool(client);
		}

	    file_encoding_constraint = DatasetListFileGenerator.buildConstraints("", "1", "30", "wav", "220050");		

		try {
			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetID, "\t", processWorkingDir, file_encoding_constraint);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
