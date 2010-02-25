package org.imirsel.nema.analytics.evaluation.melody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.EvalFileTypeImpl;
import org.imirsel.nema.analytics.util.PathAndTagCleaner;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;



public class MelodyTextFile extends EvalFileTypeImpl {

	public static final String READ_DELIMITER = "\t";
	public static final String WRITE_DELIMITER = "\t";
	public static final DecimalFormat TIMESTAMP_DEC = new DecimalFormat("0.0000");
	public static final DecimalFormat F0_DEC = new DecimalFormat("0.00");

	public MelodyTextFile() {
		super();
	}

	@Override
	public List<NemaData> readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		// Read a space-delimited melody text file as a 2D string array
		String[][] melodyDataStrArray = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);
		// Convert the data to a 2D double array
		int nrows = melodyDataStrArray.length;
		int ncols = melodyDataStrArray[0].length;
		double[][] melodyData = new double[nrows][ncols];
		for(int r = 0; r < nrows; r++) {
			for(int c = 0; c < ncols; c++) {
				melodyData[r][c] = Double.valueOf(melodyDataStrArray[r][c]);
			}
		}
		// Form the NemaData Object for this file and return as a length-1 list
		List<NemaData> out = new ArrayList<NemaData>(1);
		NemaData obj = new NemaData(PathAndTagCleaner.convertFileToMIREX_ID(theFile));
		obj.setMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA, melodyData);
		out.add(obj);
		return out;
	}

	@Override
	public void writeFile(File theFile, List<NemaData> data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		if (data.size() != 1) {
			throw new IllegalArgumentException("Received data List size greater than 1. Was expecting ..." +
					"length 1 (single file");
		}
		double[][] melodyData = data.get(0).get2dDoubleArrayMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA);
		
		// Convert the data to a 2D double array
		int nrows = melodyData.length;

		String[][] melodyDataStrArray = new String[nrows][2];
		try{
			for(int r = 0; r < nrows; r++) {		
				melodyDataStrArray[r][0] = TIMESTAMP_DEC.format(melodyData[r][0]);
				melodyDataStrArray[r][1] = F0_DEC.format(melodyData[r][1]);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException("Track " + data.get(0).getId() + " should have a double[N][2] array for metadata" +
					" type '" + NemaDataConstants.MELODY_EXTRACTION_DATA + "', number of columns is wrong" ,e);
		}
		DeliminatedTextFileUtilities.writeStringDataToDelimTextFile(theFile, WRITE_DELIMITER, melodyDataStrArray);
	}

}
