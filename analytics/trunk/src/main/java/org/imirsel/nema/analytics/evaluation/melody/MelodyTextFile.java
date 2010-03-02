package org.imirsel.nema.analytics.evaluation.melody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileTypeImpl;
import org.imirsel.nema.analytics.util.PathAndTagCleaner;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;



public class MelodyTextFile extends SingleTrackEvalFileTypeImpl {

	public static final String READ_DELIMITER = "\t";
	public static final String WRITE_DELIMITER = "\t";
	public static final DecimalFormat TIMESTAMP_DEC = new DecimalFormat("0.0000");
	public static final DecimalFormat F0_DEC = new DecimalFormat("0.00");
	private static final double TIMEINC = 0.01;

	public MelodyTextFile() {
		super();
	}

	@Override
	public NemaData readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		// Read a space-delimited melody text file as a 2D string array
		String[][] melodyDataStrArray = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);
		// Convert the data to a 2D double array
		int nrows = melodyDataStrArray.length;
		int ncols = melodyDataStrArray[0].length;
		double[][] melodyDataRaw = new double[nrows][ncols];
		for(int r = 0; r < nrows; r++) {
			for(int c = 0; c < ncols; c++) {
				melodyDataRaw[r][c] = Double.valueOf(melodyDataStrArray[r][c]);
			}
		}
		
		// Set up the 0th-order interpolation to convert to the MIREX-spec 10ms time-grid 
		List<Double> melodyInterpTimeStamp = new ArrayList<Double>();
		List<Double> melodyInterpF0 = new ArrayList<Double>();
		melodyInterpF0.add(0, new Double(0.0));
    	melodyInterpTimeStamp.add(0, new Double(0.0));
		
    	// Begin interpolation
        int index = 0;
        int oldindex = -1;
        double minDiff = 10000000.0;
        for (int i = 0; i < nrows; i++) {
            index = (int)Math.round(melodyDataRaw[i][0]/TIMEINC);
            // Case where the file's time-step is less than 10ms
            if (index == oldindex) {
                double currDiff = Math.abs(melodyDataRaw[i][0] - TIMEINC*(double)index);
                if (currDiff < minDiff) {	
                	melodyInterpF0.set(index, new Double(melodyDataRaw[i][1]));
                	melodyInterpTimeStamp.set(index, new Double(TIMEINC*(double)index));
                    minDiff = currDiff;
                }
            }
         	// Case where the file's time-step is 10ms or has 'caught up' if less than 10ms
            else if (index == oldindex + 1) {
            	melodyInterpF0.add(new Double(melodyDataRaw[i][1]));
            	melodyInterpTimeStamp.add(new Double(TIMEINC*(double)index));
                minDiff = Math.abs(melodyDataRaw[i][0] - TIMEINC*(double)index);
            }
            // Case where the file's time-step is greater than 10ms, and the sample-hold takes place
            else if (index > oldindex + 1) {
                int indDiff = index - oldindex;
                for (int j = 0; j < indDiff-1; j++) {
                	melodyInterpF0.add(melodyInterpF0.get(oldindex));
                	melodyInterpTimeStamp.add(new Double(TIMEINC*(double)oldindex + (double)(j+1)*TIMEINC));
                }
                melodyInterpF0.add(new Double(melodyDataRaw[i][1]));
                melodyInterpTimeStamp.add(new Double(TIMEINC*(double)index));
                minDiff = Math.abs(melodyDataRaw[i][0] - TIMEINC*(double)index);
            }
            oldindex = index;                                
        }   

        double[][] melodyDataInterpolated = new double[melodyInterpF0.size()][2];
        for (int i = 0; i < melodyDataInterpolated.length; i++) {
        	melodyDataInterpolated[i][0] = (melodyInterpTimeStamp.get(i)).doubleValue();
        	melodyDataInterpolated[i][1] = (melodyInterpF0.get(i)).doubleValue();
        }
		
		// Form the NemaData Object for this file and return as a length-1 list
		NemaData obj = new NemaData(PathAndTagCleaner.convertFileToMIREX_ID(theFile));
		obj.setMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA, melodyDataInterpolated);
		return obj;
	}

	@Override
	public void writeFile(File theFile, NemaData data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		
		double[][] melodyData = data.get2dDoubleArrayMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA);
		
		// Convert the data to a 2D double array
		int nrows = melodyData.length;

		String[][] melodyDataStrArray = new String[nrows][2];
		try{
			for(int r = 0; r < nrows; r++) {		
				melodyDataStrArray[r][0] = TIMESTAMP_DEC.format(melodyData[r][0]);
				melodyDataStrArray[r][1] = F0_DEC.format(melodyData[r][1]);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException("Track " + data.getId() + " should have a double[N][2] array for metadata" +
					" type '" + NemaDataConstants.MELODY_EXTRACTION_DATA + "', number of columns is wrong" ,e);
		}
		DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(theFile, WRITE_DELIMITER, melodyDataStrArray);
	}

}
