/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kris
 */
public class EvaluationDataObject implements Serializable{

    public static final long serialVersionUID = -1234567890123456789L;
    
    //Metadata key constants
    /** Constant definition for metadata key: column label     */
    public final static String PROP_COLUMN_LABELS = "columnLabels";
    /** Constant definition for metadata key: file location     */
    public final static String PROP_FILE_LOCATION  = "fileLocation";
    /** Constant definition for metadata key: directory name     */
    public final static String PROP_DIRECTORY_NAME  = "dirName";
    /** Constant definition for metadata key: class name     */
    public final static String PROP_MCNEMAR = "McNemars";
    /** Constant definition for performance metadata.     */
    public final static String PROP_PERF = "Performance";
    /** Constant definition for algorithm name (used as an identifier in evaluations).     */
    public final static String PROP_ALG_NAME = "Algorithm name";
    /** Constant definition for class membership Likelihoods.     */
    
    //Evaluation results constants
    /** Constant definition for metadata key: Evaluation report*/
    public static final String SYSTEM_RESULTS_REPORT = "Single system rsult evaluation report";
    
    
    //File format constants
    /** Constant definition for section divider used in ASCII file     */
    public final static String DIVIDER = "-===-";
    /** Constant definition for SEPARATOR used in ASCII file     */
    public final static String SEPARATOR = "\t";
    /** Constant definition for header used in ASCII file     */
    public final static String fileHeader = "M2K EvaluationDataObject (13/08/08)";
    
    //Tag classification evaluator constants
    /** Constant definition for tag classification data in the form of a 
     * <code>HashMap<String,HashSet<String>></code> - mapping paths to a set
     * of relevant tags. */
    public static final String TAG_BINARY_RELEVANCE_MAP = "Tag classification binary relevance map";
    /** Constant definition for tag classification data in the form of a 
     * <code>HashMap<String,HashMap<String,Double>></code> - mapping paths
     * to a map linking tags to their affinity values. */
    public static final String TAG_AFFINITY_MAP = "Tag classification affinity map";
    
    public static final String TAG_BINARY_ACCURACY_MAP = "Tag classification binary relevance accuracy map";
    public static final String TAG_BINARY_PRECISION_MAP = "Tag classification binary relevance precision map";
    public static final String TAG_BINARY_RECALL_MAP = "Tag classification binary relevance recall map";
    public static final String TAG_BINARY_FMEASURE_MAP = "Tag classification binary relevance fMeasure map";
    
    public static final String TAG_BINARY_OVERALL_ACCURACY = "Tag classification binary relevance overall accuracy";
    public static final String TAG_BINARY_OVERALL_PRECISION = "Tag classification binary relevance overall precision";
    public static final String TAG_BINARY_OVERALL_RECALL = "Tag classification binary relevance overall recall";
    public static final String TAG_BINARY_OVERALL_FMEASURE = "Tag classification binary relevance overall fMeasure";
    
    public static final String TAG_AFFINITY_AUC_ROC = "Tag classification affinity AUC-ROC map";
    public static final String TAG_AFFINITY_ROC_DATA = "Tag classification affinity ROC data points map";
    public static final String TAG_AFFINITY_TAG_AFFINITY_DATAPOINTS = "Tag classification affinity data points map";
    
    public static final String TAG_NUM_POSITIVE_EXAMPLES = "Tag classification number of positive examples map";
    public static final String TAG_NUM_NEGATIVE_EXAMPLES = "Tag classification number of negative examples map";
            
    /**
     * The metadata hashmap.
     */
    private HashMap metadata;
    
    /** Creates a new instance of EvaluationDataObject */
    public EvaluationDataObject() {
        metadata = new HashMap();
    }
    
    /** Creates a new instance of EvaluationDataObject with the file location as metadata
     *  @param fileLocation Original location of evaluation file, used as an identifier
     */
    public EvaluationDataObject(String fileLocation) {
        metadata = new HashMap();
        metadata.put(PROP_FILE_LOCATION, fileLocation);
    }
    
    /**
     * Creates a new instance of EvaluationDataObject which is a deep copy of 
     * the EvaluationDataObject passed as a parameter
     * @param oldObj The EvaluationDataObject to copy
     */
    public EvaluationDataObject(EvaluationDataObject oldObj) {
        //copy metadata
        metadata = new HashMap();
        Set keys = oldObj.metadata.keySet();
        Object[] keysArray = keys.toArray();
        for (int i=0;i<keysArray.length;i++) {
            metadata.put(new String((String)keysArray[i]), oldObj.metadata.get((String)keysArray[i]));
        }
        //need to check this is actually copying/cloning the metadata and not just mapping to original value
    }
    
    /**
     * Returns a flag indicating whether there is an entry in the metadata map
     * for the specified key.
     * @param key The metadata key to check.
     * @return a flag indicating whether there is an entry in the metadata map
     * for the specified key.
     */
    public boolean hasMetadata(String key){
        return metadata.containsKey(key);
    }
    
    /**
     *  Compares two EvaluationDataObject Objects for equality based on their 
     *  file location metadata.
     *  @param otherObj The EvaluationDataObject to compare this Object with.
     *  @return An integer indicating equality or ordering.
     */
    public int compareTo(Object otherObj) {
        try {
            return this.getStringMetadata(EvaluationDataObject.PROP_FILE_LOCATION).compareTo(((EvaluationDataObject)otherObj).getStringMetadata(EvaluationDataObject.PROP_FILE_LOCATION));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Unable to compare Signal Objects with filelocation metadata",ex);
        }
    }
    
    /**
     *  Compares two EvaluationDataObject Objects for equality based on their 
     *  filelocation metadata.
     *  @param otherObj The EvaluationDataObject to compare this Object with.
     *  @return A boolean indicating equality.
     */
    public boolean equals(Object otherObj) {
        try {
            return this.getStringMetadata(EvaluationDataObject.PROP_FILE_LOCATION).equals(((EvaluationDataObject)otherObj).getStringMetadata(EvaluationDataObject.PROP_FILE_LOCATION));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Unable to compare Signal Objects with filelocation metadata",ex);
        }
    }
    
    /**
     * Clones the EvaluationDataObject object (deep copy)
     * @return A deep copy of this Signal object
     */
    public Object clone() throws java.lang.CloneNotSupportedException {
        super.clone();
        return (new EvaluationDataObject(this));
    }
    
    /** Returns a File Object for the path specified by the file location metadata.
     *  @return a File Object for the path specified by the file location metadata.
     */
    public File getFile() throws noMetadataException {
        return new File(this.getStringMetadata(EvaluationDataObject.PROP_FILE_LOCATION));
    }
    
    /**
     * Adds the <code>value</code> to the metadata <code>HashMap</code> with 
     * the specified <code>key</code>. If there is already metadata 
     * corresponding to the supplied key it is replaced. Primitive datatypes 
     * must be wrapped in Object based datatypes, e.g. int is wrapped
     * in the Integer class, double is wrapped in the Double class.
     * @param key The key to be added to the <code>metadata</code> 
     * <code>HashMap</code>.
     * @param value The value to be added to the <code>metadata</code>
     * <code>HashMap</code>.
     * @throws IllegalArgumentException Thrown if both key and value are null.
     */
    public void setMetadata(String key, Object value) throws IllegalArgumentException {
        if(value == null)
        {
            if (metadata.containsKey(key)){
                metadata.remove(key);
            }
            return;
        }
        metadata.put(key, value);
    }
    
    /**
     * Lists all metadata keys for this EvaluationDataObject Object.
     * @return An array of Strings representing the available metadata.
     */
    public String[] metadataKeys() {
        return (String[])metadata.keySet().toArray(new String[metadata.size()]);
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key.
     * @param key The key to return the value for.
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The value corresponding to the supplied key.
     */
    public Object getMetadata(String key) throws noMetadataException {
        if (metadata.containsKey(key) == false) {
            Object[] keys = metadata.keySet().toArray();
            String keyString = "";
            for (int i=0;i<keys.length;i++) {
                keyString += (String)keys[i] + "\n";
            }
            throw new noMetadataException("There is no metadata corresponding to the supplied key!\n" +
                    "Key supplied = " + key + "\n" +
                    "Keys available:\n" +
                    keyString);
        }
        return metadata.get(key);
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key and casts it
     * as an Integer and returns the int value.
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The integer value corresponding to the supplied key
     */
    public int getIntMetadata(String key) throws noMetadataException {
        return ((Integer)this.getMetadata(key)).intValue();
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key and casts it
     * as an int[].
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The int[] corresponding to the supplied key
     */
    public int[] getIntArrayMetadata(String key) throws noMetadataException {
        return ((int[])this.getMetadata(key));
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key and casts it
     * as a String
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The String value corresponding to the supplied key
     */
    public String getStringMetadata(String key) throws noMetadataException {
        return ((String)this.getMetadata(key));
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key and casts it
     * as a String[]
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The String[] of values corresponding to the supplied key
     */
    public String[] getStringArrayMetadata(String key) throws noMetadataException {
        return ((String[])this.getMetadata(key));
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key and casts it
     * as a Double and returns the double value
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The double value corresponding to the supplied key
     */
    public double getDoubleMetadata(String key) throws noMetadataException {
        return ((Double)this.getMetadata(key)).doubleValue();
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key and casts it
     * as a double[]
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The double[] of values corresponding to the supplied key
     */
    public double[] getDoubleArrayMetadata(String key) throws noMetadataException {
        return ((double[])this.getMetadata(key));
    }
    
    /**
     * Reads a EvaluationDataObject Object from an ASCII file in the format 
     * produced by the <code>write</code> method.
     * @param theFile The File object to load the EvaluationDataObject from.
     * @throws java.io.IOException Thrown if an IOException occurs.
     * @throws java.lang.ClassNotFoundException Thrown if an attempt load an 
     * unknown class is made.
     * @return The loaded EvaluationDataObject.
     */
    public static EvaluationDataObject read(File theFile) throws java.io.IOException, java.lang.ClassNotFoundException {
        //Check readLine() behaviour is valid... could be more robust?
        EvaluationDataObject dataObject = new EvaluationDataObject();
        
        if (!theFile.exists()) {
            throw new FileNotFoundException("EvaluationDataObject.read(): The specified file does not exist!\n File: " + theFile.getPath());
        }
        if (theFile.isDirectory()) {
            throw new RuntimeException("EvaluationDataObject.read(): The specified file is a directory and therefore cannot be read!\n Path: " + theFile.getPath());
        }
        if (!theFile.canRead()) {
            throw new RuntimeException("EvaluationDataObject.read(): The specified file exists but cannot be read!\n File: " + theFile.getPath());
        }
        
        BufferedReader textBuffer;
        try {
            textBuffer = new BufferedReader( new FileReader(theFile) );
        } catch(java.io.FileNotFoundException fnfe) {
            throw new RuntimeException("EvaluationDataObject.read(): The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n File: " + theFile.getPath());
        }
        String line = null;
        try {
            //check headers
            line = textBuffer.readLine();
            if (!line.equals(fileHeader)) {
                System.out.println("WARNING: EvaluationDataObject.read(): Doesn't match the current format specification\nFile: " + theFile.getPath() + "\nCurrent spec: " + fileHeader + "\nFile spec: " + line);
            }
            line = textBuffer.readLine();
            if (!line.equals(DIVIDER)) {
                throw new RuntimeException("EvaluationDataObject.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
            }
            
            String[] theColumnLabels = null;
            
            //read metadata
            line = textBuffer.readLine();
            if (!line.equals("null")) {
                //read metadata
                while(!line.equals(DIVIDER)) {//Format: key className length (data1 + SEPARATOR ... datalength)
                    String[] comps = line.split(SEPARATOR);
                    if (comps[1].equals("null")){
                        //ignore line
                    } else if (comps[2].equals("-1")) {
                        if (comps.length > 3){
                            if (comps[1].equals("java.lang.Integer")) {
                                dataObject.setMetadata(comps[0], new Integer(comps[3]));
                            } else if (comps[1].equals("java.lang.String")) {
                                dataObject.setMetadata(comps[0], comps[3]);
                            } else if (comps[1].equals("java.lang.Double")) {
                                dataObject.setMetadata(comps[0], new Double(comps[3]));
                            }
                        }
                    } else {
                        if (comps.length != (3 + Integer.parseInt(comps[2]))) {
                            throw new RuntimeException("Signal.read(): The file being read is not in the correct format (wrong number of items in metadata array)!\n File: " + theFile.getPath() + "\nLine: " + line);
                        }
                        //init array
                        Object anArray = null;
                        if (comps[1].equals("int")) {
                            anArray = java.lang.reflect.Array.newInstance(java.lang.Integer.TYPE,Integer.parseInt(comps[2]));
                        } else if(comps[1].equals("double")) {
                            anArray = java.lang.reflect.Array.newInstance(java.lang.Double.TYPE,Integer.parseInt(comps[2]));
                        } else {
                            anArray = java.lang.reflect.Array.newInstance(Class.forName(comps[1]),Integer.parseInt(comps[2]));
                        }
                        
                        //populate array
                        for (int m=0; m<Integer.parseInt(comps[2]); m++) {
                            if (comps[1].equals("int")) {
                                ((int[])anArray)[m] = Integer.parseInt(comps[3+m]);
                            } else if (comps[1].equals("java.lang.String")) {
                                ((String[])anArray)[m] = comps[3+m];
                            } else if (comps[1].equals("double")) {
                                ((double[])anArray)[m] = Double.parseDouble(comps[3+m]);
                            }
                        }
                        dataObject.setMetadata(comps[0], anArray);
                    }
                    
                    line = textBuffer.readLine();
                }
            } else {
                line = textBuffer.readLine();
                if (!line.equals(DIVIDER)) {
                    throw new RuntimeException("EvaluationDataObject.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
            }
            
            textBuffer.close();
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("EvaluationDataObject.read(): An IOException occured while reading file: " + theFile.getPath() + "\n" + ioe);
        } catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            throw new RuntimeException("NullPointerException caused by: " + theFile.getCanonicalPath());
        } catch (java.lang.ArrayIndexOutOfBoundsException idxex){
            idxex.printStackTrace();
            throw new RuntimeException("ArrayIndexOutOfBoundsException caused by: " + theFile.getCanonicalPath());
        }
        
        return dataObject;
    }
    
    /**
     * Creates a String representation of a Signal Object
     * @return a String representation of a Signal Object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(fileHeader + "\n" + DIVIDER + "\n");
        
        if (metadata == null) {
            buffer.append("null\n");
        } else {
            Set keys = this.metadata.keySet();
            Object[] keysArray = keys.toArray();
            // sort the keys so items are always output
            // in the same order
            Arrays.sort(keysArray);
            for (int i=0;i<keysArray.length;i++) {
                buffer.append((String)keysArray[i] + SEPARATOR);
                int length = 0;
                if (metadata.get(keysArray[i]) == null) {
                    buffer.append("null\n");
                } else if (metadata.get(keysArray[i]).getClass().isArray()) {
                    //Supports only int array, String array and double array types
                    String compName = metadata.get(keysArray[i]).getClass().getComponentType().getName();
                    if ((!compName.equals("int"))&&(!compName.equals("double"))&&(!compName.equals("java.lang.String"))) {
                        throw new RuntimeException("EvaluationDataObject.write(): Only intger, double and String array types are supported at present, contact developers.");
                    }
                    
                    if (compName.equals("int")) {
                        length = ((int[])metadata.get(keysArray[i])).length;
                    } else if (compName.equals("java.lang.String")) {
                        length = ((String[])metadata.get(keysArray[i])).length;
                    } else if (compName.equals("double")) {
                        length = ((double[])metadata.get(keysArray[i])).length;
                    }
                    buffer.append(compName + SEPARATOR + length  + SEPARATOR);
                    
                    for (int j=0;j<length;j++) {
                        if (compName.equals("int")) {
                            buffer.append(((int[])metadata.get(keysArray[i]))[j] + SEPARATOR);
                        } else if (compName.equals("java.lang.String")) {
                            buffer.append(((String[])metadata.get(keysArray[i]))[j] + SEPARATOR);
                        } else if (compName.equals("double")) {
                            buffer.append(((double[])metadata.get(keysArray[i]))[j] + SEPARATOR);
                        }
                    }
                    buffer.append("\n");
                } else {
                    //Supports only Integer, String and Double data types
                    String className = metadata.get(keysArray[i]).getClass().getName();
                    length = -1;
                    buffer.append(className + SEPARATOR + length  + SEPARATOR);
                    if (className.equals("java.lang.Integer")) {
                        buffer.append(((Integer)metadata.get(keysArray[i])).intValue() + "\n");
                    } else if (className.equals("java.lang.String")) {
                        buffer.append(((String)metadata.get(keysArray[i])) + "\n");
                    } else if (className.equals("java.lang.Double")) {
                        buffer.append(((Double)metadata.get(keysArray[i])).doubleValue() + "\n");
                    }
                }
            }
        }
        buffer.append(DIVIDER + "\n");
        return buffer.toString();
    }
    
    /**
     * Writes a EvaluationDataObject Object to an ASCII file.
     * @param theFile The file to write the Object to.
     * @throws java.io.IOException Thrown if an IO error occurs, such as being 
     * unable to create the File or being unable to write to it.
     */
    public void write(File theFile) throws java.io.IOException {
        File theDir = theFile.getParentFile();
        theDir.mkdirs();
        BufferedWriter textBuffer;
        try {
            textBuffer = new BufferedWriter( new FileWriter(theFile, false) );
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("EvaluationDataObject.write(): An IOException occured while opening file: " + theFile.getPath() + " for writing\n" + ioe);
        }
        
        textBuffer.write(this.toString());
        textBuffer.flush();
        textBuffer.close();
    }
}
