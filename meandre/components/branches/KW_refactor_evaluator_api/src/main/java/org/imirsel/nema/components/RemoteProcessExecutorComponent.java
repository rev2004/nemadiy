package org.imirsel.nema.components;


import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;


import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.entry.Name;

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
 * @since 0.2.0
 */
public abstract class RemoteProcessExecutorComponent extends NemaComponent {
	//private Logger logger = Logger.getLogger(this.getClass().getName());

	@ComponentProperty(defaultValue = "nema.lis.uiuc.edu", description = "Service Discovery Host", name = "host")
	private static final String PROPERTY_1 = "host";
	
	@ComponentProperty(defaultValue = "exampleRun", description = "Profile Name", name = "profileName")
	private static final String PROPERTY_2 ="profileName";

	private BlockingQueue<List<ProcessArtifact>> resultQueue = new LinkedBlockingQueue<List<ProcessArtifact>>();
	private Queue<NemaProcess> processQueue = new ConcurrentLinkedQueue<NemaProcess>();
	private CountDownLatch latch = new CountDownLatch(1);
	private ProcessExecutorService  executorService;
	private RemoteProcessMonitor processMonitor;
	private String profileName = null;

	
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		String host = ccp.getProperty(PROPERTY_1);
		profileName = ccp.getProperty(PROPERTY_2);
		LookupLocator locator=null;
		try {
			locator = new LookupLocator("jini://"+host);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServiceRegistrar registrar=null;
		try {
			registrar= locator.getRegistrar();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Name name = new Name(profileName);
		Entry[] attrList = new Entry[]{name};
		Class<ProcessExecutorService>[] classes = new Class[1]; 
		classes[0] = ProcessExecutorService.class;
		ServiceTemplate template = new ServiceTemplate(null,classes,attrList);
	  
	    RemoteOutputStream ros = new SimpleRemoteOutputStream(ccp.getOutputConsole());
			RemoteProcessMonitor remoteProcessMonitor = null;
			try {
				executorService = ( ProcessExecutorService) registrar.lookup(template);
				remoteProcessMonitor = new RecordStreamProcessMonitor(latch, ros,resultQueue,processQueue);
				setProcessMonitor(remoteProcessMonitor);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		getLogger().info("ExecutorService found");
	}


	public abstract void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException;

	


	/**This method aborts all the running processes when the component is disposed.
	 * 
	 */
	public void dispose(ComponentContextProperties ccp)
			throws ComponentContextException {
		// abort all the processes that are still running -we are disposing the flow.
		getLogger().info("Aborting all the running processes.");
		abortAllProcesses();
		super.dispose(ccp);
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
	 * @throws RemoteException 
	 * @throws InvalidProcessTemplateException
	 */
	public final ProcessTemplate getProcessTemplate() throws  RemoteException, InvalidProcessTemplateException{
		ProcessTemplate processTemplate= executorService.getProcessTemplate(profileName);
		
		if(processTemplate==null){
			throw new InvalidProcessTemplateException("Profile: " + profileName + "  not found");
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
	 * @throws ComponentExecutionException 
	 * @throws InvalidProcessTemplateException 
	 */
	public final NemaProcess executeProcess(ProcessExecutionProperties processExecutionProperties) throws RemoteException, InvalidProcessMonitorException, ComponentExecutionException, InvalidProcessTemplateException{
		if(this.getProcessMonitor()==null){
			throw new InvalidProcessMonitorException("Process Monitor is NULL");
		}
		if(processExecutionProperties.getId()==null){
			throw new IllegalArgumentException("ProcessExecutionProperties -id is not set");
		}
		ProcessTemplate pt=this.getProcessTemplate();
		processExecutionProperties.setProcessTemplate(pt);
		NemaProcess np = this.getExecutorService().executeProcess(processExecutionProperties, this.getProcessMonitor());	
		return np;
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
		getLogger().info("Aborting: " + process.getId());
		boolean success=this.getExecutorService().abort(process, processMonitor);
		getLogger().info("Abort success: " + success);
		return success;
	}
	
	
	
	/**Dispatches abort process message to all the processes this component is running.
	 *  This method is called by the dispose method
	 */
	public final void abortAllProcesses(){
		Iterator<NemaProcess> np = processQueue.iterator();
		while(np.hasNext()){
			NemaProcess process = np.next();
			if(np!=null){
				getLogger().severe("Aborting: " + process.getId());
				try{
					abortProcess(process);
				}catch(Exception ex){
					System.err.println("Error dispatching abort command to the process: " + process.getId()+ " It might have already finished.");
				}
			}
		}
		
	}
	
	/**Returns the name of the profile
	 * 
	 */
	public String getProfileName(){
		return this.profileName;
	}


}
