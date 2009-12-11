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

	
	
	@ComponentOutput(description = "String[] that holds the file location for the feature extraction list file", name = "featExtList")
	public final static String DATA_OUTPUT_NAMES = "Names";
	
	@ComponentOutput(description = "String[] that holds the file location for the feature extraction list file", name = "featExtList")
	public final static String DATA_OUTPUT_RESULTS = "PublishedResultPaths";
	

	
	private int datasetID=5;
	private java.io.PrintStream cout;
	

	
	
	public void initialize(ComponentContextProperties cc) throws  ComponentExecutionException {
		cout = cc.getOutputConsole();			

	}
	
	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub
	}
	
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException {
		
		
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
		RepositoryClientImpl client = pool.getFromPool();
		List<PublishedResult> resultList = null;
		try{
		   resultList = client.getPublishedResultsForDataset(datasetID);
		   
		   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		   pool.returnToPool(client);
		}

		

		
		
		
	
	}
	
	
		
}
