/*
 * KeyFinder.java
 *
 * Created on 20 April 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.myCodeExamples;

import java.io.File;
import java.util.ArrayList;
import java.lang.Math;
import org.imirsel.m2k.io.file.AudioFileReadClass;
import org.imirsel.m2k.transforms.FFTRealClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.WindowClass;
import org.imirsel.m2k.util.noMetadataException;
import org.imirsel.m2k.math.Mathematics;
/**
 *
 * @author kw
 */
public class KeyFinder {
    
    public static final double[][] templates = new double[][]{
    {0.3963,0.1500,0.3383,0.1445,0.3664,0.2657,0.2044,0.3741,0.1395,0.3566,0.1495,0.3599},
    {0.3702,0.3841,0.1475,0.2996,0.1234,0.4066,0.2249,0.2554,0.3941,0.1506,0.3249,0.1552},
    {0.1715,0.3633,0.3782,0.1185,0.3339,0.1697,0.3637,0.2632,0.2123,0.3924,0.1444,0.3461},
    {0.3569,0.1869,0.3579,0.3585,0.0993,0.3289,0.1137,0.4075,0.2993,0.2034,0.3561,0.1620},
    {0.1495,0.3626,0.1563,0.3457,0.3746,0.1248,0.3306,0.1587,0.3949,0.2811,0.1881,0.3761},
    {0.3968,0.1320,0.3350,0.1217,0.3240,0.3919,0.1092,0.3753,0.1506,0.3950,0.2609,0.1997},
    {0.2071,0.4037,0.1488,0.3040,0.1342,0.3429,0.3722,0.1465,0.3981,0.1519,0.3747,0.2491},
    {0.2521,0.1882,0.3835,0.1141,0.3054,0.1635,0.3478,0.4463,0.1312,0.3523,0.1242,0.3796},
    {0.3635,0.2734,0.2101,0.3586,0.0864,0.3011,0.1062,0.4200,0.4562,0.1238,0.3145,0.1106},
    {0.1421,0.3726,0.2951,0.1571,0.3652,0.1207,0.3388,0.1428,0.3821,0.4329,0.1035,0.3194},
    {0.3418,0.1476,0.4152,0.2596,0.1299,0.3694,0.1023,0.3788,0.1599,0.3423,0.4018,0.1008},
    {0.1179,0.3741,0.1365,0.3737,0.2669,0.1608,0.4137,0.1392,0.3674,0.1353,0.3002,0.3961},
    {0.4325,0.1868,0.3294,0.3298,0.1662,0.2695,0.2002,0.4207,0.2371,0.2592,0.2163,0.2706},
    {0.2668,0.4272,0.1597,0.3017,0.3278,0.2200,0.2536,0.2657,0.4117,0.2223,0.2348,0.2549},
    {0.2855,0.2675,0.4087,0.1302,0.3049,0.3559,0.1594,0.2681,0.2224,0.4305,0.2132,0.2567},
    {0.2739,0.2869,0.2890,0.3897,0.1239,0.3066,0.3267,0.1780,0.3209,0.2205,0.4029,0.2134},
    {0.2068,0.2656,0.2183,0.2620,0.4062,0.1635,0.3124,0.3888,0.1494,0.2902,0.1995,0.4284},
    {0.4309,0.2077,0.2299,0.1919,0.2441,0.4144,0.1175,0.3777,0.3922,0.1670,0.2632,0.2066},
    {0.2132,0.4174,0.2416,0.1901,0.2050,0.2713,0.3997,0.1610,0.3767,0.3898,0.1580,0.2561},
    {0.2660,0.2045,0.4188,0.2173,0.1870,0.2317,0.2425,0.4688,0.1702,0.3352,0.3560,0.1718},
    {0.1645,0.2967,0.2178,0.4058,0.2090,0.1923,0.2168,0.2932,0.4857,0.1404,0.3021,0.3351},
    {0.3605,0.1519,0.3041,0.1544,0.3849,0.2224,0.2155,0.2750,0.2909,0.4673,0.1194,0.3061},
    {0.3310,0.3853,0.1697,0.2672,0.1427,0.4031,0.2114,0.2655,0.2510,0.2787,0.4339,0.1218},
    {0.1347,0.3366,0.4104,0.1312,0.2650,0.1764,0.4433,0.2543,0.2240,0.2040,0.2415,0.4126}};
    
    /** Creates a new instance of KeyFinder */
    public KeyFinder() {
    }
    
    public static Signal findKey(File audioFile){
        
        // Notification
        System.out.println("Finding key for: " + audioFile.getPath());
        
        // ------------------------------------
        // Component initialisation
        AudioFileReadClass reader = new AudioFileReadClass();
        reader.setVerbose(false);
        
        WindowClass theWindow = new WindowClass();
        FFTRealClass theFFT =  new FFTRealClass();
        reader.setDownmix(true);
        
        //setup reader
        reader.setLimitAudioFrames(false);
        reader.setDownmix(true);
        float frameLen = 0.1857596372f;
        float overlap = 0.5f;
        
        reader.setOutputFrameLength(frameLen);
        reader.setOverlapPercent(overlap);
        reader.setReadFrameSize(81920);
        
        Signal keySignal;
        try{
            keySignal = reader.initialise(new Signal(audioFile.getPath()));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Audio file reader initialisation failed!");
        }
        try {
            System.out.println("SampleRate: " + keySignal.getIntMetadata(Signal.PROP_SAMPLE_RATE));
        } catch (noMetadataException ex) {
            throw new RuntimeException("No samplerate");
        }
        
        int frameSize;
        int sampleRate;
        int overlapSize;
        
        double freq_low = 55.0f;
        double freq_high = 2000.0f;
        double freq_ref = 13.75f;
        double[] histogram = new double[12];
        
        try {
            frameSize = keySignal.getIntMetadata(Signal.PROP_FRAME_SIZE);
            sampleRate = keySignal.getIntMetadata(Signal.PROP_SAMPLE_RATE);
            overlapSize = keySignal.getIntMetadata(Signal.PROP_OVERLAP_SIZE);
        } catch (noMetadataException ex) {
            throw new RuntimeException("Step size calculation failed!",ex);
        }
        
        double frameLength = (double)(frameSize - overlapSize) / (double)sampleRate;
        
        //System.out.println("outputting at most " + reader.getMaxOutFrames() + " frames");
        System.out.println("outputting " + frameSize + " sample frames");
        
        //setup window
        theWindow.setWindowName(WindowClass.WINDOW_HAMMING);
        
        //setup FFT
        theFFT.setinverse(false);
        
        //read audio and extract MFCCs, BandPassFeatures etc.
        boolean keepReading = true;
        ArrayList featureFrames = new ArrayList();
        ArrayList signalFrames = new ArrayList();
        
        double[][] readerOutput = null;
        double[] aFrame = null;
        double[] spectrum = null;
        
        int numFrames = 0;
        
        System.out.println("Decoding audio and computing STFT");
        
        int lo_index = (int)Math.round(freq_low*(double)frameSize/(double)sampleRate);
        int hi_index = (int)Math.round(freq_high*(double)frameSize/(double)sampleRate);

        while (keepReading) {
            numFrames++;
            try {
                //get a frame of audio
                readerOutput = reader.getDataFrame();
            } catch (Exception e) {
                keepReading = false;
                break;
            }
            
            if (readerOutput == null) {
                keepReading = false;
                break;
            }
            
            aFrame = readerOutput[0];
            
            aFrame = theWindow.applyWindow(aFrame);
            spectrum = theFFT.fftMagnitude(aFrame);
            //System.out.println("speclength: " + spectrum.length + "\n");
            int ind = 0;
            for (int i = lo_index; i <= hi_index; i++) {
                ind = (int)Math.round(12.0*Math.log((double)i*(double)sampleRate/(double)frameSize/freq_ref)/Math.log(2.0));
                //System.out.println(ind);
                ind = ind % 12;
                histogram[ind] += spectrum[i];
            }
        }
        System.out.println("Estimating Musical Key");
        double histnorm = Mathematics.norm(histogram,2);
            for (int j = 0; j < histogram.length; j++) {
                histogram[j] = histogram[j]/histnorm;
            }
            double mh = Mathematics.mean(histogram);
            double[] corrs = new double[templates.length];
            for (int k = 0; k < corrs.length; k++) {
                double mt = Mathematics.mean(templates[k]);
                double corr = 0.0;
                for (int n = 0; n < templates[k].length; n++){
                    corr += (histogram[n] - mh)*(templates[k][n]-mt);
                }
                corr = corr/Mathematics.stdev(histogram);
                corr = corr/Mathematics.stdev(templates[k]);
                corr = corr/(double)(histogram.length-1);
                corrs[k] = corr;
            }
            double max = corrs[0];
            int keyInd = 0;
            for(int i = 1; i < corrs.length; i++) {
                if (corrs[i] > max) {
                    max = corrs[i];
                    keyInd = i;
                }
            }
            String tonic;
            String mode;
            if (keyInd < 12) {
                mode = "major";
            } 
            else {
                mode = "minor";
            }
            
            if ((keyInd == 0) || (keyInd == 12)) {
                tonic = "A";
            } else if ((keyInd == 1) || (keyInd == 13)) {
                tonic = "A#";
            } else if ((keyInd == 2) || (keyInd == 14)) {
                tonic = "B";
            } else if ((keyInd == 3) || (keyInd == 15)) {
                tonic = "C";
            } else if ((keyInd == 4) || (keyInd == 16)) {
                tonic = "C#";
            } else if ((keyInd == 5) || (keyInd == 17)) {
                tonic = "D";
            } else if ((keyInd == 6) || (keyInd == 18)) {
                tonic = "D#";
            } else if ((keyInd == 7) || (keyInd == 19)) {
                tonic = "E";
            } else if ((keyInd == 8) || (keyInd == 20)) {
                tonic = "F";
            } else if ((keyInd == 9) || (keyInd == 21)) {
                tonic = "F#";
            } else if ((keyInd == 10) || (keyInd == 22)) {
                tonic = "G";
            } else {
                tonic = "G#";
            }

        //finally, put key estimate into the Signal Object and return
        String[] key = new String[2];
        
        //populate key, e.g.
        key[0] = tonic;
        key[1] = mode;
        //you can use the onstant in keyFindingAccuracy if it helps:
        //key[0] = KeyFindingAccuracy.MAJOR_CIRCLE[0];
        
        
        keySignal.setMetadata(Signal.PROP_KEY,key);
        return keySignal;
    }
    
}
