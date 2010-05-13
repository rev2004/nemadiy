package org.imirsel.nema.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * Models an executable archive submitted by a NEMA system user. NEMA executable
 * archives have two legal formats:
 * </p>
 * 
 * <ol>
 *   <li>ZIP files, which may contain JAR files and/or other resources.</li>
 *   <li>JAR files, which may only contain class resources (not other JAR 
 *   files or ZIP files).</li>
 * </ol>
 * 
 * <p>
 * When a user submits an executable archive to the NEMA system, this class 
 * helps validate that the contents of the archive match the executable
 * information specified by the user. The 
 * {@link NemaZipFile#containsFile(String)} method can be used
 * to check for the presence of any file type at any location in the archive.
 * The {@link NemaZipFile#containsClass(String)} method can be used to check
 * for the presence of a class file anywhere within the archive. For example,
 * if the archive includes 5 JAR files, the specified class will be searched
 * for within every JAR.
 * </p>
 * 
 * @author shirk
 */
public class NemaZipFile {

   private static final Logger logger = 
      Logger.getLogger(NemaZipFile.class.getName());
   
   private static final String FILE_CLOSED_ERR_MSG = "NemaZipFile has " +
         "already been closed. A closed file cannot be reopened.";
   private static final String FILE_NOT_OPEN_ERR_MSG = "NemaZipFile must be " +
         "opened before it can be used. Call open() after instantiation.";

   private enum FileState{NEW,OPENED,CLOSED}
   private enum FileType{ZIP,JAR};
   
   private String fileSeparator = System.getProperty("file.separator");
   private String unzipDir = System.getProperty("java.io.tmpdir");
   
   private ZipFile sourceZip;
   private String sourceZipName;
   private File sourceZipContentDir;
   
   private FileState state = FileState.NEW;
   private FileType type = FileType.ZIP;
   
   /**
    * Create a new {@code NemaZipFile} based on the specified ZIP file. The
    * {@link NemaZipFile#open()} method must be called immediately after this
    * instance is constructed.
    * 
    * @param zipFile ZIP file to base this file upon.
    */
   public NemaZipFile(ZipFile zipFile) {
      sourceZip = zipFile;
      int lastIdxOfFileSep = 
         zipFile.getName().lastIndexOf(fileSeparator);
      sourceZipName = 
         zipFile.getName().substring(lastIdxOfFileSep+1);
      String fileExtension = sourceZipName.substring(sourceZipName.length()-3);
      if(fileExtension.equalsIgnoreCase("jar")) {
         type = FileType.JAR;
      }
   }
   
   /**
    * Create a new {@code NemaZipFile} based on the specified ZIP file, using
    * the specified directory to unzip the contents. The
    * {@link NemaZipFile#open()} method must be called immediately after this
    * instance is constructed.
    * 
    * @param zipFile ZIP file to base this file upon.
    * @param unzipDir Directory where the specified ZIP file should be
    * expanded. By default, the system temp directory is used (java.io.tmpdir).
    * @throws IllegalArgumentException if the specified directory does not
    * exist, is a not a directory, cannot be read, or cannot be written to.
    * 
    */
   public NemaZipFile(ZipFile zipFile, String unzipDir) {
      this(zipFile);
      validateUnzipDir(unzipDir);
      this.unzipDir = unzipDir;
   }
   
   /**
    * Open this {@code NemaZipFile}. Must be called before other methods
    * can be used.
    * 
    * @throws IOException if a problem occurs while opening the file.
    */
   public void open() throws IOException {
      if (state == FileState.CLOSED) {
         throw new IllegalStateException(FILE_CLOSED_ERR_MSG);
      }
      
      makeTempDirForZipContents();
      copyZipToTempDir();
      
      // If this is a ZIP file, expand the contents because it may contain
      // JAR files that need to be inspected.
      if (type == FileType.ZIP) {
         logger.info("Expanding archive " + sourceZipName + " into "
               + unzipDir + ".");
         expandZipIntoTempDir();
      }
      
      state = FileState.OPENED;
   }

   /**
    * Close the ZIP file and release used resources. After calling this method,
    * this instance is no longer usable.
    * 
    * @throws IOException if a problem occurs while closing the file.
    */
   public void close() throws IOException {
      if(state==FileState.CLOSED) {
         return;
      }
      deleteDir(sourceZipContentDir);
      state=FileState.CLOSED;
   }
   
   /**
    * Test for the presence of the specified file within this ZIP.
    * The specified file must include the full path.
    *   
    * @param fileName The full path and name of the file to search for.
    * @return True if the file exists.
    */
   public boolean containsFile(String fileName) {
      validateOpened();
      return null != sourceZip.getEntry(fileName);
   }
   
   /**
    * Test for the presence of the specified class file within the JAR files
    * that are present in this ZIP. The class name must include all package 
    * names (be fully qualified). For example, org.moo.foo.FooBar.
    * 
    * @param className Fully qualified class name.
    * @return True if the class exists.
    */
   public boolean containsClass(String className) {
      validateOpened();
      boolean containsClass = false;
      String classFilePath = className.replace('.', '/') + ".class";
      // If this is a JAR file, no need to look in nested JARs, so just use
      // containsFile().
      if (type == FileType.JAR) {
         containsClass = containsFile(classFilePath);
      } else {
         // It's a ZIP file, so it can contain nested JAR files that we need to
         // check within for the class.
         try {
            List<JarFile> jarFiles = jarFiles();
            for (JarFile jarFile : jarFiles) {
               if (jarFile.getEntry(classFilePath) != null) {
                  containsClass = true;
                  break;
               }
            }
            closeJars(jarFiles);
         } catch (IOException e) {
            throw new RuntimeException("An error occured while searching for "
                  + "class " + className + " in file " + getSourceZipName()
                  + ".", e);
         }
      }
      return containsClass;
   }
   
   /**
    * Test for the presence of JAR files within this ZIP file.
    * 
    * @return True if one or more JAR files are present in the ZIP file.
    */
   public boolean containsJars() {
      validateOpened();
      return jarEntries().size() > 0;
   }
   
   /**
    * Return the name of source ZIP file without path information.
    * 
    * @return Name of the source ZIP file.
    */
   public String getSourceZipName() {
      validateOpened();
      return sourceZipName;
   }
   
   /**
    * Return the location of the unzipped archive. If the file is of type
    * JAR, the directory will exist, but the archive will not have been
    * expanded.
    * 
    * @return Location of the ZIP content directory.
    */
   public String getSourceZipContentDir() {
      return sourceZipContentDir.getAbsolutePath();
   }
   
   /**
    * Delete the specified directory and all of its contents.
    * 
    * @param dir The directory to delete.
    */
   private void deleteDir(File dir) {
      File[] files = dir.listFiles();
      for (int i = 0; i < files.length; i++) {
         if (files[i].isDirectory()) {
            deleteDir(files[i]);
         } else {
            files[i].delete();
         }
      }
      boolean success = dir.delete();
      if(!success) {
         logger.warning("Directory " + dir + " was not deleted.");
      }
   }
   
   /**
    * Close the list of JAR files.
    * 
    * @param jarFiles Files to close.
    * @throws IOException if an error occurs while closing the files.
    */
   private void closeJars(List<JarFile> jarFiles) throws IOException {
      for(JarFile file:jarFiles) {
         file.close();
      }
   }
   
   /**
    * Return entries in this ZIP file that are JAR files.
    * 
    * @return List of ZipEntries that are JARs.
    */
   private List<ZipEntry> jarEntries() {
      List<ZipEntry> jarEntries = new ArrayList<ZipEntry>();
      Enumeration<? extends ZipEntry> fileEnumeration = sourceZip.entries();
      while (fileEnumeration.hasMoreElements()) {
         ZipEntry entry = fileEnumeration.nextElement();
         if (entry.getName().endsWith(".jar") || 
               entry.getName().endsWith(".JAR")) {
            jarEntries.add(entry);
         }
      }
      return jarEntries;
   }
   
   /**
    * Return the JAR files contained in this archive.
    * 
    * @return List JAR files in this ZIP file.
    * @throws IOException if an error occurs while loading the JAR files.
    */
   private List<JarFile> jarFiles() throws IOException {
      List<JarFile> jarFiles = new ArrayList<JarFile>(8);
      for(ZipEntry jarEntry:jarEntries()) {
         String filePath = sourceZipContentDir.getPath()+
         fileSeparator+jarEntry.getName();
         jarFiles.add(new JarFile(filePath));
      }
      return jarFiles;
   }
   
   /**
    * Return jar file paths in this ZIP file that are JAR files.
    * 
    * @return List of String.
    */
   public List<String> getSourceJarPaths() {
      List<String> jarEntries = new ArrayList<String>();
      Enumeration<? extends ZipEntry> fileEnumeration = sourceZip.entries();
      while (fileEnumeration.hasMoreElements()) {
         ZipEntry entry = fileEnumeration.nextElement();
         if (entry.getName().endsWith(".jar") || 
               entry.getName().endsWith(".JAR")) {
            jarEntries.add(entry.getName());
         }
      }
      return jarEntries;
   }

   
   /**
    * Expand the source ZIP file contents into the temp directory.
    * 
    * @throws IOException if a problem occurs while expanding the file.
    */
   private void expandZipIntoTempDir() throws IOException {
      int buffSize = 1024;
      BufferedOutputStream outStream = null;
      BufferedInputStream inStream = null;
      Enumeration<? extends ZipEntry> e = sourceZip.entries();
      while (e.hasMoreElements()) {
         ZipEntry entry = e.nextElement();
         inStream = new BufferedInputStream(sourceZip.getInputStream(entry));
         int count;
         byte data[] = new byte[buffSize];
         String fileName = sourceZipContentDir + fileSeparator + entry.getName();
         File outFile = new File(fileName);
         if (entry.isDirectory()) {
            outFile.mkdirs();
         } else {
            FileOutputStream fos = new FileOutputStream(outFile);
            outStream = new BufferedOutputStream(fos, buffSize);
            while ((count = inStream.read(data, 0, buffSize)) != -1) {
               outStream.write(data, 0, count);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
         }
      }
   }
   
   /**
    * Create a temporary directory in which the source ZIP file will be 
    * expanded.
    */
   private void makeTempDirForZipContents() {
      sourceZipContentDir = new File(unzipDir + fileSeparator + 
            UUID.randomUUID().toString());
      sourceZipContentDir.mkdir();
   }
   
   /**
    * Copies the source ZIP/JAR file to the temporary directory.
    * 
    * @throws IOException if an error occurs while copying the ZIP file.
    */
   private void copyZipToTempDir() throws IOException {
      int buffSize = 1024;
      FileInputStream source = null;
      FileOutputStream dest = null;
      try {
         source = new FileInputStream(sourceZip.getName());
         dest = new FileOutputStream(sourceZipContentDir + fileSeparator
               + sourceZipName);
         byte[] buffer = new byte[buffSize];
         int bytesRead = 0;
         while ((bytesRead = source.read(buffer)) != -1) {
            dest.write(buffer, 0, bytesRead);
         }
      } finally {
         if (source != null) {
            source.close();
         }

         if (dest != null) {
            dest.close();
         }
      }
   }
   
   /**
    * Helper method to test if this file is opened. Throws an
    * IllegalStateException if the file is not opened.
    */
   private void validateOpened() {
      if(state!=FileState.OPENED) {
         throw new IllegalStateException(FILE_NOT_OPEN_ERR_MSG);
      }
   }

   /**
    * Helper method to test if the client supplied unzip directory is usable.
    * 
    * @param unzipDirPath Path to the directory to validate.
    */
   private void validateUnzipDir(String unzipDirPath) {
      File unzipDir = new File(unzipDirPath);
      
      if(!unzipDir.exists()) {
         throw new IllegalArgumentException("The specified directory " + 
               unzipDir + " does not exist.");
      }
      
      if(!unzipDir.isDirectory()) {
         throw new IllegalArgumentException("The specified directory " + 
               unzipDir + " is a actually a file.");
      }
      
      if(!unzipDir.canRead()) {
         throw new IllegalArgumentException("The specified directory " + 
               unzipDir + " cannot be read.");
      }
      
      if(!unzipDir.canWrite()) {
         throw new IllegalArgumentException("The specified directory " + 
               unzipDir + " cannot be written to.");
      }

   }
   


   
}
