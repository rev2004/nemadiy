package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
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
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;

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

	public static final Map<String,List<Class<? extends EvalFileType>>> TEST_INPUT_FILE_TYPE_REGISTRY = new HashMap<String, List<Class<? extends EvalFileType>>>();
	public static final Map<String,List<Class<? extends EvalFileType>>> GT_AND_PREDICTION_FILE_TYPE_REGISTRY = new HashMap<String, List<Class<? extends EvalFileType>>>();	
	static {
		//register known list file types for known metadata keys
		{
			List<Class<? extends EvalFileType>> rawAudioTypeList = new ArrayList<Class<? extends EvalFileType>>(1);
			rawAudioTypeList.add(RawAudioFile.class);
			//these are tasks where individual audio files are used as input... i.e. there is no list file
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, rawAudioTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.MELODY_EXTRACTION_DATA, rawAudioTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.KEY_DETECTION_DATA, rawAudioTypeList);
			
				//classification tasks
			List<Class<? extends EvalFileType>> trackListTypeList = new ArrayList<Class<? extends EvalFileType>>(1);
			trackListTypeList.add(TrackListTextFile.class);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ALBUM, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ARTIST, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_TITLE, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_GENRE, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_COMPOSER, trackListTypeList);
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
	 * and write test input files for the specified metadata key. The file type
	 * <code>RawAudioFile</code> indicates that the raw audio file should be 
	 * used as an input, which does not need to be processed by the actual 
	 * <code>RawAudioFile</code> file type class.
	 * @param metadataKey The metadata type to retrieve file types for.
	 * @return a List of EvalFileType implementations.
	 */
	public static List<Class<? extends EvalFileType>> getTestInputFileTypes(String metadataKey){
		return TEST_INPUT_FILE_TYPE_REGISTRY.get(metadataKey);
	}
	
	/**
	 * Returns a list of EvalFileType implementations that may be used to read
	 * and write ground-truth or prediction files for the specified metadata 
	 * key.
	 * @param metadataKey The metadata type to retrieve file types for.
	 * @return a List of EvalFileType implementations.
	 */
	public static List<Class<? extends EvalFileType>> getGTAndPredictionFileTypes(String metadataKey){
		return GT_AND_PREDICTION_FILE_TYPE_REGISTRY.get(metadataKey);
	}
	
//	public static List<File> prepareExperimentRun(){
//		
//	}
//	
//	
//	public static Map<NemaTrackList,List<NemaData>> processResultsOfExperimentRun(){
//		
//	}
	
	/**
	 * Writes the list of ground-truth NemaData Objects to either a single file or 
	 * a directory of files (as appropriate to the file type). I.e. if the file
	 * type encodes data about multiple NemaData Objects in a single file the 
	 * path to the file written is returned. If the file type encodes data about 
	 * a single NemaData Object per file then a directory is created, files are 
	 * named using the the ID of each NemaData Object, written to the directory
	 * created and the path to the directory returned.
	 * 
	 * Note: if multiple files are written then it is likely that any extraction
	 * or estimation run on these files should be executed once per File as no 
	 * list file is written.
	 * 
	 * @param fold The track list being written out, used to name the output 
	 * file or directory.
	 * @param data The List of NemaData Objects to write.
	 * @param task The task that the data relates to (primarily used to type 
	 * ClassificationEvaluator.
	 * @param fileType The file type to use to write the ground-truth files.
	 * @param outputDirectory The directory to create the output in.
	 * @return A File representing the file or directory written out.
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * EvalFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 */
	public static File writeGroundTruthDataFileOrDirectory(NemaTrackList fold, List<NemaData> data, NemaTask task, Class<? extends EvalFileType> fileType, File outputDirectory) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
		//mint a file or directory path
		File outputLocation = new File(outputDirectory.getAbsolutePath() + File.separator + "train-set-" + fold.getId());
		if (SingleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			SingleTrackEvalFileType typeInstance = (SingleTrackEvalFileType)fileType.newInstance();
			//create dir to store files
			outputLocation.mkdirs();
			//write each file using id as name
			for (Iterator<NemaData> iterator = data.iterator(); iterator.hasNext();) {
				NemaData nemaData = iterator.next();
				File file = new File(outputLocation.getAbsolutePath() + File.separator + nemaData.getId() + typeInstance.getFilenameExtension());
				typeInstance.writeFile(file, nemaData);
			}
			//return dir created
			return outputLocation;
		}else if(MultipleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			//write a single file
			MultipleTrackEvalFileType typeInstance = (MultipleTrackEvalFileType)fileType.newInstance();
			outputLocation = new File(outputLocation.getAbsolutePath() + typeInstance.getFilenameExtension());
			//set type for classification evaluator
			if (ClassificationTextFile.class.equals(fileType)) {
				((ClassificationTextFile)typeInstance).setMetadataType(task.getSubjectTrackMetadataName());
			}
			//write single file and return
			typeInstance.writeFile(outputLocation, data);
			return outputLocation;
		}else {
			throw new IllegalArgumentException("Unrecognized file type: " + fileType.getName());
		}
	}
	

	/**
	 * Retrieves a A Map of NemaTrackList to a List of File Objects representing
	 * the resources.
	 * 
	 * Note: most, perhaps all, tasks which use file formats encoding data about
	 * a single track take a file at a time as input (i.e. they don't use a list 
	 * file). 
	 * 
	 * @param testData A Map of NemaTrackList to a List of NemaData Objects 
	 * encoding the data to write to each file or directory.
	 * @param task The task that the data relates (only required for 
	 * classification file types).
	 * @param outputDirectory The directory to write the data files to.
	 * @return A Map of NemaTrackList to a List of File Objects representing
	 * the resources.
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * EvalFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 */
	public static Map<NemaTrackList,List<File>> getResourceFilesList(Map<NemaTrackList,List<NemaData>> testData, NemaTask task, File outputDirectory) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
		Map<NemaTrackList,List<File>> out = new HashMap<NemaTrackList,List<File>>();
		
		for (Iterator<NemaTrackList> iterator = testData.keySet().iterator(); iterator.hasNext();) {
			NemaTrackList testSet = iterator.next();
			List<NemaData> data = testData.get(testSet);
			List<File> paths = new ArrayList<File>(data.size());
			for (Iterator<NemaData> iterator2 = data.iterator(); iterator2
					.hasNext();) {
				paths.add(new File(iterator2.next().getStringMetadata(NemaDataConstants.PROP_FILE_LOCATION)));
			}
			out.put(testSet,paths);
		}
	
		return out;
	}
	
		
	/**
	 * Writes out a test file that encodes paths for more than one track per
	 * fold of the experiment. 
	 * 
	 * @param testData A Map of NemaTrackList to a List of NemaData Objects 
	 * encoding the data to write to each file or directory.
	 * @param task The task that the data relates (only required for 
	 * classification file types).
	 * @param fileType The file type to use to write the list files.
	 * @param outputDirectory The directory to write the data files to.
	 * @return 
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * EvalFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 */
	public static Map<NemaTrackList,File> writeTestListFiles(Map<NemaTrackList,List<NemaData>> testData, NemaTask task, Class<? extends MultipleTrackEvalFileType> fileType, File outputDirectory) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
		Map<NemaTrackList,File> out = new HashMap<NemaTrackList,File>();
		
		for (Iterator<NemaTrackList> iterator = testData.keySet().iterator(); iterator.hasNext();) {
			NemaTrackList testSet = iterator.next();
			List<NemaData> data = testData.get(testSet);
			File written = writeTestListFile(testSet,data,task,fileType,outputDirectory);
			out.put(testSet,written);
		}
	
		return out;
	}
	
	/**
	 * 
	 * @param testData a List of NemaData Objects 
	 * encoding the data to write to the file or directory.
	 * @param task The task that the data relates (only required for 
	 * classification file types).
	 * @param fileType The file type to use to write the list files.
	 * @param outputDirectory The directory to write the data files to.
	 * @return 
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * EvalFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 */
	public static File writeTestListFile(NemaTrackList testSet, List<NemaData> testData, NemaTask task, Class<? extends MultipleTrackEvalFileType> fileType, File outputDirectory) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
		if(MultipleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			MultipleTrackEvalFileType typeInstance = (MultipleTrackEvalFileType)fileType.newInstance();
			//set type for classification evaluator
			if (ClassificationTextFile.class.equals(fileType)) {
				((ClassificationTextFile)typeInstance).setMetadataType(task.getSubjectTrackMetadataName());
			}
			
			//mint some sort of unique file name
			File toWriteTo = new File(outputDirectory.getAbsolutePath() + File.separator + "test-set-" + testSet.getId());
			typeInstance.writeFile(toWriteTo, testData);
			return toWriteTo;
		}else {
			throw new IllegalArgumentException("Unrecognized file type for test data files: " + fileType.getName());
		}
		
	}

	/**
	 * 
	 * @param filesOrDirectoriesToRead Map of NemaTrackList (each representing
	 * a fold of the experiment) to a File Object representing a File or 
	 * directory (as appropriate to the file type) encoding the data to read.
	 * @param task The task that the data relates (only required for 
	 * classification file types).
	 * @param fileType The file type to use to read the files or directories.
	 * @return A Map of NemaTrackList to a List of NemaData Objects encoding the 
	 * data read from each file or directory.
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * EvalFileType is received.
	 * @throws InstantiationException Thrown if the file reader can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found.
	 * @throws IOException Thrown if there is a problem reading a file.
	 */
	public static Map<NemaTrackList,List<NemaData>> readData(Map<NemaTrackList,File> filesOrDirectoriesToRead, NemaTask task, Class<? extends EvalFileType> fileType) throws IllegalArgumentException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException{
		Map<NemaTrackList,List<NemaData>> out = new HashMap<NemaTrackList,List<NemaData>>(filesOrDirectoriesToRead.size());
		for (Iterator<NemaTrackList> iterator = filesOrDirectoriesToRead.keySet().iterator(); iterator.hasNext();) {
			NemaTrackList testSet = iterator.next();
			File toRead = filesOrDirectoriesToRead.get(testSet);
			List<NemaData> data = readData(toRead, task, fileType);
			out.put(testSet, data);
		}
		return out;
	}
	
	/**
	 * Reads data from either a file (for file formats that encode data about
	 * multiple files) or a directory (for file formats that encode data about
	 * a single track per file).
	 * 
	 * @param fileOrDirectoryToRead The file or directory to read.
	 * @param task The task that the data relates (only required for 
	 * classification evaluators).
	 * @param fileType The file type to use to read the file or directory.
	 * @return A List of NemaData Objects encoding the data read.
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * EvalFileType is received.
	 * @throws InstantiationException Thrown if the file reader can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if the file or directory cannot be 
	 * found.
	 * @throws IOException Thrown if there is a problem reading the file.
	 */
	public static List<NemaData> readData(File fileOrDirectoryToRead, NemaTask task, Class<? extends EvalFileType> fileType) throws IllegalArgumentException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException{
		if (SingleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			SingleTrackEvalFileType typeInstance = (SingleTrackEvalFileType)fileType.newInstance();
			return typeInstance.readDirectory(fileOrDirectoryToRead, null);
		}else if(MultipleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			MultipleTrackEvalFileType typeInstance = (MultipleTrackEvalFileType)fileType.newInstance();
			//set type for classification evaluator
			if (ClassificationTextFile.class.equals(fileType)) {
				((ClassificationTextFile)typeInstance).setMetadataType(task.getSubjectTrackMetadataName());
			}
			return typeInstance.readFile(fileOrDirectoryToRead);
		}else {
			throw new IllegalArgumentException("Unrecognized file type: " + fileType.getName());
		}
	}
		
	
	
}
