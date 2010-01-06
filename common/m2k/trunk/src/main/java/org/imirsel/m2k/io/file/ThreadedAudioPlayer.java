package org.imirsel.m2k.io.file;
import java.io.File;
import java.util.ArrayList;
/**
 * An interface defining the methods of an M2K audio player, based on Javazoom JLayer
 * @author Kris West
 */
public interface ThreadedAudioPlayer extends Runnable{
    public void init() throws Exception;
    public void init(int secondsToPlay) throws Exception;
    public boolean isPlaying();
    public void close();
    public double getDuration();
    public int getNumFrames();
    public int getPosition();
    public boolean setPosition(int target);
    public void pause();
    public void unpause();
    public boolean paused();
}
