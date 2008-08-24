/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.m2k.evaluation2.TagClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.evaluation2.EvaluationDataObject;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.retrieval.MusicDB;

/**
 *
 * @author kris
 */
public class TagClassificationCollectionSplitter {

//    java -Xmx512M -cp m2k_core_1_3.jar  org.imirsel.m2k.evaluation2.TagClassification.TagClassificationCollectionSplitter /home/kris/Desktop/final_tag_metadata.csv testTagSplitter 3 /home/kris/Desktop/tagSplitterTest
    public static final int MAX_NUM_ADJUSTMENTS = 1000;

    public static Signal[][] crossValidateWithArtistFilter(EvaluationDataObject collectionToSplit, int numFolds, File outputDir, String testName) {

        MusicDB theDb = new MusicDB();
        theDb.indexKey(Signal.PROP_ARTIST);
        theDb.indexKey(Signal.PROP_TAGS);

        HashMap<String, HashSet<String>> pathsToRelevantTags = (HashMap<String, HashSet<String>>) collectionToSplit.getMetadata(EvaluationDataObject.TAG_BINARY_RELEVANCE_MAP);
        HashMap<String, String> pathsToArtist = (HashMap<String, String>) collectionToSplit.getMetadata(EvaluationDataObject.PATH_TO_ARTIST_MAP);
        String path;
        for (Iterator<String> it = pathsToRelevantTags.keySet().iterator(); it.hasNext();) {
            path = it.next();
            Signal toInput = new Signal(path);
            toInput.setMetadata(Signal.PROP_ARTIST, pathsToArtist.get(path));
            toInput.setMetadata(Signal.PROP_TAGS, pathsToRelevantTags.get(path));
            theDb.addFileToDatabase(toInput);
        }

        Signal[/*fold*/][/*tracks*/] dataset = theDb.getCrossValidationFoldsForTags(numFolds, System.currentTimeMillis(), MAX_NUM_ADJUSTMENTS);

        try {
            File overallGTFile = new File(outputDir.getAbsolutePath() + File.separator + "GT.txt");
            List<Signal> sigs = theDb.getSignals();
            BufferedWriter out = new BufferedWriter(new FileWriter(overallGTFile));
            for (Iterator<Signal> it = sigs.iterator(); it.hasNext();) {
                Signal signal = it.next();
                path = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                HashSet<String> tags = pathsToRelevantTags.get(path);
                for (Iterator<String> it2 = tags.iterator(); it2.hasNext();) {
                    out.write(path + "\t" + it2.next() + "\n");
                }
            }
            out.close();

            File featExtractFile = new File(outputDir.getAbsolutePath() + File.separator + "featExtractList.txt");
            out = new BufferedWriter(new FileWriter(featExtractFile));
            for (Iterator<Signal> it = sigs.iterator(); it.hasNext();) {
                Signal signal = it.next();
                path = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                out.write(path + "\n");
            }
            out.close();

            for (int i = 0; i < numFolds; i++) {

                HashSet<Signal> train = new HashSet<Signal>();
                Signal[] test = dataset[i];
                for (int j = 0; j < numFolds; j++) {
                    if (i != j) {
                        Signal[] anArray = dataset[j];
                        for (int k = 0; k < anArray.length; k++) {
                            train.add(anArray[k]);
                        }
                    }
                }

                File trainFile = new File(outputDir.getAbsolutePath() + File.separator + "train." + (i + 1) + ".txt");
                File testFile = new File(outputDir.getAbsolutePath() + File.separator + "test." + (i + 1) + ".txt");


                out = new BufferedWriter(new FileWriter(trainFile));
                for (Iterator<Signal> it = train.iterator(); it.hasNext();) {
                    Signal signal = it.next();
                    path = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                    HashSet<String> tags = pathsToRelevantTags.get(path);
                    for (Iterator<String> it2 = tags.iterator(); it2.hasNext();) {
                        out.write(path + "\t" + it2.next() + "\n");
                    }
                }
                out.close();
                out = new BufferedWriter(new FileWriter(testFile));
                for (int j = 0; j < test.length; j++) {
                    Signal signal = test[j];
                    path = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                    out.write(path + "\n");
                }
                out.close();

            }
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationCollectionSplitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO: write out datasets to file


        return dataset;
    }

    public static void main(String[] args) {
        File dataFile = new File(args[0]);
        String testName = args[1];
        int numFolds = Integer.parseInt(args[2]);
        File outputDirectory = new File(args[3]);
        TagClassificationGroundTruthFileReader reader = new TagClassificationGroundTruthFileReader();
        EvaluationDataObject GT = reader.readFile(dataFile);
        ArrayList<String> allTags = (ArrayList<String>) GT.getMetadata(EvaluationDataObject.TAG_LIST);
        System.out.println("Tags loaded: ");
        for (Iterator<String> it = allTags.iterator(); it.hasNext();) {
            System.out.println("\t" + it.next());

        }
        Signal[][] crossValidationFolds = crossValidateWithArtistFilter(GT, numFolds, outputDirectory, testName);
    }
}
