package org.imirsel.m2k.io.file;
import java.io.File;
import javazoom.jl.decoder.SampleBuffer;
import java.util.ArrayList;
/**
 * An interface defining the methods of an M2K audio player, based on Javazoom JLayer
 * @author Kris West
 */
public interface AudioPlayer {
    public boolean isPlaying();
    public boolean play(File mp3);
    public boolean play(File mp3, final double start, final double end);
    public void stop();
    public boolean returnFrames(File wav, final double start, final double end, ArrayList theFrames);
    /** Return file duration in seconds **/
    public double getDuration(File wav);
}
