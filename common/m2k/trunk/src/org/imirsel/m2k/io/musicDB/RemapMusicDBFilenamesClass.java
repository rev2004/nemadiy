package org.imirsel.m2k.io.musicDB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.util.retrieval.DistanceMatrix;
import org.imirsel.m2k.util.retrieval.MusicDB;

/**
 * Remaps filenames within a MusicDB Object to files found within a particular 
 * folder by matching the name components. Support for matching directory tree 
 * structures (partial path matching) will be added later.
 * @author Kris West (kw@cmp.uea.ac.uk)
 */

public class RemapMusicDBFilenamesClass { 
    
    /**
     * Remaps filenames within a MusicDB Object to files found within a particular 
     * folder by matching the name components. Support for matching directory tree 
     * structures (partial path matching) will be added later.
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static MusicDB remap(MusicDB theDB, String newFolderPath, boolean removeHTTPSpaces, boolean removeUnmappedFiles) throws IOException{
        
        //get available files 
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List availFileList = new ArrayList();
        List dirs = new ArrayList();
        while (availFiles != null)
        {
            for (int i = 0; i < availFiles.length; i++) {
                if (availFiles[i].isDirectory()){
                    dirs.add(availFiles[i]);
                }else{
                    availFileList.add(availFiles[i]);
                }
            }
            availFiles = null;
            //get another dir and process, ignoring empty dirs
            while ((availFiles == null)&&(dirs.size() > 0))
            {
                File[] tmp = ((File)dirs.remove(0)).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }
        
        HashMap<String,File> newFilesMap = new HashMap<String,File>(availFileList.size());
        //ArrayList locations = new ArrayList();
        
        for (int i = 0; i < availFileList.size(); i++) {
            File theFile = (File)availFileList.get(i);
            String key = theFile.getName().toLowerCase();
            if (newFilesMap.containsKey(i)){
                System.out.println("WARNING: Duplicate filename (" + key + ") detected");
            }
            newFilesMap.put(key,theFile);
        }
        
        System.out.println("Remapping files from MusicDB to files in " + newFolderPath);
        
        //iterate through all file locations
        ArrayList files = (ArrayList)theDB.getFileNames();
        int done = 0;
        for (int i = 0; i < files.size(); i++) {
            //Check whether file in dir exists with same name 
            
            File oldFile = new File((String)files.get(i));
            String filename = oldFile.getName();
            if (removeHTTPSpaces){
                filename = filename.replaceAll("%20"," ").toLowerCase();
            }
            //System.out.println("File: " + filename);
            
            if(newFilesMap.containsKey(filename))
            {
                File newLocation = newFilesMap.remove(filename);
                try {
                    //Change file location pointer and Signal Object
                    theDB.remapFileLocation((String)files.get(i),newLocation.getCanonicalPath());
                    done++;
                } catch (noMetadataException ex) {
                    throw new RuntimeException("File not mapped due to noMetadataexception. This is a bug. Contact kw@cmp.uea.ac.uk",ex);
                } 
            }else if(removeUnmappedFiles)
            {
                while (theDB.containsFile((String)files.get(i)))
                {
                    System.out.println("\tNo local copy of " + (String)files.get(i) + " found, it is being removed from the database.");
                    theDB.removeFileFromDatabase((String)files.get(i));
                }
            }
            if (i%2000 == 0){
                System.out.println("done " + i + " of " + files.size());
            }
        }
        
        System.out.println("RemapMusicDBFilenames: Remapped " + done + " of " + files.size() + " files.");
        System.out.println("New DB size: " + theDB.size());
        return theDB;
    }
    
    /**
     * Remaps filenames within a MusicDB Object to files found within a particular 
     * folder by matching the name components. Support for matching directory tree 
     * structures (partial path matching) will be added later.
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static DistanceMatrix remapDistanceMatrix(DistanceMatrix theDB, String newFolderPath, boolean removeHTTPSpaces) throws IOException{
        
        //get available files 
        /*File newDir = new File(newFolderPath);
        File[] newFiles = newDir.listFiles();
        ArrayList newFilesList = new ArrayList();
        ArrayList locations = new ArrayList();
        for (int i = 0; i < newFiles.length; i++) {
            newFilesList.add(newFiles[i].getName().toLowerCase());
            locations.add(new Integer(i));
        }*/
        
        //get available files 
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List availFileList = new ArrayList();
        List dirs = new ArrayList();
        while (availFiles != null)
        {
            for (int i = 0; i < availFiles.length; i++) {
                if (availFiles[i].isDirectory()){
                    dirs.add(availFiles[i]);
                }else{
                    availFileList.add(availFiles[i]);
                }
            }
            availFiles = null;
            //get another dir and process, ignoring empty dirs
            while ((availFiles == null)&&(dirs.size() > 0))
            {
                File[] tmp = ((File)dirs.remove(0)).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }
        
        ArrayList newFilesList = new ArrayList();
        ArrayList locations = new ArrayList();
        
        for (int i = 0; i < availFileList.size(); i++) {
            newFilesList.add(((File)availFileList.get(i)).getName().toLowerCase());
            locations.add(new Integer(i));
        }
        
        //iterate through all file locations
        List files = Arrays.asList(theDB.getFiles());
        int done = 0;
        for (int i = 0; i < files.size(); i++) {
            //Check whether file in dir exists with same name (and recursion through sub-dirs later)
            File oldFile = (File)files.get(i);
            String filename = oldFile.getName();
            if (removeHTTPSpaces){
                filename = filename.replaceAll("%20"," ").toLowerCase();
            }
            //System.out.println("File: " + filename);
            int idx = newFilesList.indexOf(filename);
            
            if(idx > -1)
            {
                int location = ((Integer)locations.remove(idx)).intValue();
                newFilesList.remove(idx);
                //Change file location pointer and Signal Object
                theDB.remapFileLocation(((File)files.get(i)).getPath(),((File)availFileList.get(location)).getCanonicalPath());
                done++;
            }else{
                System.out.println("No file was found to remap:" + oldFile.getPath() + " to!\nIt has been removed!");
                theDB.removeFile((File)files.get(i));
            }
        }
        
        System.out.println("remapDistanceMatrix: Remapped " + done + " of " + files.size() + " files.");
        System.out.println("New DistanceMatrix size: " + theDB.getFiles().length);
        return theDB;
    }
}
