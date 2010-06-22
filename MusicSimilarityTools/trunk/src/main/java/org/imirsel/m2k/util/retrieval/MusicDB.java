package org.imirsel.m2k.util.retrieval;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.imirsel.m2k.math.Mathematics;
import org.imirsel.m2k.vis.ResultCurvePlot;

/**
 * Data model for persistant storage of and operations on audio file metadata,
 * particularly regarding artist, album and genre metadata (although any metadata
 * may be index). Also allows evaluation of a retrieval system over the database,
 * by measuring how the retrieval system clusters the data according to the managed
 * metadata types.
 *
 * @author Kris West (kw@cmp.uea.ac.uk)  
 */
public class MusicDB implements java.io.Serializable, java.lang.Cloneable {

    public static final long serialVersionUID = -2179456818738132209L;

    HashMap<String,Signal> fileNameToSignalObject = null;
    HashMap<String,HashMap<String,ArrayList<Signal>>> indexKeyToIndexMap = null;
    
    
    //store other stuff in Signal objects
    
    /**
     * Creates a new instance of MusicDB
     */
    public MusicDB() {
        fileNameToSignalObject = new HashMap<String,Signal>();
        indexKeyToIndexMap = new HashMap<String,HashMap<String,ArrayList<Signal>>>();
    }
    
    public MusicDB(Signal[] someData)
    {
        fileNameToSignalObject = new HashMap<String,Signal>();
        indexKeyToIndexMap = new HashMap<String,HashMap<String,ArrayList<Signal>>>();
        
        for (int i = 0; i < someData.length; i++) {
            try {
                addFileToDatabase(someData[i]);
            } catch (noMetadataException ex) {
                throw new RuntimeException("An error occurred while initialising the MusicDB Object with the specified data!",ex);
            }
        }
    }
    
    /**
     * Clones the MusicDB. Note data is not duplicated only mappings.
     * @return the clone
     */
    @Override
    public Object clone()//
    {
        MusicDB out = new MusicDB();
        //Signal Objects (not duplicated only mappings)
        out.fileNameToSignalObject.putAll(this.fileNameToSignalObject);
        
        //index keys
        String[] indexKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
        for (int i = 0; i < indexKeys.length; i++) {
            HashMap<String,ArrayList<Signal>> oldIndex = this.indexKeyToIndexMap.get(indexKeys[i]);
            HashMap<String,ArrayList<Signal>> newIndex = new HashMap<String,ArrayList<Signal>>();
            String[] metaKeys = oldIndex.keySet().toArray(new String[oldIndex.size()]);
            //file name lists for each key
            for (int j = 0; j < metaKeys.length; j++) {//Filenames are not expected to change so shallow copied to save mem
                //but ArrayLists of names and HashMaps of those lists must be deep copied
                newIndex.put(metaKeys[j],(ArrayList<Signal>)oldIndex.get(metaKeys[j]).clone());
            }
            out.indexKeyToIndexMap.put(indexKeys[i],newIndex);
        }
        return out;
    }
    
    /**
     * Clones the MusicDB. Note data is not duplicated only mappings.
     * @return the clone
     */
    @SuppressWarnings("unchecked")
    public MusicDB deepClone()//
    {
        MusicDB out = new MusicDB();
        
        //index keys
        String[] indexKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
        for (int i = 0; i < indexKeys.length; i++) {
            HashMap<String,ArrayList<Signal>> oldIndex = this.indexKeyToIndexMap.get(indexKeys[i]);
            HashMap<String,ArrayList<Signal>> newIndex = new HashMap<String,ArrayList<Signal>>();
            String[] metaKeys = oldIndex.keySet().toArray(new String[oldIndex.size()]);
            //file name lists for each key
            for (int j = 0; j < metaKeys.length; j++) {//Filenames are not expected to change so shallow copied to save mem
                //but ArrayLists of names and HashMaps of those lists must be deep copied
                ArrayList<Signal> signalList = (ArrayList<Signal>)oldIndex.get(metaKeys[j]).clone();
                try {
                    for (int k = 0; k < signalList.size(); k++) {
                        Signal theSig = (Signal) signalList.get(k).clone();
                        signalList.set(k, theSig);
                        out.fileNameToSignalObject.put(theSig.getStringMetadata(Signal.PROP_FILE_LOCATION), theSig);
                    
                    }   
                } catch (CloneNotSupportedException ex) {
                    throw new RuntimeException("CloneNotSupportedExceptionon in deep clone?!?",ex);
                } catch (noMetadataException nme){
                    throw new RuntimeException("noMetadataException in deep clone?!?",nme);
                }
                newIndex.put(metaKeys[j],signalList);
            }
            out.indexKeyToIndexMap.put(indexKeys[i],newIndex);
        }
        return out;
    }
    
    /**
     * Adds a metadata key to the indexed metadatas
     * @param theKey The key to be added to the indexed metadatas
     */
    public void indexKey(String theKey){
        HashMap<String,ArrayList<Signal>> index = new HashMap<String,ArrayList<Signal>>();
        indexKeyToIndexMap.put(theKey, index);
        if (fileNameToSignalObject.size() > 0)//already contains Signals then index
        {
            String[] files = fileNameToSignalObject.keySet().toArray(new String[fileNameToSignalObject.size()]);
            for (int i = 0; i < files.length; i++) {
                try {
                    Signal aSig = fileNameToSignalObject.get(files[i]);
                    if (containsMetadataClass(theKey,aSig.getMetadata(theKey))) {
                        ArrayList<Signal> metadataList = index.get((String)aSig.getMetadata(theKey));
                        if (!metadataList.contains(aSig)){
                            metadataList.add(aSig);
                        }
                    }else{
                        ArrayList<Signal> metadataList = new ArrayList<Signal>();
                        metadataList.add(aSig);
                        index.put(aSig.getStringMetadata(theKey),metadataList);
                    }
                } catch (noMetadataException ex) {
                    
                }
            }
        }
    }
    
    /**
     * Renames a class of a particular indexed metadata type. All relevant Signal
     * Objects indexed are modified.
     * @param IndexKey Metadata type being operated upon.
     * @param oldClassName The class name to change.
     * @param newClassName The new classname.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the IndexKey
     * is not an indexed metadata type or if the class to be renamed
     * is not found.
     */
    public void renameClassOfMetadata(String IndexKey, String oldClassName, String newClassName) throws noMetadataException{
        if (!this.indexingMetadata(IndexKey)) {
            throw new noMetadataException("MusicDB.oldClassName(): " + IndexKey + " is not an indexed metadata type!");
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        if (!index.containsKey(oldClassName)){
            throw new noMetadataException("MusicDB.oldClassName(): " + oldClassName + " was not found in the " + IndexKey + " index!");
        }
        ArrayList<Signal> classList = index.remove(oldClassName);
        for (int i = 0; i < classList.size(); i++) {
            Signal theSig = classList.get(i);
            theSig.setMetadata(IndexKey, newClassName);
        }
        index.put(newClassName, classList);
    }
    
    /**
     * Merges two classes of a particular indexed metadata type into a single
     * new class.
     * @param IndexKey Metadata type being operated upon.
     * @param class1 The first class of this metadata type to merge.
     * @param class2 The second class of this metadata type to merge.
     * @param newClassName The new classname for the merged classes.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the IndexKey
     * is not an indexed metadata type or if either of the classes to be merged
     * is not found.
     */
    public void joinTwoClassesOfMetadata(String IndexKey, String class1, String class2, String newClassName) throws noMetadataException{
        if (!this.indexingMetadata(IndexKey)) {
            throw new noMetadataException("MusicDB.joinTwoClassesOfMetadata(): " + IndexKey + " is not an indexed metadata type!");
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        if (!index.containsKey(class1)){
            throw new noMetadataException("MusicDB.joinTwoClassesOfMetadata(): " + class1 + " was not found in the " + IndexKey + " index!");
        }
        if(!index.containsKey(class2)) {
            throw new noMetadataException("MusicDB.joinTwoClassesOfMetadata(): " + class2 + " was not found in the " + IndexKey + " index!");
        }
        ArrayList<Signal> one = index.remove(class1);
        ArrayList<Signal> two = index.remove(class2);
        ArrayList<Signal> newList = new ArrayList<Signal>();
        for (int i = 0; i < one.size(); i++) {
            Signal theSig = one.get(i);
            theSig.setMetadata(IndexKey, newClassName);
            newList.add(theSig);
        }
        for (int i = 0; i < two.size(); i++) {
            Signal theSig = two.get(i);
            theSig.setMetadata(IndexKey, newClassName);
            newList.add(theSig);
        }
        index.put(newClassName, newList);
    }
    
    /**
     * Writes the MusicDB to a deliminated text file.
     * @param csvFile The path and file name to write to.
     * @param delim The delimator to use - comma (,), space ( ) or tab (\t) are recommended.
     * @param useSpeechMarks Determines whether values are wrapped in speechmarks in the file.
     * @throws java.io.IOException Thrown if an a problem occurs while writing to the file.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if any of the fileLocations are not found.
     */
    public void writeMusicDBToDelimTextFile(File csvFile, String delim, boolean useSpeechMarks) throws IOException, noMetadataException {
        String contents;
        
        //write headers
        String[] keys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
        //check that none of the indexes are empty
        ArrayList<String> keep = new ArrayList<String>();
        for (int i = 0; i < keys.length; i++) {
            if (((HashMap)this.indexKeyToIndexMap.get(keys[i])).size() > 0)
            {
                keep.add(keys[i]);
            }else{
                System.out.println("writeMusicDBToDelimTextFile: Ignoring " + keys[i] + " as the index for it is empty!");
            }
        }
        keys = keep.toArray(new String[keep.size()]);
        
        
        String[] headers = new String[keys.length + 1];
        headers[0] = Signal.PROP_FILE_LOCATION;
        for (int i = 0; i < keys.length; i++) {
            headers[i+1] = keys[i];
        }
        Signal[] sigs = getSignals().toArray(new Signal[this.size()]);
        String[][] data = new String[sigs.length+1][headers.length];
        data[0] = headers;
        for (int i = 0; i < sigs.length; i++) {
            data[i+1][0] = sigs[i].getStringMetadata(Signal.PROP_FILE_LOCATION);
            for (int j = 0; j < keys.length; j++) {
                try {
                    String aClass = sigs[i].getStringMetadata(keys[j]);
                    data[i+1][j+1] = aClass;
                } catch (noMetadataException ex) {
                    System.out.println("Warning: writeMusicDBToDelimTextFile: No " + keys[j] + " tag found for " + data[i+1][0]);
                    data[i+1][j+1] = "";
                }
            }
        }
        
        org.imirsel.m2k.io.file.DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(csvFile,delim,data,useSpeechMarks);
    }
    
    /**
     * Writes the MusicDB to a deliminated text file.
     * @param csvFile The path and file name to write to.
     * @param delim The delimator to use - comma (,), space ( ) or tab (\t) are recommended.
     * @throws java.io.IOException Thrown if an a problem occurs while writing to the file.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if any of the fileLocations are not found.
     */
    public void writeMusicDBToDelimTextFile(File csvFile, String delim) throws IOException, noMetadataException {
        writeMusicDBToDelimTextFile(csvFile, delim, true);
    }
    
    /**
     * Removes a metadata key from the indexed metadatas
     * @param theKey The metadata type to remove from the index.
     */
    public void stopIndexingKey(String theKey){
        if(this.indexingMetadata(theKey)) {
            indexKeyToIndexMap.remove(theKey);
        }
    }
    
    /**
     * Determines whether the specified metadata key is being indexed.
     * @param IndexKey The metadata key to check.
     * @return True if this key is being indexed.
     */
    public boolean indexingMetadata(String IndexKey) {
        return this.indexKeyToIndexMap.containsKey(IndexKey);
    }
    
    /**
     * Returns a list of the indexed metadatas.
     * @return a list of the indexed metadatas.
     */
    public List<String> getIndexedMetadatas(){
        ArrayList<String> IndexList = new ArrayList<String>();
        String[] indexes = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
        //java.util.Arrays.sort(indexes);
        for (int i = 0; i < indexes.length; i++) {
            IndexList.add(indexes[i]);
        }
        
        return IndexList;
    }
    
    /**
     * Determines whether a particular class of a particular metadata type is present
     * in the index.
     * @param IndexKey The metadata type to examine.
     * @param aClass The metadata class to look for.
     * @return True if the class of metadata is found.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type is not being indexed
     */
    public boolean containsMetadataClass(String IndexKey, Object aClass) throws noMetadataException {
        if (!indexingMetadata(IndexKey)){
            String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            String metas = "";
            for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                metas += metaKeys[i] + "\n";
            }
            throw new noMetadataException("MusicDB was not indexing the indicated metadata: " + IndexKey + "\nindexed metadatas:\n" + metas);
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        return index.containsKey(aClass);
    }
    
    /**
     * Returns the list of class names for a particular metadata type.
     * @param IndexKey The type of metadata to return a class list for.
     * @return The list of class names.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type is not indexed.
     */
    public List<String> getMetadataClasses(String IndexKey) throws noMetadataException {
        if (!indexingMetadata(IndexKey)){
            String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            String metas = "";
            for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                metas += metaKeys[i] + "\n";
            }
            throw new noMetadataException("MusicDB was not indexing the indicated metadata: " + IndexKey + "\nindexed metadatas:\n" + metas);
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        ArrayList<String> keys = new ArrayList<String>();
        String[] keysArr = index.keySet().toArray(new String[index.size()]);
        for (int i = 0; i < keysArr.length; i++) {
            keys.add(keysArr[i]);
        }
        return keys;
    }
    
    /**
     * Removes a single class of data from the index. All Signal Objects of that 
     * class are removed from the index.
     * @param IndexKey The metadata type to examine.
     * @param aClass The class of the specified metadata type to remove.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type or class is not indexed.
     */
    public void deleteMetadataClass(String IndexKey, String aClass) throws noMetadataException {
        if (!indexingMetadata(IndexKey)){
            String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            String metas = "";
            for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                metas += metaKeys[i] + "\n";
            }
            throw new noMetadataException("MusicDB was not indexing the indicated metadata: " + IndexKey + "\nindexed metadatas:\n" + metas);
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        ArrayList<String> keys = new ArrayList<String>();
        String[] keysArr = index.keySet().toArray(new String[index.size()]);
        for (int i = 0; i < keysArr.length; i++) {
            keys.add(keysArr[i]);
        }
        if (!keys.contains(aClass)){
            String classes = "";
            String[] classKeys = index.keySet().toArray(new String[index.size()]);
            for (int i = 0; i < classKeys.length; i++) {
                classes += classKeys[i] + "\n";
            }
            throw new noMetadataException("The MusicDB did not have a " + aClass + " class in the "+ IndexKey + " index.\nIndexed metadatas:\n" + classes);
        }
        
        List sigList = (List)index.get(aClass);
        for (int i = sigList.size()-1; i >= 0 ; i--) {
            this.removeSignalFromDatabase((Signal)sigList.get(i));
        }
        index.remove(aClass);
    }
    
    /**
     * Returns the list of class sizes for a particular metadata type as an 
     * ArrayList of Integer Objects.
     * @param IndexKey The type of metadata to return a class size list for.
     * @return The list of class sizes.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type is not indexed.
     */
    public List<Integer> getMetadataClassSizes(String IndexKey) throws noMetadataException {
        if (!indexingMetadata(IndexKey)){
            String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            String metas = "";
            for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                metas += metaKeys[i] + "\n";
            }
            throw new noMetadataException("MusicDB was not indexing the indicated metadata: " + IndexKey + "\nindexed metadatas:\n" + metas);
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        ArrayList<Integer> sizes = new ArrayList<Integer>();
        String[] keysArr = index.keySet().toArray(new String[index.size()]);
        for (int i = 0; i < keysArr.length; i++) {
            sizes.add(new Integer(((List)index.get(keysArr[i])).size()));
        }
        return sizes;
    }
    
    /**
     * Returns the list of files for a particular class of a particular indexed
     * metadata type.
     * @param IndexKey The metadata type to examine.
     * @param aClass The class of the specified metadata type to return a list for.
     * @return a list of Signal Objects for a particular class of a particular indexed metadata type
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type or class is not indexed.
     */
    public List<Signal> getSignalListForMetadataClass(String IndexKey, String aClass) throws noMetadataException {
        if (!indexingMetadata(IndexKey)){
            String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            String metas = "";
            for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                metas += metaKeys[i] + "\n";
            }
            throw new noMetadataException("MusicDB was not indexing the indicated metadata: " + IndexKey + "\nindexed metadatas:\n" + metas);
        }
        HashMap<String,ArrayList<Signal>> index = indexKeyToIndexMap.get(IndexKey);
        ArrayList<String> keys = new ArrayList<String>();
        String[] keysArr = index.keySet().toArray(new String[index.size()]);
        for (int i = 0; i < keysArr.length; i++) {
            keys.add(keysArr[i]);
        }
        if (!keys.contains(aClass)){
            String classes = "";
            String[] classKeys = index.keySet().toArray(new String[index.size()]);
            for (int i = 0; i < classKeys.length; i++) {
                classes += classKeys[i] + "\n";
            }
            throw new noMetadataException("The MusicDB did not have a " + aClass + " class in the "+ IndexKey + " index.\nIndexed metadatas:\n" + classes);
        }
        
        return index.get(aClass);
    }
    
    /**
     * Returns the class for a particular metadata type for a particular file name.
     * @param IndexKey The metadata type to return the class of for the file.
     * @param fileLocation The path and file name of the original file (filelocation metadata in a Signal Object).
     * @return The class name of the file for the specified metadata type.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type was not in the original Signal Object.
     */
    public String getMetadataClassForFile(String IndexKey, String fileLocation) throws noMetadataException {
        return getMetadataClassForSignal(IndexKey, fileNameToSignalObject.get(fileLocation));
    }
    
    /**
     * Returns the class for a particular metadata type for a particular Signal Object.
     * @param IndexKey The metadata type to return the class of for the file.
     * @param Signal The Signal Object to return the class of.
     * @return The class name of the Signal Object for the specified metadata type.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type was not in the original Signal Object.
     */
    public String getMetadataClassForSignal(String IndexKey, Signal Signal) throws noMetadataException {
        if (this.containsSignal(Signal)) {
            Signal theSig = this.fileNameToSignalObject.get(Signal.getStringMetadata(Signal.PROP_FILE_LOCATION));
            return theSig.getStringMetadata(IndexKey);
        }else {
            System.out.println("WARNING: The MusicDB object was not indexing the file " + Signal.getFile().getPath());
            return null;
        }
    }
    /**
     * Returns the class for a particular metadata type for a particular Signal Object.
     * @param IndexKey The metadata type to return the class of for the file.
     * @param Signal The Signal Object to return the class of.
     * @return The class name of the Signal Object for the specified metadata type.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the requested metadata type was not in the original Signal Object.
     */
    public HashSet<String> getMetadataClassesForSignal(String IndexKey, Signal Signal) throws noMetadataException {
        if (this.containsSignal(Signal)) {
            Signal theSig = this.fileNameToSignalObject.get(Signal.getStringMetadata(Signal.PROP_FILE_LOCATION));
            return (HashSet<String>)theSig.getMetadata(IndexKey);
        }else {
            System.out.println("WARNING: The MusicDB object was not indexing the file " + Signal.getFile().getPath());
            return null;
        }
    }
    
    /**
     * Returns true if the MusicDB is indexing the specified Signal Object.
     * @param aSignal The Signal to test to see if it is indexed.
     * @return True if the Signal Object is in the index.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the Signal Object doesn't contain a filelocation metadata.
     */
    public boolean containsSignal(Signal aSignal) throws noMetadataException {
        return containsFile(aSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
    }
    
    /**
     * Returns true if the MusicDB is indexing the specified file name.
     * @param filelocation The file name and path to check the index for.
     * @return true if the MusicDB is indexing the specified file name.
     */
    public boolean containsFile(String filelocation) {
        return fileNameToSignalObject.containsKey(filelocation);
    }
    
    /**
     * Add Signal Object and index. Null metadatas are ignored
     * @param theSignal The Signal Object to add to the index.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the ignal Object doesn't contain filelocation or other indexed metadatas.
     */
    public void addFileToDatabase(Signal theSignal) throws noMetadataException {
        
        if (this.containsSignal(theSignal))
        {
            System.out.println(theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION) + "already indexed, removing...");
            this.removeSignalFromDatabase(theSignal);
        }
        
        try {
            fileNameToSignalObject.put(theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), theSignal);
        } catch (noMetadataException ex) {
            throw new noMetadataException("A Signal Object must contain a file location in order to be indexed by the database.\n" + ex);
        }
        
        //iterate through indexed metadatas
        for (Iterator<String> it = this.indexKeyToIndexMap.keySet().iterator();it.hasNext();) {
            String key = it.next();
            try{
                if (theSignal.hasMetadata(key)){
                    if (key.equals(Signal.PROP_TAGS)){
                        HashSet<String> tags = (HashSet<String>)theSignal.getMetadata(key);
                        for (Iterator<String> it2 = tags.iterator(); it2.hasNext();) {
                            String aTag = it2.next();
                            HashMap<String,ArrayList<Signal>> index = this.indexKeyToIndexMap.get(key);
                            if (this.containsMetadataClass(key,aTag)) {
                                ArrayList<Signal> metadataList = index.get(aTag);
                                if (!metadataList.contains(theSignal)){
                                    metadataList.add(theSignal);
                                }
                            }else{
                                ArrayList<Signal> metadataList = new ArrayList<Signal>();
                                metadataList.add(theSignal);
                                index.put(aTag,metadataList);
                            }
                        }
                    }else{
                        String aClass = theSignal.getStringMetadata(key);
                        HashMap<String,ArrayList<Signal>> index = this.indexKeyToIndexMap.get(key);
                        if (this.containsMetadataClass(key,aClass)) {
                            ArrayList<Signal> metadataList = index.get(aClass);
                            if (!metadataList.contains(theSignal)){
                                metadataList.add(theSignal);
                            }
                        }else{
                            ArrayList<Signal> metadataList = new ArrayList<Signal>();
                            metadataList.add(theSignal);
                            index.put(aClass,metadataList);
                        }
                    }
                }
            } catch (noMetadataException ex) {
                
            }
        }
    }
    
    /**
     * Removes the specified ile from the index.
     * @param fileLocation File name and path to remove from the index.
     */
    public void removeFileFromDatabase(String fileLocation){
        if(this.fileNameToSignalObject.containsKey(fileLocation)){
            Signal theSig =  this.fileNameToSignalObject.remove(fileLocation);
            //iterate through indexed metadatas
            String[] keys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            for (int i = 0; i < keys.length; i++) {//remove from each index
                HashMap index = (HashMap)this.indexKeyToIndexMap.get(keys[i]);
                //Remove from each metadata class list;
                Object[] metakeys = index.keySet().toArray();
                for (int j = 0; j < metakeys.length; j++) {
                    ((ArrayList)index.get(metakeys[j])).remove(theSig);
                    if (((ArrayList)index.get(metakeys[j])).size() == 0) {
                        index.remove(metakeys[j]);
                    }
                    
                }
            }
        }
    }
    
    /**
     * Removes the specified Signal Object from the index.
     * @param theSig The Signal Object to remove from the index.
     */
    public void removeSignalFromDatabase(Signal theSig) {
        try {
            removeFileFromDatabase(theSig.getStringMetadata(Signal.PROP_FILE_LOCATION));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Unable to remove file as it has no fillocation metadata (which means we can't find it in the index!)\n" + ex);
        }
    }
    
    /**
     * Removes any file bearing one of the specified classnames of a the specified
     * metadata type from the index. Optionally, the MusicDB can be cloned and the
     * Objects files from the clone's index.
     * @param indexKey Metadata type to filter on.
     * @param classesToRemove A String array of the class names to remove.
     * @param cloneAndRemove Determines whether the MusicDB is cloned before filtering.
     * @return The filtered MusicDB.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the specified metadata type is not indexed or the filter classes
     * aren't found in the index.
     */
    public MusicDB filterDB(String indexKey, String[] classesToRemove, boolean cloneAndRemove) throws noMetadataException{
        if (!indexingMetadata(indexKey)){
            String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
            String metas = "";
            for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                metas += metaKeys[i] + "\n";
            }
            throw new noMetadataException("MusicDB was not indexing the indicated metadata: " + indexKey + "\nindexed metadatas:\n" + metas);
        }
        if (cloneAndRemove) {
            System.out.println("Cloning and Filtering MusicDB");
            MusicDB newDB = (MusicDB)this.clone();
            HashMap theIndex = (HashMap)this.indexKeyToIndexMap.get(indexKey);
            for (int i = 0; i < classesToRemove.length; i++) {
                ArrayList toGo = (ArrayList)theIndex.get(classesToRemove[i]);
                if (toGo != null) {
                    for (int j = 0; j < toGo.size(); j++) {
                        System.out.println("\tremoving " + ((Signal)toGo.get(j)).getStringMetadata(Signal.PROP_FILE_LOCATION));
                        newDB.removeSignalFromDatabase((Signal)toGo.get(j));
                    }
                }else{
                    System.out.println("MusicDB.filterDB(): WARNING: the class of metadata: " + classesToRemove[i] + " was not found, but removal was requested!");
                }
            }
            System.out.println("done.");
            return newDB;
        }else{
            System.out.println("Creating Filtered MusicDB");
            MusicDB newDB = new MusicDB();
            List<String> metas = this.getIndexedMetadatas();
            for (int i = 0; i < metas.size(); i++) {
                newDB.indexKey(metas.get(i));
            }
            ArrayList<String> removeList = new ArrayList<String>();
            for (int i = 0; i < classesToRemove.length; i++) {
                removeList.add(classesToRemove[i]);
            }
            HashMap<String,ArrayList<Signal>> theIndex = this.indexKeyToIndexMap.get(indexKey);
            
            String[] metaKeys = theIndex.keySet().toArray(new String[theIndex.size()]);
            for (int i = 0; i < metaKeys.length; i++) {
                if (!removeList.contains(metaKeys[i])) {
                    System.out.println("\tadding " + metaKeys[i]);
                    List sigList = this.getSignalListForMetadataClass(indexKey,metaKeys[i]);
                    //ArrayList fileList = (ArrayList)theIndex.get(metaKeys[sourceFold]);
                    
                    for (int j = 0; j < sigList.size(); j++) {
                        
                        newDB.addFileToDatabase((Signal)sigList.get(j));
                    }
                }
            }
            System.out.println("done.");
            return newDB;
        }
    }
    
    /**
     * Changes all entries corresponding to the specified file name to map to the new
     * file name.
     * @param oldLocation Old file name and path.
     * @param newLocation New file name and path.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the specified (Old) file name is not found in the index.
     */
    public void remapFileLocation(String oldLocation, String newLocation) throws noMetadataException{
        boolean present = this.fileNameToSignalObject.containsKey(oldLocation);
        if(present){
            Signal theSig = this.fileNameToSignalObject.remove(oldLocation);
            theSig.setMetadata(Signal.PROP_FILE_LOCATION, newLocation);
            this.fileNameToSignalObject.put(newLocation,theSig);
            //other metadata class list references to Signal should still be valid
        }else{
            throw new noMetadataException("The specified file was not found in the MusicDB and cannot be remapped to a new location!\nFile: " + oldLocation + "\nTo be mapped to: " + newLocation);
        }
    }
    
    /**
     * Returns the number of Objects represented in the index.
     * @return the number of Objects represented in the index.
     */
    public int size() {
        return fileNameToSignalObject.size();
    }
    
    /*
     *
     * @param theFile
     * @return
     */
    /*public boolean loadDBFromFile(File theFile)
    {
        //add file loading code
        //read index file
        //load Signals and add each to MusicDB
     
        return false;
    }*/
    
    /*
     *
     * @param theFile
     * @return
     */
    /*public boolean saveDBToFile(File theFile)
    {
        //add file saving code
        //ensure all Sigs are written out and write an index to their location
     
     
        return false;
    }*/
    
    
    /**
     * Returns the list of file names in the MusicDB's index.
     * @return the list of file names in the MusicDB's index.
     */
    public List<String> getFileNames(){
        ArrayList<String> fileList = new ArrayList<String>();
        String[] files = fileNameToSignalObject.keySet().toArray(new String[fileNameToSignalObject.keySet().size()]);
        for (int i = 0; i < files.length; i++) {
            fileList.add(files[i]);
        }
        return fileList;
    }
    
    /**
     * Returns a list of the Signal Objects in the index.
     * @return a list of the Signal Objects in the index.
     */
    public List<Signal> getSignals(){
        ArrayList<Signal> SignalList = new ArrayList<Signal>();
        Signal[] theSigs = this.fileNameToSignalObject.values().toArray(new Signal[this.fileNameToSignalObject.size()]);
        for (int i = 0; i < theSigs.length; i++) {
            SignalList.add(theSigs[i]);
        }
        return SignalList;
    }
    
    /**
     * Returns a double[] indicating how well, on average, the top N results are
     * clustered according to the indexed metadatas (This is sometimes known as a
     * Neighbourhood clustering metric). 
     * <Incorrect>
     * The result array is indexed as follows:
     * { averagePercentRetrieved1, averagePercentOfAvailRetrieved1, ... averagePercentRetrievedN,
     * averagePercentOfAvailRetrievedN } and is of length <code>this.getIndexedMetadatas.size()</code>.
     * The averagePercentRetrieved is calculated as the average of the number of
     * examples retrieved in the same metadata class divided by N while the averagePercentOfAvailRetrieved
     * normalises this score by the number of examples that were available in that
     * metadata class to avoid pessemistic estimates.
     *</incorrect>
     *
     * The average distance between the examples of each class is also calculated and
     * normalised by the average distance between all examples. These statistics are not
     * returned but output to the console.
     * @param retriever An implementation of the RetrieverInterface to be evaluated.
     * @param storageDirForReportAndPlots The directory to write out plots and results to.
     * @param triIneqRandomSeed random seed for generating triangular inequality tests
     * @param numTriIneqTests number of triangular inequality tests
     * @return result array <deprecated>
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found in Signal Objects being evaluated on.
     */
    public double[] evaluateRetriever(RetrieverInterface retriever, File storageDirForReportAndPlots, int triIneqRandomSeed, int numTriIneqTests) throws noMetadataException {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String reportTime = calendar.getTime().toString();
        
        final int[] TEST_LEVELS = {5, 10, 20, 50};
        int currentTestLevel = 0;
        int maxTestLevel = TEST_LEVELS[TEST_LEVELS.length-1];
        
        List<String> indexedMetadatas = this.getIndexedMetadatas();
        
        
        //get a DistanceMatrix to work on
        DistanceMatrixInterface dists = retriever.getDistanceMatrix();
        
        //Build a retriever from the DistanceMatrix
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(dists);
        
        //List files = Arrays.asList(dists.getFiles());
        List<Signal> Signals = Arrays.asList(ret.getSignals());
        
        retriever = null;
        
        //create a file location to index hashmap to speed up times similar file search
        HashMap<String,Integer> fileLoc2Index = new HashMap<String,Integer>(Signals.size());
        for (int i = 0; i < Signals.size(); i++) {
            fileLoc2Index.put(Signals.get(i).getStringMetadata(Signal.PROP_FILE_LOCATION), new Integer(i));
        }
        
        /*System.out.println("Evaluation files:");
        for (int sourceFold = 0; sourceFold < Signals.size(); sourceFold++) {
            System.out.println("\t" + ((Signal)Signals.get(sourceFold)).getStringMetadata(Signal.PROP_FILE_LOCATION));
        }*/
        System.out.println("");
        ArrayList<String> keys = (ArrayList<String>)this.getIndexedMetadatas();
        
        //variables for genre confusion matrices
        List genreClasses = this.getMetadataClasses(Signal.PROP_GENRE);
        List genreClassSizes = this.getMetadataClassSizes(Signal.PROP_GENRE);

        //int[][][] totalGenreConfusion = new int[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        //double[][][] totalPercentGenreConfusion = new double[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        //double[][][] totalPercentGenreAvailConfusion = new double[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        
        int[][][] totalFilteredGenreConfusion = new int[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        double[][][] totalPercentFilteredGenreAvailConfusion = new double[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        double[][][] totalPercentFilteredGenreConfusion = new double[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        
        
        //variables for neighbourhood counts
        int[][] totalNumInSameClass = new int[indexedMetadatas.size()][TEST_LEVELS.length];
        int[][] totalMaxCount = new int[indexedMetadatas.size()][TEST_LEVELS.length];
        double[][] totalPercentageCount = new double[indexedMetadatas.size()][TEST_LEVELS.length];
        double[][] totalPercentageAvailCount = new double[indexedMetadatas.size()][TEST_LEVELS.length];
        
        //variables for filtered genre neighbourhood tests
        int[] totalFilteredGenreInSameClass = new int[TEST_LEVELS.length];
        int[] totalFilteredGenreMaxCount = new int[TEST_LEVELS.length];
        double[] totalFilteredGenrePercentageCount = new double[TEST_LEVELS.length];
        double[] totalFilteredGenrePercentageAvailCount = new double[TEST_LEVELS.length];
        
        //variables for counting number of times a piece appeared in a result list
        int[][] timesSeen = new int[TEST_LEVELS.length][fileLoc2Index.size()];
        
        //variables for distance counts
        double[] labelDistances = new double[indexedMetadatas.size()];
        int[] labelCounts  = new int[indexedMetadatas.size()];
        double totalDistance = 0.0;
        long totalCount = 0;
        
        //create result dir
        if (storageDirForReportAndPlots != null){
        
            if (storageDirForReportAndPlots.exists()){
                if (!storageDirForReportAndPlots.isDirectory())
                {
                    throw new RuntimeException("unable to create evaluation output dir as it already exists as a file!");
                }
            }else{
                storageDirForReportAndPlots.mkdirs();
            }
        }
        
        System.out.println("Counting metadata matches and average distances");
        
        //count matches on each metadata ignoring self retrieval
        for (int i = 0; i < Signals.size(); i++) {
            if (i % 100 == 0){
                System.out.println("evaluating on " + i + " of " + Signals.size());
            }
            //reset test level counter
            currentTestLevel = 0;
            


            Signal theQuery = Signals.get(i);

            //retrieve results
            SearchResult[] similar = ret.retrieveNMostSimilar(theQuery, maxTestLevel+1);
            
            //initialise temp storage for neighbourhood counts
            int[][] numInSameClass = new int[indexedMetadatas.size()][TEST_LEVELS.length];
            int[][] maxCount = new int[indexedMetadatas.size()][TEST_LEVELS.length];
            boolean retrievedSelf = false;
            
            File file1 = Signals.get(i).getFile();

            //measure average distance between all examples (for normalisation)
            for (int k = 0; k < Signals.size(); k++) {

                //ignore self retrieval
                if(k != i) {
                    totalDistance += dists.getDistance(file1, Signals.get(k).getFile());
                    totalCount++;
                }
            }
            
            
            //measure neighbourhood distances
            for (int k = 0; k < maxTestLevel+1; k++) {
                //if on last result of the current test level
                if (k == TEST_LEVELS[currentTestLevel]) {
                    
                    //collect last result if retriever also retrieved query
                    if(retrievedSelf){
                        for (int j = 0; j < indexedMetadatas.size(); j++) {
                            if(this.getMetadataClassForSignal(keys.get(j),similar[k].getTheResult()).equals(this.getMetadataClassForSignal(keys.get(j),theQuery))) {
                                numInSameClass[j][currentTestLevel]++;
                            }
                        }
                        
                        int idx = -1;//= Signals.indexOf(similar[k].getTheResult());
                        //File loc = new File(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION));
                        idx = (fileLoc2Index.get(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION))).intValue();
                        
                        for (int j = currentTestLevel; j < TEST_LEVELS.length; j++) {
                            if (idx == -1){
                                System.out.println("A result  (" +  similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION) + ") was not found in the evaluation database (" + Signals.size() + " files)!");
                                break;
                            }else{
                                timesSeen[j][idx]++;
                            }
                        }
                    }
                    if (currentTestLevel >= (TEST_LEVELS.length - 1)) {
                        //we're done!
                        break;
                    }else{
                        //copy vals to next test spot so we can carry on where we left off
                        for (int j = 0; j < indexedMetadatas.size(); j++) {
                            numInSameClass[j][currentTestLevel+1] = numInSameClass[j][currentTestLevel];
                        }
                    }
                    //increment test level
                    currentTestLevel++;
                }
                else{
                    //catch retrieval of query
                    
                    /*if (similar[k] == null)
                    {
                        throw new RuntimeException("Result " + k + " was null!");
                    }*/
                    
                    
                    if(similar[k].getTheResult().equals(theQuery)) {
                        retrievedSelf = true;
                    }else{
                        //record neighbourhood matches
                        for (int j = 0; j < indexedMetadatas.size(); j++){
                            if(this.getMetadataClassForSignal(keys.get(j),similar[k].getTheResult()).equals(this.getMetadataClassForSignal(keys.get(j),theQuery))) {
                                numInSameClass[j][currentTestLevel]++;
                            }
                        }
                        
                        int idx = -1;//= Signals.indexOf(similar[k].getTheResult());
                        idx = (fileLoc2Index.get(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION))).intValue();
                        
                        //record times seen
                        for (int j = currentTestLevel; j < TEST_LEVELS.length; j++) { 
                            if (idx == -1){
                                System.out.println("A result (" + similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION) + ") was not found in the evaluation database (" + Signals.size() + " files)!");
                            }else{
                                timesSeen[j][idx]++;
                            }
                        }
                    }
                }
            }
            
            
            
            
            
            
            //measure full distances
            for (int j = 0; j < indexedMetadatas.size(); j++){
                String queryMeta = this.getMetadataClassForSignal(keys.get(j),theQuery);
                for (int k = 0; k < Signals.size(); k++) {
                    //ignore self retrieval
                    if(k != i) {
                        Signal theSig = Signals.get(k);
                        if(this.getMetadataClassForSignal(keys.get(j),theSig).equals(queryMeta)) {
                            labelDistances[j] += dists.getDistance(file1, theSig.getFile());
                            labelCounts[j]++;
                        }
                    }
                }
            }
            
            
            for (int j = 0; j < indexedMetadatas.size(); j++){
                String metadataClass = this.getMetadataClassForSignal(keys.get(j),theQuery);
                HashMap<String, ArrayList<Signal>> index = this.indexKeyToIndexMap.get(keys.get(j));
                ArrayList<Signal> metaClassList = index.get(metadataClass);
                for (int k = 0; k < TEST_LEVELS.length; k++) {
                    //Collect number of examples for each query available in database
                    if (metaClassList.size()>TEST_LEVELS[k]) {
                        maxCount[j][k] = TEST_LEVELS[k];
                    }else{
                        maxCount[j][k] = metaClassList.size();
                    }
                    
                    //Add to results
                    totalNumInSameClass[j][k] += numInSameClass[j][k];
                    totalMaxCount[j][k] += maxCount[j][k];
                    totalPercentageCount[j][k] += (double)numInSameClass[j][k] / (double)TEST_LEVELS[k];
                    totalPercentageAvailCount[j][k] += (double)numInSameClass[j][k] / (double)maxCount[j][k];
                }
            }
        }
        
        //calculate random baselines for neighbourhood metrics
        System.out.println("Calculating random baselines...");
        double[][] totalPercentageCountBaseline = new double[indexedMetadatas.size()][TEST_LEVELS.length];
        
        for (int j = 0; j < indexedMetadatas.size(); j++) {
            
            HashMap index = (HashMap)this.indexKeyToIndexMap.get(keys.get(j));
            //get class list
            Object[] classLists = index.values().toArray();
            //iterate through all classes
            for (int i = 0; i < index.size(); i++) {
                for (int k = 0; k < TEST_LEVELS.length; k++) {
                    double listSize = (double)((ArrayList)classLists[i]).size();
                    double oneSelectProbMax = ( (listSize - 1.0) / (double)(this.size() - 1) );
                    totalPercentageCountBaseline[j][k] += ( listSize * oneSelectProbMax /* * (double)TEST_LEVELS[k]*/ );
                }
            }
        }
        
        
        //measure artist filtered genre neighbourhood
        if (this.indexingMetadata(Signal.PROP_GENRE) && this.indexingMetadata(Signal.PROP_ARTIST)){
            System.out.println("Counting artist-filtered genre matches");
            //count artist filtered genre metadata matches ignoring self retrieval
            for (int i = 0; i < Signals.size(); i++) {
                if (i % 100 == 0){
                    System.out.println("evaluating on " + i + " of " + Signals.size());
                }//reset test level counter
                currentTestLevel = 0;
                
                //retrieve results
                SearchResult[] similar = ret.retrieveNMostSimilar(Signals.get(i), 300);
                
                
                //initialise temp storage for neighbourhood counts
                int[] numInSameClass = new int[TEST_LEVELS.length];
                
                
                int[][][] confusion = new int[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
                
                int[] maxCount = new int[TEST_LEVELS.length];
                
                int testsPossible = 0;
                int k = 0;
                //measure filtered genre neighbourhood matches
                //end when enough filtered results collected
                int queryClass = genreClasses.indexOf(this.getMetadataClassForSignal(Signal.PROP_GENRE,Signals.get(i)));
                while ((testsPossible < maxTestLevel+1)&&(k < similar.length))
                {
                    //if on last result in current test level
                    if (testsPossible == TEST_LEVELS[currentTestLevel]) {
                        
                        if (currentTestLevel >= (TEST_LEVELS.length - 1)) {
                            //we're done!
                            break;
                        }else{
                            //copy vals to next test spot so we can carry on where we left off
                            numInSameClass[currentTestLevel+1] = numInSameClass[currentTestLevel];
                            
                            for (int q = 0; q < genreClasses.size(); q++) {
                                for (int r = 0; r < genreClasses.size(); r++) {
                                    confusion[currentTestLevel+1][q][r] = confusion[currentTestLevel][q][r];
                                }
                            }
                        }
                        //increment test level
                        currentTestLevel++;
                    }
                    else{
                        //catch retrieval of query and songs by same artist
                        if((!this.getMetadataClassForSignal(Signal.PROP_ARTIST,similar[k].getTheResult()).equals(this.getMetadataClassForSignal(Signal.PROP_ARTIST,Signals.get(i)))) && (!similar[k].getTheResult().equals(Signals.get(i)))){
                            //record filtered neighbourhood matches
                            int resultClass = genreClasses.indexOf(this.getMetadataClassForSignal(Signal.PROP_GENRE,similar[k].getTheResult()));
                            
                            if(queryClass == resultClass) {
                                numInSameClass[currentTestLevel]++;
                                confusion[currentTestLevel][queryClass][queryClass]++;
                            }else{
                                confusion[currentTestLevel][queryClass][resultClass]++;
                            }
                            testsPossible++;
                        }
                    }
                    
                    k++;
                }

                String metadataClass = this.getMetadataClassForSignal(Signal.PROP_GENRE,Signals.get(i));
                HashMap index = (HashMap)this.indexKeyToIndexMap.get(Signal.PROP_GENRE);
                ArrayList metaClassList = (ArrayList)index.get(metadataClass);
                for (int t = 0; t < TEST_LEVELS.length; t++) {
                    //Collect number of examples for each query available in database
                    if (testsPossible > TEST_LEVELS[t]) {
                        maxCount[t] = TEST_LEVELS[t];
                    }else{
                        maxCount[t] = testsPossible;
                    }
                    
                    if (metaClassList.size() < maxCount[t])
                    {
                        maxCount[t] = metaClassList.size();
                    }

                    //Add to results
                    totalFilteredGenreInSameClass[t] += numInSameClass[t];
                    totalFilteredGenreMaxCount[t] += maxCount[t];
                    totalFilteredGenrePercentageCount[t] += (double)numInSameClass[t] / (double)TEST_LEVELS[t];
                    totalFilteredGenrePercentageAvailCount[t] += (double)numInSameClass[t] / (double)maxCount[t];
                    
                    for (int j = 0; j < genreClasses.size(); j++) {
                        for (int l = 0; l < genreClasses.size(); l++) {
                            totalFilteredGenreConfusion[t][j][l] += confusion[t][j][l];
                            totalPercentFilteredGenreConfusion[t][j][l] += (double)confusion[t][j][l] / (double)TEST_LEVELS[t];
                            totalPercentFilteredGenreAvailConfusion[t][j][l] += (double)confusion[t][j][l] / (double)maxCount[t];
                        }
                    }
                    
                }
            }
        }
        
        
        
        
        
        
        
        System.out.println("Counting number of examples always and never similar");
        
        //Calculate % examples never similar and # examples similar
        double[] neverSimilar = new double[TEST_LEVELS.length];
        int[] alwaysSimilar = new int[TEST_LEVELS.length];
        int[] indexes = new int[TEST_LEVELS.length];
        
        for (int k = 0; k < TEST_LEVELS.length; k++) {
            int neverSimilarCount = 0;
            int max = 0;
            int maxIdx = -1;
            for (int i = 0; i < Signals.size(); i++) {
                if (timesSeen[k][i] == 0)
                {
                    neverSimilarCount++;
                }else if(timesSeen[k][i] > max)
                {
                    max = timesSeen[k][i];
                    maxIdx = i;
                }
            }
            neverSimilar[k] = (double)neverSimilarCount / (double)Signals.size();
            alwaysSimilar[k] = max;
            indexes[k] = maxIdx;
        }
        
        File[] filesArr = dists.getFiles();
        int indexSize = dists.indexSize();
        
        //calculate #number of times traingular inequality held, which is when d(A,C) <= (d(A,B) + d (B,C)
        System.out.println("Counting number of times traingular inequality held in " + numTriIneqTests + " random tests on " + indexSize + " files...");
        
        int timesTriangularInequalityHeld = 0;
        double percentTriangularInequalityHeld = 0.0;
        Random rnd = new Random(triIneqRandomSeed);
        int count = 0;
        int outCount = 0;
        
        
        while(count < numTriIneqTests){    
            if (outCount == 20000)
            {
                outCount = 0;
                System.out.println("\t done " + count + " tests");
            }
            outCount++;
            int a = 0;
            int b = 0;
            int c = 0;
            //get a triangle
            while ((a == b)||(a == c)||(b == c))
            {
                a = rnd.nextInt(indexSize);
                b = rnd.nextInt(indexSize);
                c = rnd.nextInt(indexSize);
            }
            
            if (dists.getDistance(filesArr[a],filesArr[c]) <= (dists.getDistance(filesArr[a],filesArr[b]) + dists.getDistance(filesArr[b],filesArr[c]))){
                if (dists.getDistance(filesArr[a],filesArr[b]) <= (dists.getDistance(filesArr[a],filesArr[c]) + dists.getDistance(filesArr[c],filesArr[b]))){
                    if (dists.getDistance(filesArr[b],filesArr[c]) <= (dists.getDistance(filesArr[b],filesArr[a]) + dists.getDistance(filesArr[a],filesArr[c]))){
                        timesTriangularInequalityHeld++;
                    }
                }   
            } 
            
            count++;
        }
        percentTriangularInequalityHeld = (double)timesTriangularInequalityHeld / (double)numTriIneqTests;
        
        
        
        
        //Write evaluation report
        System.out.println("Outputting...");
        
        //deprecate?
        double[] output = new double[2 * indexedMetadatas.size()];
        for (int i = 0; i < indexedMetadatas.size(); i++) {
            output[i*2] = totalPercentageCount[i][2] / (double)Signals.size();
            output[i*2 + 1] = totalPercentageAvailCount[i][2] / (double)Signals.size();
        }
        
        String msg = "Retriever: " + ret.getRetrieverName() + "\n";
        
        msg += "Report time stamp: " + reportTime + "\n\n";
        
        msg += "---\nNeighbourhood clustering according to indexed metadatas:\n";
        
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "\t[% top " + TEST_LEVELS[i] + " in same class, Random baseline, Normalised % top " + TEST_LEVELS[i] + " in same class], \n";
            for (int j = 0; j < indexedMetadatas.size(); j++) {
                msg += indexedMetadatas.get(j) + "\t" + (totalPercentageCount[j][i] / (double)Signals.size()) + "\t" + (totalPercentageCountBaseline[j][i]  / (double)Signals.size()) + "\t" + totalPercentageAvailCount[j][i] / (double)Signals.size() + "\n";
            }
        }
        
        msg += "---\nArtist Filtered Genre Neighbourhood clustering:\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "\t\t[% top " + TEST_LEVELS[i] + " in same class, Normalised % top " + TEST_LEVELS[i] + " in same class]\n";
            
                msg += "Filtered Genre\t" + (totalFilteredGenrePercentageCount[i] / (double)Signals.size()) + "\t" + (totalFilteredGenrePercentageAvailCount[i] / (double)Signals.size()) + "\n";
            
        }
        
        double numSignals = (double)Signals.size();
        
        DecimalFormat DF = new DecimalFormat();
        DF.setMaximumFractionDigits(5);
        msg += "---\nArtist Filtered Genre Neighbourhood confusion:\n\n";
        msg += "Classes:\n";
        for (int i = 0; i < genreClasses.size(); i++) {
            msg += i + ":\t" + (String)genreClasses.get(i) + "\n";
        }
        msg += "\n";
        
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "At " + TEST_LEVELS[i] + " results\n";
            msg += "q:\t";
            for (int j = 0; j < genreClasses.size(); j++) {
                msg += "  " + j + "  \t";
            }
            msg += "\nr:\n";
            for (int r = 0; r < genreClasses.size(); r++) {
                msg += r + ":\t";
                for (int q = 0; q < genreClasses.size(); q++) {
                    msg += DF.format(totalPercentFilteredGenreConfusion[i][q][r] / (double)((Integer)genreClassSizes.get(q)).intValue()) + "\t";
                }
                msg += "\n";
            }
        }
        msg += "\n";
        
        msg += "---\nMean filtered genre accuracy:\n\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < genreClasses.size(); j++) {
                sum += totalPercentFilteredGenreConfusion[i][j][j] / (double)((Integer)genreClassSizes.get(j)).intValue();
            }
            msg += "At " + TEST_LEVELS[i] + " results: " + sum/(double)genreClasses.size();
        }
        msg += "\n";
        
        msg += "---\nNormalised Artist Filtered Genre Neighbourhood confusion:\n\n";
        msg += "Classes:\n";
        for (int i = 0; i < genreClasses.size(); i++) {
            msg += i + ":\t" + (String)genreClasses.get(i) + "\n";
        }
        msg += "\n";
        
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "At " + TEST_LEVELS[i] + " results\n";
            msg += "q:\t";
            for (int j = 0; j < genreClasses.size(); j++) {
                msg += "  " + j + "  \t";
            }
            msg += "\nr:\n";
            for (int r = 0; r < genreClasses.size(); r++) {
                msg += r + ":\t";
                for (int q = 0; q < genreClasses.size(); q++) {
                    msg += DF.format(totalPercentFilteredGenreAvailConfusion[i][q][r] / (double)((Integer)genreClassSizes.get(q)).intValue()) + "\t";
                }
                msg += "\n";
            }
        }
        msg += "\n";
        
        msg += "---\nMean Normalised filtered genre accuracy:\n\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < genreClasses.size(); j++) {
                sum += totalPercentFilteredGenreAvailConfusion[i][j][j] / (double)((Integer)genreClassSizes.get(j)).intValue();
            }
            msg += "At " + TEST_LEVELS[i] + " results: " + sum/(double)genreClasses.size();
        }
        msg += "\n";
        
        double avgDist = totalDistance / (double)totalCount;
        
        msg += "---\nNormalised average distance between examples of same class:\n";
        double[] normLabelDists = new double[labelDistances.length];
        for (int i = 0; i < indexedMetadatas.size(); i++) {
            if (labelCounts[i] > 0) {
                normLabelDists[i] = (labelDistances[i] / (double)labelCounts[i])/avgDist;
            }else{
                normLabelDists[i] = Double.MAX_VALUE;
            }
            msg += indexedMetadatas.get(i) + "\t" + normLabelDists[i] + "\n";
        }
        
        if (indexedMetadatas.contains("genre") && indexedMetadatas.contains("artist")) {
            msg += "---\nArtist/Genre ratio:\t" + (normLabelDists[indexedMetadatas.indexOf("artist")] / normLabelDists[indexedMetadatas.indexOf("genre")]);
        }
        
        msg += "\n---\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "% of files never similar at " + TEST_LEVELS[i] + " results:\t" + neverSimilar[i] + "\n";
        }
        msg += "---\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "Maximum number of times a song was similar at " + TEST_LEVELS[i] + " results:\t" + alwaysSimilar[i] + "\n";
            Signal theSig = Signals.get(indexes[i]);
            msg += "\t\tTrack: " + theSig.getStringMetadata(Signal.PROP_FILE_LOCATION) + ", genre: " + this.getMetadataClassForSignal(Signal.PROP_GENRE,theSig) + "\n";
        }
        
        msg += "---\n";
        msg += "Number of times the triangular inequality held in " + numTriIneqTests + ": " + timesTriangularInequalityHeld + " (" + (percentTriangularInequalityHeld * 100.0) + "%)\n";
        
        System.out.println(msg);
        
        //write report to disk
        if (storageDirForReportAndPlots != null){
            BufferedWriter outputWriter = null;

            File resultFile = new File(storageDirForReportAndPlots.getPath() + File.separator + "report.txt");
            try {
                try {
                    //use buffering
                    outputWriter = new BufferedWriter( new FileWriter(resultFile) );
                    outputWriter.write( msg );
                    outputWriter.flush();
                }catch(IOException ioe){
                    System.out.println("IOException prevented writing of report to disk!\n" + ioe);
                }
                finally {
                    //flush and close both "output" and its underlying FileWriter
                    if (outputWriter != null){
                        outputWriter.close();
                    }
                }
            } catch (IOException ex) {
                System.out.println("IOException ocurred when closing output stream!\n" + ex);
                ex.printStackTrace();
            }
        }
        
//        try {
//            //produce times similar plots
//            ResultCurvePlot[] plots = new ResultCurvePlot[TEST_LEVELS.length];
//            for (int sourceFold = 0; sourceFold < TEST_LEVELS.length; sourceFold++) {
//                plots[sourceFold] = new ResultCurvePlot(timesSeen[sourceFold], ret.getRetrieverName() + " # times similar plot - top " + TEST_LEVELS[sourceFold]);
//                if (storageDirForReportAndPlots != null){
//                    File aPlotFile = new File(storageDirForReportAndPlots.getPath() + File.separator + "timesSimilarPlot_" + TEST_LEVELS[sourceFold] + ".png");
//                    try {
//                        //save to PNG files
//                        plots[sourceFold].writeImageToPNG(aPlotFile);
//                    } catch (IOException ex) {
//                        System.out.println("IOException prevented saving of plot to disk!\n" + ex);
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        } catch (java.lang.InternalError err){ //(Exception ex){
//            System.out.println("Failed to create times similar plot images, perhaps no X11 session is available or the Display variable is not set.");
//        }
//        //Display plots if possible
//        for (int sourceFold = 0; sourceFold < TEST_LEVELS.length; sourceFold++) {
//            JFrame theFrame = new JFrame(plots[sourceFold].getName());
//            theFrame.add(plots[sourceFold]);
//            theFrame.setSize(plots[sourceFold].getWidth() + 20, plots[sourceFold].getHeight() + 40);
//            theFrame.validate();
//            theFrame.setVisible(true);
//        }
        
        return output;
    }
    
    public void filteredGenreMatches(RetrieverInterface retriever, File storageDirForReportAndPlots) throws noMetadataException {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String reportTime = calendar.getTime().toString();
        
        final int[] TEST_LEVELS = {5, 10, 20, 50};
        int currentTestLevel = 0;
        int maxTestLevel = TEST_LEVELS[TEST_LEVELS.length-1];
        
        //get a DistanceMatrix to work on
        DistanceMatrixInterface dists = retriever.getDistanceMatrix();
        
        //Build a retriever from the DistanceMatrix
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(dists);
        
        //List files = Arrays.asList(dists.getFiles());
        List Signals = Arrays.asList(ret.getSignals());
        
        retriever = null;
        
        
        System.out.println("");
        
        //variables for genre confusion matrices
        List genreClasses = this.getMetadataClasses(Signal.PROP_GENRE);
        List genreClassSizes = this.getMetadataClassSizes(Signal.PROP_GENRE);

        //int[][][] totalGenreConfusion = new int[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        
        int[][][] totalFilteredGenreConfusion = new int[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        double[][][] totalPercentFilteredGenreAvailConfusion = new double[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        double[][][] totalPercentFilteredGenreConfusion = new double[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
        
        //variables for filtered genre neighbourhood tests
        int[] totalFilteredGenreInSameClass = new int[TEST_LEVELS.length];
        int[] totalFilteredGenreMaxCount = new int[TEST_LEVELS.length];
        double[] totalFilteredGenrePercentageCount = new double[TEST_LEVELS.length];
        double[] totalFilteredGenrePercentageAvailCount = new double[TEST_LEVELS.length];
        
        //create result dir
        if (storageDirForReportAndPlots != null){
        
            if (storageDirForReportAndPlots.exists()){
                if (!storageDirForReportAndPlots.isDirectory())
                {
                    throw new RuntimeException("unable to create evaluation output dir as it already exists as a file!");
                }
            }else{
                storageDirForReportAndPlots.mkdirs();
            }
        }
        
        //measure artist filtered genre neighbourhood
        if (this.indexingMetadata(Signal.PROP_GENRE) && this.indexingMetadata(Signal.PROP_ARTIST)){
            System.out.println("Counting artist-filtered genre matches");
            //count artist filtered genre metadata matches ignoring self retrieval
            for (int i = 0; i < Signals.size(); i++) {
                System.out.println("evaluating on " + i + " of " + Signals.size());
                //reset test level counter
                currentTestLevel = 0;
                
                //retrieve results
                SearchResult[] similar = ret.retrieveNMostSimilar((Signal)Signals.get(i), 300);
                
                
                //initialise temp storage for neighbourhood counts
                int[] numInSameClass = new int[TEST_LEVELS.length];
                
                
                int[][][] confusion = new int[TEST_LEVELS.length][genreClasses.size()][genreClasses.size()];
                
                int[] maxCount = new int[TEST_LEVELS.length];
                
                int testsPossible = 0;
                int k = 0;
                //measure filtered genre neighbourhood matches
                //end when enough filtered results collected
                int queryClass = genreClasses.indexOf(this.getMetadataClassForSignal(Signal.PROP_GENRE,(Signal)Signals.get(i)));
                while ((testsPossible < maxTestLevel+1)&&(k < similar.length))
                {
                    //if on last result in current test level
                    if (k == TEST_LEVELS[currentTestLevel]) {
                        
                        if (currentTestLevel >= (TEST_LEVELS.length - 1)) {
                            //we're done!
                            break;
                        }else{
                            //copy vals to next test spot so we can carry on where we left off
                            numInSameClass[currentTestLevel+1] = numInSameClass[currentTestLevel];
                            
                            for (int q = 0; q < genreClasses.size(); q++) {
                                for (int r = 0; r < genreClasses.size(); r++) {
                                    confusion[currentTestLevel+1][q][r] = confusion[currentTestLevel][q][r];
                                }
                            }
                        }
                        //increment test level
                        currentTestLevel++;
                    }
                    else{
                        //catch retrieval of query and songs by same artist
                        if((!this.getMetadataClassForSignal(Signal.PROP_ARTIST,similar[k].getTheResult()).equals(this.getMetadataClassForSignal(Signal.PROP_ARTIST,(Signal)Signals.get(i)))) && (!similar[k].getTheResult().equals((Signal)Signals.get(i)))){
                            //record filtered neighbourhood matches
                            int resultClass = genreClasses.indexOf(this.getMetadataClassForSignal(Signal.PROP_GENRE,similar[k].getTheResult()));
                            
                            if(queryClass == resultClass) {
                                numInSameClass[currentTestLevel]++;
                                confusion[currentTestLevel][queryClass][queryClass]++;
                            }else{
                                confusion[currentTestLevel][queryClass][resultClass]++;
                            }
                            testsPossible++;
                        }
                    }
                    
                    k++;
                }

                String metadataClass = this.getMetadataClassForSignal(Signal.PROP_GENRE,(Signal)Signals.get(i));
                HashMap index = (HashMap)this.indexKeyToIndexMap.get(Signal.PROP_GENRE);
                ArrayList metaClassList = (ArrayList)index.get(metadataClass);
                for (int t = 0; t < TEST_LEVELS.length; t++) {
                    //Collect number of examples for each query available in database
                    if (testsPossible > TEST_LEVELS[t]) {
                        maxCount[t] = TEST_LEVELS[t];
                    }else{
                        maxCount[t] = testsPossible;
                    }

                    //Add to results
                    totalFilteredGenreInSameClass[t] += numInSameClass[t];
                    totalFilteredGenreMaxCount[t] += maxCount[t];
                    totalFilteredGenrePercentageCount[t] += (double)numInSameClass[t] / (double)TEST_LEVELS[t];
                    totalFilteredGenrePercentageAvailCount[t] += (double)numInSameClass[t] / (double)maxCount[t];
                    
                    for (int j = 0; j < genreClasses.size(); j++) {
                        for (int l = 0; l < genreClasses.size(); l++) {
                            totalFilteredGenreConfusion[t][j][l] += confusion[t][j][l];
                            totalPercentFilteredGenreConfusion[t][j][l] += (double)confusion[t][j][l] / (double)TEST_LEVELS[t];
                            totalPercentFilteredGenreAvailConfusion[t][j][l] += (double)confusion[t][j][l] / (double)maxCount[t];
                        }
                    }
                    
                }
            }
        }
        
        
        //Write evaluation report
        System.out.println("Outputting...");
        
        String msg = "Retriever: " + ret.getRetrieverName() + "\n";
        
        msg += "Report time stamp: " + reportTime + "\n\n";
        
        msg += "---\nArtist Filtered Genre Neighbourhood clustering:\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "\t\t[% top " + TEST_LEVELS[i] + " in same class, Normalised % top " + TEST_LEVELS[i] + " in same class]\n";
            
                msg += "Filtered Genre\t" + (totalFilteredGenrePercentageCount[i] / (double)Signals.size()) + "\t" + (totalFilteredGenrePercentageAvailCount[i] / (double)Signals.size()) + "\n";
            
        }
        
        double numSignals = (double)Signals.size();
        
        DecimalFormat DF = new DecimalFormat();
        DF.setMaximumFractionDigits(5);
        msg += "---\nArtist Filtered Genre Neighbourhood confusion:\n\n";
        msg += "Classes:\n";
        for (int i = 0; i < genreClasses.size(); i++) {
            msg += i + ":\t" + (String)genreClasses.get(i) + "\n";
        }
        msg += "\n";
        
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "At " + TEST_LEVELS[i] + " results\n";
            msg += "q:\t";
            for (int j = 0; j < genreClasses.size(); j++) {
                msg += "  " + j + "  \t";
            }
            msg += "\nr:\n";
            for (int r = 0; r < genreClasses.size(); r++) {
                msg += r + ":\t";
                for (int q = 0; q < genreClasses.size(); q++) {
                    msg += DF.format(totalPercentFilteredGenreConfusion[i][q][r] / (double)((Integer)genreClassSizes.get(q)).intValue()) + "\t";
                }
                msg += "\n";
            }
        }
        msg += "\n";
        
        msg += "---\nMean filtered genre accuracy:\n\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < genreClasses.size(); j++) {
                sum += totalPercentFilteredGenreConfusion[i][j][j] / (double)((Integer)genreClassSizes.get(j)).intValue();
            }
            msg += "At " + TEST_LEVELS[i] + " results: " + (sum / (double)genreClasses.size());
        }
        msg += "\n";
        
        msg += "---\nNormalised Artist Filtered Genre Neighbourhood confusion:\n\n";
        msg += "Classes:\n";
        for (int i = 0; i < genreClasses.size(); i++) {
            msg += i + ":\t" + (String)genreClasses.get(i) + "\n";
        }
        msg += "\n";
        
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "At " + TEST_LEVELS[i] + " results\n";
            msg += "q:\t";
            for (int j = 0; j < genreClasses.size(); j++) {
                msg += "  " + j + "  \t";
            }
            msg += "\nr:\n";
            for (int r = 0; r < genreClasses.size(); r++) {
                msg += r + ":\t";
                for (int q = 0; q < genreClasses.size(); q++) {
                    msg += DF.format(totalPercentFilteredGenreAvailConfusion[i][q][r] / (double)((Integer)genreClassSizes.get(q)).intValue()) + "\t";
                }
                msg += "\n";
            }
        }
        msg += "\n";
        
        msg += "---\nMean Normalised filtered genre accuracy:\n\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < genreClasses.size(); j++) {
                sum += totalPercentFilteredGenreAvailConfusion[i][j][j] / (double)((Integer)genreClassSizes.get(j)).intValue();
            }
            msg += "At " + TEST_LEVELS[i] + " results: " + (sum / (double)genreClasses.size());
        }
        msg += "\n";
        
        System.out.println(msg);
        
        //write report to disk
        if (storageDirForReportAndPlots != null){
            BufferedWriter outputWriter = null;

            File resultFile = new File(storageDirForReportAndPlots.getPath() + File.separator + "filtGenreReport.txt");
            try {
                try {
                    //use buffering
                    outputWriter = new BufferedWriter( new FileWriter(resultFile) );
                    outputWriter.write( msg );
                    outputWriter.flush();
                }catch(IOException ioe){
                    System.out.println("IOException prevented writing of report to disk!\n" + ioe);
                }
                finally {
                    //flush and close both "output" and its underlying FileWriter
                    if (outputWriter != null){
                        outputWriter.close();
                    }
                }
            } catch (IOException ex) {
                System.out.println("IOException ocurred when closing output stream!\n" + ex);
                ex.printStackTrace();
            }
        }
        
    }
    
    
    /**
     * Counts the number of times each file was similar to a query and produces
     * plots. 
     *
     * @param retriever An implementation of the RetrieverInterface to be evaluated.
     * @param storageDirForReportAndPlots The directory to write reports and plots to..
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found in Signal Objects being evaluated on.
     */
    public void countTimesSimilar(RetrieverInterface retriever, File storageDirForReportAndPlots) throws noMetadataException {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String reportTime = calendar.getTime().toString();
        
        final int[] TEST_LEVELS = {5, 10, 20, 50};
        int currentTestLevel = 0;
        int maxTestLevel = TEST_LEVELS[TEST_LEVELS.length-1];
        
        List Signals = Arrays.asList(retriever.getSignals());
        
        //create a file location to index hashmap to speed up times similar file search
        HashMap<String,Integer> fileLoc2Index = new HashMap<String,Integer>(Signals.size());
        for (int i = 0; i < Signals.size(); i++) {
            fileLoc2Index.put(((Signal)Signals.get(i)).getStringMetadata(Signal.PROP_FILE_LOCATION), new Integer(i));
        }
        
        //variables for counting number of times a piece appeared in a result list
        int[][] timesSeen = new int[TEST_LEVELS.length][this.size()];
        
        //create result dir
        if (storageDirForReportAndPlots != null){
        
            if (storageDirForReportAndPlots.exists()){
                if (!storageDirForReportAndPlots.isDirectory())
                {
                    throw new RuntimeException("unable to create evaluation output dir as it already exists as a file!");
                }
            }else{
                storageDirForReportAndPlots.mkdirs();
            }
        }
        
        System.out.println("Counting # times each file was similar to a query");
        
        //count matches on each metadata ignoring self retrieval
        for (int i = 0; i < Signals.size(); i++) {
            System.out.println("evaluating on " + i + " of " + Signals.size());
            //reset test level counter
            currentTestLevel = 0;
            //retrieve results
            SearchResult[] similar = retriever.retrieveNMostSimilar((Signal)Signals.get(i), maxTestLevel+1);
            
            boolean retrievedSelf = false;
            
            //measure neighbourhood distances
            for (int k = 0; k < maxTestLevel+1; k++) {
                //if on last result of the current test level
                if (k == TEST_LEVELS[currentTestLevel]) {
                    
                    //collect last result if retriever also retrieved query
                    if(retrievedSelf){
                        
                        int idx = -1;//= Signals.indexOf(similar[k].getTheResult());
                        //File loc = new File(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION));
                        idx = (fileLoc2Index.get(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION))).intValue();
                        
                        /*for (int sourceArtistIndex = 0; sourceArtistIndex < Signals.size(); sourceArtistIndex++) {
                            File testLoc = new File(((Signal)Signals.get(sourceArtistIndex)).getStringMetadata(Signal.PROP_FILE_LOCATION));
                            if (loc.equals(testLoc))
                            {
                                idx = sourceArtistIndex;
                                break;
                            }
                        }*/
                        
                        for (int j = currentTestLevel; j < TEST_LEVELS.length; j++) {
                            if (idx == -1){
                                System.out.println("A result  (" +  similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION) + ") was not found in the evaluation database (" + Signals.size() + " files)!");
                                break;
                            }else{
                                timesSeen[j][idx]++;
                            }
                        }
                    }
                    if (currentTestLevel >= (TEST_LEVELS.length - 1)) {
                        //we're done!
                        break;
                    }
                    //increment test level
                    currentTestLevel++;
                }
                else{
                    //catch retrieval of query
                    if(similar[k].getTheResult().equals((Signal)Signals.get(i))) {
                        retrievedSelf = true;
                    }else{
                        int idx = -1;//= Signals.indexOf(similar[k].getTheResult());
                        //File loc = new File(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION));
                        idx = (fileLoc2Index.get(similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION))).intValue();
                        
                        /*for (int sourceArtistIndex = 0; sourceArtistIndex < Signals.size(); sourceArtistIndex++) {
                            File testLoc = new File(((Signal)Signals.get(sourceArtistIndex)).getStringMetadata(Signal.PROP_FILE_LOCATION));
                            if (loc.equals(testLoc))
                            {
                                idx = sourceArtistIndex;
                                break;
                            }
                        }*/
                        
                        //record times seen
                        for (int j = currentTestLevel; j < TEST_LEVELS.length; j++) { 
                            if (idx == -1){
                                System.out.println("A result (" + similar[k].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION) + ") was not found in the evaluation database (" + Signals.size() + " files)!");
                            }else{
                                timesSeen[j][idx]++;
                            }
                        }
                    }
                }
            }
            
        }
        
        System.out.println("Counting number of examples always and never similar");
        
        //Calculate % examples never similar and # examples similar
        double[] neverSimilar = new double[TEST_LEVELS.length];
        int[] alwaysSimilar = new int[TEST_LEVELS.length];
        for (int k = 0; k < TEST_LEVELS.length; k++) {
            int neverSimilarCount = 0;
            int max = 0;
            for (int i = 0; i < Signals.size(); i++) {
                if (timesSeen[k][i] == 0)
                {
                    neverSimilarCount++;
                }else if(timesSeen[k][i] > max)
                {
                    max = timesSeen[k][i];
                }
            }
            neverSimilar[k] = (double)neverSimilarCount / (double)Signals.size();
            alwaysSimilar[k] = max;
        }
        
        //Write evaluation report
        System.out.println("Outputting...");
        
        String msg = "Retriever: " + retriever.getRetrieverName() + "\n";
        
        msg += "Report time stamp: " + reportTime + "\n\n";
        
        msg += "\n---\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "% of files never similar at " + TEST_LEVELS[i] + " results:\t" + neverSimilar[i] + "\n";
        }
        msg += "---\n";
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            msg += "Maximum number of times a song was similar at " + TEST_LEVELS[i] + " results:\t" + alwaysSimilar[i] + "\n";
        }
        
        System.out.println(msg);
        
        //write report to disk
        if (storageDirForReportAndPlots != null){
            BufferedWriter outputWriter = null;

            File resultFile = new File(storageDirForReportAndPlots.getPath() + File.separator + "times_similar_report.txt");
            try {
                try {
                    //use buffering
                    outputWriter = new BufferedWriter( new FileWriter(resultFile) );
                    outputWriter.write( msg );
                    outputWriter.flush();
                }catch(IOException ioe){
                    System.out.println("IOException prevented writing of report to disk!\n" + ioe);
                }
                finally {
                    //flush and close both "output" and its underlying FileWriter
                    if (outputWriter != null){
                        outputWriter.close();
                    }
                }
            } catch (IOException ex) {
                System.out.println("IOException ocurred when closing output stream!\n" + ex);
                ex.printStackTrace();
            }
        }
        
        //produce times similar plots
        ResultCurvePlot[] plots = new ResultCurvePlot[TEST_LEVELS.length];
        for (int i = 0; i < TEST_LEVELS.length; i++) {
            plots[i] = new ResultCurvePlot(timesSeen[i], retriever.getRetrieverName() + " # times similar plot - top " + TEST_LEVELS[i]);
            if (storageDirForReportAndPlots != null){
                File aPlotFile = new File(storageDirForReportAndPlots.getPath() + File.separator + "timesSimilarPlot_" + TEST_LEVELS[i] + ".png");
                try {
                    //save to PNG files
                    plots[i].writeImageToPNG(aPlotFile);
                } catch (IOException ex) {
                    System.out.println("IOException prevented saving of plot to disk!\n" + ex);
                    ex.printStackTrace();
                }
            }
        }
        //Display plots if possible
        /*for (int sourceFold = 0; sourceFold < TEST_LEVELS.length; sourceFold++) {
            JFrame theFrame = new JFrame(plots[sourceFold].getName());
            theFrame.add(plots[sourceFold]);
            theFrame.setSize(plots[sourceFold].getWidth() + 20, plots[sourceFold].getHeight() + 40);
            theFrame.validate();
            theFrame.setVisible(true);
        }*/
    }
    
    
    
    /**
     * Returns a simple test and training dataset from the database.
     * Classes will be divided in equal proportion (if possible) and filtered by
     * the metadataFilter (if possible). The class metadata will be set to the
     * specified metadata type.
     * @return The test sets which are indexed[type][set] where the types are:
     * 0 - Training set
     * 1 - Test set
     * 2 - unused files
     * @param maxToTakeForEachFilterClass The maximum number of examples to use in each filter class (e.g. use at most 20 examples per artist, discard the rest).
     * @param testMetadata Metadata to base dataset on.
     * @param randomSeed Seed to use for random selection
     * @param proportionOfDB Proportion of database to use
     * @param proportionForTraining Proportion of files chosen to be used for the training set
     * @param metadataFilter the metadata to use to filter the test database, null implies no filter is to be used.
     * items with the same filter metadata will be placed in either the test or the
     * training set and will not divided across them.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal[][] getSimpleTestTrainDataSet(int randomSeed, double proportionOfDB, double proportionForTraining, String testMetadata, String metadataFilter, int maxToTakeForEachFilterClass) throws noMetadataException {
        if ((proportionOfDB > 1)||(proportionOfDB < 0)) {
            throw new RuntimeException("Proportion of DB should be between 0:1");
        }
        if (maxToTakeForEachFilterClass < -1) {
            throw new RuntimeException("maxToTakeForEachFilterClass should be -1 or greater");
        }
        if (testMetadata.equals(metadataFilter)) {
            throw new RuntimeException("Test metadata must be different from filter metadata.");
        }
        
        java.util.Random rnd = new java.util.Random(randomSeed);
        List<String> files = this.getFileNames();
        
        //sort the file list by name
        Collections.sort(files);
        
        if(metadataFilter.equals("")) {//simple random split
            System.out.println("Peforming simple random split of MusicDB (size: " + this.size() + ")");
            ArrayList<Signal> train = new ArrayList<Signal>();
            ArrayList<Signal> test = new ArrayList<Signal>();
            ArrayList<Signal> remainder = new ArrayList<Signal>();
            
            ArrayList<ArrayList<Signal>> classLists = new ArrayList<ArrayList<Signal>>();
            List<String> classNameList = null;
            HashMap<String,ArrayList<Signal>> classIndex = null;
            if(this.indexingMetadata(testMetadata)){
                classNameList = this.getMetadataClasses(testMetadata);
                //sort class list by name
                Collections.sort(classNameList);
                classIndex = this.indexKeyToIndexMap.get(testMetadata);
                for (int i = 0; i < classNameList.size(); i++) {
                    classLists.add(classIndex.get(classNameList.get(i)));
                }
            }else{//unindexed metadata
                String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
                String metas = "";
                for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                    metas += "\t" + metaKeys[i] + "\n";
                }
                throw new noMetadataException("A test set on an unindexed metadata type was requested!\n Set requested for: " + testMetadata + "\n\tindexed metadatas: \n" + metas);
            }
            
            //int numFiles = this.size();
            int[] numToUse = new int[classLists.size()];
            int[] numToTrain = new int[classLists.size()];
            int[] numToTest = new int[classLists.size()];
            
            for (int i = 0; i < classLists.size(); i++) {
                numToUse[i] = (int)Math.round((((ArrayList)classLists.get(i)).size())  * proportionOfDB);
                numToTrain[i] = (int)((double)numToUse[i] * proportionForTraining);
                numToTest[i] = numToUse[i] - numToTrain[i];
            }
            
            int[] numUsedTrain = new int[classLists.size()];
            int[] numUsedTest = new int[classLists.size()];
            for (int i = 0; i < numToUse.length; i++) {
                ArrayList<Signal> aClass = classLists.get(i);
                ArrayList<Signal> copyAClass = new ArrayList<Signal>();
                copyAClass.addAll(aClass);
                //sort by file locaitonmetadata
                Collections.sort((List<Signal>)copyAClass);
                while((numUsedTrain[i] < numToTrain[i])&&(copyAClass.size() > 0)){
                    Signal aSig = copyAClass.remove(rnd.nextInt(copyAClass.size()));
                    aSig.setMetadata(Signal.PROP_CLASS,aSig.getStringMetadata(testMetadata));
                    
                    train.add(aSig);
                    numUsedTrain[i]++;
                }
                while((numUsedTest[i] < numToTest[i])&&(copyAClass.size() > 0)){
                    Signal aSig = copyAClass.remove(rnd.nextInt(copyAClass.size()));
                    aSig.setMetadata(Signal.PROP_CLASS,aSig.getStringMetadata(testMetadata));
                    test.add(aSig);
                    numUsedTest[i]++;
                }
                for (int j = 0; j < copyAClass.size(); j++) {
                    remainder.add(copyAClass.get(j));
                }
            }
            Signal[][] out = new Signal[3][];
            out[0] = train.toArray(new Signal[train.size()]);
            out[1] = test.toArray(new Signal[test.size()]);
            out[2] = remainder.toArray(new Signal[remainder.size()]);
            System.out.println("Outputting Simple Training and Test set for " + testMetadata);
            System.out.print("\t" + testMetadata + " classes: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(classNameList.get(i) + "\t");
            }
            System.out.println("");
            System.out.println("\tTraining set: " + train.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTrain[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tTesting set: " + test.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTest[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tUnused files: " + remainder.size() + " Signal Objects");
            return out;
        }else{
            System.out.println("Performing " + metadataFilter + "-filtered split of MusicDB (size: " + this.size() + ")");
            ArrayList<Signal> train = new ArrayList<Signal>();
            ArrayList<Signal> test = new ArrayList<Signal>();
            ArrayList<Signal> remainder = new ArrayList<Signal>();
            ArrayList<ArrayList<Signal>> classLists = new ArrayList<ArrayList<Signal>>();
            List<String> classNameList = null;
            HashMap<String,ArrayList<Signal>> classIndex = null;
            
            if(this.indexingMetadata(testMetadata)){
                classNameList = this.getMetadataClasses(testMetadata);
                //sort by class name
                Collections.sort(classNameList);
                classIndex = this.indexKeyToIndexMap.get(testMetadata);
                for (int i = 0; i < classNameList.size(); i++) {
                    ArrayList<Signal> copyAClass = new ArrayList<Signal>();
                    ArrayList<Signal> aClass = classIndex.get(classNameList.get(i));
                    copyAClass.addAll(aClass);
                    //sort by file location metadata
                    
                    Collections.sort(copyAClass);
                    classLists.add(copyAClass);
                }
            }else{//unindexed metadata
                ArrayList metaKeys =  (ArrayList)this.getIndexedMetadatas();
                String metas = "";
                if(metaKeys.size() == 0) {
                    metas = "\t<none>";
                }else{
                    for (int i = 0; i < metaKeys.size(); i++) {
                        metas += "\t" + metaKeys.get(i) + "\n";
                    }
                }
                
                throw new noMetadataException("A test and training set on an unindexed metadata type was requested!\n Set requested for: " + testMetadata + "\n\tindexed metadatas: \n" + metas);
            }
            
            //int numFiles = this.size();
            int[] numToUse = new int[classLists.size()];
            int[] numToTrain = new int[classLists.size()];
            int[] numToTest = new int[classLists.size()];
            
            for (int i = 0; i < classLists.size(); i++) {
                numToUse[i] = (int)Math.round((((ArrayList)classLists.get(i)).size())  * proportionOfDB);
                numToTrain[i] = (int)((double)numToUse[i] * proportionForTraining);
                numToTest[i] = numToUse[i] - numToTrain[i];
            }
            
            int[] numUsedTrain = new int[classLists.size()];
            int[] numUsedTest = new int[classLists.size()];
            for (int i = 0; i < numToUse.length; i++) {
                //get list for main class
                ArrayList<Signal> aClass = classLists.get(i);
                
                
                //randomly select a class from filter list and add all examples to train set
                HashMap<String,ArrayList<Signal>> filterIndex = null;
                if(this.indexingMetadata(metadataFilter)){
                    filterIndex = this.indexKeyToIndexMap.get(metadataFilter);
                }else{//unindexed metadata
                    String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
                    String metas = "";
                    for (int j = 0; j < this.indexKeyToIndexMap.size(); j++) {
                        metas += "\t" + metaKeys[j] + "\n";
                    }
                    throw new noMetadataException("Filtering of a test and training set on an unindexed metadata type was requested!\n Filtering requested for: " + metadataFilter + "\n\tindexed metadatas: \n" + metas);
                }
                
                while((numUsedTrain[i] < numToTrain[i])&&(aClass.size() > 0)) {
                    Signal theSig = aClass.get(rnd.nextInt(aClass.size()));
                    String filterClass = this.getMetadataClassForSignal(metadataFilter,theSig);
                    if((filterClass == null)||(filterClass.trim().equals(""))||(filterClass.trim().toLowerCase().startsWith("various"))){
                        
                        aClass.remove(theSig);
                        theSig.setMetadata(Signal.PROP_CLASS,theSig.getMetadata(testMetadata));
                        train.add(theSig);
                        numUsedTrain[i]++;

                    }else{
                        ArrayList<Signal> filterList = filterIndex.get(filterClass);
                        /*if (Math.min(filterList.size(),maxToTakeForEachFilterClass) > numToTrain[sourceFold] - numUsedTrain[sourceFold])
                        {//skip as it will take us over the needed amount
                            //have one more go then quit
                            theSig = (Signal)aClass.get(rnd.nextInt(aClass.size()));
                            filterClass = this.getMetadataClassForSignal(metadataFilter,theSig);
                            filterList = (ArrayList)filterIndex.get(filterClass);
                            if (Math.min(filterList.size(),maxToTakeForEachFilterClass) > numToTrain[sourceFold] - numUsedTrain[sourceFold])
                            {//quit
                                break;
                            }
                        }*/
                        //sort b y file location metada
                        Collections.sort(filterList);
                        if (filterList.size() > maxToTakeForEachFilterClass){
                            System.out.println("\tdiscarding " + (filterList.size() - maxToTakeForEachFilterClass) + " files from filter class '" + filterClass + "'");
                        }
                        for (int j = 0; j < filterList.size(); j++) {
                            if (j<maxToTakeForEachFilterClass) {
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        String testClass = aSig.getStringMetadata(testMetadata);
                                        aSig.setMetadata(Signal.PROP_CLASS,testClass);
                                        train.add(aSig);
                                        
                                        numUsedTrain[classNameList.indexOf(testClass)]++;
                                        break;
                                    }
                                }
                            }else{//throw away, too many for this artist
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        remainder.add(aSig);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                }
                //done training files
                if (aClass.size() == 0){
                    System.out.println("WARNING: class " + classNameList.get(i) + " was empty before test data was selected");
                }else{
                    while((numUsedTest[i] < numToTest[i])&&(aClass.size() > 0)) {
                        Signal theSig = aClass.get(rnd.nextInt(aClass.size()));
                        Object filterClass = this.getMetadataClassForSignal(metadataFilter,theSig);
                        if((filterClass == null)||(((String)filterClass).trim().equals(""))||(((String)filterClass).trim().toLowerCase().startsWith("various"))){
                            
                            aClass.remove(theSig);
                            theSig.setMetadata(Signal.PROP_CLASS,theSig.getMetadata(testMetadata));
                            test.add(theSig);
                            numUsedTest[i]++;
                            
                        }else{
                            ArrayList<Signal> filterList = filterIndex.get(filterClass);
                            Collections.sort(filterList);
                            for (int j = 0; j < filterList.size(); j++) {
                                //if (copyAClass.contains(filterList.get(sourceArtistIndex)))
                                //{
                                //    Signal aSig = (Signal)aClass.remove(aClass.indexOf(filterList.get(sourceArtistIndex)));
                                //    aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                //    test.add(aSig);
                                //    numUsedTest[classNameList.indexOf(aSig.getMetadata(testMetadata))]++;
                                //}
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        
                                        
                                        String testClass = aSig.getStringMetadata(testMetadata);
                                        aSig.setMetadata(Signal.PROP_CLASS,testClass);
                                        test.add(aSig);
                                        numUsedTest[classNameList.indexOf(testClass)]++;
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
                for (int j = 0; j < aClass.size(); j++) {
                    remainder.add(aClass.get(j));
                }
                
            }
            Signal[][] out = new Signal[3][];
            out[0] = train.toArray(new Signal[train.size()]);
            out[1] = test.toArray(new Signal[test.size()]);
            out[2] = remainder.toArray(new Signal[remainder.size()]);
            System.out.println("Outputting Simple Training and Test set for " + testMetadata + " filtered by " + metadataFilter);
            System.out.print("\t" + testMetadata + " classes: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(classNameList.get(i) + "\t");
            }
            System.out.println("");
            System.out.println("\tTraining set: " + train.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTrain[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tTesting set: " + test.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTest[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tUnused files: " + remainder.size() + " Signal Objects");
            
            
            System.out.println("Target sizes:");
            
            System.out.println("\tTotal number to use for each class: ");
            System.out.print("\tclass breakdown: ");
//            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numToUse[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tTraining set: ");
            System.out.print("\tclass breakdown: ");
//            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numToTrain[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tTesting set: ");
            System.out.print("\tclass breakdown: ");
//            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numToTest[i] + "\t");
            }
            System.out.println("");
            
            
            return out;
        }
        
    }
    
    
    public Signal[][] getCrossValidationFoldsForTags(int numFolds, long randomSeed, int maxNumAdjustments) throws noMetadataException {
        if (!this.indexingMetadata(Signal.PROP_TAGS)) {
            throw new RuntimeException("Not indexing " + Signal.PROP_TAGS + " metadata!");
        }
        if (!this.indexingMetadata(Signal.PROP_ARTIST)) {
            throw new RuntimeException("Not indexing " + Signal.PROP_ARTIST + " metadata!");
        }
        
        List<String> tags = this.getMetadataClasses(Signal.PROP_TAGS);
        List<String> artists = this.getMetadataClasses(Signal.PROP_ARTIST);
        HashMap<String, int[]> artistToTagCountArray = new HashMap<String, int[]>();
        
        int[] totalTagCountArr = new int[tags.size()];
        String aTag, anArtist;
        for (Iterator<String> it = artists.iterator(); it.hasNext();) {
            anArtist = it.next();
            int[] tagCountArr = new int[tags.size()];
            List<Signal> artistsTracks = this.getSignalListForMetadataClass(Signal.PROP_ARTIST, anArtist);
            for (Iterator<Signal> it2 = artistsTracks.iterator(); it2.hasNext();) {
                Signal signal = it2.next();
                HashSet<String> signalTags = getMetadataClassesForSignal(Signal.PROP_TAGS,signal);
                for (Iterator<String> it3 = signalTags.iterator(); it3.hasNext();) {
                    int tagIdx = tags.indexOf(it3.next());
                    tagCountArr[tagIdx]++;
                    totalTagCountArr[tagIdx]++;
                }
                
            }
            artistToTagCountArray.put(anArtist, tagCountArr);
        }

        ArrayList<String>[] foldArtists = new ArrayList[numFolds];
        ArrayList<int[]>  foldTagCounts = new ArrayList<int[]>();
        for (int i = 0; i < numFolds; i++) {
            foldArtists[i] = new ArrayList<String>();
            foldTagCounts.add(new int[tags.size()]);
        }
        
        //randomly assign artists to folds
        Random rnd = new Random(randomSeed);
        int currFold = 0;
        while(artists.size() > 0){
            anArtist = artists.remove(rnd.nextInt(artists.size()));
            foldArtists[currFold].add(anArtist);
            int[] tagCountArr = artistToTagCountArray.get(anArtist);
            for (int i = 0; i < tagCountArr.length; i++) {
                foldTagCounts.get(currFold)[i] += tagCountArr[i];
            }
            currFold++;
            if (currFold == numFolds){
                currFold = 0;
            }
        }
        
        System.out.println("optimising split proportions...");
        double currentDeviation = measureMaxDeviationFromIdeal(numFolds, foldTagCounts, totalTagCountArr);
        double newDeviaton;
        double currentEntropy = measureEntropyOfSplit(numFolds, foldTagCounts);
        double newEntropy;
        
        
        //integrate entropy into the split eval
        
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(4);
        boolean done = false;
        for (int i = 0; i < maxNumAdjustments; i++) {
            if (i % 1 == 0){
                System.out.println("deviation (from ideal proportions) at iteration " + i + ": " +dec.format(currentDeviation) + ", entropy: " + dec.format(currentEntropy));
            }
            double[] results = tryMovingArtist(foldArtists, artistToTagCountArray, foldTagCounts, totalTagCountArr, currentDeviation, currentEntropy, tags.size());
            if (results[0] < currentDeviation){
                currentDeviation = results[0];
                currentEntropy = results[1];
            }else if(results[1] > currentEntropy){
                currentDeviation = results[0];
                currentEntropy = results[1];
            }else{
                //TODO: try swapping artists 
                results = trySwappingArtist(foldArtists, artistToTagCountArray, foldTagCounts, totalTagCountArr, currentDeviation, currentEntropy, tags.size());
                if (results[0] < currentDeviation){
                    currentDeviation = results[0];
                    currentEntropy = results[1];
                }else if(results[1] > currentEntropy){
                    currentDeviation = results[0];
                    currentEntropy = results[1];
                }else{
                    done = true;
                    System.out.println("maxima reached at deviation: " + dec.format(currentDeviation) + ", entropy: " + dec.format(currentEntropy));
                    break;
                }
            }
        }
        if (!done){
            System.out.println("Stopped trying to improve split at deviation (from ideal proportions): " + dec.format(currentDeviation));
        }
        
        ArrayList<Signal>[] folds = new ArrayList[numFolds];
        for (int i = 0; i < folds.length; i++) {
            folds[i] = new ArrayList<Signal>();
            for (Iterator<String> it = foldArtists[i].iterator(); it.hasNext();) {
                folds[i].addAll(this.getSignalListForMetadataClass(Signal.PROP_ARTIST, it.next()));
            }
        }
        
        Signal[][] theSplit = new Signal[numFolds][];
        for (int i = 0; i < theSplit.length; i++) {
            theSplit[i] = folds[i].toArray(new Signal[folds[i].size()]);
        }
        
        System.out.println("Outputting " + numFolds + "-fold cross validated tag sets");
        System.out.print("\t" + tags.size() + " tags: \n");
        for (int i = 0; i < tags.size(); i++) {
            System.out.print(tags.get(i) + "\t");
        }
        System.out.println("");
        for (int i = 0; i < theSplit.length; i++) {
            System.out.println("\tfold " + i + ": " + theSplit[i].length + " Signal Objects");
            System.out.print("\tproportion of examples used: ");
            for (int t = 0; t < tags.size(); t++) {
                System.out.print(dec.format(((float)foldTagCounts.get(i)[t] / (float)totalTagCountArr[t])) + "\t");
            }
            System.out.println("\n");
        }
        System.out.println("");

        return theSplit;
    }
    
    public Signal[][] getCrossValidationFoldsForClassification(int numFolds, long randomSeed, int maxNumAdjustments) throws noMetadataException {
        if (!this.indexingMetadata(Signal.PROP_CLASS)) {
            throw new RuntimeException("Not indexing " + Signal.PROP_CLASS + " metadata!");
        }
        if (!this.indexingMetadata(Signal.PROP_ARTIST)) {
            throw new RuntimeException("Not indexing " + Signal.PROP_ARTIST + " metadata!");
        }
        
        List<String> classes = this.getMetadataClasses(Signal.PROP_CLASS);
        List<String> artists = this.getMetadataClasses(Signal.PROP_ARTIST);
        HashMap<String, int[]> artistToClassCountArray = new HashMap<String, int[]>();
        
        int[] totalClassCountArr = new int[classes.size()];
        String aClass, anArtist;
        for (Iterator<String> it = artists.iterator(); it.hasNext();) {
            anArtist = it.next();
            int[] classCountArr = new int[classes.size()];
            List<Signal> artistsTracks = this.getSignalListForMetadataClass(Signal.PROP_ARTIST, anArtist);
            for (Iterator<Signal> it2 = artistsTracks.iterator(); it2.hasNext();) {
                Signal signal = it2.next();
                String signalClass = getMetadataClassForSignal(Signal.PROP_CLASS,signal);
                int classIdx = classes.indexOf(signalClass);
                classCountArr[classIdx]++;
                totalClassCountArr[classIdx]++;
            }
            artistToClassCountArray.put(anArtist, classCountArr);
        }

        ArrayList<String>[] foldArtists = new ArrayList[numFolds];
        ArrayList<int[]>  foldClassCounts = new ArrayList<int[]>();
        for (int i = 0; i < numFolds; i++) {
            foldArtists[i] = new ArrayList<String>();
            foldClassCounts.add(new int[classes.size()]);
        }
        
        //pseudo-randomly assign artists to folds
        Random rnd = new Random(randomSeed);
        int currFold = 0;
        while(artists.size() > 0){
            anArtist = artists.remove(rnd.nextInt(artists.size()));
            int[] classCountArr = artistToClassCountArray.get(anArtist);
            
            int artistBiggestClass = 0;
            int biggestCount = classCountArr[0];
            for (int i = 1; i < classCountArr.length; i++) {
                if(classCountArr[i] > biggestCount){
                    biggestCount = classCountArr[i];
                    artistBiggestClass = i;
                }
            }
            
            int foldSmallestCount = 0;
            int smallestCount = foldClassCounts.get(0)[artistBiggestClass];
            for (int i = 0; i < foldClassCounts.size(); i++) {
                if (foldClassCounts.get(i)[artistBiggestClass] < smallestCount){
                    smallestCount = foldClassCounts.get(i)[artistBiggestClass];
                    foldSmallestCount = i;
                }
            }
            
            foldArtists[foldSmallestCount].add(anArtist);
            
            for (int i = 0; i < classCountArr.length; i++) {
                foldClassCounts.get(currFold)[i] += classCountArr[i];
            }
            currFold++;
            if (currFold == numFolds){
                currFold = 0;
            }
        }
        
        System.out.println("optimising split proportions...");
        double currentDeviation = measureMaxDeviationFromIdeal(numFolds, foldClassCounts, totalClassCountArr);
        
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(4);
        boolean done = false;
        for (int i = 0; i < maxNumAdjustments; i++) {
            if (i % 1 == 0){
                System.out.println("deviation (from ideal proportions) at iteration " + i + ": " +dec.format(currentDeviation));
            }
            double result = tryMovingArtist(foldArtists, artistToClassCountArray, foldClassCounts, totalClassCountArr, currentDeviation, classes.size());
            if (result < currentDeviation){
                currentDeviation = result;
            }else{
                //TODO: try swapping artists 
                result = trySwappingArtist(foldArtists, artistToClassCountArray, foldClassCounts, totalClassCountArr, currentDeviation, classes.size());
                if (result < currentDeviation){
                    currentDeviation = result;
                }else{
                    done = true;
                    System.out.println("maxima reached at deviation: " + dec.format(currentDeviation));
                    break;
                }
            }
        }
        if (!done){
            System.out.println("Stopped trying to improve split at deviation (from ideal proportions): " + dec.format(currentDeviation));
        }
        
        
        ArrayList<Signal>[] folds = new ArrayList[numFolds];
//        if (currentDeviation > ((1.0 / numFolds) / 2.0)){
//            System.out.println("...cross-validaiton was not succcessful - attempting to split one artist across folds");
//            //we'return not getting close - time to give up on the artist filter...
//            String worstArtist = findArtistToSplit(foldArtists, artistToClassCountArray, foldClassCounts, totalClassCountArr, classes.size());
//            System.out.println("...worst artist: '" + worstArtist + "'");
//            int foldClipped = -1;
//            for (int i = 0; i < folds.length; i++) {
//                folds[i] = new ArrayList<Signal>();
//                for (Iterator<String> it = foldArtists[i].iterator(); it.hasNext();) {
//                    String artist = it.next();
//                    if (artist.equals(worstArtist)){
//                        foldClipped = i;
//                    }else{
//                        folds[i].addAll(this.getSignalListForMetadataClass(Signal.PROP_ARTIST, artist));
//                    }
//                }
//            }
//            System.out.println("...worst artist was in fold: " + foldClipped);
//            
//            int[] artistCounts = artistToClassCountArray.get(worstArtist);
//            int[] clippedFoldCount = foldClassCounts.get(foldClipped);
//            for (int i = 0; i < clippedFoldCount.length; i++) {
//                clippedFoldCount[i] -= artistCounts[i];
//            }
//            
//            List<Signal> artistList = this.getSignalListForMetadataClass(Signal.PROP_ARTIST, worstArtist);
//            int fold = 0;
//            for (Iterator<Signal> it = artistList.iterator(); it.hasNext();) {
//                Signal sig = it.next();
//                folds[fold].add(sig);
//                int classIdx = classes.indexOf(this.getMetadataClassForSignal(Signal.PROP_CLASS, sig));
//                foldClassCounts.get(fold)[classIdx]++;
//                fold++;
//                if (fold == numFolds){
//                    fold = 0;
//                }
//            }
//            
//        }else{
            for (int i = 0; i < folds.length; i++) {
                folds[i] = new ArrayList<Signal>();
                for (Iterator<String> it = foldArtists[i].iterator(); it.hasNext();) {
                    folds[i].addAll(this.getSignalListForMetadataClass(Signal.PROP_ARTIST, it.next()));
                }
            }
//        }
        
        
        
        Signal[][] theSplit = new Signal[numFolds][];
        for (int i = 0; i < theSplit.length; i++) {
            theSplit[i] = folds[i].toArray(new Signal[folds[i].size()]);
        }
        
        System.out.println("Outputting " + numFolds + "-fold cross validated datasets");
        System.out.print("\t" + classes.size() + " classes: \n");
        for (int i = 0; i < classes.size(); i++) {
            System.out.print(classes.get(i) + "\t");
        }
        System.out.println("");
        for (int i = 0; i < theSplit.length; i++) {
            System.out.println("\tfold " + i + ": " + theSplit[i].length + " Signal Objects");
            System.out.print("\tproportion of examples used: ");
            for (int t = 0; t < classes.size(); t++) {
                System.out.print(dec.format(((float)foldClassCounts.get(i)[t] / (float)totalClassCountArr[t])) + "\t");
            }
            System.out.println("\n");
        }
        System.out.println("");

        return theSplit;
    }
    
    private double[] tryMovingArtist(ArrayList<String>[] foldArtists, HashMap<String, int[]> artistToTagCountArray, ArrayList<int[]> foldTagCounts, int[] totalTagCountArr, double currentDeviation, double currentEntropy, int numTags){
         //basic brute force search for best move
        int bestSourceFold = -1;
        int bestArtist = -1;
        int bestDestinationFold = -1;
        
        double bestDeviation = currentDeviation;
        double currDeviation;
        double bestEntropy = currentEntropy;
        double currEntropy;
        
        int[] sourceFoldArr, destinationFoldArr;
        int[] removedFoldTagCounts, addedFoldTagCounts;
        int[] artistCounts;
        int numFolds = foldArtists.length;
        ArrayList<int[]> tempFoldTagCounts;
                
        for (int sourceFold = 0; sourceFold < numFolds; sourceFold++) {
            for (int artistIndex = 0; artistIndex < foldArtists[sourceFold].size(); artistIndex++) {
                removedFoldTagCounts = new int[numTags];
                artistCounts = artistToTagCountArray.get(foldArtists[sourceFold].get(artistIndex));
                sourceFoldArr = foldTagCounts.get(sourceFold);
                for (int i = 0; i < numTags; i++) {
                    removedFoldTagCounts[i] = sourceFoldArr[i] - artistCounts[i];
                }
                
                for (int destinationFold = 0; destinationFold < numFolds; destinationFold++) {
                    if(destinationFold != sourceFold){
                        tempFoldTagCounts = (ArrayList<int[]>)foldTagCounts.clone();
                        tempFoldTagCounts.set(sourceFold, removedFoldTagCounts);
                        addedFoldTagCounts = new int[numTags];
                        destinationFoldArr = foldTagCounts.get(destinationFold);
                        for (int i = 0; i < numTags; i++) {
                            addedFoldTagCounts[i] = destinationFoldArr[i] + artistCounts[i];
                        }
                        tempFoldTagCounts.set(destinationFold, addedFoldTagCounts);
                        currDeviation = measureMaxDeviationFromIdeal(numFolds, tempFoldTagCounts, totalTagCountArr);
                        currEntropy = measureEntropyOfSplit(numFolds, tempFoldTagCounts);
                        if(currDeviation < bestDeviation){
                            bestDeviation = currDeviation;
                            bestSourceFold = sourceFold;
                            bestDestinationFold = destinationFold;
                            bestArtist = artistIndex;
                            bestEntropy = currEntropy;
                        }else if(((currDeviation - bestDeviation) < 0.01) && (currEntropy > bestEntropy)){
                            bestDeviation = currDeviation;
                            bestSourceFold = sourceFold;
                            bestDestinationFold = destinationFold;
                            bestArtist = artistIndex;
                            bestEntropy = currEntropy;
                        }
                    }
                }
            }
        }
        
        //if we improved make changes
        if(bestSourceFold != -1){
            String artist = foldArtists[bestSourceFold].remove(bestArtist);
            artistCounts = artistToTagCountArray.get(artist);
            sourceFoldArr = foldTagCounts.get(bestSourceFold);
            for (int i = 0; i < numTags; i++) {
                sourceFoldArr[i] -= artistCounts[i];
            }
            foldArtists[bestDestinationFold].add(artist);
            destinationFoldArr = foldTagCounts.get(bestDestinationFold);
            for (int i = 0; i < numTags; i++) {
                destinationFoldArr[i] += artistCounts[i];
            }
        }
        
        return new double[]{bestDeviation,bestEntropy};
    }
    
    private double[] trySwappingArtist(ArrayList<String>[] foldArtists, HashMap<String, int[]> artistToTagCountArray, ArrayList<int[]> foldTagCounts, int[] totalTagCountArr, double currentDeviation, double currentEntropy, int numTags){
         //basic brute force search for best move
        int bestSourceFold = -1;
        int bestSourceArtist = -1;
        int bestDestinationFold = -1;
        int bestDestinationArtist = -1;
        
        double bestDeviation = currentDeviation;
        double currDeviation;
        double bestEntropy = currentEntropy;
        double currEntropy;
        
        int[] sourceFoldArr, destinationFoldArr;
        int[] removedSourceFoldTagCounts, addedSourceFoldTagCounts, addedDestinationFoldTagCounts;
        int[] sourceArtistCounts, destinationArtistCounts;
        int numFolds = foldArtists.length;
        ArrayList<int[]> tempFoldTagCounts;
                
        for (int sourceFold = 0; sourceFold < numFolds-1; sourceFold++) {
            for (int sourceArtistIndex = 0; sourceArtistIndex < foldArtists[sourceFold].size(); sourceArtistIndex++) {
                removedSourceFoldTagCounts = new int[numTags];
                sourceArtistCounts = artistToTagCountArray.get(foldArtists[sourceFold].get(sourceArtistIndex));
                sourceFoldArr = foldTagCounts.get(sourceFold);
                for (int i = 0; i < numTags; i++) {
                    removedSourceFoldTagCounts[i] = sourceFoldArr[i] - sourceArtistCounts[i];
                }
                
                for (int destinationFold = sourceFold+1; destinationFold < numFolds; destinationFold++) {
                    for (int destinationArtistIndex = 0; destinationArtistIndex < foldArtists[destinationFold].size(); destinationArtistIndex++) {
                        tempFoldTagCounts = (ArrayList<int[]>)foldTagCounts.clone();
                        destinationFoldArr = foldTagCounts.get(destinationFold);
                        destinationArtistCounts = artistToTagCountArray.get(foldArtists[destinationFold].get(destinationArtistIndex));
                        addedDestinationFoldTagCounts = new int[numTags];
                        addedSourceFoldTagCounts = new int[numTags];
                        for (int i = 0; i < numTags; i++) {
                            addedDestinationFoldTagCounts[i] = destinationFoldArr[i] + (sourceArtistCounts[i] - destinationArtistCounts[i]);
                            addedSourceFoldTagCounts[i] = removedSourceFoldTagCounts[i] + destinationArtistCounts[i];
                        }
                        
                        tempFoldTagCounts.set(sourceFold, addedSourceFoldTagCounts);
                        tempFoldTagCounts.set(destinationFold, addedDestinationFoldTagCounts);
                        
                        currDeviation = measureMaxDeviationFromIdeal(numFolds, tempFoldTagCounts, totalTagCountArr);
                        currEntropy = measureEntropyOfSplit(numFolds, tempFoldTagCounts);
                        if(currDeviation < bestDeviation){
                            bestDeviation = currDeviation;
                            bestSourceFold = sourceFold;
                            bestDestinationFold = destinationFold;
                            bestSourceArtist = sourceArtistIndex;
                            bestDestinationArtist = destinationArtistIndex;
                            bestEntropy = currEntropy;
                        }else if(((currDeviation - bestDeviation) < 0.01) && (currEntropy > bestEntropy)){
                            bestDeviation = currDeviation;
                            bestSourceFold = sourceFold;
                            bestDestinationFold = destinationFold;
                            bestSourceArtist = sourceArtistIndex;
                            bestDestinationArtist = destinationArtistIndex;
                            bestEntropy = currEntropy;
                        }
                        
                        
                    }
                }
            }
        }
        
        //if we improved make changes
        if(bestSourceFold != -1){
            String source_artist = foldArtists[bestSourceFold].remove(bestSourceArtist);
            String destination_artist = foldArtists[bestDestinationFold].remove(bestDestinationArtist);
            foldArtists[bestSourceFold].add(destination_artist);
            foldArtists[bestDestinationFold].add(source_artist);
            
            sourceArtistCounts = artistToTagCountArray.get(source_artist);
            destinationArtistCounts = artistToTagCountArray.get(destination_artist);
            sourceFoldArr = foldTagCounts.get(bestSourceFold);
            destinationFoldArr = foldTagCounts.get(bestDestinationFold);
            for (int i = 0; i < numTags; i++) {
                sourceFoldArr[i] += (destinationArtistCounts[i] - sourceArtistCounts[i]);
                destinationFoldArr[i] += (sourceArtistCounts[i] - destinationArtistCounts[i]);
            }
        }
        
        return new double[]{bestDeviation,bestEntropy};
    }
    
    
    private double tryMovingArtist(ArrayList<String>[] foldArtists, HashMap<String, int[]> artistToTagCountArray, ArrayList<int[]> foldTagCounts, int[] totalTagCountArr, double currentDeviation, int numTags){
         //basic brute force search for best move
        int bestSourceFold = -1;
        int bestArtist = -1;
        int bestDestinationFold = -1;
        
        double bestDeviation = currentDeviation;
        double currDeviation;
        
        int[] sourceFoldArr, destinationFoldArr;
        int[] removedFoldTagCounts, addedFoldTagCounts;
        int[] artistCounts;
        int numFolds = foldArtists.length;
        ArrayList<int[]> tempFoldTagCounts;
                
        for (int sourceFold = 0; sourceFold < numFolds; sourceFold++) {
            for (int artistIndex = 0; artistIndex < foldArtists[sourceFold].size(); artistIndex++) {
                removedFoldTagCounts = new int[numTags];
                artistCounts = artistToTagCountArray.get(foldArtists[sourceFold].get(artistIndex));
                sourceFoldArr = foldTagCounts.get(sourceFold);
                for (int i = 0; i < numTags; i++) {
                    removedFoldTagCounts[i] = sourceFoldArr[i] - artistCounts[i];
                }
                
                for (int destinationFold = 0; destinationFold < numFolds; destinationFold++) {
                    if(destinationFold != sourceFold){
                        tempFoldTagCounts = (ArrayList<int[]>)foldTagCounts.clone();
                        tempFoldTagCounts.set(sourceFold, removedFoldTagCounts);
                        addedFoldTagCounts = new int[numTags];
                        destinationFoldArr = foldTagCounts.get(destinationFold);
                        for (int i = 0; i < numTags; i++) {
                            addedFoldTagCounts[i] = destinationFoldArr[i] + artistCounts[i];
                        }
                        tempFoldTagCounts.set(destinationFold, addedFoldTagCounts);
                        currDeviation = measureMaxDeviationFromIdeal(numFolds, tempFoldTagCounts, totalTagCountArr);
                        if(currDeviation < bestDeviation){
                            bestDeviation = currDeviation;
                            bestSourceFold = sourceFold;
                            bestDestinationFold = destinationFold;
                            bestArtist = artistIndex;
                            
                        }
                    }
                }
            }
        }
        
        //if we improved make changes
        if(bestSourceFold != -1){
            String artist = foldArtists[bestSourceFold].remove(bestArtist);
            artistCounts = artistToTagCountArray.get(artist);
            sourceFoldArr = foldTagCounts.get(bestSourceFold);
            for (int i = 0; i < numTags; i++) {
                sourceFoldArr[i] -= artistCounts[i];
            }
            foldArtists[bestDestinationFold].add(artist);
            destinationFoldArr = foldTagCounts.get(bestDestinationFold);
            for (int i = 0; i < numTags; i++) {
                destinationFoldArr[i] += artistCounts[i];
            }
        }
        
        return bestDeviation;
    }
    
    private double trySwappingArtist(ArrayList<String>[] foldArtists, HashMap<String, int[]> artistToTagCountArray, ArrayList<int[]> foldTagCounts, int[] totalTagCountArr, double currentDeviation, int numTags){
         //basic brute force search for best move
        int bestSourceFold = -1;
        int bestSourceArtist = -1;
        int bestDestinationFold = -1;
        int bestDestinationArtist = -1;
        
        double bestDeviation = currentDeviation;
        double currDeviation;
        
        
        int[] sourceFoldArr, destinationFoldArr;
        int[] removedSourceFoldTagCounts, addedSourceFoldTagCounts, addedDestinationFoldTagCounts;
        int[] sourceArtistCounts, destinationArtistCounts;
        int numFolds = foldArtists.length;
        ArrayList<int[]> tempFoldTagCounts;
                
        for (int sourceFold = 0; sourceFold < numFolds-1; sourceFold++) {
            for (int sourceArtistIndex = 0; sourceArtistIndex < foldArtists[sourceFold].size(); sourceArtistIndex++) {
                removedSourceFoldTagCounts = new int[numTags];
                sourceArtistCounts = artistToTagCountArray.get(foldArtists[sourceFold].get(sourceArtistIndex));
                sourceFoldArr = foldTagCounts.get(sourceFold);
                for (int i = 0; i < numTags; i++) {
                    removedSourceFoldTagCounts[i] = sourceFoldArr[i] - sourceArtistCounts[i];
                }
                
                for (int destinationFold = sourceFold+1; destinationFold < numFolds; destinationFold++) {
                    for (int destinationArtistIndex = 0; destinationArtistIndex < foldArtists[destinationFold].size(); destinationArtistIndex++) {
                        tempFoldTagCounts = (ArrayList<int[]>)foldTagCounts.clone();
                        destinationFoldArr = foldTagCounts.get(destinationFold);
                        destinationArtistCounts = artistToTagCountArray.get(foldArtists[destinationFold].get(destinationArtistIndex));
                        addedDestinationFoldTagCounts = new int[numTags];
                        addedSourceFoldTagCounts = new int[numTags];
                        for (int i = 0; i < numTags; i++) {
                            addedDestinationFoldTagCounts[i] = destinationFoldArr[i] + (sourceArtistCounts[i] - destinationArtistCounts[i]);
                            addedSourceFoldTagCounts[i] = removedSourceFoldTagCounts[i] + destinationArtistCounts[i];
                        }
                        
                        tempFoldTagCounts.set(sourceFold, addedSourceFoldTagCounts);
                        tempFoldTagCounts.set(destinationFold, addedDestinationFoldTagCounts);
                        
                        currDeviation = measureMaxDeviationFromIdeal(numFolds, tempFoldTagCounts, totalTagCountArr);
                        if(currDeviation < bestDeviation){
                            bestDeviation = currDeviation;
                            bestSourceFold = sourceFold;
                            bestDestinationFold = destinationFold;
                            bestSourceArtist = sourceArtistIndex;
                            bestDestinationArtist = destinationArtistIndex;
                            
                        }
                        
                        
                    }
                }
            }
        }
        
        //if we improved make changes
        if(bestSourceFold != -1){
            String source_artist = foldArtists[bestSourceFold].remove(bestSourceArtist);
            String destination_artist = foldArtists[bestDestinationFold].remove(bestDestinationArtist);
            foldArtists[bestSourceFold].add(destination_artist);
            foldArtists[bestDestinationFold].add(source_artist);
            
            sourceArtistCounts = artistToTagCountArray.get(source_artist);
            destinationArtistCounts = artistToTagCountArray.get(destination_artist);
            sourceFoldArr = foldTagCounts.get(bestSourceFold);
            destinationFoldArr = foldTagCounts.get(bestDestinationFold);
            for (int i = 0; i < numTags; i++) {
                sourceFoldArr[i] += (destinationArtistCounts[i] - sourceArtistCounts[i]);
                destinationFoldArr[i] += (sourceArtistCounts[i] - destinationArtistCounts[i]);
            }
        }
        
        return bestDeviation;
    }
    
//    private String findArtistToSplit(ArrayList<String>[] foldArtists, HashMap<String, int[]> artistToTagCountArray, ArrayList<int[]> foldTagCounts, int[] totalTagCountArr, int numTags){
//        //find the offending fold and class
//        double worstDeviation = 0;
//        int worstDevFold = 0;
//        int worstDevCol = 0;
//        double target = 1.0 / foldTagCounts.size();
//        double tmp;
//        for (int i = 0; i < foldTagCounts.size(); i++) {
//            int[] counts = foldTagCounts.get(i);
//            for (int j = 0; j < counts.length; j++) {
//                tmp = ((double)counts[j]/(double)totalTagCountArr[j]) - target;
//                if(tmp > worstDeviation){
//                    worstDeviation = tmp;
//                    worstDevFold = i;
//                    worstDevCol = j;
//                }
//            }
//        }
//        System.out.println("Worst deviation fold=" + worstDevFold + ", deviation=" + worstDeviation + ", column=" + worstDevCol);
//        
//        //find the largest artist
//        String worstArtist = "";
//        int worstArtistCount = 0;
//        for (int i = 0; i < foldArtists[worstDevFold].size(); i++) {
//            int count = artistToTagCountArray.get(foldArtists[worstDevFold].get(i))[worstDevCol];
//            if (count > worstArtistCount){
//                worstArtistCount = count;
//                worstArtist = foldArtists[worstDevFold].get(i);
//            }
//        }
//        
//        return worstArtist;
//    }
    
    
    /**
     * Returns a simple test, training and validation dataset from the database.
     * Classes will be divided in equal proportion (if possible) and filtered by
     * the metadataFilter (if possible). The class metadata will be set to the
     * specified metadata type.
     * @return The test sets which are indexed[type][set] where the types are:
     * 0 - Training set
     * 1 - Test set
     * 2 - Validation set
     * 3 - unused files
     * @param maxToTakeForEachFilterClass The maximum number of examples to use in each filter class (e.g. use at most 20 examples per artist, discard the rest).
     * @param testMetadata Metadata to base dataset on.
     * @param randomSeed Seed to use for random selection
     * @param proportionOfDB Proportion of database to use
     * @param metadataFilter the metadata to use to filter the test database,
     * null implies no filter is to be used. Items with the same filter metadata
     * will be placed in either the test, training OR validation set and will
     * not be divided across them.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public Signal[][] getTestTrainAndValidationDataSet(int randomSeed, double proportionOfDB, String testMetadata, String metadataFilter, int maxToTakeForEachFilterClass) throws noMetadataException {
        if ((proportionOfDB > 1)||(proportionOfDB < 0)) {
            throw new RuntimeException("Proportion of DB should be between 0:1");
        }
        if (maxToTakeForEachFilterClass < -1) {
            throw new RuntimeException("maxToTakeForEachFilterClass should be -1 or greater");
        }
        if (testMetadata.equals(metadataFilter)) {
            throw new RuntimeException("Test metadata must be different from filter metadata.");
        }
        
        java.util.Random rnd = new java.util.Random(randomSeed);
        List<String> files = this.getFileNames();
        Collections.sort(files);
        if(metadataFilter.equals("")) {//simple random split
            
            ArrayList<Signal> train = new ArrayList<Signal>();
            ArrayList<Signal> test = new ArrayList<Signal>();
            ArrayList<Signal> validation = new ArrayList<Signal>();
            ArrayList<Signal> remainder = new ArrayList<Signal>();
            
            ArrayList<ArrayList<Signal>> classLists = new ArrayList<ArrayList<Signal>>();
            List<String> classNameList = null;
            HashMap<String,ArrayList<Signal>> classIndex = null;
            if(this.indexingMetadata(testMetadata)){
                classNameList = this.getMetadataClasses(testMetadata);
                Collections.sort(classNameList);
                classIndex = this.indexKeyToIndexMap.get(testMetadata);
                for (int i = 0; i < classNameList.size(); i++) {
                    classLists.add(classIndex.get(classNameList.get(i)));
                }
            }else{//unindexed metadata
                String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
                String metas = "";
                for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
                    metas += "\t" + metaKeys[i] + "\n";
                }
                throw new noMetadataException("A test set on an unindexed metadata type was requested!\n Set requested for: " + testMetadata + "\n\tindexed metadatas: \n" + metas);
            }
            
            //int numFiles = this.size();
            int[] numToUse = new int[classLists.size()];
            
            //not sure if this logic is sound for even 3 way split...
            double aThird = 1.0/3.0;
            int[] numToTrain = new int[classLists.size()];
            int[] numToTest = new int[classLists.size()];
            int[] numToValid = new int[classLists.size()];
            
            for (int i = 0; i < classLists.size(); i++) {
                numToUse[i] = (int)Math.round((((ArrayList)classLists.get(i)).size())  * proportionOfDB);
                numToTrain[i] = (int)((double)numToUse[i] * aThird);
                numToTest[i] = (int)((double)numToUse[i] * aThird);
                numToValid[i] = numToUse[i] - (numToTrain[i] + numToTest[i]);
            }
            
            int[] numUsedTrain = new int[classLists.size()];
            int[] numUsedTest = new int[classLists.size()];
            int[] numUsedValid = new int[classLists.size()];
            
            for (int i = 0; i < classLists.size(); i++) {
                ArrayList<Signal> aClass = classLists.get(i);
                ArrayList<Signal> copyAClass = new ArrayList<Signal>();
                copyAClass.addAll(aClass);
                Collections.sort(copyAClass);
                boolean skipTrain = false;
                boolean skipTest = false;
                boolean skipVal = false;
                
                while ((skipTrain == false)||(skipTest == false)||(skipVal == false)) {
                    
                    if((numUsedTrain[i] < numToTrain[i])&&(copyAClass.size() > 0)){
                        Signal aSig = copyAClass.remove(rnd.nextInt(copyAClass.size()));
                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getStringMetadata(testMetadata));
                        train.add(aSig);
                        numUsedTrain[i]++;
                    }else{
                        skipTrain = true;
                    }
                    if((numUsedTest[i] < numToTest[i])&&(copyAClass.size() > 0)){
                        Signal aSig = copyAClass.remove(rnd.nextInt(copyAClass.size()));
                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getStringMetadata(testMetadata));
                        test.add(aSig);
                        numUsedTest[i]++;
                    }else{
                        skipTest = true;
                    }
                    if((numUsedValid[i] < numToValid[i])&&(copyAClass.size() > 0)){
                        Signal aSig = copyAClass.remove(rnd.nextInt(copyAClass.size()));
                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getStringMetadata(testMetadata));
                        validation.add(aSig);
                        numUsedValid[i]++;
                    }else{
                        skipVal = true;
                    }
                }
                for (int j = 0; j < copyAClass.size(); j++) {
                    remainder.add(copyAClass.get(j));
                }
            }
            Signal[][] out = new Signal[4][];
            out[0] = train.toArray(new Signal[train.size()]);
            out[1] = test.toArray(new Signal[test.size()]);
            out[2] = validation.toArray(new Signal[validation.size()]);
            out[3] = remainder.toArray(new Signal[remainder.size()]);
            System.out.println("Outputting Training, Test and Validation sets for " + testMetadata);
            System.out.print("\t" + testMetadata + " classes: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(classNameList.get(i) + "\t");
            }
            System.out.println("");
            System.out.println("\tTraining set: " + train.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTrain[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tTesting set: " + test.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTest[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tValidation set: " + validation.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedValid[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tUnused files: " + remainder.size() + " Signal Objects");
            
            return out;
            
            
            
        }else{ //Filtered split
            ArrayList<Signal> train = new ArrayList<Signal>();
            ArrayList<Signal> test = new ArrayList<Signal>();
            ArrayList<Signal> validation = new ArrayList<Signal>();
            ArrayList<Signal> remainder = new ArrayList<Signal>();
            ArrayList<ArrayList<Signal>> classLists = new ArrayList<ArrayList<Signal>>();
            List<String> classNameList = null;
            HashMap<String,ArrayList<Signal>> classIndex = null;
            
            if(this.indexingMetadata(testMetadata)){
                classNameList = this.getMetadataClasses(testMetadata);
                Collections.sort(classNameList);
                classIndex = this.indexKeyToIndexMap.get(testMetadata);
                for (int i = 0; i < classNameList.size(); i++) {
                    ArrayList<Signal> copyAClass = new ArrayList<Signal>();
                    ArrayList<Signal> aClass = classIndex.get(classNameList.get(i));
                    copyAClass.addAll(aClass);
                    Collections.sort(copyAClass);
                    classLists.add(copyAClass);
                }
            }else{//unindexed metadata
                ArrayList metaKeys =  (ArrayList)this.getIndexedMetadatas();
                String metas = "";
                if(metaKeys.size() == 0) {
                    metas = "\t<none>";
                }else{
                    for (int i = 0; i < metaKeys.size(); i++) {
                        metas += "\t" + metaKeys.get(i) + "\n";
                    }
                }
                throw new noMetadataException("A test and training set on an unindexed metadata type was requested!\n Set requested for: " + testMetadata + "\n\tindexed metadatas: \n" + metas);
            }
            
            //int numFiles = this.size();
            int[] numToUse = new int[classLists.size()];
            
            //not sure if this logic is sound for even 3 way split...
            double aThird = 1.0/3.0;
            int[] numToTrain = new int[classLists.size()];
            int[] numToTest = new int[classLists.size()];
            int[] numToValid = new int[classLists.size()];
            
            for (int i = 0; i < classLists.size(); i++) {
                numToUse[i] = (int)Math.round((((ArrayList)classLists.get(i)).size())  * proportionOfDB);
                numToTrain[i] = (int)((double)numToUse[i] * aThird);
                numToTest[i] = (int)((double)numToUse[i] * aThird);
                numToValid[i] = numToUse[i] - (numToTrain[i] + numToTest[i]);
            }
            
            int[] numUsedTrain = new int[classLists.size()];
            int[] numUsedTest = new int[classLists.size()];
            int[] numUsedValid = new int[classLists.size()];
            int[] numNotUsed = new int[classLists.size()];
            
            for (int i = 0; i < numToUse.length; i++) {
                //get list for main class
                ArrayList<Signal> aClass = classLists.get(i);
                
                //randomly select a class from filter list and add all examples to train set
                HashMap<String,ArrayList<Signal>> filterIndex = null;
                if(this.indexingMetadata(metadataFilter)){
                    filterIndex = this.indexKeyToIndexMap.get(metadataFilter);
                }else{//unindexed metadata
                    String[] metaKeys = this.indexKeyToIndexMap.keySet().toArray(new String[this.indexKeyToIndexMap.size()]);
                    String metas = "";
                    for (int j = 0; j < this.indexKeyToIndexMap.size(); j++) {
                        metas += "\t" + metaKeys[j] + "\n";
                    }
                    throw new noMetadataException("Filtering of a test and training set on an unindexed metadata type was requested!\n Filtering requested for: " + metadataFilter + "\n\tindexed metadatas: \n" + metas);
                }
                
                
                boolean skipTrain = false;
                boolean skipTest = false;
                boolean skipVal = false;
                
                while ((skipTrain == false)||(skipTest == false)||(skipVal == false)) {
                    
                    
                    if((numUsedTrain[i] < numToTrain[i])&&(aClass.size() > 0)) {
                        Signal theSig = aClass.get(rnd.nextInt(aClass.size()));
                        String filterClass = this.getMetadataClassForSignal(metadataFilter,theSig);
                        ArrayList<Signal> filterList = filterIndex.get(filterClass);
                        Collections.sort(filterList);
                        for (int j = 0; j < filterList.size(); j++) {
                            if (j<maxToTakeForEachFilterClass) {
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        train.add(aSig);
                                        numUsedTrain[k]++;
                                        break;
                                    }
                                }
                            }else{//throw away, too many for this filter class (artist)
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        remainder.add(aSig);
                                        numNotUsed[k]++;
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        skipTrain = true;
                    }
                    
                    if((numUsedTest[i] < numToTest[i])&&(aClass.size() > 0)) {
                        Signal theSig = aClass.get(rnd.nextInt(aClass.size()));
                        String filterClass = this.getMetadataClassForSignal(metadataFilter,theSig);
                        ArrayList<Signal> filterList = filterIndex.get(filterClass);
                        Collections.sort(filterList);
                        for (int j = 0; j < filterList.size(); j++) {
                            if (j<maxToTakeForEachFilterClass) {
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        test.add(aSig);
                                        numUsedTest[k]++;
                                        break;
                                    }
                                }
                            }else{//throw away, too many for this filter class (artist)
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        remainder.add(aSig);
                                        numNotUsed[k]++;
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }else{
                        skipTest = true;
                    }
                    
                    if((numUsedValid[i] < numToValid[i])&&(aClass.size() > 0)) {
                        Signal theSig = aClass.get(rnd.nextInt(aClass.size()));
                        String filterClass = this.getMetadataClassForSignal(metadataFilter,theSig);
                        ArrayList<Signal> filterList = filterIndex.get(filterClass);
                        Collections.sort(filterList);
                        for (int j = 0; j < filterList.size(); j++) {
                            if (j<maxToTakeForEachFilterClass) {
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        validation.add(aSig);
                                        numUsedValid[k]++;
                                        break;
                                    }
                                }
                            }else{//throw away, too many for this filter class (artist)
                                for (int k = 0; k < classLists.size(); k++) {
                                    ArrayList<Signal> anotherClass = classLists.get(k);
                                    int idx = anotherClass.indexOf(filterList.get(j));
                                    if (idx > -1) {
                                        Signal aSig = anotherClass.remove(idx);
                                        aSig.setMetadata(Signal.PROP_CLASS,aSig.getMetadata(testMetadata));
                                        remainder.add(aSig);
                                        numNotUsed[k]++;
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }else{
                        skipVal = true;
                    }
                }
                for (int j = 0; j < aClass.size(); j++) {
                    remainder.add(aClass.get(j));
                }
                
            }
            Signal[][] out = new Signal[4][];
            out[0] = train.toArray(new Signal[train.size()]);
            out[1] = test.toArray(new Signal[test.size()]);
            out[2] = validation.toArray(new Signal[validation.size()]);
            out[3] = remainder.toArray(new Signal[remainder.size()]);
            System.out.println("Outputting Training, Test and Validation set for " + testMetadata + " filtered by " + metadataFilter);
            System.out.print("\t" + testMetadata + " classes: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(classNameList.get(i) + "\t");
            }
            System.out.println("");
            System.out.println("\tTraining set: " + train.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTrain[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tTesting set: " + test.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedTest[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tValidation set: " + validation.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numUsedValid[i] + "\t");
            }
            System.out.println("");
            System.out.println("\tUnused files: " + remainder.size() + " Signal Objects");
            System.out.print("\tclass breakdown: ");
            for (int i = 0; i < classNameList.size(); i++) {
                System.out.print(numNotUsed[i] + "\t");
            }
            return out;
        }
    }
    
    /**
     * Returns a String representation of the MusicDB Object.
     * @return a String representation of the MusicDB Object.
     */
    public String toString() {
        String msg = "---------------\nMusicDB\n" +
                "\tNumber of files:\t" + this.fileNameToSignalObject.size() + "\n" +
                "\tIndexed metadatas:\n";
        List<String> indexes = this.getIndexedMetadatas();
        for (int i = 0; i < this.indexKeyToIndexMap.size(); i++) {
            msg += "\t\t" + indexes.get(i) + ":\t" + this.indexKeyToIndexMap.get(indexes.get(i)).size() + "\n";
        }
        return msg;
    }

    private double measureMaxDeviationFromIdeal(int numFolds, ArrayList<int[]> foldTagCounts, int[] totalTagCountArr) {
        //measure how far from the ideal split this is
        double targetProportion = 1.0 / numFolds;
//        double totalDeviation = 0.0;
        int[] foldCounts;
//        for (int i = 0; i < numFolds; i++) {
//            foldCounts = foldTagCounts.get(i);
//            for (int j = 0; j < totalTagCountArr.length; j++) {
//                totalDeviation += Math.abs(((double)foldCounts[j] / (double)totalTagCountArr[j]) - targetProportion);
//            }
//        }
//        totalDeviation /= numFolds * totalTagCountArr.length;
//        return totalDeviation;
        
        double maxDeviation = 0.0;
        double dev;
        for (int i = 0; i < numFolds; i++) {
            foldCounts = foldTagCounts.get(i);
            for (int j = 0; j < totalTagCountArr.length; j++) {
                dev = Math.abs(((double)foldCounts[j] / (double)totalTagCountArr[j]) - targetProportion);
                if (dev > maxDeviation){
                    maxDeviation = dev;
                }
            }
        }
        
        
        return maxDeviation;
    }
    
    private double measureEntropyOfSplit(int numFolds, ArrayList<int[]> foldTagCounts) {
        double totalEntropy = 0.0;
        int[] foldCounts;
        for (int i = 0; i < numFolds; i++) {
            foldCounts = foldTagCounts.get(i);
            totalEntropy += Mathematics.entropy(foldCounts, 20);
            
        }
        
        return totalEntropy / numFolds;
    }
    
    /**
     * Returns a simple test and training dataset from the database.
     * Classes will be divided in equal proportion (if possible) and filtered by
     * the metadataFilter (if possible).  The specified metadata will be set
     * as the class metadata.
     *
     * @return The test sets which are indexed[iteration][type][object] where the types are:
     * 0 - Training set
     * 1 - Test set
     * @param numFolds Number of folds to use for crossvalidation
     * @param testMetadata The metadata to base the test and training datasets on
     * @param randomSeed Seed to use for random selection
     * @param metadataFilter the metadata to use to filter the test database, null implies no filter is to be used.
     * items with the same filter metadata will be placed in either the test or the
     * training set and will not divided across them.
     * @throws org.imirsel.m2k.util.noMetadataException
     */
/*    public Signal[][][] getNFoldTestTrainDataSet(int randomSeed, int numFolds, String testMetadata, String metadataFilter) throws noMetadataException
    {
 */
}
