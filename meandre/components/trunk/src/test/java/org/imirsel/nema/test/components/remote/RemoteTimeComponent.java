package org.imirsel.nema.test.components.remote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
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

@Component(creator = "Amit Kumar", description = "Creates a file with Time on the server", 
		name = "RemoteTimeComponent", tags = "remote time test")
public class RemoteTimeComponent implements ExecutableComponent{
	
	@ComponentProperty(defaultValue = "testProcessExecutionWithIOResults", description = "name of the execution Profile", name = "execProfile")
	private static final String PROPERTY_1 ="execProfile";
	@ComponentProperty(defaultValue = "localhost", description = "Executor Service Host", name = "host")
	private static final String PROPERTY_2 = "host";
	
	@ComponentProperty(defaultValue = "1098", description = "Executor Service port", name = "port")
	private static final String PROPERTY_3 = "port";
	
	
	@ComponentProperty(defaultValue = "ExecutorService", description = "Executor Service Name", name = "serviceName")
	private static final String PROPERTY_4 = "serviceName";
	
	@ComponentOutput(description = "This is the process artifat data",
			name = "processResult")
	private static final String DATA_OUTPUT_1="processResult";
	
	
	
	private CountDownLatch latch = new CountDownLatch(1);
	private BlockingQueue<List<ProcessArtifact>> resultQueue = new LinkedBlockingQueue<List<ProcessArtifact>>();
	
	
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
			ProcessArtifact pa = new ProcessArtifact();
			pa.setResourcePath("/tmp/"+pep.getId()+".date");
			pa.setResourceType("file");
			List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
			outputs.add(pa);
			pep.setOutputs(outputs);
			pep.setInputs(outputs);
			RemoteOutputStream ros = new SimpleRemoteOutputStream(cc.getOutputConsole());
			
			RemoteProcessMonitor processMonitor = new RecordStreamProcessMonitor(latch, ros,resultQueue);
			// start the process
			@SuppressWarnings("unused")
			NemaProcess process=service.executeProcess(pep, processMonitor);
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

	public void dispose(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		System.out.println("Component disposed: " + this.getClass().getName());
		//latch.countDown();
	}

}
