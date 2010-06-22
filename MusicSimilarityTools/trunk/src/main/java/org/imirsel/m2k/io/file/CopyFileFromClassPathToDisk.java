/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kris
 */
public class CopyFileFromClassPathToDisk {
    
    public static void copy(String classPath, File fileLocation){
        BufferedWriter writer = null;
        try {
//            ClassLoader loader = ClassLoader.getSystemClassLoader();
//            URL loc = loader.getResource(classPath);
//            InputStream iStream = new FileInputStream(loc);
//            //InputStream iStream = loader.getResourceAsStream(classPath);
            InputStream iStream = CopyFileFromClassPathToDisk.class.getResourceAsStream(classPath);
            if (iStream == null){
                 Logger.getLogger(CopyFileFromClassPathToDisk.class.getName()).log(Level.SEVERE, "Resource not found! Classpath: " + classPath);
            }
            InputStreamReader in = new InputStreamReader(iStream);
            BufferedReader reader = new BufferedReader(in);
            writer = new BufferedWriter(new FileWriter(fileLocation));

            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                writer.newLine();
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(CopyFileFromClassPathToDisk.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(CopyFileFromClassPathToDisk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
