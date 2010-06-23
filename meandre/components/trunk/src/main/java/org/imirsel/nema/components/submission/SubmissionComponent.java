package org.imirsel.nema.components.submission;

import org.imirsel.nema.components.NemaComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "This component allows the user to select the submission code from the dropdown and send it to the flow. <br/> " +
		"The submission code is used to generate the result structure.", name = "SubmissionComponent", tags = "mirex submission")
public class SubmissionComponent extends NemaComponent {

	
	@ComponentProperty(defaultValue = "TEST", description = "MIREX submission code", name = "_submissionCode")
	private static final String DATA_PROPERTY_1 ="_submissionCode"; 

	@ComponentProperty(defaultValue = "Test Name", description = "MIREX Submission name", name = "_submissionName")
	private static final String DATA_PROPERTY_2 ="_submissionName"; 
	
	
	@ComponentOutput(description = "The MIREX submission code", name = "submissionCode")
	private static final String DATA_OUT_1="submissionCode";
	
	
	@ComponentOutput(description = "The MIREX submission name", name = "submissionName")
	private static final String DATA_OUT_2="submissionName";
	
	
	@Override
	public void execute(ComponentContext componentContext)
			throws ComponentExecutionException, ComponentContextException {
		String submissionCode = (String) componentContext.getProperty(DATA_PROPERTY_1);
		String submissionName = (String) componentContext.getProperty(DATA_PROPERTY_2);
		componentContext.getOutputConsole().print("Submission Code:  " + submissionCode + " Submission Name: " + submissionName);
		componentContext.pushDataComponentToOutput(DATA_OUT_1, submissionCode);
		componentContext.pushDataComponentToOutput(DATA_OUT_2, submissionName);
	}

}
