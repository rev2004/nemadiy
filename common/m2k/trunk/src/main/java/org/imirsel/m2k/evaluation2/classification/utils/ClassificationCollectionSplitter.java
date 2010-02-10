/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.m2k.evaluation2.classification.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.io.musicDB.MusicDBDelimTextFileImportFrame;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.retrieval.MusicDB;

/**
 *
 * @author kris
 */
public class ClassificationCollectionSplitter {

//    java -Xmx512M -cp m2k_core_1_3.jar  org.imirsel.m2k.evaluation2.TagClassification.TagClassificationCollectionSplitter /home/kris/Desktop/final_tag_metadata.csv testTagSplitter 3 /home/kris/Desktop/tagSplitterTest
    public static final int MAX_NUM_ADJUSTMENTS = 1000;

    public static Signal[][] crossValidateWithArtistFilter(MusicDB collectionToSplit, int numFolds, File outputDir) {
        return crossValidateWithArtistFilter(collectionToSplit, numFolds, outputDir, null);
    }
    
    private static final HashSet<String> ACCEPTED_FILE_EXTENSIONS = new HashSet<String>();
    static{
        ACCEPTED_FILE_EXTENSIONS.add(".mp3");
        ACCEPTED_FILE_EXTENSIONS.add(".wav");
        ACCEPTED_FILE_EXTENSIONS.add(".mid");
    }

    public static Signal[][] crossValidateWithArtistFilter(MusicDB theDb, int numFolds, File outputDir, ArrayList<String[]> collectionVersionPaths) {


        Signal[/*fold*/][/*tracks*/] dataset = theDb.getCrossValidationFoldsForClassification(numFolds, System.currentTimeMillis(), MAX_NUM_ADJUSTMENTS);
        String path, aClass, id;
        try {
            File overallGTFile = new File(outputDir.getAbsolutePath() + File.separator + "GT.txt");
            List<Signal> sigs = theDb.getSignals();
            BufferedWriter out = new BufferedWriter(new FileWriter(overallGTFile));
            for (Iterator<Signal> it = sigs.iterator(); it.hasNext();) {
                Signal signal = it.next();
                path = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                aClass = theDb.getMetadataClassForFile(Signal.PROP_CLASS, path);
                out.write(path + "\t" + aClass + "\n");
            }
            out.close();

            //deal with different versions here
            if (collectionVersionPaths == null){
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
                        aClass = theDb.getMetadataClassForFile(Signal.PROP_CLASS, path);
                        out.write(path + "\t" + aClass + "\n");
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
            } else{
                for (int v = 0; v < collectionVersionPaths.size(); v++) {
                    
                    String collectionPath = collectionVersionPaths.get(v)[0];
                    String collectionName = collectionVersionPaths.get(v)[1];
                    
                    System.out.println("creating collection version: " + collectionName);
                    
                    File collectionDir = new File(outputDir.getAbsolutePath()  + File.separator + collectionName);
                    collectionDir.mkdirs();
                    
                    int notMatched = 0;
                    
                    //check for existence of all files using ids and link their paths
                    HashSet<String> ids = new HashSet<String>();
                    for (Iterator<Signal> it = sigs.iterator(); it.hasNext();) {
                        ids.add(it.next().getFile().getName());
                    }
                    HashMap<String,String> id2path = new HashMap<String,String>();
                    File[] files = new File(collectionPath).listFiles();
                    String name, ext;
                    for (int i = 0; i < files.length; i++) {
                        name = files[i].getName();
                        id = null;
                        for (Iterator<String> it = ACCEPTED_FILE_EXTENSIONS.iterator(); it.hasNext();) {
                            ext = it.next();
                            if (name.endsWith(ext)){
                                id = name.replaceAll(ext, "");
                                if (ids.contains(id)){
                                    id2path.put(id, files[i].getAbsolutePath());
                                }
                                break;
                            }
                        }
                        if (id == null){
                            System.out.println("WARNING: file: " + files[i].getAbsolutePath() + " was not matched to track ID!");
                            notMatched++;
                        }
                    }
                    
                    System.out.println("");
                    if (id2path.size() < sigs.size()){
                        System.out.println("WARNING: " + (sigs.size() - id2path.size()) + " track IDs were not matched to files");
                    }
                    if (notMatched > 0){
                        System.out.println("WARNING: " + notMatched + " paths were not matched to IDs");
                    }
                    System.out.println("");
                    
                    File featExtractFile = new File(collectionDir.getAbsolutePath() + File.separator + "featExtractList.txt");
                    out = new BufferedWriter(new FileWriter(featExtractFile));
                    for (Iterator<Signal> it = sigs.iterator(); it.hasNext();) {
                        Signal signal = it.next();
                        id = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                        path = id2path.get(id);
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

                        File trainFile = new File(collectionDir.getAbsolutePath() + File.separator + "train." + (i + 1) + ".txt");
                        File testFile = new File(collectionDir.getAbsolutePath() + File.separator + "test." + (i + 1) + ".txt");

                        out = new BufferedWriter(new FileWriter(trainFile));
                        for (Iterator<Signal> it = train.iterator(); it.hasNext();) {
                            Signal signal = it.next();
                            id = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                            path = id2path.get(id);
                            aClass = theDb.getMetadataClassForFile(Signal.PROP_CLASS, id);
                            out.write(path + "\t" + aClass + "\n");
                            
                        }
                        out.close();
                        out = new BufferedWriter(new FileWriter(testFile));
                        for (int j = 0; j < test.length; j++) {
                            Signal signal = test[j];
                            id = signal.getStringMetadata(Signal.PROP_FILE_LOCATION);
                            path = id2path.get(id);
                            out.write(path + "\n");
                        }
                        out.close();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClassificationCollectionSplitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO: write out datasets to file


        return dataset;
    }

    public static final String STANDARD_USAGE = "numFolds(int) /path/to/output/dir";
    public static final String FULL_USAGE_STRING = "Classification Collection Splitter\n" +
                                                   "--------------------------------------\n" +
                                                   "A tool designed to cross-validate a collection of audio" +
                                                   "files and produce N test and Train sets. The sets are " +
                                                   "produced using artist filtering, so that no artist " +
                                                   "appearing in a training set also appears in the corresponding" +
                                                   "test set.\n\n" +
                                                   "USAGE: \n" +
                                                   "-mirex numFolds(int) /path/to/output/dir /collection/Version/Path1 versionName1 ... /collection/Version/PathN versionNameN\n" +
                                                   "    OR\n" +
                                                   STANDARD_USAGE;
    
    public static void main(String[] args) {
        if (args.length < 3){
            System.out.println(FULL_USAGE_STRING);
            System.exit(1);
        }
        System.out.println("Please import the metadata CSV file...");
        System.out.println("...remember to ensure that the CLASS metadata is set!");
        
        if (args[0].equalsIgnoreCase("-mirex")){
            System.out.println("...also remember to use the file id, rather than its original path!");
            int numFolds = Integer.parseInt(args[1]);
            File outputDirectory = new File(args[2]);
            
            ArrayList<String[]> versions = new ArrayList<String[]>();
            for (int i = 3; (i+1) < args.length; i+=2){
                String versionPath = args[i];
                String versionName = args[i+1];
                versions.add(new String[]{versionPath,versionName});
            }
            
            MusicDBDelimTextFileImportFrame importFrame = new MusicDBDelimTextFileImportFrame();
            importFrame.validate();
            importFrame.setVisible(true);
            while(!importFrame.getDoneImport()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClassificationCollectionSplitter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            MusicDB importedDB = importFrame.getDatabase();
            
            List<String> classes = importedDB.getMetadataClasses(Signal.PROP_CLASS);
            System.out.println("Classes loaded: ");
            for (Iterator<String> it = classes.iterator(); it.hasNext();) {
                System.out.println("\t" + it.next());
            }
            
            Signal[][] crossValidationFolds = crossValidateWithArtistFilter(importedDB, numFolds, outputDirectory, versions);
            
            
        }else{

            int numFolds = Integer.parseInt(args[0]);
            File outputDirectory = new File(args[1]);
            
            MusicDBDelimTextFileImportFrame importFrame = new MusicDBDelimTextFileImportFrame();
            importFrame.validate();
            importFrame.setVisible(true);
            while(!importFrame.getDoneImport()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClassificationCollectionSplitter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            MusicDB importedDB = importFrame.getDatabase();
            
            List<String> classes = importedDB.getMetadataClasses(Signal.PROP_CLASS);
            System.out.println("Classes loaded: ");
            for (Iterator<String> it = classes.iterator(); it.hasNext();) {
                System.out.println("\t" + it.next());
            }
            
            Signal[][] crossValidationFolds = crossValidateWithArtistFilter(importedDB, numFolds, outputDirectory);
        }
    }
}
