package org.imirsel.nema.webapp.webflow.validator;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipFile;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

import org.imirsel.nema.model.NemaZipFile;
import org.imirsel.nema.webapp.model.ExecutableFile;
import org.imirsel.nema.webapp.model.ExecutableFile.ExecutableType;

/**
 * 
 * @author shirk
 */
public class ExecutableFileValidator {

   public void validateUpload(ExecutableFile executableFile, 
         ValidationContext context) {
      MessageContext messages = context.getMessageContext();
      
      String fileName = executableFile.getFileName();
      String fileExtension = fileName.substring(fileName.length()-3);
      boolean isValid = true;
      
      if(executableFile.getType()==ExecutableType.JAVA) {
         if(!fileExtension.equalsIgnoreCase("jar") && 
               !fileExtension.equalsIgnoreCase("zip")) {
              isValid = false;
              messages.addMessage(
                    new MessageBuilder().error().source("file")
                         .defaultText("Invalid file type. For Java " +
                         		"executables, only JAVA or ZIP files are " +
                         		"allowed.").build());
         }
      } else {
         if(!fileExtension.equalsIgnoreCase("zip")) {
             isValid = false;
             messages.addMessage(
                   new MessageBuilder().error().source("file")
                        .defaultText("Invalid file type. Only ZIP files " +
                        		"are allowed.").build());
         }
      }

      String tempDir = System.getProperty("java.io.tmpdir");
      String fileSeparator = System.getProperty("file.separator");
      
      File tempExecutableFile = new File(tempDir + fileSeparator + 
            executableFile.getFile().getOriginalFilename()+ 
            "_" + UUID.randomUUID());
      ZipFile zipFile = null;
      NemaZipFile nemaZipFile = null;
      try {
         executableFile.getFile().transferTo(tempExecutableFile);
         zipFile = new ZipFile(tempExecutableFile);
         nemaZipFile = new NemaZipFile(zipFile);
         nemaZipFile.open();
      } catch (IllegalStateException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            if(nemaZipFile!=null) {
            nemaZipFile.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
         tempExecutableFile.delete();
      }
  }
  
  public boolean isNullOrEmpty(String str) {
     return str == null || str.trim().equals("");
  }
  
}
