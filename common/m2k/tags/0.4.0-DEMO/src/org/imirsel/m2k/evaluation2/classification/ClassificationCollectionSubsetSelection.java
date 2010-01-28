/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.m2k.evaluation2.classification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.io.musicDB.MusicDBDelimTextFileImportFrame;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.util.retrieval.MusicDB;

/**
 *
 * @author kris
 */
public class ClassificationCollectionSubsetSelection {
        public static void selectCollectionSubset(MusicDB toSelectFrom, int collectionSize, File outputDataFile, String classField) throws noMetadataException {
        
        if (!toSelectFrom.indexingMetadata(classField)){
            String msg = "The MusicDB imported was not indexing the chosen class field: " + classField + "\n" +
                    "Indexed fields:";
            for (Iterator<String> it = toSelectFrom.getIndexedMetadatas().iterator(); it.hasNext();){
                msg += "\t" + it.next() + "\n";
            }
            
            throw new noMetadataException(msg);
        }
        
        List<String> classes = toSelectFrom.getMetadataClasses(classField);
        List[] artistsPerClass = new List[classes.size()];
        
        System.out.println("Class sizes:");
        List[] signalsPerClass = new List[classes.size()];
        for (int i = 0; i < signalsPerClass.length; i++){
            signalsPerClass[i] = toSelectFrom.getSignalListForMetadataClass(classField, classes.get(i));
            System.out.println("\t" + classes.get(i) + ":\t" + signalsPerClass[i].size());
        }
        
        int target = (int)Math.ceil(collectionSize / (double)classes.size()); 
        System.out.println("Target class size: " + target);
        
        System.out.println("Artists per class:");
        HashMap[] artistToSignalListPerClass = new HashMap[classes.size()];
        for (int i = 0; i < signalsPerClass.length; i++){
            artistsPerClass[i] = new ArrayList<String>(10);
            artistToSignalListPerClass[i] = new HashMap<String,List<Signal>>();
            HashMap<String,List<Signal>> artistMapForClass = (HashMap<String,List<Signal>>)artistToSignalListPerClass[i];
            for (Iterator<Signal> it = signalsPerClass[i].iterator(); it.hasNext();){
                Signal aSig = it.next();
                String aPath = aSig.getFile().getPath();
                String artist = toSelectFrom.getMetadataClassForFile(Signal.PROP_ARTIST, aPath);
                if (artistMapForClass.containsKey(artist)){
                    artistMapForClass.get(artist).add(aSig);
                }
                else{
                    List<Signal> newList = new ArrayList<Signal>(10);
                    newList.add(aSig);
                    artistsPerClass[i].add(artist);
                    artistMapForClass.put(artist,newList);
                }
            }
            System.out.println("\t" + classes.get(i) + ":\t" + artistsPerClass[i].size());
        }
        
        
        System.out.println("Selecting subset...");
        //truncate each class to length whilst trying to even out num per artist
        
        Random rnd = new Random();
        for (int i = 0; i < signalsPerClass.length; i++){
            System.out.println("Class: " + classes.get(i));
            int currentSize = signalsPerClass[i].size();
            System.out.println("  size: " + currentSize);
            List<String> artists = artistsPerClass[i];
            HashMap<String,List<Signal>> artistToSignalList = artistToSignalListPerClass[i];
            int[] artistSizes = new int[artists.size()];
            System.out.println("  artists:");
            for (int j = 0; j < artistSizes.length; j++){
                artistSizes[j] = artistToSignalList.get(artists.get(j)).size();
                System.out.println("    " + artists.get(j) + ": " + artistSizes[j]);
            }
            
            HashSet<Signal> toRetain = new HashSet<Signal>();
            int artistIdx = 0;
            while(toRetain.size() < Math.min(currentSize, target)){
                List<Signal> sigList = artistToSignalList.get(artists.get(artistIdx));
                if (!sigList.isEmpty()){
                    Signal sigToRetain = sigList.remove(rnd.nextInt(sigList.size()));
                    toRetain.add(sigToRetain);
                }
                artistIdx++;
                if (artistIdx == artists.size()){
                    artistIdx = 0;
                }
            }
            
            //remove the remaining tracks from the DB
            int removed = 0;
            while(toRetain.size() + removed < currentSize){
                List<Signal> sigList = artistToSignalList.get(artists.get(artistIdx));
                if (!sigList.isEmpty()){
                    Signal toRemove = sigList.remove(rnd.nextInt(sigList.size()));
                    toSelectFrom.removeSignalFromDatabase(toRemove);
                    removed++;
                }
                artistIdx++;
                if (artistIdx == artists.size()){
                    artistIdx = 0;
                }
            }
        }
        
        System.out.println("done.");
        
        System.out.println("Class sizes:");
        signalsPerClass = new List[classes.size()];
        for (int i = 0; i < signalsPerClass.length; i++){
            signalsPerClass[i] = toSelectFrom.getSignalListForMetadataClass(classField, classes.get(i));
            System.out.println("\t" + classes.get(i) + ":\t" + signalsPerClass[i].size());
        }
        
        System.out.println("Artists per class:");
        artistToSignalListPerClass = new HashMap[classes.size()];
        for (int i = 0; i < signalsPerClass.length; i++){
            artistsPerClass[i] = new ArrayList<String>(10);
            artistToSignalListPerClass[i] = new HashMap<String,List<Signal>>();
            HashMap<String,List<Signal>> artistMapForClass = (HashMap<String,List<Signal>>)artistToSignalListPerClass[i];
            for (Iterator<Signal> it = signalsPerClass[i].iterator(); it.hasNext();){
                Signal aSig = it.next();
                String aPath = aSig.getFile().getPath();
                String artist = toSelectFrom.getMetadataClassForFile(Signal.PROP_ARTIST, aPath);
                if (artistMapForClass.containsKey(artist)){
                    artistMapForClass.get(artist).add(aSig);
                }
                else{
                    List<Signal> newList = new ArrayList<Signal>(10);
                    newList.add(aSig);
                    artistsPerClass[i].add(artist);
                    artistMapForClass.put(artist,newList);
                }
            }
            System.out.println("\t" + classes.get(i) + ":\t" + artistsPerClass[i].size());
        }
        
        for (int i = 0; i < signalsPerClass.length; i++){
            System.out.println("Class: " + classes.get(i));
            int currentSize = signalsPerClass[i].size();
            System.out.println("  size: " + currentSize);
            List<String> artists = artistsPerClass[i];
            HashMap<String,List<Signal>> artistToSignalList = artistToSignalListPerClass[i];
            int[] artistSizes = new int[artists.size()];
            System.out.println("  artists:");
            for (int j = 0; j < artistSizes.length; j++){
                artistSizes[j] = artistToSignalList.get(artists.get(j)).size();
                System.out.println("    " + artists.get(j) + ": " + artistSizes[j]);
            }
        }
        
        try{
            toSelectFrom.writeMusicDBToDelimTextFile(outputDataFile, "\t", true);
        }catch (IOException ex){
            Logger.getLogger(ClassificationCollectionSplitter.class.getName()).log(Level.SEVERE, null, ex);
        }catch (noMetadataException ex){
            Logger.getLogger(ClassificationCollectionSplitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    public static final String FULL_USAGE_STRING = "Classification Subset Selection\n" +
                                                   "--------------------------------------\n" +
                                                   "A tool designed to select a subset of tracks from a collection, " +
                                                   "whilst maintaining the maximum diversity in the number of " +
                                                   "artists\n\n" +
                                                   "args: collectionSize fieldToUseAsClass /path/to/output/file";
    
    public static void main(String[] args) {
        if (args.length < 3){
            System.out.println(FULL_USAGE_STRING);
            System.exit(1);
        }
        
        String classField = args[1];
        
        System.out.println("Please import the metadata CSV file...");
        System.out.println("...remember to ensure that the field you want to use as the class metadata (e.g. genre) and ARTIST metadatas are set!");
       
        int collectionSize = Integer.parseInt(args[0]);
        File outputFile = new File(args[2]);

        System.out.println("Class field: " + classField);
        
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

        List<String> classes = importedDB.getMetadataClasses(classField);
        System.out.println("Classes loaded: ");
        for (Iterator<String> it = classes.iterator(); it.hasNext();) {
            System.out.println("\t" + it.next());
        }
        
        

        selectCollectionSubset(importedDB, collectionSize, outputFile, classField);
        
        System.out.println("--exit--");
    }
}
