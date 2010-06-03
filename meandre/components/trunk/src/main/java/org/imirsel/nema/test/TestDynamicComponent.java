package org.imirsel.nema.test;

import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.components.RemoteDynamicComponent;
import org.imirsel.nema.renderers.FileRenderer;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "Test component", name = "TestDynamicComponent", tags = "test test")
public class TestDynamicComponent extends RemoteDynamicComponent {

	@ComponentInput(description = "string in", name = "in")
	private static final String DATA_IN_1 ="in";
	
	@ComponentOutput(description = "string out", name = "out")
	private static final String DATA_OUT_1 ="out";
	
	@StringDataType(renderer=FileRenderer.class)
	@ComponentProperty(defaultValue="Feature_Values_1.xml",
			description="Feature Values Ouput File: the features",
			name="Feature Values Output File")
			final static String DATA_PROPERTY_FVFILE = "FeatureValuesOutputFile";
	private String fvFile = "Feature_Values_1.xml";
	
	
	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String in = (String) cc.getDataComponentFromInput(DATA_IN_1);
		System.out.println("--> credentials are: " + this.getContentRepositoryCredentials());
		System.out.println("--> Profile name is: "+ this.getProfileName());
		
		String fileName =cc.getProperty(fvFile);
		System.out.println("file name: " + fileName);
		cc.pushDataComponentToOutput(DATA_OUT_1, in);
	}

}
