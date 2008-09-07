package org.imirsel.m2k.io.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;


/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class AudioFileReadClass {
    
    /** File object representing file being read
     */
    private File theFile = null;
    
    /** Storage Vector for frames that have been read in.
     */
    private List retainedFrames = null;
    
    /** Read index to current frame.
     */
    private int bufferIndex = -1;
    
    /** The file reader object.
     */
    private AudioReader fileReader = null;
    
    
    
    /** The size of audio frames output, measured in samples.
     */
    private int outputFrameSize = 512;
    
    /** The number of overlapping samples in each output frame.
     */
    private int overlapSize = 256;
    
    /** Number of frames output so far
     */
    private int framesSoFar = 0;
    
    
    /** Number of channels in file being read.
     */
    private int numberOfChannels = -1;
    
    //--------------------------------------------
    //Property settings
    
    /** A flag indicating whether the audio should be downmixed to mono.
     */
    private boolean downmix = true;
    
    /** The length of audio frames output, measured in seconds. */
    private double outputFrameLength = (512.0 / 22050.0);
    
    /** The desired read frame size. This parameter can be used to adjust the number
     * of disk accesses per output frame.
     */
    private int readFrameSize = 10240;
    
    /** The percentage to overlap concurrent frames.
     */
    private double overlapPercent = 0.5;
    
    
    /** The maximum number of frames to output.
     */
    private int maxOutFrames = 10000;
    
    private int sampleRate = -1;
    
    /** Determines whether number of frames is limited
     */
    private boolean limitAudioFrames = false;
    
    private boolean verbose = true;
    
    /** Creates a new instance of AudioFileReadClass */
    public AudioFileReadClass() {
    }

    public boolean getDownmix() {
        return downmix;
    }

    public void setDownmix(boolean downmix) {
        this.downmix = downmix;
    }

    public int getReadFrameSize() {
        return readFrameSize;
    }

    public void setReadFrameSize(int readFrameSize) {
        this.readFrameSize = readFrameSize;
    }

    public double getOverlapPercent() {
        return overlapPercent;
    }

    public void setOverlapPercent(double overlapPercent) {
        this.overlapPercent = overlapPercent;
        //this.overlapSize = Math.round((float)this.outputFrameSize * (float)this.overlapPercent);
    }

    public int getMaxOutFrames() {
        return maxOutFrames;
    }

    public void setMaxOutFrames(int maxOutFrames) {
        this.maxOutFrames = maxOutFrames;
    }

    public boolean getLimitAudioFrames() {
        return limitAudioFrames;
    }

    public void setLimitAudioFrames(boolean limitAudioFrames) {
        this.limitAudioFrames = limitAudioFrames;
    }

    public double getOutputFrameLength() {
        return outputFrameLength;
    }

    public void setOutputFrameLength(double outputFrameLength) {
        this.outputFrameLength = outputFrameLength;
        //this.outputFrameSize = Math.round((float)this.outputFrameLength * (float)this.sampleRate);
        //this.overlapSize = Math.round((float)this.outputFrameSize * (float)this.overlapPercent);
    }
    
    public Signal initialise(Signal inSig) throws noMetadataException
    {
        if (fileReader != null){
            fileReader.close();
            fileReader = null;
            Thread.yield();
        }
        
        theFile = new File(inSig.getStringMetadata(Signal.PROP_FILE_LOCATION));
        if (verbose){
            System.out.println("Preparing " + theFile.getName() + " for output.");
        }
        try {
            fileReader = AudioReaderFactory.createAudioReader(theFile, this.downmix);
            numberOfChannels = fileReader.getNumChannels();
            //populate metadata
            this.sampleRate = fileReader.getSampleRate();
            this.outputFrameSize = Math.round((float)this.outputFrameLength * (float)this.sampleRate);
            this.overlapSize = Math.round((float)this.outputFrameSize * (float)this.overlapPercent);
            
            inSig.setMetadata(Signal.PROP_SAMPLE_RATE, new Integer(this.sampleRate));
            inSig.setMetadata(Signal.PROP_FRAME_SIZE, new Integer(this.outputFrameSize));
            inSig.setMetadata(Signal.PROP_OVERLAP_SIZE, new Integer(this.overlapSize));
            
            if (verbose){
                System.out.println("\tFile location:      " + theFile.getPath());
                System.out.println("\tNumber of channels: " + numberOfChannels);
                System.out.println("\t Sample rate:       " + this.sampleRate);
                System.out.println("\t Frame size:        " + this.outputFrameSize);
                System.out.println("\t Overlap size:      " + this.overlapSize);
                
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("An error IOException while initialising the file reader!",ioe);
        }
        framesSoFar = 0;
        bufferIndex = -1;
        retainedFrames = new ArrayList();
        //not all that useful at the mo, may optimize reading code using this later
        boolean gotFrameRate = fileReader.setDesiredReadFrameSize(this.readFrameSize);
        return inSig;
    }
    
    public double[][] getDataFrame()
    {   //stop if past limit
        if ((this.framesSoFar >= this.maxOutFrames)&&(limitAudioFrames == true))
        {
            if (verbose){
                System.out.println("Resetting reader - output limit exceeded. Read " + framesSoFar + " frames.");
            }
            fileReader.close();
            fileReader = null;
            numberOfChannels = -1;
            retainedFrames = null;
            bufferIndex = -1;
            theFile = null;
            framesSoFar = 0;
            sampleRate = -1;
            return null;
        }
        if (fileReader == null)
        {
            return null;
        }
        
        if (retainedFrames == null) {
            retainedFrames = new ArrayList();
        }
        int bufferedFrames = 0;
        for (int i=0;i<retainedFrames.size();i++) {
            bufferedFrames += ((double[][])retainedFrames.get(i))[0].length;
        }
        if (this.bufferIndex != -1) {
            bufferedFrames -= this.bufferIndex;
        }else{
            bufferIndex = 0;
        }

        //If we have insufficient data buffered, we must read from file
        int invalidFrames = 0;
        while((bufferedFrames < this.outputFrameSize)||((bufferedFrames >= this.outputFrameSize)&&(bufferedFrames < this.readFrameSize))) {
            try {
                double[][] aFrame = fileReader.getDataFrame();
                if (aFrame == null)//end of file reached
                {
                    if (bufferedFrames < this.outputFrameSize) {

                        if (verbose){
                            System.out.println("Resetting reader - end of file reached after " + this.framesSoFar + " frames.");
                        }
                        fileReader.close();
                        fileReader = null;
                        numberOfChannels = -1;
                        retainedFrames = null;
                        bufferIndex = -1;
                        theFile = null;
                        sampleRate = -1;
                        return null;
                    } else {
                        if (verbose){
                            System.out.println("Resetting reader and outputting buffered data - end of file reached after " + this.framesSoFar + " frames.");
                        }
                        fileReader.close();
                        fileReader = null;
                        sampleRate = -1;
                        break;
                    }
                } else {
                    retainedFrames.add(aFrame);
                    bufferedFrames += aFrame[0].length;
                }
            } catch(IOException ioe) {
                ioe.printStackTrace();
                throw new RuntimeException("An IOException occurred while trying to read from audio file.\nFileName:");
            } catch (ArrayIndexOutOfBoundsException ai) {
                System.out.println("Invalid frame found - skipping");
                invalidFrames++;
                if (invalidFrames > 25){
                    if (bufferedFrames < this.outputFrameSize){
                        if (verbose){
                            System.out.println("Resetting reader - too many invlaid frames after " + this.framesSoFar + " frames.");
                        }
                        fileReader.close();
                        fileReader = null;
                        numberOfChannels = -1;
                        retainedFrames = null;
                        bufferIndex = -1;
                        theFile = null;
                        sampleRate = -1;
                        return null;
                    }else{
                        if (verbose){
                            System.out.println("Resetting reader and outputting buffered data - too many invalid frames after " + this.framesSoFar + " frames.");
                        }
                        fileReader.close();
                        fileReader = null;
                        sampleRate = -1;
                        break;
                    }
                }
            }
        }

        double[] leftOutputFrame = new double[this.outputFrameSize];
        double[] rightOutputFrame = null;
        if (this.numberOfChannels > 1) {
            rightOutputFrame = new double[this.outputFrameSize];
        }
        int outputIndex = 0;

        int tempBufferIndex = this.bufferIndex;
        //Calculate new bufferIndex
        bufferIndex += this.outputFrameSize - this.overlapSize;
        int retainedFramesIndex = 0;

        while (outputIndex < this.outputFrameSize) {
            double[][] frame = ((double[][])this.retainedFrames.get(retainedFramesIndex));
            while((tempBufferIndex < frame[0].length)&&(outputIndex < this.outputFrameSize)) {
                leftOutputFrame[outputIndex] = frame[0][tempBufferIndex];
                if (this.numberOfChannels > 1) {
                    rightOutputFrame[outputIndex] = frame[1][tempBufferIndex];
                }
                outputIndex++;
                tempBufferIndex++;
            }

            if (tempBufferIndex >= frame[0].length) {
                if ((this.outputFrameSize - outputIndex) > this.overlapSize) {//we can throw frame away as it will not be needed again
                    bufferIndex -= frame[0].length;
                    this.retainedFrames.remove(0);
                    if (retainedFrames.size() != 0) {
                        tempBufferIndex = 0;
                    } else {
                        tempBufferIndex = -1;
                        bufferIndex = -1;
                    }
                } else {
                    retainedFramesIndex++;
                    tempBufferIndex = 0;
                }
            }//else we had enough frames
        }
        framesSoFar++;

        
        
        double[][] out;
        if (this.numberOfChannels > 1) {
            out = new double[2][];
            out[0] = leftOutputFrame;
            out[1] = rightOutputFrame;
        }else{
            out = new double[2][];
            out[0] = leftOutputFrame;
            out[1] = null;
        }
        return out;
    }

    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
