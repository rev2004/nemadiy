package org.imirsel.nema.components.process;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
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
		"defines a list of file locations. The files are read and the resulting " +
		"models output.",
		name = "OutputReader", tags = "process output reader", 
		firingPolicy = Component.FiringPolicy.all)
public class OutputReader extends NemaComponent {

	@ComponentInput(description = "Map of NemaTrackList to List of NemaFile Objects defining expected output files that will be received as ProcessArtifacts.", name = "expectedOutput")
	private static final String DATA_IN_EXPECTED_OUTPUTS ="expectedOutput";

	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	private static final String DATA_IN_NEMATASK ="NemaTask";

	@ComponentInput(description = "Class representing the output file type that is to be read.", name = "FileType")
	private static final String DATA_IN_OUTPUT_TYPE ="FileType";

	@ComponentOutput(description = "Map of NemaTrackList to List of NemaData Objects defining each track list (encoding metadata generated by the remote process).",name = "PredictedData")
	private static final String DATA_OUTPUT="PredictedData";
	
	@Override
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
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
		
		NemaTask task = (NemaTask)cc.getDataComponentFromInput(DATA_IN_NEMATASK);
		Class<NemaFileType> fileType = (Class<NemaFileType>)cc.getDataComponentFromInput(DATA_IN_OUTPUT_TYPE);
		Map<NemaTrackList,List<File>> expectedPaths = (Map<NemaTrackList,List<File>>)cc.getDataComponentFromInput(DATA_IN_EXPECTED_OUTPUTS);
		
		Map<NemaTrackList, List<NemaData>> outputData = null;
		try {
			outputData = FileConversionUtil.readProcessOutput(expectedPaths, task, fileType);
		}catch(Exception e) {
		 throw new ComponentExecutionException(e);
		}
		
		if(outputData==null){
			throw new ComponentExecutionException("Process result is null");
		}else{
			cc.pushDataComponentToOutput(DATA_OUTPUT, outputData);
		}
	}

}
