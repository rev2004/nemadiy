package org.imirsel.nema.components.process;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @author kris.west@gmail.com
 * @since 0.3.0
 */
@Component(creator = "Kris West", description = "Receives a datastructure that " +
		"defines a list of expected output file locations and then receives " +
		"a stream of ProcessArtifacts that represent entries in that datastructure " +
		"(files) being fulfilled by a process execution and retrieval of the " +
		"result to the desired local path. When all of the files have been " +
		"received, they are collected into a map of NemaTrackList to List<File> " +
		"and output.",
		name = "OmenOutputCollector", tags = "process output collector", 
		firingPolicy = Component.FiringPolicy.any)
public class OmenOutputCollector extends NemaComponent {

	@ComponentInput(description = "Map of NemaTrackList to List of NemaFile Objects defining expected output files that will be received as ProcessArtifacts.", name = "expectedOutput")
	private static final String DATA_IN_EXPECTED_OUTPUTS ="expectedOutput";

	@ComponentInput(description = "Process artifacts representing files that have been produced.", name = "processArtifacts")
	private static final String DATA_IN_PROCESS_ARTIFACTS ="processArtifacts";

	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	private static final String DATA_IN_NEMATASK ="NemaTask";

	@ComponentOutput(description = "Map of NemaTrackList to List of File Objects defining giving the paths to the generated artifacts.",name = "PredictedData")
	private static final String DATA_OUTPUT="PredictedData";
	
	NemaTask task = null;
	Map<NemaTrackList,List<File>> expectedPaths = null;
	Set<File> toReceive = null;
	List<List<ProcessArtifact>> inputBuffer = null;
	
	@Override
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		inputBuffer = new LinkedList<List<ProcessArtifact>>();
	}

	@Override
	public void dispose(ComponentContextProperties ccp)
	throws ComponentContextException {
		super.dispose(ccp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		cc.getOutputConsole().println("In output reader -execute");
		if(task == null && cc.isInputAvailable(DATA_IN_NEMATASK)){
			task = (NemaTask)cc.getDataComponentFromInput(DATA_IN_NEMATASK);
			cc.getOutputConsole().println("In output reader -got task");
				
		}
		if(expectedPaths == null && cc.isInputAvailable(DATA_IN_EXPECTED_OUTPUTS)){
			expectedPaths = (Map<NemaTrackList,List<File>>)cc.getDataComponentFromInput(DATA_IN_EXPECTED_OUTPUTS);
			toReceive = new HashSet<File>();
			cc.getOutputConsole().println("In output reader -got  expected output " + toReceive.size());
			for (Iterator<List<File>> it = expectedPaths.values().iterator(); it.hasNext();) {
				toReceive.addAll(it.next());
			}
			cc.getOutputConsole().println("The output expected size: " + toReceive.size());
		}
		
		//buffer inputs
		if(cc.isInputAvailable(DATA_IN_PROCESS_ARTIFACTS)){
			List<ProcessArtifact> in = (List<ProcessArtifact>)cc.getDataComponentFromInput(DATA_IN_PROCESS_ARTIFACTS);
			inputBuffer.add(in);
		}
		
		//when we are configured start receiving process artifacts
		if(task != null && expectedPaths != null){
			while(!inputBuffer.isEmpty()){
				List<ProcessArtifact> in = inputBuffer.remove(0);
				cc.getOutputConsole().println("Receiving a process artifact");
				cc.getOutputConsole().println("The process artifact: " + in.size() + "  " + in.get(0).getResourcePath());
				for(Iterator<ProcessArtifact> it = in.iterator();it.hasNext();){
					File path = new File(it.next().getResourcePath());
					
					if(toReceive.remove(path)){
						cc.getOutputConsole().println("Received file: " + path.getAbsolutePath() + ", waiting on " + toReceive.size() + " files.");
					}else{
						String msg = "Received unexpected file: " + path.getAbsolutePath() + ", waiting on " + toReceive.size() + " files. Paths expected but not yet received: ";
						for (Iterator<File> iterator = toReceive.iterator(); iterator.hasNext();) {
							msg += "\t" + iterator.next().getAbsolutePath() + "\n";
						}
						cc.getOutputConsole().println(msg);
					}
					if (toReceive.isEmpty()){
						//we have all the files expected, output
						cc.pushDataComponentToOutput(DATA_OUTPUT, expectedPaths);
						
						//reset
						task = null;
						expectedPaths = null;
						toReceive = null;
					}
				}
			}
		}
	}

}
