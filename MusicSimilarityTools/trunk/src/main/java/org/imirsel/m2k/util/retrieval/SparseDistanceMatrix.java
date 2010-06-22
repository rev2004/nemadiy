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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import org.imirsel.m2k.io.musicDB.RemapMusicDBFilenamesClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;


/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class SparseDistanceMatrix implements DistanceMatrixInterface{

    private static final long serialVersionUID = 1234823945825L;

    float min = Float.MAX_VALUE;
    float max = Float.MIN_VALUE;

    /** A map of distance scores from the corresponding file in the
     *  audioFiles array.*/
    private HashMap<File,HashMap<File,Float>> theDistanceMap;

    /** Name to be used to identify this distance Matrix */
    private String name;

    /**
     * Creates a new instance of DistanceMatrix
     */
    public SparseDistanceMatrix() {
        theDistanceMap = new HashMap<File,HashMap<File,Float>>();
        name = "";
    }
    
    /**
     * @param name_
     * @param aMap 
     */
    public SparseDistanceMatrix(String name_, HashMap<File,HashMap<File,Float>> aMap) {
        if (name_ == null){
            name = "<no name given>";
        }else{
            name = name_;
        }
        theDistanceMap = aMap;
        File file;

        HashSet<File> files = new HashSet<File>();

        System.out.println("Files with distances: " + theDistanceMap.size());
        HashMap<File,Float> dists;
        for (Iterator<File> it = theDistanceMap.keySet().iterator(); it.hasNext();){
            file = it.next();
            dists = theDistanceMap.get(file);
            files.addAll(dists.keySet());
        }
        System.out.println("Files that appear in results) " + files.size());
        computeMinAndMax();
    }

    public void computeMinAndMax(){
        File[] files = getFiles();
        Float aFloat;
        for (int i = 0; i < files.length; i++){
            for (Iterator<Float> it = getDistances(files[i]).values().iterator(); it.hasNext();){
                aFloat = it.next();
                if (aFloat < min)
                {
                    min = aFloat;
                }else if(aFloat > max)
                {
                    max = aFloat;
                }
            }
        }
        System.out.println("Sparse distance matrix, indexing " + files.length + " queries with min dist: " + min + " and max dist: " + max);
    }

    public float getDistance(File file1, File file2)
    {
        HashMap<File,Float> dists = theDistanceMap.get(file1);
        if(dists == null){
            throw new RuntimeException("No distances were found for query file: " + file1.getPath());
        }
        Float dist = dists.get(file2);
        if(dist == null){
            return max + Float.MIN_VALUE;
        }else{
            return dist.floatValue();
        }
    }
    
    public HashMap<File,Float> getDistances(File file)
    {
        return theDistanceMap.get(file);
    }
    
    public HashMap<File,HashMap<File,Float>> getDistanceMatrix()
    {
        return theDistanceMap;
    }
    
    public File[] getFiles()
    {
        return theDistanceMap.keySet().toArray(new File[theDistanceMap.size()]);
    }
    
    public String getName()
    {
        return name;
    }
    
    public int indexSize()
    {
        if (theDistanceMap == null){
            return 0;
        }else {
            return theDistanceMap.keySet().size();
        }
    }
    
    /**
     * Changes all entries corresponding to the specified file name map to the new
     * file names given.
     * @param newFilesMap
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the specified (Old) file name is not found in the index.
     */
    public void remapFileLocations(Map<String,File> newFilesMap){

        HashMap<File,HashMap<File,Float>> newMap = new HashMap<File,HashMap<File,Float>>();

        int resultsDrops = 0;
        int queriesDrops = 0;
        HashSet<File> uniqueResultsDropped = new HashSet<File>();
        HashSet<File> queriesDropped = new HashSet<File>();

        File aFile;
        Float aResult;
        File newFile;

        //remap queries
        for (Iterator<File> it = theDistanceMap.keySet().iterator(); it.hasNext();){
            File query = it.next();
            File newLocation = newFilesMap.get(query.getName());

            if(newLocation == null){
                queriesDropped.add(query);
            }else{

                //remap results
                HashMap<File,Float> results = theDistanceMap.get(query);
                HashMap<File,Float> newResults = new HashMap<File,Float>();
                for (Iterator<File> it1 = results.keySet().iterator(); it1.hasNext();){
                    aFile = it1.next();
                    aResult = results.get(aFile);
                    newFile = newFilesMap.get(aFile.getName());
                    if(newFile == null){
                        uniqueResultsDropped.add(aFile);
                    }else{
                        newResults.put(newFile, aResult);
                    }
                }

                newMap.put(newLocation, newResults);
            }
        }


        if (queriesDrops != 0){
            System.out.println("Dropped " + queriesDrops + " which could not be remapped to new locations");
        }
        if(resultsDrops != 0){
            System.out.println("Dropped " + resultsDrops + " searchresults which could not be remapped tp new locations");
        }
    }

    /**
     * Changes all entries corresponding to the specified file name map to the new
     * file names given.
     * @param newFilesMap
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if the specified (Old) file name is not found in the index.
     */
    public void remapFileLocationsWithMIREXIDs(HashMap<String,File> newFilesMap){

        HashMap<File,HashMap<File,Float>> newMap = new HashMap<File,HashMap<File,Float>>();

        int resultsDrops = 0;
        int queriesDrops = 0;
        HashSet<File> uniqueResultsDropped = new HashSet<File>();
        HashSet<File> queriesDropped = new HashSet<File>();

        File aFile;
        Float aResult;
        File newFile;

        //remap queries
        for (Iterator<File> it = theDistanceMap.keySet().iterator(); it.hasNext();){
            File query = it.next();
            String queryKey = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(query);
            File newLocation = newFilesMap.get(queryKey);

            if(newLocation == null){
                queriesDropped.add(query);
            }else{

                //remap results
                HashMap<File,Float> results = theDistanceMap.get(query);
                HashMap<File,Float> newResults = new HashMap<File,Float>();
                for (Iterator<File> it1 = results.keySet().iterator(); it1.hasNext();){
                    aFile = it1.next();
                    aResult = results.get(aFile);
                    newFile = newFilesMap.get(RemapMusicDBFilenamesClass.convertFileToMIREX_ID(aFile));
                    if(newFile == null){
                        uniqueResultsDropped.add(aFile);
                    }else{
                        newResults.put(newFile, aResult);
                    }
                }

                newMap.put(newLocation, newResults);
            }
        }


        if (queriesDrops != 0){
            System.out.println("Dropped " + queriesDrops + " which could not be remapped to new locations");
        }
        if(resultsDrops != 0){
            System.out.println("Dropped " + resultsDrops + " searchresults which could not be remapped tp new locations");
        }
        theDistanceMap = newMap;
    }
    
    /**
     * Changes all file name entries to the id computed frm them by discarding 
     * path and anything after the first period in a file name.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if an (Old) file name is not found in the index.
     */
    public void remapFileLocationsToIDs(){

        HashMap<File,HashMap<File,Float>> newMap = new HashMap<File,HashMap<File,Float>>();

        
        File aFile;
        Float aResult;
        String newId;

        //remap queries
        for (Iterator<File> it = theDistanceMap.keySet().iterator(); it.hasNext();){
            File query = it.next();
            String queryKey = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(query);
            
            //remap results
            HashMap<File,Float> results = theDistanceMap.get(query);
            HashMap<File,Float> newResults = new HashMap<File,Float>();
            for (Iterator<File> it1 = results.keySet().iterator(); it1.hasNext();){
                aFile = it1.next();
                aResult = results.get(aFile);
                newId = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(aFile);
                newResults.put(new File(newId), aResult);
                
            }

            newMap.put(new File(queryKey), newResults);
        }

        theDistanceMap = newMap;
    }
    
    public void removeFile(File fileLocation){
        //remove from queries
        theDistanceMap.remove(fileLocation);

        //remove from results
        for (Iterator<HashMap<File,Float>> it = theDistanceMap.values().iterator(); it.hasNext();){
            it.next().remove(fileLocation);
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
            
            //Write data matrix
            for (Iterator<File> it = theDistanceMap.keySet().iterator(); it.hasNext();){
                File query = it.next();
                HashMap<File,Float> results = theDistanceMap.get(query);
                String line = query.getPath() + "\t";
                for (Iterator<File> it1 = results.keySet().iterator(); it1.hasNext();){
                    File resultKey = it1.next();
                    Float resultVal = results.get(resultKey);
                    line += resultKey.getName() + "," + resultVal;
                    if(it1.hasNext()){
                        line += "\t";
                    }
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
    
    public static SparseDistanceMatrix read(File theFile) throws IOException{
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
                SparseDistanceMatrix distanceMatrix = null;
                HashMap<File,HashMap<File,Float>> distMap = new HashMap<File, HashMap<File, Float>>();
                int numResults = 0;
                try
                {
                    //read matrix name
                    name = textBuffer.readLine();
                    //read data
                    line = textBuffer.readLine();

                    String[] lineComps;
                    String[] itemComps;
                    while (line != null)
                    {
                        line = line.trim();
                        if (!line.equals("")){
                            lineComps = line.split("\t");
                            HashMap<File,Float> results = new HashMap<File,Float>(lineComps.length);
                            for (int i = 1; i < lineComps.length; i++){
                                itemComps = lineComps[i].split(",");
                                if (itemComps.length != 2){
                                    String msg = "Error: could not interpret '";
                                    if(line.length() > 30){
                                        msg += line.substring(0,30) + "...";
                                    }else{
                                        msg += line;
                                    }
                                    msg += "'as a 'file,distance' pair";
                                    throw new RuntimeException(msg);
                                }else{
                                    results.put(new File(itemComps[0]),Float.parseFloat(itemComps[1]));
                                    numResults++;
                                }

                            }
                            distMap.put(new File(lineComps[0]), results);
                            line = textBuffer.readLine();

                            if (distMap.size() % 1000 == 0){
                                System.out.println("\tread " + distMap.size() + " queries...");
                            }
                        }
                    }
                    
                    //create distance matrix object
                    distanceMatrix = new SparseDistanceMatrix(name, distMap);
                    System.out.println("Read sparse distance matrix containing distances for " + distMap.size() + " queries, with a total of " + numResults + " distances");
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
//
//    class FileAndDist{
//        File theFile;
//        float dist;
//
//        public FileAndDist(File theFile, float dist){
//            this.theFile = theFile;
//            this.dist = dist;
//        }
//
//        public int compareTo(FileAndDist other){
//            return Float.compare(dist, other.dist);
//        }
//    }

    public boolean containsFile(File aFile){
        return theDistanceMap.containsKey(aFile);
    }

    public SearchResult[] retrieveMostSimilar(Signal querySignal) throws noMetadataException{
        HashMap<File,Float> results = theDistanceMap.get(querySignal.getFile());
        int count = 0;
        SearchResult[] resultsArr = new SearchResult[results.size()];
        File aFile;
        for (Iterator<File> it = results.keySet().iterator(); it.hasNext();){
            aFile = it.next();
            resultsArr[count++] = new SearchResult(new Signal(aFile.getPath()), 1.0f - ((results.get(aFile) - min) / max));
        }
        Arrays.sort(resultsArr);
        return resultsArr;

    }

    public SearchResult[] retrieveNMostSimilar(Signal querySignal, int n) throws noMetadataException{
        HashMap<File,Float> resultsMap = theDistanceMap.get(querySignal.getFile());
        int count = 0;

        SortedSet<SearchResult> results = new TreeSet<SearchResult>();
        File aFile;
        for (Iterator<File> it = resultsMap.keySet().iterator(); it.hasNext();){
            aFile = it.next();
            float score = 1.0f - ((resultsMap.get(aFile) - min) / max);
            if ((results.size() < n) || (score >= results.last().getScore()) ){
                results.add(new SearchResult(new Signal(aFile.getPath()), score));
                //System.out.println("added " + j + ", score: " + score);
                if (results.size() > n){
                    results.remove(results.last());
                }
            }
        }

        SearchResult[] truncResults = results.toArray(new SearchResult[results.size()]);
        return truncResults;
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
