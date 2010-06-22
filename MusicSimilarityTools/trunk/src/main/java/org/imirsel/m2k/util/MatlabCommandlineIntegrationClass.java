package org.imirsel.m2k.util;



import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A module that executes a command via the java <code>Runtime</code> object.
 * Specifically setup for executing commands in Matlab
 * @author  Kris West & Michael Mandel
 */
public class MatlabCommandlineIntegrationClass extends ExternalCommandlineIntegrationClass {

    String matlabArgs = "-nodesktop -nosplash";
    String matlabBin = "/usr/local/bin/matlab";
    String outputDir = "";
    boolean verbose = true;

    /**
     * Returns the arguments to pass to Matlab
     * @return the arguments to pass to Matlab
     */
    public String getMatlabArgs() {
        return matlabArgs;
    }

    /**
     * Sets the arguments to pass to Matlab
     * @param val the arguments to pass to Matlab
     */
    public void setMatlabArgs(String val) {
        matlabArgs = val;
    }

    /**
     * Get the path to the Matlab binary (fully qualified)
     * @return the fully qualified path to the matlab binary
     **/
    public String getMatlabBin() {
        return matlabBin;
    }

    /**
     * Set the path to the Matlab binary (fully qualified)
     * @param bin the fully qualified path to the matlab binary
     **/
    public void setMatlabBin(String bin) {
        this.matlabBin = bin;
    }

    /** Creates a new instance of MatlabIntegrationModule */
    public MatlabCommandlineIntegrationClass() {
        super();
        mainCommand = "defaultFile.m";
        commandFormattingStr = "('$i','$o')";
    }

    /**
     * Executes a command via the java <code>Runtime</code> object.
     * Specifically setup for executing commands in Matlab.
     * @throws java.lang.Exception If an error occurs.
     */
    protected void runCommand() throws IOException {
        // Get the input filename
        if (isRunning) {
            throw new RuntimeException("Attempted to run a command, while another command is already running!");
        }

        //System.out.println("Matlab Integration Module \nby Kris West, University of East Anglia, UK, kristopher.west@uea.ac.uk");

        // Create File to represent working directory
        File dir = new File(workingDir);

        // Get the output filename
        if (addExtensionToInput == false) {
            outfile = dir.getCanonicalPath() + File.separator + outputFilename;
        } else {
            File tempInFile = new File(inputFilename);
            String tempFileName = tempInFile.getName();
            if ((!this.replaceExtension.equals("")) && (tempFileName.endsWith(this.replaceExtension))) {
                int idx = tempFileName.lastIndexOf(this.replaceExtension);
                outfile = dir.getCanonicalPath() + File.separator + tempFileName.substring(0, idx) + extension;
            } else {
                outfile = dir.getCanonicalPath() + File.separator + (new File(inputFilename)).getName() + extension;
            }
        }

        // Set any environment variable required
        String[] envp = null;
        //envp[0] = "VARIABLE=VALUE"

        // Create command
        String[] argArray = matlabArgs.split(" ");
        String[] ExternalCommand = new String[argArray.length + 3];

        ExternalCommand[0] = this.matlabBin;

        for (int i = 0; i < argArray.length; i++) {
            ExternalCommand[i + 1] = argArray[i];
        }
        ExternalCommand[argArray.length + 1] = "-r";
        int cmdArgs = argArray.length + 2;
        ExternalCommand[cmdArgs] = mainCommand;
        String[] components = commandFormattingStr.split("[$]");

        for (int i = 0; i < components.length; i++) {
            if (components[i].length() >= 1) {
                char testSymbol = components[i].charAt(0);
                if (verbose) {
                    System.out.println("testSymbol: " + testSymbol);
                }
                switch (testSymbol) {
                    case 'i':
                        ExternalCommand[cmdArgs] += inputFilename + components[i].substring(1);
                        if (verbose) {
                            System.out.println("i component: " + components[i].substring(1));
                        }
                        break;
                    case 'o':
                        ExternalCommand[cmdArgs] += outfile + components[i].substring(1);
                        if (verbose) {
                            System.out.println("o component: " + components[i].substring(1));
                        }
                        break;
                    default:
                        ExternalCommand[cmdArgs] += components[i];
                        if (verbose) {
                            System.out.println("default component: " + components[i]);
                        }
                        break;
                }
            } else {
                ExternalCommand[cmdArgs] += components[i];
                if (verbose) {
                    System.out.println("short component: " + components[i]);
                }
            }
        }

        System.out.print("\tRunning command: ");
        for (int i = 0; i < ExternalCommand.length; i++) {
            System.out.print(ExternalCommand[i] + " ");
        }
        System.out.println();
        System.out.println("\tin directory: " + dir.getCanonicalPath());
        System.out.println();

        // Get a handle to java runtime
        Runtime runtime = Runtime.getRuntime();

        // Execute the command
        process = runtime.exec(ExternalCommand, envp, dir);
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
            Logger.getLogger(MatlabCommandlineIntegrationClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuntimeException ex) {
            Logger.getLogger(MatlabCommandlineIntegrationClass.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (process != null){
                process.destroy();
                process = null;
            }
        }
    }
}
