/*
 * AudioReader.java
 *
 * Created on July 11, 2005, 5:33 PM
 *
 * An interface defining the methods of classes to read in audio files in formats
 * such as MP3, OGG Vorbis and Wave.
 */
package org.imirsel.m2k.io.file;

import java.io.IOException;

/**
 * An interface defining the methods of classes to read in audio files in formats
 * such as MP3, OGG Vorbis and Wave.
 * @author Kris West
 */
public interface AudioReader {
    /** Sets the size of of data frames that should be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @param frameSize the size of of data frames that should be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @return <code>true</code> if the frame size is successfully set.
     */
    public boolean setDesiredReadFrameSize(int frameSize);
    
    /** Returns the size of of data frames that will be returned from each call
     *  to <code>getDataFrame()</code>.
     *  @return the size of of data frames that will be returned from each call
     *  to <code>getDataFrame()</code>.
     */
    public int getDesiredReadFrameSize();
    
    /** Returns the number of channels that will be returned by the reader. (Note
     *  that some readers may be configured to down mix multiple channels to a
     *  single channel, in which case, this method returns the value '1').
     *  @return the number of channels that will be returned.
     */
    public int getNumChannels();
    
    /** Returns the sample rate of the audio file being read.
     *  @return the sample rate of the audio file being read.
     */
    public int getSampleRate();
    
    /** Returns the next frame of data to be read from the file or <code>null</code>
     *  if the end of the file has been reached. The array returned is indexed as 
     *  <code>double[numChannels][numSamples]</code>. The data is PCM normalized to
     * -1 to +1.
     *  @return the next frame of data
     *  @throws IOException If an error occurs when reading the file.
     */
    public double[][] getDataFrame() throws IOException;
    
    /** Closes the reader
     */
    public void close();
    
}
