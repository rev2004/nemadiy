package org.imirsel.nema.test.components.remote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jcr.SimpleCredentials;

import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.imirsel.nema.monitor.process.RecordStreamProcessMonitor;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.imirsel.nema.service.executor.RemoteProcessMonitor;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;

/**
 * @deprecated
 * @author kriswest
 *
 */
@Component(creator = "Amit Kumar", description = "Creates a file with Time on the server", 
		name = "RemoteTimeComponent", tags = "remote time test")
public class RemoteTimeComponent implements ExecutableComponent{
	
	@ComponentProperty(defaultValue = "timeOfTheDay", description = "name of the execution Profile", name = "execProfile")
	private static final String PROPERTY_1 ="execProfile";
	@ComponentProperty(defaultValue = "nema.lis.uiuc.edu", description = "Executor Service Host", name = "host")
	private static final String PROPERTY_2 = "host";
	
	@ComponentProperty(defaultValue = "1098", description = "Executor Service port", name = "port")
	private static final String PROPERTY_3 = "port";
	
	
	@ComponentProperty(defaultValue = "ExecutorService", description = "Executor Service Name", name = "serviceName")
	private static final String PROPERTY_4 = "serviceName";
	
	

	@ComponentProperty(defaultValue = "test:test", description = "Credentials to Login to the content repository", name = "_credentials")
	private static final String PROPERTY_5="_credentials";


	
	
	@ComponentOutput(description = "This is the process artifact data",
			name = "processResult")
	private static final String DATA_OUTPUT_1="processResult";
	
	
	
	private CountDownLatch latch = new CountDownLatch(1);
	private BlockingQueue<List<ProcessArtifact>> resultQueue = new LinkedBlockingQueue<List<ProcessArtifact>>();
	private Queue<NemaProcess> processList = new ConcurrentLinkedQueue<NemaProcess>();
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		System.out.println("Thread ID is: " + Thread.currentThread().getId());
	}


	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		System.out.println("Thread ID is: " + Thread.currentThread().getId());
		String profile = cc.getProperty(PROPERTY_1);
		String host = cc.getProperty(PROPERTY_2);
		String _port = cc.getProperty(PROPERTY_3);	
		int port = Integer.parseInt(_port);
		String serviceName = cc.getProperty(PROPERTY_4);
		String _credentials = cc.getProperty(PROPERTY_5);	
		
		SimpleCredentials sc = parseCredentials(_credentials);
		
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(host,port);
			ProcessExecutorService service = ( ProcessExecutorService) registry.lookup(serviceName);
			System.out.println(service.getProcessTemplates());
			List<ProcessTemplate> processTemplateList= service.getProcessTemplates();
			ProcessTemplate processTemplate=null;
			for(ProcessTemplate pt:processTemplateList){
				System.out.println(pt.getShellScript());
				if(pt.getId().equalsIgnoreCase(profile)){
					processTemplate = pt;
					break;
				}
			}
			
			if(processTemplate==null){
				throw new ComponentExecutionException("Profile: " + profile + "  not found");
			}
			
			
			
			// create the process execution properties
			
			ProcessExecutionProperties pep = new ProcessExecutionProperties();
			pep.setId("0");
			pep.setProcessTemplate(processTemplate);
			ProcessArtifact pa = new ProcessArtifact("/tmp/"+pep.getId()+".date","file");
			List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
			outputs.add(pa);
			pep.setOutputs(outputs);
			pep.setInputs(null);
			RemoteOutputStream ros = new SimpleRemoteOutputStream(cc.getOutputConsole());
			
			RemoteProcessMonitor processMonitor = new RecordStreamProcessMonitor(latch, ros,resultQueue,processList);
			// start the process
			@SuppressWarnings("unused")
			NemaProcess process=service.executeProcess(sc,pep, processMonitor);
			System.out.println("Now waiting for the process to finish...");
			try {
				latch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// now block until the lock is opened
			//WAIT HERE
			System.out.println("process finished -we can push data out now");
	
			List<ProcessArtifact> result=resultQueue.poll();
			System.out.println("--> result length: " + result.size());
			
			for(ProcessArtifact par:result){
				System.out.println(par.toString());
			}
			System.out.println("====> "+ result);
			if(result!=null){
				cc.pushDataComponentToOutput(DATA_OUTPUT_1, result);
			}else{
				throw new ComponentExecutionException("Error result was null...");
			}
			
		} catch (RemoteException e) {
			throw new ComponentExecutionException(e);
		} catch (NotBoundException e) {
			throw new ComponentExecutionException(e);
		}
		
		
		
		
		
		
	}

	private SimpleCredentials parseCredentials(String credentialsString) throws ComponentExecutionException {
		String[] splits = credentialsString.split(":");
		String username = splits[0];
		String password = splits[1];
		if(splits.length!=2){
			throw new ComponentExecutionException("Invalid credentials");
		}
		return new SimpleCredentials(username,password.toCharArray());
	}
	


	public void dispose(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		System.out.println("Component disposed: " + this.getClass().getName());
		//latch.countDown();
	}

}
