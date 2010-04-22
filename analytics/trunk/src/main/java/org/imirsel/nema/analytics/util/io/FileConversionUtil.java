package org.imirsel.nema.analytics.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.imirsel.nema.analytics.evaluation.MultipleTrackEvalFileType;
import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
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
 * These facilities are to be used to prepare data for use in the execution of 
 * external binary processes and to harvest the output from the same, whilst
 * preserving as much information as possible.
 * 
 * @author kris.west@gmail.com
 * @since 0.2.0
 */
public class FileConversionUtil {

	public static final Map<String,List<Class<? extends NemaFileType>>> TEST_INPUT_FILE_TYPE_REGISTRY = new HashMap<String, List<Class<? extends NemaFileType>>>();
	public static final Map<String,List<Class<? extends NemaFileType>>> GT_AND_PREDICTION_FILE_TYPE_REGISTRY = new HashMap<String, List<Class<? extends NemaFileType>>>();	
	static {
		//register known list file types for known metadata keys
		{
			//setup raw audio file input type
			List<Class<? extends NemaFileType>> rawAudioTypeList = new ArrayList<Class<? extends NemaFileType>>(4);
			rawAudioTypeList.add(RawAudioFile.class);
			//setup track list file input type
			List<Class<? extends NemaFileType>> trackListTypeList = new ArrayList<Class<? extends NemaFileType>>(5);
			trackListTypeList.add(TrackListTextFile.class);
			
			//these are tasks where individual audio files are used as input... i.e. there is no list file
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, rawAudioTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.MELODY_EXTRACTION_DATA, rawAudioTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.KEY_DETECTION_DATA, rawAudioTypeList);
			
			//chord can also use a list file
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, trackListTypeList);
			
			//classification tasks
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ALBUM, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ARTIST, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_TITLE, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_GENRE, trackListTypeList);
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_COMPOSER, trackListTypeList);
			
			//arguments that take opaque file formats (files we don't know how to read but can move around - e.g. model files)
			List<Class<? extends NemaFileType>> opaqueTypeList = new ArrayList<Class<? extends NemaFileType>>(1);
			opaqueTypeList.add(OpaqueFileFormat.class); 
			TEST_INPUT_FILE_TYPE_REGISTRY.put(NemaDataConstants.FILE_DATA, opaqueTypeList);
		}
		
		//register known GT and prediction file types for known metadata keys
		{
			//whether chord systemms return a file at a time or directory of files (which is auto detected) we need to know format
			List<Class<? extends NemaFileType>> gtTypeList = new ArrayList<Class<? extends NemaFileType>>(3);
			gtTypeList.add(ChordIntervalTextFile.class);
			gtTypeList.add(ChordNumberTextFile.class);
			gtTypeList.add(ChordShortHandTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, gtTypeList);
			
			gtTypeList = new ArrayList<Class<? extends NemaFileType>>(1);
			gtTypeList.add(MelodyTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.MELODY_EXTRACTION_DATA, gtTypeList);

			gtTypeList = new ArrayList<Class<? extends NemaFileType>>(1);
			gtTypeList.add(KeyTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.KEY_DETECTION_DATA, gtTypeList);
			
				//classification tasks
			gtTypeList = new ArrayList<Class<? extends NemaFileType>>(5);
			gtTypeList.add(ClassificationTextFile.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ALBUM, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ARTIST, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_TITLE, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_GENRE, gtTypeList);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.CLASSIFICATION_COMPOSER, gtTypeList);
			
			//arguments that produce opaque file formats (files we don't know how to read but can move around - e.g. model files)
			List<Class<? extends NemaFileType>> opaqueTypeList = new ArrayList<Class<? extends NemaFileType>>(1);
			opaqueTypeList.add(OpaqueFileFormat.class);
			GT_AND_PREDICTION_FILE_TYPE_REGISTRY.put(NemaDataConstants.FILE_DATA, opaqueTypeList);
		}
	}
	
	/**
	 * Returns a List of NemaFileType implementations that may be used to read
	 * and write test input files for the specified metadata key. The file type
	 * <code>RawAudioFile</code> indicates that the raw audio file should be 
	 * used as an input, which does not need to be processed by the actual 
	 * <code>RawAudioFile</code> file type class.
	 * @param metadataKey The metadata type to retrieve file types for.
	 * @return a List of NemaFileType implementations.
	 */
	public static List<Class<? extends NemaFileType>> getTestInputFileTypes(String metadataKey){
		return TEST_INPUT_FILE_TYPE_REGISTRY.get(metadataKey);
	}
	
	/**
	 * Returns a list of NemaFileType implementations that may be used to read
	 * and write ground-truth or prediction files for the specified metadata 
	 * key.
	 * @param metadataKey The metadata type to retrieve file types for.
	 * @return a List of NemaFileType implementations.
	 */
	public static List<Class<? extends NemaFileType>> getGTAndPredictionFileTypes(String metadataKey){
		return GT_AND_PREDICTION_FILE_TYPE_REGISTRY.get(metadataKey);
	}
	
	/**
	 * Given a task file format, output directory path, file type and a Map of 
	 * NemaTrackList to a List of NemaData Objects encoding the data relating
	 * to each fold of an experiment this method will prepare files necessary
	 * to encode the input data and return a List of File Objects to required 
	 * to perform each fold of the execution of an external process that works 
	 * with the specified file type.
	 * 
	 * Note: NemaData Objects must have been resolved to audio files prior to
	 * executing this method on them. This can be done using a
	 * <code>org.imirsel.nema.repository.RepositoryClientImpl</code> instance
	 * (from the nema-repository project) and the 
	 * <code>resolveTracksToFiles(List<NemaData> trackDataList, Set<NemaMetadataEntry> constraint)</code>
	 * method.
	 * 
	 * @param outputDirectory Path to write any files created to.
	 * @param task The NemaTask to be performed on the data.
	 * @param executionData Map of NemaTrackList to a List of NemaData Objects
	 * encoding the data to be worked on.
	 * @param fileType The file type to be used to prepare the data. Note that 
	 * the RawAudioFile type will cause the list of audio files to be written
	 * @return Map of NemaTrackList to a list of File Objects representing the 
	 * files to be used as inputs to the process.
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * NemaFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 * @throws IOException Thrown if there is a problem writing the files to
	 * disk.
	 */
	public static Map<NemaTrackList,List<File>> prepareProcessInput(
			File outputDirectory, 
			NemaTask task,
			Map<NemaTrackList,List<NemaData>> executionData, 
			Class<? extends NemaFileType> fileType
			) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
		
		Map<NemaTrackList,List<File>> out = null;
		if (SingleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			if(fileType.equals(RawAudioFile.class)) {
				//use raw audio files
				out = getResourceFilesList(executionData,task,outputDirectory);
				return out;
			}else {
				//write directory of metadata files - ignore isTrainingRun flag 
				  //as there is nothing else we can other than write the 
				  //specified file (e.g. QBT writes out timestamps of users 
				  //tap times which are used as queries - this may eventually 
				  //happen in other tasks)
				out = new HashMap<NemaTrackList,List<File>>(executionData.size());
				for (Iterator<NemaTrackList> iterator = executionData.keySet().iterator(); iterator
						.hasNext();) {
					NemaTrackList testSet = iterator.next();
					File dir = writeGroundTruthDataFileOrDirectory(testSet,executionData.get(testSet),task, fileType, outputDirectory);
					List<File> files = Arrays.asList(dir.listFiles());
					out.put(testSet, files);
				}
			}
		}else if(MultipleTrackEvalFileType.class.isAssignableFrom(fileType)) {
			out = new HashMap<NemaTrackList,List<File>>(executionData.size());
			for (Iterator<NemaTrackList> iterator = executionData.keySet().iterator(); iterator
			.hasNext();) {
				NemaTrackList testSet = iterator.next();
				File file = writeGroundTruthDataFileOrDirectory(testSet,executionData.get(testSet),task, fileType, outputDirectory);
				List<File> list = new ArrayList<File>(1);
				list.add(file);
				out.put(testSet, list);
			}
		}
		return out;
	}
	
	/**
	 * Based on a set of inputs that will be used to execute a process, a chosen
	 * output file format, an output directory and a file name extension to use,
	 * this method constructs output file or directory names to use to execute 
	 * the process.
	 * 
	 * The data-structure returned is the same as that used by the 
	 * <code>readProcessOutput</code> method and hence after being used to 
	 * execute the process, this structure maybe used to read the data files
	 * produced back in.
	 * 
	 * @param executionData The inputs that will be sent to the process (e.g.
	 * audio files to process, IDs of the test sets etc.).
	 * @param outputFileTypeInstance The file type to use to read the files or 
	 * directories.
	 * @param outputFileExt The extension to append to filenames created.
	 * @param outputDirectory The directory to create the output files in.
	 * @return Map of NemaTrackList to a list of File Objects representing the 
	 * files to be created.
	 */
	public static Map<NemaTrackList,List<File>> createOutputFileNames(
			Map<NemaTrackList,List<NemaData>> executionData, 
			Class<? extends NemaFileType> inputType,
			NemaFileType outputFileTypeInstance,
			String outputFileExt,
			File outputDirectory
			) {
		
		Map<NemaTrackList,List<File>> out = new HashMap<NemaTrackList,List<File>>(executionData.size());
		
		for (Iterator<NemaTrackList> iterator = executionData.keySet().iterator(); iterator.hasNext();) {
			
			List<File> list = null;
			NemaTrackList testSet = iterator.next();
			
			/*If the input has data on multiple tracks per file and the output 
			 * output file type contains data on a single track per file, we are 
			 * probably going to receive a directory of output files with 
			 * unspecified names, otherwise its one in one out).
			*/
			if(!MultipleTrackEvalFileType.class.isAssignableFrom(outputFileTypeInstance.getClass()) && 
					MultipleTrackEvalFileType.class.isAssignableFrom(inputType)){
				//directory
				File foldDir = new File(outputDirectory.getAbsolutePath() + File.separator +"set-" + testSet.getId());
				foldDir.mkdirs();
				list.add(foldDir);
				
			}else{
				//files
				List<NemaData> data = executionData.get(testSet);
				
				if (SingleTrackEvalFileType.class.isAssignableFrom(outputFileTypeInstance.getClass())) {
					//create directory of metadata or new raw audio files
					File foldDir = new File(outputDirectory.getAbsolutePath() + File.separator +"set-" + testSet.getId());
					foldDir.mkdirs();
					
					list = new ArrayList<File>(data.size());
					for (Iterator<NemaData> nemaDataIt = data.iterator(); nemaDataIt
							.hasNext();) {
						File fileLoc = new File(nemaDataIt.next().getStringMetadata(NemaDataConstants.PROP_FILE_LOCATION));
						String name = fileLoc.getName();
						File newPath = new File(foldDir.getAbsolutePath() + File.separator + name + outputFileExt + outputFileTypeInstance.getFilenameExtension());
						list.add(newPath);
					}
				}else if(MultipleTrackEvalFileType.class.isAssignableFrom(outputFileTypeInstance.getClass())) {
					//create one output file per fold
					list = new ArrayList<File>(1);
					File newPath = new File(outputDirectory.getAbsolutePath() + File.separator +"set-" + testSet.getId() + outputFileExt + outputFileTypeInstance.getFilenameExtension());
					list.add(newPath);
				}
			}
			
			
			
			out.put(testSet, list);
		}
		
		return out;
	}

	/**
	 * Reads the files output by a process using the specified file type. The
	 * first parameter is a Map of NemaTrackList (each representing
	 * a fold of the experiment) to a list of File Objects representing files or 
	 * directories on disk in the specified file format, which encode data about 
	 * a track or tracks (according to the file type). This is the same 
	 * data-structure as that returned by the <code>createOutputFileNames</code> 
	 * method and hence this may be passed unmodified to read back in the data 
	 * after a process has been run to generate data files at the specified 
	 * paths.
	 * 
	 * @param filesOrDirectoriesToRead Map of NemaTrackList to a list of File 
	 * Objects representing the files to be read in.
	 * @param task The task that the data relates (only required for 
	 * classification file types).
	 * @param fileType The file type to use to read the files or directories.
	 * @return A Map of NemaTrackList to a List of NemaData Objects encoding the 
	 * data read from each file or directory.
	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
	 * NemaFileType is received.
	 * @throws InstantiationException Thrown if the file reader can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found.
	 * @throws IOException Thrown if there is a problem reading a file.
	 */
	public static Map<NemaTrackList,List<NemaData>> readProcessOutput(
			Map<NemaTrackList,List<File>> filesOrDirectoriesToRead, 
			NemaTask task, 
			Class<? extends NemaFileType> fileType) 
			throws IllegalArgumentException, InstantiationException, 
				IllegalAccessException, FileNotFoundException, IOException{
		Map<NemaTrackList,List<NemaData>> out = new HashMap<NemaTrackList,List<NemaData>>(filesOrDirectoriesToRead.size());
		for (Iterator<NemaTrackList> iterator = filesOrDirectoriesToRead.keySet().iterator(); iterator.hasNext();) {
			NemaTrackList testSet = iterator.next();
			List<File> files = filesOrDirectoriesToRead.get(testSet);
			//read all files relevant to the fold and merge into single list for fold
			List<NemaData> dataRead = new ArrayList<NemaData>();
			for (Iterator<File> fileIt = files.iterator(); fileIt.hasNext();) {
				File file = fileIt.next();
				List<NemaData> data = readData(file, task, fileType);
				dataRead.addAll(data);
			}
			out.put(testSet, dataRead);
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
	 * NemaFileType is received.
	 * @throws InstantiationException Thrown if the file reader can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if the file or directory cannot be 
	 * found.
	 * @throws IOException Thrown if there is a problem reading the file.
	 */
	public static List<NemaData> readData(
			File fileOrDirectoryToRead, 
			NemaTask task, 
			Class<? extends NemaFileType> fileType
			) throws IllegalArgumentException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException{
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
	 * NemaFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 * @throws IOException Thrown if there is a problem writing the files to
	 * disk.
	 */
	public static File writeGroundTruthDataFileOrDirectory(
			NemaTrackList fold, 
			List<NemaData> data, 
			NemaTask task, 
			Class<? extends NemaFileType> fileType, 
			File outputDirectory
			) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
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
	 * NemaFileType is received.
	 * @throws InstantiationException Thrown if the file writer can't be 
	 * instantiated (for example if there is no zero-arg constructor).
	 * @throws IllegalAccessException Thrown if we do not have access to the 
	 * definition of the specified file type class.
	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
	 * found or created.
	 */
	public static Map<NemaTrackList,List<File>> getResourceFilesList(
			Map<NemaTrackList,List<NemaData>> testData, 
			NemaTask task, 
			File outputDirectory
			) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
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
	
//		
//	/**
//	 * Writes out a list of test files (one per fold of the experiment) that 
//	 * encodes paths for more than one track . 
//	 * 
//	 * @param testData A Map of NemaTrackList to a List of NemaData Objects 
//	 * encoding the data to write to each file or directory.
//	 * @param task The task that the data relates (only required for 
//	 * classification file types).
//	 * @param fileType The file type to use to write the list files.
//	 * @param outputDirectory The directory to write the data files to.
//	 * @return 
//	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
//	 * NemaFileType is received.
//	 * @throws InstantiationException Thrown if the file writer can't be 
//	 * instantiated (for example if there is no zero-arg constructor).
//	 * @throws IllegalAccessException Thrown if we do not have access to the 
//	 * definition of the specified file type class.
//	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
//	 * found or created.
//	 * @throws IOException Thrown if there is a problem writing the files to
//	 * disk.
//	 */
//	public static Map<NemaTrackList,File> writeTestListFiles(
//			Map<NemaTrackList,List<NemaData>> testData, 
//			NemaTask task, 
//			Class<? extends MultipleTrackEvalFileType> fileType, 
//			File outputDirectory
//			) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
//		Map<NemaTrackList,File> out = new HashMap<NemaTrackList,File>();
//		
//		for (Iterator<NemaTrackList> iterator = testData.keySet().iterator(); iterator.hasNext();) {
//			NemaTrackList testSet = iterator.next();
//			List<NemaData> data = testData.get(testSet);
//			File written = writeTestListFile(testSet,data,task,fileType,outputDirectory);
//			out.put(testSet,written);
//		}
//	
//		return out;
//	}
//	
//	/**
//	 * Writes out a single list
//	 * @param testData a List of NemaData Objects 
//	 * encoding the data to write to the file or directory.
//	 * @param task The task that the data relates (only required for 
//	 * classification file types).
//	 * @param fileType The file type to use to write the list files.
//	 * @param outputDirectory The directory to write the data files to.
//	 * @return 
//	 * @throws IllegalArgumentException Thrown if an unknown sub-interface of 
//	 * NemaFileType is received.
//	 * @throws InstantiationException Thrown if the file writer can't be 
//	 * instantiated (for example if there is no zero-arg constructor).
//	 * @throws IllegalAccessException Thrown if we do not have access to the 
//	 * definition of the specified file type class.
//	 * @throws FileNotFoundException Thrown if a file or directory cannot be 
//	 * found or created.
//	 * @throws IOException Thrown if there is a problem writing the files to
//	 * disk.
//	 */
//	public static File writeTestListFile(
//			NemaTrackList testSet, 
//			List<NemaData> testData, 
//			NemaTask task, 
//			Class<? extends MultipleTrackEvalFileType> fileType, 
//			File outputDirectory
//			) throws IllegalArgumentException, FileNotFoundException, IOException, InstantiationException, IllegalAccessException{
//		if(MultipleTrackEvalFileType.class.isAssignableFrom(fileType)) {
//			MultipleTrackEvalFileType typeInstance = (MultipleTrackEvalFileType)fileType.newInstance();
//			//set type for classification evaluator
//			if (ClassificationTextFile.class.equals(fileType)) {
//				((ClassificationTextFile)typeInstance).setMetadataType(task.getSubjectTrackMetadataName());
//			}
//			
//			//mint some sort of unique file name
//			File toWriteTo = new File(outputDirectory.getAbsolutePath() + File.separator + "test-set-" + testSet.getId());
//			typeInstance.writeFile(toWriteTo, testData);
//			return toWriteTo;
//		}else {
//			throw new IllegalArgumentException("Unrecognized file type for test data files: " + fileType.getName());
//		}
//		
//	}
//
//		
	
	
}
