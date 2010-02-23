/*
 * LikelihoodRetrievalInterface.java
 *
 * Created on 31 March 2006, 15:53
 *
 *
 */

package org.imirsel.m2k.util.retrieval;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.util.retrieval.RetrieverInterface;

/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public interface SimpleDistMeasureRetrievalInterface extends RetrieverInterface {
    
    /** Add a transcription to the retriever */
    public void acceptFeatureProfile(Signal featureSignal) throws noMetadataException;
    
    /** Add a transcriptions to the retriever */
    public void acceptFeatureProfiles(Signal[] featureSignals) throws noMetadataException;
    
}
