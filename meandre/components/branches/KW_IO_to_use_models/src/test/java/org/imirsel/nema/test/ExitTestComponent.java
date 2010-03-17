package org.imirsel.nema.test;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
@Component(creator = "Amit Kumar", description = "", name = "ExitTestComponent", tags = "")
public class ExitTestComponent implements ExecutableComponent{
	
	@ComponentInput(description = "string to display", name = "input")
	public String DATA_INPUT_1 = "input";

	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub
		
	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String s=(String)cc.getDataComponentFromInput(DATA_INPUT_1);
		System.out.println("Going to call exit... " + s);
		System.exit(0);
		System.out.println("After calling exit...");
		
	}

	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub
		
	}

}
