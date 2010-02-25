package org.imirsel.meandre.m2k.io.file;

import org.imirsel.m2k.io.file.AudioFileReadClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ComponentContextProperties;

/**
 * This module implements buffered audio file reading for a number of different
 * audio file formats. Formats are detected based on the file extension. The
 * data is framed and streamed out. Down mixing can also be performed.
 *
 * Note: This module should *not* be used in conjunction with either Enframe
 * or StereoToMono, as it provides the same services.
 *
 * @author Kris West
 * Modified by Lily Dong
 */
@Component(creator = "Kris West", description = "Overview: Takes a Signal object with file location metadata as input, " +
"reads in the corresponding audio file and streams out frames of data from that file." +
"Detailed Description: This module reads in audio files in a number of formats," +
"enframes the data and streams out the frames. Downmixing to mono can also be performed." +
"The frame size, overlap percentage and sample rate of the of the audio frames are appended to" +
"the metadata of the Signal object, which is \'passed through\' for later use. " +
"The \"Read Frame Size\" parameter can be used to control how often data is read from " +
"disk." +
"Currently supported audio file formats include MP3 (.mp3) and Wave (.wav)." +
"Note: This module should not be used in conjunction with StereoToMono or Enframe " +
"as it provides the same services.", name = "AudioFileRead", tags = "signal reader")
public class AudioFileRead implements ExecutableComponent {

    @ComponentInput(description = "A Signal object with \"fileLocation\" metadata, indicating the " +
    "location of the corresponding audio file.", name = "signal")
    final static String DATA_INPUT = "signal";
    @ComponentOutput(description = "Audio frames from the the left channel or from mono signals " +
    "(either mono files or stereo files with downmix set).", name = "left_frames")
    final static String DATA_OUTPUT_1 = "left_frames";
    @ComponentOutput(description = "Audio frames from the the right channel.(1-D double array)", name = "right_frames")
    final static String DATA_OUTPUT_2 = "right_frames";
    @ComponentOutput(description = "The Signal Object used to read the file, with \"frameSize\", \"overlapSize\" and \"sampleRate\" metadata set.", name = "signal")
    final static String DATA_OUTPUT_3 = "signal";
    @ComponentOutput(description = "The Sample rate of the file being read (often used by filtering modules).", name = "SampleRate")
    final static String DATA_OUTPUT_4 = "SampleRate";
//	@ComponentOutput(description="Count of the number of frames for this signal.",
//			name="numFrames")
//			final static String DATA_OUTPUT_5 = "numFrames";
    @ComponentProperty(defaultValue = "0.023219954648526078", description = "The length of audio frames to output, measured in seconds. This " +
    "will be multiplied by the samplerate of the audio file and " +
    "rounded to calculate the length in samples of the output " +
    "vectors. This ensures that constant time resolution can be " +
    "maintained at different sample rates. The default setting (0.023219...) " +
    "produces 1024 sample frames at 44.1kHz and 512 at 22.05kHz.", name = "outputFrameLength")
    final static String DATA_PROPERTY_1 = "outputFrameLength";
    @ComponentProperty(defaultValue = "0.5", description = "The percentage to overlap concurrent output frames by (0.0-1.0)", name = "overlapPercent")
    final static String DATA_PROPERTY_2 = "overlapPercent";
    @ComponentProperty(defaultValue = "true", description = "Should the audio be downmixed to mono? If true, the mono frames " +
    "will be output through the left channel.", name = "downmix")
    final static String DATA_PROPERTY_3 = "downmix";
    @ComponentProperty(defaultValue = "102400", description = "Controls frame size of data read in by this module. If possible, " +
    "this frame size will be used by the AudioReader responsible for " +
    "the file otherwise buffering will be used to achieve the correct " +
    "framesize. This parameter is used to control how often data is " +
    "read from disk.", name = "readFrameSize")
    final static String DATA_PROPERTY_4 = "readFrameSize";
    @ComponentProperty(defaultValue = "false", description = "Controls whether the number of frames that are output is limited or not.", name = "limitAudioFrames")
    final static String DATA_PROPERTY_5 = "limitAudioFrames";
    @ComponentProperty(defaultValue = "10000", description = "The maxium number of frames to output.", name = "maxOutFrames")
    final static String DATA_PROPERTY_6 = "maxOutFrames";
    private AudioFileReadClass reader = null;
    /** A flag indicating whether the audio should be downmixed to mono.
     */
    private boolean downmix = true;
    /** The length of audio frames output, measured in seconds. */
    private double outputFrameLength = (512.0 / 22050.0);
    /** The desired read frame size. This parameter can be used to adjust the number
     * of disk accesses per output frame.
     */
    private int readFrameSize = 1024;
    /** The percentage to overlap concurrent frames.
     */
    private double overlapPercent = 0.5;
    /** The maximum number of frames to output.
     */
    private int maxOutFrames = 10000;
    /** Determines whether number of frames is limited
     */
    private boolean limitAudioFrames = false;

    /**
     * Sets the value of readFrameSize.
     * @param value the value which readFrameSize is set to
     * @see #getReadFrameSize
     */
    public void setReadFrameSize(int value) {
        this.readFrameSize = value;
    }

    /**
     * Returns the value of readFrameSize.
     * @return readFrameSize
     * @see #setReadFrameSize
     */
    public int getReadFrameSize() {
        return this.readFrameSize;
    }

    /**
     * Sets the value of maxOutFrames.
     * @param value the value which maxOutFrames is set to
     * @see #getMaxOutFrames
     */
    public void setMaxOutFrames(int value) {
        this.maxOutFrames = value;
    }

    /**
     * Returns the value of maxOutFrames.
     * @return maxOutFrames
     * @see #setMaxOutFrames
     */
    public int getMaxOutFrames() {
        return this.maxOutFrames;
    }

    /**
     * Sets the flag indicating whether the audio should be downmixed to mono.
     * @param value the value which downmix is set to.
     * @see #getDownmix
     */
    public void setDownmix(boolean value) {
        this.downmix = value;
    }

    /**
     * Returns the flag indicating whether the audio should be downmixed to mono.
     * @return downmix the value of the downmix flag.
     * @see #setDownmix
     */
    public boolean getDownmix() {
        return this.downmix;
    }

    /**
     * Sets the limitAudioFrames flag.
     * @see #getLimitAudioFrames
     */
    public void setLimitAudioFrames(boolean value) {
        this.limitAudioFrames = value;
    }

    /**
     * Returns the limitAudioFrames flag.
     * @see #setLimitAudioFrames
     */
    public boolean getLimitAudioFrames() {
        return this.limitAudioFrames;
    }

    /**
     * Returns an array of strings containing the Java data types of the outputs.
     *
     * @return the fully qualified java types for each of the outputs.
     */
    public String[] getOutputTypes() {
        String[] types = {"[D", "[D", "org.imirsel.m2k.util.Signal", "java.lang.Integer"};
        return types;
    }

    /**
     * Performs operations at the beginning of execution.
     *
     * @see #dispose
     */
    public void initialize(ComponentContextProperties ccp) {
        reader = new AudioFileReadClass();
        reader.setDownmix(this.downmix);
        reader.setLimitAudioFrames(this.limitAudioFrames);
        reader.setMaxOutFrames(this.maxOutFrames);
        reader.setOutputFrameLength(this.outputFrameLength);
        reader.setOverlapPercent(this.overlapPercent);
        reader.setReadFrameSize(this.readFrameSize);
    }

    /**
     * Performs operations at the end of execution.
     *
     * @see #beginExecution
     */
    public void dispose(ComponentContextProperties ccp) {
        reader = null;
    }

    /**
     * This module implements buffered audio file reading for a number of different
     * audio file formats. Formats are detected based on the file extension. The
     * data is enframed and streamed out. Down mixing can also be performed. Data
     * is only read from the file as needed so that whole file need not be loaded
     * at once. The number of reads per output frames can be tuned with the
     * <code>readFrameSize</code> parameter.
     *
     * Note: This module should *not* be used in conjunction with either Enframe
     * or StereoToMono, as it provides the same services.
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {

        outputFrameLength = Double.valueOf(cc.getProperty(DATA_PROPERTY_1));
        overlapPercent = Double.valueOf(cc.getProperty(DATA_PROPERTY_2));
        downmix = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_3));
        readFrameSize = Integer.valueOf(cc.getProperty(DATA_PROPERTY_4));
        limitAudioFrames = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_5));
        maxOutFrames = Integer.valueOf(cc.getProperty(DATA_PROPERTY_6));

        reader.setDownmix(downmix); //reset parameters
        reader.setLimitAudioFrames(limitAudioFrames);
        reader.setMaxOutFrames(maxOutFrames);
        reader.setOutputFrameLength(outputFrameLength);
        reader.setOverlapPercent(overlapPercent);
        reader.setReadFrameSize(readFrameSize);




        Object inputObject = (Object) (cc.getDataComponentFromInput(DATA_INPUT));
        if (StreamTerminator.isStreamTerminator(inputObject)) {
            cc.pushDataComponentToOutput(DATA_OUTPUT_3, inputObject);
            return;
        }

        Signal theSignal = (Signal) inputObject;

        try {
            theSignal = reader.initialise(theSignal);
        } catch (noMetadataException e) {
            e.printStackTrace();
        }
        cc.pushDataComponentToOutput(DATA_OUTPUT_3, theSignal);
        try {
            cc.pushDataComponentToOutput(DATA_OUTPUT_4,
                    new Integer(theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE)));
        } catch (noMetadataException e) {
            e.printStackTrace();
        }

        double[][] outFrames = reader.getDataFrame();

        int count = 0;
        while (outFrames != null) {
            //System.out.println ("AudioFileRead:"+count+":"+outFrames[0]+":"+outFrames[1]);
            if (outFrames[0] != null) {
                cc.pushDataComponentToOutput(DATA_OUTPUT_1, outFrames[0]);
            }
            if (outFrames[1] != null) {
                cc.pushDataComponentToOutput(DATA_OUTPUT_2, outFrames[1]);
            }
            outFrames = reader.getDataFrame();
            count++;
        }
        cc.pushDataComponentToOutput(DATA_OUTPUT_1, new StreamTerminator());
        cc.pushDataComponentToOutput(DATA_OUTPUT_2, new StreamTerminator());
        System.out.println("AudioFileRead:countFrames = " + count);
//		cc.pushDataComponentToOutput(DATA_OUTPUT_5, Integer.valueOf(count));
    }

    public double getOutputFrameLength() {
        return outputFrameLength;
    }

    public void setOutputFrameLength(double outputFrameLength) {
        this.outputFrameLength = outputFrameLength;
    }

    public double getOverlapPercent() {
        return overlapPercent;
    }

    public void setOverlapPercent(double overlapPercent) {
        this.overlapPercent = overlapPercent;
    }
}
