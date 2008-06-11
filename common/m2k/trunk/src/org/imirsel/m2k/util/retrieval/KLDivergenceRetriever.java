package org.imirsel.m2k.util.retrieval;
import Jama.Matrix;
import java.io.File;
import org.imirsel.m2k.math.SignalMeanandCovarianceClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.HashMap;
import java.util.ArrayList;
import org.imirsel.m2k.util.retrieval.DistanceMatrix;
import org.imirsel.m2k.util.retrieval.SearchResult;

/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class KLDivergenceRetriever implements SimpleDistMeasureRetrievalInterface{
    
    //File name to symbol counts maps (map indexed by symbol linking to integer count)
    HashMap FileLocationToMeanMatrixMap = null;
    HashMap FileLocationToCovarMatrixMap = null;
    HashMap FileLocationToInvCovarMatrixMap = null;
    HashMap FileLocationToDetCovarMatrixMap = null;
    HashMap FileLocationToSignalMap = null;
    String[] Files = null;
    int numFeatures = -1;
    
    /** Creates a new instance of cooccurenceRetriever */
    public KLDivergenceRetriever() {
        FileLocationToMeanMatrixMap = new HashMap();
        FileLocationToCovarMatrixMap = new HashMap();
        FileLocationToInvCovarMatrixMap = new HashMap();
        FileLocationToDetCovarMatrixMap = new HashMap();
        FileLocationToSignalMap = new HashMap();
    }
    
    public String getRetrieverName(){
        String name = "KL Divergence retriever ";
        return name;
        
    }
    
    /** If necessary the index is built (for example an LSI index might be extracted)
     */
    public void finaliseIndex() {
        Files = (String[])this.FileLocationToSignalMap.keySet().toArray(new String[FileLocationToSignalMap.size()]);
        System.out.println(Files.length + " tracks indexed");
    }
    
    /** Add a transcription to the retriever */
    public void acceptFeatureProfile(Signal featureSignal) throws noMetadataException{
        this.Files = null;
        FileLocationToSignalMap.put(featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION),featureSignal);
        Matrix[] mats = SignalMeanandCovarianceClass.reconstructMeanAndCovarianceMatrices(featureSignal); 
        FileLocationToMeanMatrixMap.put(featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), mats[0]);
        try{
            FileLocationToInvCovarMatrixMap.put(featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), mats[1].inverse());
        }catch(RuntimeException e){
            System.out.println("Singular covar matrix detected for " + featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION) + " diagonalising");
            System.out.println("Original features: ");
            double[] row = featureSignal.getDataRow(0);
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i] + " ");
            }
            System.out.println("");
            
            //singular matrix won't invert, diagonalise and try again
            double[][] diagCovarMat = mats[1].getArrayCopy();
            for (int i = 0; i < diagCovarMat.length; i++) {
                for (int j = 0; j < diagCovarMat[i].length; j++) {
                    if (i!=j){
                        diagCovarMat[i][j] = 0;
                    }
                }
            }
            mats[1] = new Matrix(diagCovarMat);
            FileLocationToInvCovarMatrixMap.put(featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), mats[1].inverse());
        }
        FileLocationToCovarMatrixMap.put(featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), mats[1]);
        FileLocationToDetCovarMatrixMap.put(featureSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), new Double(mats[1].det()));
        if (numFeatures == -1){
            numFeatures = mats[0].getArray().length;
        }else if (numFeatures != mats[0].getArray().length)
        {
            throw new RuntimeException("The number of features (" + mats[0].getArray()[0].length + ") did not match previous Signal objects (" + numFeatures + ")");
        }
    }
    
    /** Add a transcriptions to the retriever */
    public void acceptFeatureProfiles(Signal[] featureSignals) throws noMetadataException {
        for (int i = 0; i < featureSignals.length; i++) {
            acceptFeatureProfile(featureSignals[i]);
        }
    }
    
    /** Returns the list of Signal Objects currently indexed **/
    public Signal[] getSignals(){
        Object[] entries = FileLocationToSignalMap.values().toArray();
        Signal[] out = new Signal[entries.length];
        for(int i=0;i<entries.length;i++) {
            out[i] = (Signal)entries[i];
        }
        return out;
    }
    
    public String[] getFiles(){
        if (this.Files == null) {
            this.finaliseIndex();
        }
        return this.Files;
    }
    
    /** Returns the Signal Object for the specified file */
    public Signal getSignal(String fileLocation){
        return (Signal)FileLocationToSignalMap.get(fileLocation);
    }
    
    /** Retrieve transcriptions most similar to the query */
    public SearchResult[] retrieveMostSimilar(Signal featuresSignal) throws noMetadataException{
        if (this.Files == null) {
            this.finaliseIndex();
        }
        ArrayList results = new ArrayList();
        
        String queryLocation = featuresSignal.getStringMetadata(Signal.PROP_FILE_LOCATION);
        
        //get features
        double det = ((Double)FileLocationToDetCovarMatrixMap.get(queryLocation)).doubleValue();
        Matrix means = (Matrix)FileLocationToMeanMatrixMap.get(queryLocation);
        Matrix covar = (Matrix)FileLocationToCovarMatrixMap.get(queryLocation);
        Matrix invCovar = (Matrix)FileLocationToInvCovarMatrixMap.get(queryLocation);
        
        //int numFeatures = means.getArray()[0].length;
        
        //iterate through all files
        for (int i = 0; i < Files.length; i++) {
            //get features
            double test_det = ((Double)FileLocationToDetCovarMatrixMap.get(Files[i])).doubleValue();
            Matrix test_means = (Matrix)FileLocationToMeanMatrixMap.get(Files[i]);
            Matrix test_covar = (Matrix)FileLocationToCovarMatrixMap.get(Files[i]);
            Matrix test_invCovar = (Matrix)FileLocationToInvCovarMatrixMap.get(Files[i]);
            float score = calculateSymmetricKLDivergence(numFeatures, covar, test_covar, invCovar, test_invCovar, means, test_means, det, test_det);
            System.out.println("Score: " + score);
                    
            score = 1.0f - score;
            
            Signal outSig = (Signal)FileLocationToSignalMap.get((String)Files[i]);
            results.add(new SearchResult(outSig,score));
        }
        SearchResult[] outputResults = (SearchResult[])results.toArray(new SearchResult[results.size()]);
        java.util.Arrays.sort((Object[])outputResults);
        return outputResults;
    }

    private float calculateSymmetricKLDivergence(final int numFeatures, final Matrix covar, final Matrix test_covar, final Matrix invCovar, final Matrix test_invCovar, final Matrix means, final Matrix test_means, final double det, final double test_det) {
        float score = calculateKLDivergence(numFeatures, test_det, test_means, covar, det, means, test_invCovar);
        score += calculateKLDivergence(numFeatures, det, means, test_covar, test_det, test_means, invCovar);
        score = score / 2;
        return score;
    }

    private float calculateKLDivergence(final int numFeatures, final double test_det, final Matrix test_means, final Matrix covar, final double det, final Matrix means, final Matrix test_invCovar) {
        
        //KL Divergence between features
        float score = 0.0f;
        score += Math.log(test_det / det);
        score += ((Matrix)test_invCovar.times(covar)).trace();

        //printDimensions(means, "mean");
        //printDimensions(test_means, "test mean");
        //printDimensions(test_invCovar, "test_invCovar");
        
        Matrix meanDiff = means.minus(test_means);
        Matrix term = meanDiff.transpose().times(test_invCovar).times(meanDiff);
        //System.out.println("num cols: " + term.getColumnDimension() + ", num rows: " + term.getRowDimension());
        score += term.get(0,0);
        score -= numFeatures;
        score /= 2;
        return score;
    }

    private void printDimensions(final Matrix means, final String matrixString) {
        System.out.println(matrixString+ " rows " + means.getRowDimension() + ", cols " + means.getColumnDimension());
    }
    
    public float[][] getSimilarityMatrix(){
        //iterate through all files
        if (Files == null) {
            this.finaliseIndex();
        }
        
        
        float[][] simMatrix = new float[Files.length][Files.length];
        
        for (int i = 0; i < Files.length; i++) {
            //get features
            double det = ((Double)FileLocationToDetCovarMatrixMap.get(Files[i])).doubleValue();
            Matrix means = (Matrix)FileLocationToMeanMatrixMap.get(Files[i]);
            Matrix covar = (Matrix)FileLocationToCovarMatrixMap.get(Files[i]);
            Matrix invCovar = (Matrix)FileLocationToInvCovarMatrixMap.get(Files[i]);
            
            simMatrix[i][i] = 1.0f;
            for (int j = i+1; j < Files.length; j++) {
                //get features
                double test_det = ((Double)FileLocationToDetCovarMatrixMap.get(Files[j])).doubleValue();
                Matrix test_means = (Matrix)FileLocationToMeanMatrixMap.get(Files[j]);
                Matrix test_covar = (Matrix)FileLocationToCovarMatrixMap.get(Files[j]);
                Matrix test_invCovar = (Matrix)FileLocationToInvCovarMatrixMap.get(Files[j]);
                
                float score = 1.0f - calculateSymmetricKLDivergence(numFeatures, covar, test_covar, invCovar, test_invCovar, means, test_means, det, test_det);
                simMatrix[i][j] = simMatrix[j][i] = score;
            }
        }
        return simMatrix;
    }
    
    public DistanceMatrix getDistanceMatrix(){
        //iterate through all files
        if (Files == null) {
            this.finaliseIndex();
        }
        float[][] distMatrix = new float[Files.length][Files.length];
        
        for (int i = 0; i < Files.length; i++) {
            //get features
            double det = ((Double)FileLocationToDetCovarMatrixMap.get(Files[i])).doubleValue();
            Matrix means = (Matrix)FileLocationToMeanMatrixMap.get(Files[i]);
            Matrix covar = (Matrix)FileLocationToCovarMatrixMap.get(Files[i]);
            Matrix invCovar = (Matrix)FileLocationToInvCovarMatrixMap.get(Files[i]);
            
            distMatrix[i][i] = 0.0f;
            for (int j = i+1; j < Files.length; j++) {
                //get features
                double test_det = ((Double)FileLocationToDetCovarMatrixMap.get(Files[j])).doubleValue();
                Matrix test_means = (Matrix)FileLocationToMeanMatrixMap.get(Files[j]);
                Matrix test_covar = (Matrix)FileLocationToCovarMatrixMap.get(Files[j]);
                Matrix test_invCovar = (Matrix)FileLocationToInvCovarMatrixMap.get(Files[j]);
                
                float score = calculateSymmetricKLDivergence(numFeatures, covar, test_covar, invCovar, test_invCovar, means, test_means, det, test_det);
                distMatrix[i][j] = distMatrix[j][i] = score;
            }
        }
        File[] theFiles = new File[this.Files.length];
        for (int i = 0; i < this.Files.length; i++) {
            theFiles[i] = new File(this.Files[i]);
        }
        return new DistanceMatrix(this.getRetrieverName(), distMatrix, theFiles);
    }
    
    /** Retrieve N transcriptions most similar to the query */
    public SearchResult[] retrieveNMostSimilar(Signal transcribedObject, int N) throws noMetadataException{
        SearchResult[] results = retrieveMostSimilar(transcribedObject);
        SearchResult[] truncResults = new SearchResult[N];
        for (int i = 0; i < truncResults.length; i++) {
            truncResults[i] = results[i];
        }
        return truncResults;
    }
}
