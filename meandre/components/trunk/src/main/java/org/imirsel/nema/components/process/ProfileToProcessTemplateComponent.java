package org.imirsel.nema.components.process;

import java.io.IOException;
import java.rmi.RemoteException;


import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.entry.Name;

import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "This component takes the profile name and returns the process template.",
			name = "ProfileToProcessTemplateComponent", tags = "profile process execution")
public class ProfileToProcessTemplateComponent extends NemaComponent {

	@ComponentProperty(defaultValue = "nema.lis.uiuc.edu", description = "Host that discovers compatiable execution servers that support the profile.", name = "_host")
	private static final String PROPERTY_1 = "_host";

	@ComponentProperty(defaultValue = "exampleRun", description = "Unique identifier for the executable", name = "profileName")
	private static final String PROPERTY_2 ="profileName";
	
	@ComponentOutput(description = "Process Template", name = "processTemplate")
	private static final String DATA_OUT_1 ="processTemplate";


	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
	}


	@Override
	public void execute(ComponentContext componentContext)
	throws ComponentExecutionException, ComponentContextException {
		String host = componentContext.getProperty(PROPERTY_1);
		String profileName = componentContext.getProperty(PROPERTY_2);
		LookupLocator locator=null;
		try {
			locator = new LookupLocator("jini://"+host);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ComponentExecutionException(e.getMessage());
		}
		ServiceRegistrar registrar=null;
		try {
			registrar= locator.getRegistrar();
		} catch (IOException e1) {
			throw new ComponentExecutionException(e1.getMessage());
		} catch (ClassNotFoundException e1) {
			throw new ComponentExecutionException(e1.getMessage());
		}
		Name name = new Name(profileName);
		getLogger().info("Profile Name is: " + profileName);

		Entry[] attrList = new Entry[]{name};
		Class<ProcessExecutorService>[] classes = new Class[1]; 
		classes[0] = ProcessExecutorService.class;
		ServiceTemplate template = new ServiceTemplate(null,classes,attrList);
		ProcessExecutorService executorService=null;
		ProcessTemplate processTemplate=null;
		try {
			executorService = (ProcessExecutorService) registrar.lookup(template);
			if(executorService == null){
				getLogger().severe("Error -could not find executor service that supports " + profileName);
				throw new ComponentExecutionException("Error -could not find the executor service that supports the profile: " + profileName);
			}else{
				getLogger().info("ExecutorService found");
			}
			processTemplate=executorService.getProcessTemplate(profileName);
			if(processTemplate==null){
				throw new ComponentExecutionException("Error -could not find the "+ profileName+ " process template.");
			}
		}catch (RemoteException e) {
			throw new ComponentExecutionException(e.getMessage());
		}
		componentContext.getOutputConsole().println("process template: "+processTemplate.getName());
		componentContext.pushDataComponentToOutput(DATA_OUT_1, processTemplate);
	}

}
