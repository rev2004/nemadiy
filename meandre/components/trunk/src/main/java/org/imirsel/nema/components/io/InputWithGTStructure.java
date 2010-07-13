package org.imirsel.nema.components.io;

//import jAudioFeatureExtractor.ACE.DataTypes.SegmentedClassification;
import jAudioFeatureExtractor.ACE.DataTypes.SegmentedClassification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.components.util.FileCopy;
import org.imirsel.nema.components.util.FileDownload;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @author mertbay
 * 
 * @deprecated Use TaskSelector or TrainTestTaskSelector
 */
@Component(creator = "Mert Bay", description = "Input component that outputs both the audio and ground-truth file locations.", name = "Structure Input", tags = "input, file, URL,file download, CSV reader, XML reader",
		 firingPolicy = Component.FiringPolicy.all)
public class InputWithGTStructure extends NemaComponent {
	
	@ComponentOutput(description = "String[] that holds the fileLocations for the audio", name = "Audio Files")
	public final static String DATA_OUTPUT_1 = "Audio Files";
	
	@ComponentOutput(description = "String[] that holds the fileLocations for the audio", name = "Groundtruth Files")
	public final static String DATA_OUTPUT_2 = "Groundtruth Files";
	
	
	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "", description = "A file with a list of audio files with  \"file URL\" and  class metadata, indicating the "
			+ "location of the corresponding audio file and also its class", name = "File list URL")
	final static String DATA_PROPERTY_1 = "File list URL";

	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "http://nema.lis.uiuc.edu/example_wavs/ctpt03.wav", description = "A URL or local path of an individual file instead of the file list. If a URL is entered, the file will be downloaded. If this property is filled, the input file list will be ignored. ", name = "Single file URL")
	final static String DATA_PROPERTY_2 = "Single file URL";


	
	@StringDataType(labelList = { "A Hard Day's Night", "Drive My Car", "taxman"}, 
			valueList = {"/data/raid3/collections/segmentation/wavs/01__a_hard_days_night.wav,/data/raid3/collections/segmentation/groundtruth/01__a_hard_days_night.txt","/data/raid3/collections/segmentation/wavs/01__drive_my_car.wav,/data/raid3/collections/segmentation/groundtruth/01__drive_my_car.txt","/data/raid3/collections/segmentation/wavs/01__taxman.wav,/data/raid3/collections/segmentation/groundtruth/01__taxman.txt"})//,
			//"/data/raid3/collections/segmentation/smallwavlist.csv,/data/raid3/collections/segmentation/smallgtlist.csv"}
	@ComponentProperty(defaultValue = "/data/raid3/collections/segmentation/wavs/01__a_hard_days_night.wav,/data/raid3/collections/segmentation/groundtruth/01__a_hard_days_night.txt", description = "Select a file from the dataset. If this is chosen, the FileListURL or the SingleFileURL  properties will be ignored. ", name = "Select a file")
	final static String DATA_PROPERTY_3 = "Select a file";
	
	
	@StringDataType(hide=true)
	@ComponentProperty(defaultValue = "", description = "Upload a file. If this is chosen, the FileListURL, the SingleFileURL  or the selectFile properties will be ignored. ", name = "Upload a file")
	final static String DATA_PROPERTY_4 = "Upload a file";	

	

	
	
	
	private final String FileListURL = "";
	private final String SingleFileURL = "";
	private final String uploadFile = "";	
	private String selectFile = "/data/raid3/collections/segmentation/wavs/01__a_hard_days_night.wav,/data/raid3/collections/segmentation/groundtruth/01__a_hard_days_night.txt";	
	
	private String localListFilePath;

	private String processWorkingDir;
	private String commonStorageDir;
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory())
					.getProcessWorkingDirectory(
							ccp.getFlowExecutionInstanceID());
			commonStorageDir=ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory()).getCommonStorageLocation() + File.pathSeparator + "inputSelectorDownloadedFiles";
		} catch (IOException e1) {
			throw new ComponentContextException("IOException in " + this.getClass().getName(),e1); 
		}
	}

	public void dispose(ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
	}

	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {
		//FileListURL = String.valueOf(ccp.getProperty(DATA_PROPERTY_1));
		//SingleFileURL = String.valueOf(ccp.getProperty(DATA_PROPERTY_2));
		selectFile = String.valueOf(ccp.getProperty(DATA_PROPERTY_3));
		//uploadFile = String.valueOf(ccp.getProperty(DATA_PROPERTY_4));

		
		if (!uploadFile.contentEquals("")) {
			String[] inputFiles = new String[1];
			getLogger().info("File upload is selected");
			if (uploadFile.contains("http")
					|| uploadFile.contains("ftp")) {
				 //String workingDirName = processWorkingDir;
				String workingDirName = commonStorageDir;
				
				inputFiles[0] = downloadFiles(uploadFile, workingDirName);
			} else {
				inputFiles[0] = uploadFile;
			}
			//inputFiles[0][1] = "";
			getLogger().info("no 1:\tFileName="
					+ uploadFile.subSequence(uploadFile
							.lastIndexOf("/") + 1, uploadFile.length()));
				//	+ "\t\tClassName= " + "\t\t added to output");
			
			ccp.pushDataComponentToOutput(DATA_OUTPUT_1, inputFiles);
		} 
		
		else if (!selectFile.contentEquals("")) {
			String[] inputFiles = new String[1];
			String[] gtFiles = new String[1];
			
			getLogger().info("selectFile file is selected");
			if (selectFile.contains("http")
					|| selectFile.contains("ftp")) {
				 //String workingDirName = processWorkingDir;
				String workingDirName = commonStorageDir;
				inputFiles[0] = downloadFiles(selectFile, workingDirName);
			} else {
				String dstname = commonStorageDir + File.separator +  selectFile.substring(selectFile.lastIndexOf("/")+1, selectFile.length());
					
				String[] file_pair = selectFile.split(",");
					inputFiles[0] = file_pair[0];
					gtFiles[0] = file_pair[1];
				
			}
		//	inputFiles[0][1] = "";
			getLogger().info("no 1:\tFileName="
					+ selectFile.subSequence(selectFile
							.lastIndexOf("/") + 1, selectFile.length()));
			//		+ "\t\tClassName= " + "\t\t added to output");
			System.out.println("wavfile " + inputFiles[0] + " and gt file " + gtFiles[0] +  "pushed") ;
			ccp.pushDataComponentToOutput(DATA_OUTPUT_1, inputFiles);		
			ccp.pushDataComponentToOutput(DATA_OUTPUT_2, gtFiles);		
			
		}
		else if (!SingleFileURL.contentEquals("")) {
			String[] inputFiles = new String[1];
			getLogger().info("Individual file is selected");
			if (SingleFileURL.contains("http")
					|| SingleFileURL.contains("ftp")) {
				 //String workingDirName = processWorkingDir;
				String workingDirName = commonStorageDir;
				inputFiles[0] = downloadFiles(SingleFileURL, workingDirName);
			} else {
				String dstname = commonStorageDir + File.separator + selectFile.substring(SingleFileURL.lastIndexOf("/")+1, SingleFileURL.length());
				try {
					String copiedFile = FileCopy.copy(SingleFileURL, dstname);
					inputFiles[0] = copiedFile;
				} catch (IOException e) {
					throw new ComponentExecutionException("IOException in " + this.getClass().getName(),e);
				}
	
			}
		//	inputFiles[0][1] = "";
			getLogger().info("no 1:\tFileName="
					+ SingleFileURL.subSequence(SingleFileURL
							.lastIndexOf("/") + 1, SingleFileURL.length()));
			//		+ "\t\tClassName= " + "\t\t added to output");
			
			ccp.pushDataComponentToOutput(DATA_OUTPUT_1, inputFiles);			
		}
		else {
			BufferedReader textBuffer;
			int noLines = 0;
			try {
				// Check if it is a URL
				if (FileListURL.contains("http")
						|| FileListURL.contains("ftp")) {
					// localListFilePath = ccp.getPublicResourcesDirectory()+
					// "/inputSelectorDownloadedFiles/"+FileListURL.substring(FileListURL.lastIndexOf('/')+1,
					// FileListURL.length());
					
					
					String workingDirName = commonStorageDir;
					localListFilePath = downloadFiles(FileListURL,
							workingDirName);
					textBuffer = new BufferedReader(new FileReader(
							localListFilePath));
					noLines = org.imirsel.nema.components.util.LineCounter
							.count(textBuffer) - 1;
					textBuffer.close();
					textBuffer = new BufferedReader(new FileReader(
							localListFilePath));
				} else {
					textBuffer = new BufferedReader(
							new FileReader(FileListURL));
					noLines = org.imirsel.nema.components.util.LineCounter
							.count(textBuffer) - 1;
					textBuffer.close();
					textBuffer = new BufferedReader(
							new FileReader(FileListURL));
				}
				// If it is a CSV file
				if (FileListURL.substring(FileListURL.length() - 3,
						FileListURL.length()).equalsIgnoreCase("csv")) {
					getLogger().info("The collection format is "
							+ FileListURL.substring(FileListURL.length() - 3,
									FileListURL.length()));
					getLogger().info("There are " + noLines
							+ " records in the collection");
					// Read the header info from the file
					String Header = textBuffer.readLine();
					String[] inputFiles = new String[noLines];
					for (int i = 0; i < noLines; i++) {
						String line = textBuffer.readLine();
						StringTokenizer str = new StringTokenizer(line, ",");
						String fileLocation;
						String classname;
						if (str.countTokens() == 1) {
							fileLocation = str.nextToken();
							classname = "";
							getLogger().info("There is no class label");
						} else if (str.countTokens() == 2) {
							fileLocation = str.nextToken();
							classname = str.nextToken();
						} else {
							getLogger().info("Mistake in Record no " + i
									+ ". Continuing...");
							continue;
						}
						if (fileLocation.contains("http")
								|| fileLocation.contains("ftp")) {
							String workingDirName = commonStorageDir;
							inputFiles[i] = downloadFiles(fileLocation,
									workingDirName);
						} else {
							inputFiles[i] = fileLocation;
						}
						getLogger().info("no "
								+ (i + 1)
								+ ":\tFileName="
								+ fileLocation.subSequence(fileLocation
										.lastIndexOf("/") + 1, fileLocation
										.length()) + "\t\tClassName="
								+ classname + "\t\t added to output");
					//	inputFiles[i][1] = classname;
					}
					ccp.pushDataComponentToOutput(DATA_OUTPUT_1, inputFiles);
				}
				// If it is a XML file
				else if (FileListURL.substring(FileListURL.length() - 3,
						FileListURL.length()).equalsIgnoreCase("xml")) {
					// To be implemented
					SegmentedClassification[] inputXML = SegmentedClassification
							.parseClassificationsFile(localListFilePath);
					getLogger().info("The collection format is xml");
					getLogger().info("There are " + inputXML.length
							+ " records in the collection.");
					String[] inputFiles = new String[inputXML.length];
					for (int i = 0; i < inputXML.length; i++) {
						String[] classes = inputXML[i].classifications;
						String fileLocation = inputXML[i].identifier;
						String classname;
						if (classes != null) {
							classname = classes[0];
						} else {
							classname = "";
							getLogger().info("There is no class label");
						}
						if (fileLocation.contains("http")
								|| fileLocation.contains("ftp")) {
							String workingDirName = commonStorageDir;
							inputFiles[i] = downloadFiles(fileLocation,
									workingDirName);
						} else {
							inputFiles[i] = fileLocation;
						}
					//	inputFiles[i][0] = dir_prefix+ inputFiles[i][0].subSequence(inputFiles[i][0].lastIndexOf("/") + 1, inputFiles[i][0]						.length());
						getLogger().info("no "
								+ (i + 1)
								+ ":\tFileName="
								+ inputFiles[i].subSequence(inputFiles[i]
										.lastIndexOf("/") + 1, inputFiles[i].length()) + "\t\tClassName="
								+ classname + "\t\t  added to output.");
						getLogger().info("Debugging no "
								+ (i + 1)
								+ ":\tFileName="
								+ inputFiles[i] + "\t\tClassName="
								+ classname + "\t\t  added to output.");
						
					}
					ccp.pushDataComponentToOutput(DATA_OUTPUT_1, inputFiles);
				} else {
					getLogger().info("Unsupported input file format "
							+ FileListURL.substring(FileListURL.length() - 3,
									FileListURL.length()));
				}
			} catch (Exception e) {
				throw new ComponentExecutionException(e.getClass().getSimpleName() + " in " + this.getClass().getName(),e);
			}
		}
	}

	public String downloadFiles(String fileLocation, String workingDirName) {

		getLogger().info("\nDownloading file from " + fileLocation);

		File workingDir = new File(workingDirName);
		if (!workingDir.exists()) {
			boolean done = workingDir.mkdirs();
			if (!done) {
				throw new RuntimeException(
						"Could not create the output directory");
			}
		} else if (!workingDir.isDirectory()) {
			throw new RuntimeException(workingDir + " No such Parent directory");
		}
		String localFileName = workingDirName
				+ File.pathSeparator
				+ fileLocation.substring(fileLocation.indexOf("//")+2,fileLocation.length()).replaceAll("/", "_");
		
		File localFile = new File(localFileName);
		if (localFile.exists()) {
			getLogger().info("The file " + fileLocation
					+ " alredy exists locally, avoiding re-download.");
		} else {
			FileDownload.download(fileLocation, localFileName);
			getLogger().info("File downloaded to local path." );
		}
		return localFileName;
	}

}
