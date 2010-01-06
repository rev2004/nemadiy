/*
 *Methods to get the metadata information from 
 *audio files. Currently uses JID3 and can read
 *.mp3 tags.
 * @author mert bay
 */

package org.imirsel.m2k.io.file;
import java.io.*;
import org.blinkenlights.jid3.*;
import org.blinkenlights.jid3.v1.*;
import org.blinkenlights.jid3.v2.*;


public class AudioTagReader {
    protected String genre = null;
    protected String title = null;
    protected String artist = null;
    protected String album = null;
    /** Creates a new instance of AudioTagReader */
    public AudioTagReader(File file) {
         if (file.toString().endsWith(".mp3")) loadMP3Tag(file);
         else if (file.toString().endsWith(".wma")) loadWMATag(file); 
    }
    
    public void loadMP3Tag(File F){
        try{
             MediaFile oMediaFile = new MP3File(F);
             ID3V1Tag tag = oMediaFile.getID3V1Tag();
             genre = tag.getGenre().toString();
             artist = tag.getArtist();
             title = tag.getTitle();
             album = tag.getAlbum();
         }
         catch (ID3Exception e) {
             System.out.println("Exception has occured, can not read the ID3 tag of the file " +F.getName() + " :" + e);
         }        
    }
    
   /*
    *To be implemented...
    */
    public void loadWMATag(File F){
        
    }
    
     public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    
    
}
