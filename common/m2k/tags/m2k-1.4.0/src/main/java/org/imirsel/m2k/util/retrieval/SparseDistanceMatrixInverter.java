/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.util.retrieval;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads a sparse distance matrix that has been laoded with similarity values
 * in error, inverts it and outputs as a DenseDistanceMatrix
 * @author kriswest
 */
public class SparseDistanceMatrixInverter {



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            File matToLoad = new File(args[0]);
            File destination = new File(args[1]);

            System.out.println("Loading sparse matrix: " + matToLoad.getAbsolutePath());
            SparseDistanceMatrix sparse = SparseDistanceMatrix.read(matToLoad);

            String name = sparse.getName();
            File[] files = sparse.getFiles();

            System.out.println("Setting up dense matrix to populate");

            int matDim = files.length;
            float[][] theMat = new float[matDim][matDim];
            //set all vals to pos infinity (default if no distance is given)
            for (int i = 0; i < matDim; i++){
                for (int j = 0; j < matDim; j++){
                    theMat[i][j] = Float.POSITIVE_INFINITY;
                }
            }
            //set diagonal to zeros (no dist) in case no self comparisons are returned
            for (int i = 0; i < matDim; i++){
                theMat[i][i] = 0.0f;
            }

            //setup Dense dist mat
            DenseDistanceMatrix newMat = new DenseDistanceMatrix(name, theMat, files);
            Map<File,Integer> fileMap = newMat.getFileToIndex();
            Map<Integer,File> indexMap = newMat.getIndexToFile();
            theMat = newMat.getDistanceMatrix();

            System.out.println("Inverting and populating dense distance matrix");

            //get all available dists and populate matrix
            File queryFile, resultFile;
            int queryIdx, resultIdx;
            Map<File,Float> resultMap;
            for (int i = 0; i < files.length; i++){
                queryFile = files[i];
                queryIdx = fileMap.get(queryFile);
                resultMap = sparse.getDistances(queryFile);
                for (Iterator<File> it = resultMap.keySet().iterator(); it.hasNext();){
                    resultFile = it.next();
                    resultIdx = fileMap.get(resultFile);
                    theMat[queryIdx][resultIdx] = 1.0f - resultMap.get(resultFile);
                }
                if (i % 500 == 0){
                    System.out.println("\tdone " + i + " of " + files.length);
                }
            }

            newMat.computeMinAndMax();

            System.out.println("Writing output to: " + destination.getAbsolutePath());

            newMat.write(destination);

        }catch (IOException ex){
            Logger.getLogger(SparseDistanceMatrixInverter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
