package org.imirsel.m2k.util;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Re-encodes MP3 and WAV files using lame. Used to ensure that all files that 
 * features are extracted from are at a common sample and bitrate (as variation
 * can lead to unacceptable variation in the values of features extracted).
 * 
 * Requires Lame to be installed, location of the binary is configurable 
 * (default /usr/bin/lame).
 * 
 * Can be run as a main class for batch processing folders or the static 
 * reencode method may be called from other classes.
 * 
 * @author kriswest
 */
public class ReencodeAudioWithLameAndParameters {
    
    /**
     * Command to call lame and parameter settings to pass.
     */
    //public static String[] LAME_CMD_Arr = new String[]{};
    
    /**
     * The timeout for lame extractions.
     */
    private static final int LAME_TIMEOUT = 240000;
    
    //variables for batch mode
    private File inputDir; 
    private File outputDir;
    private int numThreads;
    private ArrayList<File> dirsToDo = new ArrayList<File>();
    
    
    int bitrate = 64;
    boolean resampleTo22k = true;
    
    private String lamePath = "/usr/bin/lame";
    
    private boolean replicateDirTree = true;

    public void reencodeFileList(List<File> filesToDo) {
        LameThread[] threads = new LameThread[numThreads];
        int numPerThread = (int) Math.floor(filesToDo.size() / numThreads);
        int numBeforeLastThread = numPerThread * (numThreads - 1);
        int numLastThread = filesToDo.size() - numBeforeLastThread;

        for (int i = 0; i < threads.length - 1; i++) {
            File[] anArray = new File[numPerThread];
            for (int j = 0; j < anArray.length; j++) {
                anArray[j] = filesToDo.get(j + (numPerThread * i));
            }
            threads[i] = new LameThread(anArray, inputDir, outputDir);
        }
        File[] anArray = new File[numLastThread];
        for (int j = 0; j < anArray.length; j++) {
            anArray[j] = filesToDo.get(j + numBeforeLastThread);
        }
        threads[numThreads - 1] = new LameThread(anArray, inputDir, outputDir);

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("\n\nDONE!!!!\n\n\n");
    }
    
    public void setBitrate(int rate){
        bitrate = rate;
    }
    
    public void setResampleTo22k(boolean val){
        resampleTo22k = val;
    }
    
    public ReencodeAudioWithLameAndParameters(){
        inputDir = null;
        outputDir = null;
        numThreads = 1;
    }
    
    /**
     * Creates a new instance of the ReencodeAudioWithLameAndParameters main class. If 
     * calling the reencode method from another class you do not need to 
     * instantiate it.
     * 
     * @param inputDir_ The root directory of the audio collection. This will be 
     * used clone the directory tree beneath this directory - ensuring the same
     * directory structure in the output directory as the input directory.
     * @param outputDir_ The directory to output the encoded audio files into.
     * @param numThreads_ The number of threads to run the tool with.
     */
    public ReencodeAudioWithLameAndParameters(File inputDir_, File outputDir_, int numThreads_) {
        
        if (!outputDir_.exists()){
            outputDir_.mkdirs();
        }
        
        inputDir = inputDir_;
        outputDir = outputDir_;
                
        numThreads = numThreads_;
        
        dirsToDo.add(inputDir);
    }
    
    /**
     * Main method. Process command line arguments and executes the tool.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args){
        if (args.length != 3){
            System.out.println("Args: /input/dir/path /output/dir/path numThreads");
        }
        ReencodeAudioWithLameAndParameters tool = new ReencodeAudioWithLameAndParameters(new File(args[0]),new File(args[1]),Integer.parseInt(args[2]));
        tool.run();
    }
    
    /**
     * Executes the tool.
     */
    public void run(){
        
        System.out.println("Indexing MP3 files.");
        ArrayList<File> filesToDo = new ArrayList<File>();
   
        while(!dirsToDo.isEmpty()){
            
            File aDir = dirsToDo.remove(0);
            File[] files = aDir.listFiles();
            
            for (int i = 0; i < files.length; i++) {
                if(files[i].isDirectory()){
                    dirsToDo.add(files[i]);
                }else if (files[i].getName().toLowerCase().endsWith(".mp3")){
                    filesToDo.add(files[i]);
                }
            }
            System.out.println("found " + filesToDo.size() + " MP3 files");
        }
        
        
        System.out.println("re-encoding all files...");
        
        reencodeFileList(filesToDo);
    }

    public

    String getLamePath() {
        return lamePath;
    }

    public void setLamePath(String lamePath) {
        this.lamePath = lamePath;
    }

    public boolean getReplicateDirTree() {
        return replicateDirTree;
    }

    public void setReplicateDirTree(boolean replicateDirTree) {
        this.replicateDirTree = replicateDirTree;
    }

    /** Basic thread class for for implementing batch processing of directories
     * of audio files.
     */
    private class LameThread extends Thread{
        File[] theFiles;
        File inputDir;
        File outputDir;
        Runtime runtime;
        
        /**
         * Instantiate the LameThread Object.
         * 
         * @param theFiles_ An array of the File Objects to reencode.
         * @param inputDir_ The root directory of the audio collection. This will be 
         * used clone the directory tree beneath this directory - ensuring the same
         * directory structure in the output directory as the input directory.
         * @param outputDir_ The directory to output the encoded audio files
         * into.
         */
        LameThread (File[] theFiles_, final File inputDir_, final File outputDir_){
            theFiles = theFiles_;
            inputDir = inputDir_;
            outputDir = outputDir_;
            
            runtime = Runtime.getRuntime();
            
            System.out.println("Created thread to process " + theFiles.length + " MP3 files");
        }
        
        public void run(){
            for (int i = 0; i < theFiles.length; i++) {
                
                reencodeFile(theFiles[i],inputDir,outputDir,runtime);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (i % 10 == 0){
                    System.out.println("Done " + (i+1) + " of " + theFiles.length);
                }
            }
            System.out.println("LameThread exiting...");
        }
    }
    
    /**
     * Execute lame on the specified file. The directory structure beneath the 
     * input directory is cloned.
     * 
     * @param aFile The file to reencode.
     * @param inputDir The root directory of the audio collection. This will be 
     * used clone the directory tree beneath this directory - ensuring the same
     * directory structure in the output directory as the input directory.
     * @param outputDir The directory to output the encoded audio files
     * into.
     * @param runtime A reference to the Runtime.
     * @return A File Object representing the location that the encoded audio 
     * was written to.
     */
    public File reencodeFile(final File aFile, final File inputDir, final File outputDir, Runtime runtime) {
        //set input files
        
        File outputFile;
        if (replicateDirTree){
            String path = aFile.getAbsolutePath();
            outputFile = new File(path.replaceFirst(inputDir.getAbsolutePath(),outputDir.getAbsolutePath()));
        }else{
            String filename = aFile.getName().replaceAll(".wav", ".mp3");
            filename = filename.replaceAll(".WAV", ".mp3");
            filename = filename.replaceAll(".MP3", ".mp3");
            outputFile = new File(outputDir.getAbsolutePath() + File.separator + filename);
        }
        
        if (outputFile.exists()){
             System.out.println("File exists, skipping...");
             return outputFile;
        }
        
        return reencodeFile(runtime, outputFile, aFile);
    }
    
    /**
     * Execute lame on the specified file ad output to the specified location.
     * 
     * @param runtime A reference to the Runtime.
     * @param outputFile The file to write the encoded audio to.
     * @param file The audio file to rencode.
     * @return A File Object representing the location that the encoded audio 
     * was written to. 
     */
    public File reencodeFile(final Runtime runtime, final File outputFile, final File file) {
        
        System.out.println("Re-encoding " + file.getAbsolutePath() + " to " + outputFile.getAbsolutePath());
        int command_len = 7;
        if (resampleTo22k){
            command_len = 9;
        }
        String[] cmdArray = new String[command_len];
        int cmdIndex = 0;
        cmdArray[cmdIndex] = "/usr/bin/lame";
        cmdArray[++cmdIndex] = "-cbr";
        cmdArray[++cmdIndex] = "-b";
        cmdArray[++cmdIndex] = "" + bitrate;
        if (resampleTo22k){
            cmdArray[++cmdIndex] = "--resample";
            cmdArray[++cmdIndex] = "22";
        }
        cmdArray[++cmdIndex] = "--quiet";
        
        cmdArray[++cmdIndex] = file.getAbsolutePath();
        
        File outputDir = outputFile.getAbsoluteFile().getParentFile();
        if (!outputDir.exists()){
            outputDir.mkdirs();
        }
        
        cmdArray[++cmdIndex] = outputFile.getAbsolutePath();
        
        try {
            //stuff for monitoring lame
            int bufferSize = 1048576; // 1Mb buffer
            byte[] OutputBuffer = new byte[bufferSize];
            int OutputBufferIndex = 0;

            String cmd = "";
            for (int i = 0; i < command_len; i++) {
                cmd += cmdArray[i] + " ";
            }

            System.out.println("Executing: " + cmd);
            
            Map envmap = System.getenv();
            
            Iterator iter = envmap.keySet().iterator();
            ArrayList<String> envList = new ArrayList<String>();
            //System.out.println("environment:");
            while(iter.hasNext()){
                
                String key = (String)iter.next();
                String out = key + "=" + envmap.get(key);
                envList.add(out);
                //System.out.println(out);
            }
            String[] env = envList.toArray(new String[envList.size()]);
            
            Process proc = runtime.exec(cmdArray,env);
            
            
            BufferedInputStream inputStream = new BufferedInputStream(proc.getInputStream(), bufferSize);
            BufferedInputStream errorStream = new BufferedInputStream(proc.getErrorStream(), bufferSize);
            long startTime = System.currentTimeMillis();
            
            int numBytes = 0;
            int exitVal = -1;
            int timesRestarted = 0;
            while (true){
                try {
                    OutputBufferIndex = getOutputFromLame(OutputBufferIndex, errorStream, inputStream, numBytes, OutputBuffer);
                    exitVal = proc.exitValue();
                    break;
                } catch (IllegalThreadStateException e) {
                    
                    //check if thread got stuck
                    if (System.currentTimeMillis() - startTime > LAME_TIMEOUT){

                        if (timesRestarted >= 3){
                            System.out.println("\n\n!!!!! Lame got stuck more than 3 times on file: " + file.getAbsolutePath());
                            System.out.println("!!!!! Deleting file produced and moving on\n\n");
                            proc.destroy();
                            if (outputFile.exists()){
                                outputFile.delete();
                            }
                            return null;
                        }

                        proc.destroy();
                        if (outputFile.exists()){
                            outputFile.delete();
                        }
                        try {
                            inputStream.close();
                            errorStream.close();
                        } catch (IOException ex) {}
                        
                        
                        //re-init thread
                        proc = runtime.exec(cmdArray);

                        OutputBuffer = new byte[bufferSize];
                        OutputBufferIndex = 0;

                        inputStream = new BufferedInputStream(proc.getInputStream(), bufferSize);
                        errorStream = new BufferedInputStream(proc.getErrorStream(), bufferSize);

                        timesRestarted++;
                        System.out.println("!!!!!!!! Lame got stuck! Restarted thread " + timesRestarted + " time(s)");
                        startTime = System.currentTimeMillis();

                    }
                    
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                
                
            }
                
            inputStream.close();
            errorStream.close();
            //int exitVal = proc.waitFor();
            if (exitVal != 0){
                System.out.println("Encoding failed with code " + exitVal);
            }else{
                System.out.println("Lame exited with code 0");
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return outputFile;
    }

    private static int getOutputFromLame(int OutputBufferIndex, BufferedInputStream errorStream, BufferedInputStream inputStream, int numBytes, byte[] OutputBuffer) {
        //collect both standard and error streams from lame 
        while (true) {
            int numBytesAvailable = 0;
            try {
                numBytesAvailable = inputStream.available();
            } catch (Exception e) {
                System.out.println("inputStream.available() error!!!");
            }

            if (numBytesAvailable == 0) {
                break;
            }
            if (numBytesAvailable != 0){
                try {
                    numBytes = inputStream.read(OutputBuffer, OutputBufferIndex, numBytesAvailable);
                    if (numBytes != numBytesAvailable) {
                        System.out.println("numBytes != numBytesAvailable");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("inputStream.read() error!!!");
                }

                
                //collect error stream
                System.out.print(new String(OutputBuffer, OutputBufferIndex, numBytesAvailable));
                OutputBufferIndex += numBytesAvailable;
                
            }
            
            numBytesAvailable = 0;
            try {
                numBytesAvailable = errorStream.available();
            } catch (Exception e) {
                System.out.println("inputStream.available() error!!!");
            }

            if (numBytesAvailable == 0) {
                break;
            }

            try {
                numBytes = errorStream.read(OutputBuffer, OutputBufferIndex, numBytesAvailable);
                if (numBytes != numBytesAvailable) {
                    System.out.println("numBytes != numBytesAvailable");
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("inputStream.read() error!!!");
            }

            System.out.print(new String(OutputBuffer, OutputBufferIndex, numBytesAvailable));

            OutputBufferIndex += numBytesAvailable;
        }
        return OutputBufferIndex;
    }
    
}
