/*
 * RetrieverInterface.java
 *
 * Created on 31 March 2006, 15:54
 *
 *
 */

package org.imirsel.m2k.util.retrieval;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 * A simple RetrieverInterface wrapper for a DistanceMatrix Object.
 * No similarity tests are performed, just a simple lookup in the distance
 * matrix.
 *
 * @author Kris West
 */
public class DistanceMatrixRetriever implements RetrieverInterface{ 
    DistanceMatrix theDistMatrix = null;
    float min = Float.MAX_VALUE;
    float max = Float.MIN_VALUE;
    
    public DistanceMatrixRetriever()
    {
        
    }
    
    public DistanceMatrixRetriever(DistanceMatrix theDistMatrix_)
    {
        theDistMatrix = theDistMatrix_;
        
        //find min and max values
        float[][] dists = this.theDistMatrix.getDistanceMatrix();
        
        boolean complete = true;
        for (int i = 0; i < dists.length; i++) {
            for (int j = 0; j < dists[i].length; j++) {
                if (dists[i][j] < min)
                {
                    min = dists[i][j];
                }else if(dists[i][j] > max)
                {
                    max = dists[i][j];
                }
            }
            if (dists[i].length != dists.length)
            {
                complete = false;
                System.out.println("Inconsistent number of distances found. Index contains " + dists.length + " files but " + dists[i].length + " were found for file index " + i);
            }
        }
        
        if (complete){
            System.out.println("Created distance matrix based retriever, indexing " + dists.length + " Objects with min dist: " + min + " and max dist: " + max);
        }else{
            System.out.println("WARNING: Incomplete or corrupted distance matrix received!\nProceeding anyway...");
        }
    }
    
    /**
     * If necessary the index is built (for example an LSI index might be extracted)
     */
    public void finaliseIndex(){
        
    }

    /**
     * Returns the name of the music file retriever.
     * @return the name of the music file retriever.
     */
    public String getRetrieverName(){
        String suff = "null";
        if (this.theDistMatrix != null)
        {
            suff = this.theDistMatrix.getName();
        }
        return "DistanceMatrixRetriever - " + suff;
    }

    /**
     * Returns the indexed Signal Object for the specified file name and path.
     * @param fileLocation File name and path of file represented by the Signal Object to retrieve.
     * @return the indexed Signal Object for the specified file name and path.
     */
    public Signal getSignal(String fileLocation){
        File theFile = new File(fileLocation);
        List files = Arrays.asList(this.theDistMatrix.getFiles());
        if (files.contains(theFile)){
            return new Signal(fileLocation);
        }else{
            throw new RuntimeException("The Signal Object requested (" + fileLocation + ") was not found in the distance matrix!");
        }
    }

    /**
     * Returns the list of Signal Objects currently indexed.
     * @return the list of Signal Objects currently indexed.
     */
    public Signal[] getSignals(){
        File[] files = this.theDistMatrix.getFiles();
        Signal[] sigs = new Signal[files.length];
        for (int i = 0; i < files.length; i++) {
            sigs[i] = new Signal(files[i].getPath());
        }
        return sigs;
    }
    
    /**
     * Returns the list of Files currently indexed.
     * @return the list of Files currently indexed.
     */
    public String[] getFiles(){
        File[] files = this.theDistMatrix.getFiles();
        String[] paths = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            paths[i] = files[i].getPath();
        }
        return paths;
    }

    /**
     * Order the entire collection based on the specified SignalObject as a query.
     * Results are returned as an array of SearchResult Objects.
     * @param querySignal The Signal Object to use as the query.
     * @return An array of SearchResult Objects representing the entire collection.
     * @throws org.imirsel.m2k.util.noMetadataException Should be thrown if required metadata is not found in the query object.
     */
    public SearchResult[] retrieveMostSimilar(Signal querySignal) throws noMetadataException{
        List files = Arrays.asList(this.theDistMatrix.getFiles());
        
        int idx = files.indexOf(querySignal.getFile());
        if(idx<0){
            throw new RuntimeException("The Signal Object requested (" + querySignal.getFile().getPath() + ") was not found in the distance matrix!");
        }else{
            float[] dists = this.theDistMatrix.getDistances((File)files.get(idx));
            
            
            SearchResult[] outputResults = new SearchResult[dists.length];
            //normalise distances and create SearchResult Objects
            for (int j = 0; j < dists.length; j++) {
                outputResults[j] = new SearchResult(this.getSignal(((File)files.get(j)).getPath()), 1.0f - ((dists[j] - min) / max));
            }
            
            Arrays.sort(outputResults);
            return outputResults;
        }
    }

    /**
     * Retrieve the N most similar examples to the query Signal Object.
     * Results are returned as an array of SearchResult Objects of length N.
     * @param querySignal The Signal Object to use as the query.
     * @param N Number of results to retrieve.
     * @throws org.imirsel.m2k.util.noMetadataException Should be thrown if required metadata is not found in the query object.
     * @return an array of SearchResult Objects of length N representing the search results.
     */
    public SearchResult[] retrieveNMostSimilar(Signal querySignal, int N) throws noMetadataException{
        List files = Arrays.asList(this.theDistMatrix.getFiles());
        
        int idx = files.indexOf(querySignal.getFile());
        if(idx<0){
            throw new RuntimeException("The Signal Object requested (" + querySignal.getFile().getPath() + ") was not found in the distance matrix!");
        }else{
            float[] dists = this.theDistMatrix.getDistances((File)files.get(idx));
            
            SortedSet results = new TreeSet();
            for (int j = 0; j < dists.length; j++) {
                float score = 1.0f - ((dists[j] - min) / max);
                if ( (results.size() < N) || (score >= ((SearchResult)results.last()).getScore()) ){
                    results.add(new SearchResult(this.getSignal(((File)files.get(j)).getPath()), score));
                    //System.out.println("added " + j + ", score: " + score);
                    if (results.size() > N){
                        results.remove(results.last());
                    }
                }
            }
            
            SearchResult[] truncResults = (SearchResult[])results.toArray(new SearchResult[N]);
    
/*for (int i = 0; i < truncResults.length; i++) {
    if (truncResults[i] == null)
    {
        System.out.println("returning null result at idx: " + i);
    }
}*/
            //System.out.println("high score: " + ((SearchResult)results.first()).getScore() + ", low score: " + ((SearchResult)results.last()).getScore());
            return truncResults;
        }
    }
    
    
    /**
     * Returns a 2D double array representing the Similarity of each Object indexed by 
     * the retriever to each other Object, in the range 0:1 where the diagonal should 
     * be 1.0. The matrix should be ordered exactly the same as the output from 
     * <code>getSignals()</code> and <code>getFiles()</code>.
     * @return a 2D double array representing the Similarity of each Object indexed by 
     * the retriever to each other Object, in the range 0:1
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found in an Object being queried for the matrix.
     */
    public float[][] getSimilarityMatrix() throws noMetadataException{
        //get distance matrix
        float[][] dists = this.theDistMatrix.getDistanceMatrix();
        
        //normalise distances and convert to similarities
        //!!!FRAGILE!!! what if all dists are zero or max dist is zero - kw
        float[][] sims = new float[dists.length][];
        for (int i = 0; i < dists.length; i++) {
            sims[i] = new float[dists[i].length];
            for (int j = 0; j < dists[i].length; j++) {
                sims[i][j] = 1.0f - ((dists[i][j] - min) / max);
            }
        }
        
        //return similarities
        return sims;
    }
    
    public DistanceMatrix getDistanceMatrix() throws noMetadataException{
        return this.theDistMatrix;
    }
}
