/*
 * AudioReaderFactory.java
 *
 * Created on July 11, 2005, 5:33 PM
 *
 * A Factory  for creating audio readers.
 */
package org.imirsel.m2k.io.file;

import java.io.IOException;
import java.io.File;

/**
 * A factory for creating audio readers
 * @author Paul Lamere
 */
public class AudioReaderFactory {
    /**
     * Creates an AudioReader for the given file.
     *
     * @param file the file that will be read by the reader
     * @param downMixToMono if true, the reader will down mix all channels
     * to a single channe.
     * @return the audio reader to use to read the file or null 
     * if none could be found
     * @throws IOException if an IO error occurs
     */
    public static AudioReader createAudioReader(File file, boolean downMixToMono) throws IOException {
        if (file.getAbsolutePath().toLowerCase().endsWith(".mp3")) {
            return new MP3AudioReader(file, downMixToMono);
        } else if (file.getAbsolutePath().toLowerCase().endsWith(".mp2")) {
            return new MP3AudioReader(file, downMixToMono);
        } else if (file.getAbsolutePath().toLowerCase().endsWith(".mp1")) {
            return new MP3AudioReader(file, downMixToMono);
        } else if (file.getAbsolutePath().toLowerCase().endsWith(".wav")) {
            return new WavAudioReader(file, downMixToMono);
        } else {
            return null;
        }
    }

    /**
     * A simple test routine, dumps out the audio data
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: AudioReaderFactory audioFileName");
            System.exit(1);
        }

        try {
            AudioReader reader = AudioReaderFactory.createAudioReader(new File(args[0]), true);
            double[][] data = null;

            while ((data = reader.getDataFrame()) != null) {
                // dumpData(data);
                simpleDumpData(data);
            }
        } catch (IOException ioe) {
            System.err.println("Trouble reading file " + ioe);
            System.exit(2);
        }
    }

    public static void dumpData(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.println("Channel " + i);
            for (int j = 0; j < data[i].length; j++) {
                System.out.print(data[i][j]);
                System.out.print(" ");
                if (j % 7 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    public static void simpleDumpData(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                System.out.println(data[i][j]);
            }
        }
    }
}


