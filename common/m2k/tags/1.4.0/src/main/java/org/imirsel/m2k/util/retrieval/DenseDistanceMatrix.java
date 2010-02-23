package org.imirsel.m2k.util.retrieval;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;


/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class DenseDistanceMatrix implements DistanceMatrixInterface{

    public static final long serialVersionUID = 12903102456L;

    float min = Float.MAX_VALUE;
    float max = Float.MIN_VALUE;
    float norm;


    /** A matrix of distance scores indexed [row][column] where each row
     *  represents the distance scores from the corresponding file in the 
     *  audioFiles array.*/
    private float[][] theMatrix;

    private HashMap<File,Integer> fileToIndex;
    private HashMap<Integer,File> indexToFile;

    /** Name to be used to identify this distance Matrix */
    private String name;
    
    /**
     * Creates a new instance of DistanceMatrix
     */
    public DenseDistanceMatrix() {
        theMatrix = null;
        fileToIndex = new HashMap<File,Integer>();
        name = "";
    }
    
    /**
     * Creates a new instance of DistanceMatrix using the specified matrix of 
     * doubles and the array of file names.
     * @param name_
     * @param aMatrix
     * @param someFiles 
     */
    public DenseDistanceMatrix(String name_, float[][] aMatrix, File[] someFiles) {
        if (name_ == null){
            name = "<no name given>";
        }else{
            name = name_;
        }
        theMatrix = aMatrix;
        fileToIndex = new HashMap<File,Integer>();
        indexToFile = new HashMap<Integer,File>();
        for (int i = 0; i < someFiles.length; i++){
            fileToIndex.put(someFiles[i],i);
            indexToFile.put(i,someFiles[i]);
        }
        computeMinAndMax();
    }

    public void computeMinAndMax(){
        boolean complete = true;
        for (int i = 0; i < theMatrix.length; i++) {
            for (int j = 0; j < theMatrix[i].length; j++) {
                if (theMatrix[i][j] != Float.POSITIVE_INFINITY){
                    if (theMatrix[i][j] < min)
                    {
                        min = theMatrix[i][j];
                    }else if(theMatrix[i][j] > max)
                    {
                        max = theMatrix[i][j];
                    }
                }
            }

            if (theMatrix[i].length != theMatrix.length)
            {
                complete = false;
                System.out.println("Inconsistent number of distances found. Index contains " + theMatrix.length + " files but " + theMatrix[i].length + " were found for file index " + i);
            }
        }

        norm = max + Float.MIN_VALUE;

        if (complete){
            System.out.println("Dense distance matrix, indexing " + theMatrix.length + " Objects with min dist: " + min + " and max dist: " + max);
        }else{
            System.out.println("WARNING: Incomplete or corrupted dense distance matrix received!\nProceeding anyway...");
        }
    }
    
    public float getDistance(File file1, File file2)
    {
        try{
            return theMatrix[getFileToIndex().get(file1)][getFileToIndex().get(file2)];
        }catch(ArrayIndexOutOfBoundsException arex)
        {
            throw new RuntimeException("No distances were found for one of the files passed!\n Query file: " + file1.getPath() + "\nResult file: " + file2.getPath());
        }
    }
    
    public float[] getDistances(File file)
    {
        try{
            return theMatrix[getFileToIndex().get(file)];
        }catch(ArrayIndexOutOfBoundsException arex){
            throw new RuntimeException("No distances were found for the file passed!\n Query file: " + file.getPath());
        }
    }
    
    public float[][] getDistanceMatrix()
    {
        return theMatrix;
    }
    
    public File[] getFiles()
    {
        File[] out = new File[fileToIndex.size()];
        for (Iterator<File> it = getFileToIndex().keySet().iterator(); it.hasNext();){
            File aFile = it.next();
            Integer index = getFileToIndex().get(aFile);
            out[index] = aFile;
        }
        return out;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int indexSize()
    {
        if (theMatrix == null){
            return 0;
        }else {
            return getFileToIndex().size();
        }
    }
    
    /**
     * Changes all entries corresponding to the specified file name to map to the new
     * file name.
     * @param oldLocation Old file name and path.
     * @param newLocation New file name and path (must exist).
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the specified (Old) file name is not found in the index.
     */
    public void remapFileLocation(String oldLocation, String newLocation){
        
        Integer loc = getFileToIndex().remove(new File(oldLocation));
        getIndexToFile().remove(loc);
        File newFile = new File(newLocation);
        if(loc != null){
            getFileToIndex().put(newFile,loc);
            getIndexToFile().put(loc,newFile);
        }else{
            throw new RuntimeException("The specified file was not found in the DistanceMatrix and cannot be remapped to a new location!\nFile: " + oldLocation + "\nTo be mapped to: " + newLocation);
        }
    }
    
    public void removeFile(File fileLocation){
        Integer loc = getFileToIndex().remove(fileLocation);
        if(loc != null){
            float[][] newMatrix = new float[this.indexSize() - 1][this.indexSize() - 1];
            
            for (int i = 0; i < theMatrix.length; i++) {
                if (i < loc)
                {
                    for (int j = 0; j < theMatrix[i].length; j++) {
                        if (j < loc)
                        {
                            newMatrix[i][j] = theMatrix[i][j];
                        }else if(j > loc){
                            newMatrix[i][j-1] = theMatrix[i][j];
                        }
                    }
                }else if (i > loc)
                {
                    for (int j = 0; j < theMatrix[i].length; j++) {
                        if (j < loc)
                        {
                            newMatrix[i-1][j] = theMatrix[i][j];
                        }else if(j > loc){
                            newMatrix[i-1][j-1] = theMatrix[i][j];
                        }
                    }
                }
            }

            HashMap<File,Integer> newFiles = new HashMap<File,Integer>(getFileToIndex().size());
            HashMap<Integer,File> newIndexes = new HashMap<Integer,File>(getFileToIndex().size());
            for (Iterator<File> it = getFileToIndex().keySet().iterator(); it.hasNext();){
                File aFile = it.next();
                int pos = getFileToIndex().get(aFile);
                if(pos < loc){
                    newFiles.put(aFile, pos);
                    newIndexes.put(pos,aFile);
                }else{
                    newFiles.put(aFile, pos-1);
                    newIndexes.put(pos-1,aFile);
                }
            }

            theMatrix = newMatrix;
            fileToIndex = newFiles;
            indexToFile = newIndexes;
        }else{
            System.out.println("WARNING: The specified file (" + fileLocation.getPath()  + ") was not found in the DistanceMatrix and cannot be removed!");
        }
        
    }
    
    public void write(File theFile) throws IOException{
        BufferedWriter output = null;
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(theFile,false) );
            
            //Write name
            output.write( name );
            output.newLine();
            
            //Write file names
            File[] audioFiles = getFiles();
            for (int i = 0; i < audioFiles.length; i++) {
                output.write( (i+1) + "\t" + audioFiles[i].getPath() );
                output.newLine();
            }
            
            //write header line
            String header = "Q/R";
            for (int i = 0; i < audioFiles.length; i++) {
                header += "\t" + (i+1);
            }
            output.write( header );
            output.newLine();
            
            //Write data matrix
            for (int i = 0; i < audioFiles.length; i++) {
                String line = (i+1) + "\t" + theMatrix[i][0];
                for (int j = 1; j < audioFiles.length; j++)
                {
                    line += "\t" + theMatrix[i][j];
                }
                output.write( line );
                output.newLine();
            }
            
        } finally {
            //flush and close both "output" and its underlying FileWriter
            if (output != null){
                output.flush();
                output.close();
            }
        }
    }
    
    public static DenseDistanceMatrix read(File theFile) throws IOException{
        if (theFile.exists())
        {
            if(theFile.canRead()){
                BufferedReader textBuffer;
                ArrayList rowData = new ArrayList();
                int maxRowLength = 0;
                try
                {
                    textBuffer = new BufferedReader( new FileReader(theFile) );
                }
                catch(java.io.FileNotFoundException fnfe)
                {
                    throw new RuntimeException("The specified file does not exist.\n\tDistance matrix file: " + theFile.getPath());
                }
                String line = null; 
                List<String> files = new ArrayList<String>();
                String name;
                DenseDistanceMatrix distanceMatrix = null;
                try
                {
                    //read matrix name
                    name = textBuffer.readLine();
                    //read files
                    line = textBuffer.readLine();
                    while ((line != null) && !( (line.toLowerCase().startsWith("Q/R".toLowerCase())) || (line.toLowerCase().startsWith("Q\\R".toLowerCase())) ) )
                    {
                        String[] comps = line.split("\t");
                        files.add(comps[1]);
                        line = textBuffer.readLine();
                    }
                    if (line == null)
                    {
                        //something went wrong
                        throw new RuntimeException("Unexpected end of file.\n\tDistance matrix file: " + theFile.getPath());
                    }else if( !( (line.toLowerCase().startsWith("Q/R".toLowerCase())) || (line.toLowerCase().startsWith("Q\\R".toLowerCase())) ) )
                    {
                        //Again, something went wrong
                        throw new RuntimeException("The file is not in the expected format\nDistance matrix file: " + theFile.getPath());
                    }
                    
                    line = textBuffer.readLine();
                    
                    //read distances
                    float[][] theMatrix = new float[files.size()][files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        String[] comps = line.split("\t");
                        if (comps.length < (files.size() + 1)){
                            throw new RuntimeException("Insufficient distances found for file " + (i+1) + ", " + (comps.length+1) + " found, " + files.size() + " required.\nDistance matrix file: " + theFile.getPath());
                        }if (comps.length > (files.size() + 1))
                        {
                            System.out.println("DistanceMatrix.read(): Warning: " + (comps.length - (files.size() + 1)) + " additional tokens were found on line " + (i+1) + " of file: " + theFile.getPath());
                        }
                        for (int j = 0; j < files.size(); j++) {
                            try{
                                theMatrix[i][j] = (float)Double.parseDouble(comps[j+1]);
                            }catch(NumberFormatException nfe){
                                if (comps[j+1].equalsIgnoreCase("inf")){
                                    theMatrix[i][j] = Float.POSITIVE_INFINITY;
                                }
                            }
                        }
                        line = textBuffer.readLine();
                    }
                    
                    //Format filenames for use in DistanceMatrix
                    String[] fileStrs = files.toArray(new String[files.size()]);
    
                    File[] filesArr = new File[files.size()];
                    for (int i = 0; i < fileStrs.length; i++) {
                        filesArr[i] = new File(fileStrs[i]);
                    }
                    
                    //create distance matrix object
                    distanceMatrix = new DenseDistanceMatrix(name, theMatrix, filesArr);
                }
                catch (java.io.IOException ioe)
                {
                    textBuffer.close();
                    throw new java.io.IOException("An IOException occured while reading file: " + theFile.getPath() + "\n" + ioe);
                }
                catch (java.lang.NullPointerException npe)
                {
                    textBuffer.close();
                    npe.printStackTrace();
                    throw new RuntimeException("NullPointerException caused by: " + theFile.getPath());
                }
                finally
                {
                    textBuffer.close();
                }
                
                return distanceMatrix;
            }else{
                throw new IOException("The file: " + theFile.getPath() + " is not readable!");
            }
        }else{
            throw new FileNotFoundException("The file: " + theFile.getPath() + " was not found!");
        }
    }
    
    
    public void testTriangularInequality(File storageDirForReportAndPlots, int triIneqRandomSeed, int numTriIneqTests) throws noMetadataException {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String reportTime = calendar.getTime().toString();
        
        File[] filesArr = getFiles();
        int indexSize = indexSize();
        
        //calculate #number of times traingular inequality held, which is when d(A,C) <= (d(A,B) + d (B,C)
        System.out.println("Counting number of times traingular inequality held in " + numTriIneqTests + " random tests on " + indexSize + " files...");
        
        int timesTriangularInequalityHeld = 0;
        double percentTriangularInequalityHeld = 0.0;
        Random rnd = new Random(triIneqRandomSeed);
        int count = 0;
        int outCount = 0;
        
        
        while(count < numTriIneqTests){    
            if (outCount == 10000)
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
            
            if (getDistance(filesArr[a],filesArr[c]) <= (getDistance(filesArr[a],filesArr[b]) + getDistance(filesArr[b],filesArr[c]))){
                if (getDistance(filesArr[a],filesArr[b]) <= (getDistance(filesArr[a],filesArr[c]) + getDistance(filesArr[c],filesArr[b]))){
                    if (getDistance(filesArr[b],filesArr[c]) <= (getDistance(filesArr[b],filesArr[a]) + getDistance(filesArr[a],filesArr[c]))){
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
        String msg = "Distance Matrix: " + getName() + "\n";
        
        msg += "Report time stamp: " + reportTime + "\n\n";
        
        msg += "---\n";
        msg += "Number of times the triangular inequality held in " + numTriIneqTests + ": " + timesTriangularInequalityHeld + " (" + (percentTriangularInequalityHeld * 100.0) + "%)\n";
        
        System.out.println(msg);
        
        //write report to disk
        if (storageDirForReportAndPlots != null){
            BufferedWriter outputWriter = null;

            File resultFile = new File(storageDirForReportAndPlots.getPath() + File.separator + "triIneq3sides.txt");
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

    public boolean containsFile(File aFile){
        return getFileToIndex().containsKey(aFile);
    }

    public SearchResult[] retrieveMostSimilar(Signal querySignal) throws noMetadataException{
        float[] dists = getDistances(querySignal.getFile());

        SearchResult[] outputResults = new SearchResult[dists.length];
        //normalise distances and create SearchResult Objects
        for (int j = 0; j < dists.length; j++) {
            if(dists[j] == Float.POSITIVE_INFINITY){
                outputResults[j] = new SearchResult(new Signal(getIndexToFile().get(j).getPath()),0.0f);
            }else{
                outputResults[j] = new SearchResult(new Signal(getIndexToFile().get(j).getPath()), 1.0f - ((dists[j] - min) / norm));
            }
        }

        Arrays.sort(outputResults);
        return outputResults;
        
    }

    public SearchResult[] retrieveNMostSimilar(Signal querySignal, int n) throws noMetadataException{
        float[] dists = getDistances(querySignal.getFile());

        SortedSet<SearchResult> results = new TreeSet<SearchResult>();
        for (int j = 0; j < dists.length; j++) {
            //if(dists[j] != Float.POSITIVE_INFINITY){
                float score = 1.0f - ((dists[j] - min) / norm);
                if ((results.size() < n) || (score >= results.last().getScore()) ){
                    results.add(new SearchResult(new Signal(getIndexToFile().get(j).getPath()), score));
                    //System.out.println("added " + j + ", score: " + score);
                    if (results.size() > n){
                        results.remove(results.last());
                    }
                }
            //}
        }

        SearchResult[] truncResults = results.toArray(new SearchResult[results.size()]);
        return truncResults;
    }

    /**
     * @return the fileToIndex
     */
    public HashMap<File, Integer> getFileToIndex(){
        return fileToIndex;
    }

    /**
     * @return the indexToFile
     */
    public HashMap<Integer, File> getIndexToFile(){
        return indexToFile;
    }

    
}
