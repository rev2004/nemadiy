
package org.imirsel.m2k.io.file;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import javax.sound.sampled.AudioFormat;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.JavaSoundAudioDeviceFactory;
//import javazoom.jl.player.advanced.*;

/**
 * A Javazoom JLayer based MP3 AudioPlayer. Implementation based on
 * <code>javazoom.jl.player.advanced.AdvancedWAVPlayer</code>
 *
 * @author Kris West
 */
public class WavThreadedAudioPlayer implements ThreadedAudioPlayer {
    
    private RandomAccessFile randomAccessFile = null;
    private int dataLength;
    private int numSamples;
    private int samplesRead;
    private float scalingFactor;
    private int numChannels;
    private int sampleRate;
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
    private int position = 0;
    

    private boolean playing = false;
    boolean pause = false;
    Thread t = null;
    File audioFile = null;
    
    private int secondsToPlay = -1;
    private long startTime = -1L;
    
    public WavThreadedAudioPlayer(File audioFile_) {
        audioFile = audioFile_;
        closed = false;
        complete = false;
        position = 0;
    }
    
    public WavThreadedAudioPlayer(File audioFile_, int start_position) {
        audioFile = audioFile_;
        closed = false;
        complete = false;
        position = start_position;
    }
    
    public void init() throws Exception{
        init(-1);
    }
    
    public void init(int secondsToPlay_) throws FileNotFoundException, IOException{
        secondsToPlay = secondsToPlay_;
        
        if (randomAccessFile != null){
            try {
                randomAccessFile.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            randomAccessFile = null;
        }
        
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
        
        
        //skip to correct playback position
        int frames = getNumFrames();
        int count = 0;
        this.pause();
        boolean more = true;
        while ((count < frames)&&(count < position)&&more) {
            more = skipFrame();  
        }
        this.unpause();
        if (!more){
            complete = true;
            
            System.out.println("End of audio file reached while seeking (at frame " + position + "!");
        }else{
            complete = false;
            
        }
        
        JavaSoundAudioDeviceFactory Factory = new JavaSoundAudioDeviceFactory();
        
        try {
            audio = (JavaSoundAudioDevice)Factory.createAudioDevice();
            audio.close();
            audio.open(new AudioFormat((float)sampleRate, (bytesPerSample * 8), numChannels, true, false));
        } catch(JavaLayerException jle) {
            throw new RuntimeException("Unable to open sound device!",jle);
        }
        
        closed = false;
        System.out.println("Inited player with " + frames + " frames");
        t=new Thread(this);
        t.start();
    }
    
    public void restart() throws FileNotFoundException,IOException{
        
        if (randomAccessFile != null){
            try {
                randomAccessFile.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            randomAccessFile = null;
        }
        
            
            randomAccessFile = new RandomAccessFile(audioFile, "r");
            
            dataLength = (int) (randomAccessFile.length() - headerSize);
            
            // Read in *.wav header information
            byte[] header = new byte[44];
            int hdrSize = randomAccessFile.read(header);
        
        
        
        //skip to correct playback position
        int frames = getNumFrames();
        int count = 0;
        this.pause();
        boolean more = true;
        while ((count < frames)&&(count < position)&&more) {
            more = skipFrame();
        }
        this.unpause();
        if (!more){
            complete = true;
            
            System.out.println("End of audio file reached while seeking (at frame " + position + "!");
        }else{
            complete = false;
            
        }
        
        JavaSoundAudioDeviceFactory Factory = new JavaSoundAudioDeviceFactory();
        
        try {
            audio = (JavaSoundAudioDevice)Factory.createAudioDevice();
            audio.close();
            audio.open(new AudioFormat((float)sampleRate, (bytesPerSample * 8), numChannels, true, false));
        } catch(JavaLayerException jle) {
            throw new RuntimeException("Unable to open sound device!",jle);
        }
        
        closed = false;
        System.out.println("Inited player with " + frames + " frames");
        t=new Thread(this);
        t.start();
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
    
    public int getNumFrames() {
        return numSamples / desiredFrameSize;
    }
    
    public int getPosition(){
        return position;
    }
    
    public double getDuration() {
        return (double)this.numSamples / (double)this.sampleRate;
    }
    
    public boolean isPlaying(){
        return !closed && !complete;
    }
    
    public synchronized void pause()
    {
        pause = true;
    }
    
    public synchronized void unpause()
    {
        pause = false;
    }
    
    public boolean paused(){
        return pause;
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
            audio.close();
            audio = null;
            closed = true;
            randomAccessFile = null;
        }
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
    
    public synchronized boolean setPosition(int target) {
        int frames = getNumFrames();
        if (position < target){
            this.pause();
            boolean more = true;
            while ((position < frames)&&(position < target)&&more) {
                    more = skipFrame();
            }
            if (!more){
                complete = true;
                System.out.println("End of audio file reached while seeking (at frame " + position + "!");
            }
            this.unpause();
            return more;
        }else if(position > target){
            //pause run
            try {
                this.pause();
                restart();
                //skip to correct playback position
                boolean more = true;
                while ((position < frames)&&(position < target)&&more) {
                        more = skipFrame();
                }
                if (!more){
                    complete = true;
                    System.out.println("End of audio file reached while seeking (at frame " + position + "!");
                }
                this.unpause();
                return more;
                
            } catch (FileNotFoundException ex) {
                System.out.println("Seek failed, file not found: " + audioFile.getPath());
                ex.printStackTrace();
                return false;
            } catch (IOException ex) {
                System.out.println("Seek failed, IOException");
                ex.printStackTrace();
                return false;
            } 
        }
        return true;
    }

    public void run()
    {
        boolean ret = true;
        startTime = System.currentTimeMillis();
        while (ret) {
            if (closed || complete || ((this.secondsToPlay!= -1) && ((System.currentTimeMillis() - startTime) > this.secondsToPlay * 1000)))
            {
                break;
            }else{
                if (!pause){
                        ret = decodeFrame();
                }
                try {
                        t.sleep(0);
                } catch (InterruptedException e) {}
            }
        }

        // last frame, ensure all data flushed to the audio device.
        AudioDevice out = audio;
        if (out != null) {
            out.flush();
            synchronized (this) {
                close();
            }
        }
        
    }
}
