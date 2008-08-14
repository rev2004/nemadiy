/*
 * Methods to create a file filter with the given extensions
 * and checks if the given file has one of the extensions.
 */

package org.imirsel.m2k.io.file;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author mertbay
 */
public class DefaultFileFilter{
    private int s = 0;
    private String[] ext; 
    private boolean ok;
    /** Creates a new instance of DefaultFileFilter 
     *  with the given extensions.
     */
    
    
    public DefaultFileFilter(ArrayList extension){
        s = extension.size();
        ext = new String[s];
        for (int i=0; i<s; i++) ext[i]  = (String)(extension.get(i));
        
    }
   /**
    *Returns true if the file satisfies one of the extensions.
    * @param f the file to test against the filter.
    * @return a boolean indicating that the file should be accepted or dropped.
    */
    public boolean accept(File f){
        if (s==0){
                System.err.println("Not Initialized ");
        }
        ok = false;
       for (int i=0; i<s; i++){
           ok = ok || f.getName().endsWith(ext[i]);
       }
        return (ok);
    }
    
    public String getDescription(){
        String desc = null;
       for (int i=0; i<s; i++){
           desc = desc +  " " + ext[i];
       }
        return desc;
    }
    
    
}
