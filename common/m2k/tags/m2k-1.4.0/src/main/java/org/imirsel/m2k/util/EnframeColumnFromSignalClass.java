package org.imirsel.m2k.util;

/**
 * Takes in a Signal Object, extracts a specified column of data, enframes
 * the data and streams out the frames.
 *
 * @author Kris West
 */
public class EnframeColumnFromSignalClass {
    String   columnLabel = "";
    int      frameSize   = -1;
    int      overlapSize = -1;
    int      increment   = -1;
    int      samplerate  = -1;
    int      numTimes    = -1;
    int      count       = -1;
    double[] theColumn   = null;
    double   frameLength;
    double   overlapPercent;
    Signal   theSignal;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new instance of EnframeColumnFromSignalClass
     */
    public EnframeColumnFromSignalClass(Signal theSignal_,
            String columnLabel_, double frameLength_, double overlapPercent_)
            throws noMetadataException {
        
        theSignal      = theSignal_;
        columnLabel    = columnLabel_;
        frameLength    = frameLength_;
        overlapPercent = overlapPercent_;

        int originalSrate       = -1;
        int originalFrameSize   = -1;
        int originalOverlapSize = -1;

        // Get framerate metadata
        try {
            originalSrate = theSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE);
        } catch (noMetadataException nme) {
            throw new noMetadataException(
                "The sample rate of this signal is not set!\nNo time index can be calculated.");
        }

        try {
            originalFrameSize =
                theSignal.getIntMetadata(Signal.PROP_FRAME_SIZE);
        } catch (noMetadataException nme) {}

        try {
            originalOverlapSize =
                theSignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE);
        } catch (noMetadataException nme) {}

        // calculate new frame size
        if ((originalFrameSize != -1) && (originalOverlapSize != -1)) {
            frameSize = (int) Math.round(
                (((double) frameLength * (double) originalSrate)
                 / (double) (originalFrameSize - originalOverlapSize)) - 1.0);
        } else if (originalFrameSize != -1) {
            frameSize = (int) Math.round(
                (((double) frameLength * (double) originalSrate)
                 / (double) originalFrameSize) - 1);
        } else if (originalOverlapSize != -1) {
            throw new RuntimeException(
                "An overlap size was found with no frame size and cannot be used to calculate time indicies!");
        }

        // calculate new overlap size
        overlapSize = Math.round((float) frameSize * (float) overlapPercent);
        increment   = frameSize - overlapSize;

        // Get the data column
        try {
            int colNum = theSignal_.getColumnIndex(columnLabel);

            if (colNum == -1) {
                throw new RuntimeException(
                    "The specified column label was not found");
            }

            theColumn = theSignal.getData()[colNum];

            // System.out.println("col length = " + theColumn.length);
        } catch (noMetadataException nme) {
            throw new RuntimeException(
                "No column labels were found in the input Signal Object!");
        }

        // calculate number of frames
        this.numTimes =
            (int) Math.floor(((float) theColumn.length - frameSize)
                             / (float) increment) + 1;

        // System.out.println("srate: " + samplerate + ", frameSize: " + frameSize + ", overlapSize: " + overlapSize + ", increment: " + increment);
        // System.out.println("Expected length = " + ((numTimes + 1) * increment) + " outputting " + numTimes + " times");
        this.count = 0;

        // calculate new sample rate
        samplerate = (int) Math.round(originalSrate
                                      / (double) (originalFrameSize
                                          - originalOverlapSize));
        theSignal = theSignal.cloneNoData();
        theSignal.setMetadata(Signal.PROP_SAMPLE_RATE,
                              new Integer(samplerate));
        theSignal.setMetadata(Signal.PROP_OVERLAP_SIZE,
                              new Integer(overlapSize));
        theSignal.setMetadata(Signal.PROP_FRAME_SIZE, new Integer(frameSize));
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the next frame of data to output, or returns null if the end has
     * been reached (data is also deleted).
     * @return the next frame of data to output, or returns null if the end has
     * been reached (data is also deleted)
     */
    public double[] getFrame() {
        if (count < numTimes) {

            // System.out.println("count: " + count);
            double[] out   = new double[this.frameSize];
            int      start = count * increment;

            for (int i = 0; i < this.frameSize; i++) {
                out[i] = (double) theColumn[start + i];
            }

            count++;

            return out;
        } else {

            // delete data?
            count++;
            theColumn = null;

            return null;
        }
    }

    /**
     *  Returns the Signal Object.
     *  @return the Signal Object.
     */
    public Signal getTheSignal() {
        return this.theSignal;
    }

    /** Returns true if there are still frames to output.
     *  @return true if there are still frames to output.
     */
    public boolean hasFrames() {
        if (count <= numTimes) {
            return true;
        } else {
            return false;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
