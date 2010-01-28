/*
 * InputSignals.java
 *
 * Created on May 9, 2005, 3:29 PM
 */

package org.imirsel.m2k.io.file;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import java.util.Vector;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.JOptionPane;
import java.net.*;

/**
 * Reads in one or more files or directories of files and outputs them as Signal 
 * objects. This module has support for the streaming out of mutiple directories,
 * directory recursion and can set classname metadata in the Signal objects with
 * classnames calculated from the directory names, the top-level directories of a
 * directory structure or classnames set manually. This module can also read its
 * settings in from a file and and can save manual settings out to a file. Finally, an
 * Integer is output indicating the number of classes output.
 * @author Andreas Ehmann & Kris West
 */
public class InputSignalsClass implements Serializable
{
    // Declare any global variables and parameter properties for the module.
    Signal[] theList;
    
    Vector names;			//Strings
    Vector classNames;                  //Strings (either from classNames field or from dir names)
    Vector recurseSubDirs;		//Boolean objects
    Vector substringFileFilters; 	//Strings
    Vector excludeFileFilters;          //Strings
    Vector getNameFrom;                 //Strings{"dir","toplevelDir","manual"}
    
    /**
     * Initialise InputSignals module
     */
    public InputSignalsClass()
    {
        names = new Vector();			//Strings
        classNames = new Vector();		//Strings (either from classNames field or from dir names)
        recurseSubDirs = new Vector();		//Boolean objects
        substringFileFilters = new Vector(); 	//Strings
        excludeFileFilters = new Vector(); 	//Strings
        getNameFrom = new Vector();	
    }
    
    /**
     * Adds a full set of file input data (one record) to the storage vectors, ready to
     * produce the list of files to output.
     * @param filename File or directory path to add
     * @param className If using manual classnames, the classname to be used, otherwise should be empty String
     * @param recurseSubDirs boolean flag indicating whether a directory should be recursed for files or 
     * whether only the toplevel files should be returned.
     * @param substringFileFilter Require this substring to be in returned filenames
     * @param excludeFileFilter exclude fileames with this substring
     * @param getNamesFrom String flag indicating where classnames should come from. Set to "dir", if the 
     * directory name is to be used, "topLevelDirs" if the top-level of a directory
     * structure is to be used or "manual" if a manually set classname is to be used. 
     */
    public void addToVectors(String filename, String className, boolean recurseSubDirs, String substringFileFilter, String excludeFileFilter, String getNamesFrom)
    {
        this.names.add(filename);
        this.classNames.add(className);
        this.recurseSubDirs.add(new Boolean(recurseSubDirs));
        this.substringFileFilters.add(substringFileFilter);
        this.excludeFileFilters.add(excludeFileFilter);
        this.getNameFrom.add(getNamesFrom);
    }
    
    /**
     * Removes a full set of file input data (one record) from  the storage vectors.
     * @param i The index of the record to remove.
     */
    public void removeFromVectors(int i)
    {
        this.names.remove(i);			//Strings
        this.classNames.remove(i);		//Strings (either from classNames field or from dir names)
        this.recurseSubDirs.remove(i);		//Boolean objects
        this.substringFileFilters.remove(i); 	//Strings
        this.excludeFileFilters.remove(i); 	//Strings
        this.getNameFrom.remove(i);
    }
    
    /**
     * Write the current settings to an ascii property file
     * @param fileName The filename to write the file to
     * @throws java.io.IOException Thrown if an IO error occurs while writing to file, indicates file writing was 
     * unsuccessful.
     */
    public void writePropertiesFile(String fileName) throws IOException
    {
        BufferedWriter output = null;
        String contents;
        try {
            //use buffering
            output = new BufferedWriter( new FileWriter(fileName) );
            for (int i = 0; i < names.size(); i++) {
                String fPath = "";
                String cName = "";
                String recsd = "";
                String ssff = "";
                String exff = "";
                String gnf = "";
                String classOpt = "";
                String recOpt = "";
                String ssfOpt = "";
                String exfOpt = "";

                fPath = (String)names.elementAt(i);
                if ( ((Boolean)recurseSubDirs.elementAt(i)).booleanValue() ) {
                    recOpt = "-r";
                    recOpt += "\t";
                }
                if( ((String)getNameFrom.elementAt(i)).compareTo("manual") == 0 ) {
                    cName = (String)classNames.elementAt(i);
                    classOpt = "-m ";
                }
                else if( ((String)getNameFrom.elementAt(i)).compareTo("toplevelDir") == 0 ) {
                    classOpt = "-tl";
                }
                else {
                    classOpt = "-d";
                }
                if ( ((String)substringFileFilters.elementAt(i)).trim().length() != 0 ) {                 
                    ssfOpt = "-sf ";
                    ssff = (String)substringFileFilters.elementAt(i);
                    ssff += "\t";
                }
                if ( ((String)excludeFileFilters.elementAt(i)).trim().length() != 0 ) {                 
                    exfOpt = "-ef ";
                    exff = (String)excludeFileFilters.elementAt(i);
                    exff += "\t";
                }
                contents = fPath;
                contents += "\t";
                contents += recOpt;
                contents += ssfOpt;
                contents += ssff;
                contents += exfOpt;
                contents += exff;
                contents += classOpt;
                contents += cName;                                
                output.write( contents );
                output.newLine();
            }
            
        } finally {
            //flush and close both "output" and its underlying FileWriter
            if (output != null) 
                output.close();
        }
        
    }
    
    /**
     * Read input file parameter settings from a file.
     * @param pFile The file to read the settings from
     */
    public void readParameterFile(File pFile)
    {
        BufferedReader textBuffer = null;
        String[] dataLine = {"init"}; 

        try 
        {
            //use buffering
            //this implementation reads one line at a time
            textBuffer = new BufferedReader( new FileReader(pFile) );
            String line = null; //not declared within while loop
            int fileLineNumber = 0;
            while (( line = textBuffer.readLine()) != null) {
                dataLine = line.split("\t");
                fileLineNumber++;
                String fname = "";
                String cname = "";
                boolean recsd = false;
                String ssff = "";
                String exff = "";
                String gnf = "dir";

                fname = dataLine[0].trim();
                if (dataLine.length > 1) 
                {
                    for (int i = 1; i < dataLine.length; i++) 
                    {
                        if (((dataLine[i].trim()).toLowerCase()).startsWith("-r")) {
                            recsd = true;
                        }
                        else if (((dataLine[i].trim()).toLowerCase()).startsWith("-m")) {
                            gnf = "manual";
                            cname = (dataLine[i].substring(2)).trim();
                        }
                        else if (((dataLine[i].trim()).toLowerCase()).startsWith("-tl")) {
                            gnf = "toplevelDir";
                        }
                        else if (((dataLine[i].trim()).toLowerCase()).startsWith("-d")) {
                            gnf = "dir";
                        }
                        else if (((dataLine[i].trim()).toLowerCase()).startsWith("-sf")) {
                            ssff = (dataLine[i].substring(3)).trim();
                        }
                        else if (((dataLine[i].trim()).toLowerCase()).startsWith("-ef")) {
                            exff = (dataLine[i].substring(3)).trim();
                        }
                        else {
                            System.out.println("An unrecognized specification occurred " +
                                    "in the property file at line: " + fileLineNumber + 
                                    " of the the input file.");
                        }
                    }                         

                }
                addToVectors(fname, cname, recsd, ssff, exff, gnf);                                
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (textBuffer!= null) {
                    //flush and close both "input" and its underlying FileReader
                    textBuffer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Uses names, classNames and recurseSubDirs, getNameFrom, substringFileFilter and
     * excludeFileFilter Vectors to produce the full list of files to be output.
     * @return A File[] containing the full file list to be output
     * @throws java.io.IOException Thrown if an error occurs while calculating 
     * the canonical form of a pathname.
     */
    public Signal[] produceSignalList() throws IOException
    {
        Signal[] out = null;
        Vector outSignals = new Vector();
        for (int i=0;i<names.size();i++)
        {
            File f = new File((String)names.get(i));
            if (f.isDirectory() == true)
            {
                FileFilter filter = null;
                if ((((String)substringFileFilters.get(i)).compareTo("") != 0)&&(((String)excludeFileFilters.get(i)).compareTo("") != 0))
                {
                    filter = new DoubleSubstringFileFilter((String)substringFileFilters.get(i),(String)excludeFileFilters.get(i));
                }
                else if(((String)substringFileFilters.get(i)).compareTo("") != 0)
                {
                    filter = new SubstringFileFilter((String)substringFileFilters.get(i));
                }
                else if(((String)excludeFileFilters.get(i)).compareTo("") != 0)
                {
                    filter = new ExcludeSubstringFileFilter((String)excludeFileFilters.get(i));
                }
                
                if (((Boolean)recurseSubDirs.get(i)).booleanValue() == true)
                {
                    Vector dirsToDo = new Vector();
                    dirsToDo.add(f);
                    Vector toplevelDirsToDo = null;
                    while(dirsToDo.size() > 0)
                    {
                        File [] files = ((File)dirsToDo.get(0)).listFiles();
                        File [] filteredFiles = ((File)dirsToDo.get(0)).listFiles(filter);
                        dirsToDo.remove(0);
                        if(((String)getNameFrom.get(i)).compareTo("toplevelDir") == 0)
                        {
                            if (toplevelDirsToDo == null)
                            {
                                toplevelDirsToDo = new Vector();
                                for (int j=0;j<files.length;j++)
                                {
                                    if (files[j].isDirectory() == true)
                                    {	//store dirName
                                        toplevelDirsToDo.add(files[j].getName());
                                        dirsToDo.add(files[j]);
                                        System.out.println("Adding " + files[j].getName() + " class, dir: " + files[j].getCanonicalPath());                                        
                                    }
                                    else  //ignore
                                    {
                                    }
                                }
                            }
                            else
                            {
                                for (int j=0;j<files.length;j++)
                                {
                                    if (files[j].isDirectory() == true)
                                    {	dirsToDo.add(files[j]);
                                        toplevelDirsToDo.add(toplevelDirsToDo.get(0));
                                    }
                                    
                                }
                                for (int j=0;j<filteredFiles.length;j++)
                                {
                                    if (filteredFiles[j].isDirectory() == false)
                                    {
                                        Signal tmp = new Signal(filteredFiles[j].getCanonicalPath(),(String)toplevelDirsToDo.get(0));
                                        outSignals.add(tmp);
                                    }
                                }
                                toplevelDirsToDo.remove(0);
                            }
                            
                        }
                        else
                        {
                            for (int j=0;j<filteredFiles.length;j++)
                            {
                                if (filteredFiles[j].isDirectory() == false)
                                {
                                    if (((String)getNameFrom.get(i)).compareTo("dir") == 0)
                                    {	
                                        Signal tmp = new Signal(filteredFiles[j].getCanonicalPath(),f.getName());
                                        outSignals.add(tmp);
                                    }
                                    else if(((String)getNameFrom.get(i)).compareTo("manual") == 0)
                                    {
                                        Signal tmp = null;
                                        if (!((String)classNames.get(i)).equals(""))
                                        {
                                            tmp = new Signal(filteredFiles[j].getCanonicalPath(),(String)classNames.get(i));
                                        }else{
                                            tmp = new Signal(filteredFiles[j].getCanonicalPath());
                                        }
                                        outSignals.add(tmp);
                                    }
                                    else
                                    {
                                        //Can't do this
                                        throw new RuntimeException(getNameFrom.get(i) + " is illegal here!");
                                    }
                                    
                                }
                            }
                            for (int j=0;j<files.length;j++)
                            {
                                if (files[j].isDirectory() == true)
                                {
                                    dirsToDo.add(files[j]);
                                }
                            }
                            //dirsToDo.remove(0);
                        }
                    }
                    
                }
                else
                {
                    File [] files = f.listFiles(filter);
                    for (int j=0;j<files.length;j++)
                    {
                        if (files[j].isDirectory() == false)
                        {
                            if (((String)getNameFrom.get(i)).compareTo("dir") == 0)
                            {	Signal tmp = new Signal(files[j].getCanonicalPath(),f.getName());
                                outSignals.add(tmp);
                            }
                            else if(((String)getNameFrom.get(i)).compareTo("toplevelDir") == 0)
                            {
                                //Can't do this
                                throw new RuntimeException("Can't create class names from top-level directories if not recursing subdirectories");
                            }
                            else if(((String)getNameFrom.get(i)).compareTo("manual") == 0)
                            {
                                Signal tmp = null;
                                if (!((String)classNames.get(i)).equals(""))
                                {
                                    tmp = new Signal(files[j].getCanonicalPath(),(String)classNames.get(i));
                                }else{
                                    tmp = new Signal(files[j].getCanonicalPath());
                                }
                                outSignals.add(tmp);
                            }
                            
                        }
                    }
                }
            }
            else
            {
                if (!((String)classNames.get(i)).equals(""))
                {
                    outSignals.add(new Signal(f.getCanonicalPath(), (String)classNames.get(i)));
                }else{
                    outSignals.add(new Signal(f.getCanonicalPath()));
                }
            }
        }
        
        out = new Signal[outSignals.size()];
        Signal[] sortOut = new Signal[outSignals.size()];
        
        for (int i=0;i<outSignals.size(); i++)
        {
            out[i] = (Signal)outSignals.get(i);
        }
        int count = 0;
        try
        {
            while (count < outSignals.size())
            {
                Signal start = null;
                String className = "";
                for (int i=0;i<outSignals.size();i++)
                {
                    if ((out[i] != null)&&(start == null))
                    {
                        start = out[i];
                        className = start.getStringMetadata(Signal.PROP_CLASS);
                        System.out.println("Preparing " + className + " class for output.");
                        sortOut[count] = out[i];
                        count++;
                        out[i] = null;
                    }
                    else if ((out[i] != null)&&(out[i].getStringMetadata(Signal.PROP_CLASS).equals(className)))
                    {
                        sortOut[count] = out[i];
                        count++;
                        out[i] = null;
                    }
                }
            }
        }
        catch (noMetadataException e)
        {
            System.out.println("InputSignals: Warning, unable to sort into classes as class metadata was not present!\n" + e);
            return out;
        }
        out = sortOut;
        return out;
    }
}
