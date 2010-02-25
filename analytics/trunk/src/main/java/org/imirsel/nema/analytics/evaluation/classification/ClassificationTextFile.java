/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.imirsel.nema.model.*;
import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.analytics.util.*;


/**
 *
 * @author kris.west@gmail.com
 */
public class ClassificationTextFile extends EvalFileTypeImpl {

	String type;
	public static final String DELIMITER = "\t";
	
	public ClassificationTextFile(String type) {
		super();
		this.type = type;
	}
	
	@Override
	public List<NemaData> readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		
		HashMap<String,String> data = readClassificationFile(theFile, true);
		
		List<NemaData> examples = new ArrayList<NemaData>(data.size());
        NemaData obj;
        String trackID;
        for (Iterator<String> it = data.keySet().iterator();it.hasNext();) {
            trackID = it.next();
            obj = new NemaData(trackID);
            obj.setMetadata(type, data.get(trackID));
            examples.add(obj);
        }
        
        _logger.info(examples.size() + " examples with " + type + " metadata read from file: " + theFile.getAbsolutePath());
        
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
				writer.write(obj.getStringMetadata(NemaDataConstants.PROP_ID) + DELIMITER + obj.getStringMetadata(type));
				writer.newLine();
			}
			
			_logger.info(data.size() + " examples with " + type + " metadata written to file: " + theFile.getAbsolutePath());
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					_logger.log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	//-----------
	/**
	 * Reads a classification file and encodes the data in a map.
	 * 
	 * @param toRead The file to read.
	 * @param MIREXMode A flag that determines whether file paths are converted to just their 
	 * names, minus the extension.
	 * @return A map linking file path/track name component to its classification.
	 * @throws IllegalArgumentException Thrown if the file is not in the expected format.
	 * @throws FileNotFoundException Thrown if the specified file is not found.
	 * @throws IOException Thrown if there is a problem reading the file unrelated to the format.
	 */
    public static HashMap<String,String> readClassificationFile(File toRead, boolean MIREXMode) throws IllegalArgumentException, FileNotFoundException, IOException{
        HashMap<String,String> dataRead = new HashMap<String,String>();
        
        BufferedReader br = new BufferedReader(new FileReader(toRead));

        String str = br.readLine(); // one line one sample
        if(MIREXMode){
            while (str != null){
                str = str.trim();
                if (!str.equals("")) {
                    String[] splitted = str.split("\\s+");
                    File aPath = new File(splitted[0]);
                    String key = PathAndTagCleaner.convertFileToMIREX_ID(aPath);
                    if(key.equals("")){
                        throw new IllegalArgumentException("Error: an empty track name was read from file: " + toRead.getAbsolutePath());
                    }
                    String className = PathAndTagCleaner.cleanTag(splitted[1]);
                    if(className.equals("")){
                        throw new IllegalArgumentException("Error: an empty class name was read from file: " + toRead.getAbsolutePath());
                    }
                    dataRead.put(key, className);
                }
                str = br.readLine();
            }
        }else{
            while (str != null){
                str = str.trim();
                if (!str.equals("")) {
                    String[] splitted = new String[2];
                    splitted = str.split("\t");
                    dataRead.put(splitted[0].trim(), splitted[1].trim());
                }
                str = br.readLine();
            }
        }

        br.close();
     
        return dataRead;
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
    public List<String> readClassificationFileAsList(File toRead, boolean MIREXMode) throws IllegalArgumentException, FileNotFoundException, IOException{
        List<String> dataRead = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader(toRead));

        String str = br.readLine(); // one line one sample
        if(MIREXMode){
            while (str != null){
                str = str.trim();
                if (!str.equals("")) {
                    String[] splitted = str.split("\\s+");
                    File aPath = new File(splitted[0]);
                    String key = convertFileToMIREX_ID(aPath);
                    if(key.equals("")){
                        throw new IllegalArgumentException("Error: an empty track name was read from file: " + toRead.getAbsolutePath());
                    }

                    dataRead.add(key);
                }
                str = br.readLine();
            }
        }else{
            while (str != null){
                str = str.trim();
                if (!str.equals("")) {
                    String[] splitted = new String[2];
                    splitted = str.split("\t");
                    dataRead.add(splitted[0].trim());
                }
                str = br.readLine();
            }
        }

        br.close();

        _logger.info("Read " + dataRead.size() + " paths from " + toRead.getAbsolutePath());
        return dataRead;
    }


    
    
}
