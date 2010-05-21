package org.imirsel.nema.webapp.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.NemaZipFile;
import org.imirsel.nema.model.Path;
import org.springframework.web.multipart.MultipartFile;

public class UploadedExecutableBundle extends ExecutableBundle {

   private static final Logger logger = 
      Logger.getLogger(UploadedExecutableBundle.class.getName());
   
   /**
    * Version of this class.
    */
   private static final long serialVersionUID = -8886083754671426825L;
   
   /** Raw file posted by the user. Kept around for only a short time. */
   private transient MultipartFile uploadedFile;
   /** Bytes from the uploaded multipart file that are written to disk. */
   private transient File persistedFile;
   /** The uploaded file readable in ZIP format. */ 
   private transient ZipFile zipFile;   
   /** 
    * A wrapper for the ZIP file that provides some extra services needed by the
    * DIY application.
    */
   private transient NemaZipFile nemaZipFile;
   
   private boolean isReadableAsZip = false;
   private List<Path> jarPaths;
   
   /** Nobody knows what this is for */
   private String group;
   
   public MultipartFile getUploadedFile() {
      return uploadedFile;
   }
   
   /**
    * Create a new instance.
    */
   public UploadedExecutableBundle() {
   }
   
   /**
    * Create a new instance based on the properties of the supplied 
    * {@link ExecutableBundle}.
    * 
    * @param bundle The bundle from which properties should be copied.
    */
   public UploadedExecutableBundle(ExecutableBundle bundle) {
      this.setBundleContent(bundle.getBundleContent());
      this.setCommandLineFlags(bundle.getCommandLineFlags());
      this.setEnvironmentVariables(bundle.getEnvironmentVariables());
      this.setExecutableName(bundle.getExecutableName());
      this.setFileName(bundle.getExecutableName());
      this.setId(bundle.getId());
      this.setPreferredOs(bundle.getPreferredOs());
      this.setType(bundle.getType());
   }
   
   /**
    * Set the uploaded file.
    * 
    * @param file File that was uploaded by a user.
    * @throws IOException if a error occurs while working with the uploaded
    * file bytes.
    */
   public void setUploadedFile(MultipartFile file) throws IOException {
      try {
         if(getFileName()!=null || "".equals(getFileName())) {
            dispose();
         }
         setFileName(file.getOriginalFilename());
         setBundleContent(file.getBytes());
         persistUploadedFile();
         createZip();
         if(isReadableAsZip) {
            createAndOpenNemaZipFile();
            createJarPaths();
         }
      } catch (IOException e) {
         throw new IOException("An error occured while retrieving the " +
               "bytes of the uploaded file.",e);
      }
   }
   
   /**
    * Return the various options of executable types the user may upload.
    * 
    * @return Array of possible executable types.
    */
   public ExecutableType[] getTypeOptions(){
      return new ExecutableType[]{ExecutableType.JAVA,ExecutableType.MATLAB,
            ExecutableType.C,ExecutableType.SHELL};
   }

   /**
    * It's a mystery.
    * 
    * @return String of characters.
    */
   public String getGroup() {
      return group;
   }

   /**
    * Don't know.
    * 
    * @param group Some characters.
    */
   public void setGroup(String group) {
      this.group = group;
   }
   
   /**
    * Validate the uploaded file contains the specified executable.
    * 
    * @return True if the user-specified executable is present in the uploaded
    * file.
    * @throws IOException if an error occurs while searching for the executable
    * in the uploaded file.
    */
   public boolean containsExecutable() throws IOException {
      boolean containsExecutable = false;
      if(super.getType()==ExecutableType.JAVA) {
         containsExecutable = nemaZipFile.containsClass(getExecutableName());
      } else {
         containsExecutable = nemaZipFile.containsFile(getExecutableName());
      }
      return containsExecutable;
   }
   
   /**
    * Creates a 
    * @throws IOException
    */
   private void createAndOpenNemaZipFile() throws IOException {
      nemaZipFile = new NemaZipFile(zipFile);
      nemaZipFile.open();
   }
   
   /**
    * Cleanup open files and delete the uploaded file from disk.
    */
   public void dispose() {
      try {
         if(nemaZipFile!=null) {
           nemaZipFile.close();
         }
         if(zipFile!=null) {
           zipFile.close();
         }
         if(persistedFile!=null) {
           persistedFile.delete();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   /**
    * Save the uploaded file to disk.
    * 
    * @throws IOException if an error occurs while saving the file.
    */
   private void persistUploadedFile() throws IOException {
      String tempDir = System.getProperty("java.io.tmpdir");
      String fileSeparator = System.getProperty("file.separator");
      
      persistedFile = new File(tempDir + fileSeparator + UUID.randomUUID() + "_" +
                                           getFileName() );
      FileOutputStream fos = null;
      
      try {
         fos = new FileOutputStream(persistedFile);
         fos.write(getBundleContent());
      } catch (IOException e) {
         throw new IOException("Uploaded file could not be saved to disk.",e);
      } finally {
         try {
            if(fos!=null) {
               fos.flush();
               fos.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
   
   /**
    * Create a ZIP file from the uploaded file.
    * 
    * @return True if the file is readable as a ZIP file.
    */
   private boolean createZip() {
      try {
         zipFile = new ZipFile(persistedFile);
         isReadableAsZip = true;
         return true;
      } catch (ZipException e) {
         logger.warn(e.getMessage());
      } catch (IOException e) {
         logger.warn(e.getMessage());
      }
      return false;
   }
   
   /**
    * Test if the uploaded file is readable in the ZIP format.
    * 
    * @return True if readable in the ZIP format.
    */
   public boolean isReadableAsZip() {
      return isReadableAsZip;
   }
   
   private void createJarPaths() {
      jarPaths = new ArrayList<Path>();
      List<String> unixJarPaths = nemaZipFile.getUnixJarPaths();
      for(String path:unixJarPaths) {
         jarPaths.add(new Path(path));
      }
   }
   
   public List<Path> getJarPaths() {
      return jarPaths;
   }
}
