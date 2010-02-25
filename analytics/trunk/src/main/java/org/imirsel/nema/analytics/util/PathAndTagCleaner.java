package org.imirsel.nema.analytics.util;

import java.io.File;

/**
 * 
 * @author kris.west@gmail.com
 *
 */
public class PathAndTagCleaner {

	/**
	 * 
	 */
	private static String WINDOWS_PATH_REGEX = "[A-Z]:\\\\";
    
	/**
	 * 
	 * @param aFile
	 * @return
	 */
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
    
    /**
     * 
     * @param tag
     * @return
     */
    public static String cleanTag(String tag){
        return tag.toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-z0-9]", "");
    }

}
