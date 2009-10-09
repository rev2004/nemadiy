/*
 * RetrieverInterface.java
 *
 * Created on 31 March 2006, 15:54
 *
 *
 */

package org.imirsel.m2k.util.retrieval;

import java.io.File;
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

    public static final long serialVersionUID = 234578348538L;
    DistanceMatrixInterface theDistMatrix = null;
    
    public DistanceMatrixRetriever()
    {
        
    }
    
    public DistanceMatrixRetriever(DistanceMatrixInterface theDistMatrix_)
    {
        theDistMatrix = theDistMatrix_;
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
            if(theDistMatrix instanceof DenseDistanceMatrix){
                suff += " (dense)";
            }else{
                suff += " (sparse)";
            }
        }
        return "DistanceMatrixRetriever - " + suff;
    }

    /**
     * Returns the indexed Signal Object for the specified file name and path.
     * @param fileLocation File name and path of file represented by the Signal Object to retrieve.
     * @return the indexed Signal Object for the specified file name and path.
     */
    public Signal getSignal(String fileLocation){
        if (theDistMatrix.containsFile(new File(fileLocation))){
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
        return theDistMatrix.retrieveMostSimilar(querySignal);
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
        return theDistMatrix.retrieveNMostSimilar(querySignal,N);
    }
    
    
//    /**
//     * Returns a 2D double array representing the Similarity of each Object indexed by
//     * the retriever to each other Object, in the range 0:1 where the diagonal should
//     * be 1.0. The matrix should be ordered exactly the same as the output from
//     * <code>getSignals()</code> and <code>getFiles()</code>.
//     * @return a 2D double array representing the Similarity of each Object indexed by
//     * the retriever to each other Object, in the range 0:1
//     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found in an Object being queried for the matrix.
//     */
//    public float[][] getSimilarityMatrix() throws noMetadataException{
//        //get distance matrix
//        float[][] dists = this.theDistMatrix.getDistanceMatrix();
//
//        //normalise distances and convert to similarities
//        //!!!FRAGILE!!! what if all dists are zero or max dist is zero - kw
//        float[][] sims = new float[dists.length][];
//        for (int i = 0; i < dists.length; i++) {
//            sims[i] = new float[dists[i].length];
//            for (int j = 0; j < dists[i].length; j++) {
//                sims[i][j] = 1.0f - ((dists[i][j] - min) / max);
//            }
//        }
//
//        //return similarities
//        return sims;
//    }
//
    public DistanceMatrixInterface getDistanceMatrix() throws noMetadataException{
        return this.theDistMatrix;
    }

}
