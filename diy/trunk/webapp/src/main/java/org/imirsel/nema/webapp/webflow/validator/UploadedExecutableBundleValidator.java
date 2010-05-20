package org.imirsel.nema.webapp.webflow.validator;

import java.io.IOException;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

import org.imirsel.nema.model.ExecutableBundle.ExecutableType;
import org.imirsel.nema.webapp.model.UploadedExecutableBundle;

/**
 * 
 * @author shirk
 */
public class UploadedExecutableBundleValidator {

   public void validateUpload(UploadedExecutableBundle uploadedExecutable,
         ValidationContext context) {
      MessageContext messages = context.getMessageContext();

      String fileName = uploadedExecutable.getFileName();
      String fileExtension = fileName.substring(fileName.length() - 3);
      boolean failedValidation = false;

      if (uploadedExecutable.getType() == ExecutableType.JAVA) {
         if (!fileExtension.equalsIgnoreCase("jar")
               && !fileExtension.equalsIgnoreCase("zip")) {
            failedValidation = true;
            messages.addMessage(new MessageBuilder().error().source("file")
                  .defaultText(
                        "Invalid file type. For Java "
                              + "executables, only JAVA or ZIP files are "
                              + "allowed.").build());
         }
      } else {
         if (!fileExtension.equalsIgnoreCase("zip")) {
            failedValidation = true;
            messages.addMessage(new MessageBuilder().error().source("file")
                  .defaultText(
                        "Invalid file type. Only ZIP files are allowed.")
                  .build());
         }
      }

      if (!failedValidation) {
         if (!uploadedExecutable.passesIntegrityCheck()) {
            failedValidation = true;
            messages.addMessage(new MessageBuilder().error().source("file")
                  .defaultText("Uploaded file is corrupt or cannot be read.")
                  .build());
         }

         if (!failedValidation) {
            try {
               if (!uploadedExecutable.containsExecutable()) {
                  failedValidation = true;
                  String exeFileType;
                  if(uploadedExecutable.getType()==ExecutableType.JAVA) {
                     exeFileType = "main class";
                  } else {
                     exeFileType = "executable file";
                  }
                  messages.addMessage(new MessageBuilder().error().source(
                        "file").defaultText(
                        "The specified " + exeFileType + " was not found in the uploaded archive.")
                        .build());
               }
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      
      System.out.println("-------------> Passed validation?   " + !failedValidation);
   }

   public boolean isNullOrEmpty(String str) {
      return str == null || str.trim().equals("");
   }

}
