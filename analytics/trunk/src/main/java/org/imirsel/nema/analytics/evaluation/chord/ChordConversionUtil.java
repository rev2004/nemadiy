package org.imirsel.nema.analytics.evaluation.chord;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Handler;

import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;

public class ChordConversionUtil {
	
	private static Map<String,int[]> shorthandToNoteNumbers;
	private static Map<String,String> noteNumbersToShorthand;
	
	private static Map<String,int[]> intervalsToNoteNumbers;
	private static Map<String,String> NoteNumbersToIntervals;
	
	private static Map<String,int[]> chordNumberToNoteNumbers;
	private static Map<String,String> NoteNumbersToChordNumbers;
	
	public static final String INTERVAL_DICTIONARY_CLASSPATH = "/org/imirsel/nema/analytics/evaluation/chord/IntervalDictionary.txt";
	public static final String SHORTHAND_DICTIONARY_CLASSPATH = "/org/imirsel/nema/analytics/evaluation/chord/IntervalDictionary.txt";
	public static final String CHORDNUMBERS_DICTIONARY_CLASSPATH = "/org/imirsel/nema/analytics/evaluation/chord/IntervalDictionary.txt";

	static{
		//read dictionaries
		
		try{
			intervalsToNoteNumbers = readChordDictionary(INTERVAL_DICTIONARY_CLASSPATH);
			NoteNumbersToIntervals = reverseMap(intervalsToNoteNumbers);
			
			shorthandToNoteNumbers = readChordDictionary(SHORTHAND_DICTIONARY_CLASSPATH);
			noteNumbersToShorthand = reverseMap(shorthandToNoteNumbers);
			
			chordNumberToNoteNumbers = readChordDictionary(CHORDNUMBERS_DICTIONARY_CLASSPATH);
			NoteNumbersToChordNumbers = reverseMap(chordNumberToNoteNumbers);
			
			
		}catch(Exception e){
			throw new IllegalArgumentException("Failed to read chord dictionary from classpath: " + INTERVAL_DICTIONARY_CLASSPATH);
		}
		
		
		
		
		
	}

	
	public static int[] convertIntervalsToNotenumbers(String intervalString) throws IllegalArgumentException{
		int[] out = intervalsToNoteNumbers.get(intervalString);
		if (out == null){
			throw new IllegalArgumentException("Could not interpret '" + intervalString + "' as notes!");
		}		
		return out; 
	}
	
	public static String convertNotenumbersToIntervals(int[] notes) throws IllegalArgumentException{
		String key = createKey(notes);
		String out = NoteNumbersToIntervals.get(key);
		if (out == null){
			String msg = "Could not interpret notes: " + notes[0];
			for (int i = 1; i < notes.length; i++) {
				msg+= "," + notes[i];
			}
			msg += " as an interval!";
			throw new IllegalArgumentException(msg);
		}	
		return out;
	}

	
	public static int[] convertShorthandToNotenumbers(String shorthandChordLabel){
		int[] out = shorthandToNoteNumbers.get(shorthandChordLabel);
		if (out == null){
			throw new IllegalArgumentException("Could not interpret '" + shorthandChordLabel + "' as notes!");
		}		
		return out; 
	}
	
	public static String convertNoteNumbersToShorthand(int[] notes){
		String key = createKey(notes);
		String out = NoteNumbersToIntervals.get(key);
		if (out == null){
			String msg = "Could not interpret notes: " + notes[0];
			for (int i = 1; i < notes.length; i++) {
				msg+= "," + notes[i];
			}
			msg += " as an interval!";
			throw new IllegalArgumentException(msg);
		}	
		return out;		
	}
	
	
	
	
	public static int[] convertChordNumbersToNoteNumbers(String no){
		int[] out = chordNumberToNoteNumbers.get(no) ;
		if (out == null){
			throw new IllegalArgumentException("Could not interpret '" + no + "' as notes!");
		}		
		return out; 		
	}
	public static String convertNotenumbersToChordnumbers(int[] notes){
		
		String key = createKey(notes);
		String out = NoteNumbersToChordNumbers.get(key);
		if (out == null){
			String msg = "Could not interpret notes: " + notes[0];
			for (int i = 1; i < notes.length; i++) {
				msg+= "," + notes[i];
			}
			msg += " as an interval!";
			throw new IllegalArgumentException(msg);
		}	
		return out;		
		
		
	}
	
	
	
	
	
	
	
	private static Map<String,int[]> readChordDictionary(String classpath) throws IOException{
		String[][] chordMappings = DeliminatedTextFileUtilities.loadDelimTextData(classpath, ",", -1);
		try{
			int nrows = chordMappings.length;
			Map<String,int[]> map = new HashMap<String, int[]>(nrows);
			for(int r = 0; r < nrows; r++) {
				String key = chordMappings[r][0];
				String val = chordMappings[r][1];
				String[] comps = val.split("\\s+");
				int[] valArray = new int[comps.length];
				for (int i = 0; i < valArray.length; i++) {
					valArray[i] = Integer.valueOf(comps[i]);
				}
				map.put(key, valArray);
			}
			return map;
		}catch(Exception e){
			throw new IllegalArgumentException("Failed to read chord dictionary from classpath: " + classpath + "\nPlease check the file format.");
		}
	}	
	
	private static String createKey(int[] arr){
		try{
			String key = "" + arr[0];
			for (int i = 1; i < arr.length; i++) {
				key += "|" + arr[i];
			}
			return key;
		}catch(Exception e){
			throw new IllegalArgumentException("Received null or zero length array of notes to create key from!");
		}
	}
	
	private static Map<String,String> reverseMap(Map<String,int[]> map){
		Map<String,String> revMap = new HashMap<String, String>();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			int[] val = map.get(key);
			String newKey = createKey(val);
			revMap.put(newKey, key);
		}
		return revMap;
	}
}
