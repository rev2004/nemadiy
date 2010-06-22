package org.imirsel.m2k.util;



import java.util.*;
import java.io.*;
import java.lang.Comparable;

/**
 * A class designed to encapsulate any signal in M2K whether it is single dimensional,
 * such as a monophone waveform, or multidimensional, such as MFCCs calculated
 * from a waveform. A <code>HashMap</code> is provided to store meta-data about the
 * signal. Keys to meta-data are standardised (both key name and value data type)
 * and a list of the current standards is output by the <code>metadataStandards()</code>
 * method. Data is stored in the data variable as data[columns][rows].
 * @author  Kris West
 */
public class Signal implements Serializable, Cloneable, Comparable {
    
    public static final long serialVersionUID = -2347951075190683926L;
    
    /**
     * Constant definition for metadata key: column label
     */
    public final static String PROP_COLUMN_LABELS = "columnLabels";
    /**
     * Constant definition for metadata key: sample rate
     */
    public final static String PROP_SAMPLE_RATE  = "sampleRate";
    /**
     * Constant definition for metadata key: frame size
     */
    public final static String PROP_FRAME_SIZE  = "frameSize";
    /**
     * Constant definition for metadata key: overlap size
     */
    public final static String PROP_OVERLAP_SIZE  = "overlapSize";
    /**
     * Constant definition for metadata key: file location
     */
    public final static String PROP_FILE_LOCATION  = "fileLocation";
    /**
     * Constant definition for metadata key: directory name
     */
    public final static String PROP_DIRECTORY_NAME  = "dirName";
    /**
     * Constant definition for metadata key: class name
     */
    public final static String PROP_CLASS = "class";
    /**
     * Constant definition for metadata key: genre
     */
    public final static String PROP_GENRE = "genre";
    /**
     * Constant definition for metadata key: mood
     */
    public final static String PROP_MOOD = "mood";
    /**
     * Constant definition for metadata key: tags
     */
    public final static String PROP_TAGS = "tags";
    /**
     * Constant definition for metadata key: year
     */
    public final static String PROP_ALT_YEAR = "year";
    /**
     * Constant definition for metadata key: alternate_genre
     */
    public final static String PROP_ALT_GENRE = "alternate_genre";
    /**
     * Constant definition for metadata key: artist
     */
    public final static String PROP_ARTIST = "artist";
    /**
     * Constant definition for metadata key: album
     */
    public final static String PROP_ALBUM = "album";
    /**
     * Constant definition for metadata key: trackname
     */
    public final static String PROP_TRACKNAME = "trackname";
    /**
     * Constant definition for metadata key: array of class names
     */
    public final static String PROP_CLASSES = "classes";
    /**
     * Constant definition for metadata key: applied classification
     */
    public final static String PROP_CLASSIFICATION = "classification";
    /**
     * Constant definition for metadata key: arrays of applied classifications
     */
    public final static String PROP_CLASSIFICATIONS = "classifications";
    /**
     * Constant definition for metadata key: Onset times
     */
    public final static String PROP_ONSET_TIMES = "Onset times";
    /**
     * Constant definition for metadata key: Onset times
     */
    public final static String PROP_ONSET_TIMES_GT = "GT Onset times";
    /**
     * Constant definition for metadata key:  Drum transcription
     */
    public final static String PROP_DRUM_TRANSCRIPTION = "Drum transcription";
    /**
     * Constant definition for metadata key: Symbols used in drum transcription
     */
    public final static String PROP_DRUM_TRANSCRIPTION_LABELS = "Drum transcription labels";
    /**
     * Constant definition for metadata key:  Classifier Transcription
     */
    public final static String PROP_CLASSIFIER_TRANSCRIPTION = "Classifier Transcription";
    /**
     * Constant definition for metadata key: Segmentation transcription (Onsets and Silences)
     */
    public final static String PROP_SEGMENTATION_TRANSCRIPTION = "Segmentation labels";
    /**
     * Constant definition for metadata key: Musical key
     */
    public final static String PROP_KEY = "Key";
    /**
     * Constant definition for metadata tempo: Musical tempo
     */
    public final static String PROP_TEMPO = "Tempo";
    /**
     * Constant definition for McNemar's testing metadata.
     */
    public final static String PROP_MCNEMAR = "McNemars";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF = "Performance";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF_ACC = "Accuracy";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF_ACC_PER_CLASS = "Accuracy Per Class";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF_NORM_ACC = "Normalised Accuracy";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF_DISCOUNTED_ACC = "Discounted Accuracy";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF_DISCOUNTED_ACC_PER_CLASS = "Discounted Accuracy Per Class";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_PERF_NORM_DISCOUNTED_ACC = "Normalised Discounted Accuracy";
    /**
     * Constant definition for performance per class (rather than total) metadata.
     */
    public final static String PROP_PERF_PER_CLASS = "Performance per class";
    /**
     * Constant definition for performance metadata.
     */
    public final static String PROP_DURATION = "Duration";
    /**
     * Constant definition for algorithm name (used as an identifier in evaluations).
     */
    public final static String PROP_ALG_NAME = "Algorithm name";
    /**
     * Constant definition for class membership Likelihoods.
     */
    public final static String PROP_LIKELIHOODS = "Likelihoods";
    /**
     * Constant definition for model used to produce class membership Likelihoods.
     */
    public final static String PROP_LIKELIHOODS_MODEL = "Likelihoods model";
    
    /**
     * Constant definition for metadata key: Monophonic transcription of dominent melody
     */
    public final static String COL_F0 = "Predominant f0";
    
    /**
     * Constant definition for section divider used in ASCII file
     */
    public final static String divider = "-===-";
    
    /**
     * Constant definition for separator used in ASCII file
     */
    public final static String separator = "\t";
    
    /**
     * Constant definition for header used in ASCII file
     */
    public final static String fileHeader = "M2K Signal file (27/10/05)";
    
    /**
     * The data matrix.
     */
    private double[][] data;
    
    /**
     * The metadata hashmap.
     */
    private HashMap metadata;
    
    private boolean trimLastRow = false;
    
    /** Creates a new instance of Signal */
    public Signal() {
        data = null;
        metadata = new HashMap();
    }
    
    /** Creates a new instance of Signal with the file location as metadata
     *  @param fileLocation Original location of audio file, used as an identifier
     */
    public Signal(String fileLocation) {
        data = null;
        metadata = new HashMap();
        metadata.put(PROP_FILE_LOCATION, fileLocation);
    }
    
    /** Creates a new instance of Signal with the file location and class as metadata
     *  @param fileLocation Original location of file, used as an identifier
     *  @param classname Class that the file belongs to
     */
    public Signal(String fileLocation, String classname) {
        data = null;
        metadata = new HashMap();
        metadata.put(PROP_FILE_LOCATION, fileLocation);
        metadata.put(PROP_CLASS, classname);
    }
    
    /** Creates a new instance of Signal with the file location and classes as
     *  metadata, note this construtor is intended for use with multiple class labels
     *  for each example
     *  @param fileLocation Original location of file, used as an identifier
     *  @param classnames Classes that the file belongs to
     */
    public Signal(String fileLocation, String[] classnames) {
        data = null;
        metadata = new HashMap();
        metadata.put(PROP_FILE_LOCATION, fileLocation);
        metadata.put(PROP_CLASSES, classnames);
    }
    
    /**
     * Creates a new instance of Signal containing the data passed to it and the supplied column labels.
     * @param data_ Data to represent signal
     * @param columnLabels Labels to apply to data columns
     * @throws IllegalArgumentException Thrown if the number of column labels does not match the number of columns in the data
     */
    public Signal(double[][] data_, String[] columnLabels) throws IllegalArgumentException {
        data = data_;
        metadata = new HashMap();
        
        if (columnLabels.length != data_.length) {
            throw new IllegalArgumentException("The number of column labels does not match the number of columns in the data!\n"
                    + "columns = " + data_.length + ", number of labels = " + columnLabels.length);
        }
        
        metadata.put(PROP_COLUMN_LABELS, columnLabels);
    }
    
    /**
     * Creates a new instance of Signal containing the data passed to it and the supplied column labels.
     * @param className Name of the class that the Signal belongs to
     * @param data_ Data to represent signal
     * @param columnLabels Labels to apply to data columns
     * @param fileLocation Original location of audio file, used as an identifier
     * @throws IllegalArgumentException Thrown if the number of column labels does not match the number of columns in the data
     */
    public Signal(double[][] data_, String className, String[] columnLabels, String fileLocation) throws IllegalArgumentException {
        data = data_;
        metadata = new HashMap();
        
        if (columnLabels.length != data_.length) {
            throw new IllegalArgumentException("The number of column labels does not match the number of columns in the data!\n"
                    + "columns = " + data_.length + ", number of labels = " + columnLabels.length);
        }
        metadata.put(PROP_CLASS, className);
        metadata.put(PROP_COLUMN_LABELS, columnLabels);
        metadata.put(PROP_FILE_LOCATION, fileLocation);
    }
    
    /**
     * Creates a new instance of Signal which is a deep copy of the Signal passed as a parameter
     * @param oldSignal The Signal to copy
     */
    public Signal(Signal oldSignal) {
        //copy data
        if (oldSignal.data != null) {
            data = new double[oldSignal.data.length][oldSignal.data[0].length];
            for (int i=0; i<oldSignal.data.length; i++) {
                for (int j=0; j<oldSignal.data[0].length; j++) {
                    data[i][j] = oldSignal.data[i][j];
                }
            }
        } else {
            data = null;
        }
        
        //copy metadata
        metadata = new HashMap();
        Set keys = oldSignal.metadata.keySet();
        Object[] keysArray = keys.toArray();
        for (int i=0;i<keysArray.length;i++) {
            metadata.put(new String((String)keysArray[i]), oldSignal.metadata.get((String)keysArray[i]));
        }
        //need to check this is actually copying/cloning the metadata and not just mapping to original value
    }
    
    /**
     * Creates a new instance of Signal which is a deep copy of the Signal passed as a parameter, with
     *       only the data marked by the partition
     * @param oldSignal The Signal to copy
     * @param partition A partition marking which frames of data to copy to the new Signal. The frames to copy are identified using the <code>marker</code>
     * @param marker The integer symbol used in the partition to mark which frames to copy
     */
    public Signal(Signal oldSignal, int[] partition, int marker) {
        if (oldSignal.getNumRows() != partition.length) {
            System.out.println("Signal<init> error! Num rows (" + oldSignal.getNumRows() + ") does not match partition length(" + partition.length + ")");
        }
        int rowCount = 0;
        for (int i=0; i<partition.length; i++) {
            if (partition[i] == marker) {
                rowCount++;
            }
        }
        
        //copy data
        data = new double[oldSignal.getNumCols()][rowCount];
        rowCount = 0;
        for (int i=0; i<oldSignal.getNumRows(); i++) {
            if (partition[i] == marker) {
                for (int j=0; j<oldSignal.getNumCols(); j++) {
                    data[j][rowCount] = 0.0;
                    data[j][rowCount] = oldSignal.getData()[j][i];
                }
                rowCount++;
            }
        }
        
        //copy metadata
        metadata = new HashMap();
        Set keys = oldSignal.metadata.keySet();
        Object[] keysArray = keys.toArray();
        for (int i=0;i<keysArray.length;i++) {
            metadata.put(new String((String)keysArray[i]), oldSignal.metadata.get((String)keysArray[i]));
        }
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
     *  Compares two Signal Objects for equality based on their filelocation metadata.
     *  @param otherSignal The Signal to compare this Object with.
     *  @return An integer indicating equality or ordering.
     */
    public int compareTo(Object otherSignal) {
        try {
            return this.getStringMetadata(Signal.PROP_FILE_LOCATION).compareTo(((Signal)otherSignal).getStringMetadata(Signal.PROP_FILE_LOCATION));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Unable to compare Signal Objects with filelocation metadata",ex);
        }
    }
    
    /**
     *  Compares two Signal Objects for equality based on their filelocation metadata.
     *  @param otherSignal The Signal to compare this Object with.
     *  @return A boolean indicating equality.
     */
    public boolean equals(Object otherSignal) {
        try {
            return this.getStringMetadata(Signal.PROP_FILE_LOCATION).equals(((Signal)otherSignal).getStringMetadata(Signal.PROP_FILE_LOCATION));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Unable to compare Signal Objects with filelocation metadata",ex);
        }
    }
    
    /**
     * Clones the Signal object (deep copy)
     * @return A deep copy of this Signal object
     */
    public Object clone() throws java.lang.CloneNotSupportedException {
        super.clone();
        return (new Signal(this));
    }
    
    /**
     * Clones the Signal object (deep copy), without any data
     * @return A deep copy of this Signal object without any data
     */
    public Signal cloneNoData() {
        Signal clone = new Signal();
        //copy metadata
        Set keys = this.metadata.keySet();
        Object[] keysArray = keys.toArray();
        for (int i=0;i<keysArray.length;i++) {
            //System.out.print(((String)keysArray[i]) + ": ");
            //System.out.print(this.metadata.get((String)keysArray[i]));
            if (!((String)keysArray[i]).equals((Signal.PROP_COLUMN_LABELS))) {
                clone.setMetadata(new String((String)keysArray[i]), this.metadata.get((String)keysArray[i]));
            }
        }
        return clone;
    }
    
    /** Returns a File Object for the path specified by the file location metadata.
     * @throws noMetadataException if the Signal Object has no file locaiton metadata.
     * @return a File Object for the path specified by the file location metadata.
     */
    public File getFile() throws noMetadataException {
        return new File(this.getStringMetadata(Signal.PROP_FILE_LOCATION));
    }
    
    /**
     * Adds the <code>value</code> to the metadata <code>HashMap</code> with the specified <code>key</code>.
     * If there is already metadata corresponding to the supplied key it is replaced.
     * Primitive datatypes must be wrapped in Object based datatypes, e.g. int is wrapped
     * in the Integer class, double is wrapped in the Double class.
     * @param key The key to be added to the <code>metadata</code> <code>HashMap</code>
     * @param value The value to be added to the <code>metadata</code> <code>HashMap</code>
     * @throws IllegalArgumentException Thrown if both key and value are null
     */
    public void setMetadata(String key, Object value) throws IllegalArgumentException {
        if (key.equals(PROP_COLUMN_LABELS)) {
            if(data != null) {
                if(value == null) {
                    
                    throw new IllegalArgumentException("Cannot set null column labels for an initialised double array");
                } else if(((String[])value).length != data.length) {
                    throw new IllegalArgumentException("The number of column labels does not match the number of columns in the data!\n"
                            + "columns = " + data.length + ", number of labels = " + ((String[])value).length);
                }
            }
        }else if(value == null)
        {
            if (metadata.containsKey(key)){
                metadata.remove(key);
            }
            return;
        }
        metadata.put(key, value);
    }
    
    /**
     * Lists all metadata keys for this Signal Object.
     * @return An array of Strings representing the available metadata.
     */
    public String[] metadataKeys() {
        return (String[])metadata.keySet().toArray(new String[metadata.size()]);
    }
    
    /**
     * Returns the metadata value corresponding to the supplied key
     * @param key The key to return the value for
     * @throws noMetadataException Thrown if the key does not exist.
     * @return The value corresponding to the supplied key
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
     * Returns a String containing the details of the metadata standards that have already been defined in M2K
     * @return String array containing details of the metadata standards
     */
    public static String metadataStandards() {
        String standards = ""
                + "Key             Data type      Description<br>\n"
                + PROP_COLUMN_LABELS + "   String[]       The name for each column in the data array. There should always be one label per column<br>\n"
                + PROP_SAMPLE_RATE + "     Integer        The original sample rate of the signal in Hz<br>\n"
                + PROP_FRAME_SIZE + "      Integer        The number of samples in each frame of data in the signal<br>\n"
                + PROP_OVERLAP_SIZE + "    Integer        The number of samples which are overlapped between concurrent frames of data in the signal<br>\n"
                + PROP_FILE_LOCATION + "   String         The filename or equivalent (e.g. SQL statement) that identifies the signal<br>\n"
                + PROP_CLASS + "           String       The class of the Signal, e.g. genre, artist, instrument<br>\n"
                + PROP_CLASSES + "           String[]     The class(es) of the Signal, e.g. genre(s), artist(s), instrument(s). To be used when multiple classes are appropriate.<br>\n"
                + PROP_CLASSIFICATION + "  String         Predicted class of the Signal, e.g. genre, artist etc.<br>\n"
                + PROP_CLASSIFICATIONS + " String[]       Predicted classes of the Signal, e.g. genres, artists etc. To be used when multiple classes are appropriate.<br>\n"
                + PROP_ONSET_TIMES +     "     double[]       Notated musical event onset times<br>\n"
                + PROP_DRUM_TRANSCRIPTION + "   int[]        Transcription of the drum track in a recording, which is expected to be the sasme length as the onset times. The transcription labels should be stored in"  + Signal.PROP_DRUM_TRANSCRIPTION_LABELS + "<br>\n"
                + PROP_DRUM_TRANSCRIPTION_LABELS + " String[]    The drum transcription labels<br>\n"
                + PROP_KEY + "                    The notated key, the key should be stored in the first array cell and the major/minor flag in the second<br>\n";
        
        return standards;
    }
    
    /**
     * Calculates the time stamp for a row of this <code>Sample</code> based on the
     * sample rate metadata, the frame size (if set) and the overlap size (if set).
     * Time indexes are caluclated at the middle of the frame.
     *
     * @param rowNumber The row to calculate the time index for.
     * @throws noMetadataException Thrown if the sample rate is not set
     * @return The time index
     */
    public double getTimeStamp(int rowNumber) throws noMetadataException {
        int sampleRate = 0;
        int frameSize = 1;
        int overlapSize = 0;
        
        /*if (rowNumber > data[0].length) {
            System.out.println("WARNING: Signal.getTimestamp(): The supplied row number is greater than the number of rows in the data!");
        }*/
        if (metadata.containsKey(PROP_SAMPLE_RATE) == false) {
            throw new noMetadataException("The sample rate of this signal is not set!\nNo time index can be calculated.");
        } else {
            sampleRate = ((Integer)metadata.get(PROP_SAMPLE_RATE)).intValue();
        }
        if (metadata.containsKey(PROP_FRAME_SIZE) == true) {
            frameSize = ((Integer)metadata.get(PROP_FRAME_SIZE)).intValue();
        }
        if (metadata.containsKey(PROP_OVERLAP_SIZE) == true) {
            overlapSize = ((Integer)metadata.get(PROP_OVERLAP_SIZE)).intValue();
        }
        //compute stap as middle of frame
        double frameStart = (((double)(frameSize - overlapSize) * rowNumber) / (double)sampleRate);
        double out = frameStart + ((double)frameSize / (2.0 * sampleRate)); 
        //double out = ((((double)frameSize)/(2.0*(double)sampleRate)) + ((double)rowNumber * increment));
        if ((out == 0.0)&&(rowNumber != 0)){
            System.out.println("WARNING: returning timestamp of zero for frame beyond first frame (" + rowNumber + ")!");
        }
        if ((data != null)&&(rowNumber > this.getNumRows())){
            System.out.println("WARNING: timestamp requested for row that is beyond number of rows" );
        }
        
        return out;
    }
    
    /**
     * Returns the data matrix
     * @return The data matrix
     */
    public double[][] getData() {
        return data;
    }
    
    /** Return the column number of the column with the column label passed as a
     *  parameter.
     *  @param label The column label to search for.
     *  @return the column number of the column with the column label passed as
     *  a parameter, if the label is not found -1 is returned.
     */
    public int getColumnIndex(String label) throws noMetadataException {
        int num = -1;
        String[] colLabels = this.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
        for (int i=0;i<colLabels.length;i++) {
            if (colLabels[i].equals(label)) {
                num = i;
                break;
            }
        }
        return num;
    }
    
    /**
     * Adds the <code>column</code> to the data array, expanding the storage to accomodate it.
     * The column label is added to the existing column labels.
     * @param column The column to be added.
     * @param columnLabel The label for the column to be added.
     * @throws IllegalArgumentException Thrown if the column to be added has a different number of rows to the data matrix.
     */
    public void appendColumn(double[] column, String columnLabel) throws IllegalArgumentException {
        if (data != null) {
            if (column.length != data[0].length) {
                throw new IllegalArgumentException("The length of the column does not match the length of the matrix and cannot be added!");
            }
            double[][] newData = new double[data.length + 1][data[0].length];
            
            for (int i=0; i<data.length; i++) {
                newData[i] = data[i];
            }
            newData[data.length] = column;
            
            data = newData;
            
            String[] labels = (String[])metadata.get(PROP_COLUMN_LABELS);
            String[] newLabels = new String[labels.length + 1];
            for (int i=0; i<labels.length; i++) {
                newLabels[i] = labels[i];
            }
            newLabels[labels.length] = columnLabel;
            metadata.put(PROP_COLUMN_LABELS, newLabels);
        } else {
            data = new double[1][column.length];
            data[0] = column;
            String[] labels = new String[1];
            labels[0] = columnLabel;
            metadata.put(PROP_COLUMN_LABELS, labels);
        }
        
    }
    
    public void appendRowMajorMatrix(double[][] rowMajorMatrix, String[] columnLabels){
        int rows = rowMajorMatrix.length;
        int cols = rowMajorMatrix[0].length;
        double[][] featsMat = new double[cols][rows];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                featsMat[j][i] = rowMajorMatrix[i][j];
            }
        }
        appendMatrix(featsMat, columnLabels);
    }
    
    /**
     * Adds the matrix to the existing data matrix of the Signal, expanding the storage to accomodate it.
     * @param matrix The matrix to be added to the data array.
     * @param columnLabels The labels of the columns to be added to the data array.
     * @throws IllegalArgumentException if the matrix to be added has a different number of rows to the existing data matrix or if there are a different number of labels to the number of columns in the matrix to be added..
     */
    public void appendMatrix(double[][] matrix, String[] columnLabels) throws IllegalArgumentException {
        if (data != null) {
            if (matrix[0].length != data[0].length) {
                throw new IllegalArgumentException("The length of the matrix to be added (" + matrix[0].length + ") does not match the length of the matrix (" + data[0].length + ") and cannot be added!");
            }
            if (columnLabels.length != matrix.length) {
                throw new IllegalArgumentException("There are less labels than there are columns to be added!\n The matrix cannot be appended");
            }
            
            double[][] newData = new double[data.length + matrix.length][data[0].length];
            for (int i=0; i < data.length; i++) {
                newData[i] = data[i];
            }
            for (int j=0; j < matrix.length; j++) {
                newData[data.length + j] = matrix[j];
            }
            data = newData;
            
            String[] labels = (String[])metadata.get(PROP_COLUMN_LABELS);
            String[] newLabels = new String[labels.length + columnLabels.length];
            for (int i=0; i<labels.length; i++) {
                newLabels[i] = labels[i];
            }
            for (int i=0; i<columnLabels.length; i++) {
                newLabels[labels.length + i] = columnLabels[i];
            }
            metadata.put(PROP_COLUMN_LABELS, newLabels);
        } else {
            if (columnLabels.length != matrix.length) {
                throw new IllegalArgumentException("There are less labels than there are columns to be added!\n The matrix cannot be appended");
            }
            data = matrix;
            metadata.put(PROP_COLUMN_LABELS, columnLabels);
        }
    }
    
    /**
     * Returns the number of rows in the Signal's data matrix
     * @return the number of rows in the Signal's data matrix
     */
    public int getNumRows() {
        if (data != null) {
            if (trimLastRow) {
                return data[0].length - 1;
            } else {
                return data[0].length;
            }
        } else {
            return 0;
        }
    }
    
    /**
     * Returns the number of columns in the Signal's data matrix
     * @return the number of columns in the Signal's data matrix
     */
    public int getNumCols() {
        if (data != null) {
            return data.length;
        } else {
            return 0;
        }
    }
    
    /**
     * Returns a row of data from the Signal data array
     * @param row the row number to return
     * @return the row of data
     */
    public double[] getDataRow(int row) {
        double[] vec = new double[getNumCols()];
        for (int i = 0; i<getNumCols(); i++) {
            vec[i] = data[i][row];
        }
        return vec;
    }
    
    
    /**
     * Returns a row of data from the Signal data array as a float array
     * @param row the row number to return
     * @return the row of data
     */
    public float [] getFloatDataRow(int row) {
        float[] vec = new float[getNumCols()];
        for (int i = 0; i<getNumCols(); i++) {
            vec[i] = (float) data[i][row];
        }
        return vec;
    }
    
    /**
     * Removes the data matrix and corresponding labels from the Signal
     */
    public void deleteData() {
        data = null;
        this.setMetadata(Signal.PROP_COLUMN_LABELS, null);
    }
    
    /**
     * Removes the specified column from the data matrix and corresponding label
     * from the meta data hashmap.
     * @param col The column to remove from the data matrix
     * @throws noMetadataException Thrown if the key doesn't exist.
     */
    public void deleteColumn(int col) {
        int newLength = data.length-1;
        if (newLength <= 0) {
            data = null;
            this.setMetadata(Signal.PROP_COLUMN_LABELS, null);
        } else {
            double[][] newData = new double[data.length-1][];
            
            if (newData.length > 0) {
                try{
                    String[] colLabels = this.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
                    String[] newColLabels = new String[colLabels.length-1];
                    for (int i = 0; i<data.length; i++) {
                        if (i<col) {
                            newData[i] = data[i];
                            newColLabels[i] = colLabels[i];
                        } else if (i == col) {
                        } else {
                            newData[i-1] = data[i];
                            newColLabels[i-1] = colLabels[i];
                        }
                    }
                    data = newData;
                    this.setMetadata(Signal.PROP_COLUMN_LABELS, newColLabels);
                } catch(noMetadataException nme) {
                    
                }
            }
        }
    }
    
    /**
     * Reads a Signal Object from an ASCII file in the format produced by the
     * <code>write</code> method.
     * @param theFile The File object to load the Signal from.
     * @throws java.io.IOException Thrown if an IOException occurs.
     * @throws java.lang.ClassNotFoundException Thrown if an attempt load an unknown class is made
     * @return The loaded Signal.
     */
    public static Signal read(File theFile) throws java.io.IOException, java.lang.ClassNotFoundException {
        //Check readLine() behaviour is valid... could be more robust?
        
        Signal theSignal = new Signal();
        
        if (!theFile.exists()) {
            throw new FileNotFoundException("Signal.read(): The specified file does not exist!\n File: " + theFile.getPath());
        }
        if (theFile.isDirectory()) {
            throw new RuntimeException("Signal.read(): The specified file is a directory and therefore cannot be read!\n Path: " + theFile.getPath());
        }
        if (!theFile.canRead()) {
            throw new RuntimeException("Signal.read(): The specified file exists but cannot be read!\n File: " + theFile.getPath());
        }
        
        BufferedReader textBuffer;
        try {
            textBuffer = new BufferedReader( new FileReader(theFile) );
        } catch(java.io.FileNotFoundException fnfe) {
            throw new RuntimeException("Signal.read(): The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n File: " + theFile.getPath());
        }
        String line = null;
        try {
            //check headers
            line = textBuffer.readLine();
            if (!line.equals(fileHeader)) {
                System.out.println("WARNING: Signal.read(): Doesn't match the current format specification\nFile: " + theFile.getPath() + "\nCurrent spec: " + fileHeader + "\nFile spec: " + line);
            }
            line = textBuffer.readLine();
            if (!line.equals(divider)) {
                throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
            }
            
            String[] theColumnLabels = null;
            
            //read metadata
            line = textBuffer.readLine();
            if (!line.equals("null")) {
                //read metadata
                while(!line.equals(divider)) {//Format: key className length (data1 + separator ... datalength)
                    String[] comps = line.split(separator);
                    if (comps[1].equals("null")){
                        //ignore line
                    } else if (comps[2].equals("-1")) {
                        if (comps.length > 3){
                            if (comps[1].equals("java.lang.Integer")) {
                                theSignal.setMetadata(comps[0], new Integer(comps[3]));
                            } else if (comps[1].equals("java.lang.String")) {
                                theSignal.setMetadata(comps[0], comps[3]);
                            } else if (comps[1].equals("java.lang.Double")) {
                                theSignal.setMetadata(comps[0], new Double(comps[3]));
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
                        
                        if (comps[0].equals(Signal.PROP_COLUMN_LABELS)) {
                            theColumnLabels = (String[])anArray;
                        } else {
                            theSignal.setMetadata(comps[0], anArray);
                        }
                        
                    }
                    
                    line = textBuffer.readLine();
                }
            } else {
                line = textBuffer.readLine();
                if (!line.equals(divider)) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
            }
            
            //read data
            line = textBuffer.readLine();
            if (!line.equals("null")) {
                if (theColumnLabels == null) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format (no labels)!\n File: " + theFile.getPath());
                }
                //read rows and columns
                String[] comps = line.split(separator);
                if (comps.length != 2) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
                int cols = Integer.parseInt(comps[0]);
                int rows = Integer.parseInt(comps[1]);
                
                line = textBuffer.readLine();
                if (!line.equals(divider)) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
                
                double[][] theData = new double[cols][rows];
                
                //read data
                int count = 0;
                line = textBuffer.readLine();
                while(!line.equals(divider)) {
                    comps = line.split(separator);
                    if (comps.length != cols) {
                        throw new RuntimeException("Signal.read(): The file being read is not in the correct format (too few cols, " + comps.length + ", insted of " + cols + ")!\n File: " + theFile.getPath());
                    }
                    for (int c=0;c<cols;c++) {
                        theData[c][count] = Double.parseDouble(comps[c]);
                    }
                    count++;
                    line = textBuffer.readLine();
                }
                if (count != rows) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format (wrong number of rows, " + count + ", insted of " + rows + ")!\n File: " + theFile.getPath());
                }
                theSignal.appendMatrix(theData, theColumnLabels);
            }
            textBuffer.close();
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("Signal.read(): An IOException occured while reading file: " + theFile.getPath() + "\n" + ioe);
        } catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            throw new RuntimeException("NullPointerException caused by: " + theFile.getCanonicalPath());
        } catch (java.lang.ArrayIndexOutOfBoundsException idxex){
            idxex.printStackTrace();
            throw new RuntimeException("ArrayIndexOutOfBoundsException caused by: " + theFile.getCanonicalPath());
        }
        
        return theSignal;
    }
    
    /**
     * Reads a Signal Object from an ASCII file in the format produced by the
     * <code>write</code> method.
     * @param theFile The File object to load the Signal from.
     * @throws java.io.IOException Thrown if an IOException occurs.
     * @throws java.lang.ClassNotFoundException Thrown if an attempt load an unknown class is made
     * @return The loaded Signal.
     */
    public static Signal read(File theFile, int maxDataRows) throws java.io.IOException, java.lang.ClassNotFoundException {
        //Check readLine() behaviour is valid... could be more robust?
        
        Signal theSignal = new Signal();
        
        if (!theFile.exists()) {
            throw new FileNotFoundException("Signal.read(): The specified file does not exist!\n File: " + theFile.getPath());
        }
        if (theFile.isDirectory()) {
            throw new RuntimeException("Signal.read(): The specified file is a directory and therefore cannot be read!\n Path: " + theFile.getPath());
        }
        if (!theFile.canRead()) {
            throw new RuntimeException("Signal.read(): The specified file exists but cannot be read!\n File: " + theFile.getPath());
        }
        
        BufferedReader textBuffer;
        try {
            textBuffer = new BufferedReader( new FileReader(theFile) );
        } catch(java.io.FileNotFoundException fnfe) {
            throw new RuntimeException("Signal.read(): The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n File: " + theFile.getPath());
        }
        String line = null;
        try {
            //check headers
            line = textBuffer.readLine();
            if (!line.equals(fileHeader)) {
                System.out.println("WARNING: Signal.read(): Doesn't match the current format specification\nFile: " + theFile.getPath() + "\nCurrent spec: " + fileHeader + "\nFile spec: " + line);
            }
            line = textBuffer.readLine();
            if (!line.equals(divider)) {
                throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
            }
            
            String[] theColumnLabels = null;
            
            //read metadata
            line = textBuffer.readLine();
            if (!line.equals("null")) {
                //read metadata
                while(!line.equals(divider)) {//Format: key className length (data1 + separator ... datalength)
                    String[] comps = line.split(separator);
                    if (comps[1].equals("null")){
                        //ignore line
                    } else if (comps[2].equals("-1")) {
                        if (comps.length > 3){
                            if (comps[1].equals("java.lang.Integer")) {
                                theSignal.setMetadata(comps[0], new Integer(comps[3]));
                            } else if (comps[1].equals("java.lang.String")) {
                                theSignal.setMetadata(comps[0], comps[3]);
                            } else if (comps[1].equals("java.lang.Double")) {
                                theSignal.setMetadata(comps[0], new Double(comps[3]));
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
                        
                        if (comps[0].equals(Signal.PROP_COLUMN_LABELS)) {
                            theColumnLabels = (String[])anArray;
                        } else {
                            theSignal.setMetadata(comps[0], anArray);
                        }
                        
                    }
                    
                    line = textBuffer.readLine();
                }
            } else {
                line = textBuffer.readLine();
                if (!line.equals(divider)) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
            }
            
            //read data
            line = textBuffer.readLine();
            if (!line.equals("null")) {
                if (theColumnLabels == null) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format (no labels)!\n File: " + theFile.getPath());
                }
                //read rows and columns
                String[] comps = line.split(separator);
                if (comps.length != 2) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
                int cols = Integer.parseInt(comps[0]);
                int rows = Integer.parseInt(comps[1]);
                if (rows > maxDataRows)
                {
                    rows = maxDataRows;
                }
                
                line = textBuffer.readLine();
                if (!line.equals(divider)) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
                
                double[][] theData = new double[cols][rows];
                
                //read data
                int count = 0;
                line = textBuffer.readLine();
                while((!line.equals(divider))&&(count < maxDataRows)) {
                    comps = line.split(separator);
                    if (comps.length != cols) {
                        throw new RuntimeException("Signal.read(): The file being read is not in the correct format (too few cols, " + comps.length + ", insted of " + cols + ")!\n File: " + theFile.getPath());
                    }
                    for (int c=0;c<cols;c++) {
                        theData[c][count] = Double.parseDouble(comps[c]);
                    }
                    count++;
                    line = textBuffer.readLine();
                }
                if (count != rows) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format (wrong number of rows, " + count + ", insted of " + rows + ")!\n File: " + theFile.getPath());
                }
                theSignal.appendMatrix(theData, theColumnLabels);
            }
            textBuffer.close();
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("Signal.read(): An IOException occured while reading file: " + theFile.getPath() + "\n" + ioe);
        } catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            throw new RuntimeException("NullPointerException caused by: " + theFile.getCanonicalPath());
        } catch (java.lang.ArrayIndexOutOfBoundsException idxex){
            idxex.printStackTrace();
            throw new RuntimeException("ArrayIndexOutOfBoundsException caused by: " + theFile.getCanonicalPath());
        }
        
        return theSignal;
    }
    
    /**
     * Reads a Signal Object from an ASCII file in the format produced by the
     * <code>write</code> method.
     * @param theFile The File object to load the Signal from.
     * @throws java.io.IOException Thrown if an IOException occurs.
     * @throws java.lang.ClassNotFoundException Thrown if an attempt load an unknown class is made
     * @return The loaded Signal.
     */
    public static Signal readMetadata(File theFile) throws java.io.IOException, java.lang.ClassNotFoundException {
        //Check readLine() behaviour is valid... could be more robust?
        
        Signal theSignal = new Signal();
        
        if (!theFile.exists()) {
            throw new FileNotFoundException("Signal.read(): The specified file does not exist!\n File: " + theFile.getPath());
        }
        if (theFile.isDirectory()) {
            throw new RuntimeException("Signal.read(): The specified file is a directory and therefore cannot be read!\n Path: " + theFile.getPath());
        }
        if (!theFile.canRead()) {
            throw new RuntimeException("Signal.read(): The specified file exists but cannot be read!\n File: " + theFile.getPath());
        }
        
        BufferedReader textBuffer;
        try {
            textBuffer = new BufferedReader( new FileReader(theFile) );
        } catch(java.io.FileNotFoundException fnfe) {
            throw new RuntimeException("Signal.read(): The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n File: " + theFile.getPath());
        }
        String line = null;
        try {
            //check headers
            line = textBuffer.readLine();
            if (!line.equals(fileHeader)) {
                System.out.println("WARNING: Signal.read(): Doesn't match the current format specification\nFile: " + theFile.getPath() + "\nCurrent spec: " + fileHeader + "\nFile spec: " + line);
            }
            line = textBuffer.readLine();
            if (!line.equals(divider)) {
                throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
            }
            
            String[] theColumnLabels = null;
            
            //read metadata
            line = textBuffer.readLine();
            if (!line.equals("null")) {
                //read metadata
                while(!line.equals(divider)) {//Format: key className length (data1 + separator ... datalength)
                    String[] comps = line.split(separator);
                    if (comps[1].equals("null")){
                        //ignore line
                    } else if (comps[2].equals("-1")) {
                        if (comps.length > 3){
                            if (comps[1].equals("java.lang.Integer")) {
                                theSignal.setMetadata(comps[0], new Integer(comps[3]));
                            } else if (comps[1].equals("java.lang.String")) {
                                theSignal.setMetadata(comps[0], comps[3]);
                            } else if (comps[1].equals("java.lang.Double")) {
                                theSignal.setMetadata(comps[0], new Double(comps[3]));
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
                        
                        if (comps[0].equals(Signal.PROP_COLUMN_LABELS)) {
                            theColumnLabels = (String[])anArray;
                        } else {
                            theSignal.setMetadata(comps[0], anArray);
                        }
                        
                    }
                    
                    line = textBuffer.readLine();
                }
            } else {
                line = textBuffer.readLine();
                if (!line.equals(divider)) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
            }
            
            //read data
            /*line = textBuffer.readLine();
            if (!line.equals("null")) {
                if (theColumnLabels == null) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format (no labels)!\n File: " + theFile.getPath());
                }
                //read rows and columns
                String[] comps = line.split(separator);
                if (comps.length != 2) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
                int cols = Integer.parseInt(comps[0]);
                int rows = Integer.parseInt(comps[1]);
                
                line = textBuffer.readLine();
                if (!line.equals(divider)) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format!\n File: " + theFile.getPath());
                }
                
                double[][] theData = new double[cols][rows];
                
                //read data
                int count = 0;
                line = textBuffer.readLine();
                while(!line.equals(divider)) {
                    comps = line.split(separator);
                    if (comps.length != cols) {
                        throw new RuntimeException("Signal.read(): The file being read is not in the correct format (too few cols, " + comps.length + ", insted of " + cols + ")!\n File: " + theFile.getPath());
                    }
                    for (int c=0;c<cols;c++) {
                        theData[c][count] = Double.parseDouble(comps[c]);
                    }
                    count++;
                    line = textBuffer.readLine();
                }
                if (count != rows) {
                    throw new RuntimeException("Signal.read(): The file being read is not in the correct format (wrong number of rows, " + count + ", insted of " + rows + ")!\n File: " + theFile.getPath());
                }
                theSignal.appendMatrix(theData, theColumnLabels);
            }*/
            textBuffer.close();
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("Signal.read(): An IOException occured while reading file: " + theFile.getPath() + "\n" + ioe);
        } catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            throw new RuntimeException("NullPointerException caused by: " + theFile.getCanonicalPath());
        } catch (java.lang.ArrayIndexOutOfBoundsException idxex){
            idxex.printStackTrace();
            throw new RuntimeException("ArrayIndexOutOfBoundsException caused by: " + theFile.getCanonicalPath());
        }
        
        return theSignal;
    }
    
    /**
     * Creates a String representation of a Signal Object
     * @return a String representation of a Signal Object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(fileHeader + "\n" + divider + "\n");
        
        if (metadata == null) {
            buffer.append("null\n");
        } else {
            Set keys = this.metadata.keySet();
            Object[] keysArray = keys.toArray();
            // sort the keys so items are always output
            // in the same order
            Arrays.sort(keysArray);
            for (int i=0;i<keysArray.length;i++) {
                buffer.append((String)keysArray[i] + separator);
                int length = 0;
                if (metadata.get(keysArray[i]) == null) {
                    buffer.append("null\n");
                } else if (metadata.get(keysArray[i]).getClass().isArray()) {
                    //Supports only int array, String array and double array types
                    String compName = metadata.get(keysArray[i]).getClass().getComponentType().getName();
                    if ((!compName.equals("int"))&&(!compName.equals("double"))&&(!compName.equals("java.lang.String"))) {
                        throw new RuntimeException("Signal.write(): Only intger, double and String array types are supported at present, contact developers.");
                    }
                    
                    if (compName.equals("int")) {
                        length = ((int[])metadata.get(keysArray[i])).length;
                    } else if (compName.equals("java.lang.String")) {
                        length = ((String[])metadata.get(keysArray[i])).length;
                    } else if (compName.equals("double")) {
                        length = ((double[])metadata.get(keysArray[i])).length;
                    }
                    buffer.append(compName + separator + length  + separator);
                    
                    for (int j=0;j<length;j++) {
                        if (compName.equals("int")) {
                            buffer.append(((int[])metadata.get(keysArray[i]))[j] + separator);
                        } else if (compName.equals("java.lang.String")) {
                            buffer.append(((String[])metadata.get(keysArray[i]))[j] + separator);
                        } else if (compName.equals("double")) {
                            buffer.append(((double[])metadata.get(keysArray[i]))[j] + separator);
                        }
                    }
                    buffer.append("\n");
                } else {
                    //Supports only Integer, String and Double data types
                    String className = metadata.get(keysArray[i]).getClass().getName();
                    length = -1;
                    buffer.append(className + separator + length  + separator);
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
        buffer.append(divider + "\n");
        if (data == null) {
            buffer.append("null\n" + divider + "\n");
        } else {
            buffer.append(this.getNumCols() + separator + this.getNumRows() + "\n" + divider + "\n");
            
            for (int i=0;i<this.getNumRows();i++) {
                for (int j=0;j<this.getNumCols();j++) {
                    buffer.append(data[j][i] + separator);
                }
                buffer.append("\n");
            }
            buffer.append(divider + "\n");
        }
        return buffer.toString();
    }
    
    /**
     * Marks the signal so that the last row will be trimmed on write
     */
    public void trimLastRow() {
        trimLastRow = true;
    }
    
    /**
     * Writes a Signal Object to an ASCII file.
     * @param theFile The file to write the Object to.
     * @throws java.io.IOException Thrown if an IO error occurs, such as being unable to create the File
     * or being unable to write to it.
     */
    public void write(File theFile) throws java.io.IOException {
        File theDir = theFile.getParentFile();
        theDir.mkdirs();
        BufferedWriter textBuffer;
        try {
            textBuffer = new BufferedWriter( new FileWriter(theFile, false) );
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("Signal.write(): An IOException occured while opening file: " + theFile.getPath() + " for writing\n" + ioe);
        }
        
        textBuffer.write(this.toString());
        textBuffer.flush();
        textBuffer.close();
    }
    
    /**
     * Converts an array of onset times (in seconds) into a column of integers
     * indicating which rows in the data matrix correspond to onsets.
     * @param onsetTimes array of onset times (in seconds).
     * @return a column of integers indicating which rows in the data
     * matrix correspond to onsets.
     */
    public int[] convertOnsetTimesToDataColumn(double[] onsetTimes) throws noMetadataException {
        if (onsetTimes.length == 0) {
            throw new RuntimeException("No onsets times to convert!");
        }
        int onsetNum = 0;
        double lastDiff = Double.MAX_VALUE;
        double nextDiff = Math.abs(this.getTimeStamp(0) - onsetTimes[onsetNum]);
        double currentDiff = Double.MAX_VALUE;
        int[] segmentationCol = new int[this.getNumRows()];
        boolean hasData = false;
        for(int i=0;i<this.getNumRows()-1;i++) {
            lastDiff = currentDiff;
            currentDiff = nextDiff;
            nextDiff = Math.abs(this.getTimeStamp(i+1) - onsetTimes[onsetNum]);
            if((currentDiff < lastDiff) && (currentDiff <= nextDiff)) {
                segmentationCol[i] = 1;
                hasData = true;
                //System.out.println("found onset");
                onsetNum++;
                if (onsetNum == onsetTimes.length) {
                    break;
                } else {
                    
                }
            } else {
                segmentationCol[i] = 0;
            }
        }
        if (!hasData) {
            String msg = "convertOnsetTimesToDataColumn returned no data for " + this.getStringMetadata(Signal.PROP_FILE_LOCATION) + "\n" +
                    "Number of onsets = " + onsetTimes.length + "\n" +
                    "Data rows: " + this.getNumRows() + "\n";
            
            try{
                msg += "Signal srate: " + this.getIntMetadata(Signal.PROP_SAMPLE_RATE) + "\n";
            }catch(noMetadataException nme){
                msg += "Signal srate not set!\n";
            }
            try{
                msg += "Signal frame size: " + this.getIntMetadata(Signal.PROP_FRAME_SIZE) + "\n";
            }catch(noMetadataException nme){
                msg += "Signal frame size not set!\n";
            }
            try{
                msg += "Signal overlap size: " + this.getIntMetadata(Signal.PROP_OVERLAP_SIZE) + "\n";
            }catch(noMetadataException nme){
                msg += "Signal overlap size not set!\n";
            }
            
            msg += "Onsets: ";
            for (int i=0; i<onsetTimes.length;i++) {
                msg += onsetTimes[i] + ", ";
            }
            System.out.println(msg);
            
        }
        return segmentationCol;
    
    }
    
    public int hashCode(){
        return this.getFile().hashCode();
    }
}
