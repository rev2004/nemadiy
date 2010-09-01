package org.imirsel.nema.test;

import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Level;

import org.imirsel.nema.components.NemaComponent;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;

@Component(creator = "Mert Bay", description = "Test console output", name = "TestConsoleOut", tags = "test, console, output", resources = "", firingPolicy = Component.FiringPolicy.all)
public class TestConsoleOut extends NemaComponent {
	
	@ComponentInput(description = "the message", name = "message")
	final static String DATA_INPUT_1  ="message";

	@ComponentProperty(defaultValue = "10", description = "Number of times to display the text", name = "Number of Times to Display Text")
	final static String DATA_PROPERTY_2 = "Number of Times to Display Text";

	@ComponentProperty(defaultValue = "100", description = "wait time between message in miliseconds", name = "Wait Time in miliseconds Between Text Displays")
	final static String DATA_PROPERTY_3 = "Wait Time in miliseconds Between Text Displays";

	@ComponentOutput(description = "output message at the end of the component push", name = "output message")
	final static String DATA_OUTPUT_1 = "output message";

	public void initialize(ComponentContextProperties ccp)
			throws ComponentContextException, ComponentExecutionException {
		super.initialize(ccp);
	}

	public void dispose(ComponentContextProperties ccp)
			throws ComponentContextException {
		super.dispose(ccp);
	}

	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {
		addLogDestination(ccp.getOutputConsole());
		this.getLogger().setLevel(Level.ALL);
		String text = (String) ccp.getDataComponentFromInput(DATA_INPUT_1);
		int number = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_2));
		int wtime = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_3));
		PrintStream ps = ccp.getOutputConsole();
		getLogger().info("Here is the text output: ");
		for (int i = 0; i < number; i++) {
			// print on the console
			ps.print("%%%%" + new Date(System.currentTimeMillis())
					+ " printing to console: " + text + "\n");
			ps.flush();
			try {
				Thread.currentThread().sleep(wtime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ccp
				.pushDataComponentToOutput(DATA_OUTPUT_1, "DONE: "
						+ this.hashCode());
		getLogger().info("--exit--");
	}

}
