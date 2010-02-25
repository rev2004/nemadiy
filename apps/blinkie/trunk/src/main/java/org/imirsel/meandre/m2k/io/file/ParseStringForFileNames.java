package org.imirsel.meandre.m2k.io.file;

import java.util.StringTokenizer;
import java.util.Vector;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;


@Component(creator = "Loretta Auvil", 
		description = "Parse a string for list of filenames", 
		name = "ParseStringForFileNames", 
		tags = "input string filename", 
		firingPolicy = Component.FiringPolicy.all)
		
public class ParseStringForFileNames implements ExecutableComponent {

	public final static String INPUT_0_StringValue = "stringValue";
	@ComponentInput(description = "String value of the selection", 
			name = INPUT_0_StringValue)

	public final static String OUTPUT_1_AudioFile = "audioFile";
	@ComponentOutput(description = "The audio file.", 
			name = OUTPUT_1_AudioFile)
			
	public final static String OUTPUT_2_ModelFiles = "modelFiles";
	@ComponentOutput(description = "The model files.", 
			name = OUTPUT_2_ModelFiles)
			
	/** store audio files and model */
    private String audioFile = null;
	private Vector modelFilesVector = new Vector();
			
	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub
		
	}
	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub
		
		String stringValue = (String) ccp.getDataComponentFromInput("stringValue");
		StringTokenizer st = new StringTokenizer((String)ccp.getDataComponentFromInput("stringValue"), ",");
        while(st.hasMoreTokens()) {
            String nt = st.nextToken();
            if (nt.endsWith(".serial"))
            	modelFilesVector.add(nt);
            else if (nt.endsWith(".wav") || nt.endsWith(".mp3"))
            	//assign audioFile to the first one encountered
            	if (audioFile == null)
            		audioFile = new String(nt);
        }
        ccp.pushDataComponentToOutput(OUTPUT_2_ModelFiles, modelFilesVector);
        ccp.pushDataComponentToOutput(OUTPUT_1_AudioFile, audioFile);
	}
	public void initialize(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub
		
	}
	
}
