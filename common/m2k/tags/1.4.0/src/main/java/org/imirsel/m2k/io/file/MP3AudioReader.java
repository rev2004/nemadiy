/*
 * MP3.java
 *
 * Created on July 1, 2005, 6:04 AM
 *
 * Copyright 2005 Sun Microsystems
 */

package org.imirsel.m2k.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 * Provides MP3 conversion support
 * 
 * @author plamere, edited by Kris West 03/10/05
 */
public class MP3AudioReader implements AudioReader  {
    private Decoder decoder;
    private int sampleRate = -1;
    private int numChannels = -1;
    private Bitstream bitstream;
    private boolean downMixToMono;
    private String filename = null;
    
    
    /**
     * Prepares to read MP3 data from the given file
     * @param file the file to read from
     * @param downMixToMono if true downmix to  mono
     *
     * @throws IOException if an error occurs while reading the file
     */
    public MP3AudioReader(File file, boolean downMixToMono) throws IOException {
        
        // find the sample rate and number of channels
        // we want to make sure that this data is always available,
        // even before the first call to 'getData', so we get the data
        // now.
        collectMp3Info(file);
        this.downMixToMono = downMixToMono;
        decoder = new Decoder();
        decoder.setOutputBuffer(new SignalBuffer(sampleRate, numChannels));
        bitstream = new Bitstream(new FileInputStream(file));
    }
    
    /** Sets the desired size of data frames that should be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @param frameSize the size of of data frames that should be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @return <code>true</code> if the frame size is successfully set.
     */
    public boolean setDesiredReadFrameSize(int frameSize) {
        return false;
    }
    
    /** Returns the desired size of of data frames that will be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @return the size of of data frames that will be returned from each call
     *  to <code>getDataFrame()</code>.
     */
    public int getDesiredReadFrameSize() {
        return -1;
    }
    
    /**
     * Collects the channel and frequency information from
     * the given MP3 file. Puts the info in the class variables
     * sampleRate and numChannels.
     *
     * @param file the file to check.
     * @throws IOException if an IO error occurs.
     */
    private void collectMp3Info(File file) throws IOException {
        try {
            filename = file.getPath();
            FileInputStream fis = new FileInputStream(file);
            Bitstream bs = new Bitstream(fis);
            Header header = bs.readFrame();
            if (header == null)
            {
                //try again
                bs.closeFrame();
                header = bs.readFrame();
                
                if(header == null)
                {
                    //can't read the file! guess settings
                    numChannels = 2;
                    sampleRate = 44100;
                    bs.close();
                    return;
                }
                
            }
            numChannels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
            sampleRate = header.frequency();
            bs.close();
        } catch (BitstreamException bse) {
            throw new IOException("Trouble reading bitstream for: " + filename + "\n" + bse);
        }
    }
    
    /**
     * Gets the next data buffer from the file
     * @return the next data buffer or null if there is no more data
     * to read.
     *
     * @throws IOException if an error occurs while reading the file
     */
    public double[][] getDataFrame() throws IOException {
        
        if (bitstream == null) {
            System.out.println("No bitstream for: " + filename);
            return null;
        }
        
        try {
            Header header = bitstream.readFrame();
            if (header != null) {
                SignalBuffer buffer = (SignalBuffer)decoder.decodeFrame(header, bitstream);
                double[][] data =  buffer.getBuffer(downMixToMono);
                bitstream.closeFrame();
                return data;
            }else {
                System.out.println("null header for: " + filename + ", retrying...");
                bitstream.closeFrame();
                try {
                    //try again
                    Thread.sleep(200);
                } catch (InterruptedException ex) {  }
                header = bitstream.readFrame();
                if (header != null) {
                    SignalBuffer buffer = (SignalBuffer)decoder.decodeFrame(header, bitstream);
                    double[][] data =  buffer.getBuffer(downMixToMono);
                    bitstream.closeFrame();
                    return data;
                } else {
                    bitstream.close();
                    bitstream = null;
                    System.out.println("null header for: " + filename);
                    return null;
                }
            }
        } catch (DecoderException de) {
            throw new IOException("Decoder exception for: " + filename + "\n" + de);
        } catch (BitstreamException be) {
            throw new IOException("Bitstream exception for: " + filename + "\n" + be);
        } 
    }
    
    /**
     * Returns the number of channels
     */
    public int getNumChannels() {
        return downMixToMono ? 1 : numChannels;
    }
    
    /**
     * Returns the sample rate
     *
     * @return the sample rate
     */
    
    public int getSampleRate() {
        return sampleRate;
    }
    
    /** CLoses the reader
     */
    public void close() {
        if (bitstream != null)
        {
            try {
                bitstream.close();
                bitstream = null;
            } catch (BitstreamException bse) {
                throw new RuntimeException("Trouble closing bitstream!",bse);
            }
        }
    }
    
    /** Plays the audio file back through a java sound object.
     *  @throws IOException If an error occurs when reading the file.
     */
    public boolean play() throws IOException
    {
        boolean played = false;
        
        return played;
    }
    
    /** Plays the segment between the start and end times through a java sound object.
     *  @throws IOException If an error occurs when reading the file.
     */
    public boolean play(double start, double end) throws IOException
    {
        boolean played = false;
        /** The AudioDevice the audio samples are written to. */
	AudioDevice audio;
	/** Has the player been closed? */
	boolean closed = false;
	/** Has the player played back all frames from the stream? */
	boolean complete = false;
	int lastPosition = 0;
	/** Listener for the playback process */
	PlaybackListener listener;
        int sampleRate = getSampleRate();
        
        
        return played;
    }
}


/**
 * The <code>SignalBuffer</code> class implements an output buffer
 * that provides storage for a fixed size block of samples.
 */
class SignalBuffer extends Obuffer {
    private double[][] 		buffer;
    private int[] 		bufferp;
    private int 	        channels;
    private int			frequency;
    
    /**
     * Constructs a signal buffer
     *
     * @param sampleFrequency the sample frequency
     * @param numberOfChannels the number of channels
     *
     */
    public SignalBuffer(int sampleFrequency, int numberOfChannels) {
        channels = numberOfChannels;
        buffer = new double[channels][];
        bufferp = new int[channels];
        frequency = sampleFrequency;
        
        for (int i = 0; i < channels; ++i)  {
            buffer[i] = new double[OBUFFERSIZE];
        }
    }
    
    /**
     * Returns the number of channels
     *
     * @return the number of channels
     */
    public int getChannelCount() {
        return this.channels;
    }
    
    /**
     * Returns the sample frequency
     *
     * @return the sample frequency
     */
    public int getSampleFrequency() {
        return this.frequency;
    }
    
    /**
     * Returns the data buffer
     *
     * @return a copy of the data buffer, of proper length
     */
    public double[][] getBuffer(boolean downMixToMono) {
        if (downMixToMono && channels > 1) {
            double[][] retbuffer = new double[1][];
            double downMix[] = new double[getBufferLength()];
            retbuffer[0] = downMix;
            Arrays.fill(downMix, 0.0);
            
            for (int i = 0; i < channels; i++) {
                for (int j = 0; j < downMix.length; j++) {
                    downMix[j] +=  buffer[i][j];
                }
            }
            
            for (int j = 0; j < downMix.length; j++) {
                downMix[j] /=  channels;
            }
            
            return retbuffer;
        } else {
            
            double[][] retbuffer = new double[channels][];
            
            for (int i = 0; i < channels; i++) {
                retbuffer[i] = new double[bufferp[i]];
                System.arraycopy(buffer[i], 0, retbuffer[i], 0, bufferp[i]);
            }
            return retbuffer;
        }
    }
    
    /**
     * returns the length of the data in the buffer
     */
    public int getBufferLength() {
        return bufferp[0];
    }
    
    /**
     * Appends a 16 bit sample to the given channel
     *
     * @param channel the channel
     * @param value the value to be added
     */
    public void append(int channel, short value) {
        append(channel, value / 32767.0);
    }
    
    
    /**
     * Appends the array of data to the given channel
     *
     * @param channel the channel
     * @param f the data to append
     */
    public void appendSamples(int channel, float[] f) {
        for (int i = 0; i < f.length; i++) {
            append(channel, (double) f[i] / 32767.0);
        }
    }
    
    /**
     * Appends a single float value to the channel
     *
     * @param channel the channel
     * @param value the value
     */
    void append(int channel, double value) {
        assert value >= -1.0 && value <= 1.0;
        buffer[channel][bufferp[channel]] = value;
        bufferp[channel]++;
    }
    
    
    /**
     * Write the samples to the file (Random Acces).
     * (not used)
     */
    public void write_buffer(int val) {
    }
    
    /**
     * Close the sample buffer
     */
    public void close() {
        
    }
    
    /**
     * reset the buffer
     */
    public void clear_buffer() {
        for (int i = 0; i < channels; ++i) {
            bufferp[i] = 0;
        }
    }
    
    /**
     *
     */
    public void set_stop_flag() {
    }
}
