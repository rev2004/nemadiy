package org.imirsel.nema.analytics.evaluation.multif0;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileTypeImpl;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.analytics.util.io.PathAndTagCleaner;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;


/**
 * Melody (single F0) text file type.
 * 
 * @author afe405@gmail.com
 * @author kris.west@gmail.com
 * @since 0.1.0
 *
 */
public class MultiF0EstTextFile {//extends SingleTrackEvalFileTypeImpl {

	public static final String READ_DELIMITER = "\\s+";
	public static final String WRITE_DELIMITER = "\t";
	public static final DecimalFormat TIMESTAMP_DEC = new DecimalFormat("0.0000");
	public static final DecimalFormat F0_DEC = new DecimalFormat("0.00");
	public static final String TYPE_NAME = "Melody (single F0) text file";
	
	/**
	 * Constructor
	 */
	public MultiF0EstTextFile() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public String[][] readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {

		/* Read a space-delimited melody text file as a 2D string array */
		String[][] multiF0EstDataStrArray = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);
		return multiF0EstDataStrArray;
		/* Convert the 2D string data to a 2D double array */
//		int nrows = multiF0EstDataStrArray.length;
//		int ncols = 2;
//		double[][] multiF0EstRaw = new double[nrows][ncols];
//		for(int r = 0; r < nrows; r++) {
//			try{
//				for(int c = 0; c < ncols; c++) {
//					multiF0EstRaw[r][c] = Double.valueOf(multiF0EstDataStrArray[r][c]);
//				}
//			}catch(Exception e){
//				String msg = "Failed to parse line " + r + " of file " + theFile.getAbsolutePath() + "\n" +
//				"Content: \n";
//				for (int i = 0; i < multiF0EstDataStrArray[r].length; i++) {
//					msg += "'" + multiF0EstDataStrArray[r][i] + "'";
//					if (i<multiF0EstDataStrArray[r].length-1){
//						msg += ",";
//					}
//					
//				}
//				msg += "\n";
//				throw new IllegalArgumentException(msg,e);
//			}
//		}
		
	}

}
