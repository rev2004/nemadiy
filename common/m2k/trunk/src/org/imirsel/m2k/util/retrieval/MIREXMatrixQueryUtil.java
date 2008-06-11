/*
 * MIREXMatrixQueryUtil.java
 *
 * Created on 31 July 2006, 14:13
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.imirsel.m2k.util.retrieval;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.io.musicDB.MusicDBDelimTextFileImportFrame;
import org.imirsel.m2k.io.musicDB.RemapMusicDBFilenamesClass;
import org.imirsel.m2k.util.ReencodeAudioWithLameAndParameters;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kw
 */
public class MIREXMatrixQueryUtil {
    
    public static final String IMPORT = "importMetadataFile";
    public static final String GEN_QUERIES = "genQueries";
    public static final String QUERY = "queryDistMatrix";
    public static final String EVAL = "evaluate";
    public static final String TIME_SIM = "timesSimilar";
    public static final String TRIANGULAR_INEQ = "triIneq";
    public static final String FILTERED_GENRE = "filtgenre";
    public static final String RETURN_RESULTS = "results";
    public static final String CREATE_EVALUTRON_DATA_FILES = "evalutronPrep";
    public static final String PROCESS_EVALUTRON_RESULTS = "evalutronResults";
    
    
    public static final String USAGE = "\n\nOverview:\nImplements basic Distance Matrix services to support the MIREX 2006 " +
            "Audio Music Search task, including importing deliminated text files describing a database, " +
            "generating a stratified random set of queries on that database, querying a DistanceMatrix file with " +
            "those queries and calculating evaluation statistics for that database. " +
            "Returned results are filtered to remove results from the same artist " +
            "as the query song and any songs tagged with a genre of \"cover song\".\n\nUsage:\n\n" +
            "java -cp ...:path/to/m2k.jar org.imirsel.m2k.util.retrieval.MIREXMatrixQueryUtil MODE\n" +
            "\nWhere MODE is one of the following keys:\n" +
            "\t" + IMPORT + " path/and/file/to/save/to\n" +
            "\t" + GEN_QUERIES + " path/to/MetadataDB numQueries(Integer) randomSeed(Integer) metadatakey(genre/artist) path/to/local/dir/of/audio/files path/and/file/to/save/to\n" +
            "\t" + QUERY + " path/to/DistMatrix path/to/MetadataDB path/to/query/file numResults(5) path/to/local/dir/of/audio/files path/to/save/results/to\n" +
            "\t" + EVAL + " path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/to \n" + 
            "\t" + TIME_SIM + " path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/and/plots/to \n" +
            "\t" + TRIANGULAR_INEQ + " path/to/DistMatrix path/to/save/report/and/plots/to \n" +
            "\t" + FILTERED_GENRE + " path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/and/plots/to \n" +
            "\t" + RETURN_RESULTS + " path/to/DistMatrix path/to/MetadataDB /path/to/local/dir/of/audio/files query numResult filterCoverSongs(y/n) \n" +
            "\t" + CREATE_EVALUTRON_DATA_FILES + " path/DistMatrix/folder path/to/MetadataDB path/to/query/file numResults(5) path/to/local/dir/of/audio/files path/to/save/queryCandidateFile/to path/to/save/teamNameFile/to [dir/to/re-encode/audio/files/to]\n" +
            "\t" + PROCESS_EVALUTRON_RESULTS + " path/to/team/table/file path/to/result/table/file path/to/task/table/file \n";
            
    
    /** Creates a new instance of MIREXMatrixQueryUtil */
    public MIREXMatrixQueryUtil() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        List validArgs = new ArrayList(3);
        validArgs.add(IMPORT);
        validArgs.add(GEN_QUERIES);
        validArgs.add(QUERY);
        validArgs.add(EVAL);
        validArgs.add(TIME_SIM);
        validArgs.add(TRIANGULAR_INEQ);
        validArgs.add(FILTERED_GENRE);
        validArgs.add(RETURN_RESULTS);
        validArgs.add(CREATE_EVALUTRON_DATA_FILES);
        validArgs.add(PROCESS_EVALUTRON_RESULTS);
        
        
        if ((args.length < 2)||(!validArgs.contains(args[0])))
        {
            System.out.println("Invalid arguments!\n" + USAGE);
            return;
        }else if(args[0].equals(IMPORT))
        {
            runImport(args);
        }else if(args[0].equals(GEN_QUERIES)){
            runGenQueries(args);
        }else if(args[0].equals(QUERY))
        {
            runQuery(args);
        }else if(args[0].equals(EVAL))
        {
            runEval(args);
        }else if(args[0].equals(TIME_SIM))
        {
            runTimesSim(args);
        }else if(args[0].equals(TRIANGULAR_INEQ))
        {
            runTriEqTests(args);
        }else if(args[0].equals(FILTERED_GENRE))
        {
            runFiltGenre(args);
        }else if(args[0].equals(RETURN_RESULTS))
        {
            runReturnResults(args);
        }else if(args[0].equals(CREATE_EVALUTRON_DATA_FILES))
        {
            runEvalutronPrep(args);
        }else if(args[0].equals(PROCESS_EVALUTRON_RESULTS))
        {
            runEvalutronPostProcess(args);
        }else{
            System.out.println("Unrecognised argument (" + args[0] + ")!\n" + USAGE);
            return;
        }
    }
    
    public static void runImport(String[] args){
        //process args
        String outFile = args[1];
        if (outFile.trim().equals("")){
            System.out.println("Unable to process output file name argument!\n" + USAGE);
            return;
        }
        
        MusicDBDelimTextFileImportFrame importFrame = new MusicDBDelimTextFileImportFrame();
        importFrame.setVisible(true);
        while(!importFrame.getDoneImport())
        {
            try{
                Thread.sleep(5);
            }catch(InterruptedException ie){
                
            }
        }
        MusicDB theDB = importFrame.getDatabase();
        importFrame.dispose();
        importFrame = null;
        
        File outputFile = new File(outFile);

        FileOutputStream fw;
        try {
            fw = new FileOutputStream(outputFile, false);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for writing,\n either file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for some other reason.",fnf);
        }
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(fw);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while writing stream header",ioe);
        }
        try {
            out.writeObject(theDB);
            out.close();
        } catch(NotSerializableException nse) {
            throw new RuntimeException("The input Object is not Serializable and can't be written to a file",nse);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while writing out Object",ioe);
        }
        
        System.out.println("A MusicDB Object representing the imported file was saved to:\n\t" + outputFile.getPath() + "\n\t It contains " + theDB.size() + " indexed tracks");
    }
    
    public static void runGenQueries(String[] args) throws noMetadataException, IOException {
        //process args - " path/to/MetadataDB numQueries randomSeed metadatakey(genre/artist) path/to/local/dir/of/audio/files path/and/file/to/save/to;
        String mDB = args[1];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        int numQueries;
        try{
            numQueries = Integer.parseInt(args[2]);
        }catch(NumberFormatException nfe){
            System.out.println("Unable to parse number of queries argument(" + args[2] + ")!\n" + USAGE);
            return;
        }
        
        int randomSeed;
        try{
            randomSeed = Integer.parseInt(args[3]);
        }catch(NumberFormatException nfe){
            System.out.println("Unable to parse random number generator seed argument(" + args[3] + ")!\n" + USAGE);
            return;
        }
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        
        String key = args[4];
        List indexedMetadata = theDB.getIndexedMetadatas();
        if (!indexedMetadata.contains(key)){
            System.out.println("Unable to process metadata key argument (" + key + "), because the specified MusicDB did not index this key!\n" +
                    "Indexed keys:\n");
            for( int i=0;i<indexedMetadata.size();i++){
                System.out.println("\t" + (String)indexedMetadata.get(i) + "\n");
            }
            return;
        }
        
        String localAudioDir = args[5];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        try{
            theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
        }catch(IOException ioe){
            throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
        }
        
        String out = args[6];
        if (out.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File outFile = new File(out);
        
        //Get list of metadata classes (e.g. for genre - rock, classical etc.)
        List metaClasses = theDB.getMetadataClasses(key);
        java.util.Random rnd = new java.util.Random(randomSeed);
        if (numQueries < metaClasses.size())
        {
            System.out.println("WARNING: Less queries were requested than the number of classes in the database for key: " + key + "!");
        }
        
        //Generate list of queries
        List<File> queries = new ArrayList<File>();
        
        List[] classLists = new List[metaClasses.size()];
        for(int i=0;i<metaClasses.size();i++){
            classLists[i] = theDB.getSignalListForMetadataClass(key,  (String)metaClasses.get(i));
        }
        
        int classIdx = rnd.nextInt(classLists.length);
        
        while(queries.size() < numQueries)
        {
            Signal query = (Signal)classLists[classIdx].get(rnd.nextInt(classLists[classIdx].size()));
            queries.add(new File(query.getStringMetadata(Signal.PROP_FILE_LOCATION)));
            
            classIdx++;
            if (classIdx == metaClasses.size())
            {
                classIdx = 0;
            }
        }
        
        //write list file
        BufferedWriter output = null;
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(outFile) );
            output.write("\"id_query\"\t\"id_song\"\t\"description\"");
            output.newLine();
            for (int i = 0; i < queries.size(); i++ ) {
                output.write("\"" +  (i+1) + "\"\t\"" + convertFileToMIREX_ID(queries.get(i)) + "\"\t\"\"");
                output.newLine();
            }
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    
    public static void runQuery(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MetadataDB path/to/query/file numResults(5) path/to/local/dir/of/audio/files path/to/save/results/to
        String distMat = args[1];
        if (distMat.trim().equals("")){
            System.out.println("Unable to process distance matrix file name argument (" + distMat +")!\n" + USAGE);
            return;
        }
        File distMatFile = new File(distMat); 
        //read distanceMatrix
        DistanceMatrix distMatrix = DistanceMatrix.read(distMatFile);
        
        //read MusicDB Object describing collection
        String mDB = args[2];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        
        if ((!theDB.indexingMetadata(Signal.PROP_ARTIST))||(!theDB.indexingMetadata(Signal.PROP_GENRE)))
        {
            System.out.println("The metadata DB should index both artist and genre tags, the MusicDB loaded did not!");
            return;
        }
        
        //read query file
        String query = args[3];
        if (query.trim().equals("")){
            System.out.println("Unable to process query file name argument (" + query +")!\n" + USAGE);
            return;
        }
        File queryFile = new File(query); 
        
         if (!queryFile.exists()) {
            throw new FileNotFoundException("runQuery(): The specified file does not exist!\n File: " + queryFile.getPath());
        }
        if (queryFile.isDirectory()) {
            throw new RuntimeException("runQuery(): The specified file is a directory and therefore cannot be read!\n Path: " + queryFile.getPath());
        }
        if (!queryFile.canRead()) {
            throw new RuntimeException("runQuery(): The specified file exists but cannot be read!\n File: " + queryFile.getPath());
        }
        
        BufferedReader textBuffer;
        try {
            textBuffer = new BufferedReader( new FileReader(queryFile) );
        } catch(java.io.FileNotFoundException fnfe) {
            throw new RuntimeException("runQuery(): The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n File: " + queryFile.getPath());
        }
        List<String> queries = new ArrayList<String>();
        try {
            String line = textBuffer.readLine();
            while(line != null){
                if (line != null){
                    queries.add(line);
                }
                line = textBuffer.readLine();
            }
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("runQuery()runQuery(): An IOException occured while reading file: " + queryFile.getPath() + "\n" + ioe);
        } catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            throw new RuntimeException("runQuery(): NullPointerException caused by: " + queryFile.getPath());
        } finally {
            if (textBuffer != null)
            {
                textBuffer.close();
            }
        }
        
        
        
         //parse local dir of audio file argument
        String localAudioDir = args[5];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        
        
        
        //convert queries from IMIRSEL id format (a32459) into file names
        List<String> queryFiles = associateQueriesWithFiles(queries, new File(localAudioDir));
        
        
        
        
        
        //parse number of results
        int numResults;
        try{
            numResults = Integer.parseInt(args[4]);
        }catch(NumberFormatException nfe){
            System.out.println("Unable to number of results argument(" + args[4] + ")!\n" + USAGE);
            return;
        }
        
       
        
        try{
            theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
            distMatrix = RemapMusicDBFilenamesClass.remapDistanceMatrix(distMatrix, localAudioDir, true);
            
        }catch(IOException ioe){
            throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
        }
        
        //parse output file argument
        String out = args[6];
        if (out.trim().equals("")){
            System.out.println("Unable to processoutput result file name argument (" + out +")!\n" + USAGE);
            return;
        }
        File outFile = new File(out);
        
        //Init DistanceMatrix query object
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(distMatrix);
        
        //Init list of lists holding results
        List<List<String>> queryResults = new ArrayList<List<String>>();
        //perform each query
        for (int i=0;i<queryFiles.size();i++)
        {
            String theQuery = queryFiles.get(i);
            SearchResult[] results = ret.retrieveMostSimilar(new Signal(theQuery));
            String queryArtist = theDB.getMetadataClassForFile(Signal.PROP_ARTIST, theQuery);
            List<String> resultList = new ArrayList<String>();
            int resultIdx = 0;
            while ((resultList.size() < numResults)&&(resultIdx < results.length))
            {
                Signal theResult = results[resultIdx].getTheResult();
                //filter out results by same artist
                if (!theDB.getMetadataClassForSignal(Signal.PROP_ARTIST, theResult).equals(queryArtist)){
                    //filter out cover songs
                    if (!theDB.getMetadataClassForSignal(Signal.PROP_GENRE, theResult).toLowerCase().equals("cover song")){
                        //filter the query song out of the results
                        if (!theResult.getStringMetadata(Signal.PROP_FILE_LOCATION).equals(theQuery)){
                            resultList.add(theResult.getStringMetadata(Signal.PROP_FILE_LOCATION));
                        }
                    }
                }
                resultIdx++;
            }
            queryResults.add(resultList);
            System.out.println("Done query " + (i+1));
        }
                    
        //write result file
        //write list file
        BufferedWriter output = null;
        String theLine = null;
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(outFile) );
            System.out.println("Writing output to " + outFile.getPath());
            for (int i = 0; i < queryResults.size(); i++ ) {
                String aQuery = queries.get(i);
                List aResultList = (List)queryResults.get(i);
                
                String line = aQuery + ": ";
                for (int j=0;j<aResultList.size();j++){
                    line += (String)aResultList.get(j);
                    if (j!= aResultList.size()-1)
                    {
                        line += ", ";
                    }
                }
                
                output.write( line );
                output.newLine();
            }
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    
    private static List<String> associateQueriesWithFiles(List<String> queries, File localAudioDir){
        System.out.println("associating files with query IDs");
        //get list of files in directory tree
        LinkedList<File> dirs = new LinkedList<File>();
        HashMap<String,File> idToFile = new HashMap<String,File>();
        System.out.println("\tgetting file list...");
        dirs.add(localAudioDir);
        while(dirs.size()>0){
            File aFile = dirs.removeFirst();
            if (aFile.isFile()){
                String id = convertFileToMIREX_ID(aFile);
                if (idToFile.containsKey(id)){
                    throw new RuntimeException("IMIRSEL track id conflict on id: " + id + " for files:\n1: " + aFile.getAbsolutePath() + "\n2: " + idToFile.get(id).getAbsolutePath());
                }
                idToFile.put(id,aFile);
            }else{
                File[] dirFiles = aFile.listFiles();
                for (int i = 0; i < dirFiles.length; i++) {
                    if (dirFiles[i].isFile()){
                        String id = convertFileToMIREX_ID(dirFiles[i]);
                        if (idToFile.containsKey(id)){
                            throw new RuntimeException("IMIRSEL track id conflict on id: " + id + " for files:\n1: " + dirFiles[i].getAbsolutePath() + "\n2: " + idToFile.get(id).getAbsolutePath());
                        }
                        idToFile.put(id,dirFiles[i]);
                    }else{
                          dirs.add(dirFiles[i]);  
                    }
                }
            }
            System.out.println("\tfound " + idToFile.size() + " files so far");
        }
        dirs = null;
        System.out.println("\tfinding query audio files...");
        List<String> queryPaths = new ArrayList<String>();
        for (Iterator<String> it = queries.iterator(); it.hasNext();) {
            String key = it.next();
            if (!idToFile.containsKey(key)){
                throw new RuntimeException("Didn't find ID: " + key + " in directory tree root at " + localAudioDir.getAbsolutePath() + "\n" + idToFile.size() + " ids found.");
            }
            queryPaths.add(idToFile.get(key).getAbsolutePath());
        }
        
        System.out.println("done.");
        return queryPaths;
    }
    
    private static String convertFileToMIREX_ID(File aFile){
        String name = aFile.getName();
        String nameLowerCase = name.toLowerCase();
        if (nameLowerCase.endsWith(".mp3") 
                || nameLowerCase.endsWith(".wav") 
                || nameLowerCase.endsWith(".aac") 
                || nameLowerCase.endsWith(".wma") 
                || nameLowerCase.endsWith(".ogg") 
                || nameLowerCase.endsWith(".aif") 
                || nameLowerCase.endsWith(".mid")){
            return name.substring(0,name.length()-4);
        }
        return name;
    }
    
    public static void runEvalutronPrep(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MetadataDB path/to/query/file numResults(5) path/to/local/dir/of/audio/files path/to/save/results/to
        String distMatFolder = args[1];
        if (distMatFolder.trim().equals("")){
            System.out.println("Unable to process distance matrix folder name argument (" + distMatFolder +")!\n" + USAGE);
            return;
        }
        File distMatFolderFile = new File(distMatFolder); 
        File[] distMatrixFiles = distMatFolderFile.listFiles();
        
        String[] teamNames = new String[distMatrixFiles.length];
        System.out.println("Team Names:");
        for (int i = 0; i < teamNames.length; i++) {
            teamNames[i] = distMatrixFiles[i].getName();
            System.out.println("\t" + teamNames[i]);
        }

        
        //read MusicDB Object describing collection
        String mDB = args[2];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        
        if ((!theDB.indexingMetadata(Signal.PROP_ARTIST))||(!theDB.indexingMetadata(Signal.PROP_GENRE)))
        {
            System.out.println("The metadata DB should index both artist and genre tags, the MusicDB loaded did not!");
            return;
        }
        
        //read query file
        String query = args[3];
        if (query.trim().equals("")){
            System.out.println("Unable to process query file name argument (" + query +")!\n" + USAGE);
            return;
        }
        File queryFile = new File(query); 
        
         if (!queryFile.exists()) {
            throw new FileNotFoundException("runQuery(): The specified file does not exist!\n File: " + queryFile.getPath());
        }
        if (queryFile.isDirectory()) {
            throw new RuntimeException("runQuery(): The specified file is a directory and therefore cannot be read!\n Path: " + queryFile.getPath());
        }
        if (!queryFile.canRead()) {
            throw new RuntimeException("runQuery(): The specified file exists but cannot be read!\n File: " + queryFile.getPath());
        }
        
        BufferedReader textBuffer;
        try {
            textBuffer = new BufferedReader( new FileReader(queryFile) );
        } catch(java.io.FileNotFoundException fnfe) {
            throw new RuntimeException("runQuery(): The specified file does not exist, this exception should never be thrown and indicates a serious bug.\n File: " + queryFile.getPath());
        }
        List<String> queries = new ArrayList<String>();
        try {
            String line = textBuffer.readLine();
            line = textBuffer.readLine(); // ignore header line
            
            while(line != null){
                if (line != null){
                    String queryString = line.split("\t")[1];
                    if (queryString.startsWith("\"")&&(queryString.endsWith("\""))){
                        queryString = queryString.substring(1,queryString.length()-1);
                    }
                    queries.add(queryString);
                }
                line = textBuffer.readLine();
            }
        } catch (java.io.IOException ioe) {
            throw new java.io.IOException("runQuery()runQuery(): An IOException occured while reading file: " + queryFile.getPath() + "\n" + ioe);
        } catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            throw new RuntimeException("runQuery(): NullPointerException caused by: " + queryFile.getPath());
        } finally {
            if (textBuffer != null)
            {
                textBuffer.close();
            }
        }
        
        File reencodeAudioDir = null;
        if (args.length == 9){
            reencodeAudioDir = new File(args[8]);
            System.out.println("Re-encoding audio files to: " + reencodeAudioDir.getAbsolutePath());
        }else{
            System.out.println("Not re-encoding audio files!");
        }
        
        //parse local dir of audio file argument
        String localAudioDir = args[5];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        System.out.println("Number of queries: " + queries.size());
        
        //parse number of results
        int numResults;
        try{
            numResults = Integer.parseInt(args[4]);
        }catch(NumberFormatException nfe){
            System.out.println("Unable to number of results argument(" + args[4] + ")!\n" + USAGE);
            return;
        }
        
        
        
        
        List<String> queryFiles = associateQueriesWithFiles(queries, localAudioDirFile);
        
                
        
        
        //parse output files arguments
        String queryCandidate = args[6];
        if (queryCandidate.trim().equals("")){
            System.out.println("Unable to process output query/candidate file name argument (" + queryCandidate +")!\n" + USAGE);
            return;
        }
        File queryCandidateFile = new File(queryCandidate);
        
        System.out.println("query/candidate file: " + queryCandidate);
        
        String teamName = args[7];
        if (teamName.trim().equals("")){
            System.out.println("Unable to processoutput result file name argument (" + teamName +")!\n" + USAGE);
            return;
        }
        File teamNameFile = new File(teamName);
        
        System.out.println("team name file: " + teamName);
        
        //write team name file
            //id_team     team_name
        BufferedWriter output = null;
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(teamNameFile) );
            System.out.println("Writing team names to " + teamNameFile.getPath());
            
            //write header
            output.write("\"id_team\"\t\"title\"\t\"affiliation\"");
            output.newLine();
            
            for (int i = 0; i < teamNames.length; i++ ) {
                String aName = teamNames[i];
                output.write("\"" + (i+1) + "\"\t\"" + aName + "\"\t\"\"");
                output.newLine();
            }
            System.out.println("done.");
        } finally {
            if (output != null) {
                output.close();
            }
        }
        
        
        //load and query each distance matrix
        List<List<List<String>>> teamResultsList = new ArrayList<List<List<String>>>();
        //record number of unique results
        HashSet[] resultTracks = new HashSet[queryFiles.size()];
        for (int i = 0; i < resultTracks.length; i++) {
            resultTracks[i] = new HashSet<String>();
        }

        
        for (int d = 0; d < distMatrixFiles.length; d++) {
            File distMatrixFile = distMatrixFiles[d];
            System.out.println("loading and querying distance matrix: " + distMatrixFile.getAbsolutePath());
            //read distanceMatrix
            DistanceMatrix distMatrix = DistanceMatrix.read(distMatrixFile);

            try{
                theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
                distMatrix = RemapMusicDBFilenamesClass.remapDistanceMatrix(distMatrix, localAudioDir, true);

            }catch(IOException ioe){
                throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
            }

            //Init DistanceMatrix query object
            DistanceMatrixRetriever ret = new DistanceMatrixRetriever(distMatrix);

            //Init list of lists holding results
            List<List<String>> queryResults = new ArrayList<List<String>>();
            //perform each query
            for (int i=0;i<queryFiles.size();i++)
            {
                String theQuery = queryFiles.get(i);
                SearchResult[] results = ret.retrieveMostSimilar(new Signal(theQuery));
                String queryArtist = theDB.getMetadataClassForFile(Signal.PROP_ARTIST, theQuery);
                List<String> resultList = new ArrayList<String>();
                int resultIdx = 0;
                while ((resultList.size() < numResults)&&(resultIdx < results.length))
                {
                    Signal theResult = results[resultIdx].getTheResult();
                    //filter out results by same artist
                    if (!theDB.getMetadataClassForSignal(Signal.PROP_ARTIST, theResult).equals(queryArtist)){
                        //filter out cover songs
                        if (!theDB.getMetadataClassForSignal(Signal.PROP_GENRE, theResult).toLowerCase().equals("cover song")){
                            //filter the query song out of the results
                            if (!theResult.getStringMetadata(Signal.PROP_FILE_LOCATION).equals(theQuery)){
                                resultList.add(theResult.getStringMetadata(Signal.PROP_FILE_LOCATION));
                                resultTracks[i].add(theResult.getStringMetadata(Signal.PROP_FILE_LOCATION));
                            }
                        }
                    }
                    resultIdx++;
                }
                queryResults.add(resultList);
                System.out.println("Done query " + (i+1));
            }
            
            //store results for all queries
            teamResultsList.add(queryResults);
        }

        int sumResultsPerQuery = 0;
        for (int i = 0; i < resultTracks.length; i++) {
            sumResultsPerQuery += resultTracks[i].size();
        }
        System.out.println("Average number of unique results per query: " + (double)sumResultsPerQuery/(double)resultTracks.length);
        System.out.println("Total number of query candidate pairs: " + sumResultsPerQuery);
        
        //write query candidate file
            //id_query   	 id_team   	 id_song
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(queryCandidateFile) );
            System.out.println("Writing query/candidate pairs to: " + queryCandidateFile.getPath());
            
            //write header
            output.write("\"id_result\"\t\"id_query\"\t\"id_team\"\t\"id_song\"\t\"date\"");
            output.newLine();
            
            //format date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(Calendar.getInstance().getTime());
            
            //group by query 
            int id = 1;
            for (int i = 0; i <  queries.size(); i++ ) {
                for (int j = 0; j < teamNames.length; j++) {
                    List<List<String>> teamResults = teamResultsList.get(j);
                    List<String> resultsForQuery = teamResults.get(i);
                    for (int k = 0; k < resultsForQuery.size(); k++) {
                        output.write("\"" + id + "\"\t\"" + (i+1) + "\"\t\"" + (j+1) + "\"\t\"" + convertFileToMIREX_ID(new File(resultsForQuery.get(k))) + "\"\t\"" + date + "\"");
                        output.newLine();
                        id++;
                    }
                }
            }
        } finally {
            if (output != null) {
                output.close();
            }
        }
        
        //don't write unique queries file as we loaded that at start
        
        //re-encode audio files to a flat dir
        if (reencodeAudioDir != null){
            //make list of tracks to re-encode
            ArrayList<File> tracks = new ArrayList<File>();
            
            //do queries
            for (int i = 0; i < queryFiles.size(); i++) {
                tracks.add(new File(queryFiles.get(i)));
            }
            
            //do results
            for (int i = 0; i < resultTracks.length; i++) {
                HashSet hashSet = resultTracks[i];
                for (Iterator it = hashSet.iterator(); it.hasNext();) {
                    tracks.add(new File((String)it.next()));
                }
            }
            System.out.println("Re-encoding " + tracks.size() + " audio files to: " + reencodeAudioDir.getAbsolutePath());

            
            ReencodeAudioWithLameAndParameters reencoder = new ReencodeAudioWithLameAndParameters(localAudioDirFile,reencodeAudioDir,6);
            reencoder.setReplicateDirTree(false);
            reencoder.setResampleTo22k(true);
            reencoder.setBitrate(96);
            
            reencoder.reencodeFileList(tracks);
            
        }    
    }
    
    public static void runEval(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/to [filter/coversong/tracks(n)]
        String distMat = args[1];
        if (distMat.trim().equals("")){
            System.out.println("Unable to process distance matrix file name argument (" + distMat +")!\n" + USAGE);
            return;
        }
        File distMatFile = new File(distMat); 
        //read distanceMatrix
        DistanceMatrix distMatrix = DistanceMatrix.read(distMatFile);
        
        //read MusicDB Object describing collection
        String mDB = args[2];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        if ((!theDB.indexingMetadata(Signal.PROP_ARTIST))||(!theDB.indexingMetadata(Signal.PROP_GENRE)))
        {
            System.out.println("The metadata DB should index both artist and genre tags, the MusicDB loaded did not!");
            return;
        }
        
        //parse local dir of audio file argument
        String localAudioDir = args[3];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        try{
            theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
            distMatrix = RemapMusicDBFilenamesClass.remapDistanceMatrix(distMatrix, localAudioDir, true);
            
        }catch(IOException ioe){
            throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
        }
        
        //parse output file argument
        String out = args[4];
        if (out.trim().equals("")){
            System.out.println("Unable to process output result directory name argument (" + out +")!\n" + USAGE);
            return;
        }
        File outFile = new File(out);
        outFile.mkdirs();
        
        boolean filter = true;
        if (args.length > 5)
        {
            if (args[5].toLowerCase().equals("n")){
                filter = false;
            }
        }
        if (filter){
            //remove irrelevant coversongs
            List toRemove =  theDB.getSignalListForMetadataClass(Signal.PROP_GENRE, "cover song");
            System.out.println("Removing " + toRemove.size() + " cover songs from distance matrix (" + distMatrix.indexSize() + ") and metadata db (" + theDB.size() + ")");
            for (int i = toRemove.size()-1; i >= 0; i--) {
                distMatrix.removeFile(((Signal)toRemove.get(i)).getFile());
                theDB.removeSignalFromDatabase(((Signal)toRemove.get(i)));
            }
            System.out.println("New DistanceMatrix size: " + distMatrix.indexSize() + ", new MusicDB size: " + theDB.size());
        }
        
        //Init DistanceMatrix query object
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(distMatrix);
        
        theDB.evaluateRetriever(ret,outFile,81,200000);
    }
    
    
    public static void runTimesSim(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/to
        String distMat = args[1];
        if (distMat.trim().equals("")){
            System.out.println("Unable to process distance matrix file name argument (" + distMat +")!\n" + USAGE);
            return;
        }
        File distMatFile = new File(distMat); 
        //read distanceMatrix
        DistanceMatrix distMatrix = DistanceMatrix.read(distMatFile);
        
        //read MusicDB Object describing collection
        String mDB = args[2];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        
        if ((!theDB.indexingMetadata(Signal.PROP_ARTIST))||(!theDB.indexingMetadata(Signal.PROP_GENRE)))
        {
            System.out.println("The metadata DB should index both artist and genre tags, the MusicDB loaded did not!");
            return;
        }
        
        //parse local dir of audio file argument
        String localAudioDir = args[3];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        try{
            theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
            distMatrix = RemapMusicDBFilenamesClass.remapDistanceMatrix(distMatrix, localAudioDir, true);
            
        }catch(IOException ioe){
            throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
        }
        
        //parse output file argument
        String out = args[4];
        if (out.trim().equals("")){
            System.out.println("Unable to process output result directory name argument (" + out +")!\n" + USAGE);
            return;
        }
        File outFile = new File(out);
        outFile.mkdirs();
        
        boolean filter = true;
        if (args.length > 5)
        {
            if (args[5].toLowerCase().equals("n")){
                filter = false;
            }
        }
        if (filter){
            //remove irrelevant coversongs
            List toRemove =  theDB.getSignalListForMetadataClass(Signal.PROP_GENRE, "cover song");
            System.out.println("Removing " + toRemove.size() + " cover songs from distance matrix (" + distMatrix.indexSize() + ") and metadata db (" + theDB.size() + ")");
            for (int i = toRemove.size()-1; i >= 0; i--) {
                distMatrix.removeFile(((Signal)toRemove.get(i)).getFile());
                theDB.removeSignalFromDatabase(((Signal)toRemove.get(i)));
            }
            System.out.println("New DistanceMatrix size: " + distMatrix.indexSize() + ", new MusicDB size: " + theDB.size());
        }
        
        //Init DistanceMatrix query object
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(distMatrix);
        
        theDB.countTimesSimilar(ret,outFile);
    }
    
    public static void runTriEqTests(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/to
        String distMat = args[1];
        if (distMat.trim().equals("")){
            System.out.println("Unable to process distance matrix file name argument (" + distMat +")!\n" + USAGE);
            return;
        }
        File distMatFile = new File(distMat); 
        //read distanceMatrix
        DistanceMatrix distMatrix = DistanceMatrix.read(distMatFile);
        
        //parse output file argument
        String out = args[2];
        if (out.trim().equals("")){
            System.out.println("Unable to process output result directory name argument (" + out +")!\n" + USAGE);
            return;
        }
        File outFile = new File(out);
        outFile.mkdirs();
        
        distMatrix.testTriangularInequality(outFile,99,300000);
    }
    
    public static void runFiltGenre(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MetadataDB path/to/local/dir/of/audio/files path/to/save/report/to
        String distMat = args[1];
        if (distMat.trim().equals("")){
            System.out.println("Unable to process distance matrix file name argument (" + distMat +")!\n" + USAGE);
            return;
        }
        File distMatFile = new File(distMat); 
        //read distanceMatrix
        DistanceMatrix distMatrix = DistanceMatrix.read(distMatFile);
        
        //read MusicDB Object describing collection
        String mDB = args[2];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        
        if ((!theDB.indexingMetadata(Signal.PROP_ARTIST))||(!theDB.indexingMetadata(Signal.PROP_GENRE)))
        {
            System.out.println("The metadata DB should index both artist and genre tags, the MusicDB loaded did not!");
            return;
        }
        
        //parse local dir of audio file argument
        String localAudioDir = args[3];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        try{
            theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
            distMatrix = RemapMusicDBFilenamesClass.remapDistanceMatrix(distMatrix, localAudioDir, true);
            
        }catch(IOException ioe){
            throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
        }
        
        //parse output file argument
        String out = args[4];
        if (out.trim().equals("")){
            System.out.println("Unable to process output result directory name argument (" + out +")!\n" + USAGE);
            return;
        }
        File outFile = new File(out);
        outFile.mkdirs();
        
        boolean filter = true;
        if (args.length > 5)
        {
            if (args[5].toLowerCase().equals("n")){
                filter = false;
            }
        }
        if (filter){
            //remove irrelevant coversongs
            List toRemove =  theDB.getSignalListForMetadataClass(Signal.PROP_GENRE, "cover song");
            System.out.println("Removing " + toRemove.size() + " cover songs from distance matrix (" + distMatrix.indexSize() + ") and metadata db (" + theDB.size() + ")");
            for (int i = toRemove.size()-1; i >= 0; i--) {
                distMatrix.removeFile(((Signal)toRemove.get(i)).getFile());
                theDB.removeSignalFromDatabase(((Signal)toRemove.get(i)));
            }
            System.out.println("New DistanceMatrix size: " + distMatrix.indexSize() + ", new MusicDB size: " + theDB.size());
        }
        
        //Init DistanceMatrix query object
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(distMatrix);
        
        theDB.filteredGenreMatches(ret,outFile);
    }
    
    
            
    public static void runReturnResults(String[] args) throws IOException, noMetadataException{
        //process args -  path/to/DistMatrix path/to/MusicDB /path/to/local/dir/audio/files query numResult filter(y/n)
        String distMat = args[1];
        if (distMat.trim().equals("")){
            System.out.println("Unable to process distance matrix file name argument (" + distMat +")!\n" + USAGE);
            return;
        }
        File distMatFile = new File(distMat); 
        //read distanceMatrix
        DistanceMatrix distMatrix = DistanceMatrix.read(distMatFile);
        
        
        //read MusicDB Object describing collection
        String mDB = args[2];
        if (mDB.trim().equals("")){
            System.out.println("Unable to process metadata database file name argument (" + mDB +")!\n" + USAGE);
            return;
        }
        File mDBFile = new File(mDB); 
        
        //read MusicDB
        if (!mDBFile.exists()) {
            throw new RuntimeException("The file set as a parameter does not exist!\nFile: " + mDBFile.getAbsolutePath());
        }
        if(!mDBFile.canRead()) {
            throw new RuntimeException("Unable to read from file: " + mDBFile.getAbsolutePath());
        }
        
        FileInputStream fin;
        try {
            fin = new FileInputStream(mDBFile);
        } catch(FileNotFoundException fnf) {
            throw new RuntimeException("File could not be opened for reading,\n either file exists but is a directory rather than a regular file, does not exist, or cannot be opened for some other reason.",fnf);
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fin);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading stream header",ioe);
        }
        Object theOb;
        try {
            theOb = in.readObject();
        } catch(ClassNotFoundException cnf) {
            throw new RuntimeException("The class of the Serialized Object could not be found!",cnf);
        } catch(InvalidClassException ice) {
            throw new RuntimeException("Something is wrong with a class used by Serialization (wrong JRE version?)!",ice);
        } catch(StreamCorruptedException sce) {
            throw new RuntimeException("Control information in the stream is inconsistent!", sce);
        } catch(IOException ioe) {
            throw new RuntimeException("I/O error occured while reading in Object",ioe);
        }
        
        MusicDB theDB = (MusicDB)theOb;
        
        if ((!theDB.indexingMetadata(Signal.PROP_ARTIST))||(!theDB.indexingMetadata(Signal.PROP_GENRE)))
        {
            System.out.println("The metadata DB should index both artist and genre tags, the MusicDB loaded did not!");
            return;
        }
        
        //parse local dir of audio file argument
        String localAudioDir = args[3];
        if (localAudioDir.trim().equals("")){
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +")!\n" + USAGE);
            return;
        }
        File localAudioDirFile = new File(localAudioDir); 
        if (!localAudioDirFile.exists())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it did not exist!\n" + USAGE);
            return;
        }else if(!localAudioDirFile.isDirectory())
        {
            System.out.println("Unable to process local audio directory path argument (" + localAudioDir +") as it was not a directory!\n" + USAGE);
            return;
        }
        
        try{
            theDB = RemapMusicDBFilenamesClass.remap(theDB, localAudioDir, true, true);
            distMatrix = RemapMusicDBFilenamesClass.remapDistanceMatrix(distMatrix, localAudioDir, true);
            
        }catch(IOException ioe){
            throw new RuntimeException("An IOException occured while remapping file anems!\n",ioe);
        }
        
        //parse query file argument
        int query = Integer.parseInt(args[4]);
        
        //parse num results argument
        int numResults = Integer.parseInt(args[5]);
        
        
        boolean filter = true;
        if (args.length > 6)
        {
            if (args[6].toLowerCase().equals("n")){
                filter = false;
            }
        }
        if (filter){
            //remove irrelevant coversongs
            List toRemove =  theDB.getSignalListForMetadataClass(Signal.PROP_GENRE, "cover song");
            System.out.println("Removing " + toRemove.size() + " cover songs from distance matrix (" + distMatrix.indexSize() + ") and metadata db (" + theDB.size() + ")");
            for (int i = toRemove.size()-1; i >= 0; i--) {
                distMatrix.removeFile(((Signal)toRemove.get(i)).getFile());
                theDB.removeSignalFromDatabase(((Signal)toRemove.get(i)));
            }
            System.out.println("New DistanceMatrix size: " + distMatrix.indexSize() + ", new MusicDB size: " + theDB.size());
        }
        
        //Init DistanceMatrix query object
        DistanceMatrixRetriever ret = new DistanceMatrixRetriever(distMatrix);
        Signal[] files = ret.getSignals();
        SearchResult[] res = ret.retrieveNMostSimilar(files[query],numResults);
        
        System.out.println("\nResults:\n");
        for (int i = 0; i < res.length; i++) {
            System.out.println((i+1) + "\t" + res[i].getTheResult().getStringMetadata(Signal.PROP_FILE_LOCATION) + "\t" + res[i].getScore());
        }
    }
    
    public static void runEvalutronPostProcess(String[] args) throws IOException, noMetadataException{
        File teamTable = new File(args[1]);
        File resultTable = new File(args[2]);
        File taskTable = new File(args[3]);
        
        evaluateSystems(resultTable,taskTable,teamTable);
        
    }
    
    public static final int[] NOT_RELEVANT = new int[]{1,0};
    public static final int[] SOMEWHAT_RELEVANT = new int[]{2,1};
    public static final int[] RELEVANT = new int[]{3,2};
    
    
    private static int mapToCatScore(int returnedCat){
        switch(returnedCat){
            case 1:
                return NOT_RELEVANT[1];
            case 2:
                return SOMEWHAT_RELEVANT[1];
            case 3:
                return RELEVANT[1];
            case 4:
                throw new RuntimeException("Attempted to score a file marked as busted!");
            default:
                throw new RuntimeException("Unrecognised score (" + returnedCat + ")!");
        }
    }
    
    private static void evaluateSystems(File resultsTableFile, File taskTableFile, File teamTableFile) throws IOException{
        
        
        //parse team table
        //id_team 	title 	affiliation
        String[][] teamTable = DeliminatedTextFileUtilities.getDelimTextDataBlock(teamTableFile, ",", 0);
        
        ArrayList<String> systemNames = new ArrayList<String>(teamTable.length);
        ArrayList<Integer> systemIDs = new ArrayList<Integer>(teamTable.length);
        
        for (int i = 0; i < teamTable.length; i++) {
            systemIDs.add(Integer.parseInt(teamTable[i][0]));
            systemNames.add(teamTable[i][1]);
        }

        
        //parse results table and map result, query pairs to systems
        //id_result 	id_query 	id_team 	id_song 	date
        String[][] result_table = DeliminatedTextFileUtilities.getDelimTextDataBlock(resultsTableFile, ",", 0);
        
        HashMap<Integer,HashSet<String>>[] systemResultMaps = new HashMap[systemIDs.size()];
        for (int i = 0; i < systemResultMaps.length; i++) {
            systemResultMaps[i] = new HashMap<Integer, HashSet<String>>();
        }

        
        for (int i = 0; i < result_table.length; i++) {
            Integer id_team = new Integer(result_table[i][2]);
            String id_song = result_table[i][3];
            Integer id_query = new Integer(result_table[i][1]);
            
            int systemIdx = systemIDs.indexOf(id_team);
            
            HashSet<String> resultSetForQuery;
            if (systemResultMaps[systemIdx].containsKey(id_query)){
                resultSetForQuery = systemResultMaps[systemIdx].get(id_query);
            }else{
                resultSetForQuery = new HashSet<String>();
                systemResultMaps[systemIdx].put(id_query,resultSetForQuery);
            }
            resultSetForQuery.add(id_song);
        }

        
        //parse task table and index result scores
        //id_task 	tname 	id_evaluator 	id_query 	id_song 	relevance 	relevance2 	active 	date
        String[][] data = DeliminatedTextFileUtilities.getDelimTextDataBlock(taskTableFile, ",", 0);
        HashMap<Integer,HashMap<String,AtomicInteger>> queryToResultValidationCount = new HashMap<Integer,HashMap<String,AtomicInteger>>();
        HashMap<Integer,HashMap<String,Double>> queryToResultFineScoreSumMap = new HashMap<Integer,HashMap<String,Double>>();
        HashMap<Integer,HashMap<String,Double>> queryToResultCatScoreSumMap = new HashMap<Integer,HashMap<String,Double>>();
        
        
        for (int i = 0; i < data.length; i++) {
            
            Integer query = new Integer(data[i][3]);
            String result = data[i][4];
            
            Integer catScore = new Integer(data[i][5]);
            double fineScore = Double.parseDouble(data[i][6]);
            
            

            HashMap<String,AtomicInteger> validationCounts;
            HashMap<String,Double> fineScoreSums;
            HashMap<String,Double> catScoreSums;
            if (!queryToResultValidationCount.containsKey(query)){
                validationCounts = new HashMap<String,AtomicInteger>();
                fineScoreSums = new HashMap<String,Double>();
                catScoreSums = new HashMap<String,Double>();

                queryToResultValidationCount.put(query, validationCounts);
                queryToResultFineScoreSumMap.put(query, fineScoreSums);
                queryToResultCatScoreSumMap.put(query, catScoreSums);
            }else{
                validationCounts = queryToResultValidationCount.get(query);
                fineScoreSums = queryToResultFineScoreSumMap.get(query);
                catScoreSums = queryToResultCatScoreSumMap.get(query);
            }

            if (!validationCounts.containsKey(result)){
                validationCounts.put(result, new AtomicInteger(1));
                fineScoreSums.put(result, new Double(fineScore));
                catScoreSums.put(result, new Double(mapToCatScore(catScore)));
            }else{
                validationCounts.get(result).incrementAndGet();
                double oldFineSum = fineScoreSums.get(result).doubleValue();
                fineScoreSums.put(result,new Double(oldFineSum+fineScore));
                double oldCatSum = catScoreSums.get(result);
                catScoreSums.put(result,new Double(oldCatSum+mapToCatScore(catScore)));
            }

        }
        
        
        //compute average fine score and category score for each query for each system
        int numQueries = queryToResultValidationCount.size();
        Integer[] queries = queryToResultValidationCount.keySet().toArray(new Integer[numQueries]);
        
        double[][] avgFineScores = new double[systemNames.size()][numQueries];
        double[][] avgCatScores = new double[systemNames.size()][numQueries];
        
        HashMap<String,AtomicInteger> validationCounts;
        HashMap<String,Double> fineScoreSums;
        HashMap<String,Double> catScoreSums;
                
        for (int i = 0; i < numQueries; i++) {
            for (int j = 0; j < systemNames.size(); j++) {
                //get result list for query - and compute avg fine and cat scores
                HashSet<String> results = systemResultMaps[j].get(queries[i]);
                int numValid = 0;
                
                for (Iterator<String> iter = results.iterator(); iter.hasNext(); ) {
                    String aResult = iter.next();
                    validationCounts = queryToResultValidationCount.get(queries[i]);
                    fineScoreSums = queryToResultFineScoreSumMap.get(queries[i]);
                    catScoreSums = queryToResultCatScoreSumMap.get(queries[i]);

                    if (validationCounts.containsKey(aResult)){

                        avgFineScores[j][i] += fineScoreSums.get(aResult) / (double)validationCounts.get(aResult).intValue();
                        avgCatScores[j][i] += catScoreSums.get(aResult) / (double)validationCounts.get(aResult).intValue();

                        numValid++;
                    }

                    
                }
                avgFineScores[j][i] /= numValid;
                avgCatScores[j][i] /= numValid;
            }
        }

        //do data output
        System.out.println("Evaluation results:");
        System.out.println("System names:");
        for (int i = 0; i < systemNames.size(); i++) {
            System.out.println(i + ": " + systemNames.get(i));
        }

        System.out.println("");
        
        
        System.out.println("Average (across graders) per query fine scores:");
        System.out.print("query");
        for (int i = 0; i < systemNames.size(); i++) {
            System.out.print("," + systemNames.get(i));
        }
        System.out.println("");
        
        for (int i = 0; i < numQueries; i++) {
            System.out.print(queries[i]);
            for (int j = 0; j < systemNames.size(); j++) {
                System.out.print("," + avgFineScores[j][i]);
            }
            System.out.println("");
        }
        System.out.println("");
        
        
        
        System.out.println("Average (across graders) per query cat scores:");
        System.out.print("query");
        for (int i = 0; i < systemNames.size(); i++) {
            System.out.print("," + systemNames.get(i));
        }
        System.out.println("");
        
        for (int i = 0; i < numQueries; i++) {
            System.out.print(queries[i]);
            for (int j = 0; j < systemNames.size(); j++) {
                System.out.print("," + avgCatScores[j][i]);
            }
            System.out.println("");
        }
        System.out.println("");
        
    }
    
}
