package org.imirsel.nema.analytics.evaluation;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.imirsel.nema.analytics.evaluation.chord.ChordIntervalTextFile;
import org.imirsel.nema.analytics.evaluation.chord.ChordNumberTextFile;
import org.imirsel.nema.analytics.evaluation.chord.ChordShortHandTextFile;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationTextFile;
import org.imirsel.nema.analytics.evaluation.key.KeyTextFile;
import org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile;
import org.imirsel.nema.model.NemaDataConstants;

/**
 * File type conversion utility. Can be used to list the available (registered)
 * file types for each metadata key, convert NemaData Objects to the specified
 * file format (to be written to a specified folder and File handle returned) or
 * to read a specified File and return NemaData Objects representing the 
 * contents.
 * 
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class EvalFileUtil {

	public static final Map<String,List<Class<? extends EvalFileType>>> LIST_FILE_TYPE_REGISTRY = new HashMap<String, List<Class<? extends EvalFileType>>>();
	public static final Map<String,List<Class<? extends EvalFileType>>> GT_AND_PREDICTION_FILE_TYPE_REGISTRY = new HashMap<String, List<Class<? extends EvalFileType>>>();	
	static {
		//register known list file types for known metadata keys
		{
			List<Class<? extends EvalFileType>> trackListTypeList = new ArrayList<Class<? extends EvalFileType>>(1);
			trackListTypeList.add(TrackListFile.class);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, trackListTypeList);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.MELODY_EXTRACTION_DATA, trackListTypeList);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.KEY_DETECTION_DATA, trackListTypeList);
			
				//classification tasks
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ALBUM, trackListTypeList);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ARTIST, trackListTypeList);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_TITLE, trackListTypeList);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_GENRE, trackListTypeList);
			LIST_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_COMPOSER, trackListTypeList);
		}
		
		//register known GT and prediction file types for known metadata keys
		{
			List<Class<? extends EvalFileType>> gtTypeList = new ArrayList<Class<? extends EvalFileType>>(3);
			gtTypeList.add(ChordIntervalTextFile.class);
			gtTypeList.add(ChordNumberTextFile.class);
			gtTypeList.add(ChordShortHandTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, gtTypeList);
			
			gtTypeList = new ArrayList<Class<? extends EvalFileType>>(1);
			gtTypeList.add(MelodyTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.MELODY_EXTRACTION_DATA, gtTypeList);

			gtTypeList = new ArrayList<Class<? extends EvalFileType>>(1);
			gtTypeList.add(KeyTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.KEY_DETECTION_DATA, gtTypeList);
			
				//classification tasks
			gtTypeList = new ArrayList<Class<? extends EvalFileType>>(1);
			gtTypeList.add(ClassificationTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ALBUM, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ARTIST, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_TITLE, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_GENRE, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_COMPOSER, gtTypeList);
		}
	}
	
	/**
	 * Returns a List of EvalFileType implementations that may be used to read
	 * and write list files for the specified metadata key.
	 * @param metadataKey The metadata type to retrieve file types for.
	 * @return a List of EvalFileType implementations.
	 */
	public List<Class<? extends EvalFileType>> getListFileTypes(String metadataKey){
		return LIST_FILE_TYPE_REGISTRY.get(metadataKey);
	}
	
	/**
	 * Returns a list of EvalFileType implementations that may be used to read
	 * and write ground-truth or prediction files for the specified metadata 
	 * key.
	 * @param metadataKey The metadata type to retrieve file types for.
	 * @return a List of EvalFileType implementations.
	 */
	public List<Class<? extends EvalFileType>> getGTAndPredictionFileTypes(String metadataKey){
		return GT_AND_PREDICTION_FILE_TYPE_REGISTRY.get(metadataKey);
	}
	
	
}
