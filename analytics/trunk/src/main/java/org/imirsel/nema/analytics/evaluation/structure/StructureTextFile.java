package org.imirsel.nema.analytics.evaluation.structure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileTypeImpl;
import org.imirsel.nema.analytics.evaluation.chord.ChordConversionUtil;
import org.imirsel.nema.analytics.evaluation.chord.NemaChord;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.analytics.util.io.PathAndTagCleaner;
import org.imirsel.nema.model.NemaDataConstants;

public class StructureTextFile extends SingleTrackEvalFileTypeImpl {

	public static final String READ_DELIMITER = "\\s+";
	public static final String WRITE_DELIMITER = "\t";
	public static final DecimalFormat STRUCT_DEC = new DecimalFormat("0.000");
	public static final String TYPE_NAME = "Structural segmentation text file";

	/**
	 * Constructor
	 */
	public StructureTextFile() {
		super(TYPE_NAME);
	}

	@Override
	public NemaData readFile(File theFile) throws IllegalArgumentException,
	FileNotFoundException, IOException {

		/* Read a space-delimited key text file as a 2D string array (should have just 1 row, 2 columns)*/
		String[][] structDataStrArray = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);

		/* Check that the text file is of proper format: <tempo1>\t<tempo2>\t<salience>\n<EOF> */

		double onset, offset;
		String label;

		int nrows = structDataStrArray.length;
		List<NemaSegment> segments = new ArrayList<NemaSegment>(nrows);

		for(int r = 0; r < nrows; r++) {
			if (structDataStrArray[r].length != 3) {
				String msg = "This file could not be parsed into separate Onset, Offset, Label fields! " +
				"Format should be <Onset>\t<Offset>\t<Label>\n. Content: \n";
				for (int i = 0; i < structDataStrArray[r].length; i++) {
					msg += "'" + structDataStrArray[r][i] + "'";
					if (i<structDataStrArray[r].length-1){
						msg += ",";
					}		
				}
				throw new IllegalArgumentException(msg);
			}
			onset = Double.parseDouble(structDataStrArray[r][0]);
			offset = Double.parseDouble(structDataStrArray[r][1]);
			label = structDataStrArray[r][2];

			segments.add(new NemaSegment(onset, offset, label));
		}

		/* Fill the NemaData object with the proper data and return it*/
		NemaData obj = new NemaData(PathAndTagCleaner.convertFileToMIREX_ID(theFile));
		obj.setMetadata(NemaDataConstants.STRUCTURE_SEGMENTATION_DATA, segments);
		return obj;
	}

	@Override
	public void writeFile(File theFile, NemaData data)
	throws IllegalArgumentException, FileNotFoundException, IOException {

		BufferedWriter writer = null;

		try{
			List<NemaSegment> segments = null;
			try{
				Object obj = data.getMetadata(NemaDataConstants.STRUCTURE_SEGMENTATION_DATA);
				segments = (List<NemaSegment>)obj;
			}catch(Exception e){
				throw new IllegalArgumentException("Failed to retrieve segments from: " + data.getId()); 
			}
			writer = new BufferedWriter(new FileWriter(theFile));

			NemaSegment nemaSegment;
			for (Iterator<NemaSegment> it = segments.iterator(); it.hasNext();) {
				nemaSegment = it.next();
				writer.write(nemaSegment.toString() + "\n");
			}
			getLogger().info(NemaDataConstants.STRUCTURE_SEGMENTATION_DATA + " metadata for " + data.getId() + " written to file: " + theFile.getAbsolutePath());
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					getLogger().log(Level.SEVERE, null, ex);
				}
			}
		}

	}

}
