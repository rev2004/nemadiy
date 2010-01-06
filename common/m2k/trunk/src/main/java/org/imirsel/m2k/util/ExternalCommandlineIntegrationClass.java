package org.imirsel.m2k.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A class that executes a command via the java <code>Runtime</code> object.
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class ExternalCommandlineIntegrationClass extends Thread{
    
    String mainCommand = ""; // Command and parameters to run
    String outputFilename = ""; //Filename to pass as Output
    String inputFilename = ""; //Filename to pass as Input
    String workingDir = "";  // Absolute path to working directory to run command in
    boolean addExtensionToInput = false; // If set true, adds an extension to input file instead of using output file parameter
    String extension = ""; //extension to add to input filename
    String commandFormattingStr = "$m -anOption $i $o";
    String replaceExtension = "";
    boolean isRunning = false;
    BufferedInputStream inputStream;
    BufferedInputStream errorStream;
    //java.io.BufferedInputStream
    String outfile;
    Process process = null;
    //Buffer size in bytes
    int bufferSize = 1048576; // 1 mb buffer
    byte[] OutputBuffer = new byte[bufferSize];
    int OutputBufferIndex = 0;
    
    /** Creates a new instance of ExternalIntegrationModule. */
    public ExternalCommandlineIntegrationClass() {
        mainCommand = "";
        outputFilename = "";
        workingDir = "";
        addExtensionToInput = false;
        extension = "";
    }
    
    /** Kills the process representing the command
     */
    public void killProcess()
    {
        if (process != null) {
            process.destroy();
        }
    }
    
    /** Sets the extension that will be replaced if encountered.
     * @param theExtension the extension that will be replaced if encountered.
     */
    public void setReplaceExtension(String theExtension) {
        this.replaceExtension = theExtension;
    }
    
    /** Returns the extension that will be replaced if encountered.
     * @return the extension that will be replaced if encountered.
     */
    public String getReplaceExtension() {
        return replaceExtension;
    }
    
    /** Sets the size of the Input buffer.
     * @param bufferSize_ the size of the Input buffer.
     */
    public void setBufferSize(int bufferSize_) {
        this.bufferSize = bufferSize_;
    }
    
    /** Returns the size of the Input buffer.
     * @return the size of the Input buffer.
     */
    public int getBufferSize() {
        return bufferSize;
    }
    
    /** Sets the external command formatting string.
     * @param theCommand a string describing how the command should be entered on the command line
     */
    public void setCommandFormattingStr(String theCommand) {
        commandFormattingStr = theCommand;
    }
    
    /** Returns the external command formatting string.
     * @return a string describing how the command should be entered on the command line
     */
    public String getCommandFormattingStr() {
        return commandFormattingStr;
    }
    
    /** Sets the external command to be run.
     * @param theCommand the command to be run, as it would be entered on the command line
     */
    public void setMainCommand(String theCommand) {
        mainCommand = theCommand;
    }
    
    /** Returns the external command to be run.
     * @return the command to be run, as it would be entered on the command line
     */
    public String getMainCommand() {
        return mainCommand;
    }
    
    /** Sets filename to be used as the output file by the external command.
   * @param outFile filename to be used as the output file by the external command.
     */
    public void setOutputFilename(String outFile) {
        outputFilename = outFile;
    }
    
    /** Returns filename to be used as the output file by the external command.
     * @return filename to be used as the output file by the external command.
     */
    public String getOutputFilename() {
        return outputFilename;
    }
    
    /** Sets filename to be passed as the input file path.
     * @param inFile filename to be passed as the input file path.
     */
    public void setInputFilename(String inFile) {
        inputFilename = inFile;
    }
    
    /** Sets filename to be passed as the input file path.
     * @return filename to be passed as the input file path.
     */
    public String getInputFilename() {
        return inputFilename;
    }
    
    /** Sets the path to the working directory to execute the external command in.
     * @param path The path to the working directory to execute the external command in
     */
    public void setWorkingDir(String path) {
        workingDir = path;
    }
    
    /** Returns the path to the working directory to execute the external command in.
     * @return The path to the working directory to execute the external command in
     */
    public String getWorkingDir() {
        return workingDir;
    }
    
    /** Sets the flag that determines whether an extension is added to the input
     * filename in order to generate the output filename, this takes precedence
     * over any output filename specified.
     * @param addExtension The value of the flag that determines whether an extension
     * is added to the input filename
     */
    public void setAddExtensionToInput(boolean addExtension) {
        addExtensionToInput = addExtension;
    }
    
    /** Returns the value of the flag that determines whether an extension is added to the input
     * filename in order to generate the output filename, this takes precedence
     * over any output filename specified.
     * @return The value of the flag that determines whether an extension
     * is added to the input filename
     */
    public boolean getAddExtensionToInput() {
        return addExtensionToInput;
    }
    
    /** Sets the extension that is added to the input filename in order to generate
     * the output filename, which takes precedence over any output filename specified.
     * @param ext The extension to add to the input filename
     */
    public void setExtension(String ext) {
        extension = ext;
    }
    
    /** Returns the extension that is added to the input filename in order to generate
     * the output filename, which takes precedence over any output filename specified.
     * @return The extension to add to the input filename
     */
    public String getExtension() {
        return extension;
    }
    
    /**
     * Prints out the buffered input stream
     * @param inputStream the <code>InputStream</code> to be printed out in the console
     * @throws Exception Thrown if the <code>InputStream</code> is unavailable
     */
    void processInputStream(BufferedInputStream inputStream) throws IOException{
        int numBytes = 0;
        
        while (true) {
            int numBytesAvailable = 0;
            try {
                numBytesAvailable = inputStream.available();
            } catch (IOException e) {
                System.out.println("inputStream.available() error!!!");
                throw e;
            }
            
            if (numBytesAvailable == 0) {
                break;
            }
            
            try {
                numBytes = inputStream.read(OutputBuffer, OutputBufferIndex, numBytesAvailable);
                if (numBytes != numBytesAvailable) {
                    System.out.println("numBytes != numBytesAvailable");
                    throw new IOException("numBytes != numBytesAvailable");
                }
            } catch (IOException e) {
                System.out.println("inputStream.read() error!!!");
                throw e;
            }
            
            System.out.print(new String(OutputBuffer, OutputBufferIndex, numBytesAvailable));
            
            OutputBufferIndex += numBytesAvailable;
        }
    }
    
    private void runCommand() throws IOException {
        
        if (isRunning){
            throw new RuntimeException("Attempted to run a command, while another command is already running!");
        }
        
        // Create File to represent working directory
        File dir = new File(workingDir);
        
        // Get the output filename
        if (addExtensionToInput == false) {
            outfile = dir.getCanonicalPath() + File.separator + outputFilename;
        } else {
            File tempInFile = new File(inputFilename);
            String tempFileName = tempInFile.getName();
            if ((!this.replaceExtension.equals(""))&&(tempFileName.endsWith(this.replaceExtension))) {
                int idx = tempFileName.lastIndexOf(this.replaceExtension);
                outfile = dir.getCanonicalPath() + File.separator + tempFileName.substring(0, idx) + extension;
            } else {
                outfile = dir.getCanonicalPath() + File.separator + (new File(inputFilename)).getName() + extension;
            }
        }
        
        // Set any environment variable required
        String[] envp = null;//new String[0];
        
        //envp[0] = "VARIABLE=VALUE"
        
        // Create command
        String ExternalCommand = "";
        String[] components = commandFormattingStr.split("[$]");
        
        File command = new File(mainCommand);
        if (!command.exists()) {
            File command2 = new File(dir.getCanonicalPath() + File.separator + mainCommand);
            if (!command2.exists()) {
                throw new RuntimeException("External Integration module was unable to locate your command!\n" +
                        "File names tried:\n\t" + command.getCanonicalPath() + "\n\t" + command2.getCanonicalPath() + "\n" +
                        "Please ensure that your binaries are in the working directory set in the ExternalInteration " +
                        "modules's properties panel.");
            } else {
                command = command2;
            }
        }
        
        //System.out.println("Num components: " + components.length);
        int commandLength = 0;//components.length;
        String[] cmdArray;
        for (int i=0;i<components.length;i++) {
            if (components[i].length() >= 1) {
                char testSymbol = components[i].charAt(0);
                //System.out.println("testSymbol: " + testSymbol);
                switch (testSymbol) {
                    //case 'm': ExternalCommand += "\"" + command.getCanonicalPath() + "\"" + components[i].substring(1);
                    case 'm':
                        commandLength++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            commandLength += comps.length;
                        }
                        //System.out.println("m component: " + components[i].substring(1));
                        break;
                        // case 'i': ExternalCommand += "\"" + inputFilename + "\"" + components[i].substring(1);
                    case 'i':
                        commandLength++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            commandLength += comps.length;
                        }
                        //System.out.println("i component: " + components[i].substring(1));
                        break;
                        //case 'o': ExternalCommand += "\"" + outfile + "\"" + components[i].substring(1);
                    case 'o':
                        commandLength++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            commandLength += comps.length;
                        }
                        //System.out.println("o component: " + components[i].substring(1));
                        break;
                        //default: ExternalCommand += components[i];
                    default:
                        if(components[i].trim().equals("")) {
                            //commandLength--;
                        } else {
                            String[] comps = components[i].trim().split(" ");
                            commandLength += comps.length;
                        }
                        break;
                }
            } else {
                //ExternalCommand += components[i];
                //System.out.println("short component: " + components[i]);
            }
        }
        cmdArray = new String[commandLength];
        
        int cmdCount = 0;
        for (int i=0;i<components.length;i++) {
            if (components[i].length() >= 1) {
                char testSymbol = components[i].charAt(0);
                //System.out.println("testSymbol: " + testSymbol);
                switch (testSymbol) {
                    //case 'm': ExternalCommand += "\"" + command.getCanonicalPath() + "\"" + components[i].substring(1);
                    case 'm':
                        //cmdArray[cmdCount] = "\"" + command.getCanonicalPath() + "\"";
                        cmdArray[cmdCount] = command.getCanonicalPath();
                        cmdCount++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            for (int j=0;j<comps.length;j++) {
                                cmdArray[cmdCount] = comps[j].trim();
                                cmdCount++;
                            }
                        }
                        //System.out.println("m component: " + components[i].substring(1));
                        break;
                        // case 'i': ExternalCommand += "\"" + inputFilename + "\"" + components[i].substring(1);
                    case 'i':
                        //cmdArray[cmdCount] = "\"" + inputFilename + "\"";
                        cmdArray[cmdCount] = inputFilename;
                        cmdCount++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            for (int j=0;j<comps.length;j++) {
                                cmdArray[cmdCount] = comps[j].trim();
                                cmdCount++;
                            }
                        }
                        //System.out.println("i component: " + components[i].substring(1));
                        break;
                        //case 'o': ExternalCommand += "\"" + outfile + "\"" + components[i].substring(1);
                    case 'o':
                        //cmdArray[cmdCount] = "\"" + outfile + "\"";
                        cmdArray[cmdCount] = outfile;
                        cmdCount++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            for (int j=0;j<comps.length;j++) {
                                cmdArray[cmdCount] = comps[j].trim();
                                cmdCount++;
                            }
                        }
                        //System.out.println("o component: " + components[i].substring(1));
                        break;
                    default:
                        if(components[i].trim().equals("")) {
                        } else {
                            String[] comps = components[i].trim().split(" ");
                            for(int j=0;j<comps.length;j++) {
                                cmdArray[cmdCount] = comps[j].trim();
                                cmdCount++;
                            }
                            
                        }
                        break;
                }
            } else {
                //ExternalCommand += components[i];
                //System.out.println("short component: " + components[i]);
            }
        }
        
        System.out.print("\tRunning command: ");
        for (int i=0;i<cmdArray.length;i++) {
            System.out.print(cmdArray[i] + " ");
        }
        System.out.println("");
        System.out.println("\tin directory: " + dir.getCanonicalPath());
        
        // Get a handle to java runtime
        Runtime runtime = Runtime.getRuntime();
        //String saved = System.getProperty("user.dir");
        
        //System.setProperty("user.dir",dir.getCanonicalPath());
        //process = runtime.exec(ExternalCommand,envp,dir);
        process = runtime.exec(cmdArray,envp,dir);
        
        
        this.isRunning = true;
        inputStream = new BufferedInputStream(process.getInputStream(), bufferSize);
        errorStream = new BufferedInputStream(process.getErrorStream(), bufferSize);
    }
    
    public void run() {
        try {
            runCommand();
        
            while(isRunning){
                processInputStream(inputStream);
                processInputStream(errorStream);
                try{
                    int exitVal = process.exitValue();
                    System.out.println("precess exited with status: " + exitVal);
                    break;
                }catch(IllegalThreadStateException e){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MatlabCommandlineIntegrationClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExternalCommandlineIntegrationClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuntimeException ex) {
            Logger.getLogger(ExternalCommandlineIntegrationClass.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (process != null){
                process.destroy();
                process = null;
            }
        }
    }
    
}
