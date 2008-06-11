package org.imirsel.m2k.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;


/**
 * A class that executes a command via the java <code>Runtime</code> object.
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class ExternalIntegrationClass {
    
    String MainCommand = ""; // Command and parameters to run
    String OutputFilename = ""; //Filename to pass as Output
    String WorkingDir = "";  // Absolute path to working directory to run command in
    boolean AddExtensionToInput = false; // If set true, adds an extension to input file instead of using output file parameter
    String Extension = ""; //Extension to add to input filename
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
    public ExternalIntegrationClass() {
        MainCommand = "";
        OutputFilename = "";
        WorkingDir = "";
        AddExtensionToInput = false;
        Extension = "";
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
     * @param the extension that will be replaced if encountered.
     */
    public void setReplaceExtension(String theCommand) {
        this.replaceExtension = theCommand;
    }
    
    /** Returns the extension that will be replaced if encountered.
     * @return the extension that will be replaced if encountered.
     */
    public String getReplaceExtension() {
        return replaceExtension;
    }
    
    /** Sets the size of the Input buffer.
     * @param the size of the Input buffer.
     */
    public void setBufferSize(int theCommand) {
        this.bufferSize = theCommand;
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
        MainCommand = theCommand;
    }
    
    /** Returns the external command to be run.
     * @return the command to be run, as it would be entered on the command line
     */
    public String getMainCommand() {
        return MainCommand;
    }
    
    /** Sets filename to be passed as output to the next module in the itinery.
     * If <code>AddOutputFilenameToCommand</code> is set this filename is also appended
     * to the external command
     * @param outFile filename to be passed as output to the next module in the itinery
     */
    public void setOutputFilename(String outFile) {
        OutputFilename = outFile;
    }
    
    /** Returns filename to be passed as output to the next module in the itinery.
     * If <code>AddOutputFilenameToCommand</code> is set this filename is also appended
     * to the external command
     * @return filename to be passed as output to the next module in the itinery
     */
    public String getOutputFilename() {
        return OutputFilename;
    }
    
    /** Sets the path to the working directory to execute the external command in.
     * @param path The path to the working directory to execute the external command in
     */
    public void setWorkingDir(String path) {
        WorkingDir = path;
    }
    
    /** Returns the path to the working directory to execute the external command in.
     * @return The path to the working directory to execute the external command in
     */
    public String getWorkingDir() {
        return WorkingDir;
    }
    
    /** Sets the flag that determines whether an extension is added to the input
     * filename in order to generate the output filename, this takes precedence
     * over any output filename specified.
     * @param addExtension The value of the flag that determines whether an extension
     * is added to the input filename
     */
    public void setAddExtensionToInput(boolean addExtension) {
        AddExtensionToInput = addExtension;
    }
    
    /** Returns the value of the flag that determines whether an extension is added to the input
     * filename in order to generate the output filename, this takes precedence
     * over any output filename specified.
     * @return The value of the flag that determines whether an extension
     * is added to the input filename
     */
    public boolean getAddExtensionToInput() {
        return AddExtensionToInput;
    }
    
    /** Sets the extension that is added to the input filename in order to generate
     * the output filename, which takes precedence over any output filename specified.
     * @param ext The extension to add to the input filename
     */
    public void setExtension(String ext) {
        Extension = ext;
    }
    
    /** Returns the extension that is added to the input filename in order to generate
     * the output filename, which takes precedence over any output filename specified.
     * @return The extension to add to the input filename
     */
    public String getExtension() {
        return Extension;
    }
    
    /**
     * Prints out the buffered input stream
     * @param inputStream the <code>InputStream</code> to be printed out in the console
     * @throws Exception Thrown if the <code>InputStream</code> is unavailable
     */
    void ProcessInputStream(BufferedInputStream inputStream) throws Exception {
        int numBytes = 0;
        
        while (true) {
            int numBytesAvailable = 0;
            try {
                numBytesAvailable = inputStream.available();
            } catch (Exception e) {
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
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("inputStream.read() error!!!");
                throw e;
            }
            
            System.out.print(new String(OutputBuffer, OutputBufferIndex, numBytesAvailable));
            
            OutputBufferIndex += numBytesAvailable;
        }
    }
    
    private void runCommand(final String inputFilename) throws IOException, RuntimeException {
        System.out.println("External Code Integration Module \nby Kris West, University of East Anglia, UK, kristopher.west@uea.ac.uk");
        
        // Create File to represent working directory
        File dir = new File(WorkingDir);
        
        // Get the output filename
        if (AddExtensionToInput == false) {
            outfile = dir.getCanonicalPath() + File.separator + OutputFilename;
        } else {
            File tempInFile = new File(inputFilename);
            String tempFileName = tempInFile.getName();
            if ((!this.replaceExtension.equals(""))&&(tempFileName.endsWith(this.replaceExtension))) {
                int idx = tempFileName.lastIndexOf(this.replaceExtension);
                outfile = dir.getCanonicalPath() + File.separator + tempFileName.substring(0, idx) + Extension;
            } else {
                outfile = dir.getCanonicalPath() + File.separator + (new File(inputFilename)).getName() + Extension;
            }
        }
        
        // Set any environment variable required
        String[] envp = null;//new String[0];
        
        //envp[0] = "VARIABLE=VALUE"
        
        // Create command
        String ExternalCommand = "";
        String[] components = commandFormattingStr.split("[$]");
        
        File command = new File(MainCommand);
        if (!command.exists()) {
            File command2 = new File(dir.getCanonicalPath() + File.separator + MainCommand);
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
    
}
