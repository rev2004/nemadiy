package org.imirsel.nema.webapp.model;

import org.imirsel.nema.model.ExecutableBundle;
import org.springframework.web.multipart.MultipartFile;

public class DiyExecutableBundle extends ExecutableBundle {

   /**
    * Version of this class.
    */
   private static final long serialVersionUID = -8886083754671426825L;
   
   private MultipartFile file;
   
   public MultipartFile getFile() {
      return file;
   }
   
   public void setFile(MultipartFile file) {
      try {
         setFileName(file.getOriginalFilename());
         setBundleContent(file.getBytes());
      } catch (Exception e) {
         throw new RuntimeException("An error occured while retrieving the " +
               "bytes of the uploaded file.");
      }
   }
   
   public ExecutableType[] getTypeOptions(){
      return new ExecutableType[]{ExecutableType.JAVA,ExecutableType.MATLAB,ExecutableType.C,ExecutableType.SHELL};
   }

}
