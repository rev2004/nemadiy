package org.imirsel.m2k.evaluation2;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;


/**
 * A class that executes a command via the java <code>Runtime</code> object.
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class ExternalBinaryIntegratorImpl {
    
    
    boolean isRunning = false;
    BufferedInputStream inputStream;
    BufferedInputStream errorStream;
    //java.io.BufferedInputStream
    Process process = null;
    //Buffer size in bytes
    int bufferSize = 1048576; // 1 mb buffer
    byte[] OutputBuffer = new byte[bufferSize];
    int OutputBufferIndex = 0;
    
    /** Creates a new instance of ExternalIntegrationModule. */
    public ExternalBinaryIntegratorImpl() {
       
    }
    
    /** Kills the process representing the command
     */
    public void killProcess()
    {
        if (process != null) {
            process.destroy();
        }
    }
    
//    /** Sets the size of the Input buffer.
//     * @param the size of the Input buffer.
//     */
//    public void setBufferSize(int theCommand) {
//        this.bufferSize = theCommand;
//    }
//    
//    /** Returns the size of the Input buffer.
//     * @return the size of the Input buffer.
//     */
//    public int getBufferSize() {
//        return bufferSize;
//    }
    
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
    
    /**
     * Runs the binary located at MainCommand with the specified working directory. The parameters of 
     * the command are formated according to the commandFormattingStr, the list of input files and
     * the output file name.
     *
     * legal command formatting strings might include: $m$1 $o
     *                                                 $m -param 74 $1 $2 $o
     *                                                 $m -param 74 -1 $1 -2 $2 -3 $3 -out $o
     *                                                 $m -param 74 -model $1 -codebook $2 -audiofile $3 $o
     **/
    public void runCommand(final String MainCommand, final String WorkingDir, final String commandFormattingStr, final String[] inputFilenames, final String outfile, String[] envVariables) throws IOException, RuntimeException
    {
        System.out.println("External Code Integration Module \nby Kris West, University of East Anglia, UK, kristopher.west@uea.ac.uk");
        
        // Create File to represent working directory
        File dir = new File(WorkingDir);
        // Get the output filename
        
        // Set any environment variable required
        //String[] envp = null;//new String[0];
        
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
                if (testSymbol == 'm') {
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
                }        //System.out.println("m component: " + components[i].substring(1));
                else if(Character.isDigit(testSymbol)){    //cmdArray[cmdCount] = "\"" + inputFilename + "\"";
                        cmdArray[cmdCount] = inputFilenames[Integer.parseInt("" + testSymbol)-1];
                        cmdCount++;
                        if(!components[i].substring(1).trim().equals("")) {
                            String[] comps = components[i].substring(1).trim().split(" ");
                            for (int j=0;j<comps.length;j++) {
                                cmdArray[cmdCount] = comps[j].trim();
                                cmdCount++;
                            }
                        }
                        //System.out.println("i component: " + components[i].substring(1));
                }       //case 'o': ExternalCommand += "\"" + outfile + "\"" + components[i].substring(1);
                else if (testSymbol == 'o') {
                
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
                }
                else{
                    
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
        process = runtime.exec(cmdArray,envVariables,dir);
        
        
        this.isRunning = true;
        inputStream = new BufferedInputStream(process.getInputStream(), bufferSize);
        errorStream = new BufferedInputStream(process.getErrorStream(), bufferSize);
    }
    
}
