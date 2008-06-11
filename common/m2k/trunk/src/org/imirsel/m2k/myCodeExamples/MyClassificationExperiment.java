/*
 * Main.java
 *
 * Created on 23 April 2007, 08:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.myCodeExamples;

import java.io.File;
import java.util.Random;
import org.imirsel.m2k.evaluation.SignalArrayAccuracyClass;
import org.imirsel.m2k.featureExtractors.ExtractSingleGaussianFeatures;
import org.imirsel.m2k.featureExtractors.FeatureExtractorInterface;
import org.imirsel.m2k.featureExtractors.MFCCExtractorClass;
import org.imirsel.m2k.io.file.InputSignalsClass;
import org.imirsel.m2k.io.musicDB.MusicDBDelimTextFileImportFrame;
import org.imirsel.m2k.modelling.SignalClassifier;
import org.imirsel.m2k.modelling.TestTrainSplitSignalArrayClass;
import org.imirsel.m2k.modelling.cart.CART;
import org.imirsel.m2k.modelling.weka.trees.WekaJ48Classifier;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.retrieval.KLDivergenceRetriever;
import org.imirsel.m2k.util.retrieval.MusicDB;
import org.imirsel.m2k.util.retrieval.SearchResult;

/**
 *
 * @author kw
 */
public class MyClassificationExperiment {
    
    
    public static final String USAGE = "-listFiles parameterFiles outputDir\n" +
            "-csv - outputDir";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        if (args.length != 3){
            throw new RuntimeException("Not enough args\n" + USAGE);
        }
        Signal[][] testTrainSets = null;
        MusicDB evalDB = null;
        
        double proportionForTraining = 0.5;
        int randomSeed = 77;
        
        if (args[0].equals("-listFile")){
            //use a properties file
            InputSignalsClass inputFiles = new InputSignalsClass();
            inputFiles.readParameterFile(new File(args[1]));
            
            Signal[] filesToWorkWith = inputFiles.produceSignalList();
            
            TestTrainSplitSignalArrayClass splitter = new TestTrainSplitSignalArrayClass();
            splitter.setTrainingProportion(proportionForTraining);
            splitter.setSeed(randomSeed);
            evalDB = new MusicDB(filesToWorkWith);
            
        }else if(args[0].equals("-csv")){
            
            //alternatively import a CSV file
            MusicDBDelimTextFileImportFrame csvImport = new MusicDBDelimTextFileImportFrame();
            csvImport.setVisible(true);
            while(!csvImport.getDoneImport()){
                Thread.sleep(50);
            }
            
            evalDB = csvImport.getDatabase();
            testTrainSets = evalDB.getSimpleTestTrainDataSet(77,1.0,0.5,Signal.PROP_GENRE, "", 10);
            
        }
        
        // ------------------------------------
        // Component initialisation
        ExtractSingleGaussianFeatures extractor = new ExtractSingleGaussianFeatures((FeatureExtractorInterface)new MFCCExtractorClass(15, false));
        
        //extract train features
        System.out.println("Extracting training features");
        for (int i = 0; i < testTrainSets[0].length; i++) {
            String genre = testTrainSets[0][i].getStringMetadata(Signal.PROP_GENRE);
            testTrainSets[0][i] = extractor.ExtractFeatures(testTrainSets[0][i].getFile());
            testTrainSets[0][i].setMetadata(Signal.PROP_CLASS,genre);
            testTrainSets[0][i].setMetadata(Signal.PROP_GENRE,genre);
        }
        
        System.out.println("Extract testing features");
        for (int i = 0; i < testTrainSets[1].length; i++) {
            String genre = testTrainSets[1][i].getStringMetadata(Signal.PROP_GENRE);
            testTrainSets[1][i] = extractor.ExtractFeatures(testTrainSets[0][i].getFile());
            testTrainSets[1][i].setMetadata(Signal.PROP_GENRE,genre);
            testTrainSets[1][i].setMetadata(Signal.PROP_CLASS,genre);
        }
        
        
        //build and evaluate Weka J48 classifier
        System.out.println("Training J48");
        SignalClassifier aClassifier = new WekaJ48Classifier("");
        aClassifier.train(testTrainSets[0]);
        
        Signal[] testSet = testTrainSets[1];
        
        System.out.println("Evaluating J48");
        for (int i = 0; i < testSet.length; i++) {
            testSet[i].setMetadata(Signal.PROP_CLASSIFICATION,aClassifier.classify(testSet[i]));
        }
        
        SignalArrayAccuracyClass eval = new SignalArrayAccuracyClass("J48 - SingleGaussianMFCCFeatures", ".eval", ".signal", args[2], null, true);
        eval.evaluate(testSet);
        
        
        System.out.println("Training CART");
        //build and evaluate Weka CART classifier
        SignalClassifier anotherClassifier = new CART(CART.ENTROPY,0.001,true);
        anotherClassifier.train(testTrainSets[0]);
        
        System.out.println("Evaluating CART");
        for (int i = 0; i < testSet.length; i++) {
            testSet[i].setMetadata(Signal.PROP_CLASSIFICATION,anotherClassifier.classify(testSet[i]));
        }
        
        eval = new SignalArrayAccuracyClass("CART - SingleGaussianMFCCFeatures", ".eval", ".signal", args[2], null, true);
        eval.evaluate(testSet);
        
        
        System.out.println("Preparing a KL divergence based retirever");
        //perform a similarity evaluation
        KLDivergenceRetriever retriever = new KLDivergenceRetriever();
        retriever.acceptFeatureProfiles(testTrainSets[0]);
        retriever.acceptFeatureProfiles(testTrainSets[1]);
        
        retriever.finaliseIndex();
        
        System.out.println("Querying retiever");
        
        //perform some random queries
        Random rnd = new Random(5595);
        for (int i = 0; i < 5; i++) {
            Signal query = testSet[rnd.nextInt(testSet.length)];
            SearchResult[] results = retriever.retrieveNMostSimilar(query,10);
            System.out.println("Query: " + query.getFile().getAbsolutePath());
            System.out.println("----------------------");
            for (int j = 0; j < args.length; j++) {
                System.out.println("Result " + j + ": " + results[j].getTheResult().getFile().getAbsolutePath() + " - " + results[j].getScore());
                System.out.println("----------------------");
            }
        }
        
        System.out.println("Evaluating retiever");
        
        //evaluate
        evalDB.evaluateRetriever(retriever,new File(args[2]),998,100000);
    }
    
    
    
}
