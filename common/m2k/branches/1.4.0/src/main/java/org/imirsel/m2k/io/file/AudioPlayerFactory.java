/*
 * AudioPlayerFactory.java
 *
 * Created on July 11, 2005, 5:33 PM
 *
 * A Factory for creating audio players.
 */
package org.imirsel.m2k.io.file;

import java.io.IOException;
import java.io.File;

/**
 * A factory for creating audio players
 * @author Kris West
 */
public class AudioPlayerFactory {
    /**
     * Creates an AudioPlayer for the given file.
     *
     * @param file the file that will be read by the player
     * @return the audio player to use to read the file or null 
     * if none could be found
     * @throws IOException if an IO error occurs
     */
    public static AudioPlayer createAudioPlayer(File file){
        if (file.getAbsolutePath().toLowerCase().endsWith(".mp3")) {
            //System.out.println("Creating MP3 player.");
            return new MP3AudioPlayer();
        } else if (file.getAbsolutePath().toLowerCase().endsWith(".mp2")) {
            //System.out.println("Creating MP3 player.");
            return new MP3AudioPlayer();
        } else if (file.getAbsolutePath().toLowerCase().endsWith(".mp1")) {
            //System.out.println("Creating MP3 player.");
            return new MP3AudioPlayer();
        } else if (file.getAbsolutePath().toLowerCase().endsWith(".wav")) {
            //System.out.println("Creating WAV player.");
            return new WavAudioPlayer();
        } else {
            return null;
        }
    }
}