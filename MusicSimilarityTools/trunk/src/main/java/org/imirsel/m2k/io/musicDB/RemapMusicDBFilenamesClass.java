package org.imirsel.m2k.io.musicDB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.util.retrieval.DenseDistanceMatrix;
import org.imirsel.m2k.util.retrieval.DistanceMatrixInterface;
import org.imirsel.m2k.util.retrieval.MusicDB;
import org.imirsel.m2k.util.retrieval.SparseDistanceMatrix;

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
     * @param theDB
     * @param newFolderPath
     * @param removeHTTPSpaces
     * @param removeUnmappedFiles
     * @return
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static MusicDB remap(MusicDB theDB, String newFolderPath, boolean removeHTTPSpaces, boolean removeUnmappedFiles) throws IOException{

        //get available files
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List<File> availFileList = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
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
                File[] tmp = dirs.remove(0).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }

        HashMap<String,File> newFilesMap = new HashMap<String,File>(availFileList.size());
        //ArrayList locations = new ArrayList();

        for (int i = 0; i < availFileList.size(); i++) {
            File theFile = availFileList.get(i);
            String key = theFile.getName().toLowerCase();
            if (newFilesMap.containsKey(key)){
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

    public static DistanceMatrixInterface remapDistanceMatrix(DistanceMatrixInterface theDistMat, String newFolderPath, boolean removeHTTPSpaces) throws IOException{
        if(theDistMat instanceof DenseDistanceMatrix){
            return remapDenseDistanceMatrix((DenseDistanceMatrix)theDistMat, newFolderPath, removeHTTPSpaces);
        }else{
            return remapSparseDistanceMatrix((SparseDistanceMatrix)theDistMat, newFolderPath, removeHTTPSpaces);
        }
    }


    /**
     * Remaps filenames within a DenseDistanceMatrix Object to files found within a particular
     * folder by matching the name components. Support for matching directory tree
     * structures (partial path matching) will be added later.
     * @param theDistMat
     * @param newFolderPath
     * @param removeHTTPSpaces
     * @return
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static DenseDistanceMatrix remapDenseDistanceMatrix(DenseDistanceMatrix theDistMat, String newFolderPath, boolean removeHTTPSpaces) throws IOException{

        //get available files
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List<File> availFileList = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
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
                File[] tmp = dirs.remove(0).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }

        HashMap<String,File> newFilesMap = new HashMap<String,File>(availFileList.size());


        for (int i = 0; i < availFileList.size(); i++) {
            File theFile = availFileList.get(i);
            String key = theFile.getName();
            if (newFilesMap.containsKey(key)){
                System.out.println("WARNING: Duplicate filename (" + key + ") detected");
            }
            newFilesMap.put(key,theFile);
        }

        //iterate through all file locations
        List files = Arrays.asList(theDistMat.getFiles());
        int done = 0;
        for (int i = 0; i < files.size(); i++) {
            //Check whether file in dir exists with same name (and recursion through sub-dirs later)
            File oldFile = (File)files.get(i);
            String filename = oldFile.getName();
            if (removeHTTPSpaces){
                filename = filename.replaceAll("%20"," ").toLowerCase();
            }
            //System.out.println("File: " + filename);
            File newFile = newFilesMap.get(filename);

            if(newFile != null)
            {
                //Change file location pointer and Signal Object
                theDistMat.remapFileLocation(oldFile.getPath(),newFile.getAbsolutePath());
                done++;
            }else{
                System.out.println("No file was found to remap:" + oldFile.getPath() + " to!\nIt has been removed!");
                theDistMat.removeFile((File)files.get(i));
            }
        }

        System.out.println("remapDistanceMatrix: Remapped " + done + " of " + files.size() + " files.");
        System.out.println("New DistanceMatrix size: " + theDistMat.getFiles().length);
        return theDistMat;
    }

    /**
     * Remaps filenames within a SparseDistanceMatrix Object to files found within a particular
     * folder by matching the name components. Support for matching directory tree
     * structures (partial path matching) will be added later.
     * @param theDistMat
     * @param newFolderPath
     * @param removeHTTPSpaces
     * @return
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static SparseDistanceMatrix remapSparseDistanceMatrix(SparseDistanceMatrix theDistMat, String newFolderPath, boolean removeHTTPSpaces) throws IOException{

        //get available files
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List<File> availFileList = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
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
                File[] tmp = dirs.remove(0).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }

        HashMap<String,File> newFilesMap = new HashMap<String,File>(availFileList.size());

        for (int i = 0; i < availFileList.size(); i++) {
            File theFile = availFileList.get(i);
            String key = theFile.getName();
            if (newFilesMap.containsKey(key)){
                System.out.println("WARNING: Duplicate filename (" + key + ") detected");
            }
            newFilesMap.put(key,theFile);
        }

        theDistMat.remapFileLocations(newFilesMap);

        System.out.println("New DistanceMatrix size: " + theDistMat.getFiles().length);
        return theDistMat;
    }



    //private static Pattern WINDOWS_PATH_MATCHER = Pattern.compile("[A-Z]:\\\\");
    private static String WINDOWS_PATH_REGEX = "[A-Z]:\\\\";
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
     * Remaps filenames within a MusicDB Object to files found within a particular
     * folder by matching the name minus any audio or midi extension. Support
     * for matching directory tree structures (partial path matching) will be added later.
     * @param theDB
     * @param newFolderPath
     * @param removeHTTPSpaces
     * @param removeUnmappedFiles
     * @return
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static MusicDB remapWithMIREXIDs(MusicDB theDB, String newFolderPath, boolean removeHTTPSpaces, boolean removeUnmappedFiles) throws IOException{

        //get available files
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List<File> availFileList = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
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
                File[] tmp = dirs.remove(0).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }

        HashMap<String,File> newFilesMap = new HashMap<String,File>(availFileList.size());
        //ArrayList locations = new ArrayList();

        for (int i = 0; i < availFileList.size(); i++) {
            File theFile = availFileList.get(i);
            String key = convertFileToMIREX_ID(theFile);
            if (newFilesMap.containsKey(key)){
                System.out.println("WARNING: Duplicate filename (" + key + ") detected");
            }
            newFilesMap.put(key,theFile);
        }

        System.out.println("Remapping files from MusicDB to files in " + newFolderPath);

        //iterate through all file locations
        List<String> files = theDB.getFileNames();
        int done = 0;
        for (int i = 0; i < files.size(); i++) {
            //Check whether file in dir exists with same name

            File oldFile = new File(files.get(i));
            String key = convertFileToMIREX_ID(oldFile);
            if (removeHTTPSpaces){
                key = key.replaceAll("%20"," ");
            }
            //System.out.println("File: " + filename);

            if(newFilesMap.containsKey(key))
            {
                File newLocation = newFilesMap.remove(key);
                try {
                    //Change file location pointer and Signal Object
                    theDB.remapFileLocation(files.get(i),newLocation.getAbsolutePath());
                    done++;
                } catch (noMetadataException ex) {
                    throw new RuntimeException("File not mapped due to noMetadataexception. This is a bug. Contact kw@cmp.uea.ac.uk",ex);
                }
            }else if(removeUnmappedFiles)
            {
                while (theDB.containsFile(files.get(i)))
                {
                    System.out.println("\tNo local copy of " + files.get(i) + " found, it is being removed from the database.");
                    theDB.removeFileFromDatabase(files.get(i));
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
     * Remaps filenames within a MusicDB Object to their IDs.
     * @param theDB

     * @return
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static MusicDB remapToMIREXIDs(MusicDB theDB) {

        System.out.println("Remapping files from MusicDB to their ids");

        //iterate through all file locations
        List<String> files = theDB.getFileNames();
        int done = 0;
        for (int i = 0; i < files.size(); i++) {
            //Check whether file in dir exists with same name

            File oldFile = new File(files.get(i));
            String key = convertFileToMIREX_ID(oldFile);

            try {
                //Change file location pointer and Signal Object
                theDB.remapFileLocation(files.get(i),key);
                //System.out.println("remapping '" + files.get(i) + "' to '" + key + "'");
                done++;
            } catch (noMetadataException ex) {
                throw new RuntimeException("File not mapped due to noMetadataexception. This is a bug. Contact kw@cmp.uea.ac.uk",ex);
            }

        }

        System.out.println("RemapMusicDBFilenames: Remapped " + done + " of " + files.size() + " files.");
        System.out.println("New DB size: " + theDB.size());
        return theDB;
    }

    public static DistanceMatrixInterface remapDistanceMatrixWithMIREXIDs(DistanceMatrixInterface theDistMat, String newFolderPath, boolean removeHTTPSpaces) throws IOException{
        if(theDistMat instanceof DenseDistanceMatrix){
            return remapDenseDistanceMatrixWithMIREXIDs((DenseDistanceMatrix)theDistMat, newFolderPath, removeHTTPSpaces);
        }else{
            return remapSparseDistanceMatrixWithMIREXIDs((SparseDistanceMatrix)theDistMat, newFolderPath, removeHTTPSpaces);
        }
    }

    /**
     * Remaps filenames within a DenseDistanceMatrix Object to files found within a particular
     * folder by matching the name minus an audio or midi file name extensions.
     * Support for matching directory tree structures (partial path matching)
     * will be added later.
     * @param theDistMat
     * @param newFolderPath
     * @param removeHTTPSpaces
     * @return
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static DenseDistanceMatrix remapDenseDistanceMatrixWithMIREXIDs(DenseDistanceMatrix theDistMat, String newFolderPath, boolean removeHTTPSpaces) throws IOException{

        //get available files
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List<File> availFileList = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
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
                File[] tmp = dirs.remove(0).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }

        Map<String,File> newFilesMap = new HashMap<String,File>();
        
        for (int i = 0; i < availFileList.size(); i++) {
            File aFile = availFileList.get(i);
            newFilesMap.put(convertFileToMIREX_ID(aFile),aFile);
        }

        //iterate through all file locations
        List<File> files = Arrays.asList(theDistMat.getFiles());
        int done = 0;
        for (int i = 0; i < files.size(); i++) {
            //Check whether file in dir exists with same name (and recursion through sub-dirs later)
            File oldFile = files.get(i);
            String key = convertFileToMIREX_ID(oldFile);
            if (removeHTTPSpaces){
                key = key.replaceAll("%20"," ");
            }
            File newFile = newFilesMap.get(key);
            if(newFile == null){
                System.out.println("No file was found to remap:" + oldFile.getPath() + " to!\nIt has been removed!");
                theDistMat.removeFile(oldFile);
            }else{
                //Change file location pointer and Signal Object
                theDistMat.remapFileLocation(oldFile.getPath(),newFile.getAbsolutePath());
                done++;
            }
        }

        System.out.println("remapDistanceMatrix: Remapped " + done + " of " + files.size() + " files.");
        System.out.println("New DistanceMatrix size: " + theDistMat.getFiles().length);
        return theDistMat;
    }

    /**
     * Remaps filenames within a SparseDistanceMatrix Object to files found within a particular
     * folder by matching the name minus an audio or midi file name extensions. Support for matching directory tree
     * structures (partial path matching) will be added later.
     * @param theDistMat
     * @param newFolderPath
     * @param removeHTTPSpaces
     * @return
     * @throws java.io.IOException Thrown if a problem occurs while searching the directory.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static SparseDistanceMatrix remapSparseDistanceMatrixWithMIREXIDs(SparseDistanceMatrix theDistMat, String newFolderPath, boolean removeHTTPSpaces) throws IOException{

        //get available files
        File newDir = new File(newFolderPath);
        File[] availFiles = newDir.listFiles();
        List<File> availFileList = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
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
                File[] tmp = dirs.remove(0).listFiles();
                if (tmp.length > 0){
                    availFiles = tmp;
                }
            }
        }

        HashMap<String,File> newFilesMap = new HashMap<String,File>(availFileList.size());

        for (int i = 0; i < availFileList.size(); i++) {
            File theFile = availFileList.get(i);
            String key = convertFileToMIREX_ID(theFile);
            if (newFilesMap.containsKey(key)){
                System.out.println("WARNING: Duplicate filename (" + key + ") detected");
            }
            newFilesMap.put(key,theFile.getAbsoluteFile());
        }

        theDistMat.remapFileLocationsWithMIREXIDs(newFilesMap);

        System.out.println("New DistanceMatrix size: " + theDistMat.getFiles().length);
        return theDistMat;
    }
    
    public static DistanceMatrixInterface remapDistanceMatrixToIDs(DistanceMatrixInterface theDistMat){
        if(theDistMat instanceof DenseDistanceMatrix){
            return remapDenseDistanceMatrixToIDs((DenseDistanceMatrix)theDistMat);
        }else{
            return remapSparseDistanceMatrixToIDs((SparseDistanceMatrix)theDistMat);
        }
    }
    
    /**
     * Remaps filenames within a DenseDistanceMatrix Object to their IDs by 
     * removing everything after the first period.
     * @param theDistMat
     * @return
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static DenseDistanceMatrix remapDenseDistanceMatrixToIDs(DenseDistanceMatrix theDistMat) {

        //iterate through all file locations
        File[] files = theDistMat.getFiles();
        int done = 0;
        for (int i = 0; i < files.length; i++) {
            String key = convertFileToMIREX_ID(files[i]);
            
            //Change file location pointer and Signal Object
            theDistMat.remapFileLocation(files[i].getPath(),key);
            done++;
        }
        return theDistMat;
    }

    /**
     * Remaps filenames within a SparseDistanceMatrix Object to their IDs by 
     * removing everything after the first period.
     * @param theDistMat
     * @return
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required filelocation metadata is not found.
     */
    public static SparseDistanceMatrix remapSparseDistanceMatrixToIDs(SparseDistanceMatrix theDistMat){
        theDistMat.remapFileLocationsToIDs();
        return theDistMat;
    }
}
