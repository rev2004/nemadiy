package org.imirsel.m2k.io.file;

import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;
import javazoom.jl.decoder.SampleBuffer;

/**
 * A very simple Javasound based Javazoom JLayer SampleBuffer player.
 * @author Kris West
 */
public class SampleBufferPlayer {
    
    /** The AudioDevice the audio samples are written to. */
    private JavaSoundAudioDevice audio = null;
    private ArrayList buffers = null;
    private float currentSampleRate = 0;
    private int currentNumChannels = 0;
    private int currentBitsPerSample = 0;
    private boolean terminate = false;
    
    
    /** Has the player been closed? */
    private boolean closed = true;
    /** Has the player played back all frames from the stream? */
    //private boolean complete = false;
    
    /** Creates a new instance of SampleBufferPlayer */
    public SampleBufferPlayer() {
        buffers = new ArrayList();
    }
    
    public void play() {
        new Thread() {
            public void run() {
                try {
                    playBuffers();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }.start();
    }
    
    public void addBuffer(SampleBuffer sb){
        this.buffers.add(sb);
    }
    
    public void playBuffers() {
        terminate = false;
        closed = false;
        int bitsPerSample = 16;
        
        while((buffers.size() > 0)&&(!terminate)) {
            SampleBuffer currentBuffer = (SampleBuffer)buffers.remove(0);
            try {
                if ((audio == null)||(!audio.isOpen())) {
                    JavaSoundAudioDeviceFactory Factory = new JavaSoundAudioDeviceFactory();
                    audio = (JavaSoundAudioDevice)Factory.createAudioDevice();
                    
                }
                float sampleRate = currentBuffer.getSampleFrequency();
                int numChannels = currentBuffer.getChannelCount();
                if((sampleRate != this.currentSampleRate)||(numChannels != this.currentNumChannels)) {
                    audio.close();
                    audio.open(new AudioFormat(sampleRate, bitsPerSample, numChannels, true, false));
                    this.currentSampleRate = sampleRate;
                    this.currentNumChannels = numChannels;
                    this.currentBitsPerSample = bitsPerSample;
                    
                }
            } catch(JavaLayerException jle) {
                closed = true;
                throw new RuntimeException("Unable to open sound device!",jle);
            }
            try{
                currentBuffer.write_buffer(1);
                synchronized (this) {
                    audio.write(currentBuffer.getBuffer(), 0, currentBuffer.getBufferLength());
                }
            }catch(JavaLayerException jle) {
                closed = true;
                audio.close();
                throw new RuntimeException("Exception occured while playing buffers!", jle);
            }
        }
        
        closed = true;
        audio.flush();
        synchronized (this) {
            audio.close();
            terminate = false;
        }
    }
    
    public boolean isOpen()
    {
        return !closed;
    }
    
    public void stop()
    {
        this.terminate = true;
    }
}
