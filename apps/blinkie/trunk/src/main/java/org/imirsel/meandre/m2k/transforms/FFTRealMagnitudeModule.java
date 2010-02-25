package org.imirsel.meandre.m2k.transforms;

import org.imirsel.m2k.transforms.FFTRealClass;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

import org.meandre.core.ComponentContextProperties;

/**
 *
 *
 * @author Kris West (kw@cmp.uea.ac.uk)
 * Modified by Lily Dong
 */

@Component(creator="Kris West",
		description="Overview: This computes the magnitude or power spectrum of an input array with a DFT." +
		"Detailed Description: This module uses a radix-2 Fast Fourier Transform to " +
		"compute the Discrete Fourier Transform of a 1-dimensional input array and " +
		"then calculates magnitude or power spectrum from it.  If the input is " +
		"not a power-of-2 in length, the input will automatically be zeropadded to the next " +
		"appropriate power of two length.  The module uses only the nonredundant part of " +
		"the conjugate-symmetric spectrum. If calculating only the magnitude or power spectrum " +
		"this module may be more efficient than using FFTReal, Truncate1DDArray and Magnitude " +
		"(and pow for the power spectrum)." +
		"Data Handling: The input data is not modified.",
		name="FFTRealMagnitude",
		tags="compoute the magnitude or power spectrum of an input array with a DFT")

		public class FFTRealMagnitudeModule implements ExecutableComponent {//extends ComputeModule {
	@ComponentInput(description="input(1-D double array)",
			name= "input")
			final static String DATA_INPUT = "input";

	@ComponentOutput(description="magnitude or power spectrum(1-D double array)",
			name="Spectrum")
			final static String DATA_OUTPUT = "Spectrum";

	@ComponentProperty(defaultValue="false",
			description="scale magnitude coefficients",
			name="scaleMagnitude")
			final static String DATA_PROPERTY_1 = "scaleMagnitude";
	@ComponentProperty(defaultValue="false",
			description="produce power spectrum",
			name="powerSpectrum")
			final static String DATA_PROPERTY_2 = "powerSpectrum";

	private FFTRealClass theFFT = null;
	private boolean scaleMagnitude = false;
	private boolean powerSpectrum = false;

	/**
	 * Returns an array of description objects for each property of the Module.
	 *
	 * @return an array of description objects for each property of the Module.
	 */
	/*    public PropertyDescription[] getPropertiesDescriptions() {
        PropertyDescription[] pds = new PropertyDescription[2];

        pds[0] = new PropertyDescription("scaleMagnitude", "Scale magnitude coefficients",
                "Determines whether the output coefficients should be scaled including special-handling for Nyquist point and zero-thh coefficient.");
        pds[1] = new PropertyDescription("powerSpectrum", "Produce Power Spectrum", "Determines whether the Magnitude coefficients or the " +
                "Power Spectrum coefficients are calculted. Note: the FFT magnitudes are " +
                "always scaled when calculating the power spectrum.");

        return pds;
    }*/

	/**
	 * Performs operations at the beginning of itinerary execution.
	 *
	 * @see #dispose
	 */
	public void initialize(ComponentContextProperties ccp) {
		theFFT = new FFTRealClass();
                
                
		scaleMagnitude = Boolean.valueOf(ccp.getProperty(DATA_PROPERTY_1));
		powerSpectrum = Boolean.valueOf(ccp.getProperty(DATA_PROPERTY_2));
	}

	/**
	 * Performs operations at the end of itinerary execution.
	 *
	 * @see #beginExecution
	 */
	public void dispose(ComponentContextProperties ccp) {
		theFFT = null;
	}

	/**
	 *
	 */
	public void execute(ComponentContext cc)
	throws ComponentExecutionException, ComponentContextException {
		

                Object input = cc.getDataComponentFromInput(DATA_INPUT);
                if (input instanceof StreamTerminator){
                    cc.pushDataComponentToOutput(DATA_OUTPUT, input);
                }else{
                    double[] dataReal = (double[])(input);
                

                    /*for(int i=0; i<dataReal.length; i++)
                System.out.println("dataReal[" + i + "] = " + dataReal[i]);*/


                    if (this.powerSpectrum)
                    {
                            double[] out = this.theFFT.fftPowerSpectrum(dataReal);
                            cc.pushDataComponentToOutput(DATA_OUTPUT, out);
                    }else if(this.scaleMagnitude)
                    {
                            double[] out = this.theFFT.fftScaledMagnitude(dataReal);
                            cc.pushDataComponentToOutput(DATA_OUTPUT, out);
                    }else
                    {
                            double[] out = this.theFFT.fftMagnitude(dataReal);

                            /*for(int i=0; i<out.length; i++)
                        System.out.println("out[" + i + "] = " + out[i]);*/

                        cc.pushDataComponentToOutput(DATA_OUTPUT, out);
                    }
                }
	}
}
