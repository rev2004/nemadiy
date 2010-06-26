package org.imirsel.nema.components;


import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.entry.Name;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.contentrepository.client.ContentRepositoryService;
import org.imirsel.nema.contentrepository.client.ResultStorageService;
import org.imirsel.nema.model.DynamicType;
import org.imirsel.nema.model.OsType;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.model.ResourceGroupEntry;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.imirsel.nema.monitor.process.RecordStreamProcessMonitor;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.imirsel.nema.service.executor.RemoteProcessMonitor;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;

/** Base Class for Remote Nema Components
 * 
 * @author kumaramit01
 * @since 0.6.0
 */
public abstract class RemoteExecutorBase extends NemaComponent implements RemoteExecutableComponent{

	@StringDataType(valueList={"imirsel","mcgill"}, labelList={"imirsel","mcgill"})
	@ComponentProperty(defaultValue = "imirsel", description = "execution group", name = "_group")
	private static final String PROPERTY_1 ="_group";

	@StringDataType(valueList={"Unix Like","Windows Like"}, labelList={"Unix","Windows"})
	@ComponentProperty(defaultValue = "Unix Like", description = "operating system", name = "_os")
	private static final String PROPERTY_2 ="_os";

	
	@ComponentProperty(defaultValue = "test:test", description = "Credentials to Login to the content repository", name = "_credentials")
	private static final String PROPERTY_4="_credentials";

	
	@ComponentProperty(defaultValue = "rmi://nema-dev.lis.illinois.edu:/..", description = "Content Repository URI", name = "_contentRepositoryUri")
	private static final String PROPERTY_5="_contentRepositoryUri";

	@ComponentProperty(description="lookupHost", name="_lookupHost", defaultValue = "nema.lis.uiuc.edu")
	private static final String PROPERTY_6 ="_lookupHost";
	
	
	
	@ComponentInput(description="process template", name="processTemplate")
	private static final String DATA_IN_1 ="processTemplate";
	
	
	private ConcurrentHashMap<NemaProcess,RecordStreamProcessMonitor> processMonitorMap = 
		new ConcurrentHashMap<NemaProcess,RecordStreamProcessMonitor>();

	private static ResultStorageService resultStorageService;
	private SimpleCredentials credentials;
	private String group;
	private String OS;
	private String lookupHost;
	private ProcessTemplate processTemplate;
	private ComponentContextProperties componentContextProperties;
	private ProcessExecutorService processExecutorService;
	private String flowInstanceId;
	
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		try{
		String group= ccp.getProperty(PROPERTY_1);
		String os= ccp.getProperty(PROPERTY_2);
		String _credentials = ccp.getProperty(PROPERTY_4);
		String contentRepositoryUri = ccp.getProperty(PROPERTY_5);
		String lookupHost = ccp.getProperty(PROPERTY_6);
		
		this.componentContextProperties = ccp;
		this.flowInstanceId = ccp.getFlowExecutionInstanceID();
		this.setGroup(group);
		this.setOS(os);
		this.setLookupHost(lookupHost);
		setCredentials(parseCredentials(_credentials));
		
		synchronized(RemoteExecutorBase.class){
		if(resultStorageService==null){
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repository;
			try {
				repository = factory.getRepository(contentRepositoryUri);
			} catch (MalformedURLException e) {
				throw new ComponentExecutionException(e);
			} catch (ClassCastException e) {
				throw new ComponentExecutionException(e);
			} catch (RemoteException e) {
				throw new ComponentExecutionException(e);
			} catch (NotBoundException e) {
				throw new ComponentExecutionException(e);
			}
			ContentRepositoryService crs = new ContentRepositoryService();
			crs.setRepository(repository);
			resultStorageService = crs;
		}
		
		
		}}finally{
			this.initializeNema(ccp);	
		}
	}
	



	public void execute(ComponentContext context) throws ComponentExecutionException,ComponentContextException{
		try{
		processTemplate = (ProcessTemplate)context.getDataComponentFromInput(DATA_IN_1);
		try {
			this.processExecutorService = findExecutorService();
			if(this.processExecutorService==null){
				context.getLogger().info("Error could not find the executor service");
			}else{
				context.getLogger().info("====> PROCESS EXECUTION ID"+this.processExecutorService.getId());
			}
		} catch (RemoteException e) {
			throw new ComponentExecutionException(e);
		}}finally{
		this.executeNema(context);
		}
	}
	
	
	public void dispose(ComponentContextProperties componentContextProperties) 
	throws ComponentContextException{
		super.dispose(componentContextProperties);
		this.disposeNema(componentContextProperties);
	}
	
	
	public abstract void initializeNema(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException;
	
	
	public abstract void executeNema(ComponentContext componentContext) 
	throws ComponentExecutionException, ComponentContextException;
	
	
	public abstract void disposeNema(ComponentContextProperties componentContextProperties) 
	throws ComponentContextException;
	
	
	
	private SimpleCredentials parseCredentials(String credentialsString) throws ComponentExecutionException {
		String[] splits = credentialsString.split(":");
		String username = splits[0];
		String password = splits[1];
		if(splits.length!=2){
			throw new ComponentExecutionException("Invalid credentials");
		}
		return new SimpleCredentials(username,password.toCharArray());
	}
	
	
	private ProcessExecutorService findExecutorService() throws RemoteException{
		LookupLocator locator=null;
		try {
			locator = new LookupLocator("jini://"+lookupHost);
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
		System.out.println("PROCESS TEMPLATE: " + this.getProcessTemplate().getName() + "  " + this.getProcessTemplate().getId());
		Name name = new Name(this.getProcessTemplate().getName());
		getLogger().info("Profile Name is: " + this.getProcessTemplate().getName());
		
		DynamicType dynamicType = new DynamicType();
		OsType osType = new OsType(this.getOS());
		ResourceGroupEntry rgt = new ResourceGroupEntry(this.getGroup());
		
		ProcessExecutorService executorService=null;
		
		Entry[] attrList = new Entry[]{name};
		Class<ProcessExecutorService>[] classes = new Class[1]; 
		classes[0] = ProcessExecutorService.class;
		ServiceTemplate template = new ServiceTemplate(null,classes,attrList);
				ServiceMatches serviceMatches=registrar.lookup(template,10);
				ProcessExecutorService serviceFound=null;
				
				if(serviceMatches.totalMatches>0){
					getLogger().info("Found:  " + serviceMatches.totalMatches);
					int min = Integer.MAX_VALUE;
					for(ServiceItem item:serviceMatches.items){
						ProcessExecutorService pes = (ProcessExecutorService)item.service;
						System.out.println("Number of processes running: "+pes.numProcesses());
						if(min> pes.numProcesses()){
							serviceFound =  pes;
							min= pes.numProcesses();
						}
					}
					
					
				}else{
					getLogger().info("NO PROCESS EXECUTOR SERVICE FOUND: "  + name);
				}
				
				if(serviceFound==null){
					throw new RemoteException("Suitable Service not found " + this.getProcessTemplate().getName());
				}
				executorService = serviceFound;
				this.getLogger().info("Selecting: " + executorService.toString() + " for the execution. ");
				
			
		
		return executorService;
		
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
	 */
	public final NemaProcess executeProcess(ProcessExecutionProperties processExecutionProperties) throws RemoteException{
		if(processExecutionProperties.getId()==null){
			throw new IllegalArgumentException("ProcessExecutionProperties -id is not set");
		}
		synchronized(this){
			RecordStreamProcessMonitor rpm=createRemoteProcessMonitor();
			ProcessTemplate pt=this.getProcessTemplate();
			processExecutionProperties.setProcessTemplate(pt);
			processExecutionProperties.setCredentials(getCredentials());
			processExecutionProperties.setFlowInstanceId(this.flowInstanceId);
			if(this.getProcessExecutorService()==null){
				System.out.println("ERROR: EXECUTOR SERVICE IS NULL");
			}
			NemaProcess np = this.getProcessExecutorService().executeProcess(processExecutionProperties, rpm);	
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
		boolean success=this.getProcessExecutorService().abort(process, processMonitor);
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
					boolean success=this.getProcessExecutorService().abort(process, processMonitor);
					getLogger().info("Abort success: " + success);
				}catch(Exception ex){
					System.err.println("Error dispatching abort command to the process: " + process.getId()+ " It might have already finished.");
				}
			}
		}
		this.processMonitorMap.clear();
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
	
	
	

	private void setCredentials(SimpleCredentials credentials) {
		this.credentials = credentials;
	}

	public SimpleCredentials getCredentials() {
		return credentials;
	}

	private void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

	private void setOS(String oS) {
		OS = oS;
	}

	public String getOS() {
		return OS;
	}

	public void setProcessTemplate(ProcessTemplate processTemplate) {
		this.processTemplate = processTemplate;
	}

	public ProcessTemplate getProcessTemplate() {
		return processTemplate;
	}

	public String getLookupHost() {
		return lookupHost;
	}

	public void setLookupHost(String lookupHost) {
		this.lookupHost = lookupHost;
	}


	private ProcessExecutorService getProcessExecutorService() {
		return processExecutorService;
	}




	/**Return the ResultStorageService
	 * 
	 * @return resultStorageService -returns the result storage service
	 */
	public static ResultStorageService getResultStorageService() {
		return resultStorageService;
	}


}
