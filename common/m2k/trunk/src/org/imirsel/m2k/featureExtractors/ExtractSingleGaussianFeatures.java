/*
 * ExtractFeatures.java
 *
 * Created on 23 April 2007, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.featureExtractors;

import java.io.File;
import java.util.ArrayList;
import org.imirsel.m2k.io.file.AudioFileReadClass;
import org.imirsel.m2k.math.SignalMeanandCovarianceClass;
import org.imirsel.m2k.transforms.FFTRealClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.WindowClass;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kw
 */
public class ExtractSingleGaussianFeatures {
    
    AudioFileReadClass reader;
    WindowClass theWindow;
    FFTRealClass theFFT;
    FeatureExtractorInterface featExtractor;
    SignalMeanandCovarianceClass summariser;
    
    /** Creates a new instance of ExtractFeatures */
    public ExtractSingleGaussianFeatures(FeatureExtractorInterface aFeatureExtractor) {
        featExtractor = aFeatureExtractor;
        // ------------------------------------
        // Component initialisation
        reader = new AudioFileReadClass();
        reader.setVerbose(false);
        reader.setDownmix(true);
        
        //setup reader
        reader.setLimitAudioFrames(false);
        reader.setDownmix(true);
        
        reader.setOutputFrameLength(0.023219954f);
        reader.setOverlapPercent(0.5f);
        reader.setReadFrameSize(81920);
        
        //setup window
        theWindow = new WindowClass();
        theWindow.setWindowName(WindowClass.WINDOW_HAMMING);
        //setup STFT
        theFFT =  new FFTRealClass();
        
        //setup summariser
        summariser = new SignalMeanandCovarianceClass();
        
    }
    
    public Signal ExtractFeatures(File theFile){
        
        Signal featSignal;
        try{
            featSignal = reader.initialise(new Signal(theFile.getPath()));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Audio file reader initialisation failed!");
        }
        try {
            System.out.println("SampleRate: " + featSignal.getIntMetadata(Signal.PROP_SAMPLE_RATE));
        } catch (noMetadataException ex) {
            throw new RuntimeException("No samplerate");
        }
        
        //init feature extractor
        featExtractor.init(featSignal);
            
        boolean keepReading = true;
        System.out.println("Decoding audio and computing features vectors");
        ArrayList featFrames = new ArrayList();
        double[] readerOutput = null;
        while (keepReading) {
            try {
                //get a frame of audio
                //we down mixed to mono so only one vector to collect
                readerOutput = reader.getDataFrame()[0];
            } catch (Exception e) {
                keepReading = false;
                break;
            }
            
            if (readerOutput == null) {
                keepReading = false;
                break;
            }
            
            double[] aFrame = theWindow.applyWindow(readerOutput);
            double[] spectrum = theFFT.fftScaledMagnitude(aFrame);
            double[] featureFrame = featExtractor.extractMFCCsFromSpectra(spectrum);
            featFrames.add(featureFrame);
        }
            
        int cols = ((double[])featFrames.get(0)).length;
        int rows = featFrames.size();
        double[][] featsMat = new double[cols][featFrames.size()];
        
        for (int i = 0; i < rows; i++) {
            double[] aFrame = (double[])featFrames.remove(0);
            for (int j = 0; j < cols; j++) {
                featsMat[j][i] = aFrame[j];
            }
        }
        
        featSignal.appendMatrix(featsMat, featExtractor.getColNames());
        Signal out = null;
        try {
            out = summariser.summarise(featSignal);
        } catch (noMetadataException ex) {
            ex.printStackTrace();
        }
        return out;
    }
}
