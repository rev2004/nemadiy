package org.imirsel.nema.model.fileTypes;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.model.*;
import org.imirsel.nema.model.util.DeliminatedTextFileUtilities;
import org.imirsel.nema.model.util.PathAndTagCleaner;


/**
 * Reads and writes Query By Singing/Humming and Query by Tapping 
 * ground truth text files.
 * 
 * The ground truth text files are simply two-column, tab-delimited 
 * text files. The first column is the query, the second is the 
 * relevant document.
 * 
 * @author cwillis
 */
public class SimpleKeyValueTextFile extends MultipleTrackEvalFileTypeImpl {

    public static final String READ_DELIMITER = "\t";
	public static final String WRITE_DELIMITER = "\t";	
	public static final String TYPE_NAME = "Query by singing/humming/tapping ground truth file";
	
	public SimpleKeyValueTextFile() {
		super(TYPE_NAME);
	}
	
	@Override
	public List<NemaData> readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
        
        String[][] srcData = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);
        
		List<NemaData> gtData = new ArrayList<NemaData>(srcData.length);
		
		for (int row = 0; row < srcData.length; row++)
		{
			String pathStr = srcData[row][0].trim();
			String trackID = getQBSHTrackID(pathStr);
			String answer = srcData[row][1].trim();
			NemaData obj = new NemaData(trackID);
			obj.setMetadata(NemaDataConstants.QBSHT_DATA, answer);
			gtData.add(obj);
        }

        getLogger().info(gtData.size() + " records with " + NemaDataConstants.QBSHT_DATA + " metadata read from file: " + theFile.getAbsolutePath());
        
        return gtData;
	}
	
	private String getQBSHTrackID(String path)
	{
		String subpath = "";
		if (path.indexOf("year") > 0) 
			subpath = path.substring(path.indexOf("year"), path.length());
		else if (path.indexOf("\\") != -1)
			subpath = path.substring(path.lastIndexOf("\\")+1, path.length());
		else if (path.indexOf("/") != -1)
			subpath = path.substring(path.lastIndexOf("/")+1, path.length());
		else 
			subpath = path;
		subpath = subpath.replace("\\", "_");
		subpath = subpath.replace("/", "_");
		subpath = subpath.replace(".wav", "");
		return subpath;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void writeFile(File theFile, List<NemaData> data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		//TODO:  Required for ingest process
	}
}
