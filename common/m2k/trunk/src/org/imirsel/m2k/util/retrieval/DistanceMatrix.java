package org.imirsel.m2k.util.retrieval;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.imirsel.m2k.util.noMetadataException;


/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class DistanceMatrix implements Serializable{
    
    /** A matrix of similarity scores indexed [row][column] where each row 
     *  represents the distance scores from the corresponding file in the 
     *  audioFiles array.*/
    private float[][] theMatrix;

    /** The list of file objects representing the files that distances are 
     * calculated between.
     */
    private File[] audioFiles;
    
    /** Name to be used to identify this distance Matrix */
    private String name;
    
    /**
     * Creates a new instance of DistanceMatrix
     */
    public DistanceMatrix() {
        theMatrix = null;
        audioFiles = null;
        name = "";
    }
    
    /*public void removeFileFromMatrix(File theFile){
        int idx = Arrays.asList(audioFiles).indexOf(theFile);
        if (idx == -1){
            throw new RuntimeException("The file: " + theFile.getPath() + " was not indexed by the DistanceMatrix!");
        }
        
        File[] audioFiles_ = new File[audioFiles.length-1];
        float[][] theMatrix_ = new float[audioFiles.length-1][audioFiles.length-1];
        for (int i = 0; i < audioFiles.length; i++) {
            if (i < idx)
            {
                audioFiles_[i] = audioFiles[i];
                for (int j = 0; j < audioFiles.length; j++) {
                    if (j < idx){
                        theMatrix_[i][j] = theMatrix[i][j];
                    }else if (j > idx){
                        theMatrix_[i][j-1] = theMatrix[i][j];
                    }
                }
            }
            else if (i > idx){
                audioFiles_[i-1] = audioFiles[i];
                for (int j = 0; j < audioFiles.length; j++) {
                    if (j < idx){
                        theMatrix_[i-1][j] = theMatrix[i][j];
                    }else if (j > idx){
                        theMatrix_[i-1][j-1] = theMatrix[i][j];
                    }
                }
            }
        }
        
        audioFiles = audioFiles_;
        theMatrix = theMatrix_;
    }*/
    
    /**
     * Creates a new instance of DistanceMatrix using the specified matrix of 
     * doubles and the array of file names.
     */
    public DistanceMatrix(String name_, float[][] aMatrix, File[] someFiles) {
        if (name_ == null){
            name = "<no name given>";
        }else{
            name = name_;
        }
        theMatrix = aMatrix;
        audioFiles = someFiles;
    }
    
    public float getDistance(File file1, File file2)
    {
        List fileList = Arrays.asList(audioFiles);
        try{
            return theMatrix[fileList.indexOf(file1)][fileList.indexOf(file2)];
        }catch(ArrayIndexOutOfBoundsException arex)
        {
            throw new RuntimeException("No distances were found for one of the files passed!\n Query file: " + file1.getPath() + "\nResult file: " + file2.getPath());
        }
    }
    
    public float[] getDistances(File file)
    {
        List fileList = Arrays.asList(audioFiles);
        try{
            return theMatrix[fileList.indexOf(file)];
        }catch(ArrayIndexOutOfBoundsException arex)
        {
            throw new RuntimeException("No distances were found for the file passed!\n Query file: " + file.getPath());
        }
    }
    
    public float[][] getDistanceMatrix()
    {
        return theMatrix;
    }
    
    public File[] getFiles()
    {
        return audioFiles;
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
            return audioFiles.length;
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
        
        int loc = Arrays.asList(audioFiles).indexOf(new File(oldLocation));
        if(loc != -1){
            audioFiles[loc] = new File(newLocation);
        }else{
            throw new RuntimeException("The specified file was not found in the DistanceMatrix and cannot be remapped to a new location!\nFile: " + oldLocation + "\nTo be mapped to: " + newLocation);
        }
    }
    
    public void removeFile(File fileLocation){
        int loc = Arrays.asList(audioFiles).indexOf(fileLocation);
        if(loc != -1){
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
            File[] newFiles = new File[audioFiles.length-1];
            for (int i = 0; i < audioFiles.length; i++) {
                if (i<loc)
                {
                    newFiles[i] = audioFiles[i];
                }else if (i>loc){
                    newFiles[i-1] = audioFiles[i];
                }
            }
            theMatrix = newMatrix;
            audioFiles = newFiles;
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
    
    public static DistanceMatrix read(File theFile) throws IOException{
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
                    throw new RuntimeException("The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n\tDistance matrix file: " + theFile.getPath());
                }
                String line = null; 
                List files = new ArrayList();
                String name;
                DistanceMatrix distanceMatrix = null;
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
                        throw new RuntimeException("Unexpected end of file, this exception should never be thrown and indicates a serious bug.\nDistance matrix file: " + theFile.getPath());
                    }else if( !( (line.toLowerCase().startsWith("Q/R".toLowerCase())) || (line.toLowerCase().startsWith("Q\\R".toLowerCase())) ) )
                    {
                        //Again, something went wrong
                        throw new RuntimeException("The file is not in the expected format, this exception should never be thrown and indicates a serious bug.\nDistance matrix file: " + theFile.getPath());
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
                            theMatrix[i][j] = (float)Double.parseDouble(comps[j+1]);
                        }
                        line = textBuffer.readLine();
                    }
                    
                    //Format filenames for use in DistanceMatrix
                    String[] fileStrs = (String[])files.toArray(new String[files.size()]);
    
                    File[] filesArr = new File[files.size()];
                    for (int i = 0; i < fileStrs.length; i++) {
                        filesArr[i] = new File(fileStrs[i]);
                    }
                    
                    
                    //create distance matrix object
                    distanceMatrix = new DistanceMatrix(name, theMatrix, filesArr);
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
}
