/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.util.retrieval;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kriswest
 */
public interface DistanceMatrixInterface extends Serializable {

    float getDistance(File file1, File file2);

//    float[][] getDistanceMatrix();
//
//    float[] getDistances(File file);

    File[] getFiles();

    boolean containsFile(File aFile);

    String getName();

    int indexSize();

    public SearchResult[] retrieveMostSimilar(Signal querySignal) throws noMetadataException;

    public SearchResult[] retrieveNMostSimilar(Signal querySignal, int n) throws noMetadataException;

    void removeFile(File fileLocation);

    void testTriangularInequality(File storageDirForReportAndPlots,
                                  int triIneqRandomSeed, int numTriIneqTests)
            throws noMetadataException;

    void write(File theFile) throws IOException;

}
