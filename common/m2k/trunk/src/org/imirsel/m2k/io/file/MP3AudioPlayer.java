
package org.imirsel.m2k.io.file;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
public class MP3AudioPlayer implements AudioPlayer {
    
    double frameLength;
    static AdvancedPlayer thePlayer;
    private boolean playing = false;
    
    public MP3AudioPlayer() {
        
    }
    
    public boolean isPlaying()
    {
        return playing;
    }
    
    public double getDuration(File mp3)
    {
        try{
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(mp3));
            Bitstream aStream = new Bitstream(stream);
            int length;
            try {
                length = stream.available();
            } catch (IOException ex) {
                throw new JavaLayerException("Unable to calcuate stream length!");
            }
            Header h = aStream.readFrame();
            int frames = h.max_number_of_frames(length);
            double dur = h.ms_per_frame();
            aStream.unreadFrame();
            return ((double)frames * dur) / 1000.0;
//        
//                   thePlayer = new AdvancedPlayer(new BufferedInputStream(new FileInputStream(mp3)));
//            return thePlayer.getDuration();
        }catch(FileNotFoundException fnfe){
            System.out.println("The file " + mp3.getPath() + " was not found!");
            fnfe.printStackTrace();
            return 0.0;
        }catch(JavaLayerException jle){
            System.out.println("An error occured during while calculating duration of: " + mp3.getPath());
            jle.printStackTrace();
            return 0.0;
        }
    }
    
    /**
     * Plays an MP3 file
     * @return true if the segment was succesfully played, or false otherwise.
     */
    public boolean play(File mp3)
    {
        if (isPlaying()) {
            thePlayer.stop();
        }
        
        try {
            thePlayer = new AdvancedPlayer(new BufferedInputStream(new FileInputStream(mp3)));
            
            new Thread()
            {
              public void run()
              {
                try
                {
                  thePlayer.play();
                }
                catch (Exception e)
                {
                  throw new RuntimeException(e.getMessage());
                }
              }
            }.start();
            playing = true;
            return true;
        }catch(FileNotFoundException fnfe){
            System.out.println("The file " + mp3.getPath() + " was not found!");
            fnfe.printStackTrace();
            return false;
        }catch(JavaLayerException jle){
            System.out.println("An error occured during decoding of: " + mp3.getPath());
            jle.printStackTrace();
            return false;
        }
    }
    
    /**
     * Plays a range of MPEG audio frames, between two time indices
     * @param start	The start time index
     * @param end	The end time index
     * @return true if the segment was succesfully played, or false otherwise.
     */
    public boolean play(File mp3, final double start, final double end) {
        if (isPlaying()) {
            thePlayer.stop();
        }
        try {
            Bitstream aBitstream = new Bitstream(new BufferedInputStream(new FileInputStream(mp3)));
            Header h = aBitstream.readFrame();
            if (h == null) return false;
            frameLength = h.ms_per_frame() / 1000.0;
            
            aBitstream.close();
            
            thePlayer = new AdvancedPlayer(new BufferedInputStream(new FileInputStream(mp3)));
            
            //double blockLength = (double)blockSize / (double)sampleRate;
            final int startFrame = (int)Math.round(start / this.frameLength);
            final int endFrame = (int)Math.round(end / this.frameLength);
            
            new Thread()
            {
              public void run()
              {
                try
                {
                  thePlayer.play(startFrame, endFrame);
                }
                catch (Exception e)
                {
                  throw new RuntimeException(e.getMessage());
                }
              }
            }.start();
            playing = true;
            return true;
        }catch(FileNotFoundException fnfe){
            System.out.println("The file " + mp3.getPath() + " was not found!");
            fnfe.printStackTrace();
            return false;
        }catch(JavaLayerException jle){
            System.out.println("An error occured during decoding of: " + mp3.getPath());
            jle.printStackTrace();
            return false;
        } 
    }
    
    public boolean returnFrames(File mp3, final double start, final double end, ArrayList theFrames)
    {
        try{
            Bitstream aBitstream = new Bitstream(new BufferedInputStream(new FileInputStream(mp3)));
            Header h = aBitstream.readFrame();
            if (h == null) {
                theFrames = null;
                return false;
            }
            frameLength = h.ms_per_frame() / 1000.0;
            aBitstream.close();

            thePlayer = new AdvancedPlayer(new BufferedInputStream(new FileInputStream(mp3)));
            
            //SampleBuffer firstFrame = this.thePlayer.returnFrame();
            //frameLength = ((double)firstFrame.getBufferLength()) / ((double)firstFrame.getSampleFrequency());
            System.out.println("mp3 frame length (s): " + frameLength);
            
            final int startFrame = (int)Math.round(start / this.frameLength);
            final int endFrame = (int)Math.round(end / this.frameLength);
            System.out.println("Start frame: " + startFrame + ", End frame: " + endFrame);
            final int frames = endFrame-startFrame;

            System.out.println("skipping " + startFrame + " frames.");
            boolean ret = true;
            int offset = startFrame;
            while (offset-- > 0 && ret) ret = thePlayer.skipFrame();
            if (ret == false) {
                System.out.println("End of file reached while seeking!");
                //theFrames = null;
                return false;
            }
            
            int numInvalid = 0;

            //ArrayList theFrames_ = new ArrayList();
            for(int i=0;i<frames;i++)
            {
                SampleBuffer aFrame;
                try {
                    aFrame = this.thePlayer.returnFrame();
                } catch (ArrayIndexOutOfBoundsException ai) {
                    System.out.println("Invalid frame found - skipping");
                    numInvalid++;
                    if (numInvalid > 60){
                        System.out.println("Killing read of " + mp3.getPath() + " as it appears to have been corrupted!");
                        break;
                    }
                    continue;
                }
                
                if (aFrame == null)
                {
                    System.out.println("End of file reached while returning frames!");
                    //theFrames = new SampleBuffer[theFrames_.size()];
                    //theFrames_.toArray(theFrames);

                    synchronized (this) {
                        thePlayer.close();
                    }
                    return false;
                }
                theFrames.add(aFrame);
            }
           // theFrames = new SampleBuffer[theFrames_.size()];
            //theFrames_.toArray(theFrames);

            synchronized (this) {
                thePlayer.close();
            }
            return true;
        }
        catch(FileNotFoundException fnfe){
            System.out.println("The file " + mp3.getPath() + " was not found!");
            fnfe.printStackTrace();
            //theFrames = null;
            return false;
        }catch(JavaLayerException jle){
            System.out.println("An error occured during decoding of: " + mp3.getPath());
            jle.printStackTrace();
            //theFrames = null;
            return false;
        }
    }
    
    public void stop()
    {
        if ((thePlayer != null))
        {
            System.out.println("Stop called");
            thePlayer.stop();
        }
    }
    
    /**
     * a hybrid of javazoom.jl.player.Player tweeked to include <code>play(startFrame, endFrame)</code>
     * hopefully this will be included in the api
 */
    class AdvancedPlayer
    {
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
	private int lastPosition = 0;
        private int length = 0;
	
        /**
	 * Creates a new <code>Player</code> instance.
	 */
	public AdvancedPlayer(InputStream stream) throws JavaLayerException
	{
		this(stream, null);
	}

	public AdvancedPlayer(InputStream stream, AudioDevice device) throws JavaLayerException
	{
		bitstream = new Bitstream(stream);
                try {
                    length = stream.available();
                } catch (IOException ex) {
                    throw new JavaLayerException("Unable to calcuate stream length!");
                }
		if (device!=null) audio = device;
		else audio = FactoryRegistry.systemRegistry().createAudioDevice();
		audio.open(decoder = new Decoder());
                
	}

	public void play() throws JavaLayerException
	{
		play(Integer.MAX_VALUE);
	}

	/**
	 * Plays a number of MPEG audio frames.
	 *
	 * @param frames	The number of frames to play.
	 * @return	true if the last frame was played, or false if there are
	 *			more frames.
	 */
	public boolean play(int frames) throws JavaLayerException
	{
		boolean ret = true;
                while (frames-- > 0 && ret)
		{
			ret = decodeFrame();
		}

//		if (!ret)
		{
			// last frame, ensure all data flushed to the audio device.
			AudioDevice out = audio;
			if (out != null)
			{
//				System.out.println(audio.getPosition());
				out.flush();
//				System.out.println(audio.getPosition());
				synchronized (this)
				{
					complete = (!closed);
					close();
				}
			}
		}
		return ret;
	}

	/**
	 * Cloases this player. Any audio currently playing is stopped
	 * immediately.
	 */
	public synchronized void close()
	{
		AudioDevice out = audio;
		if (out != null)
		{
			closed = true;
			audio = null;
			// this may fail, so ensure object state is set up before
			// calling this method.
			out.close();
			lastPosition = out.getPosition();
			try
			{
				bitstream.close();
			}
			catch (BitstreamException ex)
			{}
		}
	}

	/**
	 * Decodes a single frame.
	 *
	 * @return true if there are no more frames to decode, false otherwise.
	 */
	protected boolean decodeFrame() throws JavaLayerException
	{
		try
		{
			AudioDevice out = audio;
			if (out == null) return false;

			Header h = bitstream.readFrame();
			if (h == null) return false;

			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

			synchronized (this)
			{
				out = audio;
				if(out != null)
				{
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}
			}

			bitstream.closeFrame();
		}
		catch (RuntimeException ex)
		{
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		return true;
	}
        
        public double getDuration() throws JavaLayerException
        {
            Header h = bitstream.readFrame();
            int frames = h.max_number_of_frames(length);
            double dur = h.ms_per_frame();
            bitstream.unreadFrame();
            return ((double)frames * dur) / 1000.0;
        }
        
        /**
	 * Returns a single frame.
	 *
	 * @return the frame if successfully decoded, null otherwise.
	 */
	protected SampleBuffer returnFrame() throws JavaLayerException
	{
		try
		{
			Header h = bitstream.readFrame();
                        /*if (h.vbr())
                        {
                            throw new RuntimeException("VBR MP3s are not currently supported.");
                        }*/
			if (h == null) return null;

			// sample buffer set when decoder constructed
			SampleBuffer aBuffer = (SampleBuffer) decoder.decodeFrame(h, bitstream);
                        int channels = aBuffer.getChannelCount();
                        //copy buffer so it is not overwritten
                        SampleBuffer output = new SampleBuffer(aBuffer.getSampleFrequency(), channels);
                        int y = 0;
                        for (int i=0;i<aBuffer.getBuffer().length;i++)
                        {
                            output.append(y, aBuffer.getBuffer()[i]);
                            y++;
                            if (y == channels)
                            {
                                y = 0;
                            }
                        }
                        bitstream.closeFrame();
                        return output;
		}
		catch (RuntimeException ex)
		{
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
	}

	/**
	 * skips over a single frame
	 * @return false	if there are no more frames to decode, true otherwise.
	 */
	protected boolean skipFrame() throws JavaLayerException
	{
		Header h = bitstream.readFrame();
		if (h == null) return false;
		bitstream.closeFrame();
		return true;
	}

	/**
	 * Plays a range of MPEG audio frames
	 * @param start	The first frame to play
	 * @param end		The last frame to play
	 * @return true if the last frame was played, or false if there are more frames.
	 */
	public boolean play(final int start, final int end) throws JavaLayerException
	{
		boolean ret = true;
		int offset = start;
		while (offset-- > 0 && ret) ret = skipFrame();
		return play(end - start);
	}
        
        /**
	 * closes the player
	 */
	public void stop()
	{
		close();
	}
    }
}
