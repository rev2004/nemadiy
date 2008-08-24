/*
 * Mathematics.java
 *
 * Created on December 12, 2005, 5:49 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.imirsel.m2k.math;

/**
 * A class providing simplae and array-based mathmatical functions
 * @author Andreas Ehmann
 */
public class Mathematics {
    
    /**
     * Creates a new instance of Mathematics
     */
    public Mathematics() {
    }
    
    /**
     * Calculates the mean of an input array of doubles
     *
     * @param input the array for which the mean is calculated
     *
     * @return output the mean of the input array
     */
    public static double mean(double[] input) {
        double output = 0.0;
        for(int i = 0; i < input.length; i++) {
            output += input[i];
        }
        output = output/(double)input.length;
        return output;
    }
    
    /**
     * Calculates the centroid of an input array of doubles
     *
     * @param input the array for which the centroid is calculated
     *
     * @return output the centroid of the input array
     */
    public static double centroid(double[] input){
        double output;
        double numerator = 0.0;
        double denominator = 0.0;
        
        for (int i = 0; i < input.length; i++ ) {
            numerator += i*input[i];
            denominator += input[i];
        }
        if (denominator == 0) {
            denominator = 1.0;
        }
        output = numerator/denominator;
        return output;
    }
    
    /**
     * Calculates the variance of an input array of doubles
     *
     * @param input the array for which the variance is calculated
     *
     * @return output the variance of the input array
     */
    public static double variance(double[] input) {
        double output = 0.0;
        double mean = 0.0;
        for(int i = 0; i < input.length; i++) {
            mean += input[i];
        }
        mean = mean/(double)input.length;
        for(int i = 0; i < input.length; i++) {
            output += (input[i] - mean) * (input[i] - mean);
        }
        output = output/(double)input.length;
        return output;
    }
    
    /**
     * Calculates the standard deviation of an input array of doubles
     *
     * @param input the array for which the standard deviation is calculated
     *
     * @return output the standard deviation of the input array
     */
    public static double stdev(double[] input) {
        double output = 0.0;
        double mean = 0.0;
        for(int i = 0; i < input.length; i++) {
            mean += input[i];
        }
        mean = mean/(double)input.length;
        for(int i = 0; i < input.length; i++) {
            output += (input[i] - mean) * (input[i] - mean);
        }
        output = output/(double)input.length;
        output = Math.sqrt(output);
        return output;
    }
    
    /**
     * Calculates the autocorrelation of an input array of doubles
     *
     * @param input the array for which the autocorrelation is calculated
     *
     * @return output the variance of the input array
     */
    public static double[] autocorrelate(double[] input)
   {
       double mean = mean(input);
               
       double[] output = new double[input.length];
       for (int i = 0; i < input.length; i++) {//lag
           for (int j = 0; j < input.length; j++) {//value being correlated
               if ((i+j) < input.length){
                   output[i] += (input[i+j] - mean)*(input[j] - mean);
               }else{
                   break;
               }
           }
       }
       return output;
   }
    
    /**
     * Calculates the skew of an input array of doubles
     *
     * @param input the array for which the skew is calculated
     *
     * @return output the skew of the input array
     */
    public static double skew(double[] input) {
        double output;
        double mean = 0.0;
        double numerator = 0.0;
        double denominator = 0.0;
        for(int i = 0; i < input.length; i++) {
            mean += input[i];
        }
        mean = mean/(double)input.length;
        for(int i = 0; i < input.length; i++) {
            numerator += Math.pow((input[i] - mean),3.0);
            denominator += Math.pow((input[i] - mean),2.0);
        }
        if (denominator == 0) {
            denominator = 1.0;
        }
        numerator = numerator * Math.sqrt((double)input.length);
        denominator = Math.pow(denominator,  1.5);
        output = numerator/denominator;
        return output;
    }
    
    /**
     * Calculates the kurtosis of an input array of doubles, corrected
     * for the normal distribution
     *
     * @param input the array for which the kurtosis is calculated
     *
     * @return output the kurtosis of the input array
     */
    public static double kurtosis(double[] input) {
        double output;
        double mean = 0.0;
        double numerator = 0.0;
        double denominator = 0.0;
        for(int i = 0; i < input.length; i++) {
            mean += input[i];
        }
        mean = mean/(double)input.length;
        for(int i = 0; i < input.length; i++) {
            numerator += Math.pow((input[i] - mean),4.0);
            denominator += Math.pow((input[i] - mean),2.0);
        }
        if (denominator == 0) {
            denominator = 1.0;
        }
        numerator = numerator*(double)input.length;
        denominator = denominator*denominator;
        
        output = numerator/denominator - 3.0;
        return output;
    }
    
    /**
     * Calculates the max value in an input array of doubles
     *
     * @param input the array for which the max is found
     *
     * @return output the mean of the input array
     */
    public static double max(double[] input) {
        double max = input[0];
        for(int i = 1; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
            }
        }
        return max;
    }
    
    /**
     * Calculates the min value in an input array of doubles
     *
     * @param input the array for which the min is found
     *
     * @return output the mean of the input array
     */
    public static double min(double[] input) {
        double min = input[0];
        for(int i = 1; i < input.length; i++) {
            if (input[i] < min) {
                min = input[i];
            }
        }
        return min;
    }
    
    /**
     * Calculates the p-Norm of an input array of doubles.
     * p = 2 is the RMS of the input array.
     *
     * @param input the array for which the p-Norm is calculated
     * @param p the power p of the norm
     *
     * @return output the p-Norm of the input array
     */
    public static double norm(double[] input, double p) {
        double output = 0.0;
        for(int i = 0; i < input.length; i++) {
            output += Math.pow(Math.abs(input[i]), p);
        }
        output = Math.pow(output, 1.0/p);
        return output;
    }
    
    /**
     * Calculates the inf-Norm of an input array of doubles, i.e.
     * the largest absolute value in the input array
     *
     * @param input the array for which the norm is calculated
     *
     * @return output the inf-Norm of the input array
     */
    public static double normInf(double[] input) {
        double output = max(absArray(input));
        return output;
    }
    
    /**
     * Calculates the -inf-Norm of an input array of doubles, i.e.
     * the smallest absolute value in the input array
     *
     * @param input the array for which the -inf-Norm is calculated
     *
     * @return output the -inf-Norm of the input array
     */
    public static double normNegInf(double[] input) {
        double output = min(absArray(input));
        return output;
    }
    
    /**
     * Sums all elements of an input array of doubles
     *
     * @param input the array for which the elements are summed
     *
     * @return output the sum of elements of the input array
     */
    public static double sum(double[] input) {
        double output = 0.0;
        for(int i = 0; i < input.length; i++) {
            output += input[i];
        }
        return output;
    }
    
    /**
     * Computes the absolute value all elements of an input array of doubles
     * (Full wave rectification)
     *
     * @param input the array for which the elements rectified
     *
     * @return output the rectified elements of the input array
     */
    public static double[] absArray(double[] input){
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            output[i] = Math.abs(input[i]);
        }
        return output;
    }
    
    /**
     * Raises each element of an array to the p-throws power
     *
     * @param input the array for which the elements are exponentiated
     * @param p the power to raise each element of the array to
     *
     * @return output the elements of the input array raised to the
     * p-th power
     */
    public static double[] powArray(double[] input, double p){
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            output[i] = Math.pow(input[i],p);
        }
        return output;
    }
    
    /**
     * Takes the square-root of each element of an array
     *
     * @param input the array for which the square-rooting is done
     *
     * @return output the array with all elements square-rooted
     */
    public static double[] sqrtArray(double[] input){
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            output[i] = Math.sqrt(input[i]);
        }
        return output;
    }
    
    /**
     * Scales each element of an array by p
     *
     * @param input the array to scale by factor p
     * @param p the multiplicative scaling factor
     *
     * @return output the elements of the input array raised to the
     * p-th power
     */
    public static double[] scaleArray(double[] input, double p){
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            output[i] = p*input[i];
        }
        return output;
    }
    
    /**
     * Half wave rectifies all elements of an input array of doubles
     * with a setable threshold. All elements greater than the threshold
     * are unchanged; elements less than the threshold are set to zero.
     *
     * @param input the array for which the elements rectified
     *
     * @return output the rectified elements of the input array
     */
    public static double[] halfWaveRectify(double[] input, double threshold){
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            output[i] = input[i] > threshold ? input[i] : threshold;
        }
        return output;
    }
    
    /**
     * Computes the magnitude of all elements of two arrays representing
     * the real and imaginary components of complex numbers
     *
     * @param real the array of real components
     * @param imag the array of imaginary components
     *
     * @return output the magnitude of each complex element
     */
    public static double[] magnitude(double[] real, double[] imag) {
        double[] output = new double[real.length];
        for(int i = 0; i < real.length; i++) {
            output[i] = Math.sqrt((real[i]*real[i] + imag[i]*imag[i]));
        }
        return output;
    }
    
    /**
     * Computes the phase angle of all elements of two arrays representing
     * the real and imaginary components of complex numbers
     *
     * @param real the array of real components
     * @param imag the array of imaginary components
     *
     * @return output the phase angle of each complex element
     */
    public static double[] angle(double[] real, double[] imag) {
        double[] output = new double[real.length];
        for(int i = 0; i < real.length; i++) {
            output[i] = Math.atan2(imag[i],real[i]);
        }
        return output;
    }
    
    /**
     * Computes the inner product of two arrays of doubles
     *
     * @param vector1 the array of representing the first vector
     * @param vector2 the array of representing the second vector
     *
     * @return output the inner product of the two vectors
     */
    public static double innerProduct(double[] vector1, double[] vector2) {
        double output = 0.0;
        for (int i = 0; i < vector1.length; i++ ) {
            output += vector1[i] * vector2[i];
        }
        return output;
    }
    
    /**
     * Computes the log base N of elements of an array of doubles.  Defaults to
     * 1e-9 for logarithms of 0. Specifying N as 0.0 denotes natural log, base e.
     *
     * @param input the array for which the logarith of the elements is computed
     * @param N the base of the logarithm.  Specifying N = 0.0 will result in the
     * natural logarithm (base e) being taken
     *
     * @return output an array whose elements are log base N of the elements of
     * the input
     */
    public static double[] logN(double[] input, double N) {
        double[] output = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            if(input[i] <= 0.0){
                if (N != 0.0) {
                    output[i] = -50.0/Math.log(N);
                } else {
                    output[i] = -11.5;
                }
            }
            else {
                if (N != 0.0) {
                    output[i] = Math.log(input[i])/Math.log(N);
                } else {
                    output[i] = Math.log(input[i]);
                }
            }
        }
        return output;
    }
    
    /**
     * Calculates the rolloff point (array index) of an input array of doubles,
     * i.e. The point in the array below which a given percent of the sum
     * total lies.
     *
     * @param input the array for which the rolloff is calculated
     * @param percent the percent of the sum total at which the rolloff point is
     * calculated
     *
     * @return output the kurtosis of the input array
     */
    public static double rollOff(double[] input, double percent) {
        double output = 0;
        double tot_energy = 0;
        double partial_energy = 0;
        
        for (int i = 0; i < input.length; i++) {
            tot_energy += input[i];
        }
        
        for (int i = 0; i < input.length; i++) {
            partial_energy += input[i];
            if (partial_energy >= percent * tot_energy) {
                output = i;
                break;
            }
        }
        return output;
    }
    
    /**
     * Computes the vector sum of two arrays of doubles
     *
     * @param vector1 the array of representing the first vector
     * @param vector2 the array of representing the second vector
     *
     * @return output the vector sum of the two vectors
     */
    public static double[] elementAdd(double[] vector1, double[] vector2) {
        double[] output = new double[vector1.length];
        for (int i = 0; i < vector1.length; i++ ) {
            output[i] = vector1[i] + vector2[i];
        }
        return output;
    }
    
    /**
     * Computes the vector difference of two arrays of doubles, i.e.
     * vector1 - vector2
     *
     * @param vector1 the array of representing the first vector
     * @param vector2 the array of representing the second vector
     *
     * @return output the vector sum of the two vectors
     */
    public static double[] elementSubtract(double[] vector1, double[] vector2) {
        double[] output = new double[vector1.length];
        for (int i = 0; i < vector1.length; i++ ) {
            output[i] = vector1[i] - vector2[i];
        }
        return output;
    }
    
    /**
     * Computes the element by element product of two arrays of doubles
     * i.e. vector1 .* vector2
     *
     * @param vector1 the array of representing the first vector
     * @param vector2 the array of representing the second vector
     *
     * @return output the vector of element multplies of the inputs
     */
    public static double[] elementMultiply(double[] vector1, double[] vector2) {
        double[] output = new double[vector1.length];
        for (int i = 0; i < vector1.length; i++ ) {
            output[i] = vector1[i] * vector2[i];
        }
        return output;
    }
    
    /**
     * Computes the element by element division of two arrays of doubles
     * i.e. vector1 ./ vector2
     *
     * @param numerator the array of representing the first vector
     * @param divisor the array of representing the divisor vector
     *
     * @return output the vector of element multplies of the inputs
     */
    public static double[] elementDivide(double[] numerator, double[] divisor) {
        double[] output = new double[numerator.length];
        for (int i = 0; i < numerator.length; i++ ) {
            output[i] = numerator[i] / divisor[i];
        }
        return output;
    }
    
    /**
     * Calcualte the value of a sign function.
     *
     * @param in independent variable of the sign function
     *
     * @return 1.0 if the independent variable is greater or equal than 0
     * 		 -1.0 otherwise
     */
    public static double signum(double in) {
        double out;
        if (in >= 0.0)
            out = 1.0;
        else
            out = -1.0;
        return out;
    }
    
    /**
     * Counts the number of zero crossings in the input array
     *
     * @param input the array for which the zero crossings are counted
     *
     * @return output the number of zero crossings
     */
    public static double zeroCrossings(double[] input) {
        double output = 0.0;
        for (int i = 1; i < input.length; i++) {
            output += Math.abs( signum(input[i]) - signum(input[i-1]) );
        }
        output = output/2.0;
        return output;
    }
    
    /**
     * Computes the cosine distance between two arrays/vectors
     *
     * @param arr1 the first array/vector 
     * @param arr2 the second array/vector
     *
     * @return dist the cosine distance
     */
    public static double cosineDist(double[] arr1, double[] arr2) {
        double arr1RootSquareSum = 0.0;
        for (int j = 0; j < arr1.length; j++) {
            arr1RootSquareSum += arr1[j] * arr1[j];
        }
        arr1RootSquareSum = Math.sqrt(arr1RootSquareSum);
        
        double prodSum = 0.0;
        double arr2Sum = 0.0;
        for (int j = 0; j < arr1.length; j++) {
            prodSum += arr1[j] * arr2[j];
            arr2Sum += arr2[j] * arr2[j];
        }
        double dist = 1.0 - (prodSum / arr1RootSquareSum * Math.sqrt(arr2Sum));
        return dist;
    }
    
    /**
     * Computes the Euclidean distance between two arrays/vectors
     *
     * @param arr1 the first array/vector 
     * @param arr2 the second array/vector
     *
     * @return dist the Euclidean distance
     */
    public static double euclideanDist(double[] arr1, double[] arr2) {
        double squareSum = 0.0;
        for (int j = 0; j < arr1.length; j++) {
            squareSum += Math.pow((arr1[j] - arr2[j]),2);
        }
        double dist = Math.sqrt(squareSum);
        return dist;
    }
    
    /**
     * Computes the entropy of an array using a histogram to compute probabilities
     *
     * @param input the array for which to calculate the entropy
     * @param histogramBins the number of histogram bins to use in calculation
     *
     * @return dist the entropy of the array
     */
    public static double entropy(double[] input, int histogramBins) {
        int array1NumValues = input.length;
        int[] hist = new int[histogramBins];
        double[] upperBounds = new double[histogramBins+1];
        double maxVal = 0.0, minVal = 0.0;
        
        for (int i = 0; i < array1NumValues; i++ ) {
            if (input[i] > maxVal) {
                maxVal = input[i];
            } else if (input[i] < minVal) {
                minVal = input[i];
            }
        }
        double range = maxVal - minVal;
        
        for (int i = 0; i <= (histogramBins); i++ ) {
            upperBounds[i] = minVal + ((range/(double)histogramBins)*(double)i);    
        }
        
        for (int i = 0; i < array1NumValues; i++ ) {
            for (int j = 0; j < (histogramBins); j++ ) {
                if (input[i] <= upperBounds[j+1]) {
                    hist[j]++;
                    break;
                }
            }
        }
        
        double entropy = 0.0;
        for (int j = 0; j < (histogramBins); j++ ) {
            if (hist[j] > 0) {
                double prob = (double)hist[j] / (double)array1NumValues;
                entropy += (prob * Math.log(prob));
            }
        }
        entropy = -entropy;
        return entropy;
    }    
    
    /**
     * Computes the entropy of an int array using a histogram to compute probabilities
     *
     * @param input the array for which to calculate the entropy
     * @param histogramBins the number of histogram bins to use in calculation
     *
     * @return dist the entropy of the array
     */
    public static double entropy(int[] input, int histogramBins) {
        int array1NumValues = input.length;
        int[] hist = new int[histogramBins];
        int[] upperBounds = new int[histogramBins+1];
        int maxVal = 0, minVal = 0;
        
        for (int i = 0; i < array1NumValues; i++ ) {
            if (input[i] > maxVal) {
                maxVal = input[i];
            } else if (input[i] < minVal) {
                minVal = input[i];
            }
        }
        int range = maxVal - minVal;
        
        for (int i = 0; i <= (histogramBins); i++ ) {
            upperBounds[i] = (int)Math.round(minVal + ((range)/(double)histogramBins)*(double)i);    
        }
        
        for (int i = 0; i < array1NumValues; i++ ) {
            for (int j = 0; j < (histogramBins); j++ ) {
                if (input[i] <= upperBounds[j+1]) {
                    hist[j]++;
                    break;
                }
            }
        }
        
        double entropy = 0.0;
        for (int j = 0; j < (histogramBins); j++ ) {
            if (hist[j] > 0) {
                double prob = (double)hist[j] / (double)array1NumValues;
                entropy += (prob * Math.log(prob));
            }
        }
        entropy = -entropy;
        
        return entropy;
    }    
}
