package org.imirsel.nema.components.process;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.jcr.SimpleCredentials;


import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceMatches;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;


import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.DynamicType;
import org.imirsel.nema.model.OsType;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.model.ResourceGroupEntry;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @since 0.3.0
 */
@Component(creator = "Amit Kumar", description = "This component takes the profile name and returns the process template.",
			name = "ProfileToProcessTemplateComponent", tags = "profile process execution")
public class ProfileToProcessTemplateComponent extends NemaComponent {

	@ComponentProperty(defaultValue = "nema.lis.uiuc.edu", description = "Host that discovers compatiable execution servers that support the profile.", name = "_lookupHost")
	private static final String PROPERTY_1 = "_lookupHost";

	@ComponentProperty(defaultValue = "exampleRun", description = "Unique identifier for the executable", name = "profileName")
	private static final String PROPERTY_2 ="profileName";
	
	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "true", description = "indicates to the UI that this is a remote component", name = "_remoteDynamicComponent")
	private static final String PROPERTY_3 ="_remoteDynamicComponent";
	

	@StringDataType(valueList={"Unix Like","Windows Like"}, labelList={"Unix","Windows"})
	@ComponentProperty(defaultValue = "Unix Like", description = "operating system", name = "_os")
	private static final String PROPERTY_4 ="_os";
	
	
	@StringDataType(valueList={"imirsel","mcgill"}, labelList={"imirsel","mcgill"})
	@ComponentProperty(defaultValue = "imirsel", description = "execution group", name = "_group")
	private static final String PROPERTY_5 ="_group";
	

	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "test:test", description = "", name = "_credentials")
	private static final String PROPERTY_6 ="_credentials";

	


	@ComponentOutput(description = "Process Template", name = "processTemplate")
	private static final String DATA_OUT_1 ="processTemplate";

	@ComponentOutput(description = "Preferred OS", name = "preferredOS")
	private static final String DATA_OUT_2 ="preferredOS";
	
	
	private String profileName;
	private String preferredOS;
	private String host;
	private String group;
	private SimpleCredentials credentials=null;
	


	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
	}


	@Override
	public void execute(ComponentContext componentContext)
	throws ComponentExecutionException, ComponentContextException {
		host = componentContext.getProperty(PROPERTY_1);
		profileName = componentContext.getProperty(PROPERTY_2);
		preferredOS = componentContext.getProperty(PROPERTY_4);
		group = componentContext.getProperty(PROPERTY_5);
		String _credentials = componentContext.getProperty(PROPERTY_6);
		this.setCredentials(parseCredentials(_credentials));
		ProcessExecutorService pes=null;
		try {
			pes = findExecutorService();
		} catch (RemoteException e1) {
			throw new  ComponentExecutionException("Error: could not find profileName: " + profileName+"  " +e1.getMessage());
		}
		
		if(pes==null){
			throw new  ComponentExecutionException("Error: could not find process executor: " + profileName);
		}
		
		ProcessTemplate processTemplate = null;
		try {
			processTemplate = pes.getDynamicProcessTemplate(getCredentials(), profileName);
		} catch (RemoteException e) {
			throw new  ComponentExecutionException("Error: could not find processTemplate: " + profileName + "  " + e.getMessage());
		}
		
		componentContext.getOutputConsole().println("process template: "+profileName);
		componentContext.pushDataComponentToOutput(DATA_OUT_1, processTemplate);
		componentContext.pushDataComponentToOutput(DATA_OUT_2, preferredOS);
		
	}
	
	private ProcessExecutorService findExecutorService() throws RemoteException{
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
		
		DynamicType dynamicType = new DynamicType();
		OsType osType = new OsType(this.preferredOS);
		ResourceGroupEntry rgt = new ResourceGroupEntry(this.group);
		
		ProcessExecutorService executorService=null;
		
		Entry[] attrList = new Entry[]{osType,rgt,dynamicType};
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
					getLogger().info("NO PROCESS EXECUTOR SERVICE FOUND: "  + this.profileName);
				}
				
				if(serviceFound==null){
					throw new RemoteException("Suitable Service not found " + this.profileName);
				}
				executorService = serviceFound;
				this.getLogger().info("Selecting: " + executorService.toString() + " for the execution. ");
				
			
		
		return executorService;
		
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

	private void setCredentials(SimpleCredentials credentials) {
		this.credentials = credentials;
	}

	public SimpleCredentials getCredentials() {
		return credentials;
	}


}
