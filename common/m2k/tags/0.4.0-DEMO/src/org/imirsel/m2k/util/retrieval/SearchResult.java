package org.imirsel.m2k.util.retrieval;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.lang.Comparable;

/**
 * A simple class to represent a search result within an M2K-based retrieval system.
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class SearchResult implements Comparable{ 
    
    private Signal theResult;
    private float score = 0.0f;
    
    /**
     * Creates a new instance of SearchResult
     * @param theResult_ A Signal Object representing the audio file for this result.
     * @param score_ The score for this result, to be used in ranking.
     */
    public SearchResult(Signal theResult_, float score_) {
        theResult = theResult_;
        if (theResult == null){
            throw new RuntimeException("The result was null??");
        }
                
        score = score_;
    }

    /**
     * Returns the Signal Object representing the search result.
     * @return Returns the Signal Object representing the search result.
     */
    public Signal getTheResult() {
        return theResult;
        
    }

    /**
     * Sets the Signal Object representing the search result.
     * @param theResult the Signal Object representing the search result.
     */
    public void setTheResult(Signal theResult) {
        this.theResult = theResult;
        if (theResult == null){
            throw new RuntimeException("The result was null??");
        }
    }

    /**
     * Returns the score for this search result, intended to range from 0 
     * (irrelevant to 1.0 (100% relevant).
     * @return the score for this search result, intended to range from 0 
     * (irrelevant to 1.0 (100% relevant).
     */
    public float getScore() {
        return score;
    }

    /**
     * Sets the score for this search result, intended to range from 0 
     * (irrelevant to 1.0 (100% relevant).
     * @param score the score for this search result, intended to range from 0 
     * (irrelevant to 1.0 (100% relevant).
     */
    public void setScore(float score) {
        this.score = score;
    }
    
    /**
     * Compares two results for equality and ordering, to be used in sorting.
     * @param otherResult The SearchResult to compare with.
     * @return Returns -1 if this score is higher than the <code>otherResult</code>, 0 if they 
     * are equal, 1 if the other result is higher.
     */
    public int compareTo(Object otherResult) {
        float otherScore = ((SearchResult)otherResult).getScore();
        
        if (score == otherScore)
        {
            return 0 - (this.theResult.compareTo(((SearchResult)otherResult).getTheResult()));
        }else if (Float.isNaN(score)){
            return 1;
        }else if (Float.isNaN(otherScore)){
            return -1;
        }
        else{
            return 0 - (Float.compare(score,otherScore));
        }
    }
    
    public boolean equals(Object otherObject){
        return this.theResult.equals(((SearchResult)otherObject).getTheResult());
    }
}
