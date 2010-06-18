/**
 * 
 */
package org.imirsel.nema.components;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/**
 * This abstract class is extended by all the Nema Components.
 * @author kriswest
 * @author kumaramit01
 * @since 0.5.0
 * @version 0.6.0 -changed the job id to be a uuid string based on the flow execution instance
 */
public abstract class NemaComponent implements ExecutableComponent {

	// log messages are here
	private Logger _logger;
	private PrintStream logDestination = null;
	private String flowExecutionInstanceID= null;
	private String publicResourceDirectory = null;
	private String nemaResourceDirectory=null;
	private String processWorkingDirectory=null;
	private String processWorkingDirectoryAbsolutePath=null;
	private String resultLocationDirectory=null;
	private String resultLocationDirectoryAbsolutePath=null;
	private String commonLocationDirectory=null;
	private String commonLocationDirectoryAbsolutePath=null;
	
	
	/**
	 * Marks the cout as null after flushing and releases the resources.
	 * This method is called by the components extending this class.
	 * 
	 * @param componentContextProperties The component context properties of
	 * component.
	 * @throws ComponentContextException {@link ComponentContextException}
	 */
	public void dispose(ComponentContextProperties componentContextProperties) 
			throws ComponentContextException {
		getLogger().fine("Disposing of " + this.getClass().getName());
		componentContextProperties.getOutputConsole().flush();
	}

	/**
	 * All classes extending the NemaComponent class must implement this method.
	 */
	public abstract void execute(ComponentContext componentContext) 
			throws ComponentExecutionException, ComponentContextException;

	/**
	 * Initializes the the cout and sets up the logger.
	 * 
	 * @param componentContextProperties The Component Context Property of the component.
	 * @throws ComponentExecutionException {@link ComponentContextException}
	 * @throws ComponentContextException {@link ComponentContextException}
	 */
	public void initialize(ComponentContextProperties componentContextProperties)
			throws ComponentExecutionException, ComponentContextException {
		flowExecutionInstanceID= componentContextProperties.getFlowExecutionInstanceID();
		publicResourceDirectory=componentContextProperties.getPublicResourcesDirectory();
		
		getLogger().setLevel(Level.FINEST);
		synchronized(NemaComponent.class){
			nemaResourceDirectory =publicResourceDirectory+ File.separator+"nema";
			if(!(new File(nemaResourceDirectory)).exists()){
				File file = new File(nemaResourceDirectory);
				file.mkdir();
			}
			String jobId = getJobId(flowExecutionInstanceID);
			processWorkingDirectory = nemaResourceDirectory + File.separator + jobId;
			File file = new File(processWorkingDirectory);
			file.mkdir();
			try {
				processWorkingDirectoryAbsolutePath = file.getCanonicalPath();
				processWorkingDirectory = file.getPath();
			} catch (IOException e) {
				throw new ComponentContextException("Failed to get process working directory paths",e);
			}
			
			resultLocationDirectory= processWorkingDirectory + File.separator + "results";
			File file1 = new File(resultLocationDirectory);
			file1.mkdir();
			try {
				resultLocationDirectory = file1.getPath();
				resultLocationDirectoryAbsolutePath = file1.getCanonicalPath();
			} catch (IOException e) {
				throw new ComponentContextException("Failed to get process results directory paths",e);
			}
			
			commonLocationDirectory= publicResourceDirectory+ File.separator + "common";
			File file2 = new File(commonLocationDirectory);
			file2.mkdir();
			try {
				commonLocationDirectoryAbsolutePath = file2.getCanonicalPath();
				commonLocationDirectory = file2.getPath();
			} catch (IOException e) {
				throw new ComponentContextException("Failed to get common storage directory paths",e);
			}
			
			
			addLogDestination(componentContextProperties.getOutputConsole());
			getLogger().info("Initialized logging for " + this.getClass().getName());
		}
	}
	
	private String getJobId(String flowExecutionInstanceID) {
		String jobId= "uuid";
		String[] splits = flowExecutionInstanceID.split("/");
		if(splits.length>0){
			jobId = splits[splits.length-1];
		}
		return jobId;
	}

	/**
	 * 
	 * @return Logger for the components
	 */
	public Logger getLogger() {
		if (_logger == null){
			_logger = Logger.getLogger(this.getClass().getName());
		}
		return _logger;
	}

	public void addLogDestination(PrintStream stream) {
		logDestination = stream;
		Handler handler = new StreamHandler(logDestination, new ComponentLogFormatter());
		getLogger().addHandler(handler);
	}

	public PrintStream getLogDestination(){
		return logDestination;
	}
	
	/**
	 * 
	 * @return process working directory
	 */
	public String getProcessWorkingDirectory(){
		return this.processWorkingDirectory;
	}
	
	/**
	 * 
	 * @return result location for the the job
	 */
	public String getResultLocationForJob(){
		return this.resultLocationDirectory;
	}
	
	/**
	 * 
	 * @return absolute result location for the job
	 */
	public String getAbsoluteResultLocationForJob(){
		return this.resultLocationDirectoryAbsolutePath;
	}
	
	/**
	 * 
	 * @return common storage location -relative to the meandre
	 */
	public String getCommonStorageLocation(){
		return this.commonLocationDirectory;
	}
	
	/**
	 * 
	 * @return absolute common storage location
	 */
	public String getAbsoluteCommonStorageLocation(){
		return this.commonLocationDirectoryAbsolutePath;
	}
	
	/**
	 * 
	 * @return absolute process working directory
	 */
	public String getAbsoluteProcessWorkingDirectory(){
		return this.processWorkingDirectoryAbsolutePath;
	}
	
	/**
	 * 
	 * @return public resource directory
	 */
	public String getPublicResourceDirectory(){
		return this.publicResourceDirectory;
	}
	
}
