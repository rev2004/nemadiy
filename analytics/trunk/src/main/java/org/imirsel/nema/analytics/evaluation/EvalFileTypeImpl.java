package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;


import org.imirsel.nema.model.NemaData;

/**
 * 
 * @author kris.west@gmail.com
 *
 */
public abstract class EvalFileTypeImpl implements EvalFileType {

	protected Logger _logger;
	private static String WINDOWS_PATH_REGEX = "[A-Z]:\\\\";
    
	public EvalFileTypeImpl() {
		_logger = Logger.getLogger(this.getClass().getName());
	}
	
	public Logger getLogger() {
		return _logger;
	}

	public void addLogHandler(Handler logHandler){
		getLogger().addHandler(logHandler);
	}
	
    //private static Pattern WINDOWS_PATH_MATCHER = Pattern.compile("[A-Z]:\\\\");
    public static String convertFileToMIREX_ID(File aFile){
        String name = aFile.getName();
        String key;
        //detect windows paths
        if (name.substring(0,3).matches(WINDOWS_PATH_REGEX)){
            key = name.substring(name.lastIndexOf("\\")+1,name.length()).toLowerCase();
        }else{
            key = name.toLowerCase();
        }
        if (key.endsWith(".mp3")
                || key.endsWith(".wav")
                || key.endsWith(".aac")
                || key.endsWith(".wma")
                || key.endsWith(".ogg")
                || key.endsWith(".aif")
                || key.endsWith(".mid")){
            return key.substring(0,key.length()-4);
        }
        return key;
    }
    
    public List<List<NemaData>> readDirectory(File theDir, String extension)
			throws IllegalArgumentException, FileNotFoundException, IOException{
		
    	List<List<NemaData>> out = new ArrayList<List<NemaData>>();
		
		File[] files = theDir.listFiles();
		ArrayList<File> filesToUse = new ArrayList<File>();
		
		_logger.info("got " + files.length + " files for " + theDir.getAbsolutePath());
		
		if(extension == null){
			for (int i = 0; i < files.length; i++){
		        File file = files[i];
		        if (!file.isDirectory()){
		            filesToUse.add(file);
		        }
		    }
		}else{
			for (int i = 0; i < files.length; i++){
		        File file = files[i];
		        if (!file.isDirectory() && file.getName().endsWith(extension)){
		            filesToUse.add(file);
		        }
		    }
		}
		//this should sort results consistently across all submissions,
		//   if they use the same names for their results files 
		//   (otherwise there is no way to know if they are about the same test across different submissions)
		Collections.sort(filesToUse);
		
		for(Iterator<File> it = filesToUse.iterator();it.hasNext();){
			out.add(readFile(it.next()));
		}
		
		_logger.info("Retrieved " + out.size() + " of " + files.length + " files from " + theDir.getAbsolutePath());
		
		
		return out;
	}
    
    public abstract List<NemaData> readFile(File theFile)
		throws IllegalArgumentException, FileNotFoundException, IOException;
	
	public abstract void writeFile(File theFile, List<NemaData> data)
		throws IllegalArgumentException, FileNotFoundException, IOException;

}
