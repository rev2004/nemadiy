//
package org.imirsel.m2k.io.file;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
//import javazoom.jl.player.advanced.*;

/**
 * A Javazoom JLayer based MP3 AudioPlayer. Implementation based on
 * <code>javazoom.jl.player.advanced.AdvancedWAVPlayer</code>
 *
 * @author Kris West
 */
public class MP3ThreadedAudioPlayer implements ThreadedAudioPlayer {
    
    /** The MPEG audio bitstream.*/
    private Bitstream bitstream;
    
    /** The MPEG audio decoder. */
    private Decoder decoder;
    /** The AudioDevice the audio samples are written to. */
    private AudioDevice audio;
    /** Has the player been closed? */
    private boolean closed = false;
    /** Has the player played back all frames from the stream? */
    private boolean complete = false;
    private int position = 0;
    private int frames = 0;
    private int streamLength = 0;
    private double frameLength = 0;
    private double duration = 0.0;
    private boolean playing = false;
    boolean pause = false;
    Thread t = null;
    File audioFile = null;
    
    private int secondsToPlay = -1;
    private long startTime = -1L;
    
    public MP3ThreadedAudioPlayer(File audioFile_) {
        audioFile = audioFile_;
        closed = false;
        complete = false;
        position = 0;
    }
    
    public MP3ThreadedAudioPlayer(File audioFile_, int start_position) {
        audioFile = audioFile_;
        closed = false;
        complete = false;
        position = start_position;
    }
    
    public void init() throws Exception{
        init(-1);
    }
    
    public void init(int secondsToPlay_) throws Exception{
        secondsToPlay = secondsToPlay_;
        
        InputStream in = new BufferedInputStream(new FileInputStream(audioFile));
        bitstream = new Bitstream(in);
        
        audio = FactoryRegistry.systemRegistry().createAudioDevice();
        audio.open(decoder = new Decoder());
        
        Header h = bitstream.readFrame();
        streamLength = in.available();
        frames = h.max_number_of_frames(streamLength);
        
        duration = h.total_ms(streamLength) / 1000.0;
        
        frameLength = h.ms_per_frame();
        bitstream.unreadFrame();
        
        //skip to correct playback position
        int count = 0;
        if (position > count){
            boolean more = true;
            while ((position < count)&&!more) {
                try {
                    h = bitstream.readFrame();
                    if (h == null){
                        complete = true;
                    }
                    bitstream.closeFrame();
                    count++;
                } catch (JavaLayerException ex) {}
            }
        }
        closed = false;
        complete = false;
        System.out.println("Inited player with " + frames + " frames");
        t=new Thread(this);
        t.start();
    }
    
    public int getNumFrames() {
        return frames;
    }
    
    public int getPosition(){
        return position;
    }
    
    public double getDuration() {
        return duration;
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
     * Closes this player. Any audio currently playing is stopped
     * immediately.
     */
    public synchronized void close() {
        AudioDevice out = audio;
        if (out != null) {
            try {
                bitstream.close();
            } catch (BitstreamException ex) {
            }
            closed = true;
            audio.close();
            audio = null;
            closed = true;
        }
    }
    
    /**
     * Closes this player. Any audio currently playing is stopped
     * immediately.
     */
    public synchronized void restart() throws FileNotFoundException, IOException, JavaLayerException{
        
        try {
            boolean isPaused = this.paused();
            if (bitstream != null)
            {
                bitstream.close();
            }
            if (audio != null)
            {
                audio.flush();
                audio.close();
                audio = FactoryRegistry.systemRegistry().createAudioDevice();
                audio.open(decoder = new Decoder());
            }else{
                audio = FactoryRegistry.systemRegistry().createAudioDevice();
                audio.open(decoder = new Decoder());
            }
            
            InputStream in = new BufferedInputStream(new FileInputStream(audioFile));
            streamLength = in.available();
            bitstream = new Bitstream(in);
            position = 0;

            Header h = bitstream.readFrame();
            frames =h.max_number_of_frames(streamLength);
            frameLength = h.ms_per_frame();
            duration = h.total_ms(streamLength);
            bitstream.unreadFrame();
            closed = false;
            complete = false;
            startTime = System.currentTimeMillis();
            if (t == null){
                System.out.println("Restart: Inited player with " + frames + " frames");
                t=new Thread(this);
                t.start();
            }

        } catch (BitstreamException ex) {
            System.out.println("Unable to restart stream, BitstreamException");
        }
        

    }
    
    /**
     * Decodes a single frame.
     *
     * @return true if there are no more frames to decode, false otherwise.
     */
    protected boolean decodeFrame() throws JavaLayerException {
        try {
            AudioDevice out = audio;
            if (out == null) return false;
            
            Header h = bitstream.readFrame();
            if (h == null){
                complete = true;
                return false;
            }
            
            // sample buffer set when decoder constructed
            SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);
            
            synchronized (this) {
                out = audio;
                if(out != null) {
                    out.write(output.getBuffer(), 0, output.getBufferLength());
                }
            }
            
            bitstream.closeFrame();
            position++;
        } catch (RuntimeException ex) {
            throw new JavaLayerException("Exception decoding audio frame", ex);
        }
        return true;
    }
    
    /**
     * skips over a single frame
     * @return false	if there are no more frames to decode, true otherwise.
     */
    protected boolean skipFrame() throws JavaLayerException {
        Header h = bitstream.readFrame();
        if (h == null){
            complete = true;
            return false;
        }
        bitstream.closeFrame();
        position++;
        return true;
    }
    
    public synchronized boolean setPosition(int target) {
        if (position < target){
            this.pause();
            boolean more = true;
            while ((position < frames)&&(position < target)&&more) {
                try {
                    more = skipFrame();
                } catch (JavaLayerException ex) {
                    System.out.println("JavaLayerException occured while skipping!");
                    //return false;
                }
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
                    try {
                        more = skipFrame();
                    } catch (JavaLayerException ex) {
                        System.out.println("JavaLayerException occured while skipping!");
                        return false;
                    }
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
            } catch (JavaLayerException ex){
                System.out.println("Seek failed, couldn't restart stream!");
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
                    try {
                        ret = decodeFrame();
                    } catch (JavaLayerException ex) {
                        System.out.println("An Exception occured during playback of " + this.audioFile.getPath() + "\nPlayback terminated!");
                        ex.printStackTrace();
                        break;
                    }
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
