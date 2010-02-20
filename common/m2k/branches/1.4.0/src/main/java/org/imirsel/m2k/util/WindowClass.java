package org.imirsel.m2k.util;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;


/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 */
public class WindowClass {
    
    public static final String WINDOW_RECT = "Rectangular";
    public static final String WINDOW_BARTLETT= "Bartlett";
    public static final String WINDOW_HANNING = "Hanning";
    public static final String WINDOW_HAMMING = "Hamming";
    public static final String WINDOW_BLACKMAN = "Blackman";
    
    /**
     * Window type to use.
     */
    private String   WindowName = "Rectangular";
    private double[] window     = null;
    
    /** Creates a new instance of WindowClass */
    public WindowClass() {
    }
    
    public WindowClass(String WindowName)
    {
        setWindowName(WindowName);
    }
    
    public double[] applyWindow(final double[] double1DArray) {
        if (window == null)
        {
            initWindow(double1DArray.length);
        }else if(window.length != double1DArray.length){
            initWindow(double1DArray.length);
        }
        double[] output = new double[window.length];
        for (int i = 0; i < window.length; i++) {
            output[i] = window[i] * double1DArray[i];
        }
        return output;
    }

    private void initWindow(final int array1NumValues) {
        int windowType = 1;

        if (WindowName.equals(WINDOW_RECT)) {
            windowType = 1;
        }

        if (WindowName.equals(WINDOW_BARTLETT)) {
            windowType = 2;
        }

        if (WindowName.equals(WINDOW_HANNING)) {
            windowType = 3;
        }

        if (WindowName.equals(WINDOW_HAMMING)) {
            windowType = 4;
        }

        if (WindowName.equals(WINDOW_BLACKMAN)) {
            windowType = 4;
        }

        window = new double[array1NumValues];

        switch (windowType) {
        case 1 :    /* Rectangular */
            break;

        case 2 :    /* Bartlett */
            if ((array1NumValues % 2) == 0) {
                for (int d1 = 0; d1 < array1NumValues; d1++) {
                    if (d1 <= array1NumValues / 2) {
                        window[d1] = 
                            (2 * (double) d1
                             / ((double) array1NumValues - 1));
                    } else {
                        window[d1] = 
                            (2 - 2 * (double) d1
                             / ((double) array1NumValues - 1));
                    }
                }
            } else {
                for (int d1 = 0; d1 < array1NumValues; d1++) {
                    if (d1 <= array1NumValues / 2) {
                        window[d1] = 
                            (2 * (double) d1
                             / ((double) array1NumValues - 1));
                    } else {
                        window[d1] = 
                            2 * ((array1NumValues - (double) d1 - 1)
                                 / ((double) array1NumValues - 1));
                    }
                }
            }

            break;

        case 3 :    /* Hanning */
            for (int d1 = 0; d1 < array1NumValues; d1++) {
                window[d1] = (0.5
                              - 0.5
                                * Math.cos(2 * Math.PI * (d1 + 1)
                                           / (array1NumValues + 1)));
            }

            break;

        case 4 :    /* Hamming */
            for (int d1 = 0; d1 < array1NumValues; d1++) {
                window[d1] = (0.54
                              - 0.46
                                * Math.cos(2 * Math.PI * d1
                                           / (array1NumValues - 1)));
            }

            break;

        case 5 :    /* Blackman */
            for (int d1 = 0; d1 < array1NumValues; d1++) {
                window[d1] =
                    (0.42
                     - 0.5
                       * Math.cos(
                           2 * Math.PI * d1
                           / (array1NumValues - 1)) + 0.08 
                           * Math.cos(4 * Math.PI * d1 / (array1NumValues - 1)));
            }

            break;
        }
    }

    public String getWindowName() {
        return WindowName;
    }

    public void setWindowName(String WindowName) {
        this.WindowName = WindowName;
        window = null;
    }

    public double[] getWindow() {
        return window;
    }
}
