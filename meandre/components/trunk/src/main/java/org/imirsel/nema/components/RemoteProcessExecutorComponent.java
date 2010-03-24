package org.imirsel.nema.components;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.imirsel.nema.monitor.process.RecordStreamProcessMonitor;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.imirsel.nema.service.executor.RemoteProcessMonitor;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;

/**Extend the RemoteProcessExecutorComponent to call remote processes in your NEMA components.
 * 
 * @author kumaramit01
 * @since 0.2.0-SNAPSHOT
 */
public abstract class RemoteProcessExecutorComponent implements ExecutableComponent {

	@ComponentProperty(defaultValue = "localhost", description = "Executor Service Host", name = "host")
	private static final String PROPERTY_2 = "host";
	
	@ComponentProperty(defaultValue = "2098", description = "Executor Service port", name = "port")
	private static final String PROPERTY_3 = "port";
	
	
	@ComponentProperty(defaultValue = "ExecutorService", description = "Executor Service Name", name = "serviceName")
	private static final String PROPERTY_4 = "serviceName";

	
	private CountDownLatch latch = new CountDownLatch(1);
	private BlockingQueue<List<ProcessArtifact>> resultQueue = new LinkedBlockingQueue<List<ProcessArtifact>>();
	private ProcessExecutorService  executorService;
	private RemoteProcessMonitor processMonitor;

	
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	
		String host = ccp.getProperty(PROPERTY_2);
		String _port = ccp.getProperty(PROPERTY_3);	
		int port = Integer.parseInt(_port);
		String serviceName = ccp.getProperty(PROPERTY_4);
		
		
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(host,port);
			executorService = ( ProcessExecutorService) registry.lookup(serviceName);
			System.out.println(executorService.getProcessTemplates());
		
			RemoteOutputStream ros = new SimpleRemoteOutputStream(ccp.getOutputConsole());
			setProcessMonitor(new RecordStreamProcessMonitor(latch, ros,resultQueue));
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new ComponentExecutionException(e);
		} catch (NotBoundException e) {
			e.printStackTrace();
			throw new ComponentExecutionException(e);
		}
	}


	public abstract void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException;

	


	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	}

	
	/**Returns the {@link ProcessExecutorService}
	 * 
	 * @return the process executor service
	 */
	public final ProcessExecutorService getExecutorService() {
		return executorService;
	}


	
	private final void setProcessMonitor(RemoteProcessMonitor processMonitor) {
		this.processMonitor = processMonitor;
	}


	/** Returns the process lifecycle monitor
	 * 
	 * @return processMonitor
	 */
	public RemoteProcessMonitor getProcessMonitor() {
		return processMonitor;
	}
	
	/** Returns Process Template
	 * 
	 * @return process template @{link ProcessTemplate"
	 * @throws ComponentExecutionException 
	 * @throws RemoteException 
	 */
	public final ProcessTemplate getProcessTemplate(String profile) throws ComponentExecutionException, RemoteException{
		List<ProcessTemplate> processTemplateList= executorService.getProcessTemplates();
		ProcessTemplate processTemplate = null;
		for(ProcessTemplate pt:processTemplateList){
			if(pt.getId().equalsIgnoreCase(profile)){
				processTemplate = pt;
				break;
			}
		}
		
		if(processTemplate==null){
			throw new ComponentExecutionException("Profile: " + profile + "  not found");
		}
		return processTemplate;
	}
	
	/**Wait for the process to finish before continuing. This method
	 * blocks.
	 * 
	 * @throws InterruptedException
	 */
	public final void waitForProcess() throws InterruptedException{
		latch.await();
	}
	
	
	/** Returns the process result. <b>Call this method after waiting for the process to finish</b>.
	 * This method will return null if the process has not finished or if the process does not
	 * produce any result.
	 * 
	 * @return the result produced by the process as a list of {@link ProcessArtifact}
 	 */
	public final List<ProcessArtifact> getResult(){
		return resultQueue.poll();
	}
	
	
	/** Starts the execution of the remote process.
	 * 
	 * @param processExecutionProperties
	 * @return NemaProcess -reference to remote process
	 * @throws RemoteException
	 * @throws InvalidProcessMonitorException
	 */
	public final NemaProcess executeProcess(ProcessExecutionProperties processExecutionProperties) throws RemoteException, InvalidProcessMonitorException{
		if(this.getProcessMonitor()==null){
			throw new InvalidProcessMonitorException("Process Monitor is NULL");
		}
		if(processExecutionProperties.getId()==null){
			throw new IllegalArgumentException("ProcessExecutionProperties -id is not set");
		}
		
		return this.getExecutorService().executeProcess(processExecutionProperties, this.getProcessMonitor());	
	}
	
	/** Aborts the remote process.
	 * 
	 * @param process
	 * @return success or failure result
	 * @throws RemoteException
	 */
	public final boolean abortProcess(NemaProcess process) throws RemoteException{
		if(process == null){
			throw new IllegalArgumentException("Invalid process");
		}
		boolean success=this.getExecutorService().abort(process);
		return success;
	}


}
