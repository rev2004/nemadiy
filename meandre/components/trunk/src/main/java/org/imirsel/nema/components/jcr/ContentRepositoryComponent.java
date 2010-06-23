package org.imirsel.nema.components.jcr;

import javax.jcr.SimpleCredentials;

import org.imirsel.nema.components.NemaComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "The content repository where the flow artifacts are stored" +
		" and the credentials used to connect to this repository", name = "ContentRepositoryComponent", tags = "content repository")
public class ContentRepositoryComponent extends NemaComponent {
	
	@ComponentProperty(defaultValue = "test:test", description = "Credentials that must be supplied to connect to the content repository", name = "_credentials")
	private static final String PROPERTY_1 ="_credentials";
	@ComponentProperty(defaultValue = "rmi://", description = "Content Repository URI that is used to connect to the repository", name = "_contentRepositoryUri")
	private static final String PROPERTY_2  ="_contentRepositoryUri";
	
	@ComponentOutput(description = "Simple Credentials used to connect to the content repository", name = "credentials")
	private static final String DATA_OUT_1 ="credentials";
	@ComponentOutput(description = "Content repository URI", name = "contentRepositoryUri")
	private static final String DATA_OUT_2 ="contentRepositoryUri";
	

	@Override
	public void execute(ComponentContext componentContext)
			throws ComponentExecutionException, ComponentContextException {
		
		String credentialString = componentContext.getProperty(PROPERTY_1);
		String contentRepositoryUri = componentContext.getProperty(PROPERTY_2);
		
		
		String[] splits = credentialString.split(":");
		if(splits.length<2){
			throw new ComponentExecutionException("Error could not get credentials");
		}
		SimpleCredentials credentials = new SimpleCredentials(splits[0], splits[1].toCharArray());
		componentContext.pushDataComponentToOutput(DATA_OUT_1, credentials);
		componentContext.pushDataComponentToOutput(DATA_OUT_2,contentRepositoryUri);
	}

}
