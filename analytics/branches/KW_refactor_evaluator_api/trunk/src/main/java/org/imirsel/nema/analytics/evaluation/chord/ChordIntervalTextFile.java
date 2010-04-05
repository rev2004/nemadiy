/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.chord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.imirsel.nema.model.*;
import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.analytics.util.*;
import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;


/**
 *
 * @author kris.west@gmail.com
 */
public class ChordIntervalTextFile extends SingleTrackEvalFileTypeImpl {

	public static final String READ_DELIMITER = "\\s+";
	public static final String WRITE_DELIMITER = "\t";	
	
	@Override
	public NemaData readFile(File theFile)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		
		String[][] chordStringsData = DeliminatedTextFileUtilities.loadDelimTextData(theFile, READ_DELIMITER, -1);
		
		// Convert the data to a 2D double array
		int nrows = chordStringsData.length;
		List<NemaChord> chords = new ArrayList<NemaChord>(nrows);
		
		double onset, offset;
		String interval;
		int[] notes;
		for(int r = 0; r < nrows; r++) {
			onset = Double.parseDouble(chordStringsData[r][0]);
			offset = Double.parseDouble(chordStringsData[r][1]);
			interval = chordStringsData[r][2];
			notes = ChordConversionUtil.getInstance().convertIntervalsToNotenumbers(interval);
			chords.add(new NemaChord(onset, offset, notes));
		}
		
		// Form the NemaData Object for this file and return as a length-1 list
		NemaData obj = new NemaData(PathAndTagCleaner.convertFileToMIREX_ID(theFile));
		obj.setMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE, chords);
		return obj;
	}
	
	@Override
	public void writeFile(File theFile, NemaData data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		//TODO implement me
		BufferedWriter writer = null;
		
		try{
			List<NemaChord> chords = null;
			try{
				Object obj = data.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
				chords = (List<NemaChord>)obj;
			}catch(Exception e){
				throw new IllegalArgumentException("Failed to retrieve chords from: " + data.getId()); 
			}
			writer = new BufferedWriter(new FileWriter(theFile));
			
			NemaChord nemaChord;
			for (Iterator<NemaChord> it = chords.iterator(); it.hasNext();) {
				nemaChord = it.next();
				writer.write(nemaChord.onset + WRITE_DELIMITER + nemaChord.offset + WRITE_DELIMITER + ChordConversionUtil.getInstance().convertNotenumbersToIntervals(nemaChord.notes) + "\n");
			}
			getLogger().info(NemaDataConstants.CHORD_LABEL_SEQUENCE + " metadata for " + data.getId() + " written to file: " + theFile.getAbsolutePath());
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