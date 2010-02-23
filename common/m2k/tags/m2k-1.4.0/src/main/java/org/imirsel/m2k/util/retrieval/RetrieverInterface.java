/*
 * RetrieverInterface.java
 *
 * Created on 31 March 2006, 15:54
 *
 *
 */

package org.imirsel.m2k.util.retrieval;

import java.io.Serializable;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 * An interface specifying the methods of classes that retrieve audio files 
 * (represented by Signal Objects) with content or example-based queries.
 * @author Kris West
 */
public interface RetrieverInterface extends Serializable{ 
    /**
     * If necessary the index is built (for example an LSI index might be extracted)
     */
    public void finaliseIndex();

    /**
     * Returns the name of the music file retriever.
     * @return the name of the music file retriever.
     */
    public String getRetrieverName();

    /**
     * Returns the indexed Signal Object for the specified file name and path.
     * @param fileLocation File name and path of file represented by the Signal Object to retrieve.
     * @return the indexed Signal Object for the specified file name and path.
     */
    public Signal getSignal(String fileLocation);

    /**
     * Returns the list of Signal Objects currently indexed.
     * @return the list of Signal Objects currently indexed.
     */
    public Signal[] getSignals();
    
    /**
     * Returns the list of Files currently indexed.
     * @return the list of Files currently indexed.
     */
    public String[] getFiles();

    /**
     * Order the entire collection based on the specified SignalObject as a query.
     * Results are returned as an array of SearchResult Objects.
     * @param querySignal The Signal Object to use as the query.
     * @return An array of SearchResult Objects representing the entire collection.
     * @throws org.imirsel.m2k.util.noMetadataException Should be thrown if required metadata is not found in the query object.
     */
    public SearchResult[] retrieveMostSimilar(Signal querySignal) throws noMetadataException;

    /**
     * ORetrieve the N most similar examples to the query Signal Object.
     * Results are returned as an array of SearchResult Objects of length N.
     * @param querySignal The Signal Object to use as the query.
     * @param N Number of results to retrieve.
     * @throws org.imirsel.m2k.util.noMetadataException Should be thrown if required metadata is not found in the query object.
     * @return an array of SearchResult Objects of length N representing the search results.
     */
    public SearchResult[] retrieveNMostSimilar(Signal querySignal, int N) throws noMetadataException;
    
//
//    /**
//     * Returns a 2D double array representing the Similarity of each Object indexed by
//     * the retriever to each other Object, in the range 0:1 where the diagonal should
//     * be 1.0. The matrix should be ordered exactly the same as the output from
//     * <code>getSignals()</code> and <code>getFiles()</code>.
//     * @return a 2D double array representing the Similarity of each Object indexed by
//     * the retriever to each other Object, in the range 0:1
//     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found in an Object being queried for the matrix.
//     */
//    public float[][] getSimilarityMatrix() throws noMetadataException;
    
    /** 
     * Returns a <CODE>DistanceMatrix</CODE> Object representing the distance
     * of each Object indexed by the retriever to each other Object. The matrix 
     * should be ordered exactly the same as the output from 
     * <code>getSignals()</code> and <code>getFiles()</code>.
     * @return a <CODE>DistanceMatrix</CODE> Object representing the distance of 
     * each Object indexed by the retriever to each other Object.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required 
     * metadata is not found in an Object being queried for the matrix.
     */
    public DistanceMatrixInterface getDistanceMatrix() throws noMetadataException;
}
