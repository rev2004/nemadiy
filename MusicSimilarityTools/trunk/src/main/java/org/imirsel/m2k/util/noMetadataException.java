package org.imirsel.m2k.util;



import java.lang.*;

/**
 * An exception thrown when an element of metadata is requested but has not been set
 * @author  kw
 */
public class noMetadataException extends RuntimeException {

    /**
     * Creates a new instance of the noMetadataException with the specified message
     * @param s The message to initialise the exception with
     */
    public noMetadataException(String s) {
        super(s);    // Disallow initCause
    }
}
