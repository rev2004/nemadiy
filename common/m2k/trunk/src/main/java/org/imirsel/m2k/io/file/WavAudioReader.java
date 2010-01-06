/*
 * WavAudioReader.java
 *
 * Created on July 11, 2005, 5:33 PM
 *
 * An AudioReader for WAV files
 */
package org.imirsel.m2k.io.file;

import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * An Audio Reader for Wav Files
 * @author Paul Lamere
 */
public class WavAudioReader implements AudioReader {
    private RandomAccessFile randomAccessFile;
    private int dataLength;
    private int numSamples;
    private int samplesRead;
    private double scalingFactor;
    private int numChannels;
    private int sampleRate;
    private boolean downMixToMono = false;
    private int headerSize = 44;
    private int bytesPerSample = 2;
    private boolean displayProperties = false;
    private int desiredFrameSize = 1024;


    /** 
     * Creates the Wav file Reader
     * @param file the file to open
     * @param downMixToMono if true, down mix all channels to a single channel
     * @throws IOException if an IO error occurs
     */
    public WavAudioReader(File file, boolean downMixToMono) throws IOException {
        openFile(file);
        this.downMixToMono = downMixToMono;
    }

    /** Sets the size of of data frames that should be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @param frameSize the size of of data frames that should be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @return <code>true</code> if the frame size is successfully set.
     */
    public boolean setDesiredReadFrameSize(int frameSize) {
        desiredFrameSize = frameSize;
        return true;
    }
    
    /** Returns the size of of data frames that will be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @return the size of of data frames that will be returned from each call
     *  to <code>getDataFrame()</code>.
     */
    public int getDesiredReadFrameSize() {
        return desiredFrameSize;
    }
    
    /** Returns the number of channels in the audio file being read.
     *  @return the number of channels in the audio file being read.
     */
    public int getNumChannels() {
        return downMixToMono ? 1 : numChannels;
    }
    
    /** Returns the sample rate of the audio file being read.
     *  @return the sample rate of the audio file being read.
     */
    public int getSampleRate() {
        return sampleRate;
    }
    
    /** Returns the next frame of data to be read from the file or <code>null</code>
     *  if the end of the file has been reached. The array returned is indexed as 
     *  <code>double[numChannels][numSamples]</code>. The data is PCM normalized to
     * -1 to +1.
     *  @return the next frame of data
     *  @throws IOException If an error occurs when reading the file.
     */
    public double[][] getDataFrame() throws IOException {
        if (randomAccessFile == null) {
            return null;
        }

        int samplesRemaining = numSamples - samplesRead;

        if (samplesRemaining == 0) {
            close();
            return null;
        }

        int samplesToRead = samplesRemaining < desiredFrameSize 
                          ? samplesRemaining : desiredFrameSize;

        double[] left = new double[samplesToRead];
        double[] right = null;

        if (numChannels == 2) {
            right = new double[samplesToRead];
        }

        byte[] buffer = new byte[samplesToRead * bytesPerSample * numChannels];

        int numRead = randomAccessFile.read(buffer);

        if (numRead != buffer.length) {
            throw new IOException("Mismatch Wav Read expected " + buffer.length + " read " + numRead);
        }

        samplesRead += samplesToRead;
        
        int index = 0;
        switch (bytesPerSample) {
            case 1:
                for (int i = 0; i < samplesToRead; i++) {
                    int value1L = unsignedByte(buffer[index++]);
                    left[i] = (value1L - 128) / scalingFactor;
                    if (numChannels == 2) {
                        int value1R = unsignedByte(buffer[index++]);
                        right[i] = (value1R - 128) / scalingFactor;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < samplesToRead; i++) {
                    int value1L = unsignedByte(buffer[index++]);
                    int value2L = unsignedByte(buffer[index++]);
                    int signedValue = (short) ((value2L << 8) + value1L);
                    left[i] = signedValue / scalingFactor;
                    if (numChannels == 2) {
                        int value1R = unsignedByte(buffer[index++]);
                        int value2R = unsignedByte(buffer[index++]);
                        signedValue = (short) ((value2R << 8) + value1R);
                        right[i] = signedValue / scalingFactor;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < samplesToRead; i++) {
                    int value1L = unsignedByte(buffer[index++]);
                    int value2L = unsignedByte(buffer[index++]);
                    int value3L = unsignedByte(buffer[index++]);
                    // TODO: What's with this division by eight? Andreas will tell us.
                    int signedValue = ((int) (((((value3L << 8) + value2L) << 8) + value1L)));
                    left[i] = signedValue / scalingFactor;
                    if (numChannels == 2) {
                        int value1R = unsignedByte(buffer[index++]);
                        int value2R = unsignedByte(buffer[index++]);
                        int value3R = unsignedByte(buffer[index++]);
                        signedValue = ((int) (((((value3R << 8) + value2R) << 8) + value1R)));
                        right[i] = signedValue / scalingFactor;
                    }
                }
                break;
        }

        // now we have read all the data for this buffer, stuff it into
        // the double[][] format (down mixing as necessary).
        
        double[][] returnData = null;

        if (numChannels == 1) {
            returnData = new double[1][];
            returnData[0] = left;
        } else if (numChannels == 2 && !downMixToMono) {
            returnData = new double[2][];
            returnData[0] = left;
            returnData[1] = right;
        } else if (numChannels == 2 && downMixToMono) {
            returnData = new double[1][];
            returnData[0] = left;

            if (left.length != right.length) {
                throw new IOException("Can't down mix, mismatched channel sizes");
            }

            for (int i = 0; i < left.length; i++) {
                left[i] =  (left[i] + right[i]) / 2;
            }
        }  else {
            throw new IOException("Unsupported channel count in WavAudioReader");
        }
        return returnData;
    }

    /**
     * Opens the audio file
     *
     * @param audioFile the audio file to read
     * @throws IOException if an IO error occurs
     */
    private void openFile(File audioFile) throws IOException {
        randomAccessFile = new RandomAccessFile(audioFile, "r");

        dataLength = (int) (randomAccessFile.length() - headerSize);
        
        // Read in *.wav header information
        byte[] header = new byte[44];
        int hdrSize = randomAccessFile.read(header);
        
        if (displayProperties) {
            System.out.println("Path           : " + audioFile.getPath());
        }
        
        // The "RIFF" chunk descriptor from byte 0 through 3
        if (header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F') {
            if (displayProperties) {
                System.out.println("ChunkID        : RIFF");
            }
        } else {
            throw new IOException("Incorrect RIFF ChunkID! Not a .wav file");
        }
        
        // The "WAVE" format descriptor from byte 8 through 11
        if (header[8] == 'W' && header[9] == 'A' && header[10] == 'V' && header[11] == 'E') {
            if (displayProperties) {
                System.out.println("Format         : WAVE");
            }
        } else {
            throw new IOException("Incorrect WAVE format descriptor! Not a .wav file");
        }
        
        
        if (header[22] == 1)
            numChannels = 1;
        else
            numChannels = 2;
        if (header[34] == 8)
            bytesPerSample = 1;
        else if (header[34] == 16)
            bytesPerSample = 2;
        else
            bytesPerSample = 3;
        
        sampleRate = (int) ((unsignedByte(header[27]) << 24)
            + (unsignedByte(header[26]) << 16)
            + (unsignedByte(header[25]) << 8) + (unsignedByte(header[24])));
        
        
        if (displayProperties) {
            System.out.println("numChannels    : " + numChannels);
            System.out.println("Sample Rate    : " + sampleRate);
            System.out.println("Bits per Sample: " + header[34]);
        }
        
        // Read in *.wav sample data
        numSamples = dataLength / bytesPerSample / numChannels;
        scalingFactor = 1 << (bytesPerSample * 8 - 1);
        samplesRead = 0;
    }


    /**
     * Closes the random access file
     *
     */
    public void close() {
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException ioe) {
                // trouble closing .. nothing
                // really to do here
            }
            randomAccessFile = null;
        }
    }

    /**
     * Returns the unsigned byte
     *
     * @param value the value to convert
     * @return unsigned byte conversion
     */
    int unsignedByte(byte value) {
        if (value >= 0) {
            return value;
        }
        return 256 + value;
    }
}
