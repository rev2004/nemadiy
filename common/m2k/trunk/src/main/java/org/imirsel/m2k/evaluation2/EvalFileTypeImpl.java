package org.imirsel.m2k.evaluation2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public abstract class EvalFileTypeImpl implements EvalFileType {

	protected Logger _logger;
	private static String WINDOWS_PATH_REGEX = "[A-Z]:\\\\";
    
	public EvalFileTypeImpl(Logger logger) {
		setLogger(logger);
	}
	
	public Logger getLogger() {
		return _logger;
	}

	public void setLogger(Logger logger) {
		_logger = logger;
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
    
    public List<List<DataObj>> readDirectory(File theDir, String extension)
			throws IllegalArgumentException, FileNotFoundException, IOException{
		
    	List<List<DataObj>> out = new ArrayList<List<DataObj>>();
		
		System.out.println("\tretrieving files for " + theDir.getAbsolutePath());
		File[] files = theDir.listFiles();
		ArrayList<File> filesToUse = new ArrayList<File>();
		
		System.out.println("\t\tgot " + files.length + " files");
		
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
		return out;
	}
    
    public abstract List<DataObj> readFile(File theFile)
		throws IllegalArgumentException, FileNotFoundException, IOException;
	
	public abstract void writeFile(File theFile, List<DataObj> data)
		throws IllegalArgumentException, FileNotFoundException, IOException;

}
