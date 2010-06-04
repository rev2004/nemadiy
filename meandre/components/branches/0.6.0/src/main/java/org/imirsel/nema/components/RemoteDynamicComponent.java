package org.imirsel.nema.components;


import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jcr.SimpleCredentials;


import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.entry.Name;

import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.imirsel.nema.monitor.process.RecordStreamProcessMonitor;
import org.imirsel.nema.service.executor.DynamicType;
import org.imirsel.nema.service.executor.OsType;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.imirsel.nema.service.executor.RemoteProcessMonitor;
import org.imirsel.nema.service.executor.ResourceGroupEntry;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;

/**Extend the RemoteTemplateComponent to call remote processes in your NEMA components.
 * 
 * @author kumaramit01
 * @since 0.3.0
 */
public abstract class RemoteDynamicComponent extends NemaComponent {
	//private Logger logger = Logger.getLogger(this.getClass().getName());

	@ComponentProperty(defaultValue = "nema.lis.uiuc.edu", description = "Service Discovery Host", name = "host")
	private static final String PROPERTY_1 = "host";
	
	@ComponentProperty(defaultValue = "exampleRun", description = "Profile Name", name = "profileName")
	private static final String PROPERTY_2 ="profileName";
	
	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "true", description = "indicates to the UI that this is a remote component", name = "_remoteComponent")
	private static final String PROPERTY_3 ="_remoteComponent";
	
	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "test:test", description = "", name = "_credentials")
	private static final String PROPERTY_4 ="_credentials";
	
	@StringDataType(valueList={"imirsel","imirsel"}, labelList={"imirsel","imirsel"})
	@ComponentProperty(defaultValue = "imirsel", description = "execution group", name = "_group")
	private static final String PROPERTY_5 ="_group";
	
	@StringDataType(valueList={"Unix Like","Windows Like"}, labelList={"Unix","Windows"})
	@ComponentProperty(defaultValue = "Unix Like", description = "operating system", name = "_os")
	private static final String PROPERTY_6 ="_os";
	
	
	
	private ConcurrentHashMap<NemaProcess,RecordStreamProcessMonitor> processMonitorMap = 
		new ConcurrentHashMap<NemaProcess,RecordStreamProcessMonitor>();

	private ComponentContextProperties componentContextProperties;
	private ProcessExecutorService  executorService;
	private String profileName = null;
	private SimpleCredentials _credentials=null;
	private String _group = "imirsel";	
	private String _os = "Unix Like";	

	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		this.componentContextProperties=ccp;
		String host = ccp.getProperty(PROPERTY_1);
		profileName = ccp.getProperty(PROPERTY_2);
		_group=ccp.getProperty(PROPERTY_5);
		_os=ccp.getProperty(PROPERTY_6);
		
		String credentialString = ccp.getProperty(PROPERTY_4);
		
		String[] splits = credentialString.split(":");
		if(splits.length<2){
			throw new ComponentExecutionException("Error could not get credentials");
		}
		_credentials = new SimpleCredentials(splits[0], splits[1].toCharArray());

		
		
		
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
		DynamicType dynamicType = new DynamicType();
		OsType osType = new OsType(_os);
		ResourceGroupEntry rgt = new ResourceGroupEntry(_group);
		
		Entry[] attrList = new Entry[]{name,osType,rgt,dynamicType};
		Class<ProcessExecutorService>[] classes = new Class[1]; 
		classes[0] = ProcessExecutorService.class;
		ServiceTemplate template = new ServiceTemplate(null,classes,attrList);
			try {
				ServiceMatches serviceMatches=registrar.lookup(template,10);
				ProcessExecutorService serviceFound=null;
				
				if(serviceMatches.totalMatches>0){
					int min = Integer.MAX_VALUE;
					for(ServiceItem item:serviceMatches.items){
						ProcessExecutorService pes = (ProcessExecutorService)item;
						System.out.println("Number of processes running: "+pes.numProcesses());
						if(min> pes.numProcesses()){
							serviceFound =  pes;
							min= pes.numProcesses();
						}
					}
					
					
				}
				
				if(serviceFound==null){
					throw new ComponentExecutionException("Suitable Service not found");
				}
				executorService = serviceFound;
				System.out.println("Selecting: " + executorService.toString() + " for the execution. ");
				
			}catch (RemoteException e) {
				throw new ComponentExecutionException(e.getMessage());
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
		this.processMonitorMap.clear();
		super.dispose(ccp);
	}

	
	/**Returns the {@link ProcessExecutorService}
	 * 
	 * @return the process executor service
	 */
	public final ProcessExecutorService getExecutorService() {
		return executorService;
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
	public final void waitForProcess(NemaProcess nemaProcess) throws InterruptedException{
		getLogger().info("waiting on the latch\n");
		RecordStreamProcessMonitor rpm=this.processMonitorMap.get(nemaProcess);
		rpm.getLatch().await();
		getLogger().info("latch unlatched -run another process now\n");
	}
	
	
	/** Returns the process result. <b>Call this method after waiting for the process to finish</b>.
	 * This method will return null if the process has not finished or if the process does not
	 * produce any result.
	 * 
	 * @return the result produced by the process as a list of {@link ProcessArtifact}
 	 */
	public final List<ProcessArtifact> getResult(NemaProcess nemaProcess){
		RecordStreamProcessMonitor rpm =this.getProcessMonitor(nemaProcess);
		if(rpm==null){
			getLogger().severe("Calling getResults on a process that does not have the RemoteProcessMonitor " +
					"\n Either the process finished, or was aborted.");
			return null;
		}
		return rpm.getResultQueue().poll();
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
		if(processExecutionProperties.getId()==null){
			throw new IllegalArgumentException("ProcessExecutionProperties -id is not set");
		}
		synchronized(this){
			RecordStreamProcessMonitor rpm=createRemoteProcessMonitor();
			ProcessTemplate pt=this.getProcessTemplate();
			processExecutionProperties.setProcessTemplate(pt);
			NemaProcess np = this.getExecutorService().executeProcess(processExecutionProperties, rpm);	
			this.processMonitorMap.put(np, rpm);
			return np;
		}
		
	}
	
	/** Remove the process monitor from the hashmap
	 * 
	 * @param process
	 */
	public final void cleanProcess(NemaProcess process){
		this.processMonitorMap.remove(process);
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
		RemoteProcessMonitor processMonitor = this.getProcessMonitor(process);
		getLogger().info("Aborting: " + process.getId());
		boolean success=this.getExecutorService().abort(process, processMonitor);
		this.processMonitorMap.remove(process);
		getLogger().info("Abort success: " + success);
		return success;
	}
	
	
	
	/**Dispatches abort process message to all the processes this component is running.
	 *  This method is called by the dispose method
	 */
	public final void abortAllProcesses(){
		Iterator<NemaProcess> np = this.processMonitorMap.keySet().iterator();
		while(np.hasNext()){
			NemaProcess process = np.next();
			if(np!=null){
				getLogger().severe("Aborting: " + process.getId());
				try{
					RemoteProcessMonitor processMonitor = this.getProcessMonitor(process);
					getLogger().info("Aborting: " + process.getId());
					boolean success=this.getExecutorService().abort(process, processMonitor);
					getLogger().info("Abort success: " + success);
				}catch(Exception ex){
					System.err.println("Error dispatching abort command to the process: " + process.getId()+ " It might have already finished.");
				}
			}
		}
		this.processMonitorMap.clear();
	}
	
	/**Returns the name of the profile
	 * 
	 */
	public String getProfileName(){
		return this.profileName;
	}

	
	private RecordStreamProcessMonitor createRemoteProcessMonitor() throws RemoteException{
		BlockingQueue<List<ProcessArtifact>> resultQueue = new LinkedBlockingQueue<List<ProcessArtifact>>();
		Queue<NemaProcess> processQueue = new ConcurrentLinkedQueue<NemaProcess>();
		CountDownLatch latch = new CountDownLatch(1);
		RemoteOutputStream ros = new SimpleRemoteOutputStream(this.componentContextProperties.getOutputConsole());
		RecordStreamProcessMonitor remoteProcessMonitor = null;
		remoteProcessMonitor = new RecordStreamProcessMonitor(latch, ros,resultQueue,processQueue);
		return remoteProcessMonitor;
	}
	
	
	private RecordStreamProcessMonitor getProcessMonitor(NemaProcess nemaProcess){
		return this.processMonitorMap.get(nemaProcess);
	}
	
	/**
	 * Used to connect to the credentials 
	 * 
	 * @return the credentials 
	 */
	public SimpleCredentials getContentRepositoryCredentials(){
		return this._credentials;
	}
	
	

}
