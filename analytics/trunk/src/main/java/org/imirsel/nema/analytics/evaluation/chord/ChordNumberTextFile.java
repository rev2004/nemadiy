/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.chord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.imirsel.nema.analytics.evaluation.EvalFileTypeImpl;
import org.imirsel.nema.analytics.util.PathAndTagCleaner;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;


/**
 *
 * @author kris.west@gmail.com
 */
public class ChordNumberTextFile extends EvalFileTypeImpl {

	public static final String READ_DELIMITER = "\\s";
	public static final String WRITE_DELIMITER = "\t";	
	String type;
	public ChordNumberTextFile(String type) {
		this.type = type;
	}
	
	@Override
	public List<NemaData> readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		
		String[][] chordStringsData = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);
		
		// Convert the data to a 2D double array
		int nrows = chordStringsData.length;
		List<NemaChord> chords = new ArrayList<NemaChord>(nrows);
		
		double onset,offset;
		int no;
		int[] notes;
		for(int r = 0; r < nrows-1; r++) {
			onset = Double.parseDouble(chordStringsData[r][0]);
			offset = Double.parseDouble(chordStringsData[r+1][0]);
			no = Integer.parseInt(chordStringsData[r][1]);
			notes = ChordConversionUtil.convertChordNumbersToNoteNumbers(no);
			chords.add(new NemaChord(onset, offset, notes));
		}
		
		// Form the NemaData Object for this file and return as a length-1 list
		List<NemaData> out = new ArrayList<NemaData>(1);
		NemaData obj = new NemaData(PathAndTagCleaner.convertFileToMIREX_ID(theFile));
		obj.setMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE, chords);
		out.add(obj);
		return out;
	}
	
	@Override
	public void writeFile(File theFile, List<NemaData> data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		//TODO implement me
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(theFile));
			NemaData obj;
			for (Iterator<NemaData> it = data.iterator(); it.hasNext();) {
				obj = it.next();
				writer.write(obj.getStringMetadata(NemaDataConstants.PROP_ID) + WRITE_DELIMITER + obj.getStringMetadata(type));
				writer.newLine();
			}
			
			_logger.info(data.size() + " examples with " + type + " metadata written to file: " + theFile.getAbsolutePath());
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					_logger.log(Level.SEVERE, null, ex);
				}
			}
		}
	}    
}
