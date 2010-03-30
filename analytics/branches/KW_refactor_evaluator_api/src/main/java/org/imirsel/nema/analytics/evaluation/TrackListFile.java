/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.imirsel.nema.model.*;
import org.imirsel.nema.analytics.util.*;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;

/**
 * Reads and writes list files giving multiple file paths per file.
 * 
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class TrackListFile extends MultipleTrackEvalFileTypeImpl {

	public static final String READ_DELIMITER = "\\s+";
	public static final String TYPE_NAME = "Track list text file";
	
	public TrackListFile() {
		super(TYPE_NAME);
	}
	
	@Override
	public List<NemaData> readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		
		List<String> data = readClassificationFileAsList(theFile);
		
		List<NemaData> examples = new ArrayList<NemaData>(data.size());
        NemaData obj;
        String trackID;
        for (Iterator<String> it = data.iterator();it.hasNext();) {
            trackID = it.next();
            obj = new NemaData(trackID);
            examples.add(obj);
        }
        
        getLogger().info(examples.size() + " examples read from list file: " + theFile.getAbsolutePath());
        
        return examples;
	}
	
	@Override
	public void writeFile(File theFile, List<NemaData> data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(theFile));
			NemaData obj;
			for (Iterator<NemaData> it = data.iterator(); it.hasNext();) {
				obj = it.next();
				writer.write(obj.getId());
				writer.newLine();
			}
			
			getLogger().info(data.size() + " examples written to list file: " + theFile.getAbsolutePath());
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					getLogger().log(Level.SEVERE, null, ex);
				}
			}
		}
	}

    /**
     * Reads the file list data from a classification file. The class data is ignored.
     * 
     * @param toRead The file to read.
     * @param MIREXMode A flag that determines whether file paths are converted to just their 
	 * names, minus the extension.
	 * @return A list of the file path/track name components
	 * @throws IllegalArgumentException Thrown if the file is not in the expected format.
	 * @throws FileNotFoundException Thrown if the specified file is not found.
	 * @throws IOException Thrown if there is a problem reading the file unrelated to the format.
     */
    public List<String> readClassificationFileAsList(File toRead) throws IllegalArgumentException, FileNotFoundException, IOException{
    	List<String> dataRead = new ArrayList<String>();

        String[][] classificationStringsData = DeliminatedTextFileUtilities.loadDelimTextData(toRead, READ_DELIMITER, -1);

		int nrows = classificationStringsData.length;
        
        for(int r = 0; r < nrows; r++) {
            File aPath = new File(classificationStringsData[r][0].trim());
            String key = PathAndTagCleaner.convertFileToMIREX_ID(aPath);
            if(key.equals("")){
                throw new IllegalArgumentException("Error: an empty track name was read from file: " + toRead.getAbsolutePath());
            }
            dataRead.add(key);
        }

        getLogger().info("Read " + dataRead.size() + " paths from " + toRead.getAbsolutePath());
        return dataRead;
    }


    
    
}
