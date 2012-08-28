package org.imirsel.nema.model.fileTypes;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.model.*;


/**
 * Reads and writes Query By Singing/Humming and Query by Tapping 
 * results files.
 * 
 * @author cwillis
 */
public class QBSHTResultsTextFile extends MultipleTrackEvalFileTypeImpl {

    public static final String READ_DELIMITER = "\t";
	public static final String TYPE_NAME = "Query by singing/humming/tapping text file";
	
	public QBSHTResultsTextFile() {
		super(TYPE_NAME);
	}
	
	@Override
	public List<NemaData> readFile(File file)
			throws IllegalArgumentException, FileNotFoundException, IOException 
	{
		
		List<NemaData> data = new ArrayList<NemaData>();
		
		if (file.canRead())
		{    
			BufferedReader br = new BufferedReader(new FileReader(file));
            
			String line;
			while ((line =br.readLine()) != null){
				String[] fields = line.split(": ");
				String pathStr = fields[0].trim();
				String resultStr = fields[1].trim();
				resultStr = resultStr.replaceAll("t01", "");
				String[] results = resultStr.split(" ");
				
				//System.out.println(pathStr + "," + resultStr);
				
				String trackID = getQBSHTrackID(pathStr);
				NemaData obj = new NemaData(trackID);
				obj.setMetadata(NemaDataConstants.QBSHT_DATA, results);
				
				data.add(obj);
			}
			br.close();
           
        } else {
        	throw new IOException("The file: " + file.getPath() + " is not readable.");
        }

        getLogger().info(data.size() + " results with " + NemaDataConstants.QBSHT_DATA + " metadata read from file: " + file.getAbsolutePath());
        
        return data;
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
	
	@Override
	public void writeFile(File theFile, List<NemaData> data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		//TODO:  Required for ingest process
	}
}
