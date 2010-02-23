package org.imirsel.m2k.io.file;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;
import javazoom.jl.decoder.SampleBuffer;

/**
 * A hybrid of javazoom.jl.player.AudioPlayer tweeked to play WAV files.
 */
public class WavAudioPlayer implements AudioPlayer {
    private RandomAccessFile randomAccessFile;
    private int dataLength;
    private int numSamples;
    private int samplesRead;
    private float scalingFactor;
    private int numChannels;
    private int sampleRate;
    private boolean downMixToMono = false;
    private int headerSize = 44;
    private int bytesPerSample = 2;
    private boolean displayProperties = false;
    private static final int desiredFrameSize = 32;
    /** The AudioDevice the audio samples are written to. */
    private JavaSoundAudioDevice audio;
    /** Has the player been closed? */
    private boolean closed = false;
    /** Has the player played back all frames from the stream? */
    private boolean complete = false;
    private int lastPosition = 0;
    //SampleBuffer theBuffer;
    
    public boolean isPlaying() {
        return !closed;
    }
    
    public WavAudioPlayer(){
        
    }
    
    public boolean playFile(int frames) {
        //System.out.println("playFile() called");
        boolean ret = true;
        
        while (frames-- > 0 && ret) {
            ret = decodeFrame();
            //System.out.println("Frames to go: " + frames);
        }
        
        // last frame, ensure all data flushed to the audio device.
        AudioDevice out = audio;
        if (out != null) {
            out.flush();
            synchronized (this) {
                complete = (!closed);
                close();
            }
        }
        return ret;
    }
    
    /**
     * Plays a WAV file
     * @return true if the segment was succesfully played, or false otherwise.
     */
    public boolean play(File wav) {
        if (isPlaying()) {
            stop();
        }
        try{
            openFile(wav);
        } catch(FileNotFoundException fnfe){
            System.out.println("The file " + wav.getPath() + " was not found!");
            fnfe.printStackTrace();
            return false;
        }catch(IOException ioe) {
            System.out.println("An IOException occured while playing file " + wav.getPath() + "!");
            ioe.printStackTrace();
            return false;
        }
        JavaSoundAudioDeviceFactory Factory = new JavaSoundAudioDeviceFactory();
        
        try {
            audio = (JavaSoundAudioDevice)Factory.createAudioDevice();
            audio.close();
            audio.open(new AudioFormat((float)sampleRate, (bytesPerSample * 8), numChannels, true, false));
        } catch(JavaLayerException jle) {
            throw new RuntimeException("Unable to open sound device!",jle);
        }
        
        new Thread() {
            public void run() {
                try {
                    playFile(Integer.MAX_VALUE);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }.start();
        return true;
        
    }
    
    public boolean play(File wav, final double start, final double end) {
        if (isPlaying()) {
            stop();
        }
        try{
            openFile(wav);
        } catch(FileNotFoundException fnfe){
            System.out.println("The file " + wav.getPath() + " was not found!");
            fnfe.printStackTrace();
            return false;
        }catch(IOException ioe) {
            System.out.println("An IOException occured while playing file " + wav.getPath() + "!");
            ioe.printStackTrace();
            return false;
        }
        JavaSoundAudioDeviceFactory Factory = new JavaSoundAudioDeviceFactory();
        
        try {
            audio = (JavaSoundAudioDevice)Factory.createAudioDevice();
            audio.close();
            audio.open(new AudioFormat((float)sampleRate, (bytesPerSample * 8), numChannels, true, false));
        } catch(JavaLayerException jle) {
            throw new RuntimeException("Unable to open sound device!",jle);
        }
        
        
        //double blockLength = (double)blockSize / (double)sampleRate;
        final int startFrame;
        final int endFrame;
        if (start == 0.0){
            startFrame = 0;
        }else{
            startFrame = (int)Math.round(start / ((double)this.desiredFrameSize / (double)this.sampleRate));
        }
        if (end == Double.MAX_VALUE){
            endFrame = Integer.MAX_VALUE;
        }else{
            endFrame = (int)Math.round(end / ((double)this.desiredFrameSize / (double)this.sampleRate));
        }
        
        //System.out.println("skipping " + startFrame + " frames.");
        boolean ret = true;
        int offset = startFrame;
        while (offset-- > 0 && ret) ret = skipFrame();
        if (ret == false) {
            System.out.println("End of file reached while seeking!");
            return false;
        }
        
        new Thread() {
            public void run() {
                try {
                    playFile(endFrame - startFrame);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }.start();
        return true;
    }
    
    public boolean returnFrames(final int frames, ArrayList theFrames)
    {
        try{
            boolean ret = true;
            //ArrayList theFrames_ = new ArrayList();
            for(int i=0;i<frames;i++)
            {

                SampleBuffer aFrame = this.getDataFrame();
                if (aFrame == null)
                {
                    System.out.println("End of file reached while returning frames!");
                    //theFrames = new SampleBuffer[theFrames_.size()];
                    //theFrames_.toArray(theFrames);
                    return false;
                }
                theFrames.add(aFrame);
            }
            //theFrames = new SampleBuffer[theFrames_.size()];
            //theFrames_.toArray(theFrames);

            synchronized (this) {
                complete = (!closed);
                close();
            }
            return true;
        }
        catch (IOException ioe)
        {
            throw new RuntimeException ("IOException occured while reading frames!",  ioe);
        }
    }
    
    public boolean returnFrames(File wav, final double start, final double end, ArrayList theFrames) {
        if (isPlaying()) {
            stop();
        }
        try{
            openFile(wav);
        } catch(FileNotFoundException fnfe){
            System.out.println("The file " + wav.getPath() + " was not found!");
            fnfe.printStackTrace();
            //theFrames = null;
            return false;
        }catch(IOException ioe) {
            System.out.println("An IOException occured while returning frames from file " + wav.getPath() + "!");
            ioe.printStackTrace();
            //theFrames = null;
            return false;
        }
        
        final int startFrame;
        final int endFrame;
        if (start == 0.0){
            startFrame = 0;
        }else{
            startFrame = (int)Math.round(start / ((double)this.desiredFrameSize / (double)this.sampleRate));
        }
        if (end == Double.MAX_VALUE){
            endFrame = Integer.MAX_VALUE;
        }else{
            endFrame = (int)Math.round(end / ((double)this.desiredFrameSize / (double)this.sampleRate));
        }
        
        System.out.println("skipping " + startFrame + " frames.");
        boolean ret = true;
        int offset = startFrame;
        while (offset-- > 0 && ret) ret = skipFrame();
        if (ret == false) {
            System.out.println("End of file reached while seeking!");
            //theFrames = null;
            return false;
        }
        
        return returnFrames(endFrame - startFrame, theFrames);
    }
    
    
    public boolean skipFrame() {
        if (randomAccessFile == null) {
            return false;
        }
        int samplesRemaining = numSamples - samplesRead;
        
        if (samplesRemaining == 0) {
            synchronized (this) {
                complete = (!closed);
                close();
            }
            return false;
        }
        int samplesToRead = samplesRemaining < desiredFrameSize
                ? samplesRemaining : desiredFrameSize;
        
        try {
            int numRead = randomAccessFile.skipBytes(samplesToRead * bytesPerSample * numChannels);
        }catch(IOException ioe) {
            System.out.println("Unexpected end of file when seeking!");
        }
        return true;
    }
    
    /**
     * Decodes a single frame.
     *
     * @return true if there are no more frames to decode, false otherwise.
     */
    protected boolean decodeFrame() {
        try {
            AudioDevice out = audio;
            if (out == null) return false;
            
            SampleBuffer output = getDataFrame();
            
            if (output == null) {
                return false;
            }
            output.write_buffer(1);
            synchronized (this) {
                out = audio;
                if((out != null)&&(out.isOpen())) {
                    //System.out.println("writing buffer length " + output.getBufferLength());
                    out.write(output.getBuffer(), 0, output.getBufferLength());
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Exception decoding audio frame", ex);
        } catch (JavaLayerException jle) {
            throw new RuntimeException("Exception playing audio frame", jle);
        }
        return true;
    }
    
    /** Returns the next frame of data to be read from the file or <code>null</code>
     *  if the end of the file has been reached.
     *  @return Buffer containing next frame of data
     *  @throws IOException If an error occurs when reading the file.
     */
    public SampleBuffer getDataFrame() throws IOException {
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
        
        SampleBuffer theBuffer = new SampleBuffer(sampleRate, numChannels);
        
        float[] left = new float[desiredFrameSize];
        float[] right = null;
        
        if (numChannels == 2) {
            right = new float[desiredFrameSize];
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
                    left[i] = (float)((value1L - 128) /* / scalingFactor*/);
//System.out.println("left: " + left[i]);
                    if (numChannels == 2) {
                        int value1R = unsignedByte(buffer[index++]);
                        right[i] = (float)((value1R - 128)/* / scalingFactor*/);
                    }
                }
                theBuffer.appendSamples(0, left);
                if (numChannels == 2) {
                    theBuffer.appendSamples(1, right);
                }
                break;
            case 2:
                for (int i = 0; i < samplesToRead; i++) {
                    int value1L = unsignedByte(buffer[index++]);
                    int value2L = unsignedByte(buffer[index++]);
                    int signedValue = (short) ((value2L << 8) + value1L);
                    left[i] = (float)(signedValue /* / scalingFactor*/);
//System.out.println("left: " + left[i]);
                    if (numChannels == 2) {
                        int value1R = unsignedByte(buffer[index++]);
                        int value2R = unsignedByte(buffer[index++]);
                        signedValue = (short) ((value2R << 8) + value1R);
                        right[i] = (float)(signedValue /* / scalingFactor*/);
                    }
                }
                theBuffer.appendSamples(0, left);
                if (numChannels == 2) {
                    theBuffer.appendSamples(1, right);
                }
                break;
            case 3:
                for (int i = 0; i < samplesToRead; i++) {
                    int value1L = unsignedByte(buffer[index++]);
                    int value2L = unsignedByte(buffer[index++]);
                    int value3L = unsignedByte(buffer[index++]);
                    int signedValue = ((int) (((((value3L << 8) + value2L) << 8) + value1L)));
                    left[i] = (float)(signedValue /* / scalingFactor*/);
//System.out.println("left: " + left[i]);
                    if (numChannels == 2) {
                        int value1R = unsignedByte(buffer[index++]);
                        int value2R = unsignedByte(buffer[index++]);
                        int value3R = unsignedByte(buffer[index++]);
                        signedValue = ((int) (((((value3R << 8) + value2R) << 8) + value1R)));
                        right[i] = (float)(signedValue /* / scalingFactor*/);
                    }
                }
                theBuffer.appendSamples(0, left);
                if (numChannels == 2) {
                    theBuffer.appendSamples(0, right);
                }
                break;
        }
        /*System.out.print("buffer:");
        for(int i=0;i<theBuffer.getBufferLength();i++)
        {
            System.out.print(" " + theBuffer.getBuffer()[i]);
        }
        System.out.println("");
         */
        
        return theBuffer;
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
        //theBuffer = new SampleBuffer(sampleRate, numChannels);
    }
    
    public double getDuration(File wav) {
        try{
            openFile(wav);
        } catch(FileNotFoundException fnfe){
            System.out.println("The file " + wav.getPath() + " was not found!");
            fnfe.printStackTrace();
            return 0.0;
        }catch(IOException ioe) {
            System.out.println("An IOException occured while calcuating duration of file " + wav.getPath() + "!");
            ioe.printStackTrace();
            return 0.0;
        }
        double duration = (double)this.numSamples / (double)this.sampleRate;
        close();
        return duration;
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
            closed = true;
            randomAccessFile = null;
        }
    }
    
    public void stop() {
        close();
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